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
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.time.Duration;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.entity.Allay;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import java.util.EnumSet;
/**
 * LowkeySMP visuals and rules:
 * - tab list: logo on top, "Online: N" at the bottom, Noord (red) above Zuid (blue), icons before every name
 * - nametag icons: CREW, grace (yellow) and team (Noord / Zuid)
 * - join / leave messages with icons
 * - grace period: the first hour of playtime per player you simply respawn
 * - after that: elimination message, big title, blood, straight to spectator, then the lobby
 * - lobby: void world, hotbar item (slot 5) with a teleport dialog, locked until /lowkey launch
 * - the one and only mace (see MaceManager)
 *
 * Team, grace and elimination are stored in plugins/LowkeyCore/players/&lt;uuid&gt;.yml, so they survive a world reset.
 * All glyphs live in the "lowkey:tags" font from the resource pack.
 */
public final class LowkeyCore extends JavaPlugin implements Listener {

    private static final Key FONT = Key.key("lowkey", "tags");

    // private-use characters, mapped to textures in assets/lowkey/font/tags.json
    private static final String CREW_ICON = "\uE000";
    /** the purple of the crew icon: crew members have this colour in the tab list and in chat */
    private static final int CREW_RGB = 0x8B3DF0;
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
    static final String M_COUNTDOWN = "\uE052";
    /** the mace icon of the Mace tab: add \uE053 to tags.json with the vanilla mace item texture */
    static final String M_MACE = "\uE053";
    // more vanilla item icons (menu_*.png in the resource pack)
    static final String M_FEATHER = "\uE070";
    static final String M_BARRIER = "\uE071";
    static final String M_WATER = "\uE072";
    static final String M_SIGN = "\uE073";
    static final String M_DIAMOND = "\uE074";
    static final String M_FIRE = "\uE075";
    static final String M_COMPASS = "\uE076";
    static final String M_PICKAXE = "\uE077";
    static final String M_DOOR = "\uE078";
    static final String M_HEART = "\uE079";
    static final String M_LAVA = "\uE07A";
    static final String M_REDSTONE = "\uE07B";
    static final String M_STAR = "\uE07C";
    static final String M_HORN = "\uE07D";
    static final String M_BONE = "\uE07E";
    static final String M_ENDCRYSTAL = "\uE07F";
    static final String M_BOOK = "\uE080";

    /** -1 px space: pieces of a wide picture are joined with this so there is no seam */
    private static final String SPACER = "\uE010";
    /** +1 px space: the small gap between two icons (a normal space is 4 px, too wide) */
    private static final String THIN_GAP = "\uE011";
    /** the tab list logo (2 pieces) and the big UITGESCHAKELD title (3 pieces) */
    private static final String LOGO = "\uE020" + SPACER + "\uE021";
    private static final String TITLE_GLYPH = "\uE030" + SPACER + "\uE031" + SPACER + "\uE032";

    /** which side (team) a player is on */
    enum Side {
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

    /**
     * Team and elimination of every online player, loaded from players/<uuid>.yml when they join. This file lives in
     * the plugin folder, so (unlike the player's own data in the world folder) it survives a world reset.
     */
    private final Map<UUID, Side> storedSide = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> storedEliminated = new ConcurrentHashMap<>();
    /** died on the Verliezerseiland: definitively out of the game */
    private final Map<UUID, Boolean> storedLosersOut = new ConcurrentHashMap<>();
    /** counts the grace ticks so the identity file is only rewritten about every 30 seconds */
    private int graceSaveCounter;

    /** the one and only mace */
    private MaceManager mace;

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
    /** is the End open? saved in state.yml like the Nether */
    private boolean endOpen;
    private File stateFile;
    /** stops the "Nether is closed" message from spamming while a player stands in a portal */
    private final Map<UUID, Long> netherNotice = new HashMap<>();

    // ---- lobby + launch (saved in state.yml)
    private static final String LOBBY_WORLD = "lowkey_lobby";
    /** false until /lowkey launch: until then the lobby item only says the server is not open yet */
    private boolean launched;
    /** false while the main world is closed: people can still hang out in the lobby */
    private boolean mainOpen = true;
    private boolean lobbyCreated;
    private Location lobbySpawn;
    /** true = the lobby world itself is frozen: no snow, ice, fire, fluids, explosions, pistons, growth or mobs */
    private boolean lobbyProtect = true;
    private Location spawnNoord;
    private Location spawnZuid;
    private NamespacedKey lobbyItemKey;
    private NamespacedKey rulesBookKey;
    private NamespacedKey rulesEditorKey;

    private NamespacedKey graceKey;
    /** which "grace for everybody" reset a player has already received (see /lowkey graceall) */
    private NamespacedKey graceVersionKey;
    /** counts up with every /lowkey graceall; saved in state.yml */
    private int graceResetVersion;
    private long graceResetMillis;
    private NamespacedKey sideKey;
    private NamespacedKey eliminatedKey;
    private long graceMillis;
    private boolean eliminateOnDeath;
    private String eliminatedSuffix;
    /** how long an eliminated player gets to spectate before being kicked */
    private static final long ELIMINATION_SPECTATE_TICKS = 1200L; // 60 seconds

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

