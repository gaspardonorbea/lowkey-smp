package nl.lowkey.core;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LowkeySMP visuals and rules:
 * - tab list: logo on top, "Online: N" at the bottom, Noord (red) above Zuid (blue), icons before every name
 * - nametag icons: CREW, grace (yellow) and team (Noord / Zuid)
 * - join / leave messages with icons
 * - grace period: the first hour of playtime per player you simply respawn
 * - after that: elimination message, big title, blood, straight to spectator
 *
 * All glyphs live in the "lowkey:tags" font from the resource pack.
 */
public final class LowkeyCore extends JavaPlugin implements Listener {

    private static final Key FONT = Key.key("lowkey", "tags");

    // private-use characters, mapped to textures in assets/lowkey/font/tags.json
    private static final String CREW_ICON = "\uE000";
    private static final String LOWKEY_BADGE = "\uE001";
    private static final String JOIN_ICON = "\uE002";
    private static final String LEAVE_ICON = "\uE003";
    private static final String GRACE_ICON = "\uE005";
    private static final String NOORD_ICON = "\uE006";
    private static final String ZUID_ICON = "\uE007";

    /** -1 px space: pieces of a wide picture are joined with this so there is no seam */
    private static final String SPACER = "\uE010";
    /** +1 px space: the small gap between two icons (a normal space is 4 px, too wide) */
    private static final String THIN_GAP = "\uE011";
    /** the tab list logo (2 pieces) and the big UITGESCHAKELD title (3 pieces) */
    private static final String LOGO = "\uE020" + SPACER + "\uE021";
    private static final String TITLE_GLYPH = "\uE030" + SPACER + "\uE031" + SPACER + "\uE032";

    /** which side (team) a player is on */
    private enum Side {
        NOORD("noord", NOORD_ICON, 0xFF5555, '1'),
        ZUID("zuid", ZUID_ICON, 0x55AAFF, '2'),
        NONE("geen", null, 0xFFFFFF, '3');

        final String id;
        final String icon;
        final int rgb;
        /** decides the tab list order: Noord first, then Zuid, then everybody else */
        final char sortKey;

        Side(String id, String icon, int rgb, char sortKey) {
            this.id = id;
            this.icon = icon;
            this.rgb = rgb;
            this.sortKey = sortKey;
        }

        static Side fromId(String id) {
            if (id == null) {
                return NONE;
            }
            for (Side side : values()) {
                if (side.id.equalsIgnoreCase(id)) {
                    return side;
                }
            }
            return null;
        }
    }

    private final Set<String> crew = new HashSet<>();

    /** grace time left in ms, as of the timestamp in graceLast (only for online players) */
    private final Map<UUID, Long> graceLeft = new HashMap<>();
    private final Map<UUID, Long> graceLast = new HashMap<>();

    /** side of every online player, readable from the async chat thread */
    private final Map<UUID, Side> sideCache = new ConcurrentHashMap<>();

    /** name of the scoreboard team each online player is currently in */
    private final Map<UUID, String> teamNames = new HashMap<>();

    /** server closed for maintenance? Only the names in "closed-access" can join. Saved in state.yml. */
    private volatile boolean serverClosed;
    private volatile Set<String> closedAccess = Set.of();

    /** is the Nether open? saved in state.yml so it survives restarts */
    private boolean netherOpen;
    private File stateFile;
    /** stops the "Nether is closed" message from spamming while a player stands in a portal */
    private final Map<UUID, Long> netherNotice = new HashMap<>();

    private NamespacedKey graceKey;
    private NamespacedKey sideKey;
    private long graceMillis;
    private boolean eliminateOnDeath;
    private String eliminatedSuffix;

    // ------------------------------------------------------------------ lifecycle

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        stateFile = new File(getDataFolder(), "state.yml");
        YamlConfiguration state = YamlConfiguration.loadConfiguration(stateFile);
        netherOpen = state.getBoolean("nether-open", false);
        serverClosed = state.getBoolean("server-closed", false);
        graceKey = new NamespacedKey(this, "grace_left");
        sideKey = new NamespacedKey(this, "side");

