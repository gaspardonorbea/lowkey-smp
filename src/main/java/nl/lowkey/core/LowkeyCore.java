package nl.lowkey.core;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * LowkeySMP visuals:
 * - CREW badge for configured players (chat, tab list, nametag)
 * - join / leave messages with icons
 * - elimination message with LOWKEY badge, big title and blood particles
 *
 * All glyphs live in the "lowkey:tags" font from the resource pack.
 */
public final class LowkeyCore extends JavaPlugin implements Listener {

    private static final Key FONT = Key.key("lowkey", "tags");

    // private-use characters, mapped to textures in assets/lowkey/font/tags.json
    private static final String CREW_BADGE = "\uE000";
    private static final String LOWKEY_BADGE = "\uE001";
    private static final String JOIN_ICON = "\uE002";
    private static final String LEAVE_ICON = "\uE003";
    private static final String TITLE_GLYPH = "\uE004";

    private static final String CREW_TEAM = "lowkey_crew";

    private final Set<String> crew = new HashSet<>();
    private final Map<UUID, Location> deathSpots = new HashMap<>();

    private boolean eliminateOnDeath;
    private String eliminatedSuffix;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        getServer().getPluginManager().registerEvents(this, this);
        for (Player player : getServer().getOnlinePlayers()) {
            applyCrew(player);
        }
    }

    private void loadSettings() {
        crew.clear();
        for (String name : getConfig().getStringList("crew")) {
            crew.add(name.toLowerCase(Locale.ROOT));
        }
        eliminateOnDeath = getConfig().getBoolean("eliminate-on-death", true);
        eliminatedSuffix = getConfig().getString("eliminated-suffix", " is uitgeschakeld!");
    }

    // ------------------------------------------------------------------ helpers

    private static Component glyph(String character) {
        return Component.text(character).font(FONT).color(NamedTextColor.WHITE);
    }

    private boolean isCrew(Player player) {
        return crew.contains(player.getName().toLowerCase(Locale.ROOT));
    }

    private void applyCrew(Player player) {
        if (!isCrew(player)) {
            return;
        }
        // NB: appending to the glyph itself would make the space/name inherit the "lowkey:tags" font
        // (that showed up as empty squares). Build the pieces as siblings instead.
        Component badge = Component.text()
                .append(glyph(CREW_BADGE))
                .append(Component.space())
                .build();

        // nametag above the head
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(CREW_TEAM);
        if (team == null) {
            team = board.registerNewTeam(CREW_TEAM);
        }
        team.prefix(badge);
        team.addEntry(player.getName());

        // tab list
        player.playerListName(Component.text()
                .append(badge)
                .append(Component.text(player.getName(), NamedTextColor.WHITE))
                .build());
    }

    // ------------------------------------------------------------------ join / leave

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component.text()
                .append(glyph(JOIN_ICON))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.GREEN))
                .build());
        applyCrew(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component.text()
                .append(glyph(LEAVE_ICON))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .build());
    }

    // ------------------------------------------------------------------ chat

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        final boolean isCrew = isCrew(event.getPlayer());
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> {
            Component line = Component.empty();
            if (isCrew) {
                line = line.append(glyph(CREW_BADGE)).append(Component.space());
            }
            return line.append(sourceDisplayName)
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(message);
        }));
    }

    // ------------------------------------------------------------------ elimination

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        // hide the vanilla death message, show ours
        event.deathMessage(null);
        getServer().sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.WHITE))
                .append(Component.text(eliminatedSuffix, NamedTextColor.RED))
                .build());

        Location spot = player.getLocation().clone();
        bloodBurst(spot.clone().add(0, 1, 0));

        if (eliminateOnDeath) {
            deathSpots.put(player.getUniqueId(), spot);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location spot = deathSpots.remove(player.getUniqueId());
        if (spot == null) {
            return;
        }

        // respawn where you died (not too deep in the void), then switch to spectator
        World world = spot.getWorld();
        if (world != null && spot.getY() < world.getMinHeight() + 10) {
            spot.setY(world.getMinHeight() + 10);
        }
        event.setRespawnLocation(spot);

        getServer().getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline()) {
                return;
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.showTitle(Title.title(
                    glyph(TITLE_GLYPH),
                    Component.empty(),
                    Title.Times.times(Duration.ofMillis(150), Duration.ofMillis(3000), Duration.ofMillis(1000))));
        }, 2L);
    }

    private void bloodBurst(Location location) {
        final World world = location.getWorld();
        if (world == null) {
            return;
        }
        final BlockData redstone = Material.REDSTONE_BLOCK.createBlockData();
        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(150, 8, 20), 1.8f);

        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.6f, 1.2f);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                world.spawnParticle(Particle.BLOCK, location, 40, 0.35, 0.6, 0.35, 0.2, redstone);
                world.spawnParticle(Particle.DUST, location, 30, 0.45, 0.7, 0.45, 0.0, dust);
                if (++ticks >= 6) {
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 2L);
    }
}