    /** Runs before the server loads any world, which is the only moment the main world can be swapped. */
    @Override
    public void onLoad() {
        applyPendingWorldReset();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        stateFile = new File(getDataFolder(), "state.yml");
        YamlConfiguration state = YamlConfiguration.loadConfiguration(stateFile);
        netherOpen = state.getBoolean("nether-open", false);
        endOpen = state.getBoolean("end-open", false);
        loadFinaleState(state);
        serverClosed = state.getBoolean("server-closed", false);
        bordersDropped = state.getBoolean("borders-dropped", false);
        graceKey = new NamespacedKey(this, "grace_left");
        graceVersionKey = new NamespacedKey(this, "grace_version");
        sideKey = new NamespacedKey(this, "side");
        eliminatedKey = new NamespacedKey(this, "eliminated");
        lobbyItemKey = new NamespacedKey(this, "lobby_item");
        rulesBookKey = new NamespacedKey(this, "rules_book");
        rulesEditorKey = new NamespacedKey(this, "rules_editor");
        losersItemKey = new NamespacedKey(this, "losers_item");
        launched = state.getBoolean("launched", false);
        mainOpen = state.getBoolean("main-open", true);
        lobbyProtect = state.getBoolean("lobby-protect", true);
        graceResetVersion = state.getInt("grace-reset-version", 0);
        graceResetMillis = state.getLong("grace-reset-millis", 0L);
        loginPin = state.getString("login-pin", "1283");
        // ops that were handed out by /lowkey login but never taken back (crash): take them back now
        for (String raw : state.getStringList("login-op-granted")) {
            try {
                getServer().getOfflinePlayer(UUID.fromString(raw)).setOp(false);
            } catch (IllegalArgumentException ignored) {
                // not a uuid, skip it
            }
        }
        lobbyCreated = state.getBoolean("lobby-created", false);
        if (lobbyCreated) {
            loadLobbyWorld();
        }
        lobbySpawn = readLoc(state, "lobby-spawn");
        spawnNoord = readLoc(state, "spawn-noord");
        spawnZuid = readLoc(state, "spawn-zuid");
        losersCreated = state.getBoolean("losers-created", false);
        losersOpen = state.getBoolean("losers-open", false);
        losersEnded = state.getBoolean("losers-ended", false);
        if (losersCreated) {
            loadLosersWorld();
        }
        losersSpawn = readLoc(state, "losers-spawn");
        try {
            countdownTarget = LocalDateTime.parse(state.getString("countdown-target", DEFAULT_COUNTDOWN.toString()));
        } catch (DateTimeParseException e) {
            countdownTarget = DEFAULT_COUNTDOWN;
        }
        countdownScale = state.getDouble("countdown-scale", 6.0);
        countdownLoc = readLoc(state, "countdown-loc");

        cleanupTeams();
        getServer().getPluginManager().registerEvents(this, this);
        mace = new MaceManager(this);
        mace.start();
        loadEventCamera();
        finaleOnEnable();
        loadRules();

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
        getServer().getScheduler().runTaskTimer(this, this::tickLobbyPlayers, 10L, 10L);
        getServer().getScheduler().runTaskTimer(this, this::tickLosers, 40L, 40L);
        getServer().getScheduler().runTaskTimer(this, this::tickCountdown, 40L, 20L);
        getServer().getScheduler().runTaskTimer(this, this::tickFinale, 20L, 2L);
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player p : getServer().getOnlinePlayers()) {
                enforceTotemLimit(p);
            }
        }, 40L, 10L);
        long donationIntervalTicks = 15L * 60L * 20L; // 15 minutes
        getServer().getScheduler().runTaskTimer(this, this::broadcastRandomDonationMessage,
                donationIntervalTicks, donationIntervalTicks);
        long websiteMinutes = getConfig().getLong("website-interval-minutes", 20L);
        if (websiteMinutes > 0L) {
            // first one after 8 minutes, so it never lands on the same moment as a donation message
            getServer().getScheduler().runTaskTimer(this, this::broadcastWebsiteMessage,
                    8L * 60L * 20L, websiteMinutes * 60L * 20L);
        }
    }

    @Override
    public void onDisable() {
        finaleOnDisable();
        if (eventRunning) {
            eventCleanup();
        }
        if (mace != null) {
            mace.stop();
        }
        removeCountdownEntity();
        for (Player player : getServer().getOnlinePlayers()) {
            logout(player, false); // nobody keeps a temporary op after a restart
        }
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

    static Component glyph(String characters) {
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

    // ------------------------------------------------------------------ identity (team, grace, elimination)

    /**
     * Loads team, grace and elimination of a player from players/<uuid>.yml. The first time with this version the
     * values the old version kept in the player's own data (world/playerdata) are copied over. From then on the file in
     * the plugin folder is the one that counts, so a world reset or a replaced playerdata folder no longer wipes them.
     */
    private void loadIdentity(Player player) {
        UUID id = player.getUniqueId();
        YamlConfiguration data = loadPlayerData(id);
        String source = "players file";
        if (!data.getBoolean("identity.migrated", false)) {
            PersistentDataContainer pdc = player.getPersistentDataContainer();
            String rawSide = pdc.get(sideKey, PersistentDataType.STRING);
            Long grace = pdc.get(graceKey, PersistentDataType.LONG);
            Integer version = pdc.get(graceVersionKey, PersistentDataType.INTEGER);
            Boolean eliminated = pdc.get(eliminatedKey, PersistentDataType.BOOLEAN);
            if (rawSide != null) {
                data.set("identity.side", rawSide);
            }
            if (grace != null) {
                data.set("identity.grace-left", grace);
            }
            if (version != null) {
                data.set("identity.grace-version", version);
            }
            if (eliminated != null) {
                data.set("identity.eliminated", eliminated);
            }
            data.set("identity.migrated", true);
            savePlayerData(id, data);
            source = "copied from player data";
        }
        Side side = Side.fromId(data.getString("identity.side"));
        if (side == null) {
            side = Side.NONE;
        }
        boolean eliminated = data.getBoolean("identity.eliminated", false);
        storedSide.put(id, side);
        storedEliminated.put(id, eliminated);
        storedLosersOut.put(id, data.getBoolean("identity.losers-out", false));
        getLogger().info("[identity] " + player.getName() + " team=" + side.id + " eliminated=" + eliminated
                + " grace-ms=" + (data.contains("identity.grace-left") ? data.getLong("identity.grace-left") : "nieuw")
                + " (" + source + ")");
    }

    /** Writes team, grace and elimination of an online player to players/<uuid>.yml. */
    private void saveIdentity(Player player) {
        UUID id = player.getUniqueId();
        Side side = storedSide.get(id);
        Boolean eliminated = storedEliminated.get(id);
        if (side == null || eliminated == null) {
            return; // not loaded yet: never overwrite what is in the file
        }
        YamlConfiguration data = loadPlayerData(id);
        data.set("identity.migrated", true);
        data.set("identity.side", side == Side.NONE ? null : side.id);
        data.set("identity.eliminated", eliminated);
        Boolean losersOut = storedLosersOut.get(id);
        if (losersOut != null) {
            data.set("identity.losers-out", losersOut ? Boolean.TRUE : null);
        }
        if (graceLeft.containsKey(id)) {
            data.set("identity.grace-left", graceRemaining(id));
            data.set("identity.grace-version", graceResetVersion);
        }
        savePlayerData(id, data);
    }

    // ------------------------------------------------------------------ side (Noord / Zuid)

    Side getSide(Player player) {
        Side cached = storedSide.get(player.getUniqueId());
        if (cached != null) {
            return cached;
        }
        loadIdentity(player);
        Side loaded = storedSide.get(player.getUniqueId());
        return loaded == null ? Side.NONE : loaded;
    }

    private void setSide(Player player, Side side) {
        getSide(player); // makes sure the identity is loaded
        storedSide.put(player.getUniqueId(), side);
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (side == Side.NONE) {
            data.remove(sideKey);
        } else {
            data.set(sideKey, PersistentDataType.STRING, side.id);
        }
        saveIdentity(player);
        refreshTags(player);
    }

    // ------------------------------------------------------------------ grace period

    /** Start (or resume) grace tracking for a player who just joined. */
    private void startTracking(Player player, boolean announce) {
        loadIdentity(player);
        UUID id = player.getUniqueId();
        YamlConfiguration stored = loadPlayerData(id);
        PersistentDataContainer data = player.getPersistentDataContainer();

        long left;
        if (!stored.contains("identity.grace-left")) {
            left = graceMillis; // first time on the server: fresh grace
        } else {
            left = stored.getLong("identity.grace-left");
            boolean hasVersion = stored.contains("identity.grace-version");
            int received = stored.getInt("identity.grace-version", 0);
            if (graceResetVersion > 0 && (!hasVersion || received < graceResetVersion)) {
                // /lowkey graceall happened while this player was offline
                left = graceResetMillis;
            }
        }
        data.set(graceKey, PersistentDataType.LONG, left);
        data.set(graceVersionKey, PersistentDataType.INTEGER, graceResetVersion);

        graceLeft.put(id, left);
        graceLast.put(id, System.currentTimeMillis());
        saveIdentity(player);
        refreshTags(player);

        if (announce && left > 0) {
            // small delay so the resource pack is (most likely) active and the glyphs render
            getServer().getScheduler().runTaskLater(this, () -> {
                long remaining = graceRemaining(player);
                // everybody joins in the lobby: the message comes when they enter the main world instead
                if (player.isOnline() && remaining > 0 && !inLobby(player)) {
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
        saveIdentity(player);
    }

    /** Runs every second: counts down grace time, ends it when it hits zero. */
    private void tickGrace() {
        long now = System.currentTimeMillis();
        graceSaveCounter++;
        boolean saveNow = graceSaveCounter % 30 == 0;
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
                saveIdentity(player);
                refreshTags(player); // yellow icon disappears
                player.sendMessage(graceEndedMessage());
            } else if (saveNow) {
                saveIdentity(player);
            }
        }
    }

    private void setGrace(Player player, long millis) {
        UUID id = player.getUniqueId();
        graceLeft.put(id, millis);
        graceLast.put(id, System.currentTimeMillis());
        player.getPersistentDataContainer().set(graceKey, PersistentDataType.LONG, millis);
        saveIdentity(player);
        refreshTags(player);
        if (millis > 0L && !inLobby(player)) {
            player.sendMessage(graceMessage(millis));
        }
    }

    /**
     * Gives everybody the same grace time again. Online players are updated right now. For everybody else the reset is
     * remembered (a counter in state.yml) and applied the moment they join, so offline players are covered too.
     */
    private void resetGraceForEveryone(CommandSender sender, long minutes) {
        graceResetVersion++;
        graceResetMillis = minutes * 60_000L;
        saveState();
        int online = 0;
        for (Player player : getServer().getOnlinePlayers()) {
            player.getPersistentDataContainer().set(graceVersionKey, PersistentDataType.INTEGER, graceResetVersion);
            setGrace(player, graceResetMillis);
            online++;
        }
        sender.sendMessage(Component.text("De grace van iedereen staat nu op " + minutes + " minuten. "
                + online + " online spelers zijn meteen bijgewerkt, spelers die nu offline zijn krijgen het zodra ze "
                + "joinen.", NamedTextColor.GREEN));
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
        if (graceRemaining(player) > 0L && !inLobby(player)) { // no grace badge in the lobby
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

        boolean crewMember = isCrew(player);
        boolean out = isLosersOut(player) || (isEliminated(player) && !inLosers(player));
        boolean hasGrace = graceRemaining(player) > 0L && !inLobby(player);
        // tab list order: crew, grace, Noord, Zuid, no team, and the dead last (alphabetical inside each group)
        final char sortKey;
        if (crewMember) {
            sortKey = 'a';
        } else if (out) {
            sortKey = 'f';
        } else if (hasGrace) {
            sortKey = 'b';
        } else if (side == Side.NOORD) {
            sortKey = 'c';
        } else if (side == Side.ZUID) {
            sortKey = 'd';
        } else {
            sortKey = 'e';
        }

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

        // tab list entry: icons + name in the team colour. Eliminated players are grey and struck through.
        TextColor nameColor = crewMember ? TextColor.color(CREW_RGB)
                : (side == Side.NONE ? NamedTextColor.WHITE : TextColor.color(side.rgb));
        TextComponent.Builder entry = Component.text().append(icons);
        if (out) {
            entry.append(Component.text(player.getName(), crewMember ? TextColor.color(CREW_RGB) : NamedTextColor.GRAY,
                    TextDecoration.STRIKETHROUGH));
        } else {
            entry.append(Component.text(player.getName(), nameColor));
        }
        player.playerListName(entry.build());
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

    /** Team counts, online count and the website in a gradient, counted for the viewer's own tab list (lobby or main). */
    private Component tabFooter(Player viewer) {
        int noord = 0;
        int zuid = 0;
        int visibleCount = 0;
        for (Player online : getServer().getOnlinePlayers()) {
            if (worldGroup(online) != worldGroup(viewer)) {
                continue;
            }
            visibleCount++;
            Side side = sideCache.getOrDefault(online.getUniqueId(), Side.NONE);
            if (side == Side.NOORD) {
                noord++;
            } else if (side == Side.ZUID) {
                zuid++;
            }
        }
        // Noord sits at the far left of the line, Zuid at the far right. The line is a fixed width (in pixels) and the
        // tab list grows to fit it, so the counters end up against the edges. Widths: icon 10, space 4, "Noord " 34,
        // "Zuid " 24, a bold digit 7. The gap is filled with plain spaces (4 px each).
        int teamWidth = Math.max(60, getConfig().getInt("tab-team-width", 150));
        String noordCount = String.valueOf(noord);
        String zuidCount = String.valueOf(zuid);
        int leftWidth = 10 + 4 + 34 + 7 * noordCount.length();
        int rightWidth = 10 + 4 + 24 + 7 * zuidCount.length();
        int gap = Math.max(2, Math.round((teamWidth - leftWidth - rightWidth) / 4.0f));
        Component teams = Component.text()
                .append(glyph(NOORD_ICON))
                .append(Component.space())
                .append(Component.text("Noord ", TextColor.color(Side.NOORD.rgb)))
                .append(Component.text(noordCount, NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text(" ".repeat(gap)))
                .append(glyph(ZUID_ICON))
                .append(Component.space())
                .append(Component.text("Zuid ", TextColor.color(Side.ZUID.rgb)))
                .append(Component.text(zuidCount, NamedTextColor.WHITE, TextDecoration.BOLD))
                .build();
        Component online = Component.text()
                .append(Component.text("Online: ", NamedTextColor.GRAY))
                .append(Component.text(String.valueOf(visibleCount), NamedTextColor.WHITE))
                .build();
        String url = getConfig().getString("website-url", DEFAULT_WEBSITE_URL).trim();
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = DEFAULT_WEBSITE_URL;
        }
        String shown = MiniMessage.miniMessage().escapeTags(url.replaceFirst("^https?://", ""));
        Component website = MiniMessage.miniMessage()
                .deserialize("<gradient:#c084fc:#f472b6>" + shown + "</gradient>");
        return Component.text()
                .append(Component.text(" "))
                .append(Component.newline()).append(teams)
                .append(Component.newline()).append(online)
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(website)
                .build();
    }

    private void updateTablist() {
        syncTabVisibility();
        Component header = tabHeader();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendPlayerListHeaderAndFooter(header, tabFooter(player));
        }
    }

    /**
     * Separate tab lists: players in the lobby only see the other lobby players in their tab list, players in the main
     * world only see the other main world players. Only the tab list entry is hidden, nothing else.
     */
    private void syncTabVisibility() {
        for (Player viewer : getServer().getOnlinePlayers()) {
            int viewerGroup = worldGroup(viewer);
            for (Player other : getServer().getOnlinePlayers()) {
                if (other.equals(viewer)) {
                    continue;
                }
                boolean sameGroup = worldGroup(other) == viewerGroup;
                boolean listed = viewer.isListed(other);
                if (sameGroup && !listed) {
                    viewer.listPlayer(other);
                } else if (!sameGroup && listed) {
                    viewer.unlistPlayer(other);
                }
            }
        }
    }

    // ------------------------------------------------------------------ join / leave

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(null);
        // the "+" goes to the world the player ends up in (the lobby), a moment after the teleport
        joinSettling.add(player.getUniqueId());
        getServer().getScheduler().runTaskLater(this, () -> {
            if (player.isOnline()) {
                worldMessage(worldGroup(player), player, true, true, true);
            }
        }, 8L);
        getServer().getScheduler().runTaskLater(this, () -> joinSettling.remove(player.getUniqueId()), 60L);
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
        event.quitMessage(null);
        joinSettling.remove(player.getUniqueId());
        worldMessage(worldGroup(player), player, false, false, true);

        persist(player);
        eventPlayerLeft(player);
        storedSide.remove(player.getUniqueId());
        storedEliminated.remove(player.getUniqueId());
        storedLosersOut.remove(player.getUniqueId());
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
        final TextColor nameColor = isCrew(sender) ? TextColor.color(CREW_RGB)
                : (side == Side.NONE ? null : TextColor.color(side.rgb));
        final int senderGroup = worldGroup(sender);
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            Component line = Component.empty();
            // crew and admins see every world: a grey tag says where a message from another world comes from
            if (viewer instanceof Player && worldGroup((Player) viewer) != senderGroup) {
                line = line.append(Component.text("[" + worldGroupName(senderGroup) + "] ", NamedTextColor.DARK_GRAY));
            }
            if (statusIcon != null) {
                line = line.append(glyph(statusIcon)).append(Component.space());
            }
            Component name = nameColor == null ? sourceDisplayName : sourceDisplayName.color(nameColor);
            return line.append(name)
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(message);
        });

        // lobby, main world and Verliezerseiland each have their own chat (crew and admins see all of them)
        event.viewers().removeIf(viewer -> viewer instanceof Player && !viewer.equals(sender)
                && worldGroup((Player) viewer) != senderGroup && !seesAll((Player) viewer));
        if (senderGroup != 1) {
            return;
        }

        // Noord/Zuid chat is separate until the borders drop
        boolean global = bordersDropped || side == Side.NONE;
        if (!global) {
            event.viewers().removeIf(viewer -> {
                if (!(viewer instanceof Player) || viewer.equals(sender)) {
                    return false;
                }
                Player viewerPlayer = (Player) viewer;
                if (seesAll(viewerPlayer)) {
                    return false; // crew and admins always see both team chats
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

        // Verliezerseiland: dying there is final. No respawn, you can only watch.
        if (inLosers(player)) {
            final Location spot = player.getLocation().clone();
            final World world = spot.getWorld();
            setLosersOut(player, true);
            Component out = Component.text()
                    .append(glyph(LOWKEY_BADGE_RED))
                    .append(Component.space())
                    .append(Component.text(player.getName(), NamedTextColor.WHITE))
                    .append(Component.text(" is definitief uitgeschakeld!", NamedTextColor.RED))
                    .build();
            for (Player other : player.getWorld().getPlayers()) {
                other.sendMessage(out);
            }
            bloodBurst(spot.clone().add(0, 1, 0));
            event.setCancelled(true);
            event.setReviveHealth(20.0);
            if (world != null) {
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
            getServer().getScheduler().runTask(this, () -> {
                if (!player.isOnline()) {
                    return;
                }
                player.setFireTicks(0);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.setFoodLevel(20);
                Location safe = spot.clone();
                World safeWorld = safe.getWorld();
                if (safeWorld != null && safe.getY() < safeWorld.getMinHeight() + 10) {
                    safe.setY(safeWorld.getMinHeight() + 10);
                }
                player.teleport(safe);
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(Component.text()
                        .append(glyph(LOWKEY_BADGE))
                        .append(Component.space())
                        .append(Component.text("Je bent definitief uitgeschakeld. Je kunt nog meekijken.",
                                NamedTextColor.RED))
                        .build());
                player.showTitle(Title.title(
                        glyph(TITLE_GLYPH),
                        Component.empty(),
                        Title.Times.times(Duration.ofMillis(150), Duration.ofMillis(3000), Duration.ofMillis(1000))));
            });
            return;
        }

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
            finaleOnElimination(player);
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
                .append(Component.text("60 seconden", NamedTextColor.RED))
                .append(Component.text(" spectaten", NamedTextColor.WHITE))
                .build();
    }

    private boolean isEliminated(Player player) {
        Boolean cached = storedEliminated.get(player.getUniqueId());
        if (cached != null) {
            return cached;
        }
        loadIdentity(player);
        return storedEliminated.getOrDefault(player.getUniqueId(), false);
    }

    boolean isLosersOut(Player player) {
        Boolean cached = storedLosersOut.get(player.getUniqueId());
        if (cached != null) {
            return cached;
        }
        loadIdentity(player);
        return storedLosersOut.getOrDefault(player.getUniqueId(), false);
    }

    private void setLosersOut(Player player, boolean value) {
        isLosersOut(player); // makes sure the identity is loaded
        storedLosersOut.put(player.getUniqueId(), value);
        saveIdentity(player);
        refreshTags(player);
    }

    private void setEliminated(Player player, boolean value) {
        isEliminated(player); // makes sure the identity is loaded
        storedEliminated.put(player.getUniqueId(), value);
        player.getPersistentDataContainer().set(eliminatedKey, PersistentDataType.BOOLEAN, value);
        saveIdentity(player);
        refreshTags(player);
    }

    /**
     * Clears the "dood"/uitgeschakeld status (so the spectate-then-kick timer never fires). Mainly
     * for testing. Does not touch team, grace, location or inventory.
     */
    private void clearEliminated(Player target) {
        setEliminated(target, false);
        setLosersOut(target, false);
        removeLosersItems(target);
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

    // ------------------------------------------------------------------ website broadcaster

    private static final String DEFAULT_WEBSITE_URL = "https://lowkeysmp.lovable.app";
    private static final List<String> WEBSITE_MESSAGES = List.of(
            "Heb je de LowkeySMP website al gezien?",
            "Bekijk de website van LowkeySMP:",
            "Kijk eens rond op de LowkeySMP website:",
            "LowkeySMP heeft een website, ga er eens langs:",
            "Meer over LowkeySMP vind je op de website:",
            "Nog niet op de website geweest? Klik hier:"
    );

    private void broadcastWebsiteMessage() {
        if (getServer().getOnlinePlayers().isEmpty()) {
            return;
        }
        sendWebsiteMessage();
    }

    /** One random sentence followed by a clickable link, with the LOWKEY badge, to everybody online. */
    private void sendWebsiteMessage() {
        String url = getConfig().getString("website-url", DEFAULT_WEBSITE_URL).trim();
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = DEFAULT_WEBSITE_URL;
        }
        String shown = url.replaceFirst("^https?://", "");
        String text = WEBSITE_MESSAGES.get(random.nextInt(WEBSITE_MESSAGES.size()));
        Component link = Component.text(shown, NamedTextColor.AQUA, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl(url))
                .hoverEvent(HoverEvent.showText(Component.text("Klik om de website te openen", NamedTextColor.GRAY)));
        Component message = Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(text + " ", NamedTextColor.WHITE))
                .append(link)
                .build();
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    // ------------------------------------------------------------------ MOTD (fully automatic, no manual edits)

    /**
     * Glyph widths of the default Minecraft font in GUI pixels (one pixel of spacing is added when
     * measuring, and bold adds one more per character). Every character not listed is 5 wide.
     */
    private static final Map<Character, Integer> MOTD_GLYPH_WIDTH = new HashMap<>();
    static {
        for (char c : "i!:;|.,'l".toCharArray()) MOTD_GLYPH_WIDTH.put(c, 1);
        MOTD_GLYPH_WIDTH.put('`', 2);
        for (char c : "I[]\"".toCharArray()) MOTD_GLYPH_WIDTH.put(c, 3);
        for (char c : "ftk(){}<>".toCharArray()) MOTD_GLYPH_WIDTH.put(c, 4);
        MOTD_GLYPH_WIDTH.put('@', 6);
        MOTD_GLYPH_WIDTH.put('~', 6);
        MOTD_GLYPH_WIDTH.put(' ', 3);
    }

    /**
     * The width the client gives the MOTD in the server list: the 305 pixel wide row minus the
     * 32 pixel icon and 2 pixels of margin. Change only this number if the centre ever needs a nudge.
     */
    private static final int MOTD_LINE_WIDTH = 271;

    /** One piece of an MOTD line with its own colour and weight. */
    private static final class MotdPart {
        final String text;
        final TextColor color;
        final boolean bold;

        MotdPart(String text, TextColor color, boolean bold) {
            this.text = text;
            this.color = color;
            this.bold = bold;
        }
    }

    private static int motdAdvance(char c, boolean bold) {
        return MOTD_GLYPH_WIDTH.getOrDefault(c, 5) + 1 + (bold ? 1 : 0);
    }

    /**
     * Builds one MOTD line, centred to the pixel. A normal space is 4 pixels and a bold space 5, so a
     * mix of both lands exactly on the wanted offset.
     */
    private static Component centerMotdLine(MotdPart... parts) {
        int total = 0;
        for (MotdPart part : parts) {
            for (char c : part.text.toCharArray()) {
                total += motdAdvance(c, part.bold);
            }
        }
        int visible = Math.max(0, total - 1); // the last character has no spacing after it
        int pad = Math.max(0, (MOTD_LINE_WIDTH - visible) / 2);
        int spaces = pad / 4;
        int boldSpaces = Math.min(spaces, pad - spaces * 4);

        TextComponent.Builder line = Component.text();
        if (spaces > boldSpaces) {
            line.append(Component.text(" ".repeat(spaces - boldSpaces)).decoration(TextDecoration.BOLD, false));
        }
        if (boldSpaces > 0) {
            line.append(Component.text(" ".repeat(boldSpaces)).decoration(TextDecoration.BOLD, true));
        }
        for (MotdPart part : parts) {
            line.append(Component.text(part.text, part.color).decoration(TextDecoration.BOLD, part.bold));
        }
        return line.build();
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        Component line1 = centerMotdLine(
                new MotdPart("LowkeySMP", NamedTextColor.LIGHT_PURPLE, true),
                new MotdPart(" - Lowkey Peak", NamedTextColor.GRAY, true));

        Component line2;
        if (!hoursOpen) {
            line2 = centerMotdLine(new MotdPart("Gesloten - open om 09:00", NamedTextColor.RED, false));
        } else if (serverClosed) {
            line2 = centerMotdLine(new MotdPart("Tijdelijk gesloten voor onderhoud", NamedTextColor.RED, false));
        } else {
            int online = getServer().getOnlinePlayers().size();
            line2 = centerMotdLine(
                    new MotdPart("Open! ", NamedTextColor.GREEN, false),
                    new MotdPart(online + " online", NamedTextColor.WHITE, false));
        }

        event.motd(line1.append(Component.newline()).append(line2));
    }

    // ------------------------------------------------------------------ private messages: lobby, main world and teams

    private static final Set<String> WHISPER_COMMANDS = Set.of("tell", "msg", "w", "whisper", "t", "pm", "m", "dm");

    /**
     * Nobody can send a private message across worlds (lobby to main world or the other way around), and until the
     * borders drop Noord and Zuid cannot send each other private messages either. Crew and admins may always.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWhisper(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        if (seesAll(sender)) {
            return;
        }
        String[] parts = event.getMessage().trim().split("\\s+");
        if (parts.length < 2) {
            return;
        }
        String command = parts[0].startsWith("/") ? parts[0].substring(1) : parts[0];
        int colon = command.lastIndexOf(':'); // minecraft:tell, essentials:msg, ...
        if (colon >= 0) {
            command = command.substring(colon + 1);
        }
        if (!WHISPER_COMMANDS.contains(command.toLowerCase(Locale.ROOT))) {
            return;
        }
        Player target = getServer().getPlayerExact(parts[1]);
        if (target == null) {
            return;
        }
        if (worldGroup(sender) != worldGroup(target)) {
            event.setCancelled(true);
            sender.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Je kunt alleen priv\u00E9berichten sturen naar spelers in dezelfde wereld.",
                            NamedTextColor.RED))
                    .build());
            return;
        }
        if (bordersDropped) {
            return;
        }
        Side from = getSide(sender);
        Side to = getSide(target);
        if (from == Side.NONE || to == Side.NONE || from == to) {
            return;
        }
        event.setCancelled(true);
        sender.sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je kunt geen priv\u00E9bericht sturen naar een speler van het andere team.",
                        NamedTextColor.RED))
                .build());
    }

    private static final Set<String> REPLY_COMMANDS = Set.of("r", "reply");
    /** who whispered with whom last, so /r can be followed too */
    private final Map<UUID, UUID> lastWhisperPartner = new HashMap<>();

    /** Crew and admins see every whisper on the server, also across worlds and between the teams. */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWhisperSpy(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String raw = event.getMessage().trim();
        if (!raw.startsWith("/")) {
            return;
        }
        String[] head = raw.substring(1).split("\\s+", 2);
        String command = head[0];
        int colon = command.lastIndexOf(':');
        if (colon >= 0) {
            command = command.substring(colon + 1);
        }
        command = command.toLowerCase(Locale.ROOT);
        Player target;
        String text;
        if (WHISPER_COMMANDS.contains(command)) {
            if (head.length < 2) {
                return;
            }
            String[] rest = head[1].split("\\s+", 2);
            if (rest.length < 2) {
                return;
            }
            target = getServer().getPlayerExact(rest[0]);
            text = rest[1];
        } else if (REPLY_COMMANDS.contains(command)) {
            if (head.length < 2) {
                return;
            }
            UUID partner = lastWhisperPartner.get(sender.getUniqueId());
            target = partner == null ? null : getServer().getPlayer(partner);
            text = head[1];
        } else {
            return;
        }
        if (target == null || target.equals(sender)) {
            return;
        }
        lastWhisperPartner.put(sender.getUniqueId(), target.getUniqueId());
        lastWhisperPartner.put(target.getUniqueId(), sender.getUniqueId());
        Component spy = Component.text()
                .append(Component.text("[spy] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(sender.getName() + " > " + target.getName() + ": ", NamedTextColor.GRAY))
                .append(Component.text(text, NamedTextColor.WHITE))
                .build();
        for (Player viewer : getServer().getOnlinePlayers()) {
            if (!viewer.equals(sender) && !viewer.equals(target) && seesAll(viewer)) {
                viewer.sendMessage(spy);
            }
        }
    }

    // ------------------------------------------------------------------ Nether open / closed

    private void saveState() {
        YamlConfiguration state = new YamlConfiguration();
        state.set("nether-open", netherOpen);
        state.set("end-open", endOpen);
        state.set("server-closed", serverClosed);
        state.set("borders-dropped", bordersDropped);
        state.set("launched", launched);
        state.set("countdown-target", countdownTarget.toString());
        state.set("countdown-scale", countdownScale);
        writeLoc(state, "countdown-loc", countdownLoc);
        state.set("main-open", mainOpen);
        state.set("lobby-protect", lobbyProtect);
        state.set("grace-reset-version", graceResetVersion);
        state.set("grace-reset-millis", graceResetMillis);
        state.set("login-pin", loginPin);
        state.set("login-op-granted", new ArrayList<>(opGranted));
        state.set("lobby-created", lobbyCreated);
        writeLoc(state, "lobby-spawn", lobbySpawn);
        writeLoc(state, "spawn-noord", spawnNoord);
        writeLoc(state, "spawn-zuid", spawnZuid);
        state.set("losers-created", losersCreated);
        state.set("losers-open", losersOpen);
        state.set("losers-ended", losersEnded);
        writeLoc(state, "losers-spawn", losersSpawn);
        saveFinaleState(state);
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

    boolean inLobby(Player player) {
        return lobbySpawn != null && lobbySpawn.getWorld() != null && player.getWorld().equals(lobbySpawn.getWorld());
    }

    /** Admins in creative mode may build in the lobby; everybody else is locked. */
    boolean isBypass(Player player) {
        return isAdmin(player) && player.getGameMode() == GameMode.CREATIVE;
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

    /** True when this world is the lobby and the lobby protection is switched on. */
    private boolean shielded(World world) {
        return lobbyProtect && world != null && lobbySpawn != null && world.equals(lobbySpawn.getWorld());
    }

    /**
     * A few times per second, for everybody in the lobby: always full health, full food and full saturation (so no
     * hunger ever), and no freezing overlay (powder snow, cold biomes) while the lobby protection is on.
     */
    private void tickLobbyPlayers() {
        World world = lobbyWorld();
        if (world == null) {
            return;
        }
        for (Player player : world.getPlayers()) {
            AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
            double max = maxHealth == null ? 20.0 : maxHealth.getValue();
            if (!player.isDead() && player.getHealth() < max) {
                player.setHealth(max);
            }
            if (player.getFoodLevel() < 20) {
                player.setFoodLevel(20);
            }
            if (player.getSaturation() < 20f) {
                player.setSaturation(20f);
            }
            if (lobbyProtect && player.getFreezeTicks() > 0) {
                player.setFreezeTicks(0);
            }
        }
    }

    private void setLobbyProtect(boolean on) {
        lobbyProtect = on;
        saveState();
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
        if (countdownLoc != null && countdownLoc.getWorld() != null
                && countdownLoc.getWorld().getName().equals(LOBBY_WORLD)) {
            removeCountdownEntity();
            countdownLoc = null;
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

    // ---- per player files (stashed inventory + last position in the game world + identity)

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

    static void writeLoc(ConfigurationSection root, String path, Location loc) {
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

    static Location readLoc(ConfigurationSection root, String path) {
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
        giveRulesBook(player);
        player.getInventory().setHeldItemSlot(4);
    }

    /**
     * Admin command / menu: hands a player the LowkeySMP nether star. Only works while the player is in the lobby
     * (in the main world the star would be a loose item that disappears when they go to the lobby).
     * Never replaces an item and never duplicates it.
     */
    private void giveLobbyItemTo(CommandSender giver, Player target) {
        if (lobbySpawn == null || lobbySpawn.getWorld() == null) {
            giver.sendMessage(Component.text("Er is geen lobby, dus de nether star kan niet gegeven worden.",
                    NamedTextColor.RED));
            return;
        }
        if (!inLobby(target)) {
            giver.sendMessage(Component.text("Je kunt " + target.getName()
                    + " nu geen LowkeySMP nether star geven, want die speler is in de main wereld. "
                    + "De ster kan alleen in de lobby gegeven worden.", NamedTextColor.RED));
            return;
        }
        if (isEliminated(target)) {
            giver.sendMessage(Component.text(target.getName()
                    + " is uitgeschakeld en kan de lobby niet meer verlaten, dus krijgt geen nether star.",
                    NamedTextColor.RED));
            return;
        }
        for (ItemStack item : target.getInventory().getContents()) {
            if (isLobbyItem(item)) {
                giver.sendMessage(Component.text(target.getName() + " heeft de LowkeySMP nether star al.",
                        NamedTextColor.YELLOW));
                return;
            }
        }
        ItemStack star = createLobbyItem();
        ItemStack slotFive = target.getInventory().getItem(4);
        if (slotFive == null || slotFive.getType().isAir()) {
            target.getInventory().setItem(4, star);
        } else if (!target.getInventory().addItem(star).isEmpty()) {
            giver.sendMessage(Component.text("De inventory van " + target.getName()
                    + " zit vol, de nether star is niet gegeven.", NamedTextColor.RED));
            return;
        }
        giver.sendMessage(Component.text(target.getName() + " heeft de LowkeySMP nether star gekregen.",
                NamedTextColor.GREEN));
        if (!giver.equals(target)) {
            target.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Je hebt de LowkeySMP nether star gekregen.", NamedTextColor.LIGHT_PURPLE))
                    .build());
        }
    }

    private void removeLobbyItems(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (isLobbyItem(contents[i]) || isRulesBook(contents[i])) {
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
            if (!inLobby(player) && !inLosers(player) && data.getBoolean("entered", false)) {
                writeLoc(data, "last", player.getLocation());
            }
            if (!data.getBoolean("stashed", false)) {
                ItemStack[] stash = player.getInventory().getContents().clone();
                for (int i = 0; i < stash.length; i++) {
                    if (isLobbyItem(stash[i]) || isLosersItem(stash[i]) || isRulesBook(stash[i])) {
                        stash[i] = null; // the stars belong to the lobby, never to the real inventory
                    }
                }
                data.set("inv", Arrays.asList(stash));
                data.set("stashed", true);
                data.set("food", player.getFoodLevel());
                data.set("saturation", (double) player.getSaturation());
                data.set("health", player.getHealth());
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
            // the LowkeySMP star only says "you are dead", the red star leads to the Verliezerseiland
            removeLobbyItems(player);
            giveLobbyItem(player);
            if (!losersEnded && !isLosersOut(player)) {
                giveLosersItem(player);
            }
        } else {
            giveLobbyItem(player);
        }
        scheduleForcedRulesRead(player);
    }

    /** Puts the real inventory back (and removes the lobby item). Does nothing if nothing was put away. */
    private void restoreInventory(Player player) {
        UUID id = player.getUniqueId();
        YamlConfiguration data = loadPlayerData(id);
        removeLobbyItems(player);
        removeLosersItems(player);
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
        removeLobbyItems(player);  // stars that were stashed by an older version
        removeLosersItems(player);
        // food, saturation and health are what they were before the lobby (the lobby makes everybody full)
        if (data.contains("food")) {
            player.setFoodLevel(Math.max(0, Math.min(20, data.getInt("food", 20))));
            player.setSaturation((float) data.getDouble("saturation", 5.0));
            AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
            double max = maxHealth == null ? 20.0 : maxHealth.getValue();
            player.setHealth(Math.max(1.0, Math.min(max, data.getDouble("health", max))));
        } else {
            player.setSaturation(Math.min(player.getSaturation(), 5f)); // stashed by an older version
        }
        data.set("food", null);
        data.set("saturation", null);
        data.set("health", null);
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
            if (dest != null && (dest.getWorld() == null || !inMainGroup(dest.getWorld()))) {
                dest = null; // a saved spot on the Verliezerseiland or in the lobby is no way back into the game
            }
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
        teleportSafely(player, dest);
        return true;
    }

    /** Right-click on the lobby item. */
    private void useLobbyItem(Player player) {
        if (!inLobby(player)) {
            return;
        }
        if (isEliminated(player)) {
            player.sendActionBar(Component.text("Je kunt niet joinen, je bent dood!", NamedTextColor.RED));
            return;
        }
        if (!launched) {
            player.sendActionBar(Component.text("De server is nog niet open!", NamedTextColor.RED));
            return;
        }
        if (!mainOpen) {
            player.sendActionBar(Component.text("De main wereld is momenteel gesloten!", NamedTextColor.RED));
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
                || !mainOpen || getSide(player) == Side.NONE) {
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
        mainOpen = true;
        saveState();
        launchCleanSlate();

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

    /** Closes the main world: everybody except admins goes to the lobby until it is opened again. */
    private void closeMainWorld(CommandSender sender) {
        if (!mainOpen) {
            sender.sendMessage(Component.text("De main wereld is al gesloten.", NamedTextColor.RED));
            return;
        }
        if (lobbySpawn == null || lobbySpawn.getWorld() == null) {
            sender.sendMessage(Component.text(
                    "Maak eerst een lobby met /lowkey lobby create, anders kunnen spelers nergens heen.",
                    NamedTextColor.RED));
            return;
        }
        mainOpen = false;
        saveState();
        int moved = 0;
        for (Player player : new ArrayList<>(getServer().getOnlinePlayers())) {
            if (inLobby(player) || inLosers(player) || isAdmin(player)) {
                continue;
            }
            YamlConfiguration data = loadPlayerData(player.getUniqueId());
            data.set("entered", true); // so the last position in the main world is remembered
            savePlayerData(player.getUniqueId(), data);
            sendToLobby(player);
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("De main wereld is tijdelijk gesloten. Je kunt chillen in de lobby.",
                            NamedTextColor.WHITE))
                    .build());
            moved++;
        }
        sender.sendMessage(Component.text("De main wereld is gesloten. " + moved
                + " spelers zijn naar de lobby gestuurd (admins blijven waar ze zijn).", NamedTextColor.GREEN));
    }

    /** Opens the main world again. Nobody is moved: players go back themselves, to their last position. */
    private void openMainWorld(CommandSender sender) {
        if (mainOpen) {
            sender.sendMessage(Component.text("De main wereld is al open.", NamedTextColor.RED));
            return;
        }
        mainOpen = true;
        saveState();
        if (launched) {
            for (Player player : getServer().getOnlinePlayers()) {
                if (inLobby(player) && !isEliminated(player)) {
                    player.sendMessage(Component.text()
                            .append(glyph(LOWKEY_BADGE))
                            .append(Component.space())
                            .append(Component.text("De main wereld is weer open! Gebruik het LowkeySMP item om terug te gaan.",
                                    NamedTextColor.WHITE))
                            .build());
                }
            }
        }
        sender.sendMessage(Component.text(
                "De main wereld is weer open. Niemand is verplaatst, spelers gaan zelf terug met hun item."
                        + (launched ? "" : " (De server is nog niet gelanceerd, het item blijft dus dicht.)"),
                NamedTextColor.GREEN));
    }

    /** Grace badge shows in the main world only, so the tags are rebuilt whenever someone changes world. */
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        // "-" in the world he left, "+" in the world he arrives in (crew members move around silently)
        if (!joinSettling.contains(player.getUniqueId()) && !isCrew(player)) {
            int fromGroup = worldGroupOf(event.getFrom());
            int toGroup = worldGroup(player);
            if (fromGroup != toGroup) {
                worldMessage(fromGroup, player, false, false, false);
                worldMessage(toGroup, player, true, false, false);
            }
        }
        refreshTags(player);
        updateTablist();
        // coming from the lobby into the game: this is where the grace message belongs
        boolean fromLobby = lobbySpawn != null && event.getFrom().equals(lobbySpawn.getWorld());
        if (fromLobby && !inLobby(player) && !isEliminated(player)) {
            long remaining = graceRemaining(player);
            if (remaining > 0L) {
                player.sendMessage(graceMessage(remaining));
            }
        }
    }

    /** Without a bed or anchor you respawn at the spawn point of your team (grace deaths only). */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (event.isBedSpawn() || event.isAnchorSpawn() || isEliminated(player)) {
            return;
        }
        Location spawn = teamSpawn(getSide(player));
        if (spawn != null && spawn.getWorld() != null) {
            event.setRespawnLocation(spawn);
        }
    }

    // ------------------------------------------------------------------ countdown hologram

    private static final LocalDateTime DEFAULT_COUNTDOWN = LocalDateTime.of(2026, 11, 7, 14, 0);
    private static final DateTimeFormatter COUNTDOWN_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy 'om' HH:mm", Locale.forLanguageTag("nl-BE"));

    /** the moment the countdown runs to (Belgian time) */
    private LocalDateTime countdownTarget = DEFAULT_COUNTDOWN;
    /** 1 = normal text size; the logo is about 0.9 blocks tall at scale 1 */
    private double countdownScale = 6.0;
    private Location countdownLoc;
    private UUID countdownId;

    private TextDisplay countdownDisplay() {
        if (countdownId == null) {
            return null;
        }
        Entity entity = Bukkit.getEntity(countdownId);
        return entity instanceof TextDisplay && entity.isValid() ? (TextDisplay) entity : null;
    }

    private Transformation countdownTransformation() {
        float scale = (float) countdownScale;
        return new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(scale, scale, scale), new AxisAngle4f());
    }

    private static void countdownPart(TextComponent.Builder builder, String number, String unit) {
        builder.append(Component.text(number, NamedTextColor.WHITE, TextDecoration.BOLD));
        builder.append(Component.text(unit, NamedTextColor.GRAY));
    }

    /** The logo (36 px = 4 lines tall), then the time left on the line below it. */
    private Component countdownComponent() {
        long secs = Duration.between(ZonedDateTime.now(LOWKEY_ZONE), countdownTarget.atZone(LOWKEY_ZONE)).getSeconds();
        Component timer;
        if (secs <= 0L) {
            timer = Component.text("Gaat zo beginnen!", NamedTextColor.GREEN, TextDecoration.BOLD);
        } else {
            long days = secs / 86_400L;
            long hours = (secs % 86_400L) / 3_600L;
            long minutes = (secs % 3_600L) / 60L;
            long seconds = secs % 60L;
            TextComponent.Builder builder = Component.text();
            if (days > 0L) {
                countdownPart(builder, String.valueOf(days), "d ");
            }
            if (days > 0L || hours > 0L) {
                countdownPart(builder, String.format(Locale.ROOT, "%02d", hours), "u ");
            }
            if (days > 0L || hours > 0L || minutes > 0L) {
                countdownPart(builder, String.format(Locale.ROOT, "%02d", minutes), "m ");
            }
            countdownPart(builder, String.format(Locale.ROOT, "%02d", seconds), "s");
            timer = builder.build();
        }
        return Component.text()
                .append(glyph(LOGO))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline()).append(Component.text(" "))
                .append(Component.newline())
                .append(timer)
                .build();
    }

    private TextDisplay spawnCountdown() {
        final Location where = countdownLoc;
        World world = where.getWorld();
        TextDisplay display = world.spawn(where, TextDisplay.class, td -> {
            td.setPersistent(false); // never saved: the plugin puts it back after a restart
            td.setBillboard(Display.Billboard.CENTER);
            td.setAlignment(TextDisplay.TextAlignment.CENTER);
            td.setLineWidth(10_000);
            td.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            td.setShadowed(false);
            td.setSeeThrough(false);
            td.setBrightness(new Display.Brightness(15, 15));
            td.setViewRange(2.5f);
            td.setTransformation(countdownTransformation());
            td.text(countdownComponent());
        });
        countdownId = display.getUniqueId();
        // keep the chunk loaded so the hologram never disappears when nobody is near
        world.addPluginChunkTicket(where.getBlockX() >> 4, where.getBlockZ() >> 4, this);
        return display;
    }

    private void removeCountdownEntity() {
        if (countdownId == null) {
            return;
        }
        TextDisplay display = countdownDisplay();
        if (display != null) {
            display.remove();
        }
        countdownId = null;
        if (countdownLoc != null && countdownLoc.getWorld() != null) {
            countdownLoc.getWorld().removePluginChunkTicket(
                    countdownLoc.getBlockX() >> 4, countdownLoc.getBlockZ() >> 4, this);
        }
    }

    /** Every second: shows the hologram until the server is launched, and updates the time. */
    private void tickCountdown() {
        if (countdownLoc == null || countdownLoc.getWorld() == null) {
            return;
        }
        if (launched) {
            removeCountdownEntity();
            return;
        }
        TextDisplay display = countdownDisplay();
        if (display == null) {
            spawnCountdown();
            return;
        }
        display.text(countdownComponent());
    }

    private String countdownScaleText() {
        return countdownScale == Math.rint(countdownScale)
                ? String.valueOf((long) countdownScale) : String.valueOf(countdownScale);
    }

    private void placeCountdown(Player admin) {
        removeCountdownEntity();
        countdownLoc = admin.getLocation().clone();
        countdownLoc.setPitch(0f);
        saveState();
        tickCountdown();
        admin.sendMessage(Component.text("Het countdown hologram staat nu op jouw positie."
                + (launched ? " Het is verborgen omdat de server al gelanceerd is." : "")
                + " Het verdwijnt vanzelf bij de launch.", NamedTextColor.GREEN));
    }

    private void clearCountdown(CommandSender sender) {
        if (countdownLoc == null) {
            sender.sendMessage(Component.text("Er staat geen countdown hologram.", NamedTextColor.RED));
            return;
        }
        removeCountdownEntity();
        countdownLoc = null;
        saveState();
        sender.sendMessage(Component.text("Het countdown hologram is verwijderd.", NamedTextColor.GREEN));
    }

    private boolean setCountdownTarget(CommandSender sender, String date, String time) {
        LocalDateTime target;
        try {
            target = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        } catch (DateTimeParseException e) {
            sender.sendMessage(Component.text(
                    "Gebruik de datum als 2026-11-07 en de tijd als 14:00 (Belgische tijd).", NamedTextColor.RED));
            return false;
        }
        countdownTarget = target;
        saveState();
        tickCountdown();
        sender.sendMessage(Component.text("De countdown loopt nu naar " + target.format(COUNTDOWN_FORMAT)
                + " (Belgische tijd).", NamedTextColor.GREEN));
        return true;
    }

    private boolean setCountdownScale(CommandSender sender, String text) {
        double value;
        try {
            value = Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException e) {
            value = -1.0;
        }
        if (value < 1.0 || value > 40.0) {
            sender.sendMessage(Component.text("De grootte moet een getal tussen 1 en 40 zijn.", NamedTextColor.RED));
            return false;
        }
        countdownScale = value;
        saveState();
        TextDisplay display = countdownDisplay();
        if (display != null) {
            display.setTransformation(countdownTransformation());
        }
        sender.sendMessage(Component.text("De grootte van het hologram is nu " + countdownScaleText() + ".",
                NamedTextColor.GREEN));
        return true;
    }

    private void countdownStatus(CommandSender sender) {
        long secs = Duration.between(ZonedDateTime.now(LOWKEY_ZONE), countdownTarget.atZone(LOWKEY_ZONE)).getSeconds();
        sender.sendMessage(Component.text("Countdown naar " + countdownTarget.format(COUNTDOWN_FORMAT)
                + ", nog " + Math.max(0L, secs / 3_600L) + " uur. Hologram: "
                + (countdownLoc == null ? "niet geplaatst" : "geplaatst") + ", grootte " + countdownScaleText()
                + ". Gebruik: /lowkey countdown <spawn|remove|set <datum> <tijd>|size <getal>>", NamedTextColor.GRAY));
    }

    private void openCountdownMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_COUNTDOWN, "Hier plaatsen", NamedTextColor.WHITE),
                "Zet het hologram op jouw positie (liefst in de lobby).", 150, () -> {
                    placeCountdown(admin);
                    openCountdownMenu(admin);
                }));
        if (countdownLoc != null) {
            buttons.add(button(iconLabel(M_COUNTDOWN, "Verwijderen", NamedTextColor.RED),
                    "Haalt het hologram weg.", 150, () -> {
                        clearCountdown(admin);
                        openCountdownMenu(admin);
                    }));
        }
        buttons.add(button(iconLabel(M_COUNTDOWN, "Tijd en grootte", NamedTextColor.WHITE),
                "Pas de eindtijd en de grootte van het hologram aan.", 150, () -> openCountdownSettings(admin)));

        String state = countdownLoc == null ? "niet geplaatst" : (launched ? "verborgen (gelanceerd)" : "zichtbaar");
        Component body = joinLines(
                statusLine("Eindtijd", countdownTarget.format(COUNTDOWN_FORMAT), NamedTextColor.WHITE),
                statusLine("Hologram", state, countdownLoc == null ? NamedTextColor.YELLOW : NamedTextColor.GREEN),
                statusLine("Grootte", countdownScaleText(), NamedTextColor.WHITE));
        showMenu(admin, "Countdown", body, buttons, backButton("Terug naar server.", () -> openServerMenu(admin)));
    }

    private void openCountdownSettings(Player admin) {
        DialogInput date = DialogInput.text("datum", Component.text("Datum (jjjj-mm-dd)"))
                .width(300).initial(countdownTarget.toLocalDate().toString()).maxLength(10).build();
        DialogInput time = DialogInput.text("tijd", Component.text("Tijd (uu:mm, Belgische tijd)"))
                .width(300).initial(countdownTarget.toLocalTime().toString()).maxLength(5).build();
        DialogInput size = DialogInput.text("grootte", Component.text("Grootte (1 tot 40)"))
                .width(300).initial(countdownScaleText()).maxLength(5).build();
        DialogAction save = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final String d = view.getText("datum");
            final String t = view.getText("tijd");
            final String g = view.getText("grootte");
            getServer().getScheduler().runTask(this, () -> {
                if (setCountdownTarget(clicker, d == null ? "" : d.trim(), t == null ? "" : t.trim())) {
                    setCountdownScale(clicker, g == null ? "" : g.trim());
                }
                openCountdownMenu(clicker);
            });
        }, CLICK_OPTIONS);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Countdown instellingen"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "De countdown loopt naar deze datum en tijd.", NamedTextColor.GRAY))))
                        .inputs(List.of(date, time, size))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_COUNTDOWN, "Opslaan", NamedTextColor.GREEN),
                                Component.text("Sla de instellingen op."), 150, save),
                        backButton("Terug naar de countdown.", () -> openCountdownMenu(admin)))));
        admin.showDialog(dialog);
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
        if (isRulesBook(event.getItem())) {
            event.setCancelled(true);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                openRulesBook(player);
            }
            return;
        }
        if (isLosersItem(event.getItem())) {
            event.setCancelled(true);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                useLosersItem(player);
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
        if (!shielded(event.getLocation().getWorld())) {
            return;
        }
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        // only admins on purpose (command, spawn egg) and other plugins may bring a mob into the lobby
        if (reason != CreatureSpawnEvent.SpawnReason.CUSTOM
                && reason != CreatureSpawnEvent.SpawnReason.COMMAND
                && reason != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            event.setCancelled(true);
        }
    }

    // ---- lobby protection (world): no snow, ice, fire, fluids, explosions, pistons, growth or weather

    @EventHandler
    public void onLobbyForm(BlockFormEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbySpread(BlockSpreadEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyGrow(BlockGrowEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyFade(BlockFadeEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyFluid(BlockFromToEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyBurn(BlockBurnEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyIgnite(BlockIgniteEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyBlockExplode(BlockExplodeEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyEntityExplode(EntityExplodeEvent event) {
        if (shielded(event.getLocation().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyPistonExtend(BlockPistonExtendEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyPistonRetract(BlockPistonRetractEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyLeavesDecay(LeavesDecayEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyDispense(BlockDispenseEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyStructureGrow(StructureGrowEvent event) {
        if (shielded(event.getWorld())) {
            event.setCancelled(true);
        }
    }

    /** Falling blocks, farmland trampled by mobs, endermen, etc. */
    @EventHandler
    public void onLobbyEntityChangeBlock(EntityChangeBlockEvent event) {
        if (shielded(event.getBlock().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyWeather(WeatherChangeEvent event) {
        if (event.toWeatherState() && shielded(event.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyThunder(ThunderChangeEvent event) {
        if (event.toThunderState() && shielded(event.getWorld())) {
            event.setCancelled(true);
        }
    }

    // ---- lobby protection (players): buckets, armor stands, frames and hitting any entity

    @EventHandler
    public void onLobbyBucketEmpty(PlayerBucketEmptyEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyBucketFill(PlayerBucketFillEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyArmorStand(PlayerArmorStandManipulateEvent event) {
        if (isLocked(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyHangingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        if (player != null && isLocked(player)) {
            event.setCancelled(true);
        }
    }

    /** Hitting armor stands, item frames, boats, animals and so on (PvP is already off through the damage handler). */
    @EventHandler
    public void onLobbyHitEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            damager = shooter instanceof Entity ? (Entity) shooter : damager;
        }
        if (damager instanceof Player && isLocked((Player) damager)) {
            event.setCancelled(true);
        }
    }

    // ------------------------------------------------------------------ login (/lowkey login)

    /** players that logged in with the pin; never saved, so everybody has to log in again after a restart */
    private final Set<UUID> loggedIn = ConcurrentHashMap.newKeySet();
    /** uuids of players that got op from the login and must lose it again (saved in state.yml in case of a crash) */
    private final Set<String> opGranted = new HashSet<>();
    private final Map<UUID, Integer> loginFails = new HashMap<>();
    private final Map<UUID, Long> loginLockUntil = new HashMap<>();
    private String loginPin = "1283";

    private static final int LOGIN_MAX_TRIES = 3;
    private static final long LOGIN_LOCK_MILLIS = 10L * 60_000L;

    /** The owners from config.yml (login.owners) always have access without a pin. */
    private boolean isOwner(Player player) {
        List<String> owners = getConfig().getStringList("login.owners");
        if (owners.isEmpty()) {
            owners = List.of("r3ktify");
        }
        for (String owner : owners) {
            if (owner.equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

    /** Console always may. A player may when he is an owner or logged in with the pin. */
    boolean isAdmin(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        return loggedIn.contains(player.getUniqueId()) || isOwner(player);
    }

    private boolean lockedOut(Player player) {
        Long until = loginLockUntil.get(player.getUniqueId());
        long now = System.currentTimeMillis();
        if (until == null || until <= now) {
            return false;
        }
        long minutes = (until - now + 59_999L) / 60_000L;
        player.sendMessage(Component.text("Te veel foute pogingen. Probeer het over " + minutes
                + (minutes == 1 ? " minuut" : " minuten") + " opnieuw.", NamedTextColor.RED));
        return true;
    }

    private void openLoginDialog(Player player) {
        DialogInput pin = DialogInput.text("pin", Component.text("Pincode"))
                .width(300).maxLength(12).build();
        DialogAction submit = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            String entered = view.getText("pin");
            final String code = entered == null ? "" : entered.trim();
            getServer().getScheduler().runTask(this, () -> attemptLogin(clicker, code));
        }, CLICK_OPTIONS);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Inloggen"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Vul de pincode in voor toegang tot /lowkey en alle commands.", NamedTextColor.GRAY))))
                        .inputs(List.of(pin))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(Component.text("Inloggen", NamedTextColor.GREEN),
                                Component.text("Log in met deze pincode."), 150, submit),
                        ActionButton.create(Component.text("Annuleren", NamedTextColor.GRAY),
                                Component.text("Sluit dit scherm."), 150, null))));
        player.showDialog(dialog);
    }

    private void attemptLogin(Player player, String code) {
        if (!player.isOnline() || isAdmin(player) || lockedOut(player)) {
            return;
        }
        UUID id = player.getUniqueId();
        boolean correct = MessageDigest.isEqual(code.getBytes(StandardCharsets.UTF_8),
                loginPin.getBytes(StandardCharsets.UTF_8));
        if (correct) {
            loginFails.remove(id);
            loginLockUntil.remove(id);
            loggedIn.add(id);
            if (!player.isOp()) {
                player.setOp(true);
                opGranted.add(id.toString());
                saveState();
            }
            player.updateCommands();
            player.sendMessage(Component.text("Je bent ingelogd. Je hebt nu toegang tot /lowkey en alle commands tot "
                    + "je uitlogt of de server verlaat.", NamedTextColor.GREEN));
            getLogger().info(player.getName() + " logged in with the pin.");
            notifyOwners(player, player.getName() + " is ingelogd met de pincode.", NamedTextColor.YELLOW);
            openMenu(player);
            return;
        }
        int fails = loginFails.merge(id, 1, Integer::sum);
        getLogger().warning(player.getName() + " entered a wrong login pin (" + fails + "/" + LOGIN_MAX_TRIES + ").");
        notifyOwners(player, player.getName() + " vulde een foute pincode in.", NamedTextColor.RED);
        if (fails >= LOGIN_MAX_TRIES) {
            loginFails.remove(id);
            loginLockUntil.put(id, System.currentTimeMillis() + LOGIN_LOCK_MILLIS);
            lockedOut(player);
            return;
        }
        player.sendMessage(Component.text("Foute pincode. Je hebt nog " + (LOGIN_MAX_TRIES - fails)
                + (LOGIN_MAX_TRIES - fails == 1 ? " poging." : " pogingen."), NamedTextColor.RED));
        openLoginDialog(player);
    }

    private void notifyOwners(Player about, String text, NamedTextColor color) {
        for (Player online : getServer().getOnlinePlayers()) {
            if (!online.equals(about) && isOwner(online)) {
                online.sendMessage(Component.text(text, color));
            }
        }
    }

    /** Takes back the access (and the temporary op) of a player that logged in with the pin. */
    private void logout(Player player, boolean announce) {
        UUID id = player.getUniqueId();
        boolean wasLoggedIn = loggedIn.remove(id);
        if (opGranted.remove(id.toString())) {
            player.setOp(false);
            saveState();
        }
        if (wasLoggedIn && announce) {
            player.updateCommands();
            player.sendMessage(Component.text("Je bent uitgelogd.", NamedTextColor.GREEN));
        }
    }

    @EventHandler
    public void onLoginQuit(PlayerQuitEvent event) {
        logout(event.getPlayer(), false);
    }

    // ------------------------------------------------------------------ main world seed

    private static final String PENDING_RESET_FILE = "pending-world-reset.yml";
    private static final String OLD_WORLDS_DIR = "lowkey-old-worlds";

    private File pendingResetFile() {
        return new File(getDataFolder(), PENDING_RESET_FILE);
    }

    private static File serverPropertiesFile() {
        return new File("server.properties");
    }

    private static String readServerProperty(String key, String fallback) {
        File file = serverPropertiesFile();
        if (!file.isFile()) {
            return fallback;
        }
        try {
            for (String line : Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1)) {
                String trimmed = line.trim();
                if (!trimmed.startsWith("#") && trimmed.startsWith(key + "=")) {
                    return trimmed.substring(key.length() + 1).trim();
                }
            }
        } catch (IOException ignored) {
            // fall through to the fallback
        }
        return fallback;
    }

    /** Changes one line of server.properties and leaves every other line exactly as it was. */
    private static boolean writeServerProperty(String key, String value) {
        File file = serverPropertiesFile();
        if (!file.isFile()) {
            return false;
        }
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1));
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String trimmed = lines.get(i).trim();
                if (!trimmed.startsWith("#") && trimmed.startsWith(key + "=")) {
                    lines.set(i, key + "=" + value);
                    found = true;
                }
            }
            if (!found) {
                lines.add(key + "=" + value);
            }
            Files.write(file.toPath(), lines, StandardCharsets.ISO_8859_1);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Called from onLoad, before the server creates the worlds. If /lowkey seed queued a reset, the old main world
     * (and its Nether and End) is MOVED to lowkey-old-worlds (nothing is deleted), so the server generates a new
     * one with the seed that is now in server.properties.
     */
    private void applyPendingWorldReset() {
        File marker = pendingResetFile();
        if (!marker.isFile()) {
            return;
        }
        String levelName = readServerProperty("level-name", "world");
        if (!levelName.matches("[A-Za-z0-9_\\-]+") || levelName.equals(LOBBY_WORLD)) {
            getLogger().severe("World reset cancelled: the level-name '" + levelName + "' is not safe to move.");
            marker.delete();
            return;
        }
        File container = getServer().getWorldContainer();
        File main = new File(container, levelName);
        if (main.isDirectory() && !new File(main, "level.dat").isFile()) {
            getLogger().severe("World reset cancelled: " + main.getPath() + " has no level.dat.");
            marker.delete();
            return;
        }

        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        File archive = new File(new File(OLD_WORLDS_DIR), levelName + "-" + stamp);
        List<String> names = Arrays.asList(levelName + "_nether", levelName + "_the_end", levelName);
        archive.mkdirs();
        for (String name : names) {
            File dir = new File(container, name);
            if (!dir.isDirectory()) {
                continue;
            }
            try {
                Files.move(dir.toPath(), new File(archive, name).toPath());
            } catch (IOException e) {
                getLogger().severe("World reset failed: could not move " + dir.getPath() + " (" + e.getMessage()
                        + "). The reset stays queued and is tried again at the next restart.");
                return;
            }
        }
        restoreCustomWorlds(new File(archive, levelName), main);
        forgetMainWorldLocations(names);
        marker.delete();
        getLogger().info("Main world reset: the old world was moved to " + archive.getPath()
                + ", a new world is generated with the seed from server.properties.");
    }

    /**
     * Newer Paper versions keep EVERY world inside the main world folder (world/dimensions/<namespace>/<key>), so the
     * lobby and any other custom world lives there too. Everything except the three vanilla dimensions is copied
     * from the archived world into the new one, so the lobby survives a seed change.
     */
    private void restoreCustomWorlds(File archivedLevel, File newLevel) {
        File dimensions = new File(archivedLevel, "dimensions");
        File[] namespaces = dimensions.listFiles(File::isDirectory);
        if (namespaces == null) {
            return;
        }
        Set<String> vanilla = Set.of("overworld", "the_nether", "the_end");
        for (File namespace : namespaces) {
            File[] keys = namespace.listFiles(File::isDirectory);
            if (keys == null) {
                continue;
            }
            for (File key : keys) {
                if (namespace.getName().equals("minecraft") && vanilla.contains(key.getName())) {
                    continue;
                }
                File target = new File(new File(new File(newLevel, "dimensions"), namespace.getName()), key.getName());
                try {
                    copyDirectory(key.toPath(), target.toPath());
                    getLogger().info("Kept custom world " + namespace.getName() + "/" + key.getName()
                            + " through the reset.");
                } catch (IOException e) {
                    getLogger().severe("Could not keep custom world " + namespace.getName() + "/" + key.getName()
                            + " (" + e.getMessage() + "). Copy " + key.getPath() + " to " + target.getPath()
                            + " by hand while the server is stopped.");
                }
            }
        }
    }

    private static void copyDirectory(Path from, Path to) throws IOException {
        List<Path> all;
        try (Stream<Path> walk = Files.walk(from)) {
            all = walk.collect(Collectors.toList());
        }
        for (Path source : all) {
            Path destination = to.resolve(from.relativize(source).toString());
            if (Files.isDirectory(source)) {
                Files.createDirectories(destination);
            } else {
                Files.createDirectories(destination.getParent());
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /** Spawn points and last known positions in the old main world mean nothing in the new one. */
    private void forgetMainWorldLocations(List<String> worldNames) {
        File stateYml = new File(getDataFolder(), "state.yml");
        if (stateYml.isFile()) {
            YamlConfiguration state = YamlConfiguration.loadConfiguration(stateYml);
            boolean changed = false;
            for (String key : Arrays.asList("spawn-noord", "spawn-zuid", "countdown-loc")) {
                if (worldNames.contains(state.getString(key + ".world"))) {
                    state.set(key, null);
                    changed = true;
                }
            }
            if (changed) {
                try {
                    state.save(stateYml);
                } catch (IOException e) {
                    getLogger().warning("Kon state.yml niet opslaan: " + e.getMessage());
                }
            }
        }
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            if (worldNames.contains(data.getString("last.world"))) {
                data.set("last", null);
                try {
                    data.save(file);
                } catch (IOException e) {
                    getLogger().warning("Kon speler data niet opslaan: " + e.getMessage());
                }
            }
        }
    }

    /** /lowkey seed [seed|random|cancel] [confirm] */
    private void handleSeedCommand(CommandSender sender, String[] args) {
        File marker = pendingResetFile();
        if (args.length == 1) {
            sender.sendMessage(Component.text("De seed van de main wereld is nu " + gameWorld().getSeed() + ".",
                    NamedTextColor.GRAY));
            if (marker.isFile()) {
                String queued = YamlConfiguration.loadConfiguration(marker).getString("seed", "");
                sender.sendMessage(Component.text("Er staat een wereld reset klaar met seed: "
                        + (queued.isEmpty() ? "willekeurig" : queued)
                        + ". Hij gaat in bij de volgende herstart, of stop hem met /lowkey seed cancel.",
                        NamedTextColor.YELLOW));
            } else {
                sender.sendMessage(Component.text("Gebruik: /lowkey seed <seed|random|cancel>", NamedTextColor.GRAY));
            }
            return;
        }

        String arg = args[1];
        if (arg.equalsIgnoreCase("cancel")) {
            if (!marker.isFile()) {
                sender.sendMessage(Component.text("Er staat geen wereld reset klaar.", NamedTextColor.YELLOW));
                return;
            }
            String previous = YamlConfiguration.loadConfiguration(marker).getString("previous-seed", "");
            writeServerProperty("level-seed", previous);
            marker.delete();
            sender.sendMessage(Component.text("De wereld reset is geannuleerd.", NamedTextColor.GREEN));
            return;
        }

        boolean random = arg.equalsIgnoreCase("random");
        if (!random && !arg.matches("-?[A-Za-z0-9_]{1,64}")) {
            sender.sendMessage(Component.text("Een seed mag alleen letters, cijfers en _ bevatten, met een min "
                    + "ervoor voor een negatief getal. Gebruik: /lowkey seed <seed|random|cancel>",
                    NamedTextColor.RED));
            return;
        }
        String value = random ? "" : arg;
        String label = random ? "een willekeurige seed" : "seed " + value;

        if (args.length < 3 || !args[2].equalsIgnoreCase("confirm")) {
            sender.sendMessage(Component.text("Let op: bij de volgende herstart wordt de huidige main wereld "
                    + "(met Nether en End) verplaatst naar de map " + OLD_WORLDS_DIR + " en maakt de server een "
                    + "nieuwe wereld met " + label + ". Alle bouwwerken, inventories en voortgang in de main wereld "
                    + "beginnen dan opnieuw, de lobby blijft behouden. Bevestig met: /lowkey seed " + arg + " confirm", NamedTextColor.RED));
            return;
        }
        if (!serverPropertiesFile().isFile()) {
            sender.sendMessage(Component.text("server.properties niet gevonden, de seed is niet aangepast.",
                    NamedTextColor.RED));
            return;
        }

        String previous = marker.isFile()
                ? YamlConfiguration.loadConfiguration(marker).getString("previous-seed", "")
                : readServerProperty("level-seed", "");
        if (!writeServerProperty("level-seed", value)) {
            sender.sendMessage(Component.text("Kon server.properties niet aanpassen.", NamedTextColor.RED));
            return;
        }
        YamlConfiguration pending = new YamlConfiguration();
        pending.set("seed", value);
        pending.set("previous-seed", previous);
        pending.set("requested", LocalDateTime.now().toString());
        try {
            getDataFolder().mkdirs();
            pending.save(marker);
        } catch (IOException e) {
            writeServerProperty("level-seed", previous);
            sender.sendMessage(Component.text("Kon de reset niet klaarzetten: " + e.getMessage(), NamedTextColor.RED));
            return;
        }
        sender.sendMessage(Component.text("Klaar. Stop de server en start hem weer op, dan wordt de nieuwe main "
                + "wereld gemaakt met " + label + ". De oude wereld blijft bewaard in " + OLD_WORLDS_DIR
                + ". Met /lowkey seed zie je daarna de nieuwe seed. Stel je de seed bij je host in een paneel in, "
                + "zet hem daar dan ook.", NamedTextColor.GREEN));
    }

    // ------------------------------------------------------------------ admin command

    /**
     * /lowkey  (of /lowkey menu) : opent het beheerpaneel met knoppen
     * /lowkey graceall [minuten] : zet de grace van iedereen weer op dit aantal minuten (standaard grace-minutes uit config.yml), ook van spelers die offline zijn
     * /lowkey grace <speler> <minuten> : zet de resterende grace tijd (0 = grace beeindigen)
     * /lowkey team <speler> <noord|zuid|geen> : zet de speler in Noord, Zuid of geen team
     * /lowkey nether <open|close> : opent of sluit de Nether (bij openen: titel + bericht voor iedereen)
     * /lowkey server <open|close> : sluit de server voor iedereen behalve 'closed-access' (geen bans), of maakt hem weer open
     * /lowkey border <open|close> : opent of sluit de grenzen tussen Noord- en Zuid-chat
     * /lowkey revive <speler> : verwijdert de dood/uitgeschakeld-status van een speler (vooral voor testen)
     * /lowkey give <speler> : geeft een speler in de lobby de LowkeySMP nether star (hotbar slot 5)
     * /lowkey website : stuurt meteen een websitebericht met klikbare link (ook elke website-interval-minutes minuten automatisch)
     * /lowkey donate : stuurt meteen een willekeurig donatiebericht (voor testen)
     * /lowkey launch : vraagt bevestiging, stuurt dan iedereen met een team naar zijn team spawn (confirm = meteen)
     * /lowkey launch reset : zet de launch terug (voor testen), niemand wordt verplaatst
     * /lowkey countdown <spawn|remove|set <datum> <tijd>|size <getal>> : groot hologram met het logo en de aftelling (verdwijnt bij de launch)
     * /lowkey login : vraagt om de pincode (standaard 1283) en geeft toegang tot /lowkey en alle commands, tot je uitlogt of de server verlaat. Eigenaars (login.owners in config.yml) hebben altijd toegang
     * /lowkey pin <4 tot 8 cijfers> : verandert de pincode (alleen eigenaars en de console)
     * /lowkey seed [seed|random|cancel] [confirm] : zet een nieuwe seed voor de main wereld klaar, die bij de volgende herstart wordt aangemaakt (oude wereld wordt bewaard)
     * /lowkey mainworld <open|close> : sluit de main wereld (iedereen behalve admins naar de lobby) of opent hem weer (niemand wordt verplaatst)
     * /lowkey lobby <create|tp|setspawn|delete> : maakt de lege lobby wereld, gaat erheen, zet het lobby spawnpunt of wist de lobby
     * /lowkey lobby protect <aan|uit> : zet de wereldbescherming van de lobby aan of uit (sneeuw, ijs, vuur, water, explosies, pistons, groei, mobs, weer)
     * /lowkey setspawn <noord|zuid> : zet het spawnpunt van een team op jouw positie
     * /lowkey main : stuurt jou (admin) naar de gamewereld, ook als de server nog niet gelanceerd is
     * /lowkey mace [status|aan|uit|cooldown <minuten>|cooldownreset|give <speler>|reset|test <noord|zuid|geen>] : de ene mace van de server
     * /lowkey event <start|test [aantal]|stop|camera|teams|reset> : het team verdeling event (camera, shuffle, teams toepassen)
     * /lowkey losers <create [seed]|tp|setspawn|open|close|return <speler>|delete> : het Verliezerseiland (aparte survival wereld, rode ster)
     * /lowkey menu : opent het beheerpaneel
     * /lowkey end <open|close> : opent of sluit het End (bij openen: titel + bericht voor iedereen)
     * /lowkey say <bericht> : stuurt een bericht met de LOWKEY badge naar iedereen
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("lowkey")) {
            return false;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("login")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Alleen spelers kunnen inloggen.", NamedTextColor.RED));
                return true;
            }
            Player player = (Player) sender;
            if (isOwner(player)) {
                player.sendMessage(Component.text("Je hebt altijd toegang, inloggen is niet nodig.",
                        NamedTextColor.YELLOW));
            } else if (isAdmin(player)) {
                player.sendMessage(Component.text("Je bent al ingelogd.", NamedTextColor.YELLOW));
            } else if (!lockedOut(player)) {
                openLoginDialog(player);
            }
            return true;
        }
        if (!isAdmin(sender)) {
            sender.sendMessage(Component.text("Je hebt hier geen toegang toe.", NamedTextColor.RED));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("pin")) {
            if (sender instanceof Player && !isOwner((Player) sender)) {
                sender.sendMessage(Component.text("Alleen de eigenaar kan de pincode aanpassen.", NamedTextColor.RED));
                return true;
            }
            if (args.length != 2 || !args[1].matches("\\d{4,8}")) {
                sender.sendMessage(Component.text("Gebruik: /lowkey pin <4 tot 8 cijfers>", NamedTextColor.GRAY));
                return true;
            }
            loginPin = args[1];
            saveState();
            sender.sendMessage(Component.text("De pincode is aangepast.", NamedTextColor.GREEN));
            return true;
        }
        if (sender instanceof Player && args.length >= 1 && args[0].equalsIgnoreCase("menu")) {
            openMenu((Player) sender);
            return true;
        }
        if (sender instanceof Player && (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("menu")))) {
            openMenu((Player) sender);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("graceall")) {
            long minutes = graceMillis / 60_000L;
            if (args.length >= 2) {
                try {
                    minutes = Math.max(0L, Long.parseLong(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Geef het aantal minuten als getal. Gebruik: /lowkey graceall [minuten]",
                            NamedTextColor.RED));
                    return true;
                }
            }
            resetGraceForEveryone(sender, minutes);
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
        if (args.length >= 1 && args[0].equalsIgnoreCase("give")) {
            Player target = args.length == 2 ? getServer().getPlayerExact(args[1]) : null;
            if (args.length != 2) {
                sender.sendMessage(Component.text("Gebruik: /lowkey give <speler>", NamedTextColor.GRAY));
            } else if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
            } else {
                giveLobbyItemTo(sender, target);
            }
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("donate")) {
            sender.sendMessage(Component.text("Donatiebericht verstuurd.", NamedTextColor.GREEN));
            broadcastRandomDonationMessage();
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("website")) {
            sender.sendMessage(Component.text("Websitebericht verstuurd.", NamedTextColor.GREEN));
            sendWebsiteMessage();
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
        if (args.length >= 1 && args[0].equalsIgnoreCase("end")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("open")) {
                setEnd(true);
                sender.sendMessage(Component.text("Het End is nu open.", NamedTextColor.GREEN));
                return true;
            }
            if (args.length == 2 && args[1].equalsIgnoreCase("close")) {
                setEnd(false);
                sender.sendMessage(Component.text("Het End is nu dicht.", NamedTextColor.GREEN));
                return true;
            }
            sender.sendMessage(Component.text(
                    "Het End is nu " + (endOpen ? "open" : "dicht") + ". Gebruik: /lowkey end <open|close>",
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
            if (sub.equals("protect")) {
                if (args.length == 3 && (args[2].equalsIgnoreCase("aan") || args[2].equalsIgnoreCase("on"))) {
                    setLobbyProtect(true);
                    sender.sendMessage(Component.text("De lobby bescherming staat aan.", NamedTextColor.GREEN));
                    return true;
                }
                if (args.length == 3 && (args[2].equalsIgnoreCase("uit") || args[2].equalsIgnoreCase("off"))) {
                    setLobbyProtect(false);
                    sender.sendMessage(Component.text("De lobby bescherming staat uit.", NamedTextColor.GREEN));
                    return true;
                }
                sender.sendMessage(Component.text("De lobby bescherming staat nu " + (lobbyProtect ? "aan" : "uit")
                        + ". Gebruik: /lowkey lobby protect <aan|uit>", NamedTextColor.GRAY));
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
            sender.sendMessage(Component.text("Gebruik: /lowkey lobby <create|tp|setspawn|delete|protect>", NamedTextColor.GRAY));
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
        if (args.length >= 1 && args[0].equalsIgnoreCase("finale")) {
            handleFinaleCommand(sender, args);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("losers")) {
            handleLosersCommand(sender, args);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("event")) {
            handleEventCommand(sender, args);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("mace")) {
            mace.handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("countdown")) {
            String choice = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "";
            if (choice.equals("spawn")) {
                if (sender instanceof Player) {
                    placeCountdown((Player) sender);
                } else {
                    sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
                }
            } else if (choice.equals("remove")) {
                clearCountdown(sender);
            } else if (choice.equals("set") && args.length == 4) {
                setCountdownTarget(sender, args[2], args[3]);
            } else if (choice.equals("size") && args.length == 3) {
                setCountdownScale(sender, args[2]);
            } else {
                countdownStatus(sender);
            }
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("seed")) {
            handleSeedCommand(sender, args);
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("mainworld")) {
            String choice = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "";
            if (choice.equals("close")) {
                closeMainWorld(sender);
            } else if (choice.equals("open")) {
                openMainWorld(sender);
            } else {
                sender.sendMessage(Component.text("De main wereld is nu " + (mainOpen ? "open" : "gesloten")
                        + ". Gebruik: /lowkey mainworld <open|close>", NamedTextColor.GRAY));
            }
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
                "Gebruik: /lowkey grace <speler> <minuten>  |  /lowkey graceall [minuten]  |  /lowkey team <speler> <noord|zuid|geen>  |  /lowkey crew <speler> <aan|uit>  |  /lowkey nether <open|close>  |  /lowkey server <open|close>  |  /lowkey border <open|close>  |  /lowkey revive <speler>  |  /lowkey donate  |  /lowkey website  |  /lowkey launch [reset]  |  /lowkey lobby <create|tp|setspawn|delete>  |  /lowkey setspawn <noord|zuid>  |  /lowkey main  |  /lowkey mainworld <open|close>  |  /lowkey seed <seed|random|cancel>  |  /lowkey pin <pincode>  |  /lowkey countdown <spawn|remove|set|size>  |  /lowkey mace <status|aan|uit|cooldown|cooldownreset|give|reset|test>  |  /lowkey event <start|test|stop|camera|teams|reset>  |  /lowkey losers <create|tp|setspawn|open|close|return|returnall|delete>  |  /lowkey end <open|close>  |  /lowkey say <bericht>",
                NamedTextColor.GRAY));
        return true;
    }

    // ------------------------------------------------------------------ admin panel (dialog menus)

    static final ClickCallback.Options CLICK_OPTIONS =
            ClickCallback.Options.builder().uses(1).lifetime(Duration.ofMinutes(10)).build();

    /**
     * One clickable button in a dialog. The action runs on the main thread and only when the person
     * who clicked really has the lowkey.admin permission (so a menu can never be abused).
     */
    ActionButton button(Component label, String tooltip, int width, Runnable action) {
        DialogAction dialogAction = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            getServer().getScheduler().runTask(this, action);
        }, CLICK_OPTIONS);
        return ActionButton.create(label, Component.text(tooltip), width, dialogAction);
    }

    /** A menu label: the vanilla item icon from the resource pack, a space and the text. */
    Component iconLabel(String icon, String text, TextColor color) {
        return Component.text()
                .append(glyph(icon))
                .append(Component.space())
                .append(Component.text(text, color))
                .build();
    }

    static Component menuTitle(String text) {
        return Component.text(text, NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD);
    }

    /** One status line: grey label, coloured value. */
    static Component statusLine(String label, String value, TextColor valueColor) {
        return Component.text()
                .append(Component.text(label + ": ", NamedTextColor.GRAY))
                .append(Component.text(value, valueColor))
                .build();
    }

    static Component joinLines(Component... lines) {
        Component result = Component.empty();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                result = result.append(Component.newline());
            }
            result = result.append(lines[i]);
        }
        return result;
    }

    ActionButton backButton(String tooltip, Runnable back) {
        return button(iconLabel(M_BACK, "Terug", NamedTextColor.GRAY), tooltip, 150, back);
    }

    // ------------------------------------------------------------------ menus (one design: the classic one)

    void showMenu(Player admin, String title, Component body, List<ActionButton> buttons, ActionButton exit) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle(title))
                        .body(List.of(DialogBody.plainMessage(body)))
                        .build())
                .type(DialogType.multiAction(buttons, exit, 2)));
        admin.showDialog(dialog);
    }

    /** Title for any dialog. */
    Component dlgTitle(Player admin, String text) {
        return menuTitle(text);
    }

    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    /** A switch that shows its state: [OPEN] Nether. Click to flip it. */
    private ActionButton stateButton(String name, boolean on, String onText, String offText, String tooltip,
                                     Runnable action) {
        Component label = Component.text()
                .append(Component.text("[" + (on ? onText : offText).toUpperCase(Locale.ROOT) + "] ",
                        on ? NamedTextColor.GREEN : NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text(name, NamedTextColor.WHITE))
                .build();
        return button(label, tooltip, 150, action);
    }

    /** The main menu. */
    void openMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_SERVER, "Server", NamedTextColor.WHITE),
                "Launch, main wereld en de server open of dicht.", 150, () -> openServerMenu(admin)));
        buttons.add(button(iconLabel(M_MESSAGES, "Berichten", NamedTextColor.WHITE),
                "Countdown, eigen bericht, donatiebericht en websitebericht.", 150, () -> openMessagesMenu(admin)));
        buttons.add(button(iconLabel(M_BOOK, "Regels", NamedTextColor.WHITE),
                "Het regelboekje in de lobby: pagina's schrijven en iedereen opnieuw laten lezen.", 150,
                () -> openRulesMenu(admin)));
        buttons.add(button(iconLabel(M_MACE, "Mace", NamedTextColor.WHITE),
                "De ene mace: cooldown, houder en reset.", 150, () -> mace.openMenu(admin)));
        buttons.add(button(iconLabel(M_TEAM, "Teams", NamedTextColor.WHITE),
                "Noord en Zuid, grace, crew en elke speler apart.", 150, () -> openTeamsMenu(admin)));
        buttons.add(button(iconLabel(M_WORLD, "Wereld", NamedTextColor.WHITE),
                "Lobby, spawns, Nether, End en de grenzen.", 150, () -> openWorldMenu(admin)));
        buttons.add(button(iconLabel(M_STAR, "Finale", NamedTextColor.LIGHT_PURPLE),
                "De krimpende border en de winnaarsshow.", 150, () -> openFinaleMenu(admin)));
        buttons.add(button(iconLabel(M_HORN, "Event", NamedTextColor.WHITE),
                "Het team event en het Verliezerseiland.", 150, () -> openEventHub(admin)));
        buttons.add(button(iconLabel(M_REVIVE, "Testen en herstellen", NamedTextColor.WHITE),
                "Reviven, resetten en alles terugzetten.", 150, () -> openRepairMenu(admin)));
        if (loggedIn.contains(admin.getUniqueId()) && !isOwner(admin)) {
            buttons.add(button(Component.text("Uitloggen", NamedTextColor.RED),
                    "Log uit en verlies je toegang tot /lowkey en de commands.", 150, () -> logout(admin, true)));
        }
        ActionButton close = ActionButton.create(Component.text("Sluiten", NamedTextColor.GRAY),
                Component.text("Sluit dit menu."), 150, null);

        Component status = joinLines(
                statusLine("Server", serverClosed ? "gesloten" : "open",
                        serverClosed ? NamedTextColor.RED : NamedTextColor.GREEN),
                statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Main wereld", mainOpen ? "open" : "gesloten",
                        mainOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Nether", netherOpen ? "open" : "dicht",
                        netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Finale", finaleStateText(),
                        finaleState == FinaleState.RUNNING ? NamedTextColor.GREEN : NamedTextColor.GRAY),
                statusLine("Online", String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE));
        showMenu(admin, "LowkeySMP Beheer", status, buttons, close);
    }

    // ---- Server

    private void openServerMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openServerMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();

        if (!launched) {
            buttons.add(button(iconLabel(M_LAUNCH, "Server launchen", NamedTextColor.GREEN),
                    "Stuurt iedereen met een team naar zijn team spawn.", 150, () -> openLaunchConfirm(admin, again)));
        } else {
            buttons.add(button(iconLabel(M_LAUNCH, "Launch resetten", NamedTextColor.RED),
                    "Zet de launch terug (voor testen). Niemand wordt verplaatst.", 150, () -> confirm(admin,
                            "Launch resetten?",
                            "Het lobby-item zegt daarna weer dat de server nog niet open is.",
                            "Spelers in de gamewereld blijven daar.",
                            Component.text("Ja, resetten", NamedTextColor.RED),
                            () -> {
                                resetLaunch(admin);
                                again.run();
                            },
                            again)));
        }

        if (mainOpen) {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld sluiten", NamedTextColor.RED),
                    "Iedereen behalve admins gaat naar de lobby. Handig om iets aan te passen.", 150, () -> confirm(admin,
                            "Main wereld sluiten?",
                            "Iedereen behalve admins gaat naar de lobby en kan er niet uit tot je de main wereld opent.",
                            "Spelers houden hun spullen en komen later terug op hun laatste plek.",
                            Component.text("Ja, sluiten", NamedTextColor.RED),
                            () -> {
                                closeMainWorld(admin);
                                again.run();
                            },
                            again)));
        } else {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld openen", NamedTextColor.GREEN),
                    "Spelers in de lobby kunnen er weer in met hun item. Niemand wordt verplaatst.", 150, () -> {
                        openMainWorld(admin);
                        again.run();
                    }));
        }

        if (serverClosed) {
            buttons.add(button(iconLabel(M_SERVER, "Server openen", NamedTextColor.GREEN),
                    "Maak de server weer open voor iedereen.", 150, () -> {
                        setServerClosed(false);
                        admin.sendMessage(Component.text("De server is weer open voor iedereen.", NamedTextColor.GREEN));
                        again.run();
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
                                again.run();
                            },
                            again)));
        }

        Component body = joinLines(
                statusLine("Server", serverClosed ? "gesloten" : "open",
                        serverClosed ? NamedTextColor.RED : NamedTextColor.GREEN),
                statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Main wereld", mainOpen ? "open" : "gesloten",
                        mainOpen ? NamedTextColor.GREEN : NamedTextColor.RED));
        showMenu(admin, "Server", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    private void openLaunchConfirm(Player admin, Runnable back) {
        confirm(admin,
                "Server launchen?",
                "Iedereen met een team wordt naar zijn team spawn gestuurd, met de titel Veel succes!",
                "Opgeslagen laatste plekken en dood-statussen van tests worden gewist. Dit kan niet ongedaan gemaakt worden.",
                Component.text("Ja, launch", NamedTextColor.GREEN),
                () -> launchServer(admin),
                back);
    }

    // ---- Berichten

    private void openMessagesMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_COUNTDOWN, "Countdown", NamedTextColor.WHITE),
                "Hologram met de aftelling naar de start.", 150, () -> openCountdownMenu(admin)));
        buttons.add(button(iconLabel(M_SAY, "Eigen bericht", NamedTextColor.WHITE),
                "Stuur een bericht met de LOWKEY badge naar iedereen.", 150, () -> openSayDialog(admin)));
        buttons.add(button(iconLabel(M_DONATE, "Donatiebericht", NamedTextColor.WHITE),
                "Stuurt meteen een willekeurig donatiebericht naar iedereen.", 150, () -> {
                    broadcastRandomDonationMessage();
                    openMessagesMenu(admin);
                }));
        buttons.add(button(iconLabel(M_MESSAGES, "Websitebericht", NamedTextColor.WHITE),
                "Stuurt meteen een bericht met een klikbare link naar de website naar iedereen.", 150, () -> {
                    sendWebsiteMessage();
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
            if (!isAdmin(clicker)) {
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
                .base(DialogBase.builder(dlgTitle(admin, "Eigen bericht"))
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

    // ---- Wereld

    private void openWorldMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openWorldMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_LOBBY, "Naar de lobby", NamedTextColor.WHITE),
                "Teleporteert jou naar de lobby.", 150, () -> teleportAdminToLobby(admin)));
        buttons.add(button(iconLabel(M_MAIN, "Naar main eiland", NamedTextColor.WHITE),
                "Teleporteert jou naar de gamewereld (laatste plek of team spawn).", 150,
                () -> enterGame(admin, false, true)));
        buttons.add(button(iconLabel(M_SPAWN_N, "Spawn Noord zetten", NamedTextColor.RED),
                "Zet het spawnpunt van Noord op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.NOORD);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_SPAWN_Z, "Spawn Zuid zetten", NamedTextColor.BLUE),
                "Zet het spawnpunt van Zuid op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.ZUID);
                    again.run();
                }));
        buttons.add(netherToggle(admin, again));
        buttons.add(endToggle(admin, again));
        if (bordersDropped) {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen dicht", NamedTextColor.RED),
                    "Noord en Zuid zien daarna weer alleen hun eigen chat. De WorldGuard flags zet je zelf terug.", 150,
                    () -> {
                        setBordersDropped(false);
                        admin.sendMessage(Component.text("De grenzen zijn dicht. Zet de WorldGuard flags van "
                                + BORDER_REGION + " zelf weer terug.", NamedTextColor.YELLOW));
                        again.run();
                    }));
        } else {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen droppen", NamedTextColor.GREEN),
                    "Haalt de WorldGuard flags van de ijsmuur weg en opent de gedeelde chat.", 150,
                    () -> confirmDropBorders(admin)));
        }
        Component body = joinLines(
                statusLine("Nether", netherOpen ? "open" : "dicht", netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("End", endOpen ? "open" : "dicht", endOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Spawn Noord", spawnNoord != null ? "ingesteld" : "niet ingesteld",
                        spawnNoord != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Zuid", spawnZuid != null ? "ingesteld" : "niet ingesteld",
                        spawnZuid != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Wereld", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    /** The WorldGuard region around the ice wall and the flags that keep it closed. */
    private static final String BORDER_REGION = "teamgrens";
    private static final String[] BORDER_FLAGS = {"block-break", "block-place", "entry", "enderpearl",
            "chorus-fruit-teleport"};

    private void confirmDropBorders(Player admin) {
        Runnable again = () -> openWorldMenu(admin);
        confirm(admin,
                "Grenzen droppen?",
                "Dit haalt de WorldGuard flags van " + BORDER_REGION + " weg (" + String.join(", ", BORDER_FLAGS)
                        + ") en Noord en Zuid krijgen weer een gedeelde chat.",
                "Je kunt de grenzen later weer dichtzetten, de flags zet je dan zelf terug.",
                Component.text("Ja, droppen", NamedTextColor.GREEN),
                () -> {
                    dropBorders(admin);
                    again.run();
                },
                again);
    }

    /** Removes the WorldGuard flags (a flag command without a value removes the flag) and opens the shared chat. */
    private void dropBorders(Player admin) {
        String world = gameWorld().getName();
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            for (String flag : BORDER_FLAGS) {
                admin.performCommand("region flag -w " + world + " " + BORDER_REGION + " " + flag);
            }
            admin.performCommand("region info -w " + world + " " + BORDER_REGION);
            admin.sendMessage(Component.text("De flags van " + BORDER_REGION + " zijn weggehaald. Controleer hierboven of "
                    + "er geen flags meer staan.", NamedTextColor.GREEN));
        } else {
            admin.sendMessage(Component.text("WorldGuard is niet gevonden. Doe deze commands zelf (klik om ze in je "
                    + "chatbalk te zetten):", NamedTextColor.YELLOW));
            for (String flag : BORDER_FLAGS) {
                String cmd = "/region flag -w " + world + " " + BORDER_REGION + " " + flag;
                admin.sendMessage(Component.text(cmd, NamedTextColor.AQUA)
                        .clickEvent(ClickEvent.suggestCommand(cmd))
                        .hoverEvent(HoverEvent.showText(Component.text("Klik om in je chatbalk te zetten",
                                NamedTextColor.GRAY))));
            }
        }
        setBordersDropped(true);
    }

    // ---- Teams

    /** One person in the Teams screen, online or not. */
    private static final class TeamEntry {
        final UUID id;
        final String name;
        final boolean online;
        final boolean dead;
        final Side side;

        TeamEntry(UUID id, String name, boolean online, boolean dead, Side side) {
            this.id = id;
            this.name = name;
            this.online = online;
            this.dead = dead;
            this.side = side;
        }
    }

    private static String sideName(Side side) {
        return side == Side.NOORD ? "Noord" : (side == Side.ZUID ? "Zuid" : "geen team");
    }

    /** Everybody who ever joined, online and offline, sorted by name. */
    private List<TeamEntry> collectTeamEntries() {
        List<TeamEntry> all = new ArrayList<>();
        Set<UUID> seen = new HashSet<>();
        for (Player p : getServer().getOnlinePlayers()) {
            seen.add(p.getUniqueId());
            all.add(new TeamEntry(p.getUniqueId(), p.getName(), true, isEliminated(p) || isLosersOut(p), getSide(p)));
        }
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                UUID id;
                try {
                    id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));
                } catch (IllegalArgumentException e) {
                    continue;
                }
                if (seen.contains(id)) {
                    continue;
                }
                YamlConfiguration y = YamlConfiguration.loadConfiguration(f);
                Side side = Side.fromId(y.getString("identity.side"));
                boolean dead = y.getBoolean("identity.eliminated", false) || y.getBoolean("identity.losers-out", false);
                String name = getServer().getOfflinePlayer(id).getName();
                if (name == null) {
                    name = id.toString().substring(0, 8);
                }
                all.add(new TeamEntry(id, name, false, dead, side));
            }
        }
        all.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        return all;
    }

    /** The name on a button: white, or grey and italic when the player is dead. */
    private Component teamName(TeamEntry e) {
        Component name = e.dead
                ? Component.text(e.name, NamedTextColor.GRAY, TextDecoration.ITALIC)
                : Component.text(e.name, NamedTextColor.WHITE);
        if (!e.online) {
            name = Component.text()
                    .append(name)
                    .append(Component.text(" (offline)", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
                    .build();
        }
        return name;
    }

    /** An empty slot: Minecraft cannot hide a button, so it is a plain grey one that only refreshes the screen. */
    private ActionButton teamBlank(Player admin, int width) {
        return button(Component.text(" "), "", width, () -> openTeamsMenu(admin));
    }

    private ActionButton teamPencilButton(Player admin, TeamEntry e) {
        return button(glyph(M_FEATHER), "Beheer " + e.name + ": grace, crew en meer.", 28,
                () -> openPlayerActions(admin, e.id));
    }

    private ActionButton teamNameButton(Player admin, TeamEntry e) {
        return button(teamName(e), "Beheer " + e.name + ".", 130, () -> openPlayerActions(admin, e.id));
    }

    private ActionButton teamMoveButton(Player admin, TeamEntry e, Side to) {
        return button(Component.text(to == Side.ZUID ? ">" : "<", NamedTextColor.WHITE, TextDecoration.BOLD),
                "Zet " + e.name + " in " + sideName(to) + ".", 28, () -> confirmTeamMove(admin, e, to));
    }

    private void confirmTeamMove(Player admin, TeamEntry e, Side to) {
        String target = sideName(to);
        confirm(admin,
                "Naar " + target + " verplaatsen?",
                e.name + " zit nu in " + sideName(e.side) + ".",
                e.name + " komt in " + target + (e.online ? "." : " (offline, het geldt vanaf zijn volgende join)."),
                Component.text("Ja, verplaatsen", to == Side.ZUID ? NamedTextColor.BLUE : NamedTextColor.RED),
                () -> {
                    setSideByUuid(e.id, to);
                    admin.sendMessage(Component.text(e.name + " zit nu in " + target + ".", NamedTextColor.GREEN));
                    openTeamsMenu(admin);
                },
                () -> openTeamsMenu(admin));
    }

    /** Noord on the left, Zuid on the right: [pencil] [name] [>]   [<] [name] [pencil]. */
    private void openTeamsMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<TeamEntry> noord = new ArrayList<>();
        List<TeamEntry> zuid = new ArrayList<>();
        List<TeamEntry> geen = new ArrayList<>();
        for (TeamEntry e : collectTeamEntries()) {
            if (e.side == Side.NOORD) {
                noord.add(e);
            } else if (e.side == Side.ZUID) {
                zuid.add(e);
            } else {
                geen.add(e);
            }
        }

        List<ActionButton> buttons = new ArrayList<>();
        int rows = Math.max(noord.size(), zuid.size());
        for (int i = 0; i < rows; i++) {
            if (i < noord.size()) {
                TeamEntry e = noord.get(i);
                buttons.add(teamPencilButton(admin, e));
                buttons.add(teamNameButton(admin, e));
                buttons.add(teamMoveButton(admin, e, Side.ZUID));
            } else {
                buttons.add(teamBlank(admin, 28));
                buttons.add(teamBlank(admin, 130));
                buttons.add(teamBlank(admin, 28));
            }
            if (i < zuid.size()) {
                TeamEntry e = zuid.get(i);
                buttons.add(teamMoveButton(admin, e, Side.NOORD));
                buttons.add(teamNameButton(admin, e));
                buttons.add(teamPencilButton(admin, e));
            } else {
                buttons.add(teamBlank(admin, 28));
                buttons.add(teamBlank(admin, 130));
                buttons.add(teamBlank(admin, 28));
            }
        }
        // last row: Terug under the Noord names, Zonder team under the Zuid names
        buttons.add(teamBlank(admin, 28));
        buttons.add(button(iconLabel(M_BACK, "Terug", NamedTextColor.WHITE), "Terug naar het hoofdmenu.", 130,
                () -> openMenu(admin)));
        buttons.add(teamBlank(admin, 28));
        buttons.add(teamBlank(admin, 28));
        buttons.add(button(iconLabel(M_PLAYERS, "Zonder team (" + geen.size() + ")", NamedTextColor.WHITE),
                "Spelers zonder team in Noord of Zuid zetten.", 130, () -> openNoTeamList(admin)));
        buttons.add(teamBlank(admin, 28));

        // Grace voor iedereen is the exit button of the dialog, so it always sits in the middle under everything
        ActionButton grace = button(iconLabel(M_GRACE, "Grace voor iedereen", NamedTextColor.WHITE),
                "Zet de grace van alle spelers weer op de standaard tijd.", 200, () -> confirm(admin,
                        "Grace voor iedereen?",
                        "De grace van alle spelers gaat terug naar " + (graceMillis / 60_000L) + " minuten, ook van "
                                + "spelers die offline zijn.",
                        "Dit geldt voor iedereen.",
                        Component.text("Ja, voor iedereen", NamedTextColor.GREEN),
                        () -> {
                            resetGraceForEveryone(admin, graceMillis / 60_000L);
                            openTeamsMenu(admin);
                        },
                        () -> openTeamsMenu(admin)));

        Component logos = Component.text()
                .append(glyph(NOORD_BIG))
                .append(glyph(EVENT_GAP))
                .append(glyph(EVENT_GAP))
                .append(glyph(EVENT_GAP))
                .append(glyph(ZUID_BIG))
                .build();
        Component counts = joinLines(
                Component.text()
                        .append(Component.text("Noord " + noord.size(), TextColor.color(Side.NOORD.rgb), TextDecoration.BOLD))
                        .append(Component.text("    "))
                        .append(Component.text("Zuid " + zuid.size(), TextColor.color(Side.ZUID.rgb), TextDecoration.BOLD))
                        .append(Component.text("    Zonder team " + geen.size(), NamedTextColor.GRAY))
                        .build(),
                Component.text("Pijl: naar het andere team. Veer: de speler zelf beheren.", NamedTextColor.DARK_GRAY));
        List<DialogBody> bodies = new ArrayList<>();
        bodies.add(DialogBody.plainMessage(logos, 400));
        bodies.add(DialogBody.plainMessage(counts, 400));
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Teams"))
                        .canCloseWithEscape(false)
                        .body(bodies)
                        .build())
                .type(DialogType.multiAction(buttons, grace, 6)));
        admin.showDialog(dialog);
    }

    /** Players without a team: pick Noord or Zuid for each of them. */
    private void openNoTeamList(Player admin) {
        List<ActionButton> buttons = new ArrayList<>();
        int count = 0;
        for (TeamEntry e : collectTeamEntries()) {
            if (e.side != Side.NONE) {
                continue;
            }
            count++;
            buttons.add(teamNameButton(admin, e));
            buttons.add(button(Component.text("Noord", NamedTextColor.RED), "Zet " + e.name + " in Noord.", 60, () -> {
                setSideByUuid(e.id, Side.NOORD);
                openNoTeamList(admin);
            }));
            buttons.add(button(Component.text("Zuid", NamedTextColor.BLUE), "Zet " + e.name + " in Zuid.", 60, () -> {
                setSideByUuid(e.id, Side.ZUID);
                openNoTeamList(admin);
            }));
        }
        ActionButton back = backButton("Terug naar teams.", () -> openTeamsMenu(admin));
        Component body = Component.text(count == 0 ? "Iedereen zit in een team." : count + " spelers zonder team.",
                NamedTextColor.GRAY);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(menuTitle("Zonder team")).body(List.of(DialogBody.plainMessage(body))).build())
                .type(DialogType.multiAction(buttons, back, 3)));
        admin.showDialog(dialog);
    }

    /** Everything for one player: team, grace, crew, dead status. */
    private void openPlayerActions(Player admin, UUID id) {
        if (!admin.isOnline()) {
            return;
        }
        Player target = getServer().getPlayer(id);
        if (target == null) {
            String name = getServer().getOfflinePlayer(id).getName();
            admin.sendMessage(Component.text((name == null ? "Deze speler" : name) + " is offline. Grace, crew en "
                    + "dood-status kun je alleen aanpassen als hij online is. Het team kan wel met de pijltjes.",
                    NamedTextColor.YELLOW));
            openTeamsMenu(admin);
            return;
        }
        Runnable again = () -> openPlayerActions(admin, id);
        boolean dead = isEliminated(target) || isLosersOut(target);
        boolean crew = isCrew(target);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_TEAM, "Team wijzigen", NamedTextColor.WHITE),
                "Zet " + target.getName() + " in Noord, Zuid of geen team.", 150, () -> openTeamPick(admin, id)));
        buttons.add(button(iconLabel(M_GRACE, "Grace instellen", NamedTextColor.WHITE),
                "Zet de grace tijd van " + target.getName() + ".", 150, () -> openGracePick(admin, id)));
        buttons.add(button(iconLabel(M_CREW, crew ? "Crew verwijderen" : "Crew geven", NamedTextColor.WHITE),
                crew ? target.getName() + " geen crew meer maken." : target.getName() + " crew maken.", 150, () -> {
                    setCrew(admin, target, !crew);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_LOBBY, "LowkeySMP ster geven", NamedTextColor.WHITE),
                "Geeft " + target.getName() + " in de lobby de LowkeySMP nether star (hotbar slot 5).", 150, () -> {
                    giveLobbyItemTo(admin, target);
                    again.run();
                }));
        if (dead) {
            buttons.add(button(iconLabel(M_REVIVE, "Reviven", NamedTextColor.GREEN),
                    "Zet " + target.getName() + " weer levend, waar hij ook is.", 150, () -> {
                        revivePlayer(admin, target);
                        again.run();
                    }));
            buttons.add(button(iconLabel(M_BARRIER, "Dood-status weg", NamedTextColor.WHITE),
                    "Verwijdert alleen de uitgeschakeld-status (vooral voor testen).", 150, () -> {
                        clearEliminated(target);
                        admin.sendMessage(Component.text("Dood-status van " + target.getName() + " is verwijderd.",
                                NamedTextColor.GREEN));
                        again.run();
                    }));
        }
        long graceMinutes = (graceRemaining(target) + 59_999L) / 60_000L;
        Side side = getSide(target);
        Component body = joinLines(
                statusLine("Speler", target.getName(), NamedTextColor.WHITE),
                statusLine("Team", sideName(side), side == Side.NOORD ? NamedTextColor.RED
                        : (side == Side.ZUID ? NamedTextColor.BLUE : NamedTextColor.GRAY)),
                statusLine("Grace", graceMinutes + " minuten", graceMinutes > 0 ? NamedTextColor.YELLOW : NamedTextColor.GRAY),
                statusLine("Crew", crew ? "ja" : "nee", crew ? NamedTextColor.GOLD : NamedTextColor.GRAY),
                statusLine("Status", dead ? "dood" : "levend", dead ? NamedTextColor.RED : NamedTextColor.GREEN));
        showMenu(admin, "Speler", body, buttons, backButton("Terug naar teams.", () -> openTeamsMenu(admin)));
    }

    private void openTeamPick(Player admin, UUID id) {
        Player target = getServer().getPlayer(id);
        if (target == null) {
            openTeamsMenu(admin);
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        Side[] sides = {Side.NOORD, Side.ZUID, Side.NONE};
        NamedTextColor[] colors = {NamedTextColor.RED, NamedTextColor.BLUE, NamedTextColor.GRAY};
        for (int i = 0; i < sides.length; i++) {
            final Side side = sides[i];
            String label = side == Side.NONE ? "Geen team" : sideName(side);
            buttons.add(button(Component.text(label, colors[i]), target.getName() + " in " + sideName(side) + " zetten.",
                    150, () -> {
                        setSide(target, side);
                        admin.sendMessage(Component.text(target.getName() + " zit nu in " + sideName(side) + ".",
                                NamedTextColor.GREEN));
                        openPlayerActions(admin, id);
                    }));
        }
        showMenu(admin, "Team voor " + target.getName(), Component.text("Nu: " + sideName(getSide(target)),
                NamedTextColor.GRAY), buttons, backButton("Terug naar de speler.", () -> openPlayerActions(admin, id)));
    }

    private void openGracePick(Player admin, UUID id) {
        Player target = getServer().getPlayer(id);
        if (target == null) {
            openTeamsMenu(admin);
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        int[] minutes = {0, 10, 30, 60};
        for (int m : minutes) {
            String label = m == 0 ? "Beeindigen" : m + " minuten";
            buttons.add(button(Component.text(label), "Zet de grace van " + target.getName() + " op " + m + " minuten.",
                    150, () -> {
                        setGrace(target, m * 60_000L);
                        admin.sendMessage(Component.text("Grace van " + target.getName() + " staat nu op " + m
                                + " minuten.", NamedTextColor.GREEN));
                        openPlayerActions(admin, id);
                    }));
        }
        long left = (graceRemaining(target) + 59_999L) / 60_000L;
        showMenu(admin, "Grace voor " + target.getName(), Component.text("Nu nog: " + left + " minuten",
                NamedTextColor.GRAY), buttons, backButton("Terug naar de speler.", () -> openPlayerActions(admin, id)));
    }

    // ---- Event (team event and Verliezerseiland)

    private void openEventHub(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_HORN, "Team event", NamedTextColor.WHITE),
                "Verdeel iedereen over Noord en Zuid met een show.", 150, () -> openEventMenu(admin)));
        buttons.add(button(iconLabel(M_BONE, "Verliezerseiland", NamedTextColor.WHITE),
                "De aparte wereld voor iedereen die dood is.", 150, () -> openLosersMenu(admin)));
        Component body = joinLines(
                statusLine("Team event", eventRunning ? "bezig" : "niet bezig",
                        eventRunning ? NamedTextColor.YELLOW : NamedTextColor.GRAY),
                statusLine("Verliezerseiland", losersEnded ? "afgelopen" : (losersOpen ? "open" : "dicht"),
                        losersOpen ? NamedTextColor.GREEN : NamedTextColor.RED));
        showMenu(admin, "Event", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    // ---- shared screens

    /** A yes / no screen. "Annuleren" runs `back`. */
    void confirm(Player admin, String title, String line1, String line2, Component yesLabel,
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
        pickPlayer(admin, title, next, () -> openMenu(admin));
    }

    /** Same, but "Terug" goes to wherever `backTo` says. */
    void pickPlayer(Player admin, String title, Consumer<Player> next, Runnable backTo) {
        List<ActionButton> buttons = new ArrayList<>();
        for (Player online : getServer().getOnlinePlayers()) {
            buttons.add(button(Component.text(online.getName()), "Kies " + online.getName() + ".", 100, () -> {
                if (online.isOnline()) {
                    next.accept(online);
                } else {
                    admin.sendMessage(Component.text("Die speler is niet meer online.", NamedTextColor.RED));
                    backTo.run();
                }
            }));
        }
        ActionButton back = backButton("Terug.", backTo);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, title))
                        .body(List.of(DialogBody.plainMessage(Component.text("Kies een speler:", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 3)));
        admin.showDialog(dialog);
    }

    // ------------------------------------------------------------------ the End (open / closed, like the Nether)

    private void setEnd(boolean open) {
        endOpen = open;
        saveState();
        Component chat = Component.text()
                .append(glyph(LOWKEY_BADGE_RED))
                .append(Component.space())
                .append(Component.text("Het End is nu ", NamedTextColor.RED))
                .append(Component.text(open ? "geopend" : "gesloten", NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text("!", NamedTextColor.RED))
                .build();
        Title title = Title.title(
                Component.text("Het End is geopend!", NamedTextColor.LIGHT_PURPLE),
                Component.empty(),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(chat);
            if (open) {
                player.showTitle(title);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
            }
        }
    }

    /** While the End is closed, nobody can travel INTO it through the end portal (leaving it is always allowed). */
    @EventHandler
    public void onEndPortal(PlayerPortalEvent event) {
        if (endOpen || event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
            return;
        }
        event.setCancelled(true);
        long now = System.currentTimeMillis();
        Long last = netherNotice.get(player.getUniqueId());
        if (last == null || now - last > 3000L) {
            netherNotice.put(player.getUniqueId(), now);
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE_RED))
                    .append(Component.space())
                    .append(Component.text("Het End is nog ", NamedTextColor.RED))
                    .append(Component.text("gesloten", NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text("!", NamedTextColor.RED))
                    .build());
        }
    }

    private ActionButton netherToggle(Player admin, Runnable again) {
        if (netherOpen) {
            return button(iconLabel(M_NETHER, "Nether sluiten", NamedTextColor.RED),
                    "Sluit de Nether weer. Wie erin zit kan er nog uit.", 150, () -> {
                        setNether(false);
                        again.run();
                    });
        }
        return button(iconLabel(M_NETHER, "Nether openen", NamedTextColor.GREEN),
                "Opent de Nether voor iedereen, met titel en bericht.", 150, () -> confirm(admin,
                        "Nether openen?",
                        "Iedereen krijgt de titel en het bericht dat de Nether open is.",
                        "Je kunt hem later weer sluiten.",
                        Component.text("Ja, openen", NamedTextColor.GREEN),
                        () -> {
                            setNether(true);
                            again.run();
                        },
                        again));
    }

    private ActionButton endToggle(Player admin, Runnable again) {
        if (endOpen) {
            return button(iconLabel(M_ENDCRYSTAL, "End sluiten", NamedTextColor.RED),
                    "Sluit het End weer. Wie erin zit kan er nog uit.", 150, () -> {
                        setEnd(false);
                        again.run();
                    });
        }
        return button(iconLabel(M_ENDCRYSTAL, "End openen", NamedTextColor.GREEN),
                "Opent het End voor iedereen, met titel en bericht.", 150, () -> confirm(admin,
                        "End openen?",
                        "Iedereen krijgt de titel en het bericht dat het End open is.",
                        "Je kunt het later weer sluiten.",
                        Component.text("Ja, openen", NamedTextColor.GREEN),
                        () -> {
                            setEnd(true);
                            again.run();
                        },
                        again));
    }

    // ------------------------------------------------------------------ finale (shrinking border)

    private enum FinaleState { IDLE, RUNNING, PAUSED, ENDED }

    /** One elimination during the finale, with the server tick it happened in. */
    private static final class FinaleDeath {
        final UUID id;
        final int tick;

        FinaleDeath(UUID id, int tick) {
            this.id = id;
            this.tick = tick;
        }
    }

    // settings (saved in state.yml)
    private boolean finaleTargetSet;
    private double finaleTargetX;
    private double finaleTargetZ;
    private double finaleTargetSize = 30.0;
    private int finaleDurationMin = 10;
    private int finaleRoundMin = 3;
    private int finaleShrinkSec = 30;
    private int finalePauseSec = 30;
    private double finaleDamage = 1.0;
    private double finaleBuffer = 2.0;
    private boolean finaleCloseDims = true;
    private boolean finaleAutoTie = true;

    // the run itself (saved in state.yml, so a restart does not lose it)
    private FinaleState finaleState = FinaleState.IDLE;
    private long finaleElapsedMs;
    private double runStartCx;
    private double runStartCz;
    private double runStartSize;
    private double runEndCx;
    private double runEndCz;
    private double runEndSize;
    private long runTotalMs;
    private long runShrinkMs;
    private long runPauseMs;
    private int runRounds;
    private double origDamage = 0.2;
    private double origBuffer = 5.0;
    private String finaleWinner;
    private boolean finaleRestartNotice;

    // not saved
    private long finaleLastTickMs;
    private int finaleAppliedKey = -1;
    private boolean finaleSilentApply;
    private int finaleTickCount;
    private boolean finaleCheckPending;
    private final List<FinaleDeath> finaleDeaths = new ArrayList<>();
    private final Set<Integer> finaleAnnounced = new HashSet<>();
    private final Set<UUID> showInvulnerable = new HashSet<>();
    private final Random finaleRandom = new Random();

    private void loadFinaleState(YamlConfiguration s) {
        finaleTargetSet = s.getBoolean("finale.target-set", false);
        finaleTargetX = s.getDouble("finale.target-x", 0.0);
        finaleTargetZ = s.getDouble("finale.target-z", 0.0);
        finaleTargetSize = s.getDouble("finale.target-size", 30.0);
        finaleDurationMin = s.getInt("finale.duration-min", 10);
        finaleRoundMin = s.getInt("finale.round-min", 3);
        finaleShrinkSec = s.getInt("finale.shrink-sec", 30);
        finalePauseSec = s.getInt("finale.pause-sec", 30);
        finaleDamage = s.getDouble("finale.damage", 1.0);
        finaleBuffer = s.getDouble("finale.buffer", 2.0);
        finaleCloseDims = true;
        finaleAutoTie = true;
        try {
            finaleState = FinaleState.valueOf(s.getString("finale.state", "IDLE"));
        } catch (IllegalArgumentException e) {
            finaleState = FinaleState.IDLE;
        }
        finaleElapsedMs = s.getLong("finale.elapsed-ms", 0L);
        runStartCx = s.getDouble("finale.run.start-x", 0.0);
        runStartCz = s.getDouble("finale.run.start-z", 0.0);
        runStartSize = s.getDouble("finale.run.start-size", 0.0);
        runEndCx = s.getDouble("finale.run.end-x", 0.0);
        runEndCz = s.getDouble("finale.run.end-z", 0.0);
        runEndSize = s.getDouble("finale.run.end-size", 30.0);
        runTotalMs = s.getLong("finale.run.total-ms", 0L);
        runShrinkMs = s.getLong("finale.run.shrink-ms", 0L);
        runPauseMs = s.getLong("finale.run.pause-ms", 0L);
        runRounds = s.getInt("finale.run.rounds", 0);
        origDamage = s.getDouble("finale.run.orig-damage", 0.2);
        origBuffer = s.getDouble("finale.run.orig-buffer", 5.0);
        finaleWinner = s.getString("finale.winner", null);
    }

    private void saveFinaleState(YamlConfiguration s) {
        s.set("finale.target-set", finaleTargetSet);
        s.set("finale.target-x", finaleTargetX);
        s.set("finale.target-z", finaleTargetZ);
        s.set("finale.target-size", finaleTargetSize);
        s.set("finale.duration-min", finaleDurationMin);
        s.set("finale.round-min", finaleRoundMin);
        s.set("finale.shrink-sec", finaleShrinkSec);
        s.set("finale.pause-sec", finalePauseSec);
        s.set("finale.damage", finaleDamage);
        s.set("finale.buffer", finaleBuffer);
        s.set("finale.close-dimensions", finaleCloseDims);
        s.set("finale.auto-tie", finaleAutoTie);
        s.set("finale.state", finaleState.name());
        s.set("finale.elapsed-ms", finaleElapsedMs);
        s.set("finale.run.start-x", runStartCx);
        s.set("finale.run.start-z", runStartCz);
        s.set("finale.run.start-size", runStartSize);
        s.set("finale.run.end-x", runEndCx);
        s.set("finale.run.end-z", runEndCz);
        s.set("finale.run.end-size", runEndSize);
        s.set("finale.run.total-ms", runTotalMs);
        s.set("finale.run.shrink-ms", runShrinkMs);
        s.set("finale.run.pause-ms", runPauseMs);
        s.set("finale.run.rounds", runRounds);
        s.set("finale.run.orig-damage", origDamage);
        s.set("finale.run.orig-buffer", origBuffer);
        s.set("finale.winner", finaleWinner);
    }

    /** After a restart a running finale is put on pause: the admin decides when it continues. */
    private void finaleOnEnable() {
        if (finaleState == FinaleState.RUNNING) {
            finaleState = FinaleState.PAUSED;
            finaleRestartNotice = true;
            saveState();
        }
        if (finaleState == FinaleState.PAUSED) {
            finaleFreezeBorder();
        }
    }

    private void finaleOnDisable() {
        if (finaleState != FinaleState.IDLE) {
            saveState();
        }
        for (UUID id : new ArrayList<>(showInvulnerable)) {
            Player p = getServer().getPlayer(id);
            if (p != null) {
                p.setInvulnerable(false);
            }
        }
        showInvulnerable.clear();
    }

    private static String finaleNum(double d) {
        if (Math.abs(d - Math.rint(d)) < 0.05) {
            return String.valueOf((long) Math.rint(d));
        }
        return String.format(Locale.ROOT, "%.1f", d);
    }

    private static double parseNum(String raw) {
        return Double.parseDouble(raw.trim().replace(',', '.'));
    }

    private static String clock(long ms) {
        long s = Math.max(0L, (ms + 999L) / 1000L);
        return String.format(Locale.ROOT, "%02d:%02d", s / 60L, s % 60L);
    }

    private Component finaleLine(Component text) {
        return Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(text)
                .build();
    }

    private void finaleBroadcast(Component text) {
        Component line = finaleLine(text);
        for (Player p : getServer().getOnlinePlayers()) {
            p.sendMessage(line);
        }
    }

    private void finaleNotifyAdmins(String text, TextColor color) {
        for (Player p : getServer().getOnlinePlayers()) {
            if (isAdmin(p)) {
                p.sendMessage(finaleLine(Component.text(text, color)));
            }
        }
    }

    /** Is this world part of the main game (overworld, Nether or End), so not the lobby and not the island? */
    private boolean inMainGroup(World world) {
        String main = gameWorld().getName();
        String name = world.getName();
        return name.equals(main) || name.equals(main + "_nether") || name.equals(main + "_the_end");
    }

    /** Everybody who still counts as alive in the finale. Admins in creative or spectator are not players. */
    private List<Player> finaleAlivePlayers() {
        List<Player> result = new ArrayList<>();
        for (Player p : getServer().getOnlinePlayers()) {
            if (!inMainGroup(p.getWorld())) {
                continue;
            }
            if (isEliminated(p) || isLosersOut(p)) {
                continue;
            }
            GameMode mode = p.getGameMode();
            if (mode != GameMode.SURVIVAL && mode != GameMode.ADVENTURE) {
                continue;
            }
            result.add(p);
        }
        return result;
    }

    // ---- the timeline: continuous shrinking first, then rounds of shrink + pause

    private long finaleContMs() {
        return Math.max(0L, runTotalMs - (long) runRounds * (runShrinkMs + runPauseMs));
    }

    private long finaleShrinkTotal() {
        return finaleContMs() + (long) runRounds * runShrinkMs;
    }

    /**
     * Where the finale is at `e` ms of active time. Returns {phase, shrunkMs, msLeftInPhase, shrunkAtPhaseEnd, index}.
     * Phase 0 = continuous shrink, 1 = round shrink (red), 2 = round pause (blue), 3 = done.
     */
    private long[] finalePhaseAt(long e) {
        long cont = finaleContMs();
        long shrinkTotal = finaleShrinkTotal();
        long cycle = runShrinkMs + runPauseMs;
        if (e < cont) {
            return new long[] {0L, e, cont - e, cont, 0L};
        }
        long r = e - cont;
        long idx = cycle <= 0L ? runRounds : r / cycle;
        if (idx >= runRounds) {
            return new long[] {3L, shrinkTotal, 0L, shrinkTotal, runRounds};
        }
        long within = r - idx * cycle;
        if (within < runShrinkMs) {
            long shrunk = cont + idx * runShrinkMs + within;
            return new long[] {1L, shrunk, runShrinkMs - within, cont + (idx + 1L) * runShrinkMs, idx};
        }
        return new long[] {2L, cont + (idx + 1L) * runShrinkMs, cycle - within, cont + (idx + 1L) * runShrinkMs, idx};
    }

    private double finaleProgress(long shrunkMs) {
        long total = finaleShrinkTotal();
        return total <= 0L ? 1.0 : Math.min(1.0, Math.max(0.0, (double) shrunkMs / total));
    }

    /** Puts the border in the state the phase asks for: red lerp while shrinking, still (blue) while paused. */
    private void finaleApply(long[] ph, boolean announce) {
        WorldBorder border = gameWorld().getWorldBorder();
        int phase = (int) ph[0];
        double p = finaleProgress(ph[1]);
        double sizeNow = runStartSize + (runEndSize - runStartSize) * p;
        double cxNow = runStartCx + (runEndCx - runStartCx) * p;
        double czNow = runStartCz + (runEndCz - runStartCz) * p;
        if (phase == 3) {
            border.setCenter(runEndCx, runEndCz);
            border.setSize(runEndSize);
            if (announce) {
                finaleBroadcast(Component.text("De border heeft zijn eindgrootte bereikt!", NamedTextColor.WHITE));
            }
            return;
        }
        border.setCenter(cxNow, czNow);
        if (phase == 2) {
            border.setSize(sizeNow);
            if (announce) {
                finaleBroadcast(Component.text("De border staat even stil.", NamedTextColor.AQUA));
            }
            return;
        }
        double pEnd = finaleProgress(ph[3]);
        double sizeEnd = runStartSize + (runEndSize - runStartSize) * pEnd;
        border.setSize(sizeNow);
        long seconds = Math.max(1L, (ph[2] + 999L) / 1000L);
        border.setSize(sizeEnd, seconds);
        if (announce && phase == 1) {
            finaleBroadcast(Component.text("De border is aan het krimpen!", NamedTextColor.RED));
        }
    }

    /** Runs every 2 ticks. Moves the centre smoothly and switches phase when it is time. */
    private void tickFinale() {
        if (finaleState != FinaleState.RUNNING) {
            return;
        }
        long now = System.currentTimeMillis();
        long delta = Math.max(0L, Math.min(now - finaleLastTickMs, 5000L));
        finaleLastTickMs = now;
        finaleElapsedMs = Math.min(runTotalMs, finaleElapsedMs + delta);

        long[] ph = finalePhaseAt(finaleElapsedMs);
        int key = (int) (ph[0] * 1000L + ph[4]);
        if (key != finaleAppliedKey) {
            finaleApply(ph, !finaleSilentApply);
            finaleAppliedKey = key;
            finaleSilentApply = false;
        } else if (ph[0] == 0L || ph[0] == 1L) {
            double p = finaleProgress(ph[1]);
            gameWorld().getWorldBorder().setCenter(
                    runStartCx + (runEndCx - runStartCx) * p,
                    runStartCz + (runEndCz - runStartCz) * p);
        }
        finaleTickCount++;
        if (finaleTickCount % 50 == 0) {
            saveState();
        }
    }

    private void finaleFreezeBorder() {
        WorldBorder border = gameWorld().getWorldBorder();
        border.setSize(border.getSize());
    }

    @EventHandler
    public void onFinaleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (finaleState != FinaleState.RUNNING && finaleState != FinaleState.PAUSED) {
            return;
        }
        getServer().getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline()) {
                return;
            }
            if (finaleRestartNotice && finaleState == FinaleState.PAUSED && isAdmin(player)) {
                player.sendMessage(finaleLine(Component.text("De finale stond aan toen de server herstartte en staat nu "
                        + "op pauze. Open /lowkey finale om hem te hervatten.", NamedTextColor.YELLOW)));
            }
        }, 40L);
    }

    @EventHandler
    public void onFinaleQuit(PlayerQuitEvent event) {
        if (showInvulnerable.remove(event.getPlayer().getUniqueId())) {
            event.getPlayer().setInvulnerable(false);
        }
    }

    // ---- start, pause, resume, stop

    private void finaleStart(CommandSender sender) {
        if (finaleState == FinaleState.RUNNING || finaleState == FinaleState.PAUSED) {
            sender.sendMessage(Component.text("De finale is al bezig. Stop hem eerst.", NamedTextColor.RED));
            return;
        }
        WorldBorder border = gameWorld().getWorldBorder();
        double startSize = border.getSize();
        double endSize = finaleTargetSize;
        if (endSize < 1.0 || endSize >= startSize) {
            sender.sendMessage(Component.text("De eindgrootte (" + finaleNum(endSize) + ") moet kleiner zijn dan de "
                    + "huidige border (" + finaleNum(startSize) + "). Pas het doel aan.", NamedTextColor.RED));
            return;
        }
        long total = finaleDurationMin * 60_000L;
        long shrink = finaleShrinkSec * 1000L;
        long pause = finalePauseSec * 1000L;
        long cycle = shrink + pause;
        int rounds = cycle <= 0L ? 0 : (int) (Math.min(finaleRoundMin * 60_000L, total) / cycle);
        runStartCx = border.getCenter().getX();
        runStartCz = border.getCenter().getZ();
        runStartSize = startSize;
        runEndCx = finaleTargetSet ? finaleTargetX : runStartCx;
        runEndCz = finaleTargetSet ? finaleTargetZ : runStartCz;
        runEndSize = endSize;
        runTotalMs = total;
        runShrinkMs = shrink;
        runPauseMs = pause;
        runRounds = rounds;
        origDamage = border.getDamageAmount();
        origBuffer = border.getDamageBuffer();
        border.setDamageAmount(finaleDamage);
        border.setDamageBuffer(finaleBuffer);

        if (finaleCloseDims) {
            if (netherOpen) {
                setNether(false);
            }
            if (endOpen) {
                setEnd(false);
            }
        }

        finaleElapsedMs = 0L;
        finaleWinner = null;
        finaleAnnounced.clear();
        finaleDeaths.clear();
        finaleCheckPending = false;
        finaleRestartNotice = false;
        finaleAppliedKey = -1;
        finaleSilentApply = true;
        finaleLastTickMs = System.currentTimeMillis();
        finaleState = FinaleState.RUNNING;
        saveState();

        int alive = finaleAlivePlayers().size();
        Title title = Title.title(
                Component.text("De finale is van start!", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
                Component.text("Ga bovengronds!", NamedTextColor.WHITE),
                Title.Times.times(Duration.ofMillis(400), Duration.ofMillis(3500), Duration.ofMillis(1000)));
        for (Player p : getServer().getOnlinePlayers()) {
            p.showTitle(title);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.6f, 1.0f);
        }
        finaleBroadcast(Component.text("De finale is van start!", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD));
        finaleBroadcast(Component.text("De border begint te krimpen, ga bovengronds!", NamedTextColor.WHITE));
        finaleBroadcast(Component.text("Er zijn nog " + alive + (alive == 1 ? " speler" : " spelers") + " over.",
                NamedTextColor.GRAY));
        if (sender != null) {
            sender.sendMessage(Component.text("De finale is gestart.", NamedTextColor.GREEN));
        }
    }

    private void finalePause(CommandSender sender) {
        if (finaleState != FinaleState.RUNNING) {
            sender.sendMessage(Component.text("De finale loopt niet.", NamedTextColor.RED));
            return;
        }
        finaleState = FinaleState.PAUSED;
        finaleFreezeBorder();
        saveState();
        finaleBroadcast(Component.text("De finale staat even op pauze.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("De finale staat op pauze. De border blijft staan.", NamedTextColor.GREEN));
    }

    private void finaleResume(CommandSender sender) {
        if (finaleState != FinaleState.PAUSED) {
            sender.sendMessage(Component.text("De finale staat niet op pauze.", NamedTextColor.RED));
            return;
        }
        finaleRestartNotice = false;
        finaleAppliedKey = -1;
        finaleSilentApply = true;
        finaleLastTickMs = System.currentTimeMillis();
        finaleState = FinaleState.RUNNING;
        saveState();
        finaleBroadcast(Component.text("De finale gaat weer verder!", NamedTextColor.WHITE));
        sender.sendMessage(Component.text("De finale loopt weer.", NamedTextColor.GREEN));
    }

    /** Stops the finale and puts the border (and its damage settings) back like it was at the start. */
    private void finaleStop(CommandSender sender) {
        if (finaleState == FinaleState.IDLE) {
            sender.sendMessage(Component.text("Er is geen finale bezig.", NamedTextColor.RED));
            return;
        }
        WorldBorder border = gameWorld().getWorldBorder();
        border.setCenter(runStartCx, runStartCz);
        border.setSize(runStartSize);
        border.setDamageAmount(origDamage);
        border.setDamageBuffer(origBuffer);
        finaleState = FinaleState.IDLE;
        finaleWinner = null;
        finaleElapsedMs = 0L;
        finaleDeaths.clear();
        finaleCheckPending = false;
        finaleRestartNotice = false;
        saveState();
        sender.sendMessage(Component.text("De finale is gestopt en de border staat weer als bij de start.",
                NamedTextColor.GREEN));
    }

    // ---- eliminations, the player count and the winner

    /** Called from onDeath when somebody is eliminated. The check runs 2 ticks later, so deaths in the same tick count together. */
    private void finaleOnElimination(Player player) {
        if (finaleState != FinaleState.RUNNING) {
            return;
        }
        finaleDeaths.add(new FinaleDeath(player.getUniqueId(), getServer().getCurrentTick()));
        if (finaleCheckPending) {
            return;
        }
        finaleCheckPending = true;
        getServer().getScheduler().runTaskLater(this, this::finaleCheck, 2L);
    }

    private void finaleCheck() {
        finaleCheckPending = false;
        if (finaleState != FinaleState.RUNNING) {
            finaleDeaths.clear();
            return;
        }
        List<Player> alive = finaleAlivePlayers();
        int n = alive.size();
        if (n == 1) {
            finaleWin(alive.get(0));
            return;
        }
        if (n == 0) {
            finaleTie();
            return;
        }
        if ((n == 10 || n == 5 || n == 3 || n == 2) && finaleAnnounced.add(n)) {
            finaleBroadcast(Component.text()
                    .append(Component.text("Nog ", NamedTextColor.WHITE))
                    .append(Component.text(String.valueOf(n), NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD))
                    .append(Component.text(" spelers over!", NamedTextColor.WHITE))
                    .build());
        }
    }

    /** Nobody is left: the last two (or more) died in the same tick, or the only player died. */
    private void finaleTie() {
        int now = getServer().getCurrentTick();
        List<Player> candidates = new ArrayList<>();
        Player last = null;
        for (FinaleDeath d : finaleDeaths) {
            Player p = getServer().getPlayer(d.id);
            if (p == null) {
                continue;
            }
            last = p;
            if (now - d.tick <= 5 && !candidates.contains(p)) {
                candidates.add(p);
            }
        }
        finaleDeaths.clear();
        if (last == null) {
            finaleNotifyAdmins("Niemand is meer over in de finale en er is geen winnaar te bepalen.", NamedTextColor.RED);
            return;
        }
        if (finaleAutoTie) {
            finaleNotifyAdmins("Gelijktijdige dood: " + last.getName() + " was als laatste aan de beurt en is teruggehaald "
                    + "als winnaar.", NamedTextColor.YELLOW);
            finaleReviveInPlace(last);
            finaleWin(last);
            return;
        }
        if (candidates.isEmpty()) {
            candidates.add(last);
        }
        final List<Player> choices = candidates;
        for (Player admin : getServer().getOnlinePlayers()) {
            if (!isAdmin(admin)) {
                continue;
            }
            admin.sendMessage(finaleLine(Component.text("Gelijkspel in de finale. Kies de winnaar in het menu.",
                    NamedTextColor.YELLOW)));
            List<ActionButton> buttons = new ArrayList<>();
            for (Player c : choices) {
                buttons.add(button(Component.text(c.getName()), "Maak " + c.getName() + " de winnaar.", 150, () -> {
                    if (finaleState != FinaleState.RUNNING) {
                        admin.sendMessage(Component.text("De winnaar is al bepaald.", NamedTextColor.GRAY));
                        return;
                    }
                    if (!c.isOnline()) {
                        admin.sendMessage(Component.text("Die speler is niet meer online.", NamedTextColor.RED));
                        return;
                    }
                    finaleReviveInPlace(c);
                    finaleWin(c);
                }));
            }
            ActionButton later = ActionButton.create(Component.text("Later beslissen", NamedTextColor.GRAY),
                    Component.text("Sluit dit menu."), 150, null);
            showMenu(admin, "Gelijkspel", Component.text("Meerdere spelers gingen tegelijk dood. Wie wint er?",
                    NamedTextColor.WHITE), buttons, later);
        }
    }

    /** Full health, alive again, on the spot where he died (he is in spectator for a tick or two). */
    private void finaleReviveInPlace(Player p) {
        setEliminated(p, false);
        setLosersOut(p, false);
        p.setGameMode(GameMode.SURVIVAL);
        AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);
        p.setHealth(maxHealth == null ? 20.0 : maxHealth.getValue());
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.setFallDistance(0f);
        World w = p.getWorld();
        if (p.getLocation().getY() < w.getMinHeight() + 5) {
            p.teleport(w.getSpawnLocation());
        }
        refreshTags(p);
    }

    private void finaleWin(Player winner) {
        finaleState = FinaleState.ENDED;
        finaleWinner = winner.getName();
        finaleDeaths.clear();
        finaleFreezeBorder();
        saveState();
        winnerShow(winner, false);
        finaleNotifyAdmins("De finale is afgelopen. " + winner.getName() + " heeft gewonnen. Zet de border terug met "
                + "/lowkey finale stop.", NamedTextColor.GREEN);
    }

    /**
     * Title, chat and about 20 seconds of fireworks in a ring around the winner. With test = true it is only for the
     * admin who pressed the button and nothing about the finale changes.
     */
    private void winnerShow(Player winner, boolean test) {
        List<Player> audience = new ArrayList<>();
        if (test) {
            audience.add(winner);
        } else {
            audience.addAll(getServer().getOnlinePlayers());
        }
        Side side = getSide(winner);
        TextColor nameColor = side == Side.NONE ? NamedTextColor.WHITE : TextColor.color(side.rgb);
        Component subtitle = MiniMessage.miniMessage()
                .deserialize("<gradient:#c084fc:#f472b6><bold>heeft LowkeySMP gewonnen!</bold></gradient>");
        Title title = Title.title(
                Component.text(winner.getName(), nameColor, TextDecoration.BOLD),
                subtitle,
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(7000), Duration.ofMillis(1500)));
        Component chat = finaleLine(Component.text()
                .append(Component.text(winner.getName(), nameColor, TextDecoration.BOLD))
                .append(Component.text(" heeft LowkeySMP gewonnen!", NamedTextColor.WHITE))
                .append(Component.text(test ? " (test)" : "", NamedTextColor.GRAY))
                .build());
        for (Player p : audience) {
            p.showTitle(title);
            p.sendMessage(chat);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        }

        // fireworks hurt everybody within about 5 blocks, so the ring is wider than that and the winner is safe
        winner.setInvulnerable(true);
        showInvulnerable.add(winner.getUniqueId());
        new BukkitRunnable() {
            int runs = 0;

            @Override
            public void run() {
                if (!winner.isOnline() || runs >= 40) {
                    if (showInvulnerable.remove(winner.getUniqueId()) && winner.isOnline()) {
                        winner.setInvulnerable(false);
                    }
                    cancel();
                    return;
                }
                Location center = winner.getLocation();
                World world = center.getWorld();
                if (world != null) {
                    for (int k = 0; k < 3; k++) {
                        double angle = Math.toRadians(runs * 37 + k * 120 + finaleRandom.nextInt(30));
                        double radius = 8.0 + finaleRandom.nextDouble() * 4.0;
                        Location spot = center.clone().add(Math.cos(angle) * radius, 1.0, Math.sin(angle) * radius);
                        boolean burst = finaleRandom.nextBoolean();
                        if (burst) {
                            spot.setY(center.getY() + 3.0 + finaleRandom.nextInt(6));
                        }
                        Firework firework = world.spawn(spot, Firework.class, f -> {
                            FireworkMeta meta = f.getFireworkMeta();
                            meta.addEffect(finaleEffect());
                            meta.setPower(1 + finaleRandom.nextInt(2));
                            f.setFireworkMeta(meta);
                        });
                        if (burst) {
                            firework.detonate();
                        }
                    }
                }
                runs++;
            }
        }.runTaskTimer(this, 10L, 10L);
    }

    private FireworkEffect finaleEffect() {
        Color[] palette = {
                Color.fromRGB(0xC0, 0x84, 0xFC), Color.fromRGB(0xF4, 0x72, 0xB6), Color.WHITE, Color.YELLOW, Color.AQUA
        };
        FireworkEffect.Type[] types = {
                FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.STAR, FireworkEffect.Type.BURST,
                FireworkEffect.Type.BALL, FireworkEffect.Type.CREEPER
        };
        return FireworkEffect.builder()
                .with(types[finaleRandom.nextInt(types.length)])
                .withColor(palette[finaleRandom.nextInt(palette.length)])
                .withColor(palette[finaleRandom.nextInt(palette.length)])
                .withFade(palette[finaleRandom.nextInt(palette.length)])
                .flicker(finaleRandom.nextBoolean())
                .trail(finaleRandom.nextBoolean())
                .build();
    }

    // ---- command: /lowkey finale <menu|start|pauze|hervat|stop|status|test>

    private void handleFinaleCommand(CommandSender sender, String[] args) {
        String sub = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "menu";
        switch (sub) {
            case "start":
                finaleStart(sender);
                return;
            case "pauze":
            case "pause":
                finalePause(sender);
                return;
            case "hervat":
            case "resume":
                finaleResume(sender);
                return;
            case "stop":
                finaleStop(sender);
                return;
            case "status":
                sender.sendMessage(finaleStatusBody());
                return;
            case "test":
                if (sender instanceof Player) {
                    winnerShow((Player) sender, true);
                } else {
                    sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
                }
                return;
            default:
                if (sender instanceof Player) {
                    openFinaleMenu((Player) sender);
                } else {
                    sender.sendMessage(finaleStatusBody());
                }
        }
    }

    // ---- menu

    private String finaleStateText() {
        switch (finaleState) {
            case RUNNING:
                return "bezig";
            case PAUSED:
                return "gepauzeerd";
            case ENDED:
                return "afgelopen";
            default:
                return "niet gestart";
        }
    }

    private Component finaleStatusBody() {
        boolean active = finaleState != FinaleState.IDLE;
        double cx = finaleTargetSet ? finaleTargetX : gameWorld().getWorldBorder().getCenter().getX();
        double cz = finaleTargetSet ? finaleTargetZ : gameWorld().getWorldBorder().getCenter().getZ();
        List<Component> lines = new ArrayList<>();
        lines.add(statusLine("Finale", finaleStateText(), finaleState == FinaleState.RUNNING ? NamedTextColor.GREEN
                : (finaleState == FinaleState.IDLE ? NamedTextColor.YELLOW : NamedTextColor.AQUA)));
        lines.add(statusLine("Border nu", finaleNum(gameWorld().getWorldBorder().getSize()) + " blokken",
                NamedTextColor.WHITE));
        lines.add(statusLine("Doel", "x " + finaleNum(cx) + ", z " + finaleNum(cz) + ", " + finaleNum(finaleTargetSize)
                + " blokken" + (finaleTargetSet ? "" : " (midden blijft)"), NamedTextColor.WHITE));
        lines.add(statusLine("Duur", finaleDurationMin + " min, laatste " + finaleRoundMin + " min in rondes van "
                + finaleShrinkSec + " s krimpen en " + finalePauseSec + " s pauze", NamedTextColor.WHITE));
        if (active && finaleState != FinaleState.ENDED) {
            lines.add(statusLine("Tijd over", clock(Math.max(0L, runTotalMs - finaleElapsedMs)), NamedTextColor.WHITE));
        }
        if (finaleState == FinaleState.ENDED && finaleWinner != null) {
            lines.add(statusLine("Winnaar", finaleWinner, NamedTextColor.LIGHT_PURPLE));
        }
        lines.add(statusLine("Spelers over", String.valueOf(finaleAlivePlayers().size()), NamedTextColor.WHITE));
        return joinLines(lines.toArray(new Component[0]));
    }

    private void openFinaleMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openFinaleMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();
        if (finaleState == FinaleState.IDLE) {
            buttons.add(button(iconLabel(M_LAUNCH, "Finale starten", NamedTextColor.GREEN),
                    "Titel, bericht en de border begint te krimpen.", 150, () -> confirm(admin,
                            "Finale starten?",
                            "De border begint direct te krimpen naar het doel en iedereen krijgt de titel.",
                            "De Nether en het End gaan automatisch dicht.",
                            Component.text("Ja, starten", NamedTextColor.GREEN),
                            () -> {
                                finaleStart(admin);
                                again.run();
                            },
                            again)));
        } else if (finaleState == FinaleState.RUNNING) {
            buttons.add(button(iconLabel(M_REDSTONE, "Pauzeren", NamedTextColor.YELLOW),
                    "Zet de border stil en stopt de tijd.", 150, () -> {
                        finalePause(admin);
                        again.run();
                    }));
        } else if (finaleState == FinaleState.PAUSED) {
            buttons.add(button(iconLabel(M_LAUNCH, "Hervatten", NamedTextColor.GREEN),
                    "De border en de tijd lopen verder.", 150, () -> {
                        finaleResume(admin);
                        again.run();
                    }));
        }
        if (finaleState != FinaleState.IDLE) {
            buttons.add(button(iconLabel(M_BARRIER, "Stoppen en terugzetten", NamedTextColor.RED),
                    "Stopt de finale en zet de border terug zoals bij de start.", 150, () -> confirm(admin,
                            "Finale stoppen?",
                            "De border gaat meteen terug naar zijn grootte en plek van voor de finale.",
                            "De finale begint daarna weer van nul als je hem opnieuw start.",
                            Component.text("Ja, stoppen", NamedTextColor.RED),
                            () -> {
                                finaleStop(admin);
                                again.run();
                            },
                            again)));
        }
        buttons.add(button(iconLabel(M_COMPASS, "Doel en grootte", NamedTextColor.WHITE),
                "Midden, eindgrootte en je eigen positie als doel.", 150, () -> openFinaleTargetDialog(admin)));
        buttons.add(button(iconLabel(M_COUNTDOWN, "Tijden en schade", NamedTextColor.WHITE),
                "Duur, rondes, pauzes en de schade buiten de border.", 150, () -> openFinaleTimeDialog(admin)));
        buttons.add(button(iconLabel(M_STAR, "Winnaarsshow testen", NamedTextColor.AQUA),
                "Title en vuurwerk rond jou. Raakt niemand anders en niets van de finale.", 150,
                () -> winnerShow(admin, true)));

        showMenu(admin, "Finale", finaleStatusBody(), buttons,
                backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    private void openFinaleTargetDialog(Player admin) {
        WorldBorder border = gameWorld().getWorldBorder();
        double curX = finaleTargetSet ? finaleTargetX : border.getCenter().getX();
        double curZ = finaleTargetSet ? finaleTargetZ : border.getCenter().getZ();
        DialogInput x = DialogInput.text("x", Component.text("Midden X"))
                .width(300).initial(finaleNum(curX)).maxLength(12).build();
        DialogInput z = DialogInput.text("z", Component.text("Midden Z"))
                .width(300).initial(finaleNum(curZ)).maxLength(12).build();
        DialogInput size = DialogInput.text("grootte", Component.text("Eindgrootte (diameter in blokken)"))
                .width(300).initial(finaleNum(finaleTargetSize)).maxLength(10).build();
        DialogAction save = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final String sx = view.getText("x");
            final String sz = view.getText("z");
            final String sg = view.getText("grootte");
            getServer().getScheduler().runTask(this, () -> {
                applyFinaleTarget(clicker, sx, sz, sg);
                openFinaleMenu(clicker);
            });
        }, CLICK_OPTIONS);
        DialogAction here = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final String sg = view.getText("grootte");
            getServer().getScheduler().runTask(this, () -> {
                if (!inMainGroup(clicker.getWorld())) {
                    clicker.sendMessage(Component.text("Ga in de main wereld staan om je positie te gebruiken.",
                            NamedTextColor.RED));
                } else {
                    applyFinaleTarget(clicker, String.valueOf(clicker.getLocation().getBlockX() + 0.5),
                            String.valueOf(clicker.getLocation().getBlockZ() + 0.5), sg);
                }
                openFinaleTargetDialog(clicker);
            });
        }, CLICK_OPTIONS);
        List<ActionButton> actions = new ArrayList<>();
        actions.add(ActionButton.create(iconLabel(M_LAUNCH, "Opslaan", NamedTextColor.GREEN),
                Component.text("Sla het doel op."), 300, save));
        actions.add(ActionButton.create(iconLabel(M_COMPASS, "Mijn positie gebruiken", NamedTextColor.AQUA),
                Component.text("Zet het midden op waar jij staat en slaat de eindgrootte ook op."), 300, here));
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Finale doel"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Waar de border naartoe krimpt. Geldt voor de volgende start.", NamedTextColor.GRAY))))
                        .inputs(List.of(x, z, size))
                        .build())
                .type(DialogType.multiAction(actions, backButton("Terug naar de finale.",
                        () -> openFinaleMenu(admin)), 1)));
        admin.showDialog(dialog);
    }

    private void applyFinaleTarget(Player admin, String sx, String sz, String sg) {
        try {
            double x = parseNum(sx == null ? "" : sx);
            double z = parseNum(sz == null ? "" : sz);
            double size = parseNum(sg == null ? "" : sg);
            if (Math.abs(x) > 29_999_000.0 || Math.abs(z) > 29_999_000.0) {
                admin.sendMessage(Component.text("Het midden ligt te ver weg (maximaal 29999000).", NamedTextColor.RED));
                return;
            }
            if (size < 1.0 || size > 59_999_000.0) {
                admin.sendMessage(Component.text("De eindgrootte moet tussen 1 en 59999000 liggen.", NamedTextColor.RED));
                return;
            }
            finaleTargetX = x;
            finaleTargetZ = z;
            finaleTargetSize = size;
            finaleTargetSet = true;
            saveState();
            admin.sendMessage(Component.text("Het doel is opgeslagen.", NamedTextColor.GREEN));
        } catch (NumberFormatException e) {
            admin.sendMessage(Component.text("Geef getallen op voor het midden en de grootte.", NamedTextColor.RED));
        }
    }

    private void openFinaleTimeDialog(Player admin) {
        DialogInput duration = DialogInput.text("duur", Component.text("Totale duur (minuten)"))
                .width(300).initial(String.valueOf(finaleDurationMin)).maxLength(3).build();
        DialogInput rounds = DialogInput.text("ronde", Component.text("Laatste minuten in rondes"))
                .width(300).initial(String.valueOf(finaleRoundMin)).maxLength(3).build();
        DialogInput shrink = DialogInput.text("krimp", Component.text("Krimpen per ronde (seconden)"))
                .width(300).initial(String.valueOf(finaleShrinkSec)).maxLength(4).build();
        DialogInput pause = DialogInput.text("pauze", Component.text("Pauze per ronde (seconden)"))
                .width(300).initial(String.valueOf(finalePauseSec)).maxLength(4).build();
        DialogInput damage = DialogInput.text("schade", Component.text("Schade per blok per seconde buiten de border"))
                .width(300).initial(finaleNum(finaleDamage)).maxLength(6).build();
        DialogInput buffer = DialogInput.text("buffer", Component.text("Veilige afstand buiten de border (blokken)"))
                .width(300).initial(finaleNum(finaleBuffer)).maxLength(6).build();
        DialogAction save = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final String sd = view.getText("duur");
            final String sr = view.getText("ronde");
            final String sk = view.getText("krimp");
            final String sp = view.getText("pauze");
            final String sc = view.getText("schade");
            final String sb = view.getText("buffer");
            getServer().getScheduler().runTask(this, () -> {
                applyFinaleTimes(clicker, sd, sr, sk, sp, sc, sb);
                openFinaleMenu(clicker);
            });
        }, CLICK_OPTIONS);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Finale tijden"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "In de laatste minuten krimpt de border in rondes met pauzes ertussen. Geldt voor de "
                                        + "volgende start.", NamedTextColor.GRAY))))
                        .inputs(List.of(duration, rounds, shrink, pause, damage, buffer))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_COUNTDOWN, "Opslaan", NamedTextColor.GREEN),
                                Component.text("Sla de tijden op."), 150, save),
                        backButton("Terug naar de finale.", () -> openFinaleMenu(admin)))));
        admin.showDialog(dialog);
    }

    private void applyFinaleTimes(Player admin, String sd, String sr, String sk, String sp, String sc, String sb) {
        try {
            int duration = (int) Math.round(parseNum(sd == null ? "" : sd));
            int rounds = (int) Math.round(parseNum(sr == null ? "" : sr));
            int shrink = (int) Math.round(parseNum(sk == null ? "" : sk));
            int pause = (int) Math.round(parseNum(sp == null ? "" : sp));
            double damage = parseNum(sc == null ? "" : sc);
            double buffer = parseNum(sb == null ? "" : sb);
            if (duration < 1 || duration > 180) {
                admin.sendMessage(Component.text("De duur moet tussen 1 en 180 minuten liggen.", NamedTextColor.RED));
                return;
            }
            if (rounds < 0 || rounds > duration) {
                admin.sendMessage(Component.text("De rondes mogen niet langer duren dan de totale duur.",
                        NamedTextColor.RED));
                return;
            }
            if (shrink < 5 || shrink > 3600 || pause < 0 || pause > 3600) {
                admin.sendMessage(Component.text("Krimpen: 5 tot 3600 seconden. Pauze: 0 tot 3600 seconden.",
                        NamedTextColor.RED));
                return;
            }
            if (damage < 0.0 || damage > 100.0 || buffer < 0.0 || buffer > 1000.0) {
                admin.sendMessage(Component.text("Schade: 0 tot 100. Veilige afstand: 0 tot 1000.", NamedTextColor.RED));
                return;
            }
            finaleDurationMin = duration;
            finaleRoundMin = rounds;
            finaleShrinkSec = shrink;
            finalePauseSec = pause;
            finaleDamage = damage;
            finaleBuffer = buffer;
            saveState();
            admin.sendMessage(Component.text("De tijden zijn opgeslagen.", NamedTextColor.GREEN));
        } catch (NumberFormatException e) {
            admin.sendMessage(Component.text("Geef getallen op.", NamedTextColor.RED));
        }
    }

    // ---- testing and repairing

    /** A real revive: alive again, in the lobby with the star or at the team spawn, wherever he was. */
    private void revivePlayer(CommandSender sender, Player target) {
        if (!isEliminated(target) && !isLosersOut(target)) {
            sender.sendMessage(Component.text(target.getName() + " is niet uitgeschakeld.", NamedTextColor.RED));
            return;
        }
        if (inLobby(target)) {
            clearEliminated(target);
        } else {
            sendBackAlive(target);
        }
        sender.sendMessage(Component.text(target.getName() + " leeft weer.", NamedTextColor.GREEN));
    }

    /** Puts every switch of the season back to the start (nobody is moved, no team changes). For testing. */
    private void resetSeasonState(CommandSender sender) {
        launched = false;
        mainOpen = true;
        netherOpen = false;
        endOpen = false;
        bordersDropped = false;
        losersOpen = false;
        losersEnded = false;
        saveState();
        sender.sendMessage(Component.text("Alle schakelaars staan weer als aan het begin: niet gelanceerd, main wereld "
                + "open, Nether, End en grenzen dicht, Verliezerseiland dicht. Niemand is verplaatst en "
                + "geen team is veranderd.", NamedTextColor.GREEN));
    }

    private void openRepairMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openRepairMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_REVIVE, "Speler reviven", NamedTextColor.WHITE),
                "Zet een uitgeschakelde speler weer levend, waar hij ook is.", 150,
                () -> pickPlayer(admin, "Speler reviven", target -> {
                    revivePlayer(admin, target);
                    again.run();
                }, again)));
        buttons.add(button(iconLabel(M_HEART, "Terug uit Verliezerseiland", NamedTextColor.WHITE),
                "Kies een speler die uit is en zet hem terug in de main wereld.", 150, () -> openLosersPick(admin)));
        buttons.add(button(iconLabel(M_LAVA, "Alle teams resetten", NamedTextColor.RED),
                "Haalt van iedereen het team weg.", 150, () -> confirm(admin,
                        "Alle teams resetten?",
                        "Iedereen, ook wie offline is, heeft daarna geen team meer.",
                        "Dit kan niet ongedaan gemaakt worden.",
                        Component.text("Ja, resetten", NamedTextColor.RED),
                        () -> {
                            resetAllTeams(admin);
                            again.run();
                        },
                        again)));
        if (mainOpen) {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld sluiten", NamedTextColor.RED),
                    "Iedereen behalve admins gaat naar de lobby.", 150, () -> confirm(admin,
                            "Main wereld sluiten?",
                            "Iedereen behalve admins gaat naar de lobby en kan er niet uit tot je de main wereld opent.",
                            "Spelers houden hun spullen en komen later terug op hun laatste plek.",
                            Component.text("Ja, sluiten", NamedTextColor.RED),
                            () -> {
                                closeMainWorld(admin);
                                again.run();
                            },
                            again)));
        } else {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld openen", NamedTextColor.GREEN),
                    "Spelers in de lobby kunnen er weer in met hun item. Niemand wordt verplaatst.", 150, () -> {
                        openMainWorld(admin);
                        again.run();
                    }));
        }
        if (launched) {
            buttons.add(button(iconLabel(M_LAUNCH, "Launch resetten", NamedTextColor.RED),
                    "Zet de launch terug. Niemand wordt verplaatst.", 150, () -> {
                        resetLaunch(admin);
                        again.run();
                    }));
        } else {
            buttons.add(button(iconLabel(M_LAUNCH, "Server launchen", NamedTextColor.GREEN),
                    "Noord gaat naar de spawn van Noord, Zuid naar die van Zuid.", 150,
                    () -> openLaunchConfirm(admin, again)));
        }
        buttons.add(button(iconLabel(M_COMPASS, "Spawn terugzetten voor iedereen", NamedTextColor.WHITE),
                "Iedereen komt de volgende keer op zijn team spawn in plaats van op zijn laatste plek.", 150,
                () -> confirm(admin,
                        "Spawn terugzetten voor iedereen?",
                        "Van iedereen, ook wie offline is, wordt de opgeslagen laatste plek gewist.",
                        "Daarna staan ze op hun team spawn als ze de main wereld ingaan. Niemand wordt nu verplaatst.",
                        Component.text("Ja, terugzetten", NamedTextColor.GREEN),
                        () -> {
                            int n = clearAllLastLocations();
                            admin.sendMessage(Component.text("De laatste plek van " + n + " spelers is gewist.",
                                    NamedTextColor.GREEN));
                            again.run();
                        },
                        again)));
        buttons.add(button(iconLabel(M_BARRIER, "Inventory legen voor iedereen", NamedTextColor.RED),
                "Leegt inventory, armor, offhand, ender chest en XP van iedereen behalve admins, ook offline.", 150,
                () -> confirm(admin,
                        "Inventory van iedereen legen?",
                        "Alles van alle spelers behalve admins: inventory, armor, offhand, ender chest, XP en effecten, ook "
                                + "van spelers die offline zijn.",
                        "De sterren en het regelboekje blijven. Dit kan niet ongedaan gemaakt worden.",
                        Component.text("Ja, alles legen", NamedTextColor.RED),
                        () -> {
                            String result = clearEveryoneInventory(admin);
                            admin.sendMessage(Component.text(result, NamedTextColor.GREEN));
                            again.run();
                        },
                        again)));
        buttons.add(button(iconLabel(M_WATER, "Mace cooldown wissen", NamedTextColor.WHITE),
                "Haalt de lopende cooldown van de mace weg, handig na een test op een bot.", 150, () -> {
                    mace.resetCooldown(admin);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_FIRE, "Alles terugzetten", NamedTextColor.RED),
                "Zet launch, Nether, End, grenzen en Verliezerseiland terug naar het begin.", 150,
                () -> confirm(admin,
                        "Alle schakelaars terugzetten?",
                        "Launch, Nether, End, grenzen en Verliezerseiland gaan terug naar het begin.",
                        "Niemand wordt verplaatst en geen team verandert.",
                        Component.text("Ja, terugzetten", NamedTextColor.RED),
                        () -> {
                            resetSeasonState(admin);
                            again.run();
                        },
                        again)));
        Component body = joinLines(
                Component.text("Main wereld sluiten: iedereen (behalve admins) gaat naar de lobby.", NamedTextColor.GRAY),
                Component.text("Launch: Noord gaat naar spawn Noord, Zuid naar spawn Zuid.", NamedTextColor.GRAY),
                statusLine("Main wereld", mainOpen ? "open" : "gesloten",
                        mainOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Testen en herstellen", body, buttons, backButton("Terug naar het hoofdmenu.",
                () -> openMenu(admin)));
    }

    // ------------------------------------------------------------------ Verliezerseiland (own world for everybody who died)

    private static final String LOSERS_WORLD = "lowkey_losers";
    private boolean losersCreated;
    /** false until an admin opens it: until then the red star only says it is not open yet */
    private boolean losersOpen;
    /** true after the finale: the red star is gone for everybody and cannot come back until it is opened again */
    private boolean losersEnded;
    private Location losersSpawn;
    private NamespacedKey losersItemKey;
    private int losersAmbient;

    private static final Sound[] LOSERS_SOUNDS = {Sound.AMBIENT_CAVE, Sound.ENTITY_WITHER_AMBIENT,
            Sound.BLOCK_PORTAL_AMBIENT, Sound.ENTITY_GHAST_AMBIENT};

    private World losersWorld() {
        return Bukkit.getWorld(LOSERS_WORLD);
    }

    private World loadLosersWorld() {
        return loadLosersWorld(null);
    }

    /** A normal survival world with real terrain. An existing world folder is loaded as it is. */
    private World loadLosersWorld(Long seed) {
        World existing = Bukkit.getWorld(LOSERS_WORLD);
        if (existing != null) {
            return existing;
        }
        WorldCreator creator = new WorldCreator(LOSERS_WORLD).environment(World.Environment.NORMAL);
        if (seed != null) {
            creator.seed(seed);
        }
        World world = creator.createWorld();
        if (world == null) {
            getLogger().warning("Kon de Verliezerseiland wereld niet laden.");
        }
        return world;
    }

    boolean inLosers(Player player) {
        return losersSpawn != null && losersSpawn.getWorld() != null && player.getWorld().equals(losersSpawn.getWorld());
    }

    /** Lobby, main world and Verliezerseiland each have their own tab list, chat and private messages. */
    private int worldGroup(Player player) {
        if (inLosers(player)) {
            return 2;
        }
        return inLobby(player) ? 0 : 1;
    }

    // ---- the red star

    private boolean isLosersItem(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(losersItemKey, PersistentDataType.BYTE);
    }

    private ItemStack createLosersItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Verliezerseiland", NamedTextColor.RED, TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Rechtermuisklik om naar het Verliezerseiland te gaan.", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)));
        meta.setEnchantmentGlintOverride(true);
        NamespacedKey model = NamespacedKey.fromString("lowkey:losers_star"); // red star from the resource pack
        if (model != null) {
            meta.setItemModel(model);
        }
        meta.getPersistentDataContainer().set(losersItemKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private void giveLosersItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isLosersItem(item)) {
                return;
            }
        }
        player.getInventory().setItem(5, createLosersItem());
    }

    private void removeLosersItems(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (isLosersItem(contents[i])) {
                player.getInventory().setItem(i, null);
            }
        }
    }

    /** Right-click on the red star. */
    private void useLosersItem(Player player) {
        if (!inLobby(player) || !isEliminated(player)) {
            return;
        }
        if (isLosersOut(player)) {
            player.sendActionBar(Component.text("Je bent definitief uitgeschakeld.", NamedTextColor.RED));
            return;
        }
        if (losersEnded) {
            player.sendActionBar(Component.text("Het Verliezerseiland is afgelopen.", NamedTextColor.RED));
            return;
        }
        if (!losersOpen || losersSpawn == null || losersSpawn.getWorld() == null) {
            player.sendActionBar(Component.text("Verliezerseiland is nog niet geopend!", NamedTextColor.RED));
            return;
        }
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Verliezerseiland", NamedTextColor.RED, TextDecoration.BOLD))
                        .body(List.of(DialogBody.plainMessage(
                                Component.text("Wil je naar het Verliezerseiland teleporteren?", NamedTextColor.WHITE))))
                        .build())
                .type(DialogType.confirmation(
                        playerButton(Component.text("Ja", NamedTextColor.GREEN),
                                "Teleporteer naar het Verliezerseiland.", 100, this::teleportToLosers),
                        ActionButton.create(Component.text("Nee", NamedTextColor.RED),
                                Component.text("Sluit dit venster."), 100, null))));
        player.showDialog(dialog);
    }

    private void teleportToLosers(Player player) {
        if (!player.isOnline() || !inLobby(player) || !isEliminated(player) || !losersOpen || isLosersOut(player)
                || losersSpawn == null || losersSpawn.getWorld() == null) {
            return;
        }
        removeLobbyItems(player);
        removeLosersItems(player);
        if (!isBypass(player)) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
        player.setHealth(maxHealth == null ? 20.0 : maxHealth.getValue());
        player.setFoodLevel(20);
        player.setFallDistance(0f);
        player.teleport(losersSpawn);
    }

    /** Gives the red star to everybody who is eliminated and waiting in the lobby. */
    private void giveLosersItemsInLobby() {
        for (Player p : getServer().getOnlinePlayers()) {
            if (inLobby(p) && isEliminated(p) && !isLosersOut(p)) {
                giveLosersItem(p);
            }
        }
    }

    // ---- admin actions

    private void createLosers(CommandSender sender, Long seed) {
        if (losersCreated && losersWorld() != null) {
            sender.sendMessage(Component.text("Het Verliezerseiland bestaat al.", NamedTextColor.RED));
            return;
        }
        World world = loadLosersWorld(seed);
        if (world == null) {
            sender.sendMessage(Component.text("De wereld kon niet gemaakt worden, kijk in de console.",
                    NamedTextColor.RED));
            return;
        }
        losersCreated = true;
        Location base = world.getSpawnLocation();
        int y = world.getHighestBlockYAt(base.getBlockX(), base.getBlockZ()) + 1;
        losersSpawn = new Location(world, base.getBlockX() + 0.5, y, base.getBlockZ() + 0.5, 0f, 0f);
        saveState();
        sender.sendMessage(Component.text("Het Verliezerseiland is gemaakt: een normale survival wereld (seed "
                + world.getSeed() + ") met altijd onweer en nacht. Het spawnpunt staat op de wereld spawn, verplaats het "
                + "met /lowkey losers setspawn.", NamedTextColor.GREEN));
    }

    private void teleportAdminToLosers(Player admin) {
        if (losersSpawn == null || losersSpawn.getWorld() == null) {
            admin.sendMessage(Component.text("Maak eerst het Verliezerseiland aan.", NamedTextColor.RED));
            return;
        }
        admin.teleport(losersSpawn);
        // like a player who goes through the red star: the lobby items (star and rules book) stay behind
        removeLobbyItems(admin);
    }

    private void setLosersSpawn(Player admin) {
        if (losersWorld() == null || !admin.getWorld().equals(losersWorld())) {
            admin.sendMessage(Component.text("Je moet op het Verliezerseiland staan om het spawnpunt te zetten.",
                    NamedTextColor.RED));
            return;
        }
        losersSpawn = admin.getLocation().clone();
        saveState();
        admin.sendMessage(Component.text("Het spawnpunt van het Verliezerseiland staat nu op jouw plek.",
                NamedTextColor.GREEN));
    }

    private void openLosers(CommandSender sender) {
        if (!losersCreated || losersSpawn == null) {
            sender.sendMessage(Component.text("Maak eerst het Verliezerseiland aan.", NamedTextColor.RED));
            return;
        }
        if (losersOpen) {
            sender.sendMessage(Component.text("Het Verliezerseiland is al open.", NamedTextColor.YELLOW));
            return;
        }
        losersOpen = true;
        losersEnded = false;
        saveState();
        giveLosersItemsInLobby();
        Title title = Title.title(
                Component.text("Verliezerseiland is open!", NamedTextColor.RED),
                Component.text("Gebruik de rode ster in je hotbar", NamedTextColor.GRAY),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(3000), Duration.ofMillis(1000)));
        int told = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            if (isEliminated(p)) {
                p.sendMessage(Component.text()
                        .append(glyph(LOWKEY_BADGE_RED))
                        .append(Component.space())
                        .append(Component.text("Het Verliezerseiland is open! Gebruik de rode ster in je hotbar.",
                                NamedTextColor.RED))
                        .build());
                p.showTitle(title);
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.6f, 0.8f);
                told++;
            }
        }
        sender.sendMessage(Component.text("Het Verliezerseiland is open. " + told + " uitgeschakelde spelers zijn "
                + "verwittigd.", NamedTextColor.GREEN));
    }

    private void closeLosers(CommandSender sender) {
        if (!losersOpen) {
            sender.sendMessage(Component.text("Het Verliezerseiland is al dicht.", NamedTextColor.YELLOW));
            return;
        }
        losersOpen = false;
        saveState();
        sender.sendMessage(Component.text("Het Verliezerseiland is dicht. Wie er al is blijft er, nieuwe spelers kunnen "
                + "er niet meer heen.", NamedTextColor.GREEN));
    }

    /** Puts a loser back into the real game: alive again, at the spawn of his team. */
    private void sendBackAlive(Player target) {
        setEliminated(target, false);
        setLosersOut(target, false);
        removeLosersItems(target);
        removeLobbyItems(target);
        Location dest = teamSpawn(getSide(target));
        if (dest == null || dest.getWorld() == null) {
            dest = gameWorld().getSpawnLocation();
        }
        if (!isBypass(target)) {
            target.setGameMode(GameMode.SURVIVAL);
        }
        AttributeInstance maxHealth = target.getAttribute(Attribute.MAX_HEALTH);
        target.setHealth(maxHealth == null ? 20.0 : maxHealth.getValue());
        target.setFoodLevel(20);
        target.setFallDistance(0f);
        target.teleport(dest);
        refreshTags(target);
        target.sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je bent terug in het spel!", NamedTextColor.GREEN))
                .build());
    }

    /** One chosen player (also someone who died on the island) goes back to the main world. */
    private void returnFromLosers(CommandSender sender, Player target) {
        if (!isEliminated(target) && !isLosersOut(target)) {
            sender.sendMessage(Component.text(target.getName() + " is niet uitgeschakeld.", NamedTextColor.RED));
            return;
        }
        sendBackAlive(target);
        sender.sendMessage(Component.text(target.getName() + " is terug in de main wereld gezet.",
                NamedTextColor.GREEN));
    }

    /** Players who are still alive on the island. */
    private List<Player> aliveOnIsland() {
        List<Player> result = new ArrayList<>();
        for (Player p : getServer().getOnlinePlayers()) {
            if (inLosers(p) && isEliminated(p) && !isLosersOut(p) && p.getGameMode() != GameMode.SPECTATOR) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * The end of the island: everybody who is still alive on it goes back to the main world, the island closes and the
     * red star disappears for everybody.
     */
    private void finishLosers(CommandSender sender) {
        List<Player> winners = aliveOnIsland();
        for (Player p : winners) {
            sendBackAlive(p);
        }
        losersOpen = false;
        losersEnded = true;
        saveState();
        for (Player p : getServer().getOnlinePlayers()) {
            removeLosersItems(p);
        }
        Component message = Component.text()
                .append(glyph(LOWKEY_BADGE_RED))
                .append(Component.space())
                .append(Component.text("Het Verliezerseiland is afgelopen.", NamedTextColor.RED))
                .build();
        for (Player p : getServer().getOnlinePlayers()) {
            if (isEliminated(p)) {
                p.sendMessage(message);
            }
        }
        sender.sendMessage(Component.text(winners.size() + " spelers zijn terug in de main wereld gezet. Het eiland is "
                + "gesloten en de rode ster is weg.", NamedTextColor.GREEN));
    }

    private void openLosersFinishConfirm(Player admin) {
        List<Player> winners = aliveOnIsland();
        StringBuilder names = new StringBuilder();
        for (Player p : winners) {
            names.append(names.length() == 0 ? "" : ", ").append(p.getName());
        }
        confirm(admin,
                "Iedereen die leeft terugsturen?",
                winners.isEmpty() ? "Er is niemand meer levend op het eiland."
                        : winners.size() + " spelers gaan terug naar de main wereld: " + names + ".",
                "Daarna sluit het eiland en verdwijnt de rode ster bij iedereen.",
                Component.text("Ja, terugsturen", NamedTextColor.GREEN),
                () -> {
                    finishLosers(admin);
                    openLosersMenu(admin);
                },
                () -> openLosersMenu(admin));
    }

    /** A list of everybody who is out, to pick one to bring back. */
    private void openLosersPick(Player admin) {
        List<ActionButton> buttons = new ArrayList<>();
        for (Player p : getServer().getOnlinePlayers()) {
            if (!isEliminated(p) && !isLosersOut(p)) {
                continue;
            }
            String where = isLosersOut(p) ? " (uit)" : (inLosers(p) ? " (eiland)" : " (lobby)");
            buttons.add(button(Component.text(p.getName() + where), "Zet " + p.getName() + " terug in de main wereld.",
                    150, () -> {
                        if (p.isOnline()) {
                            returnFromLosers(admin, p);
                        }
                        openLosersMenu(admin);
                    }));
        }
        if (buttons.isEmpty()) {
            admin.sendMessage(Component.text("Er zijn geen uitgeschakelde spelers online.", NamedTextColor.YELLOW));
            openLosersMenu(admin);
            return;
        }
        ActionButton back = backButton("Terug naar het Verliezerseiland.", () -> openLosersMenu(admin));
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Speler terugsturen"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Kies wie weer levend terug in de main wereld gaat:", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons, back, 2)));
        admin.showDialog(dialog);
    }

    private void deleteLosers(CommandSender sender) {
        World world = losersWorld();
        File folder = world != null ? world.getWorldFolder() : new File(getServer().getWorldContainer(), LOSERS_WORLD);
        if (!losersCreated && world == null && !folder.isDirectory()) {
            sender.sendMessage(Component.text("Er is geen Verliezerseiland om te verwijderen.", NamedTextColor.RED));
            return;
        }
        if (world != null) {
            for (Player p : new ArrayList<>(world.getPlayers())) {
                if (lobbySpawn != null && lobbySpawn.getWorld() != null) {
                    sendToLobby(p);
                } else {
                    p.teleport(gameWorld().getSpawnLocation());
                }
            }
            if (!Bukkit.unloadWorld(world, false)) {
                sender.sendMessage(Component.text("De wereld kon niet worden afgesloten, probeer het opnieuw.",
                        NamedTextColor.RED));
                return;
            }
        }
        boolean wiped = !folder.exists() || deleteRecursively(folder);
        losersCreated = false;
        losersOpen = false;
        losersSpawn = null;
        saveState();
        sender.sendMessage(Component.text(wiped ? "Het Verliezerseiland is verwijderd."
                : "Het Verliezerseiland is losgekoppeld, maar de map '" + folder.getName()
                + "' kon niet helemaal gewist worden.", wiped ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
    }

    // ---- atmosphere: always a thunderstorm at night, with scary sounds and lightning

    private void tickLosers() {
        World world = losersWorld();
        if (world == null) {
            return;
        }
        world.setTime(18000L);
        if (!world.hasStorm()) {
            world.setStorm(true);
        }
        if (!world.isThundering()) {
            world.setThundering(true);
        }
        List<Player> players = world.getPlayers();
        if (players.isEmpty()) {
            return;
        }
        if (--losersAmbient > 0) {
            return;
        }
        losersAmbient = 6 + random.nextInt(12); // every 12 to 36 seconds
        Sound sound = LOSERS_SOUNDS[random.nextInt(LOSERS_SOUNDS.length)];
        for (Player p : players) {
            p.playSound(p.getLocation(), sound, 1.0f, 0.7f + random.nextFloat() * 0.5f);
        }
        Player target = players.get(random.nextInt(players.size()));
        Location strike = target.getLocation().clone().add(random.nextInt(61) - 30, 0, random.nextInt(61) - 30);
        world.strikeLightningEffect(strike);
    }

    /** The weather on the island never clears. */
    @EventHandler
    public void onLosersWeather(WeatherChangeEvent event) {
        if (!event.toWeatherState() && event.getWorld().getName().equals(LOSERS_WORLD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLosersThunder(ThunderChangeEvent event) {
        if (!event.toThunderState() && event.getWorld().getName().equals(LOSERS_WORLD)) {
            event.setCancelled(true);
        }
    }

    // ---- menu and command

    private void openLosersMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        if (losersCreated && losersWorld() != null) {
            buttons.add(button(iconLabel(M_COMPASS, "Naar het eiland", NamedTextColor.WHITE),
                    "Teleporteert jou naar het Verliezerseiland.", 150, () -> teleportAdminToLosers(admin)));
            buttons.add(button(iconLabel(M_SIGN, "Spawn zetten", NamedTextColor.WHITE),
                    "Zet het spawnpunt op jouw plek. Je moet op het eiland staan.", 150, () -> {
                        setLosersSpawn(admin);
                        openLosersMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_PICKAXE, "Eiland aanmaken", NamedTextColor.GREEN),
                    "Maakt een normale survival wereld met altijd onweer en nacht.", 150, () -> {
                        createLosers(admin, null);
                        openLosersMenu(admin);
                    }));
        }
        if (losersOpen) {
            buttons.add(button(iconLabel(M_BARRIER, "Eiland sluiten", NamedTextColor.RED),
                    "Nieuwe spelers kunnen er niet meer heen. Wie er is blijft er.", 150, () -> {
                        closeLosers(admin);
                        openLosersMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_DOOR, "Eiland openen", NamedTextColor.GREEN),
                    "Uitgeschakelde spelers kunnen de rode ster gebruiken.", 150, () -> confirm(admin,
                            "Verliezerseiland openen?",
                            "Alle uitgeschakelde spelers krijgen een melding en kunnen de rode ster gebruiken.",
                            "Je kunt het later weer sluiten.",
                            Component.text("Ja, openen", NamedTextColor.GREEN),
                            () -> {
                                openLosers(admin);
                                openLosersMenu(admin);
                            },
                            () -> openLosersMenu(admin))));
        }
        buttons.add(button(iconLabel(M_REVIVE, "Speler terugsturen", NamedTextColor.WHITE),
                "Kies zelf wie weer levend terug in de main wereld gaat.", 150, () -> openLosersPick(admin)));
        buttons.add(button(iconLabel(M_HEART, "Iedereen die leeft terug", NamedTextColor.GREEN),
                "Iedereen die nog leeft op het eiland gaat terug. Daarna sluit het eiland en verdwijnt de rode ster.", 150,
                () -> openLosersFinishConfirm(admin)));
        if (losersCreated || losersWorld() != null) {
            buttons.add(button(iconLabel(M_LAVA, "Eiland verwijderen", NamedTextColor.RED),
                    "Wist de hele wereld.", 150, () -> confirm(admin,
                            "Verliezerseiland verwijderen?",
                            "De hele wereld wordt gewist, met alles wat erin staat.",
                            "Spelers op het eiland gaan naar de lobby.",
                            Component.text("Ja, verwijderen", NamedTextColor.RED),
                            () -> {
                                deleteLosers(admin);
                                openLosersMenu(admin);
                            },
                            () -> openLosersMenu(admin))));
        }
        int waiting = 0;
        int out = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            if (isLosersOut(p)) {
                out++;
            } else if (inLobby(p) && isEliminated(p)) {
                waiting++;
            }
        }
        Component body = joinLines(
                statusLine("Eiland", losersCreated ? "aangemaakt" : "nog niet",
                        losersCreated ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Status", losersEnded ? "afgelopen" : (losersOpen ? "open" : "dicht"),
                        losersOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Nog levend op het eiland", String.valueOf(aliveOnIsland().size()), NamedTextColor.WHITE),
                statusLine("Wachten in de lobby", String.valueOf(waiting), NamedTextColor.WHITE),
                statusLine("Definitief uit", String.valueOf(out), NamedTextColor.WHITE));
        showMenu(admin, "Verliezerseiland", body, buttons, backButton("Terug naar event.", () -> openEventHub(admin)));
    }

    private void handleLosersCommand(CommandSender sender, String[] args) {
        String sub = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "";
        if (sub.equals("create")) {
            Long seed = null;
            if (args.length >= 3) {
                try {
                    seed = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    seed = (long) args[2].hashCode(); // like in vanilla: text becomes a number
                }
            }
            createLosers(sender, seed);
        } else if (sub.equals("open")) {
            openLosers(sender);
        } else if (sub.equals("close")) {
            closeLosers(sender);
        } else if (sub.equals("delete")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
                deleteLosers(sender);
            } else {
                sender.sendMessage(Component.text("Bevestig met: /lowkey losers delete confirm", NamedTextColor.GRAY));
            }
        } else if (sub.equals("tp") || sub.equals("setspawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
            } else if (sub.equals("tp")) {
                teleportAdminToLosers((Player) sender);
            } else {
                setLosersSpawn((Player) sender);
            }
        } else if (sub.equals("returnall")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
                finishLosers(sender);
            } else if (sender instanceof Player) {
                openLosersFinishConfirm((Player) sender);
            } else {
                sender.sendMessage(Component.text("Gebruik in de console: /lowkey losers returnall confirm",
                        NamedTextColor.GRAY));
            }
        } else if (sub.equals("return")) {
            Player target = args.length == 3 ? getServer().getPlayerExact(args[2]) : null;
            if (args.length != 3) {
                sender.sendMessage(Component.text("Gebruik: /lowkey losers return <speler>", NamedTextColor.GRAY));
            } else if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
            } else {
                returnFromLosers(sender, target);
            }
        } else if (sender instanceof Player) {
            openLosersMenu((Player) sender);
        } else {
            sender.sendMessage(Component.text("Gebruik: /lowkey losers <create|tp|setspawn|open|close|return|delete>",
                    NamedTextColor.GRAY));
        }
    }

    // ------------------------------------------------------------------ team division event

    private static final String NOORD_BIG = "\uE060";
    private static final String ZUID_BIG = "\uE061";
    /** wide space glyph (advance 56) between the two big logos in the title */
    private static final String EVENT_GAP = "\uE012";

    /** Real accounts, so the test heads get real skins (a name that cannot be found shows a default head). */
    private static final String[] FAKE_NAMES = {"Notch", "jeb_", "Dinnerbone", "Grumm", "Searge", "Dream", "Technoblade",
            "TommyInnit", "Tubbo_", "Ranboo", "Sapnap", "GeorgeNotFound", "Skeppy", "Ph1LzA", "WilburSoot", "Quackity",
            "Fundy", "Punz", "Antfrost", "Herobrine"};

    // Layout of the stage in blocks, relative to the camera. Tune these numbers if it does not look right on screen.
    private static final double EV_DIST = 8.0;            // how far in front of the camera everything hangs
    private static final double EV_COL_X = 4.6;           // distance of each team column from the middle
    private static final double EV_TOP_Y = 0.4;           // height of the first row (0 = camera height)
    private static final double EV_CELL_W = 1.5;          // width of one head + name cell
    private static final double EV_ROW_MAX = 1.0;         // biggest distance between two rows
    private static final double EV_ROWS_H = 3.6;          // all rows together stay inside this height
    private static final double EV_LOGO_Y = 2.7;          // height of the two team logos
    private static final float EV_LOGO_SCALE = 4.0f;
    private static final float EV_HEAD_SCALE = 1.3f;
    private static final float EV_NAME_SCALE = 0.5f;
    private static final double EV_CENTER_Y = 0.7;        // height of the big shuffle head
    private static final float EV_CENTER_HEAD_SCALE = 1.5f;

    /** One person in the event (a real player, or a fake name in test mode). */
    private static final class EvPlayer {
        final UUID id;
        final String name;
        final boolean fake;
        Side side = Side.NONE;
        boolean placed;
        /** the head with the real skin of a fake (test) player, once it has been loaded */
        volatile ItemStack head;

        EvPlayer(UUID id, String name, boolean fake) {
            this.id = id;
            this.name = name;
            this.fake = fake;
        }
    }

    private boolean eventRunning;
    private boolean eventTest;
    private Location eventCamera;
    private ArmorStand eventStand;
    private ItemDisplay eventCenterHead;
    private BukkitRunnable eventTask;
    private final List<Entity> eventEntities = new ArrayList<>();
    private final List<EvPlayer> eventPlayers = new ArrayList<>();
    private final Map<UUID, Location> eventOrigin = new HashMap<>();
    private final Map<UUID, GameMode> eventMode = new HashMap<>();
    private final Set<UUID> eventLocked = new HashSet<>();
    private final int[] eventPlan = new int[2];
    private final int[] eventPlaced = new int[2];
    private int eventPhase;       // 0 intro, 1 shuffle, 2 hold, 3 outro
    private int eventTick;
    private int eventAbsTick;
    private int eventPick;
    private int eventDone;
    private int eventShuffleEnd;
    private int eventNextFrame;
    private double eventInterval;
    private int eventShown;

    private File eventFile() {
        return new File(getDataFolder(), "event.yml");
    }

    private void loadEventCamera() {
        eventCamera = readLoc(YamlConfiguration.loadConfiguration(eventFile()), "camera");
    }

    private void setEventCameraHere(Player admin) {
        if (!inLobby(admin)) {
            admin.sendMessage(Component.text("Ga eerst naar de lobby, de camera hoort daar.", NamedTextColor.RED));
            return;
        }
        if (eventRunning) {
            admin.sendMessage(Component.text("Er is nu een event bezig.", NamedTextColor.RED));
            return;
        }
        eventCamera = admin.getEyeLocation().clone();
        YamlConfiguration y = new YamlConfiguration();
        writeLoc(y, "camera", eventCamera);
        try {
            getDataFolder().mkdirs();
            y.save(eventFile());
        } catch (IOException e) {
            getLogger().warning("Kon event.yml niet opslaan: " + e.getMessage());
        }
        admin.sendMessage(Component.text("De camera staat nu op jouw ogen en kijkrichting. Start een test om het beeld "
                + "te zien.", NamedTextColor.GREEN));
    }

    private Component eventLogos() {
        return Component.text()
                .append(glyph(NOORD_BIG))
                .append(glyph(EVENT_GAP))
                .append(glyph(ZUID_BIG))
                .build();
    }

    /** The big logos on the left and right as the title, with the given line under it. */
    private void eventTitle(Component subtitle, int stayTicks) {
        Title title = Title.title(Component.empty(), subtitle,
                Title.Times.times(Duration.ZERO, Duration.ofMillis(stayTicks * 50L), Duration.ZERO));
        for (UUID id : eventLocked) {
            Player p = getServer().getPlayer(id);
            if (p != null) {
                p.showTitle(title);
            }
        }
    }

    private void eventSound(Sound sound, float volume, float pitch) {
        for (UUID id : eventLocked) {
            Player p = getServer().getPlayer(id);
            if (p != null) {
                p.playSound(p.getLocation(), sound, volume, pitch);
            }
        }
    }

    /** A point on the stage: x to the right, y up, EV_DIST blocks in front of the camera. */
    private Location eventPoint(double x, double y) {
        Vector forward = eventCamera.getDirection().normalize();
        Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        Vector up = right.clone().crossProduct(forward).normalize();
        return eventCamera.clone()
                .add(forward.clone().multiply(EV_DIST))
                .add(right.multiply(x))
                .add(up.multiply(y));
    }

    /** Looks up the skin of a fake (test) player by name, in the background. */
    private void loadFakeSkin(EvPlayer ep) {
        try {
            Bukkit.createProfile(ep.name).update().thenAccept(profile ->
                    getServer().getScheduler().runTask(this, () -> {
                        ItemStack skinned = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta meta = (SkullMeta) skinned.getItemMeta();
                        meta.setPlayerProfile(profile);
                        skinned.setItemMeta(meta);
                        ep.head = skinned;
                    })).exceptionally(t -> null);
        } catch (Exception e) {
            getLogger().warning("Kon de skin van " + ep.name + " niet laden: " + e.getMessage());
        }
    }

    private ItemStack eventHead(EvPlayer ep) {
        if (ep.fake) {
            ItemStack skinned = ep.head;
            return skinned != null ? skinned.clone() : new ItemStack(Material.PLAYER_HEAD);
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (!ep.fake) {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            Player online = getServer().getPlayer(ep.id);
            if (online != null) {
                meta.setPlayerProfile(online.getPlayerProfile());
            } else {
                meta.setOwningPlayer(getServer().getOfflinePlayer(ep.id));
            }
            head.setItemMeta(meta);
        }
        return head;
    }

    private ItemDisplay spawnEventHead(Location at, ItemStack head, float scale) {
        ItemDisplay display = at.getWorld().spawn(at, ItemDisplay.class, d -> {
            d.setItemStack(head);
            d.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            d.setBillboard(Display.Billboard.CENTER);
            d.setBrightness(new Display.Brightness(15, 15));
            d.setViewRange(2.5f);
            d.setPersistent(false);
            // turned half a circle so the face looks at the camera instead of away from it
            d.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f((float) Math.PI, 0f, 1f, 0f),
                    new Vector3f(scale, scale, scale), new AxisAngle4f()));
        });
        eventEntities.add(display);
        return display;
    }

    private TextDisplay spawnEventText(Location at, Component text, float scale) {
        TextDisplay display = at.getWorld().spawn(at, TextDisplay.class, d -> {
            d.text(text);
            d.setAlignment(TextDisplay.TextAlignment.CENTER);
            d.setBillboard(Display.Billboard.CENTER);
            d.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            d.setShadowed(true);
            d.setSeeThrough(false);
            d.setBrightness(new Display.Brightness(15, 15));
            d.setViewRange(2.5f);
            d.setPersistent(false);
            d.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(),
                    new Vector3f(scale, scale, scale), new AxisAngle4f()));
        });
        eventEntities.add(display);
        return display;
    }

    // ---- start / stop

    private void startEvent(CommandSender admin, boolean test, int fakeCount) {
        if (eventRunning) {
            admin.sendMessage(Component.text("Er is al een event bezig. Stop het eerst met /lowkey event stop.",
                    NamedTextColor.RED));
            return;
        }
        if (lobbySpawn == null || lobbySpawn.getWorld() == null) {
            admin.sendMessage(Component.text("Maak eerst de lobby aan.", NamedTextColor.RED));
            return;
        }
        if (eventCamera == null || eventCamera.getWorld() == null
                || !eventCamera.getWorld().equals(lobbySpawn.getWorld())) {
            admin.sendMessage(Component.text("Zet eerst de camera in de lobby (menu Team event, Camera zetten).",
                    NamedTextColor.RED));
            return;
        }
        List<Player> real = new ArrayList<>();
        for (Player p : getServer().getOnlinePlayers()) {
            if (!isEliminated(p)) {
                real.add(p);
            }
        }
        int fakes = test ? Math.max(0, Math.min(fakeCount, FAKE_NAMES.length)) : 0;
        if (real.size() + fakes < 2) {
            admin.sendMessage(Component.text(test ? "Kies minstens 1 nep-speler als je alleen bent."
                    : "Er zijn minstens 2 spelers nodig.", NamedTextColor.RED));
            return;
        }

        // everybody who is not in the lobby goes there first (their things are put away like always)
        for (Player p : real) {
            if (!inLobby(p)) {
                YamlConfiguration data = loadPlayerData(p.getUniqueId());
                data.set("entered", true);
                savePlayerData(p.getUniqueId(), data);
                sendToLobby(p);
            }
        }

        // who takes part, in which order, and who ends up where (decided now, the show is only the show)
        eventPlayers.clear();
        for (Player p : real) {
            eventPlayers.add(new EvPlayer(p.getUniqueId(), p.getName(), false));
        }
        if (fakes > 0) {
            List<String> names = new ArrayList<>(Arrays.asList(FAKE_NAMES));
            Collections.shuffle(names, random);
            for (int i = 0; i < fakes; i++) {
                EvPlayer fake = new EvPlayer(UUID.randomUUID(), names.get(i), true);
                eventPlayers.add(fake);
                loadFakeSkin(fake);
            }
        }
        Collections.shuffle(eventPlayers, random);
        int n = eventPlayers.size();
        int noord = n / 2;
        int zuid = n / 2;
        if (n % 2 == 1) {
            if (random.nextBoolean()) {
                noord++;
            } else {
                zuid++;
            }
        }
        List<Side> sides = new ArrayList<>();
        for (int i = 0; i < noord; i++) {
            sides.add(Side.NOORD);
        }
        for (int i = 0; i < zuid; i++) {
            sides.add(Side.ZUID);
        }
        Collections.shuffle(sides, random);
        for (int i = 0; i < n; i++) {
            eventPlayers.get(i).side = sides.get(i);
        }
        eventPlan[0] = noord;
        eventPlan[1] = zuid;
        eventPlaced[0] = 0;
        eventPlaced[1] = 0;

        // the camera: an invisible armor stand that everybody spectates
        Location eye = eventCamera.clone();
        eventStand = eye.getWorld().spawn(eye, ArmorStand.class, a -> {
            a.setInvisible(true);
            a.setMarker(true);
            a.setGravity(false);
            a.setInvulnerable(true);
            a.setSilent(true);
            a.setBasePlate(false);
            a.setPersistent(false);
        });
        Location at = eye.clone().subtract(0, eventStand.getEyeHeight(), 0);
        eventStand.teleport(at);

        // the team logos hang in the world, so they always sit right above their own column
        spawnEventText(eventPoint(-EV_COL_X, EV_LOGO_Y), glyph(NOORD_BIG), EV_LOGO_SCALE);
        spawnEventText(eventPoint(EV_COL_X, EV_LOGO_Y), glyph(ZUID_BIG), EV_LOGO_SCALE);
        eventCenterHead = spawnEventHead(eventPoint(0, EV_CENTER_Y), new ItemStack(Material.PLAYER_HEAD),
                EV_CENTER_HEAD_SCALE);

        eventLocked.clear();
        eventOrigin.clear();
        eventMode.clear();
        for (Player p : real) {
            eventLocked.add(p.getUniqueId());
            eventOrigin.put(p.getUniqueId(), p.getLocation().clone());
            eventMode.put(p.getUniqueId(), p.getGameMode());
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(eye);
        }
        getServer().getScheduler().runTaskLater(this, this::eventEnforceCamera, 5L);

        eventTest = test;
        eventPhase = 0;
        eventTick = 0;
        eventAbsTick = 0;
        eventPick = 0;
        eventDone = 0;
        eventShown = -1;
        eventRunning = true;
        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                eventStep();
            }
        };
        eventTask.runTaskTimer(this, 1L, 1L);
        admin.sendMessage(Component.text("Het team event is gestart" + (test ? " (testmodus, er worden geen teams "
                + "veranderd)." : "."), NamedTextColor.GREEN));
    }

    /** Stops everything and puts every player back. Nothing is removed that was already applied. */
    private void eventCleanup() {
        if (eventTask != null) {
            eventTask.cancel();
            eventTask = null;
        }
        for (Entity e : eventEntities) {
            if (e.isValid()) {
                e.remove();
            }
        }
        eventEntities.clear();
        eventCenterHead = null;
        if (eventStand != null) {
            eventStand.remove();
            eventStand = null;
        }
        for (UUID id : new ArrayList<>(eventLocked)) {
            Player p = getServer().getPlayer(id);
            if (p == null) {
                continue;
            }
            p.setSpectatorTarget(null);
            p.clearTitle();
            GameMode back = eventMode.getOrDefault(id, GameMode.ADVENTURE);
            if (back == GameMode.SPECTATOR || back == GameMode.SURVIVAL) {
                back = GameMode.ADVENTURE;
            }
            p.setGameMode(back);
            Location origin = eventOrigin.get(id);
            if (origin != null && origin.getWorld() != null) {
                p.teleport(origin);
            }
        }
        eventLocked.clear();
        eventOrigin.clear();
        eventMode.clear();
        eventPlayers.clear();
        eventRunning = false;
    }

    private void stopEvent(CommandSender sender) {
        if (!eventRunning) {
            sender.sendMessage(Component.text("Er is geen event bezig.", NamedTextColor.RED));
            return;
        }
        eventCleanup();
        sender.sendMessage(Component.text("Het event is gestopt en iedereen staat weer vrij.", NamedTextColor.GREEN));
    }

    /** A locked player left: he stays out of the event. */
    private void eventPlayerLeft(Player player) {
        eventLocked.remove(player.getUniqueId());
        eventOrigin.remove(player.getUniqueId());
        eventMode.remove(player.getUniqueId());
    }

    private void eventEnforceCamera() {
        if (!eventRunning || eventStand == null) {
            return;
        }
        for (UUID id : eventLocked) {
            Player p = getServer().getPlayer(id);
            if (p == null) {
                continue;
            }
            if (p.getGameMode() != GameMode.SPECTATOR) {
                p.setGameMode(GameMode.SPECTATOR);
            }
            if (!p.getWorld().equals(eventStand.getWorld())) {
                p.teleport(eventCamera);
            }
            if (!eventStand.equals(p.getSpectatorTarget())) {
                p.setSpectatorTarget(eventStand);
            }
        }
    }

    // ---- the show, one step per tick

    private boolean eventAvailable(EvPlayer ep) {
        return ep.fake || getServer().getPlayer(ep.id) != null;
    }

    private void eventStep() {
        if (!eventRunning) {
            return;
        }
        if (eventStand == null || !eventStand.isValid()) {
            eventCleanup();
            return;
        }
        if (eventAbsTick++ % 10 == 0) {
            eventEnforceCamera();
        }
        if (eventPhase == 0) {
            eventStepIntro();
        } else if (eventPhase == 1) {
            eventStepShuffle();
        } else if (eventPhase == 2) {
            if (eventTick >= 30) {
                eventBeginPick();
                return;
            }
        } else if (eventTick >= 120) {
            eventCleanup();
            return;
        }
        eventTick++;
    }

    private void eventStepIntro() {
        if (eventTick == 0) {
            eventTitle(Component.text("Teams worden verdeeld", NamedTextColor.WHITE, TextDecoration.BOLD), 60);
            eventSound(Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        } else if (eventTick == 40 || eventTick == 60 || eventTick == 80) {
            String number = eventTick == 40 ? "3" : (eventTick == 60 ? "2" : "1");
            eventTitle(Component.text(number, NamedTextColor.YELLOW, TextDecoration.BOLD), 25);
            eventSound(Sound.UI_BUTTON_CLICK, 1.0f, 1.0f + (eventTick - 40) / 80f);
        } else if (eventTick >= 100) {
            eventBeginPick();
        }
    }

    /** Next person: how long the shuffle takes depends on how many are already done (the first ones take longest). */
    private void eventBeginPick() {
        while (eventPick < eventPlayers.size() && !eventAvailable(eventPlayers.get(eventPick))) {
            eventPick++;
        }
        if (eventPick >= eventPlayers.size()) {
            eventStartOutro();
            return;
        }
        double seconds = Math.max(3.0, 8.0 - 0.35 * eventDone);
        eventShuffleEnd = (int) (seconds * 20);
        eventNextFrame = 0;
        eventInterval = 2.0;
        eventShown = -1;
        eventPhase = 1;
        eventTick = 0;
    }

    private void eventStepShuffle() {
        EvPlayer chosen = eventPlayers.get(eventPick);
        if (eventTick >= eventShuffleEnd) {
            eventReveal(chosen);
            eventPhase = 2;
            eventTick = 0;
            return;
        }
        if (eventTick < eventNextFrame) {
            return;
        }
        List<Integer> pool = new ArrayList<>();
        for (int i = eventPick; i < eventPlayers.size(); i++) {
            if (eventAvailable(eventPlayers.get(i))) {
                pool.add(i);
            }
        }
        int index = pool.get(random.nextInt(pool.size()));
        for (int guard = 0; guard < 10 && pool.size() > 1 && index == eventShown; guard++) {
            index = pool.get(random.nextInt(pool.size()));
        }
        eventShown = index;
        EvPlayer shown = eventPlayers.get(index);
        if (eventCenterHead != null && eventCenterHead.isValid()) {
            eventCenterHead.setItemStack(eventHead(shown));
        }
        eventTitle(Component.text(shown.name, NamedTextColor.WHITE, TextDecoration.BOLD), 30);
        eventSound(Sound.BLOCK_NOTE_BLOCK_HAT, 0.7f, 0.8f + 0.8f * eventTick / Math.max(1, eventShuffleEnd));
        eventInterval = eventInterval * 1.17 + 0.2;
        eventNextFrame = eventTick + (int) Math.round(eventInterval);
    }

    private void eventReveal(EvPlayer ep) {
        ep.placed = true;
        eventDone++;
        eventPick++;
        TextColor color = TextColor.color(ep.side.rgb);
        if (eventCenterHead != null && eventCenterHead.isValid()) {
            eventCenterHead.setItemStack(eventHead(ep));
        }
        eventTitle(Component.text(ep.name, color, TextDecoration.BOLD), 40);

        boolean noord = ep.side == Side.NOORD;
        eventSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0f, noord ? 0.75f : 1.3f);
        eventSound(noord ? Sound.BLOCK_NOTE_BLOCK_BASS : Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, noord ? 0.7f : 1.4f);

        // the head and the name go into the column under the logo
        int sideIndex = noord ? 0 : 1;
        int k = eventPlaced[sideIndex]++;
        int cols = 3;
        int rows = (int) Math.ceil(Math.max(1, eventPlan[sideIndex]) / (double) cols);
        double spacing = Math.min(EV_ROW_MAX, EV_ROWS_H / Math.max(1, rows));
        double x = (noord ? -EV_COL_X : EV_COL_X) + ((k % cols) - 1) * EV_CELL_W;
        double y = EV_TOP_Y - (k / cols) * spacing;
        Location headAt = eventPoint(x, y);
        spawnEventHead(headAt, eventHead(ep), EV_HEAD_SCALE);
        spawnEventText(eventPoint(x, y - 0.62), Component.text(ep.name, color, TextDecoration.BOLD), EV_NAME_SCALE);
        headAt.getWorld().spawnParticle(Particle.END_ROD, headAt, 25, 0.3, 0.3, 0.3, 0.05);

        Component bar = Component.text("Verdeeld: " + eventDone + " van " + eventPlayers.size(), NamedTextColor.GRAY);
        for (UUID id : eventLocked) {
            Player p = getServer().getPlayer(id);
            if (p != null) {
                p.sendActionBar(bar);
            }
        }
    }

    private void eventStartOutro() {
        eventPhase = 3;
        eventTick = 0;
        if (eventCenterHead != null && eventCenterHead.isValid()) {
            eventCenterHead.remove();
        }
        List<String> noord = new ArrayList<>();
        List<String> zuid = new ArrayList<>();
        for (EvPlayer ep : eventPlayers) {
            if (!ep.placed) {
                continue;
            }
            (ep.side == Side.NOORD ? noord : zuid).add(ep.name);
            if (!eventTest && !ep.fake) {
                setSideByUuid(ep.id, ep.side); // the teams are only applied for real now
            }
        }
        eventTitle(Component.text(eventTest ? "Test klaar" : "Teams verdeeld!", NamedTextColor.GREEN,
                TextDecoration.BOLD), 110);
        eventSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

        Component summary = Component.text()
                .append(glyph(LOWKEY_BADGE)).append(Component.space())
                .append(Component.text("Noord: ", TextColor.color(Side.NOORD.rgb), TextDecoration.BOLD))
                .append(Component.text(String.join(", ", noord), NamedTextColor.WHITE))
                .append(Component.newline())
                .append(glyph(LOWKEY_BADGE)).append(Component.space())
                .append(Component.text("Zuid: ", TextColor.color(Side.ZUID.rgb), TextDecoration.BOLD))
                .append(Component.text(String.join(", ", zuid), NamedTextColor.WHITE))
                .build();
        for (UUID id : eventLocked) {
            Player p = getServer().getPlayer(id);
            if (p != null) {
                p.sendMessage(summary);
            }
        }
    }

    // ---- teams of players, also for players who are offline

    private void setSideByUuid(UUID id, Side side) {
        Player online = getServer().getPlayer(id);
        if (online != null) {
            setSide(online, side);
            return;
        }
        YamlConfiguration data = loadPlayerData(id);
        data.set("identity.migrated", true);
        data.set("identity.side", side == Side.NONE ? null : side.id);
        savePlayerData(id, data);
    }

    /** Everybody back to "no team", online and offline. For testing. */
    private void resetAllTeams(CommandSender sender) {
        if (eventRunning) {
            sender.sendMessage(Component.text("Stop eerst het event.", NamedTextColor.RED));
            return;
        }
        int count = 0;
        Set<UUID> online = new HashSet<>();
        for (Player p : getServer().getOnlinePlayers()) {
            online.add(p.getUniqueId());
            if (getSide(p) != Side.NONE) {
                count++;
            }
            setSide(p, Side.NONE);
        }
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                UUID id;
                try {
                    id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));
                } catch (IllegalArgumentException e) {
                    continue;
                }
                if (online.contains(id)) {
                    continue;
                }
                YamlConfiguration y = YamlConfiguration.loadConfiguration(f);
                if (y.getString("identity.side") != null) {
                    y.set("identity.side", null);
                    try {
                        y.save(f);
                        count++;
                    } catch (IOException e) {
                        getLogger().warning("Kon speler data niet opslaan: " + e.getMessage());
                    }
                }
            }
        }
        sender.sendMessage(Component.text("Alle teams zijn gereset (" + count + " spelers hadden een team).",
                NamedTextColor.GREEN));
    }

    // ---- menu

    private void openEventMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        if (eventRunning) {
            buttons.add(button(iconLabel(M_BARRIER, "Event afbreken", NamedTextColor.RED),
                    "Stopt het event meteen en zet iedereen weer vrij.", 150, () -> {
                        stopEvent(admin);
                        openEventMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_HORN, "Event starten", NamedTextColor.GREEN),
                    "Verdeelt iedereen die online is over Noord en Zuid.", 150, () -> confirm(admin,
                            "Team event starten?",
                            "Iedereen die online is (ook in de main wereld) gaat naar de lobby en kijkt mee. "
                                    + "Ieders team wordt opnieuw verdeeld.",
                            "Dit duurt ongeveer 2 tot 3 minuten.",
                            Component.text("Ja, starten", NamedTextColor.GREEN),
                            () -> {
                                startEvent(admin, false, 0);
                            },
                            () -> openEventMenu(admin))));
            buttons.add(button(iconLabel(M_STAR, "Testen", NamedTextColor.WHITE),
                    "Oefenen met nep-spelers. Er verandert geen enkel team.", 150, () -> openEventTestDialog(admin)));
        }
        buttons.add(button(iconLabel(M_COMPASS, "Camera zetten", NamedTextColor.WHITE),
                "Zet de camera op jouw ogen en kijkrichting. Je moet in de lobby staan.", 150, () -> {
                    setEventCameraHere(admin);
                    openEventMenu(admin);
                }));
        buttons.add(button(iconLabel(M_PLAYERS, "Teamverdeling", NamedTextColor.WHITE),
                "Alle spelers met hun team.", 150, () -> openTeamListMenu(admin)));
        buttons.add(button(iconLabel(M_LAVA, "Alle teams resetten", NamedTextColor.RED),
                "Haalt van iedereen het team weg (voor testen).", 150, () -> confirm(admin,
                        "Alle teams resetten?",
                        "Iedereen, ook wie offline is, heeft daarna geen team meer.",
                        "Dit kan niet ongedaan gemaakt worden.",
                        Component.text("Ja, resetten", NamedTextColor.RED),
                        () -> {
                            resetAllTeams(admin);
                            openEventMenu(admin);
                        },
                        () -> openEventMenu(admin))));

        int eligible = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            if (!isEliminated(p)) {
                eligible++;
            }
        }
        Component body = joinLines(
                statusLine("Event", eventRunning ? "bezig" : "niet bezig",
                        eventRunning ? NamedTextColor.YELLOW : NamedTextColor.GRAY),
                statusLine("Camera", eventCamera != null ? "ingesteld" : "niet ingesteld",
                        eventCamera != null ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Lobby", lobbyCreated ? "aangemaakt" : "nog niet",
                        lobbyCreated ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Spelers die meedoen", String.valueOf(eligible), NamedTextColor.WHITE));
        showMenu(admin, "Team event", body, buttons, backButton("Terug naar event.", () -> openEventHub(admin)));
    }

    private void openEventTestDialog(Player admin) {
        DialogInput count = DialogInput.text("aantal", Component.text("Aantal nep-spelers (1 tot 20)"))
                .width(300).initial("8").maxLength(2).build();
        DialogAction go = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final String text = view.getText("aantal");
            getServer().getScheduler().runTask(this, () -> {
                int value;
                try {
                    value = Integer.parseInt(text == null ? "" : text.trim());
                } catch (NumberFormatException e) {
                    value = -1;
                }
                if (value < 1 || value > FAKE_NAMES.length) {
                    clicker.sendMessage(Component.text("Geef een getal tussen 1 en " + FAKE_NAMES.length + ".",
                            NamedTextColor.RED));
                    openEventMenu(clicker);
                } else {
                    startEvent(clicker, true, value);
                }
            });
        }, CLICK_OPTIONS);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Test met nep-spelers"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Jij en de nep-spelers worden verdeeld. Er verandert geen enkel echt team.",
                                NamedTextColor.GRAY))))
                        .inputs(List.of(count))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_TEAM, "Starten", NamedTextColor.GREEN),
                                Component.text("Start de test."), 150, go),
                        backButton("Terug naar het event.", () -> openEventMenu(admin)))));
        admin.showDialog(dialog);
    }

    /** Everybody with their team, online and offline. */
    private void openTeamListMenu(Player admin) {
        List<String> noord = new ArrayList<>();
        List<String> zuid = new ArrayList<>();
        List<String> geen = new ArrayList<>();
        Set<UUID> seen = new HashSet<>();
        for (Player p : getServer().getOnlinePlayers()) {
            seen.add(p.getUniqueId());
            Side side = getSide(p);
            (side == Side.NOORD ? noord : (side == Side.ZUID ? zuid : geen)).add(p.getName());
        }
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                UUID id;
                try {
                    id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));
                } catch (IllegalArgumentException e) {
                    continue;
                }
                if (seen.contains(id)) {
                    continue;
                }
                Side side = Side.fromId(YamlConfiguration.loadConfiguration(f).getString("identity.side"));
                String name = getServer().getOfflinePlayer(id).getName();
                if (name == null) {
                    name = id.toString().substring(0, 8);
                }
                name = name + " (offline)";
                (side == Side.NOORD ? noord : (side == Side.ZUID ? zuid : geen)).add(name);
            }
        }
        noord.sort(String.CASE_INSENSITIVE_ORDER);
        zuid.sort(String.CASE_INSENSITIVE_ORDER);
        geen.sort(String.CASE_INSENSITIVE_ORDER);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Teamverdeling"))
                        .body(List.of(
                                DialogBody.plainMessage(teamLine("Noord", noord, TextColor.color(Side.NOORD.rgb))),
                                DialogBody.plainMessage(teamLine("Zuid", zuid, TextColor.color(Side.ZUID.rgb))),
                                DialogBody.plainMessage(teamLine("Geen team", geen, NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.confirmation(
                        button(iconLabel(M_PLAYERS, "Verversen", NamedTextColor.WHITE), "Laad de lijst opnieuw.", 150,
                                () -> openTeamListMenu(admin)),
                        backButton("Terug naar het event.", () -> openEventMenu(admin)))));
        admin.showDialog(dialog);
    }

    private static Component teamLine(String title, List<String> names, TextColor color) {
        return Component.text()
                .append(Component.text(title + " (" + names.size() + "): ", color, TextDecoration.BOLD))
                .append(Component.text(names.isEmpty() ? "-" : String.join(", ", names), NamedTextColor.WHITE))
                .build();
    }

    private void handleEventCommand(CommandSender sender, String[] args) {
        String sub = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "";
        if (sub.equals("start")) {
            startEvent(sender, false, 0);
        } else if (sub.equals("test")) {
            int count = 8;
            if (args.length >= 3) {
                try {
                    count = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    count = -1;
                }
            }
            if (count < 1 || count > FAKE_NAMES.length) {
                sender.sendMessage(Component.text("Gebruik: /lowkey event test [1 tot " + FAKE_NAMES.length + "]",
                        NamedTextColor.GRAY));
            } else {
                startEvent(sender, true, count);
            }
        } else if (sub.equals("stop")) {
            stopEvent(sender);
        } else if (sub.equals("camera")) {
            if (sender instanceof Player) {
                setEventCameraHere((Player) sender);
            } else {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
            }
        } else if (sub.equals("teams")) {
            if (sender instanceof Player) {
                openTeamListMenu((Player) sender);
            } else {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
            }
        } else if (sub.equals("reset")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
                resetAllTeams(sender);
            } else if (sender instanceof Player) {
                openEventMenu((Player) sender);
            } else {
                sender.sendMessage(Component.text("Gebruik in de console: /lowkey event reset confirm",
                        NamedTextColor.GRAY));
            }
        } else if (sender instanceof Player) {
            openEventMenu((Player) sender);
        } else {
            sender.sendMessage(Component.text("Gebruik: /lowkey event <start|test [aantal]|stop|camera|teams|reset>",
                    NamedTextColor.GRAY));
        }
    }

    // ------------------------------------------------------------------ rules book (lobby slot 9)

    /** The pages of the rules book, saved in rules.yml. Plain text as typed in the book; & colour codes work (&c red). */
    private final List<String> rulesPages = new ArrayList<>();

    private File rulesFile() {
        return new File(getDataFolder(), "rules.yml");
    }

    private void loadRules() {
        rulesPages.clear();
        rulesPages.addAll(YamlConfiguration.loadConfiguration(rulesFile()).getStringList("pages"));
    }

    private void saveRules() {
        YamlConfiguration y = new YamlConfiguration();
        y.set("pages", new ArrayList<>(rulesPages));
        try {
            getDataFolder().mkdirs();
            y.save(rulesFile());
        } catch (IOException e) {
            getLogger().warning("Kon rules.yml niet opslaan: " + e.getMessage());
        }
    }

    private List<Component> rulesComponents() {
        LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
        List<Component> pages = new ArrayList<>();
        for (String raw : rulesPages) {
            pages.add(legacy.deserialize(raw));
        }
        return pages;
    }

    private boolean isRulesBook(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(rulesBookKey, PersistentDataType.BYTE);
    }

    private ItemStack createRulesBook() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.title(Component.text("Regels", NamedTextColor.LIGHT_PURPLE));
        meta.author(Component.text("LowkeySMP"));
        meta.pages(rulesComponents());
        meta.setGeneration(BookMeta.Generation.ORIGINAL);
        meta.getPersistentDataContainer().set(rulesBookKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    /** The book goes in hotbar slot 9 of everybody in the lobby (when there are rules). */
    private void giveRulesBook(Player player) {
        if (!rulesPages.isEmpty()) {
            player.getInventory().setItem(8, createRulesBook());
        }
    }

    /** Opens the book with the current rules, whatever the book item in the inventory still says. */
    private void openRulesBook(Player player) {
        if (rulesPages.isEmpty()) {
            player.sendMessage(Component.text("Er zijn nog geen regels ingesteld.", NamedTextColor.YELLOW));
            return;
        }
        player.openBook(Book.book(Component.text("Regels", NamedTextColor.LIGHT_PURPLE), Component.text("LowkeySMP"),
                rulesComponents()));
    }

    private boolean rulesSeen(UUID id) {
        return loadPlayerData(id).getBoolean("rules-seen", false);
    }

    private void setRulesSeen(UUID id, boolean seen) {
        YamlConfiguration data = loadPlayerData(id);
        data.set("rules-seen", seen ? Boolean.TRUE : null);
        savePlayerData(id, data);
    }

    /** The first time somebody is in the lobby the book opens by itself. */
    private void scheduleForcedRulesRead(Player player) {
        if (rulesPages.isEmpty() || rulesSeen(player.getUniqueId())) {
            return;
        }
        getServer().getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline() || !inLobby(player) || rulesPages.isEmpty()) {
                return;
            }
            setRulesSeen(player.getUniqueId(), true);
            player.sendMessage(Component.text()
                    .append(glyph(LOWKEY_BADGE))
                    .append(Component.space())
                    .append(Component.text("Lees eerst de regels! Je kunt ze later altijd teruglezen in slot 9.",
                            NamedTextColor.LIGHT_PURPLE))
                    .build());
            openRulesBook(player);
        }, 40L);
    }

    /** After an edit: every book in the lobby gets the new text (or disappears when there are no rules left). */
    private void refreshRulesBooks() {
        for (Player p : getServer().getOnlinePlayers()) {
            if (!inLobby(p)) {
                continue;
            }
            ItemStack[] contents = p.getInventory().getContents();
            for (int i = 0; i < contents.length; i++) {
                if (isRulesBook(contents[i])) {
                    p.getInventory().setItem(i, null);
                }
            }
            giveRulesBook(p);
        }
    }

    /** Everybody has to read the book again the next time they are in the lobby (and the ones in it now, right away). */
    private void resetRulesSeen() {
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                try {
                    UUID id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));
                    setRulesSeen(id, false);
                } catch (IllegalArgumentException ignored) {
                    // not a player file
                }
            }
        }
        for (Player p : getServer().getOnlinePlayers()) {
            if (inLobby(p)) {
                scheduleForcedRulesRead(p);
            }
        }
    }


    private void openRulesMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openRulesMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_FEATHER, "Regels schrijven", NamedTextColor.GREEN),
                "Je krijgt een echt Book and Quill met de huidige regels erin. Pas het aan en klik op Klaar.", 150,
                () -> giveRulesEditorBook(admin)));
        if (!rulesPages.isEmpty()) {
            buttons.add(button(iconLabel(M_BOOK, "Boek lezen", NamedTextColor.AQUA),
                    "Open het boek zoals spelers het zien.", 150, () -> openRulesBook(admin)));
            buttons.add(button(iconLabel(M_STAR, "Iedereen opnieuw laten lezen", NamedTextColor.LIGHT_PURPLE),
                    "Het boek opent de volgende keer in de lobby weer vanzelf voor iedereen.", 150, () -> confirm(admin,
                            "Iedereen opnieuw laten lezen?",
                            "Het boek opent weer vanzelf bij iedereen die in de lobby komt, ook bij wie het al las.",
                            "Wie nu in de lobby is krijgt het boek meteen.",
                            Component.text("Ja, opnieuw", NamedTextColor.GREEN),
                            () -> {
                                resetRulesSeen();
                                admin.sendMessage(Component.text("Iedereen leest de regels opnieuw.",
                                        NamedTextColor.GREEN));
                                again.run();
                            },
                            again)));
            buttons.add(button(iconLabel(M_LAVA, "Alle regels wissen", NamedTextColor.RED),
                    "Haalt alle pagina's weg. Het boek verdwijnt dan uit de lobby.", 150, () -> confirm(admin,
                            "Alle regels wissen?",
                            "Het boek met " + rulesPages.size() + " pagina's wordt leeg en verdwijnt uit de lobby.",
                            "Dit kan niet ongedaan gemaakt worden.",
                            Component.text("Ja, wissen", NamedTextColor.RED),
                            () -> {
                                rulesPages.clear();
                                saveRules();
                                refreshRulesBooks();
                                again.run();
                            },
                            again)));
        }
        Component body = joinLines(
                statusLine("Pagina's", String.valueOf(rulesPages.size()),
                        rulesPages.isEmpty() ? NamedTextColor.YELLOW : NamedTextColor.WHITE),
                statusLine("Boek in de lobby", rulesPages.isEmpty() ? "nog niet (geen regels)" : "slot 9",
                        rulesPages.isEmpty() ? NamedTextColor.YELLOW : NamedTextColor.GREEN),
                Component.text("Schrijf in het boek zelf. Kleuren kan met &: &c rood, &6 goud, &l dik, &r terug.",
                        NamedTextColor.DARK_GRAY));
        showMenu(admin, "Regels", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
    }

    /** A real Book and Quill with the current rules in it. Done or Sign and Close saves them. */
    private void giveRulesEditorBook(Player admin) {
        removeRulesEditorBooks(admin);
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setPages(rulesPages.isEmpty() ? new ArrayList<>(List.of("")) : new ArrayList<>(rulesPages));
        meta.displayName(Component.text("Regels bewerken", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text("Schrijf de regels en klik op Klaar.", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("Dan gaan ze naar het boek in de lobby.", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)));
        meta.getPersistentDataContainer().set(rulesEditorKey, PersistentDataType.BYTE, (byte) 1);
        book.setItemMeta(meta);
        if (!admin.getInventory().addItem(book).isEmpty()) {
            admin.sendMessage(Component.text("Je inventory is vol, maak eerst een plek vrij.", NamedTextColor.RED));
            return;
        }
        admin.sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text("Je hebt een Book and Quill met de huidige regels gekregen. Pas het aan en klik "
                        + "op Klaar (of Sign and Close), dan staan de regels in het boek in de lobby.",
                        NamedTextColor.LIGHT_PURPLE))
                .build());
    }

    private void removeRulesEditorBooks(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.hasItemMeta()
                    && item.getItemMeta().getPersistentDataContainer().has(rulesEditorKey, PersistentDataType.BYTE)) {
                player.getInventory().setItem(i, null);
            }
        }
    }

    /** Saving the editor book (Done or Sign and Close) puts its pages in the rules. The book itself is removed again. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRulesBookEdit(PlayerEditBookEvent event) {
        if (!event.getPreviousBookMeta().getPersistentDataContainer().has(rulesEditorKey, PersistentDataType.BYTE)) {
            return;
        }
        event.setCancelled(true); // the book never gets signed or changed, it only carried the text
        Player player = event.getPlayer();
        if (!isAdmin(player)) {
            return;
        }
        List<String> pages = new ArrayList<>(event.getNewBookMeta().getPages());
        while (!pages.isEmpty() && pages.get(pages.size() - 1).isBlank()) {
            pages.remove(pages.size() - 1);
        }
        rulesPages.clear();
        rulesPages.addAll(pages);
        saveRules();
        refreshRulesBooks();
        player.sendMessage(Component.text()
                .append(glyph(LOWKEY_BADGE))
                .append(Component.space())
                .append(Component.text(rulesPages.isEmpty()
                        ? "De regels zijn leeg, het boek verdwijnt uit de lobby."
                        : "De regels zijn opgeslagen (" + rulesPages.size() + " pagina's). Het boek in de lobby is "
                                + "bijgewerkt.", NamedTextColor.GREEN))
                .build());
        getServer().getScheduler().runTask(this, () -> removeRulesEditorBooks(player));
    }


    // ------------------------------------------------------------------ totem limit

    /** The most Totems of Undying one player may carry (config.yml: max-totems, 2 when it is not there). */
    private int maxTotems() {
        return Math.max(0, getConfig().getInt("max-totems", 2));
    }

    private static boolean isTotem(ItemStack item) {
        return item != null && item.getType() == Material.TOTEM_OF_UNDYING;
    }

    /** Totems that are lying loose in this stack (stacks of totems count by amount). */
    private static int looseTotems(ItemStack item) {
        return isTotem(item) ? item.getAmount() : 0;
    }

    /** Inventories whose items go back to the player when he closes them: what is in there is still "on him". */
    private static boolean returnsItems(InventoryType type) {
        switch (type) {
            case CRAFTING:
            case WORKBENCH:
            case ANVIL:
            case SMITHING:
            case GRINDSTONE:
            case LOOM:
            case CARTOGRAPHY:
            case STONECUTTER:
            case ENCHANTING:
            case MERCHANT:
                return true;
            default:
                return false;
        }
    }

    /** Totems in a stack, including the ones inside a shulker box (or chest or barrel item) and a bundle. */
    private static int totemsIn(ItemStack stack) {
        if (stack == null || stack.getType().isAir()) {
            return 0;
        }
        int loose = looseTotems(stack);
        if (loose > 0) {
            return loose;
        }
        int nested = 0;
        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta instanceof BlockStateMeta) {
                BlockState state = ((BlockStateMeta) meta).getBlockState();
                if (state instanceof Container) {
                    for (ItemStack inner : ((Container) state).getInventory().getContents()) {
                        nested += looseTotems(inner);
                    }
                }
            } else if (meta instanceof BundleMeta) {
                for (ItemStack inner : ((BundleMeta) meta).getItems()) {
                    nested += looseTotems(inner);
                }
            }
        }
        return nested * Math.max(1, stack.getAmount());
    }

    /** Everything a player has on him: inventory, offhand, cursor, the crafting grid or anvil he has open, and what his shulker boxes and bundles hold. */
    private int countTotems(Player player) {
        int n = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            n += totemsIn(item);
        }
        n += totemsIn(player.getItemOnCursor());
        Inventory top = player.getOpenInventory().getTopInventory();
        if (returnsItems(top.getType())) {
            for (ItemStack item : top.getContents()) {
                n += totemsIn(item);
            }
        }
        return n;
    }

    private void totemDenied(Player player) {
        player.sendActionBar(Component.text()
                .append(Component.text("Je mag maximaal ", NamedTextColor.RED))
                .append(Component.text(String.valueOf(maxTotems()), NamedTextColor.WHITE))
                .append(Component.text(" Totem of Undying hebben.", NamedTextColor.RED))
                .build());
    }

    /** Takes up to `need` totems out of one slot (a loose totem or a container item) and puts them in `drops`. Returns how many. */
    private static int takeFromStack(ItemStack stack, int need, List<ItemStack> drops, boolean looseOnly) {
        if (stack == null || stack.getType().isAir() || need <= 0) {
            return 0;
        }
        if (isTotem(stack)) {
            if (!looseOnly) {
                return 0;
            }
            int take = Math.min(need, stack.getAmount());
            drops.add(new ItemStack(Material.TOTEM_OF_UNDYING, take));
            stack.setAmount(stack.getAmount() - take);
            return take;
        }
        if (looseOnly || !stack.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = stack.getItemMeta();
        int taken = 0;
        if (meta instanceof BlockStateMeta) {
            BlockState state = ((BlockStateMeta) meta).getBlockState();
            if (state instanceof Container) {
                Inventory inner = ((Container) state).getInventory();
                for (int i = 0; i < inner.getSize() && taken < need; i++) {
                    ItemStack item = inner.getItem(i);
                    if (isTotem(item)) {
                        int take = Math.min(need - taken, item.getAmount());
                        drops.add(new ItemStack(Material.TOTEM_OF_UNDYING, take));
                        item.setAmount(item.getAmount() - take);
                        inner.setItem(i, item.getAmount() <= 0 ? null : item);
                        taken += take;
                    }
                }
                if (taken > 0) {
                    ((BlockStateMeta) meta).setBlockState(state);
                    stack.setItemMeta(meta);
                }
            }
        } else if (meta instanceof BundleMeta) {
            List<ItemStack> items = new ArrayList<>(((BundleMeta) meta).getItems());
            List<ItemStack> kept = new ArrayList<>();
            for (ItemStack item : items) {
                if (isTotem(item) && taken < need) {
                    int take = Math.min(need - taken, item.getAmount());
                    drops.add(new ItemStack(Material.TOTEM_OF_UNDYING, take));
                    taken += take;
                    if (item.getAmount() - take > 0) {
                        ItemStack rest = item.clone();
                        rest.setAmount(item.getAmount() - take);
                        kept.add(rest);
                    }
                } else {
                    kept.add(item);
                }
            }
            if (taken > 0) {
                ((BundleMeta) meta).setItems(kept);
                stack.setItemMeta(meta);
            }
        }
        return taken;
    }

    /**
     * The safety net: whatever way somebody found to get more totems (a command, a trick nobody thought of), within half
     * a second the extra ones drop on the ground. Loose totems go first, then the ones inside shulker boxes and bundles.
     */
    private void enforceTotemLimit(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        int excess = countTotems(player) - maxTotems();
        if (excess <= 0) {
            return;
        }
        List<ItemStack> drops = new ArrayList<>();
        for (int pass = 0; pass < 2 && excess > 0; pass++) {
            boolean looseOnly = pass == 0;
            // the cursor first, then the open crafting grid or anvil, then the inventory from the back to the front
            ItemStack cursor = player.getItemOnCursor();
            int took = takeFromStack(cursor, excess, drops, looseOnly);
            if (took > 0) {
                player.setItemOnCursor(cursor.getAmount() <= 0 ? null : cursor);
                excess -= took;
            }
            Inventory top = player.getOpenInventory().getTopInventory();
            if (excess > 0 && returnsItems(top.getType())) {
                for (int i = top.getSize() - 1; i >= 0 && excess > 0; i--) {
                    ItemStack item = top.getItem(i);
                    took = takeFromStack(item, excess, drops, looseOnly);
                    if (took > 0) {
                        top.setItem(i, item.getAmount() <= 0 ? null : item);
                        excess -= took;
                    }
                }
            }
            PlayerInventory inv = player.getInventory();
            for (int i = inv.getSize() - 1; i >= 0 && excess > 0; i--) {
                ItemStack item = inv.getItem(i);
                took = takeFromStack(item, excess, drops, looseOnly);
                if (took > 0) {
                    inv.setItem(i, item.getAmount() <= 0 ? null : item);
                    excess -= took;
                }
            }
        }
        if (drops.isEmpty()) {
            return;
        }
        for (ItemStack drop : drops) {
            Item dropped = player.getWorld().dropItem(player.getLocation(), drop);
            dropped.setPickupDelay(40);
        }
        totemDenied(player);
    }

    /** Picking one up from the ground: the message stays on the action bar as long as you stand next to it. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTotemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        int incoming = totemsIn(event.getItem().getItemStack());
        if (incoming <= 0 || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (countTotems(player) + incoming > maxTotems()) {
            event.setCancelled(true);
            totemDenied(player);
        }
    }

    /** Taking one out of a chest and so on (to the cursor, shift click, hotbar swap, double click). */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTotemClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        int limit = maxTotems();

        // double click: gathers every totem of the open container onto the cursor
        if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            Inventory top = event.getView().getTopInventory();
            if (looseTotems(cursor) > 0 && top.getType() != InventoryType.PLAYER && !returnsItems(top.getType())) {
                int gain = 0;
                for (ItemStack item : top.getContents()) {
                    gain += totemsIn(item);
                }
                if (gain > 0 && countTotems(player) + gain > limit) {
                    event.setCancelled(true);
                    totemDenied(player);
                }
            }
            return;
        }

        Inventory clicked = event.getClickedInventory();
        if (clicked == null || clicked.getType() == InventoryType.PLAYER || returnsItems(clicked.getType())) {
            return; // only things coming from another inventory can add a totem
        }
        int gain = 0;
        switch (event.getAction()) {
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case MOVE_TO_OTHER_INVENTORY:
                gain = totemsIn(current);
                break;
            case SWAP_WITH_CURSOR:
                gain = totemsIn(current) - totemsIn(cursor);
                break;
            case HOTBAR_SWAP:
            case HOTBAR_MOVE_AND_READD:
                ItemStack other = event.getClick() == ClickType.SWAP_OFFHAND
                        ? player.getInventory().getItemInOffHand()
                        : (event.getHotbarButton() >= 0 ? player.getInventory().getItem(event.getHotbarButton()) : null);
                gain = totemsIn(current) - totemsIn(other);
                break;
            default:
                break;
        }
        if (gain > 0 && countTotems(player) + gain > limit) {
            event.setCancelled(true);
            totemDenied(player);
        }
    }

    // ------------------------------------------------------------------ join / leave messages per world

    /** Players that just joined: the move from where they logged out to the lobby is not a "move" for the messages. */
    private final Set<UUID> joinSettling = new HashSet<>();

    /** 0 = lobby, 1 = main world (with Nether and End), 2 = Verliezerseiland. */
    private int worldGroupOf(World world) {
        if (losersSpawn != null && world.equals(losersSpawn.getWorld())) {
            return 2;
        }
        if (lobbySpawn != null && world.equals(lobbySpawn.getWorld())) {
            return 0;
        }
        return 1;
    }

    /** Crew and admins see everything: every message, every whisper and every world. */
    private boolean seesAll(Player viewer) {
        return isAdmin(viewer) || isCrew(viewer);
    }

    private static String worldGroupName(int group) {
        return group == 0 ? "lobby" : (group == 2 ? "eiland" : "main");
    }

    /** "+ name" or "- name", for the people who are in that world right now (and for crew and admins, in every world). */
    private void worldMessage(int group, Player subject, boolean joined, boolean includeSubject, boolean log) {
        Component message = Component.text()
                .append(glyph(joined ? JOIN_ICON : LEAVE_ICON))
                .append(Component.space())
                .append(Component.text(subject.getName(), joined ? NamedTextColor.GREEN : NamedTextColor.RED))
                .build();
        Component tagged = Component.text("[" + worldGroupName(group) + "] ", NamedTextColor.DARK_GRAY).append(message);
        for (Player viewer : getServer().getOnlinePlayers()) {
            if (viewer.equals(subject) && !includeSubject) {
                continue;
            }
            if (worldGroup(viewer) == group) {
                viewer.sendMessage(message);
            } else if (seesAll(viewer) && !viewer.equals(subject)) {
                viewer.sendMessage(tagged);
            }
        }
        if (log) {
            getServer().getConsoleSender().sendMessage(message);
        }
    }

    // ------------------------------------------------------------------ safe teleport into the game world

    private static boolean spotFree(World world, int x, int y, int z) {
        if (y < world.getMinHeight() || y + 1 >= world.getMaxHeight()) {
            return false;
        }
        Block feet = world.getBlockAt(x, y, z);
        Block head = world.getBlockAt(x, y + 1, z);
        return !feet.getType().isSolid() && !head.getType().isSolid() && !feet.isLiquid() && !head.isLiquid();
    }

    /** The same spot, or the first free spot above it when it lies inside blocks. */
    private Location safeLocation(Location loc) {
        World world = loc.getWorld();
        if (world == null) {
            return loc;
        }
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        if (spotFree(world, x, y, z)) {
            return loc;
        }
        for (int yy = y + 1; yy < Math.min(world.getMaxHeight() - 2, y + 80); yy++) {
            if (spotFree(world, x, yy, z)) {
                Location up = loc.clone();
                up.setY(yy);
                return up;
            }
        }
        Location top = loc.clone();
        top.setY(world.getHighestBlockYAt(x, z) + 1.0);
        return top;
    }

    /** Loads the chunk first, so nobody lands in blocks that did not exist yet, then teleports to a free spot. */
    private void teleportSafely(Player player, Location dest) {
        World world = dest.getWorld();
        if (world == null) {
            player.teleport(dest);
            return;
        }
        if (world.isChunkLoaded(dest.getBlockX() >> 4, dest.getBlockZ() >> 4)) {
            player.teleport(safeLocation(dest));
            return;
        }
        world.getChunkAtAsync(dest).thenAccept(chunk -> getServer().getScheduler().runTask(this, () -> {
            if (player.isOnline()) {
                player.teleport(safeLocation(dest));
            }
        })).exceptionally(t -> {
            getServer().getScheduler().runTask(this, () -> {
                if (player.isOnline()) {
                    player.teleport(dest);
                }
            });
            return null;
        });
    }

    // ------------------------------------------------------------------ clean slate, spawn reset, empty everybody's inventory

    /** Every player the plugin has a file for. */
    private List<UUID> allPlayerIds() {
        List<UUID> ids = new ArrayList<>();
        File[] files = new File(getDataFolder(), "players").listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                try {
                    ids.add(UUID.fromString(f.getName().substring(0, f.getName().length() - 4)));
                } catch (IllegalArgumentException ignored) {
                    // not a player file
                }
            }
        }
        return ids;
    }

    /** Wipes the saved "last position" of everybody (also offline), so the next time they enter they stand on their team spawn. */
    private int clearAllLastLocations() {
        int cleared = 0;
        for (UUID id : allPlayerIds()) {
            YamlConfiguration data = loadPlayerData(id);
            if (data.contains("last")) {
                data.set("last", null);
                savePlayerData(id, data);
                cleared++;
            }
        }
        return cleared;
    }

    /** The official launch is a fresh start: leftovers of tests (saved positions, dead statuses, red stars) are wiped. */
    private void launchCleanSlate() {
        clearAllLastLocations();
        for (UUID id : allPlayerIds()) {
            if (getServer().getPlayer(id) != null) {
                continue;
            }
            YamlConfiguration data = loadPlayerData(id);
            if (data.getBoolean("identity.eliminated", false) || data.getBoolean("identity.losers-out", false)) {
                data.set("identity.eliminated", false);
                data.set("identity.losers-out", null);
                savePlayerData(id, data);
            }
        }
        for (Player p : new ArrayList<>(getServer().getOnlinePlayers())) {
            if (isEliminated(p) || isLosersOut(p)) {
                setEliminated(p, false);
                setLosersOut(p, false);
            }
            removeLosersItems(p);
            refreshTags(p);
        }
    }

    /**
     * Empties everything of everybody except the admins: inventory, armor, offhand, ender chest, XP and effects. Online
     * players are done directly. For players who are offline the saved inventory of the lobby is emptied and their player
     * data file is deleted, so they join with nothing. The stars and the rules book belong to the lobby and stay.
     */
    private String clearEveryoneInventory(Player admin) {
        int online = 0;
        int offline = 0;
        Set<UUID> handled = new HashSet<>();
        for (Player p : new ArrayList<>(getServer().getOnlinePlayers())) {
            if (isAdmin(p)) {
                continue;
            }
            handled.add(p.getUniqueId());
            YamlConfiguration data = loadPlayerData(p.getUniqueId());
            if (data.getBoolean("stashed", false)) {
                data.set("inv", new ArrayList<ItemStack>());
                data.set("food", null);
                data.set("saturation", null);
                data.set("health", null);
                savePlayerData(p.getUniqueId(), data);
            }
            p.closeInventory();
            p.setItemOnCursor(null);
            if (!inLobby(p)) {
                p.getInventory().clear();
                p.getInventory().setArmorContents(new ItemStack[4]);
                p.getInventory().setItemInOffHand(null);
            }
            p.getEnderChest().clear();
            p.setLevel(0);
            p.setExp(0f);
            p.setTotalExperience(0);
            for (PotionEffect effect : new ArrayList<>(p.getActivePotionEffects())) {
                p.removePotionEffect(effect.getType());
            }
            online++;
        }

        Set<UUID> everybody = new HashSet<>(allPlayerIds());
        World main = gameWorld();
        String[] folders = {"playerdata", "players/data"};
        for (String folder : folders) {
            File[] dats = new File(main.getWorldFolder(), folder).listFiles((d, n) -> n.endsWith(".dat"));
            if (dats != null) {
                for (File f : dats) {
                    try {
                        everybody.add(UUID.fromString(f.getName().substring(0, f.getName().length() - 4)));
                    } catch (IllegalArgumentException ignored) {
                        // not a player file
                    }
                }
            }
        }
        List<String> staff = getConfig().getStringList("closed-access");
        for (UUID id : everybody) {
            if (handled.contains(id) || getServer().getPlayer(id) != null) {
                continue;
            }
            OfflinePlayer other = getServer().getOfflinePlayer(id);
            String name = other.getName();
            if (other.isOp() || (name != null && staff.stream().anyMatch(s -> s.equalsIgnoreCase(name)))) {
                continue;
            }
            YamlConfiguration data = loadPlayerData(id);
            data.set("stashed", false);
            data.set("inv", null);
            data.set("food", null);
            data.set("saturation", null);
            data.set("health", null);
            savePlayerData(id, data);
            for (String folder : folders) {
                for (String suffix : new String[] {".dat", ".dat_old"}) {
                    File f = new File(main.getWorldFolder(), folder + "/" + id + suffix);
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
            offline++;
        }
        return online + " spelers die online zijn en " + offline + " die offline zijn zijn geleegd.";
    }

    // ------------------------------------------------------------------ tab completion

    /** Typing /lowkey (and pressing space / tab) suggests every possibility. */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("lowkey") || !isAdmin(sender)) {
            return List.of();
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("mace")) {
            return mace.tabComplete(args);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("losers")) {
            List<String> losersOptions = new ArrayList<>(Arrays.asList("create", "tp", "setspawn", "open", "close",
                    "return", "returnall", "delete"));
            losersOptions.removeIf(o -> !o.startsWith(args[1].toLowerCase(Locale.ROOT)));
            return losersOptions;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("losers") && args[1].equalsIgnoreCase("return")) {
            List<String> names = new ArrayList<>();
            for (Player online : getServer().getOnlinePlayers()) {
                if (online.getName().toLowerCase(Locale.ROOT).startsWith(args[2].toLowerCase(Locale.ROOT))) {
                    names.add(online.getName());
                }
            }
            return names;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("event")) {
            List<String> eventOptions = new ArrayList<>(Arrays.asList("start", "test", "stop", "camera", "teams", "reset"));
            eventOptions.removeIf(o -> !o.startsWith(args[1].toLowerCase(Locale.ROOT)));
            return eventOptions;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("finale")) {
            List<String> finaleOptions = new ArrayList<>(Arrays.asList("menu", "start", "pauze", "hervat", "stop", "status", "test"));
            finaleOptions.removeIf(o -> !o.startsWith(args[1].toLowerCase(Locale.ROOT)));
            return finaleOptions;
        }
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            options.add("menu");
            options.add("grace");
            options.add("graceall");
            options.add("team");
            options.add("crew");
            options.add("nether");
            options.add("server");
            options.add("border");
            options.add("revive");
            options.add("donate");
            options.add("website");
            options.add("give");
            options.add("launch");
            options.add("lobby");
            options.add("setspawn");
            options.add("main");
            options.add("mainworld");
            options.add("seed");
            options.add("pin");
            options.add("countdown");
            options.add("mace");
            options.add("event");
            options.add("losers");
            options.add("end");
            options.add("finale");
            options.add("say");
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("grace") || sub.equals("team") || sub.equals("revive") || sub.equals("crew") || sub.equals("give")) {
                for (Player player : getServer().getOnlinePlayers()) {
                    options.add(player.getName());
                }
            } else if (sub.equals("nether") || sub.equals("end") || sub.equals("server") || sub.equals("border") || sub.equals("mainworld")) {
                options.add("open");
                options.add("close");
            } else if (sub.equals("lobby")) {
                options.add("create");
                options.add("tp");
                options.add("setspawn");
                options.add("delete");
                options.add("protect");
            } else if (sub.equals("seed")) {
                options.add("random");
                options.add("cancel");
            } else if (sub.equals("setspawn")) {
                options.add("noord");
                options.add("zuid");
            } else if (sub.equals("launch")) {
                options.add("confirm");
                options.add("reset");
            } else if (sub.equals("countdown")) {
                options.add("spawn");
                options.add("remove");
                options.add("set");
                options.add("size");
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("countdown") && args[1].equalsIgnoreCase("set")) {
                options.add("14:00");
            }
        } else if (args.length == 3) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("countdown")) {
                if (args[1].equalsIgnoreCase("set")) {
                    options.add("2026-11-07");
                } else if (args[1].equalsIgnoreCase("size")) {
                    options.add("4");
                    options.add("6");
                    options.add("8");
                    options.add("10");
                }
            } else if (sub.equals("team")) {
                options.add("noord");
                options.add("zuid");
                options.add("geen");
            } else if (sub.equals("grace")) {
                options.add("0");
                options.add("1");
                options.add("10");
                options.add("60");
            } else if (sub.equals("graceall")) {
                options.add("60");
            } else if (sub.equals("crew")) {
                options.add("aan");
                options.add("uit");
            } else if (sub.equals("lobby") && args[1].equalsIgnoreCase("delete")) {
                options.add("confirm");
            } else if (sub.equals("lobby") && args[1].equalsIgnoreCase("protect")) {
                options.add("aan");
                options.add("uit");
            } else if (sub.equals("seed") && !args[1].equalsIgnoreCase("cancel")) {
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

// ====================================================================================
// The one and only mace. Package-private on purpose: Java allows it in this same file.
// ====================================================================================

/**
 * The one and only mace of LowkeySMP.
 * <ul>
 * <li>Vanilla recipe, but only one mace can ever be crafted. From that moment the recipe is removed and every other way
 * to get a mace is blocked (crafter, creative, /give, stale copies).</li>
 * <li>When it is crafted everybody gets a title: "Noord heeft de mace ontgrendeld" (red) or "Zuid heeft de mace
 * ontgrendeld" (blue).</li>
 * <li>A real smash attack (falling more than 1.5 blocks) that hits a PLAYER starts a cooldown (default 10 minutes).
 * The cooldown belongs to the mace: it keeps running when the mace changes owner or lies on the ground, and it is shown
 * as the item cooldown overlay on the mace. It only runs while the mace is in the main world.</li>
 * <li>The mace cannot be put in chests, ender chests, shulkers, item frames, bundles, pots and so on, so it is always
 * in a player's inventory or on the ground. If it is destroyed, it is gone for good (until an admin resets it).</li>
 * </ul>
 * Everything is saved in mace.yml (separate from state.yml, which is rewritten as a whole by LowkeyCore).
 */
final class MaceManager implements Listener {

    private enum Where { NONE, CARRIED, GROUND, DESTROYED }

    /** Inventories a mace may be in while it is open. Everything else counts as storage and is blocked. */
    private static final Set<InventoryType> ALLOWED_TOP = EnumSet.of(
            InventoryType.CRAFTING, InventoryType.PLAYER, InventoryType.WORKBENCH, InventoryType.ANVIL,
            InventoryType.ENCHANTING, InventoryType.GRINDSTONE, InventoryType.SMITHING, InventoryType.CREATIVE,
            InventoryType.MERCHANT);

    private final LowkeyCore core;
    private final NamespacedKey maceKey;
    private final NamespacedKey recipeKey = NamespacedKey.minecraft("mace");
    private final File file;
    private Recipe vanillaRecipe;

    // ---- saved in mace.yml
    private boolean enabled = true;
    /** true from the moment the first mace is crafted (or given): no second one can be made */
    private boolean crafted;
    /** counts up with every reset: a mace of an older generation is removed wherever it shows up */
    private int generation = 1;
    private long cooldownMinutes = 10L;
    private long cooldownLeftMs;
    private Where where = Where.NONE;
    /** the player who carries (or last carried) the mace */
    private UUID holder;
    private String holderName = "";
    private UUID groundItem;
    private Location groundLoc;

    // ---- not saved
    private UUID pendingCrafter;
    private long lastTickMs;
    private boolean dirty;
    private int saveCounter;

    MaceManager(LowkeyCore core) {
        this.core = core;
        this.maceKey = new NamespacedKey(core, "mace");
        this.file = new File(core.getDataFolder(), "mace.yml");
        load();
    }

    // ------------------------------------------------------------------ lifecycle

    void start() {
        vanillaRecipe = Bukkit.getRecipe(recipeKey);
        if (crafted) {
            Bukkit.removeRecipe(recipeKey);
        }
        lastTickMs = System.currentTimeMillis();
        core.getServer().getPluginManager().registerEvents(this, core);
        core.getServer().getScheduler().runTaskTimer(core, this::tick, 20L, 20L);
    }

    void stop() {
        save();
    }

    private static UUID parseUuid(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void load() {
        YamlConfiguration y = YamlConfiguration.loadConfiguration(file);
        enabled = y.getBoolean("enabled", true);
        crafted = y.getBoolean("crafted", false);
        generation = Math.max(1, y.getInt("generation", 1));
        cooldownMinutes = Math.max(0L, y.getLong("cooldown-minutes", 10L));
        cooldownLeftMs = Math.max(0L, y.getLong("cooldown-left-ms", 0L));
        try {
            where = Where.valueOf(y.getString("where", "NONE"));
        } catch (IllegalArgumentException e) {
            where = Where.NONE;
        }
        holder = parseUuid(y.getString("holder"));
        holderName = y.getString("holder-name", "");
        groundItem = parseUuid(y.getString("ground-item"));
        groundLoc = LowkeyCore.readLoc(y, "ground-loc");
    }

    private void save() {
        YamlConfiguration y = new YamlConfiguration();
        y.set("enabled", enabled);
        y.set("crafted", crafted);
        y.set("generation", generation);
        y.set("cooldown-minutes", cooldownMinutes);
        y.set("cooldown-left-ms", cooldownLeftMs);
        y.set("where", where.name());
        y.set("holder", holder == null ? null : holder.toString());
        y.set("holder-name", holderName);
        y.set("ground-item", groundItem == null ? null : groundItem.toString());
        LowkeyCore.writeLoc(y, "ground-loc", groundLoc);
        try {
            core.getDataFolder().mkdirs();
            y.save(file);
        } catch (IOException e) {
            core.getLogger().warning("Kon mace.yml niet opslaan: " + e.getMessage());
        }
        dirty = false;
        saveCounter = 0;
    }

    // ------------------------------------------------------------------ item helpers

    private static boolean isMace(ItemStack item) {
        return item != null && item.getType() == Material.MACE;
    }

    private Integer taggedGeneration(ItemStack item) {
        if (!isMace(item) || !item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(maceKey, PersistentDataType.INTEGER);
    }

    /** The real mace of this generation. */
    private boolean isValidMace(ItemStack item) {
        Integer gen = taggedGeneration(item);
        return gen != null && gen == generation;
    }

    /** A mace of an older generation (from before a reset). */
    private boolean isStale(ItemStack item) {
        Integer gen = taggedGeneration(item);
        return gen != null && gen != generation;
    }

    private static boolean isBundle(ItemStack item) {
        return item != null && item.getType().name().endsWith("BUNDLE");
    }

    private ItemStack createMace() {
        ItemStack item = new ItemStack(Material.MACE);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(maceKey, PersistentDataType.INTEGER, generation);
        item.setItemMeta(meta);
        return item;
    }

    private boolean hasValidMace(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isValidMace(item)) {
                return true;
            }
        }
        return false;
    }

    private static String formatTime(long ms) {
        long totalSeconds = (ms + 999L) / 1000L;
        return String.format(Locale.ROOT, "%d:%02d", totalSeconds / 60L, totalSeconds % 60L);
    }

    private static String where(Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return "onbekend";
        }
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " (" + loc.getWorld().getName() + ")";
    }

    // ------------------------------------------------------------------ the one-second tick

    private boolean cooldownRuns() {
        if (where == Where.GROUND) {
            return true;
        }
        if (where == Where.CARRIED && holder != null) {
            Player p = Bukkit.getPlayer(holder);
            return p != null && !core.inLobby(p);
        }
        return false;
    }

    private void tick() {
        long now = System.currentTimeMillis();
        long elapsed = Math.max(0L, now - lastTickMs);
        lastTickMs = now;

        if (cooldownLeftMs > 0L && cooldownRuns()) {
            cooldownLeftMs = Math.max(0L, cooldownLeftMs - elapsed);
            dirty = true;
        }
        if (where == Where.GROUND) {
            checkGround();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            purgeInvalidMaces(player);
            syncOverlay(player);
        }
        if (dirty && ++saveCounter >= 30) {
            save();
        }
    }

    /** The mace lies on the ground: follow the item, and notice when it is gone (lava, void, despawn...). */
    private void checkGround() {
        Entity entity = groundItem == null ? null : Bukkit.getEntity(groundItem);
        if (entity != null && entity.isValid()) {
            groundLoc = entity.getLocation().clone();
            return;
        }
        if (groundLoc != null && groundLoc.getWorld() != null) {
            World world = groundLoc.getWorld();
            if (world.isChunkLoaded(groundLoc.getBlockX() >> 4, groundLoc.getBlockZ() >> 4)) {
                markDestroyed();
            }
        }
    }

    private void markDestroyed() {
        where = Where.DESTROYED;
        groundItem = null;
        cooldownLeftMs = 0L;
        save();
        core.getLogger().info("De mace is vernietigd.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (core.isAdmin(player)) {
                player.sendMessage(Component.text("De mace is vernietigd en is nu weg. Reset hem in /lowkey, Mace, "
                        + "als hij opnieuw gecraft mag worden.", NamedTextColor.YELLOW));
            }
        }
    }

    /** Removes every mace that should not exist: older generations, and (once the real one is crafted) untagged ones. */
    private void purgeInvalidMaces(Player player) {
        if (core.isBypass(player)) {
            return;
        }
        PlayerInventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (isMace(item) && !isValidMace(item) && (isStale(item) || crafted)) {
                inv.setItem(i, null);
                core.getLogger().info("Een ongeldige mace is weggehaald bij " + player.getName() + ".");
            }
        }
        ItemStack cursor = player.getItemOnCursor();
        if (isMace(cursor) && !isValidMace(cursor) && (isStale(cursor) || crafted)) {
            player.setItemOnCursor(null);
        }
    }

    /** Puts the cooldown overlay on the mace of whoever holds it, and takes it away from everybody else. */
    private void syncOverlay(Player player) {
        int want = 0;
        if (cooldownLeftMs > 0L && !core.inLobby(player) && hasValidMace(player)) {
            want = (int) Math.max(1L, (cooldownLeftMs + 49L) / 50L);
        }
        int current = player.getCooldown(Material.MACE);
        if (want == 0) {
            if (current > 0) {
                player.setCooldown(Material.MACE, 0);
            }
        } else if (Math.abs(current - want) > 10) {
            player.setCooldown(Material.MACE, want);
        }
    }

    // ------------------------------------------------------------------ crafting (only one, ever)

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (!isMace(result)) {
            return;
        }
        if (!enabled || crafted) {
            event.getInventory().setResult(null);
            return;
        }
        event.getInventory().setResult(createMace());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCraftCheck(CraftItemEvent event) {
        if (!isMace(event.getRecipe().getResult()) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!enabled || crafted) {
            event.setCancelled(true);
            return;
        }
        if (event.isShiftClick()) {
            event.setCancelled(true);
            player.sendMessage(Component.text("Craft de mace zonder shift te gebruiken.", NamedTextColor.RED));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!isMace(event.getRecipe().getResult()) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final UUID crafterId = event.getWhoClicked().getUniqueId();
        // provisional: blocks everybody else right away, confirmed (or undone) one tick later
        crafted = true;
        pendingCrafter = crafterId;
        Bukkit.getScheduler().runTask(core, () -> finishCraft(crafterId));
    }

    private void finishCraft(UUID crafterId) {
        if (pendingCrafter == null || !pendingCrafter.equals(crafterId)) {
            return;
        }
        pendingCrafter = null;
        Player crafter = Bukkit.getPlayer(crafterId);
        boolean found = where == Where.GROUND
                || (crafter != null && (hasValidMace(crafter) || isValidMace(crafter.getItemOnCursor())));
        if (!found) {
            // the click did not actually hand out a mace
            crafted = false;
            where = Where.NONE;
            save();
            return;
        }
        holder = crafterId;
        holderName = crafter != null ? crafter.getName() : "";
        if (where != Where.GROUND) {
            where = Where.CARRIED;
        }
        cooldownLeftMs = 0L;
        Bukkit.removeRecipe(recipeKey);
        save();
        if (crafter != null) {
            announce(crafter);
        }
    }

    private void restoreRecipe() {
        if (Bukkit.getRecipe(recipeKey) != null) {
            return;
        }
        Recipe recipe = vanillaRecipe;
        if (recipe == null) {
            ShapedRecipe fallback = new ShapedRecipe(recipeKey, new ItemStack(Material.MACE));
            fallback.shape("H", "B");
            fallback.setIngredient('H', Material.HEAVY_CORE);
            fallback.setIngredient('B', Material.BREEZE_ROD);
            recipe = fallback;
            vanillaRecipe = fallback;
        }
        Bukkit.addRecipe(recipe);
    }

    // ------------------------------------------------------------------ the title

    private static Component titleFor(LowkeyCore.Side side) {
        if (side == LowkeyCore.Side.NONE) {
            return Component.text("De mace is gecraft", NamedTextColor.WHITE);
        }
        String name = side == LowkeyCore.Side.NOORD ? "Noord" : "Zuid";
        return Component.text()
                .append(Component.text(name, TextColor.color(side.rgb)))
                .append(Component.text(" heeft de mace ontgrendeld", NamedTextColor.WHITE))
                .build();
    }

    private static Title buildTitle(LowkeyCore.Side side) {
        return Title.title(titleFor(side), Component.empty(),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(3000), Duration.ofMillis(1000)));
    }

    private void announce(Player crafter) {
        Title title = buildTitle(core.getSide(crafter));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.8f, 1.0f);
        }
    }

    // ------------------------------------------------------------------ smash attack + cooldown

    /** The attacker when this hit is a real smash attack with a mace, otherwise null. */
    private Player smashAttacker(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return null;
        }
        Player attacker = (Player) event.getDamager();
        if (!isMace(attacker.getInventory().getItemInMainHand())) {
            return null;
        }
        double fall = attacker.getFallDistance();
        if (fall > 1.5 && !attacker.isGliding()) {
            return attacker;
        }
        return null;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSmashCheck(EntityDamageByEntityEvent event) {
        Player attacker = smashAttacker(event);
        if (attacker == null) {
            return;
        }
        if (!enabled) {
            event.setCancelled(true);
            attacker.sendActionBar(Component.text("De mace staat uit.", NamedTextColor.RED));
            return;
        }
        if (event.getEntity() instanceof Player && cooldownLeftMs > 0L) {
            event.setCancelled(true);
            attacker.sendActionBar(Component.text()
                    .append(Component.text("De mace is nog ", NamedTextColor.RED))
                    .append(Component.text(formatTime(cooldownLeftMs), NamedTextColor.WHITE))
                    .append(Component.text(" op cooldown", NamedTextColor.RED))
                    .build());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSmashApply(EntityDamageByEntityEvent event) {
        Player attacker = smashAttacker(event);
        if (attacker == null || !(event.getEntity() instanceof Player) || event.getEntity().equals(attacker)) {
            return;
        }
        if (isValidMace(attacker.getInventory().getItemInMainHand())) {
            holder = attacker.getUniqueId();
            holderName = attacker.getName();
            where = Where.CARRIED;
        }
        if (cooldownMinutes <= 0L) {
            return;
        }
        cooldownLeftMs = cooldownMinutes * 60_000L;
        lastTickMs = System.currentTimeMillis();
        save();
        syncOverlay(attacker);
    }

    // ------------------------------------------------------------------ where is the mace

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack stack = item.getItemStack();
        if (!isMace(stack)) {
            return;
        }
        if (isValidMace(stack)) {
            where = Where.GROUND;
            groundItem = item.getUniqueId();
            groundLoc = item.getLocation().clone();
            UUID thrower = item.getThrower();
            if (thrower != null) {
                holder = thrower;
                String name = Bukkit.getOfflinePlayer(thrower).getName();
                holderName = name == null ? "" : name;
            }
            save();
        } else if (isStale(stack) || crafted) {
            event.setCancelled(true); // an old or fake mace never lands on the ground
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        ItemStack stack = event.getItem().getItemStack();
        if (!isValidMace(stack)) {
            return;
        }
        Player player = (Player) event.getEntity();
        where = Where.CARRIED;
        holder = player.getUniqueId();
        holderName = player.getName();
        groundItem = null;
        save();
    }

    /** Mobs never pick the mace up. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMobPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player) && isMace(event.getItem().getItemStack())) {
            event.setCancelled(true);
        }
    }

    // ------------------------------------------------------------------ the mace never goes into storage

    private static boolean isStorage(Inventory top) {
        return top != null && !ALLOWED_TOP.contains(top.getType());
    }

    private void denyStorage(Player player) {
        player.sendMessage(Component.text("De mace kan nergens in opgeslagen worden. Hij moet in je inventory blijven.",
                NamedTextColor.RED));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();

        // bundles
        if ((isMace(cursor) && isBundle(current)) || (isBundle(cursor) && isMace(current))) {
            event.setCancelled(true);
            denyStorage(player);
            return;
        }

        Inventory top = event.getView().getTopInventory();
        if (!isStorage(top)) {
            return;
        }
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) {
            return;
        }
        boolean intoStorage = false;
        if (clicked.equals(top)) {
            if (isMace(cursor)) {
                intoStorage = true;
            }
            if (event.getClick() == ClickType.NUMBER_KEY) {
                int button = event.getHotbarButton();
                if (button >= 0 && isMace(player.getInventory().getItem(button))) {
                    intoStorage = true;
                }
            }
            if (event.getClick() == ClickType.SWAP_OFFHAND && isMace(player.getInventory().getItemInOffHand())) {
                intoStorage = true;
            }
        } else if (event.isShiftClick() && isMace(current)) {
            intoStorage = true;
        }
        if (intoStorage) {
            event.setCancelled(true);
            denyStorage(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || !isMace(event.getOldCursor())) {
            return;
        }
        Inventory top = event.getView().getTopInventory();
        if (!isStorage(top)) {
            return;
        }
        for (int slot : event.getRawSlots()) {
            if (slot < top.getSize()) {
                event.setCancelled(true);
                denyStorage((Player) event.getWhoClicked());
                return;
            }
        }
    }

    /** Hoppers never suck the mace up. */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (isMace(event.getItem().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!isMace(player.getInventory().getItem(event.getHand()))) {
            return;
        }
        if (event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof Allay) {
            event.setCancelled(true);
            denyStorage(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        if (isMace(event.getPlayerItem())) {
            event.setCancelled(true);
            denyStorage(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.DECORATED_POT && isMace(event.getItem())) {
            event.setCancelled(true);
            denyStorage(event.getPlayer());
        }
    }

    // ------------------------------------------------------------------ admin actions

    private void setEnabled(CommandSender sender, boolean on) {
        enabled = on;
        save();
        sender.sendMessage(Component.text("De mace staat nu " + (on ? "aan." : "uit. Hij kan niet gecraft of gebruikt worden."),
                NamedTextColor.GREEN));
    }

    private void setCooldownMinutes(CommandSender sender, long minutes) {
        cooldownMinutes = Math.max(0L, Math.min(1440L, minutes));
        save();
        sender.sendMessage(Component.text("De mace cooldown is nu " + cooldownMinutes + " minuten"
                + (cooldownMinutes == 0L ? " (geen cooldown)." : "."), NamedTextColor.GREEN));
    }

    void resetCooldown(CommandSender sender) {
        cooldownLeftMs = 0L;
        save();
        sender.sendMessage(Component.text("De cooldown van de mace is weggehaald.", NamedTextColor.GREEN));
    }

    private void giveMace(CommandSender giver, Player target) {
        if (core.inLobby(target)) {
            giver.sendMessage(Component.text(target.getName() + " is in de lobby. De mace hoort bij de echte game en kan "
                    + "daar niet in de inventory zitten.", NamedTextColor.RED));
            return;
        }
        if (crafted && (where == Where.CARRIED || where == Where.GROUND)) {
            giver.sendMessage(Component.text("De mace bestaat al (" + statusWord() + "). Reset hem eerst als je een nieuwe "
                    + "wilt geven.", NamedTextColor.RED));
            return;
        }
        if (!target.getInventory().addItem(createMace()).isEmpty()) {
            giver.sendMessage(Component.text("De inventory van " + target.getName() + " zit vol.", NamedTextColor.RED));
            return;
        }
        crafted = true;
        where = Where.CARRIED;
        holder = target.getUniqueId();
        holderName = target.getName();
        groundItem = null;
        cooldownLeftMs = 0L;
        Bukkit.removeRecipe(recipeKey);
        save();
        giver.sendMessage(Component.text(target.getName() + " heeft de mace gekregen.", NamedTextColor.GREEN));
    }

    private void reset(CommandSender sender) {
        generation++;
        int removed = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = player.getInventory();
            ItemStack[] contents = inv.getContents();
            for (int i = 0; i < contents.length; i++) {
                if (isMace(contents[i])) {
                    inv.setItem(i, null);
                    removed++;
                }
            }
            if (isMace(player.getItemOnCursor())) {
                player.setItemOnCursor(null);
                removed++;
            }
            player.setCooldown(Material.MACE, 0);
        }
        if (groundItem != null) {
            Entity entity = Bukkit.getEntity(groundItem);
            if (entity != null) {
                entity.remove();
                removed++;
            }
        }
        crafted = false;
        where = Where.NONE;
        holder = null;
        holderName = "";
        groundItem = null;
        groundLoc = null;
        cooldownLeftMs = 0L;
        pendingCrafter = null;
        restoreRecipe();
        save();
        sender.sendMessage(Component.text("De mace is gereset: hij mag weer gecraft worden. " + removed
                + " oude mace(s) zijn weggehaald (een mace in een offline inventory verdwijnt zodra die speler joint).",
                NamedTextColor.GREEN));
    }

    private void testTitle(Player admin, LowkeyCore.Side side) {
        admin.showTitle(buildTitle(side));
        admin.playSound(admin.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.8f, 1.0f);
    }

    // ------------------------------------------------------------------ status

    private String statusWord() {
        if (!crafted) {
            return "nog niet gecraft";
        }
        if (where == Where.DESTROYED) {
            return "vernietigd";
        }
        if (where == Where.GROUND) {
            return "op de grond";
        }
        return "in bezit van " + (holderName.isEmpty() ? "onbekend" : holderName);
    }

    private String locationWord() {
        if (!crafted || where == Where.DESTROYED || where == Where.NONE) {
            return "-";
        }
        if (where == Where.GROUND) {
            return where(groundLoc);
        }
        Player p = holder == null ? null : Bukkit.getPlayer(holder);
        if (p == null) {
            return "houder is offline";
        }
        if (core.inLobby(p)) {
            return "houder is in de lobby";
        }
        return where(p.getLocation());
    }

    private String cooldownWord() {
        if (cooldownLeftMs <= 0L) {
            return "klaar";
        }
        return formatTime(cooldownLeftMs) + (cooldownRuns() ? "" : " (gepauzeerd)");
    }

    private void sendStatus(CommandSender sender) {
        sender.sendMessage(Component.text("Mace: " + (enabled ? "aan" : "uit") + " | status: " + statusWord()
                + " | locatie: " + locationWord() + " | cooldown: " + cooldownWord() + " | cooldown duur: "
                + cooldownMinutes + " min", NamedTextColor.GRAY));
        sender.sendMessage(Component.text("Gebruik: /lowkey mace <status|aan|uit|cooldown <minuten>|cooldownreset|give <speler>"
                + "|reset|test <noord|zuid|geen>>", NamedTextColor.GRAY));
    }

    // ------------------------------------------------------------------ command

    /** args are the arguments after "mace". */
    void handleCommand(CommandSender sender, String[] args) {
        String sub = args.length == 0 ? "status" : args[0].toLowerCase(Locale.ROOT);
        if (sub.equals("status")) {
            sendStatus(sender);
        } else if (sub.equals("aan")) {
            setEnabled(sender, true);
        } else if (sub.equals("uit")) {
            setEnabled(sender, false);
        } else if (sub.equals("cooldown")) {
            long minutes = -1L;
            if (args.length == 2) {
                try {
                    minutes = Long.parseLong(args[1]);
                } catch (NumberFormatException ignored) {
                    minutes = -1L;
                }
            }
            if (minutes < 0L) {
                sender.sendMessage(Component.text("Gebruik: /lowkey mace cooldown <minuten>", NamedTextColor.GRAY));
            } else {
                setCooldownMinutes(sender, minutes);
            }
        } else if (sub.equals("cooldownreset")) {
            resetCooldown(sender);
        } else if (sub.equals("give")) {
            Player target = args.length == 2 ? Bukkit.getPlayerExact(args[1]) : null;
            if (args.length != 2) {
                sender.sendMessage(Component.text("Gebruik: /lowkey mace give <speler>", NamedTextColor.GRAY));
            } else if (target == null) {
                sender.sendMessage(Component.text("Die speler is niet online.", NamedTextColor.RED));
            } else {
                giveMace(sender, target);
            }
        } else if (sub.equals("reset")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                reset(sender);
            } else if (sender instanceof Player) {
                final Player admin = (Player) sender;
                openResetConfirm(admin);
            } else {
                sender.sendMessage(Component.text("Gebruik in de console: /lowkey mace reset confirm", NamedTextColor.GRAY));
            }
        } else if (sub.equals("test")) {
            LowkeyCore.Side side = args.length == 2 ? LowkeyCore.Side.fromId(args[1].toLowerCase(Locale.ROOT)) : null;
            if (!(sender instanceof Player) || side == null) {
                sender.sendMessage(Component.text("Gebruik in-game: /lowkey mace test <noord|zuid|geen>", NamedTextColor.GRAY));
            } else {
                testTitle((Player) sender, side);
            }
        } else {
            sendStatus(sender);
        }
    }

    /** args are ALL arguments (args[0] is "mace"). */
    List<String> tabComplete(String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 2) {
            options.add("status");
            options.add("aan");
            options.add("uit");
            options.add("cooldown");
            options.add("cooldownreset");
            options.add("give");
            options.add("reset");
            options.add("test");
        } else if (args.length == 3) {
            String sub = args[1].toLowerCase(Locale.ROOT);
            if (sub.equals("give")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    options.add(player.getName());
                }
            } else if (sub.equals("cooldown")) {
                options.add("5");
                options.add("10");
                options.add("15");
            } else if (sub.equals("reset")) {
                options.add("confirm");
            } else if (sub.equals("test")) {
                options.add("noord");
                options.add("zuid");
                options.add("geen");
            }
        }
        String typed = args[args.length - 1].toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(typed)) {
                result.add(option);
            }
        }
        return result;
    }

    // ------------------------------------------------------------------ the Mace tab in /lowkey

    void openMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_COUNTDOWN, "Cooldown instellen", NamedTextColor.WHITE),
                "Stel de cooldown van de mace in (in minuten).", 150, () -> openCooldownDialog(admin)));
        if (enabled) {
            buttons.add(core.button(core.iconLabel(LowkeyCore.M_BARRIER, "Mace uitzetten", NamedTextColor.RED),
                    "Niemand kan de mace dan craften of gebruiken.", 150, () -> {
                        setEnabled(admin, false);
                        openMenu(admin);
                    }));
        } else {
            buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Mace aanzetten", NamedTextColor.GREEN),
                    "De mace kan weer gecraft en gebruikt worden.", 150, () -> {
                        setEnabled(admin, true);
                        openMenu(admin);
                    }));
        }
        if (cooldownLeftMs > 0L) {
            buttons.add(core.button(core.iconLabel(LowkeyCore.M_WATER, "Cooldown wissen", NamedTextColor.WHITE),
                    "Haalt de lopende cooldown van de mace weg.", 150, () -> {
                        resetCooldown(admin);
                        openMenu(admin);
                    }));
        }
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_HORN, "Titel testen", NamedTextColor.WHITE),
                "Laat de titel alleen aan jou zien.", 150, () -> openTitleTest(admin)));
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_DIAMOND, "Mace geven", NamedTextColor.WHITE),
                "Geeft de mace aan een speler (alleen als hij nog niet bestaat).", 150,
                () -> core.pickPlayer(admin, "Mace geven", target -> {
                    giveMace(admin, target);
                    openMenu(admin);
                }, () -> openMenu(admin))));
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_FIRE, "Mace resetten", NamedTextColor.RED),
                "Haalt de mace weg en laat hem opnieuw craften.", 150, () -> openResetConfirm(admin)));

        Component body = LowkeyCore.joinLines(
                LowkeyCore.statusLine("Mace", enabled ? "aan" : "uit", enabled ? NamedTextColor.GREEN : NamedTextColor.RED),
                LowkeyCore.statusLine("Status", statusWord(), !crafted ? NamedTextColor.YELLOW
                        : (where == Where.DESTROYED ? NamedTextColor.RED : NamedTextColor.GREEN)),
                LowkeyCore.statusLine("Locatie", locationWord(), NamedTextColor.WHITE),
                LowkeyCore.statusLine("Cooldown", cooldownWord(),
                        cooldownLeftMs > 0L ? NamedTextColor.RED : NamedTextColor.GREEN),
                LowkeyCore.statusLine("Cooldown duur", cooldownMinutes + " minuten", NamedTextColor.WHITE));
        core.showMenu(admin, "Mace", body, buttons, core.backButton("Terug naar het hoofdmenu.", () -> core.openMenu(admin)));
    }

    private void openResetConfirm(Player admin) {
        core.confirm(admin,
                "Mace resetten?",
                "De mace wordt overal weggehaald en mag daarna opnieuw gecraft worden.",
                "De cooldown wordt ook gewist.",
                Component.text("Ja, resetten", NamedTextColor.RED),
                () -> {
                    reset(admin);
                    openMenu(admin);
                },
                () -> openMenu(admin));
    }

    private void openTitleTest(Player admin) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(core.button(Component.text("Noord", NamedTextColor.RED), "Laat de Noord titel zien.", 100,
                () -> testTitle(admin, LowkeyCore.Side.NOORD)));
        buttons.add(core.button(Component.text("Zuid", NamedTextColor.BLUE), "Laat de Zuid titel zien.", 100,
                () -> testTitle(admin, LowkeyCore.Side.ZUID)));
        buttons.add(core.button(Component.text("Geen team", NamedTextColor.GRAY), "Laat de titel zonder team zien.", 100,
                () -> testTitle(admin, LowkeyCore.Side.NONE)));
        core.showMenu(admin, "Titel testen",
                Component.text("De titel verschijnt alleen bij jou. Sluit dit scherm om hem te zien.", NamedTextColor.GRAY),
                buttons, core.backButton("Terug naar de mace.", () -> openMenu(admin)));
    }

    private void openCooldownDialog(Player admin) {
        DialogInput minutes = DialogInput.text("minuten", Component.text("Cooldown in minuten (0 tot 1440)"))
                .width(300).initial(String.valueOf(cooldownMinutes)).maxLength(4).build();
        DialogAction save = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!core.isAdmin(clicker)) {
                return;
            }
            final String text = view.getText("minuten");
            Bukkit.getScheduler().runTask(core, () -> {
                long value = -1L;
                try {
                    value = Long.parseLong(text == null ? "" : text.trim());
                } catch (NumberFormatException ignored) {
                    value = -1L;
                }
                if (value < 0L || value > 1440L) {
                    clicker.sendMessage(Component.text("Geef een getal tussen 0 en 1440.", NamedTextColor.RED));
                } else {
                    setCooldownMinutes(clicker, value);
                }
                openMenu(clicker);
            });
        }, LowkeyCore.CLICK_OPTIONS);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(core.dlgTitle(admin, "Mace cooldown"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Zo lang moet de mace wachten na een smash attack op een speler.", NamedTextColor.GRAY))))
                        .inputs(List.of(minutes))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(core.iconLabel(LowkeyCore.M_MACE, "Opslaan", NamedTextColor.GREEN),
                                Component.text("Sla de cooldown op."), 150, save),
                        core.backButton("Terug naar de mace.", () -> openMenu(admin)))));
        admin.showDialog(dialog);
    }
}