        cleanupTeams();
        getServer().getPluginManager().registerEvents(this, this);

        for (Player player : getServer().getOnlinePlayers()) {
            startTracking(player, false);
        }
        getServer().getScheduler().runTaskTimer(this, this::tickGrace, 20L, 20L);
        getServer().getScheduler().runTaskTimer(this, this::updateTablist, 20L, 100L);
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            persist(player);
        }
    }

    private void loadSettings() {
        crew.clear();
        for (String name : getConfig().getStringList("crew")) {
            crew.add(name.toLowerCase(Locale.ROOT));
        }
        Set<String> access = new HashSet<>();
        for (String name : getConfig().getStringList("closed-access")) {
            access.add(name.toLowerCase(Locale.ROOT));
        }
        closedAccess = access;
        graceMillis = Math.max(0L, getConfig().getLong("grace-minutes", 60L)) * 60_000L;
        eliminateOnDeath = getConfig().getBoolean("eliminate-on-death", true);
        eliminatedSuffix = getConfig().getString("eliminated-suffix", " is uitgeschakeld!");
    }

    // ------------------------------------------------------------------ helpers

    private static Component glyph(String characters) {
        return Component.text(characters).font(FONT).color(NamedTextColor.WHITE);
    }

    private boolean isCrew(Player player) {
        return crew.contains(player.getName().toLowerCase(Locale.ROOT));
    }

    // ------------------------------------------------------------------ side (Noord / Zuid)

    private Side getSide(Player player) {
        String raw = player.getPersistentDataContainer().get(sideKey, PersistentDataType.STRING);
        Side side = Side.fromId(raw);
        return side == null ? Side.NONE : side;
    }

    private void setSide(Player player, Side side) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (side == Side.NONE) {
            data.remove(sideKey);
        } else {
            data.set(sideKey, PersistentDataType.STRING, side.id);
        }
        refreshTags(player);
    }

    // ------------------------------------------------------------------ grace period

    /** Start (or resume) grace tracking for a player who just joined. */
    private void startTracking(Player player, boolean announce) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        Long stored = data.get(graceKey, PersistentDataType.LONG);

        long left;
        if (stored == null) {
            left = graceMillis; // first time on the server: fresh grace
            data.set(graceKey, PersistentDataType.LONG, left);
        } else {
            left = stored;
        }

        UUID id = player.getUniqueId();
        graceLeft.put(id, left);
        graceLast.put(id, System.currentTimeMillis());
        refreshTags(player);

        if (announce && left > 0) {
            // small delay so the resource pack is (most likely) active and the glyphs render
            getServer().getScheduler().runTaskLater(this, () -> {
                long remaining = graceRemaining(player);
                if (player.isOnline() && remaining > 0) {
                    player.sendMessage(graceMessage(remaining));
                }
            }, 60L);
        }
    }

    private long graceRemaining(Player player) {
        return graceRemaining(player.getUniqueId());
    }

    private long graceRemaining(UUID id) {
        Long left = graceLeft.get(id);
        if (left == null || left <= 0L) {
            return 0L;
        }
        long now = System.currentTimeMillis();
        long last = graceLast.getOrDefault(id, now);
        return Math.max(0L, left - (now - last));
    }

    private void persist(Player player) {
        if (graceLeft.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(graceKey, PersistentDataType.LONG, graceRemaining(player));
        }
    }

    /** Runs every second: counts down grace time, ends it when it hits zero. */
    private void tickGrace() {
        long now = System.currentTimeMillis();
        for (Player player : getServer().getOnlinePlayers()) {
            UUID id = player.getUniqueId();
            Long left = graceLeft.get(id);
            if (left == null || left <= 0L) {
                continue;
            }
            long remaining = graceRemaining(id);
            graceLeft.put(id, remaining);
            graceLast.put(id, now);
            player.getPersistentDataContainer().set(graceKey, PersistentDataType.LONG, remaining);

            if (remaining <= 0L) {
                refreshTags(player); // yellow icon disappears
                player.sendMessage(graceEndedMessage());
            }
        }
    }

    private void setGrace(Player player, long millis) {
        UUID id = player.getUniqueId();
        graceLeft.put(id, millis);
        graceLast.put(id, System.currentTimeMillis());
        player.getPersistentDataContainer().set(graceKey, PersistentDataType.LONG, millis);
        refreshTags(player);
        if (millis > 0L) {
            player.sendMessage(graceMessage(millis));
        }
    }

    private Component graceMessage(long remainingMillis) {
        long minutes = Math.max(1L, (remainingMillis + 59_999L) / 60_000L);
        String unit = minutes == 1L ? "minuut" : "minuten";
        return Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je kunt voor de komende ", NamedTextColor.RED))
                .append(Component.text(minutes + " " + unit, NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(" respawnen!", NamedTextColor.RED))
                .build();
    }

    /** Sent to one player, at the moment that player's own grace time runs out. */
    private Component graceEndedMessage() {
        return Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je kunt ", NamedTextColor.RED))
                .append(Component.text("niet", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(" meer respawnen!", NamedTextColor.RED))
                .build();
    }

    // ------------------------------------------------------------------ nametag + tab list entry

    /** Scoreboard team name: "lk" + side digit + player name. The tab list sorts on this name. */
    private static String teamNameFor(Player player, Side side) {
        String name = "lk" + side.sortKey + player.getName().toLowerCase(Locale.ROOT);
        return name.length() > 16 ? name.substring(0, 16) : name;
    }

    /**
     * Rebuilds the icons in front of the name (CREW, grace, Noord/Zuid), the nametag prefix, the
     * tab list entry (icons + name in the team colour) and the tab list position.
     * Every icon is a sibling component, because children of a glyph would inherit the
     * "lowkey:tags" font and turn into empty squares.
     */
    private void refreshTags(Player player) {
        UUID id = player.getUniqueId();
        Side side = getSide(player);

        sideCache.put(id, side);

        List<String> parts = new ArrayList<>();
        if (isCrew(player)) {
            parts.add(CREW_ICON);
        }
        if (graceRemaining(player) > 0L) {
            parts.add(GRACE_ICON);
        }
        if (side.icon != null) {
            parts.add(side.icon);
        }
        // small gap between the icons, a normal space between the last icon and the name
        TextComponent.Builder prefix = Component.text();
        for (int i = 0; i < parts.size(); i++) {
            prefix.append(glyph(parts.get(i)));
            prefix.append(i < parts.size() - 1 ? glyph(THIN_GAP) : Component.space());
        }
        Component icons = prefix.build();

        // one small scoreboard team per player: gives the nametag prefix and the tab list order
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        String wanted = teamNameFor(player, side);
        String old = teamNames.get(id);
        if (old != null && !old.equals(wanted)) {
            Team oldTeam = board.getTeam(old);
            if (oldTeam != null) {
                oldTeam.unregister();
            }
        }
        Team team = board.getTeam(wanted);
        if (team != null && !team.hasEntry(player.getName()) && !team.getEntries().isEmpty()) {
            // two long names that start the same: fall back to a name based on the UUID
            wanted = "lk" + side.sortKey + id.toString().substring(0, 8);
            team = board.getTeam(wanted);
        }
        if (team == null) {
            team = board.registerNewTeam(wanted);
        }
        team.prefix(icons);
        team.addEntry(player.getName());
        teamNames.put(id, wanted);

        // tab list entry: icons + name in the colour of the team
        TextColor nameColor = side == Side.NONE ? NamedTextColor.WHITE : TextColor.color(side.rgb);
        player.playerListName(Component.text()
                .append(icons)
                .append(Component.text(player.getName(), nameColor))
                .build());
    }

    private void removeTeam(Player player) {
        String name = teamNames.remove(player.getUniqueId());
        if (name == null) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
        if (team != null) {
            team.unregister();
        }
    }

    /** Removes leftover teams from earlier runs / older versions of this plugin. */
    private void cleanupTeams() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : new ArrayList<>(board.getTeams())) {
            String name = team.getName();
            if (name.startsWith("lk") || name.equals("lowkey_crew")) {
                team.unregister();
            }
        }
    }

    // ------------------------------------------------------------------ tab list header + footer

    /** The logo, followed by blank lines so the list starts below it (logo is 36 px = 4 lines). */
    private Component tabHeader() {
        return Component.text()
                .append(glyph(LOGO))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(Component.text(" "))
                .build();
    }

    private Component tabFooter() {
        return Component.text()
                .append(Component.text("Online: ", NamedTextColor.GRAY))
                .append(Component.text(String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE))
                .build();
    }

    private void updateTablist() {
        Component header = tabHeader();
        Component footer = tabFooter();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendPlayerListHeaderAndFooter(header, footer);
        }
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
        startTracking(player, true);
        getServer().getScheduler().runTask(this, this::updateTablist);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component.text()
                .append(glyph(LEAVE_ICON))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .build());

        persist(player);
        graceLeft.remove(player.getUniqueId());
        graceLast.remove(player.getUniqueId());
        netherNotice.remove(player.getUniqueId());
        sideCache.remove(player.getUniqueId());
        removeTeam(player);
        getServer().getScheduler().runTask(this, this::updateTablist); // count is right one tick later
    }

    // ------------------------------------------------------------------ chat

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        final boolean isCrew = isCrew(event.getPlayer());
        final Side side = sideCache.getOrDefault(event.getPlayer().getUniqueId(), Side.NONE);
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> {
            Component line = Component.empty();
            if (isCrew) {
                line = line.append(glyph(CREW_ICON)).append(Component.space());
            }
            // Noord = red name, Zuid = blue name
            Component name = side == Side.NONE ? sourceDisplayName : sourceDisplayName.color(TextColor.color(side.rgb));
            return line.append(name)
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(message);
        }));
    }

    // ------------------------------------------------------------------ death

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getPlayer();

        // never show the vanilla death message
        event.deathMessage(null);

        // GRACE: a completely normal death and respawn, only with our own message
        if (graceRemaining(player) > 0L) {
            getServer().sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text(player.getName(), NamedTextColor.WHITE))
                    .append(Component.text(" is doodgegaan.", NamedTextColor.RED))
                    .build());
            return;
        }

        // ELIMINATION
        final Location spot = player.getLocation().clone();
        final World world = spot.getWorld();

        getServer().sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.WHITE))
                .append(Component.text(eliminatedSuffix, NamedTextColor.RED))
                .build());

        bloodBurst(spot.clone().add(0, 1, 0));

        // no death screen: cancel the death and keep the player alive with full health
        event.setCancelled(true);
        event.setReviveHealth(20.0);

        if (eliminateOnDeath && world != null) {
            if (!event.getKeepInventory()) {
                for (ItemStack drop : event.getDrops()) {
                    if (drop != null && !drop.getType().isAir()) {
                        world.dropItemNaturally(spot, drop);
                    }
                }
                player.getInventory().clear();
            }
            final int xp = event.getDroppedExp();
            if (event.shouldDropExperience() && xp > 0) {
                world.spawn(spot, ExperienceOrb.class, orb -> orb.setExperience(xp));
            }
            player.setLevel(0);
            player.setExp(0f);
        }

        // finish on the next tick, after the server has processed the cancelled death
        getServer().getScheduler().runTask(this, () -> {
            if (!player.isOnline()) {
                return;
            }
            player.setFireTicks(0);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.setFoodLevel(20);

            if (eliminateOnDeath) {
                Location safe = spot.clone();
                World safeWorld = safe.getWorld();
                if (safeWorld != null && safe.getY() < safeWorld.getMinHeight() + 10) {
                    safe.setY(safeWorld.getMinHeight() + 10); // died in the void
                }
                player.teleport(safe);
                player.setGameMode(GameMode.SPECTATOR);
            }

            player.showTitle(Title.title(
                    glyph(TITLE_GLYPH),
                    Component.empty(),
                    Title.Times.times(Duration.ofMillis(150), Duration.ofMillis(3000), Duration.ofMillis(1000))));
        });
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

    // ------------------------------------------------------------------ server open / closed (maintenance)

    private boolean hasClosedAccess(String name) {
        return closedAccess.contains(name.toLowerCase(Locale.ROOT));
    }

    private Component closedMessage() {
        return Component.text("De server is tijdelijk gesloten. Kom later terug!", NamedTextColor.RED);
    }

    /**
     * Closing does NOT use the ban list: it only blocks logging in (except for the names in
     * "closed-access") and kicks everybody else who is online. Opening just lifts the block,
     * so real bans are never touched. The state is saved, so a restart keeps the server closed.
     */
    private void setServerClosed(boolean closed) {
        serverClosed = closed;
        saveState();
        if (closed) {
            for (Player player : new ArrayList<>(getServer().getOnlinePlayers())) {
                if (!hasClosedAccess(player.getName())) {
                    player.kick(closedMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (serverClosed && !hasClosedAccess(event.getName())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, closedMessage());
        }
    }

    // ------------------------------------------------------------------ Nether open / closed

    private void saveState() {
        YamlConfiguration state = new YamlConfiguration();
        state.set("nether-open", netherOpen);
        state.set("server-closed", serverClosed);
        try {
            getDataFolder().mkdirs();
            state.save(stateFile);
        } catch (IOException e) {
            getLogger().warning("Kon state.yml niet opslaan: " + e.getMessage());
        }
    }

    /** Opens or closes the Nether for everybody and tells all players. */
    private void setNether(boolean open) {
        netherOpen = open;
        saveState();

        Component chat = Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("De Nether is nu ", NamedTextColor.RED))
                .append(Component.text(open ? "geopend" : "gesloten", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text("!", NamedTextColor.RED))
                .build();
        Title title = Title.title(
                Component.text("De Nether is geopend!", NamedTextColor.RED),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(3000), Duration.ofMillis(1000)));

        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(chat);
            if (open) {
                player.showTitle(title);
                player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 0.6f, 1.0f);
            }
        }
    }

    /** While the Nether is closed, nobody can travel INTO it through a portal (leaving it is always allowed). */
    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if (netherOpen || event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            return; // already in the Nether: the way back is free
        }
        event.setCancelled(true);

        long now = System.currentTimeMillis();
        Long last = netherNotice.get(player.getUniqueId());
        if (last == null || now - last > 3000L) {
            netherNotice.put(player.getUniqueId(), now);
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("De Nether is nog ", NamedTextColor.RED))
                    .append(Component.text("gesloten", NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text("!", NamedTextColor.RED))
                    .build());
        }
    }

    // ------------------------------------------------------------------ admin command

    /**
     * /lowkey grace <speler> <minuten> : zet de resterende grace tijd (0 = grace beeindigen)
     * /lowkey team <speler> <noord|zuid|geen> : zet de speler in Noord, Zuid of geen team
     * /lowkey nether <open|close> : opent of sluit de Nether (bij openen: titel + bericht voor iedereen)
     * /lowkey server <open|close> : sluit de server voor iedereen behalve 'closed-access' (geen bans), of maakt hem weer open
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("lowkey")) {
            return false;
        }
        if (!sender.hasPermission("lowkey.admin")) {
            sender.sendMessage(Component.text("Je hebt hier geen toegang toe.", NamedTextColor.RED));
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("grace")) {
            Player target = getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
                return true;
            }
            long minutes;
            try {
                minutes = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Geef het aantal minuten als getal.", NamedTextColor.RED));
                return true;
            }
            minutes = Math.max(0L, minutes);
            setGrace(target, minutes * 60_000L);
            sender.sendMessage(Component.text(
                    "Grace van " + target.getName() + " staat nu op " + minutes + " minuten.", NamedTextColor.GREEN));
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("team")) {
            Player target = getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
                return true;
            }
            Side side = Side.fromId(args[2].toLowerCase(Locale.ROOT));
            if (side == null) {
                sender.sendMessage(Component.text("Kies noord, zuid of geen.", NamedTextColor.RED));
                return true;
            }
            setSide(target, side);
            sender.sendMessage(Component.text(
                    target.getName() + " zit nu in team " + side.id + ".", NamedTextColor.GREEN));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("server")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("close")) {
                if (sender instanceof Player) {
                    Player senderPlayer = (Player) sender;
                    if (!hasClosedAccess(senderPlayer.getName())) {
                        sender.sendMessage(Component.text(
                                "Je staat zelf niet in 'closed-access' in config.yml, dan zou je jezelf buitensluiten. "
                                        + "Voeg je naam eerst toe of sluit de server via de console.", NamedTextColor.RED));
                        return true;
                    }
                }
                setServerClosed(true);
                sender.sendMessage(Component.text(
                        "De server is nu gesloten. Alleen deze spelers kunnen joinen: "
                                + String.join(", ", getConfig().getStringList("closed-access")), NamedTextColor.GREEN));
                return true;
            }
            if (args.length == 2 && args[1].equalsIgnoreCase("open")) {
                setServerClosed(false);
                sender.sendMessage(Component.text("De server is weer open voor iedereen.", NamedTextColor.GREEN));
                return true;
            }
            sender.sendMessage(Component.text(
                    "De server is nu " + (serverClosed ? "gesloten" : "open") + ". Gebruik: /lowkey server <open|close>",
                    NamedTextColor.GRAY));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("nether")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("open")) {
                setNether(true);
                sender.sendMessage(Component.text("De Nether is nu open.", NamedTextColor.GREEN));
                return true;
            }
            if (args.length == 2 && args[1].equalsIgnoreCase("close")) {
                setNether(false);
                sender.sendMessage(Component.text("De Nether is nu dicht.", NamedTextColor.GREEN));
                return true;
            }
            sender.sendMessage(Component.text(
                    "De Nether is nu " + (netherOpen ? "open" : "dicht") + ". Gebruik: /lowkey nether <open|close>",
                    NamedTextColor.GRAY));
            return true;
        }
        sender.sendMessage(Component.text(
                "Gebruik: /lowkey grace <speler> <minuten>  |  /lowkey team <speler> <noord|zuid|geen>  |  /lowkey nether <open|close>  |  /lowkey server <open|close>",
                NamedTextColor.GRAY));
        return true;
    }

    // ------------------------------------------------------------------ tab completion

    /** Typing /lowkey (and pressing space / tab) suggests every possibility. */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("lowkey") || !sender.hasPermission("lowkey.admin")) {
            return List.of();
        }
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            options.add("grace");
            options.add("team");
            options.add("nether");
            options.add("server");
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("grace") || sub.equals("team")) {
                for (Player player : getServer().getOnlinePlayers()) {
                    options.add(player.getName());
                }
            } else if (sub.equals("nether") || sub.equals("server")) {
                options.add("open");
                options.add("close");
            }
        } else if (args.length == 3) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("team")) {
                options.add("noord");
                options.add("zuid");
                options.add("geen");
            } else if (sub.equals("grace")) {
                options.add("0");
                options.add("1");
                options.add("10");
                options.add("60");
            }
        }
        String typed = args.length == 0 ? "" : args[args.length - 1].toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(typed)) {
                result.add(option);
            }
        }
        return result;
    }
}
