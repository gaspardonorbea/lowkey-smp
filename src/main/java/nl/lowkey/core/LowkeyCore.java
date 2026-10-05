package nl.lowkey.core;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
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
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * LowkeySMP visuals and rules:
 * - tab list: logo on top, "Online: N" at the bottom, Noord (red) above Zuid (blue), icons before every name
 * - nametag icons: CREW, grace (yellow) and team (Noord / Zuid)
 * - join / leave messages with icons
 * - grace period: the first hour of playtime per player you simply respawn
 * - after that: elimination message, big title, blood, straight to spectator, then the lobby
 * - lobby: void world, hotbar item (slot 5) with a teleport dialog, locked until /lowkey launch
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
    /** the same badge in red: only used for death messages and the Nether messages */
    private static final String LOWKEY_BADGE_RED = "\uE009";

    // admin menu icons (vanilla item textures, see tags.json)
    private static final String M_LAUNCH = "\uE040";
    private static final String M_SERVER = "\uE041";
    private static final String M_WORLD = "\uE042";
    private static final String M_PLAYERS = "\uE043";
    private static final String M_MESSAGES = "\uE044";
    private static final String M_NETHER = "\uE045";
    private static final String M_BORDERS = "\uE046";
    private static final String M_LOBBY = "\uE047";
    private static final String M_SPAWN_N = "\uE048";
    private static final String M_SPAWN_Z = "\uE049";
    private static final String M_MAIN = "\uE04A";
    private static final String M_TEAM = "\uE04B";
    private static final String M_CREW = "\uE04C";
    private static final String M_GRACE = "\uE04D";
    private static final String M_REVIVE = "\uE04E";
    private static final String M_SAY = "\uE04F";
    private static final String M_DONATE = "\uE050";
    private static final String M_BACK = "\uE051";

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

    /**
     * true once the borders between Noord and Zuid are dropped: from then on chat is global again.
     * Until then, Noord and Zuid only see their own team's chat. Saved in state.yml.
     */
    private boolean bordersDropped;

    /** is the Nether open? saved in state.yml so it survives restarts */
    private boolean netherOpen;
    private File stateFile;
    /** stops the "Nether is closed" message from spamming while a player stands in a portal */
    private final Map<UUID, Long> netherNotice = new HashMap<>();

    // ---- lobby + launch (saved in state.yml)
    private static final String LOBBY_WORLD = "lowkey_lobby";
    /** false until /lowkey launch: until then the lobby item only says the server is not open yet */
    private boolean launched;
    private boolean lobbyCreated;
    private Location lobbySpawn;
    private Location spawnNoord;
    private Location spawnZuid;
    private NamespacedKey lobbyItemKey;

    private NamespacedKey graceKey;
    private NamespacedKey sideKey;
    private NamespacedKey eliminatedKey;
    private long graceMillis;
    private boolean eliminateOnDeath;
    private String eliminatedSuffix;
    /** how long an eliminated player gets to spectate before being kicked */
    private static final long ELIMINATION_SPECTATE_TICKS = 3600L; // 3 minutes

    // ------------------------------------------------------------------ server hours (09:00 - 00:00)

    private static final ZoneId LOWKEY_ZONE = ZoneId.of("Europe/Brussels");
    private static final int HOURS_OPEN_FROM = 9; // 09:00
    /** current cached state: is the server within opening hours right now? */
    private boolean hoursOpen = true;
    /** so the per-second check only does real work once per calendar minute */
    private int lastCheckedMinuteOfDay = -1;

    // ------------------------------------------------------------------ donation broadcaster

    private final Random random = new Random();
    private static final List<String> DONATION_MESSAGES = List.of(
            "Stuur Gaspard een privebericht voor een donatie te doen. Elke cent helpt.",
            "Donaties houden LowkeySMP draaiende. Elke cent helpt.",
            "Vind je LowkeySMP leuk? Een kleine donatie wordt gewaardeerd.",
            "Elke cent helpt om de server draaiende te houden.",
            "Wil je bijdragen aan de server? Elke donatie helpt, hoe klein ook.",
            "Steun LowkeySMP met een donatie, elke cent helpt.",
            "Stuur Gaspard een privebericht als je wil doneren.",
            "Achter de schermen kost een server geld. Elke donatie helpt.",
            "LowkeySMP draait dankzij mensen zoals jij. Elke cent helpt.",
            "Wil je doneren? Stuur Gaspard een privebericht."
    );

    // ------------------------------------------------------------------ lifecycle

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        stateFile = new File(getDataFolder(), "state.yml");
        YamlConfiguration state = YamlConfiguration.loadConfiguration(stateFile);
        netherOpen = state.getBoolean("nether-open", false);
        serverClosed = state.getBoolean("server-closed", false);
        bordersDropped = state.getBoolean("borders-dropped", false);
        graceKey = new NamespacedKey(this, "grace_left");
        sideKey = new NamespacedKey(this, "side");
        eliminatedKey = new NamespacedKey(this, "eliminated");
        lobbyItemKey = new NamespacedKey(this, "lobby_item");
        launched = state.getBoolean("launched", false);
        lobbyCreated = state.getBoolean("lobby-created", false);
        if (lobbyCreated) {
            loadLobbyWorld();
        }
        lobbySpawn = readLoc(state, "lobby-spawn");
        spawnNoord = readLoc(state, "spawn-noord");
        spawnZuid = readLoc(state, "spawn-zuid");

        cleanupTeams();
        getServer().getPluginManager().registerEvents(this, this);

        LocalTime startupTime = LocalTime.now(LOWKEY_ZONE);
        hoursOpen = startupTime.getHour() >= HOURS_OPEN_FROM;
        lastCheckedMinuteOfDay = startupTime.getHour() * 60 + startupTime.getMinute();

        for (Player player : getServer().getOnlinePlayers()) {
            startTracking(player, false);
        }
        getServer().getScheduler().runTaskTimer(this, this::tickGrace, 20L, 20L);
        getServer().getScheduler().runTaskTimer(this, this::updateTablist, 20L, 100L);
        getServer().getScheduler().runTaskTimer(this, this::tickServerHours, 20L, 20L);
        getServer().getScheduler().runTaskTimer(this, this::tickLobby, 40L, 200L);
        long donationIntervalTicks = 15L * 60L * 20L; // 15 minutes
        getServer().getScheduler().runTaskTimer(this, this::broadcastRandomDonationMessage,
                donationIntervalTicks, donationIntervalTicks);
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

    /** Adds or removes a player's crew status, persists it to config.yml, and refreshes their tags. */
    private void setCrew(CommandSender admin, Player target, boolean crewStatus) {
        String name = target.getName().toLowerCase(Locale.ROOT);
        if (crewStatus) {
            crew.add(name);
        } else {
            crew.remove(name);
        }
        getConfig().set("crew", new ArrayList<>(crew));
        saveConfig();
        refreshTags(target);
        if (admin != null) {
            admin.sendMessage(Component.text(
                    target.getName() + (crewStatus ? " is nu crew." : " is geen crew meer."), NamedTextColor.GREEN));
        }
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
            if (inLobby(player)) {
                graceLast.put(id, now); // time in the lobby does not count as playtime
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

    /** Scoreboard team name: "lk" + sort digit + player name. The tab list sorts on this name. */
    private static String teamNameFor(Player player, char sortKey) {
        String name = "lk" + sortKey + player.getName().toLowerCase(Locale.ROOT);
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

        char sortKey = side.sortKey;

        // one small scoreboard team per player: gives the nametag prefix and the tab list order
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        String wanted = teamNameFor(player, sortKey);
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
            wanted = "lk" + sortKey + id.toString().substring(0, 8);
            team = board.getTeam(wanted);
        }
        if (team == null) {
            team = board.registerNewTeam(wanted);
        }
        team.prefix(icons);
        team.addEntry(player.getName());
        teamNames.put(id, wanted);

        // tab list entry: icons + name in the team colour
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
        if (lobbySpawn != null) {
            // everybody starts in the lobby
            getServer().getScheduler().runTask(this, () -> sendToLobby(player));
        } else if (playerFile(player.getUniqueId()).exists()) {
            // the lobby was deleted while this player was away: give the real inventory back
            getServer().getScheduler().runTask(this, () -> {
                if (!player.isOnline()) {
                    return;
                }
                restoreInventory(player);
                if (!isEliminated(player) && player.getGameMode() == GameMode.ADVENTURE) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            });
        }
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
        final Player sender = event.getPlayer();
        final Side side = sideCache.getOrDefault(sender.getUniqueId(), Side.NONE);
        // chat only ever shows Noord/Zuid - grace and elimination never show a badge here. Crew only
        // shows here when the player has no team (otherwise the team badge takes priority, crew still
        // shows in the tab list either way).
        final String statusIcon;
        if (side != Side.NONE) {
            statusIcon = side.icon;
        } else if (isCrew(sender)) {
            statusIcon = CREW_ICON;
        } else {
            statusIcon = null;
        }
        final TextColor nameColor = side == Side.NONE ? null : TextColor.color(side.rgb);
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> {
            Component line = Component.empty();
            if (statusIcon != null) {
                line = line.append(glyph(statusIcon)).append(Component.space());
            }
            Component name = nameColor == null ? sourceDisplayName : sourceDisplayName.color(nameColor);
            return line.append(name)
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(message);
        }));

        // Noord/Zuid chat is separate until the borders drop
        boolean global = bordersDropped || side == Side.NONE;
        if (!global) {
            event.viewers().removeIf(viewer -> {
                if (!(viewer instanceof Player) || viewer.equals(sender)) {
                    return false;
                }
                Player viewerPlayer = (Player) viewer;
                if (viewerPlayer.hasPermission("lowkey.admin")) {
                    return false; // staff always sees both team chats
                }
                Side viewerSide = sideCache.getOrDefault(viewerPlayer.getUniqueId(), Side.NONE);
                return viewerSide != side;
            });
        }
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
                    .append(glyph(LOWKEY_BADGE_RED))
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
                .append(glyph(LOWKEY_BADGE_RED))
                .append(Component.space())
                .append(Component.text(player.getName(), NamedTextColor.WHITE))
                .append(Component.text(eliminatedSuffix, NamedTextColor.RED))
                .build());

        bloodBurst(spot.clone().add(0, 1, 0));

        // no death screen: cancel the death and keep the player alive with full health
        event.setCancelled(true);
        event.setReviveHealth(20.0);

        if (eliminateOnDeath && world != null) {
            setEliminated(player, true);
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
                player.sendMessage(spectateMessage());
                // a few minutes to spectate, then to the lobby (for good). Without a lobby: off the server.
                getServer().getScheduler().runTaskLater(this, () -> {
                    if (!player.isOnline() || !isEliminated(player)) {
                        return;
                    }
                    if (lobbySpawn != null && lobbySpawn.getWorld() != null) {
                        sendToLobby(player);
                    } else {
                        player.kick(Component.text()
                                .append(glyph(LOWKEY_BADGE))
                                .append(Component.space())
                                .append(Component.text("Je bent uitgeschakeld. Bedankt voor het spelen!",
                                        NamedTextColor.RED))
                                .build());
                    }
                }, ELIMINATION_SPECTATE_TICKS);
            }

            player.showTitle(Title.title(
                    glyph(TITLE_GLYPH),
                    Component.empty(),
                    Title.Times.times(Duration.ofMillis(150), Duration.ofMillis(3000), Duration.ofMillis(1000))));
        });
    }

    /** Private message to the player who was just eliminated. */
    private Component spectateMessage() {
        return Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je kunt voor de komende ", NamedTextColor.WHITE))
                .append(Component.text("3 minuten", NamedTextColor.RED))
                .append(Component.text(" spectaten", NamedTextColor.WHITE))
                .build();
    }

    private boolean isEliminated(Player player) {
        Boolean value = player.getPersistentDataContainer().get(eliminatedKey, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

    private void setEliminated(Player player, boolean value) {
        player.getPersistentDataContainer().set(eliminatedKey, PersistentDataType.BOOLEAN, value);
    }

    /**
     * Clears the "dood"/uitgeschakeld status (so the spectate-then-kick timer never fires). Mainly
     * for testing. Does not touch team, grace, location or inventory.
     */
    private void clearEliminated(Player target) {
        setEliminated(target, false);
        refreshTags(target);
        if (inLobby(target)) {
            sendToLobby(target); // gives the lobby item back
        }
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
            return;
        }
        if (!hoursOpen && !crew.contains(event.getName().toLowerCase(Locale.ROOT))) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, hoursClosedMessage());
        }
    }

    // ------------------------------------------------------------------ server hours (09:00 - 00:00, crew always welcome)

    private Component hoursClosedMessage() {
        return Component.text("De server is open van 09:00 tot 00:00. Kom later terug", NamedTextColor.RED);
    }

    private Component closingWarningMessage(int minutesLeft) {
        String unit = minutesLeft == 1 ? "minuut" : "minuten";
        return Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("De server sluit over ", NamedTextColor.RED))
                .append(Component.text(minutesLeft + " " + unit, NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(".", NamedTextColor.RED))
                .build();
    }

    /**
     * Runs every second, but only does real work once a calendar minute actually changes (in the
     * Europe/Brussels zone). Kicks everyone except crew the moment the clock hits 00:00, and warns
     * everyone online 10, 3 and 1 minute before that happens.
     */
    private void tickServerHours() {
        LocalTime now = LocalTime.now(LOWKEY_ZONE);
        int minuteOfDay = now.getHour() * 60 + now.getMinute();
        if (minuteOfDay == lastCheckedMinuteOfDay) {
            return;
        }
        lastCheckedMinuteOfDay = minuteOfDay;

        boolean shouldBeOpen = now.getHour() >= HOURS_OPEN_FROM;
        if (shouldBeOpen != hoursOpen) {
            hoursOpen = shouldBeOpen;
            if (!hoursOpen) {
                for (Player player : new ArrayList<>(getServer().getOnlinePlayers())) {
                    if (!isCrew(player)) {
                        player.kick(hoursClosedMessage());
                    }
                }
            }
        }

        if (!hoursOpen) {
            return;
        }
        int minutesToClose = (24 * 60) - minuteOfDay; // minutes left until the next 00:00
        if (minutesToClose == 10 || minutesToClose == 3 || minutesToClose == 1) {
            Component warning = closingWarningMessage(minutesToClose);
            for (Player player : getServer().getOnlinePlayers()) {
                player.sendMessage(warning);
            }
        }
    }

    // ------------------------------------------------------------------ donation broadcaster

    private void broadcastRandomDonationMessage() {
        if (getServer().getOnlinePlayers().isEmpty()) {
            return;
        }
        sendDonationMessage(null);
    }

    /** Sends one random donation message. If `only` is null it goes to everyone, otherwise just to that player. */
    private void sendDonationMessage(Player only) {
        String text = DONATION_MESSAGES.get(random.nextInt(DONATION_MESSAGES.size()));
        Component message = Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(text, NamedTextColor.WHITE))
                .build();
        if (only != null) {
            only.sendMessage(message);
        } else {
            for (Player player : getServer().getOnlinePlayers()) {
                player.sendMessage(message);
            }
        }
    }

    // ------------------------------------------------------------------ MOTD (fully automatic, no manual edits)

    // approximate pixel widths of the default Minecraft font, used to centre the MOTD lines
    private static final Map<Character, Integer> MOTD_CHAR_WIDTH = new HashMap<>();
    static {
        String narrow2 = "il.,:;'!|";
        String narrow3 = "I[]t";
        String wide7 = "@~mMW";
        for (char c : narrow2.toCharArray()) MOTD_CHAR_WIDTH.put(c, 2);
        for (char c : narrow3.toCharArray()) MOTD_CHAR_WIDTH.put(c, 3);
        for (char c : wide7.toCharArray()) MOTD_CHAR_WIDTH.put(c, 7);
        for (char c : "0123456789".toCharArray()) MOTD_CHAR_WIDTH.put(c, 6);
        for (char c : "ABCDEFGHJKLNOPQRSTUVXYZ".toCharArray()) MOTD_CHAR_WIDTH.put(c, 6);
        for (char c : "abcdeghknopqsuvxyz".toCharArray()) MOTD_CHAR_WIDTH.put(c, 6);
        MOTD_CHAR_WIDTH.put(' ', 4);
        MOTD_CHAR_WIDTH.put('-', 6);
        MOTD_CHAR_WIDTH.put('!', 2);
    }
    // the box the client draws the MOTD in is roughly this many pixels wide at default font size
    private static final int MOTD_LINE_WIDTH = 200;

    private static int motdTextWidth(String plain) {
        int total = 0;
        for (char c : plain.toCharArray()) {
            total += MOTD_CHAR_WIDTH.getOrDefault(c, 6) + 1;
        }
        return total == 0 ? 0 : total - 1;
    }

    /** Pads a line with leading spaces so it renders roughly centred in the server list. */
    private static Component centerMotdLine(Component styled, String plainForWidth) {
        int width = motdTextWidth(plainForWidth);
        int padPixels = Math.max(0, (MOTD_LINE_WIDTH - width) / 2);
        int spaces = padPixels / MOTD_CHAR_WIDTH.get(' ');
        return spaces == 0 ? styled : Component.text(" ".repeat(spaces)).append(styled);
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        String line1Text = "LowkeySMP - Lowkey Peak";
        Component line1 = centerMotdLine(
                Component.text("LowkeySMP", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)
                        .append(Component.text(" - Lowkey Peak", NamedTextColor.GRAY)),
                line1Text);

        String line2Text;
        Component line2Styled;
        if (!hoursOpen) {
            line2Text = "Gesloten - open om 09:00";
            line2Styled = Component.text(line2Text, NamedTextColor.RED);
        } else if (serverClosed) {
            line2Text = "Tijdelijk gesloten voor onderhoud";
            line2Styled = Component.text(line2Text, NamedTextColor.RED);
        } else {
            int online = getServer().getOnlinePlayers().size();
            line2Text = "Open! " + online + " online";
            line2Styled = Component.text("Open! ", NamedTextColor.GREEN)
                    .append(Component.text(online + " online", NamedTextColor.WHITE));
        }
        Component line2 = centerMotdLine(line2Styled, line2Text);

        event.motd(line1.append(Component.newline()).append(line2));
    }

    // ------------------------------------------------------------------ Nether open / closed

    private void saveState() {
        YamlConfiguration state = new YamlConfiguration();
        state.set("nether-open", netherOpen);
        state.set("server-closed", serverClosed);
        state.set("borders-dropped", bordersDropped);
        state.set("launched", launched);
        state.set("lobby-created", lobbyCreated);
        writeLoc(state, "lobby-spawn", lobbySpawn);
        writeLoc(state, "spawn-noord", spawnNoord);
        writeLoc(state, "spawn-zuid", spawnZuid);
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
                .append(glyph(LOWKEY_BADGE_RED))
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

    /** Drops (or restores) the borders between Noord and Zuid chat. Does not touch the world border itself. */
    private void setBordersDropped(boolean dropped) {
        bordersDropped = dropped;
        saveState();
        Component message = dropped
                ? Component.text("De grenzen zijn open! Noord en Zuid kunnen nu bij elkaar in de chat.", NamedTextColor.GREEN)
                : Component.text("De grenzen zijn weer dicht: Noord en Zuid zien alleen hun eigen chat.", NamedTextColor.RED);
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(message);
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
                    .append(glyph(LOWKEY_BADGE_RED))
                    .append(Component.space())
                    .append(Component.text("De Nether is nog ", NamedTextColor.RED))
                    .append(Component.text("gesloten", NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text("!", NamedTextColor.RED))
                    .build());
        }
    }

    // ------------------------------------------------------------------ lobby (void world) + server launch

    /** The empty world: no terrain at all, so the lobby is a clean canvas to build in. */
    private static final class VoidGenerator extends ChunkGenerator {
        @Override
        public Location getFixedSpawnLocation(World world, Random random) {
            return new Location(world, 0.5, 65, 0.5);
        }
    }

    private World lobbyWorld() {
        return Bukkit.getWorld(LOBBY_WORLD);
    }

    /** Loads the lobby world (or creates it the first time). */
    private World loadLobbyWorld() {
        World existing = Bukkit.getWorld(LOBBY_WORLD);
        if (existing != null) {
            return existing;
        }
        World world = new WorldCreator(LOBBY_WORLD)
                .environment(World.Environment.NORMAL)
                .generator(new VoidGenerator())
                .createWorld();
        if (world == null) {
            getLogger().warning("Kon de lobby wereld niet laden.");
        }
        return world;
    }

    private World gameWorld() {
        return getServer().getWorlds().get(0);
    }

    private boolean inLobby(Player player) {
        return lobbySpawn != null && lobbySpawn.getWorld() != null && player.getWorld().equals(lobbySpawn.getWorld());
    }

    /** Admins in creative mode may build in the lobby; everybody else is locked. */
    private boolean isBypass(Player player) {
        return player.hasPermission("lowkey.admin") && player.getGameMode() == GameMode.CREATIVE;
    }

    private boolean isLocked(Player player) {
        return inLobby(player) && !isBypass(player);
    }

    /** Keeps the lobby world a calm, sunny place. */
    private void tickLobby() {
        World world = lobbyWorld();
        if (world == null) {
            return;
        }
        world.setTime(6000L);
        if (world.hasStorm()) {
            world.setStorm(false);
        }
        if (world.isThundering()) {
            world.setThundering(false);
        }
    }

    private void createLobby(CommandSender sender) {
        if (lobbyCreated && lobbyWorld() != null) {
            sender.sendMessage(Component.text("De lobby bestaat al. Gebruik /lowkey lobby tp om erheen te gaan.",
                    NamedTextColor.RED));
            return;
        }
        World world = loadLobbyWorld();
        if (world == null) {
            sender.sendMessage(Component.text("De lobby wereld kon niet gemaakt worden, kijk in de console.",
                    NamedTextColor.RED));
            return;
        }
        lobbyCreated = true;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                world.getBlockAt(x, 64, z).setType(Material.POLISHED_DEEPSLATE);
            }
        }
        world.setSpawnLocation(0, 65, 0);
        lobbySpawn = new Location(world, 0.5, 65, 0.5, 0f, 0f);
        saveState();
        sender.sendMessage(Component.text(
                "De lobby is gemaakt (lege wereld met een klein platform). Verplaats het spawnpunt met /lowkey lobby setspawn.",
                NamedTextColor.GREEN));
    }

    private boolean lobbyExists() {
        return lobbyCreated || lobbyWorld() != null
                || new File(getServer().getWorldContainer(), LOBBY_WORLD).isDirectory();
    }

    private static boolean deleteRecursively(File file) {
        boolean ok = true;
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                ok &= deleteRecursively(child);
            }
        }
        return file.delete() && ok;
    }

    /** Someone is in a lobby that is about to be deleted: back to the game world with their own things. */
    private void leaveDeletedLobby(Player player) {
        YamlConfiguration data = loadPlayerData(player.getUniqueId());
        Location dest = readLoc(data, "last");
        if (dest == null || dest.getWorld() == null || dest.getWorld().getName().equals(LOBBY_WORLD)) {
            dest = gameWorld().getSpawnLocation();
        }
        boolean eliminated = isEliminated(player);
        restoreInventory(player);
        if (eliminated) {
            player.setGameMode(GameMode.SPECTATOR);
        } else if (!isBypass(player) && player.getGameMode() == GameMode.ADVENTURE) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        player.setFallDistance(0f);
        player.teleport(dest);
    }

    /** Wipes the lobby world completely (also an old leftover folder), so /lowkey lobby create starts clean. */
    private void deleteLobby(CommandSender sender) {
        if (!lobbyExists()) {
            sender.sendMessage(Component.text("Er is geen lobby om te verwijderen.", NamedTextColor.RED));
            return;
        }
        World world = lobbyWorld();
        File folder = world != null ? world.getWorldFolder() : new File(getServer().getWorldContainer(), LOBBY_WORLD);
        if (world != null) {
            for (Player player : new ArrayList<>(world.getPlayers())) {
                leaveDeletedLobby(player);
            }
            if (!Bukkit.unloadWorld(world, false)) {
                sender.sendMessage(Component.text(
                        "De lobby wereld kon niet worden afgesloten, er staat nog iemand in. Probeer het opnieuw.",
                        NamedTextColor.RED));
                return;
            }
        }
        boolean wiped = !folder.exists() || deleteRecursively(folder);
        lobbyCreated = false;
        lobbySpawn = null;
        saveState();
        if (wiped) {
            sender.sendMessage(Component.text(
                    "De lobby is verwijderd. Maak een nieuwe met /lowkey lobby create.", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text(
                    "De lobby is losgekoppeld, maar de map '" + folder.getName()
                            + "' kon niet helemaal gewist worden. Verwijder die handmatig.", NamedTextColor.YELLOW));
        }
    }

    private void openLobbyDeleteConfirm(Player admin, Runnable back) {
        confirm(admin,
                "Lobby verwijderen?",
                "De hele lobby wereld wordt gewist, met alles wat erin staat.",
                "Spelers in de lobby gaan terug naar de gamewereld.",
                Component.text("Ja, verwijderen", NamedTextColor.RED),
                () -> {
                    deleteLobby(admin);
                    back.run();
                },
                back);
    }

    private void teleportAdminToLobby(Player admin) {
        if (lobbySpawn == null || lobbySpawn.getWorld() == null) {
            admin.sendMessage(Component.text("Maak de lobby eerst aan met /lowkey lobby create.", NamedTextColor.RED));
            return;
        }
        sendToLobby(admin);
    }

    private void setLobbySpawn(Player admin) {
        if (lobbyWorld() == null || !admin.getWorld().equals(lobbyWorld())) {
            admin.sendMessage(Component.text("Je moet in de lobby staan om het lobby spawnpunt te zetten.",
                    NamedTextColor.RED));
            return;
        }
        lobbySpawn = admin.getLocation().clone();
        saveState();
        admin.sendMessage(Component.text("Het lobby spawnpunt is verplaatst naar jouw positie.", NamedTextColor.GREEN));
    }

    private void setTeamSpawn(Player admin, Side side) {
        if (side == Side.NONE) {
            return;
        }
        if (inLobby(admin)) {
            admin.sendMessage(Component.text("Ga eerst naar de gamewereld, dit spawnpunt hoort daar.",
                    NamedTextColor.RED));
            return;
        }
        if (side == Side.NOORD) {
            spawnNoord = admin.getLocation().clone();
        } else {
            spawnZuid = admin.getLocation().clone();
        }
        saveState();
        admin.sendMessage(Component.text("Het spawnpunt van " + side.id + " staat nu op jouw positie.",
                NamedTextColor.GREEN));
    }

    private Location teamSpawn(Side side) {
        if (side == Side.NOORD) {
            return spawnNoord;
        }
        if (side == Side.ZUID) {
            return spawnZuid;
        }
        return null;
    }

    // ---- per player files (stashed inventory + last position in the game world)

    private File playerFile(UUID id) {
        return new File(new File(getDataFolder(), "players"), id + ".yml");
    }

    private YamlConfiguration loadPlayerData(UUID id) {
        return YamlConfiguration.loadConfiguration(playerFile(id));
    }

    private boolean savePlayerData(UUID id, YamlConfiguration data) {
        try {
            File file = playerFile(id);
            file.getParentFile().mkdirs();
            data.save(file);
            return true;
        } catch (IOException e) {
            getLogger().warning("Kon speler data niet opslaan: " + e.getMessage());
            return false;
        }
    }

    private static void writeLoc(ConfigurationSection root, String path, Location loc) {
        if (loc == null || loc.getWorld() == null) {
            root.set(path, null);
            return;
        }
        root.set(path + ".world", loc.getWorld().getName());
        root.set(path + ".x", loc.getX());
        root.set(path + ".y", loc.getY());
        root.set(path + ".z", loc.getZ());
        root.set(path + ".yaw", (double) loc.getYaw());
        root.set(path + ".pitch", (double) loc.getPitch());
    }

    private static Location readLoc(ConfigurationSection root, String path) {
        String worldName = root.getString(path + ".world");
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        return new Location(world,
                root.getDouble(path + ".x"), root.getDouble(path + ".y"), root.getDouble(path + ".z"),
                (float) root.getDouble(path + ".yaw"), (float) root.getDouble(path + ".pitch"));
    }

    // ---- the lobby item (nether star in hotbar slot 5)

    private boolean isLobbyItem(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(lobbyItemKey, PersistentDataType.BYTE);
    }

    private ItemStack createLobbyItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("LowkeySMP", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Rechtermuisklik om naar het main eiland te gaan.", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)));
        meta.setEnchantmentGlintOverride(true);
        meta.getPersistentDataContainer().set(lobbyItemKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private void giveLobbyItem(Player player) {
        player.getInventory().setItem(4, createLobbyItem());
        player.getInventory().setHeldItemSlot(4);
    }

    private void removeLobbyItems(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (isLobbyItem(contents[i])) {
                player.getInventory().setItem(i, null);
            }
        }
    }

    // ---- moving between lobby and game world

    /**
     * Sends a player to the lobby. Alive players first get their real inventory put away in
     * players/<uuid>.yml (and their last game position remembered); the lobby item goes in slot 5.
     * Eliminated players just land in the lobby without the item and can never leave it.
     */
    private void sendToLobby(Player player) {
        if (lobbySpawn == null || lobbySpawn.getWorld() == null || !player.isOnline()) {
            return;
        }
        UUID id = player.getUniqueId();
        boolean eliminated = isEliminated(player);

        if (!eliminated) {
            YamlConfiguration data = loadPlayerData(id);
            if (!inLobby(player) && data.getBoolean("entered", false)) {
                writeLoc(data, "last", player.getLocation());
            }
            if (!data.getBoolean("stashed", false)) {
                data.set("inv", Arrays.asList(player.getInventory().getContents()));
                data.set("stashed", true);
            }
            if (!savePlayerData(id, data)) {
                player.sendMessage(Component.text("Er ging iets mis, vraag een admin om hulp.", NamedTextColor.RED));
                return;
            }
            player.getInventory().clear();
        }

        if (!isBypass(player)
                && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.SPECTATOR)) {
            player.setGameMode(GameMode.ADVENTURE);
        }
        player.setFallDistance(0f);
        player.setFireTicks(0);
        player.teleport(lobbySpawn);

        if (eliminated) {
            removeLobbyItems(player);
        } else {
            giveLobbyItem(player);
        }
    }

    /** Puts the real inventory back (and removes the lobby item). Does nothing if nothing was put away. */
    private void restoreInventory(Player player) {
        UUID id = player.getUniqueId();
        YamlConfiguration data = loadPlayerData(id);
        removeLobbyItems(player);
        if (!data.getBoolean("stashed", false)) {
            return;
        }
        List<?> saved = data.getList("inv");
        ItemStack[] contents = new ItemStack[player.getInventory().getContents().length];
        if (saved != null) {
            for (int i = 0; i < saved.size() && i < contents.length; i++) {
                Object entry = saved.get(i);
                if (entry instanceof ItemStack) {
                    contents[i] = (ItemStack) entry;
                }
            }
        }
        player.getInventory().clear();
        player.getInventory().setContents(contents);
        data.set("stashed", false);
        data.set("inv", null);
        savePlayerData(id, data);
    }

    /**
     * Lets a player leave the lobby. Normally to the last place they were in the game world, and if
     * there is none (or toTeamSpawn is true) to the spawn point of their team. Admins can use the
     * main world spawn as a last resort. Returns true if the player was moved.
     */
    private boolean enterGame(Player player, boolean toTeamSpawn, boolean adminFallback) {
        UUID id = player.getUniqueId();
        YamlConfiguration data = loadPlayerData(id);

        Location dest = null;
        if (!toTeamSpawn) {
            dest = readLoc(data, "last");
        }
        if (dest == null) {
            dest = teamSpawn(getSide(player));
        }
        if (dest == null && adminFallback) {
            dest = gameWorld().getSpawnLocation();
        }
        if (dest == null) {
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Het spawnpunt van jouw team is nog niet ingesteld.", NamedTextColor.RED))
                    .build());
            return false;
        }

        restoreInventory(player);
        if (player.getGameMode() == GameMode.ADVENTURE) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        data = loadPlayerData(id);
        data.set("entered", true);
        data.set("last", null);
        savePlayerData(id, data);

        player.setFallDistance(0f);
        player.teleport(dest);
        return true;
    }

    /** Right-click on the lobby item. */
    private void useLobbyItem(Player player) {
        if (!inLobby(player) || isEliminated(player)) {
            return;
        }
        if (!launched) {
            player.sendActionBar(Component.text("De server is nog niet open!", NamedTextColor.RED));
            return;
        }
        if (getSide(player) == Side.NONE) {
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Je zit nog niet in een team!", NamedTextColor.RED))
                    .build());
            return;
        }
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Teleporteren", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                        .body(List.of(DialogBody.plainMessage(
                                Component.text("Wil je naar het main eiland teleporteren?", NamedTextColor.WHITE))))
                        .build())
                .type(DialogType.confirmation(
                        playerButton(Component.text("Ja", NamedTextColor.GREEN),
                                "Teleporteer naar het main eiland.", 100, this::teleportToMain),
                        ActionButton.create(Component.text("Nee", NamedTextColor.RED),
                                Component.text("Sluit dit venster."), 100, null))));
        player.showDialog(dialog);
    }

    private void teleportToMain(Player player) {
        if (!player.isOnline() || !inLobby(player) || isEliminated(player) || !launched
                || getSide(player) == Side.NONE) {
            return;
        }
        enterGame(player, false, false);
    }

    /** A button anybody can press (no admin check): used for the lobby dialog. */
    private ActionButton playerButton(Component label, String tooltip, int width, Consumer<Player> action) {
        DialogAction dialogAction = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            getServer().getScheduler().runTask(this, () -> action.accept(clicker));
        }, CLICK_OPTIONS);
        return ActionButton.create(label, Component.text(tooltip), width, dialogAction);
    }

    /** Opens the server: everybody with a team goes to the spawn of their team. */
    private void launchServer(CommandSender admin) {
        if (launched) {
            admin.sendMessage(Component.text("De server is al gelanceerd.", NamedTextColor.RED));
            return;
        }
        if (spawnNoord == null || spawnZuid == null) {
            admin.sendMessage(Component.text(
                    "Stel eerst beide spawnpunten in met /lowkey setspawn <noord|zuid>.", NamedTextColor.RED));
            return;
        }
        launched = true;
        saveState();

        Title title = Title.title(
                Component.text("Veel succes!", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        int moved = 0;
        List<String> noTeam = new ArrayList<>();
        for (Player player : new ArrayList<>(getServer().getOnlinePlayers())) {
            if (isEliminated(player)) {
                continue;
            }
            if (getSide(player) == Side.NONE) {
                noTeam.add(player.getName());
                player.sendMessage(Component.text()
                        .append(glyph(LOWKEY_BADGE))
                        .append(Component.space())
                        .append(Component.text("Je zit nog niet in een team!", NamedTextColor.RED))
                        .build());
                continue;
            }
            if (enterGame(player, true, false)) {
                moved++;
                player.showTitle(title);
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }
        String extra = noTeam.isEmpty() ? "" : " Zonder team (blijven in de lobby): " + String.join(", ", noTeam) + ".";
        admin.sendMessage(Component.text("De server is gelanceerd! " + moved
                + " spelers zijn naar hun team spawn gestuurd." + extra, NamedTextColor.GREEN));
    }

    /** Undoes the launch (for testing): the lobby item says "nog niet open" again. Nobody is moved. */
    private void resetLaunch(CommandSender admin) {
        if (!launched) {
            admin.sendMessage(Component.text("De server is nog niet gelanceerd.", NamedTextColor.RED));
            return;
        }
        launched = false;
        saveState();
        admin.sendMessage(Component.text(
                "De launch is teruggezet. Spelers die al in de gamewereld zijn blijven daar, "
                        + "wie in de lobby staat kan er pas weer uit na een nieuwe launch.", NamedTextColor.GREEN));
    }

    // ---- custom broadcast with the LOWKEY badge

    private void broadcastMessage(String text) {
        Component message = Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(text, NamedTextColor.WHITE))
                .build();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
        getLogger().info("[broadcast] " + text);
    }

    // ---- lobby protection: no building, no dropping, no PvP, no damage, no hunger

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!inLobby(player)) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            if (!isBypass(player)) {
                event.setCancelled(true);
            }
            return;
        }
        if (isLobbyItem(event.getItem())) {
            event.setCancelled(true);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                useLobbyItem(player);
            }
            return;
        }
        if (!isBypass(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyBreak(BlockBreakEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyPlace(BlockPlaceEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (isLocked(player)) {
            event.setCancelled(true);
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Je mag hier geen items droppen!", NamedTextColor.RED))
                    .build());
        }
    }

    @EventHandler
    public void onLobbyClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && isLocked((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player && isLocked((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbySwapHands(PlayerSwapHandItemsEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player && isLocked((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && inLobby((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        if (!inLobby(player)) {
            return;
        }
        event.setCancelled(true);
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            getServer().getScheduler().runTask(this, () -> {
                if (player.isOnline() && inLobby(player)) {
                    player.setFallDistance(0f);
                    player.teleport(lobbySpawn);
                }
            });
        }
    }

    @EventHandler
    public void onLobbyEntityInteract(PlayerInteractEntityEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player && isLocked((Player) event.getRemover())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyMobSpawn(CreatureSpawnEvent event) {
        World lobby = lobbyWorld();
        if (lobby == null || !event.getLocation().getWorld().equals(lobby)) {
            return;
        }
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL || reason == CreatureSpawnEvent.SpawnReason.CHUNK_GEN) {
            event.setCancelled(true);
        }
    }

    // ------------------------------------------------------------------ admin command

    /**
     * /lowkey  (of /lowkey menu) : opent het beheerpaneel met knoppen
     * /lowkey grace <speler> <minuten> : zet de resterende grace tijd (0 = grace beeindigen)
     * /lowkey team <speler> <noord|zuid|geen> : zet de speler in Noord, Zuid of geen team
     * /lowkey nether <open|close> : opent of sluit de Nether (bij openen: titel + bericht voor iedereen)
     * /lowkey server <open|close> : sluit de server voor iedereen behalve 'closed-access' (geen bans), of maakt hem weer open
     * /lowkey border <open|close> : opent of sluit de grenzen tussen Noord- en Zuid-chat
     * /lowkey revive <speler> : verwijdert de dood/uitgeschakeld-status van een speler (vooral voor testen)
     * /lowkey donate : stuurt meteen een willekeurig donatiebericht (voor testen)
     * /lowkey launch : vraagt bevestiging, stuurt dan iedereen met een team naar zijn team spawn (confirm = meteen)
     * /lowkey launch reset : zet de launch terug (voor testen), niemand wordt verplaatst
     * /lowkey lobby <create|tp|setspawn|delete> : maakt de lege lobby wereld, gaat erheen, zet het lobby spawnpunt of wist de lobby
     * /lowkey setspawn <noord|zuid> : zet het spawnpunt van een team op jouw positie
     * /lowkey main : stuurt jou (admin) naar de gamewereld, ook als de server nog niet gelanceerd is
     * /lowkey say <bericht> : stuurt een bericht met de LOWKEY badge naar iedereen
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
        if (sender instanceof Player && (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("menu")))) {
            openMenu((Player) sender);
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
        if (args.length == 3 && args[0].equalsIgnoreCase("crew")) {
            Player target = getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
                return true;
            }
            String choice = args[2].toLowerCase(Locale.ROOT);
            if (!choice.equals("aan") && !choice.equals("uit")) {
                sender.sendMessage(Component.text("Gebruik: /lowkey crew <speler> <aan|uit>", NamedTextColor.GRAY));
                return true;
            }
            setCrew(sender, target, choice.equals("aan"));
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("revive")) {
            Player target = getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
                return true;
            }
            clearEliminated(target);
            sender.sendMessage(Component.text(
                    "Dood-status van " + target.getName() + " is verwijderd.", NamedTextColor.GREEN));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("donate")) {
            sender.sendMessage(Component.text("Donatiebericht verstuurd.", NamedTextColor.GREEN));
            broadcastRandomDonationMessage();
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
        if (args.length >= 1 && args[0].equalsIgnoreCase("border")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("open")) {
                setBordersDropped(true);
                sender.sendMessage(Component.text(
                        "De grenzen zijn open: chat is weer voor iedereen zichtbaar.", NamedTextColor.GREEN));
                return true;
            }
            if (args.length == 2 && args[1].equalsIgnoreCase("close")) {
                setBordersDropped(false);
                sender.sendMessage(Component.text(
                        "De grenzen zijn weer dicht: Noord en Zuid zien alleen hun eigen chat.", NamedTextColor.GREEN));
                return true;
            }
            sender.sendMessage(Component.text(
                    "Grenzen zijn nu " + (bordersDropped ? "open" : "dicht") + ". Gebruik: /lowkey border <open|close>",
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
        if (args.length >= 1 && args[0].equalsIgnoreCase("lobby")) {
            String sub = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "";
            if (sub.equals("create")) {
                createLobby(sender);
                return true;
            }
            if (sub.equals("delete")) {
                if (args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
                    deleteLobby(sender);
                } else if (sender instanceof Player) {
                    final Player admin = (Player) sender;
                    openLobbyDeleteConfirm(admin, () -> openWorldMenu(admin));
                } else {
                    sender.sendMessage(Component.text("Gebruik in de console: /lowkey lobby delete confirm",
                            NamedTextColor.GRAY));
                }
                return true;
            }
            if (sub.equals("tp") || sub.equals("setspawn")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
                    return true;
                }
                if (sub.equals("tp")) {
                    teleportAdminToLobby((Player) sender);
                } else {
                    setLobbySpawn((Player) sender);
                }
                return true;
            }
            sender.sendMessage(Component.text("Gebruik: /lowkey lobby <create|tp|setspawn|delete>", NamedTextColor.GRAY));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("setspawn")) {
            Side side = args.length == 2 ? Side.fromId(args[1].toLowerCase(Locale.ROOT)) : null;
            if (!(sender instanceof Player) || side == null || side == Side.NONE) {
                sender.sendMessage(Component.text("Gebruik in-game: /lowkey setspawn <noord|zuid>", NamedTextColor.GRAY));
                return true;
            }
            setTeamSpawn((Player) sender, side);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("main")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
                return true;
            }
            enterGame((Player) sender, false, true);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("launch")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
                resetLaunch(sender);
                return true;
            }
            if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                launchServer(sender);
                return true;
            }
            if (launched) {
                sender.sendMessage(Component.text("De server is al gelanceerd.", NamedTextColor.RED));
            } else if (sender instanceof Player) {
                final Player admin = (Player) sender;
                openLaunchConfirm(admin, () -> openMenu(admin));
            } else {
                sender.sendMessage(Component.text("Gebruik in de console: /lowkey launch confirm", NamedTextColor.GRAY));
            }
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("say")) {
            if (args.length < 2) {
                sender.sendMessage(Component.text("Gebruik: /lowkey say <bericht>", NamedTextColor.GRAY));
                return true;
            }
            broadcastMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
            return true;
        }
        sender.sendMessage(Component.text(
                "Gebruik: /lowkey grace <speler> <minuten>  |  /lowkey team <speler> <noord|zuid|geen>  |  /lowkey crew <speler> <aan|uit>  |  /lowkey nether <open|close>  |  /lowkey server <open|close>  |  /lowkey border <open|close>  |  /lowkey revive <speler>  |  /lowkey donate  |  /lowkey launch [reset]  |  /lowkey lobby <create|tp|setspawn|delete>  |  /lowkey setspawn <noord|zuid>  |  /lowkey main  |  /lowkey say <bericht>",
                NamedTextColor.GRAY));
        return true;
    }

    // ------------------------------------------------------------------ admin panel (dialog menus)

    private static final ClickCallback.Options CLICK_OPTIONS =
            ClickCallback.Options.builder().uses(1).lifetime(Duration.ofMinutes(10)).build();

    /**
     * One clickable button in a dialog. The action runs on the main thread and only when the person
     * who clicked really has the lowkey.admin permission (so a menu can never be abused).
     */
    private ActionButton button(Component label, String tooltip, int width, Runnable action) {
        DialogAction dialogAction = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!clicker.hasPermission("lowkey.admin")) {
                return;
            }
            getServer().getScheduler().runTask(this, action);
        }, CLICK_OPTIONS);
        return ActionButton.create(label, Component.text(tooltip), width, dialogAction);
    }

    /** A menu label: the vanilla item icon from the resource pack, a space and the text. */
    private Component iconLabel(String icon, String text, TextColor color) {
        return Component.text()
                .append(glyph(icon))
                .append(Component.space())
                .append(Component.text(text, color))
                .build();
    }

    private static Component menuTitle(String text) {
        return Component.text(text, NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD);
    }

    /** One status line: grey label, coloured value. */
    private static Component statusLine(String label, String value, TextColor valueColor) {
        return Component.text()
                .append(Component.text(label + ": ", NamedTextColor.GRAY))
                .append(Component.text(value, valueColor))
                .build();
    }

    private static Component joinLines(Component... lines) {
        Component result = Component.empty();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                result = result.append(Component.newline());
            }
            result = result.append(lines[i]);
        }
        return result;
    }

    private ActionButton backButton(String tooltip, Runnable back) {
        return button(iconLabel(M_BACK, "Terug", NamedTextColor.GRAY), tooltip, 150, back);
    }

    private void showMenu(Player admin, String title, Component body, List<ActionButton> buttons, ActionButton exit) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle(title))
                        .body(List.of(DialogBody.plainMessage(body)))
                        .build())
                .type(DialogType.multiAction(buttons, exit, 2)));
        admin.showDialog(dialog);
    }

    /** The main menu, opened with /lowkey: four sections. */
    private void openMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_SERVER, "Server", NamedTextColor.WHITE),
                "Launch en server open of dicht.", 150, () -> openServerMenu(admin)));
        buttons.add(button(iconLabel(M_WORLD, "Wereld", NamedTextColor.WHITE),
                "Nether, grenzen, lobby en spawnpunten.", 150, () -> openWorldMenu(admin)));
        buttons.add(button(iconLabel(M_PLAYERS, "Spelers", NamedTextColor.WHITE),
                "Team, crew, grace en dood-status.", 150, () -> openPlayersMenu(admin)));
        buttons.add(button(iconLabel(M_MESSAGES, "Berichten", NamedTextColor.WHITE),
                "Eigen bericht en donatiebericht.", 150, () -> openMessagesMenu(admin)));
        ActionButton close = ActionButton.create(Component.text("Sluiten", NamedTextColor.GRAY),
                Component.text("Sluit dit menu."), 150, null);

        Component status = joinLines(
                statusLine("Server", serverClosed ? "gesloten" : "open",
                        serverClosed ? NamedTextColor.RED : NamedTextColor.GREEN),
                statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Nether", netherOpen ? "open" : "dicht",
                        netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Online", String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE));
        showMenu(admin, "LowkeySMP Beheer", status, buttons, close);
    }

    // ---- section: Server

    private void openServerMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable back = () -> openMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();

        if (!launched) {
            buttons.add(button(iconLabel(M_LAUNCH, "Server launchen", NamedTextColor.GREEN),
                    "Stuurt iedereen met een team naar zijn team spawn.", 150,
                    () -> openLaunchConfirm(admin, () -> openServerMenu(admin))));
        } else {
            buttons.add(button(iconLabel(M_LAUNCH, "Launch resetten", NamedTextColor.RED),
                    "Zet de launch terug (voor testen). Niemand wordt verplaatst.", 150, () -> confirm(admin,
                            "Launch resetten?",
                            "Het lobby-item zegt daarna weer dat de server nog niet open is.",
                            "Spelers in de gamewereld blijven daar.",
                            Component.text("Ja, resetten", NamedTextColor.RED),
                            () -> {
                                resetLaunch(admin);
                                openServerMenu(admin);
                            },
                            () -> openServerMenu(admin))));
        }

        if (serverClosed) {
            buttons.add(button(iconLabel(M_SERVER, "Server openen", NamedTextColor.GREEN),
                    "Maak de server weer open voor iedereen.", 150, () -> {
                        setServerClosed(false);
                        admin.sendMessage(Component.text("De server is weer open voor iedereen.", NamedTextColor.GREEN));
                        openServerMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_SERVER, "Server sluiten", NamedTextColor.RED),
                    "Kickt iedereen behalve de spelers bij closed-access.", 150, () -> confirm(admin,
                            "Server sluiten?",
                            "Iedereen behalve " + String.join(", ", getConfig().getStringList("closed-access")) + " wordt gekickt.",
                            "De server blijft dicht tot je hem weer opent.",
                            Component.text("Ja, sluiten", NamedTextColor.RED),
                            () -> {
                                if (!hasClosedAccess(admin.getName())) {
                                    admin.sendMessage(Component.text(
                                            "Je staat zelf niet in 'closed-access' in config.yml, dan zou je jezelf buitensluiten.",
                                            NamedTextColor.RED));
                                } else {
                                    setServerClosed(true);
                                    admin.sendMessage(Component.text("De server is nu gesloten.", NamedTextColor.GREEN));
                                }
                                openServerMenu(admin);
                            },
                            () -> openServerMenu(admin))));
        }

        Component body = joinLines(
                statusLine("Server", serverClosed ? "gesloten" : "open",
                        serverClosed ? NamedTextColor.RED : NamedTextColor.GREEN),
                statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Server", body, buttons, backButton("Terug naar het hoofdmenu.", back));
    }

    private void openLaunchConfirm(Player admin, Runnable back) {
        confirm(admin,
                "Server launchen?",
                "Iedereen met een team wordt naar zijn team spawn gestuurd, met de titel Veel succes!",
                "Dit kan niet ongedaan gemaakt worden.",
                Component.text("Ja, launch", NamedTextColor.GREEN),
                () -> launchServer(admin),
                back);
    }

    // ---- section: Wereld

    private void openWorldMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();

        if (netherOpen) {
            buttons.add(button(iconLabel(M_NETHER, "Nether sluiten", NamedTextColor.RED),
                    "Sluit de Nether weer. Wie erin zit kan er nog uit.", 150, () -> {
                        setNether(false);
                        openWorldMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_NETHER, "Nether openen", NamedTextColor.GREEN),
                    "Opent de Nether voor iedereen, met titel en bericht.", 150, () -> confirm(admin,
                            "Nether openen?",
                            "Iedereen krijgt de titel en het bericht dat de Nether open is.",
                            "Je kunt hem later weer sluiten.",
                            Component.text("Ja, openen", NamedTextColor.GREEN),
                            () -> {
                                setNether(true);
                                openWorldMenu(admin);
                            },
                            () -> openWorldMenu(admin))));
        }

        if (bordersDropped) {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen dicht", NamedTextColor.RED),
                    "Noord en Zuid zien daarna weer alleen hun eigen chat.", 150, () -> {
                        setBordersDropped(false);
                        openWorldMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen droppen", NamedTextColor.GREEN),
                    "Chat van Noord en Zuid wordt weer een gedeelde chat.", 150, () -> {
                        setBordersDropped(true);
                        openWorldMenu(admin);
                    }));
        }

        if (lobbyCreated && lobbyWorld() != null) {
            buttons.add(button(iconLabel(M_LOBBY, "Naar de lobby", NamedTextColor.WHITE),
                    "Teleporteert jou naar de lobby.", 150, () -> {
                        teleportAdminToLobby(admin);
                    }));
            buttons.add(button(iconLabel(M_LOBBY, "Lobby spawn zetten", NamedTextColor.WHITE),
                    "Zet het lobby spawnpunt op jouw plek. Je moet in de lobby staan.", 150, () -> {
                        setLobbySpawn(admin);
                        openWorldMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_LOBBY, "Lobby aanmaken", NamedTextColor.GREEN),
                    "Maakt de lege lobby wereld met een klein platform.", 150, () -> {
                        createLobby(admin);
                        openWorldMenu(admin);
                    }));
        }
        if (lobbyExists()) {
            buttons.add(button(iconLabel(M_LOBBY, "Lobby verwijderen", NamedTextColor.RED),
                    "Wist de hele lobby wereld, ook oude resten.", 150,
                    () -> openLobbyDeleteConfirm(admin, () -> openWorldMenu(admin))));
        }

        buttons.add(button(iconLabel(M_SPAWN_N, "Spawn Noord zetten", NamedTextColor.RED),
                "Zet het spawnpunt van Noord op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.NOORD);
                    openWorldMenu(admin);
                }));
        buttons.add(button(iconLabel(M_SPAWN_Z, "Spawn Zuid zetten", NamedTextColor.BLUE),
                "Zet het spawnpunt van Zuid op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.ZUID);
                    openWorldMenu(admin);
                }));
        buttons.add(button(iconLabel(M_MAIN, "Naar main eiland", NamedTextColor.WHITE),
                "Teleporteert jou naar de gamewereld (laatste plek of team spawn).", 150, () -> {
                    enterGame(admin, false, true);
                }));

        Component body = joinLines(
                statusLine("Nether", netherOpen ? "open" : "dicht",
                        netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Lobby", lobbyCreated ? "aangemaakt" : (lobbyExists() ? "oude rest" : "nog niet"),
                        lobbyCreated ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Noord", spawnNoord != null ? "ingesteld" : "niet ingesteld",
                        spawnNoord != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Zuid", spawnZuid != null ? "ingesteld" : "niet ingesteld",
                        spawnZuid != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Wereld", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    // ---- section: Spelers

    private void openPlayersMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_TEAM, "Team instellen", NamedTextColor.WHITE),
                "Zet een speler in Noord, Zuid of geen team.", 150,
                () -> pickPlayer(admin, "Team instellen", target -> pickTeam(admin, target))));
        buttons.add(button(iconLabel(M_CREW, "Crew badge", NamedTextColor.WHITE),
                "Geef of verwijder de crew-badge van een speler.", 150,
                () -> pickPlayer(admin, "Crew badge", target -> pickCrew(admin, target))));
        buttons.add(button(iconLabel(M_GRACE, "Grace instellen", NamedTextColor.WHITE),
                "Zet de grace tijd van een speler.", 150,
                () -> pickPlayer(admin, "Grace instellen", target -> pickGrace(admin, target))));
        buttons.add(button(iconLabel(M_REVIVE, "Dood-status weg", NamedTextColor.WHITE),
                "Verwijdert de uitgeschakeld-status van een speler (vooral voor testen).", 150,
                () -> pickPlayer(admin, "Dood-status verwijderen", target -> {
                    clearEliminated(target);
                    admin.sendMessage(Component.text(
                            "Dood-status van " + target.getName() + " is verwijderd.", NamedTextColor.GREEN));
                    openPlayersMenu(admin);
                })));
        Component body = Component.text("Kies wat je wilt aanpassen. Daarna kies je de speler.", NamedTextColor.GRAY);
        showMenu(admin, "Spelers", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    // ---- section: Berichten

    private void openMessagesMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_SAY, "Eigen bericht", NamedTextColor.WHITE),
                "Stuur een bericht met de LOWKEY badge naar iedereen.", 150, () -> openSayDialog(admin)));
        buttons.add(button(iconLabel(M_DONATE, "Donatiebericht", NamedTextColor.WHITE),
                "Stuurt meteen een willekeurig donatiebericht naar iedereen.", 150, () -> {
                    broadcastRandomDonationMessage();
                    openMessagesMenu(admin);
                }));
        Component body = Component.text("Berichten naar alle spelers, met de LOWKEY badge.", NamedTextColor.GRAY);
        showMenu(admin, "Berichten", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    /** A dialog with a text field: whatever you type goes to everybody with the LOWKEY badge. */
    private void openSayDialog(Player admin) {
        DialogInput input = DialogInput.text("message", Component.text("Bericht"))
                .width(300)
                .maxLength(200)
                .build();
        DialogAction send = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!clicker.hasPermission("lowkey.admin")) {
                return;
            }
            final String text = view.getText("message");
            getServer().getScheduler().runTask(this, () -> {
                if (text == null || text.isBlank()) {
                    clicker.sendMessage(Component.text("Typ eerst een bericht.", NamedTextColor.RED));
                } else {
                    broadcastMessage(text.trim());
                    clicker.sendMessage(Component.text("Bericht verstuurd.", NamedTextColor.GREEN));
                }
                openMessagesMenu(clicker);
            });
        }, CLICK_OPTIONS);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Eigen bericht"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Dit bericht gaat naar iedereen, met de LOWKEY badge ervoor.", NamedTextColor.GRAY))))
                        .inputs(List.of(input))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_SAY, "Versturen", NamedTextColor.GREEN),
                                Component.text("Stuur het bericht naar iedereen."), 150, send),
                        backButton("Terug naar berichten.", () -> openMessagesMenu(admin)))));
        admin.showDialog(dialog);
    }

    // ---- shared screens

    /** A yes / no screen. "Annuleren" runs `back`. */
    private void confirm(Player admin, String title, String line1, String line2, Component yesLabel,
                         Runnable yes, Runnable back) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text(title, NamedTextColor.RED, TextDecoration.BOLD))
                        .body(List.of(
                                DialogBody.plainMessage(Component.text(line1, NamedTextColor.WHITE)),
                                DialogBody.plainMessage(Component.text(line2, NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.confirmation(
                        button(yesLabel, "Bevestigen", 150, yes),
                        button(Component.text("Annuleren", NamedTextColor.GRAY), "Terug.", 150, back))));
        admin.showDialog(dialog);
    }

    /** A list with one button per online player; clicking one continues with `next`. */
    private void pickPlayer(Player admin, String title, Consumer<Player> next) {
        List<ActionButton> buttons = new ArrayList<>();
        for (Player online : getServer().getOnlinePlayers()) {
            buttons.add(button(Component.text(online.getName()), "Kies " + online.getName() + ".", 100, () -> {
                if (online.isOnline()) {
                    next.accept(online);
                } else {
                    admin.sendMessage(Component.text("Die speler is niet meer online.", NamedTextColor.RED));
                    openPlayersMenu(admin);
                }
            }));
        }
        ActionButton back = backButton("Terug naar spelers.", () -> openPlayersMenu(admin));

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle(title))
                        .body(List.of(DialogBody.plainMessage(Component.text("Kies een speler:", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 3)));
        admin.showDialog(dialog);
    }

    private void pickTeam(Player admin, Player target) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(teamButton(admin, target, Side.NOORD, "Noord", NamedTextColor.RED));
        buttons.add(teamButton(admin, target, Side.ZUID, "Zuid", NamedTextColor.BLUE));
        buttons.add(teamButton(admin, target, Side.NONE, "Geen team", NamedTextColor.GRAY));
        ActionButton back = backButton("Terug naar spelers.", () -> openPlayersMenu(admin));

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Team voor " + target.getName()))
                        .body(List.of(DialogBody.plainMessage(
                                Component.text("Nu: " + getSide(target).id, NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 3)));
        admin.showDialog(dialog);
    }

    private ActionButton teamButton(Player admin, Player target, Side side, String label, NamedTextColor color) {
        return button(Component.text(label, color), target.getName() + " in " + label + " zetten.", 100, () -> {
            setSide(target, side);
            admin.sendMessage(Component.text(target.getName() + " zit nu in team " + side.id + ".", NamedTextColor.GREEN));
            openPlayersMenu(admin);
        });
    }

    private void pickCrew(Player admin, Player target) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(Component.text("Crew geven", NamedTextColor.GOLD), target.getName() + " crew maken.", 100,
                () -> {
                    setCrew(admin, target, true);
                    openPlayersMenu(admin);
                }));
        buttons.add(button(Component.text("Crew verwijderen", NamedTextColor.GRAY),
                target.getName() + " geen crew meer maken.", 100, () -> {
                    setCrew(admin, target, false);
                    openPlayersMenu(admin);
                }));
        ActionButton back = backButton("Terug naar spelers.", () -> openPlayersMenu(admin));

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Crew badge voor " + target.getName()))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Nu: " + (isCrew(target) ? "crew" : "geen crew"), NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 2)));
        admin.showDialog(dialog);
    }

    private void pickGrace(Player admin, Player target) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(graceButton(admin, target, 0, "Beeindigen"));
        buttons.add(graceButton(admin, target, 10, "10 minuten"));
        buttons.add(graceButton(admin, target, 30, "30 minuten"));
        buttons.add(graceButton(admin, target, 60, "60 minuten"));
        ActionButton back = backButton("Terug naar spelers.", () -> openPlayersMenu(admin));

        long leftMinutes = (graceRemaining(target) + 59_999L) / 60_000L;
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Grace voor " + target.getName()))
                        .body(List.of(DialogBody.plainMessage(
                                Component.text("Nu nog: " + leftMinutes + " minuten", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 2)));
        admin.showDialog(dialog);
    }

    private ActionButton graceButton(Player admin, Player target, int minutes, String label) {
        return button(Component.text(label), "Zet de grace van " + target.getName() + " op " + minutes + " minuten.", 100, () -> {
            setGrace(target, minutes * 60_000L);
            admin.sendMessage(Component.text(
                    "Grace van " + target.getName() + " staat nu op " + minutes + " minuten.", NamedTextColor.GREEN));
            openPlayersMenu(admin);
        });
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
            options.add("menu");
            options.add("grace");
            options.add("team");
            options.add("crew");
            options.add("nether");
            options.add("server");
            options.add("border");
            options.add("revive");
            options.add("donate");
            options.add("launch");
            options.add("lobby");
            options.add("setspawn");
            options.add("main");
            options.add("say");
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("grace") || sub.equals("team") || sub.equals("revive") || sub.equals("crew")) {
                for (Player player : getServer().getOnlinePlayers()) {
                    options.add(player.getName());
                }
            } else if (sub.equals("nether") || sub.equals("server") || sub.equals("border")) {
                options.add("open");
                options.add("close");
            } else if (sub.equals("lobby")) {
                options.add("create");
                options.add("tp");
                options.add("setspawn");
                options.add("delete");
            } else if (sub.equals("setspawn")) {
                options.add("noord");
                options.add("zuid");
            } else if (sub.equals("launch")) {
                options.add("confirm");
                options.add("reset");
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
            } else if (sub.equals("crew")) {
                options.add("aan");
                options.add("uit");
            } else if (sub.equals("lobby") && args[1].equalsIgnoreCase("delete")) {
                options.add("confirm");
            }
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("say")) {
            return List.of();
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
