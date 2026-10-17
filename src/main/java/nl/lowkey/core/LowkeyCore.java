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
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
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
    private static final String M_COUNTDOWN = "\uE052";
    /** the mace icon of the Mace tab: add \uE053 to tags.json with the vanilla mace item texture */
    static final String M_MACE = "\uE053";

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
        loadChecklist();
        loadEventCamera();
        loadMenuDesigns();
        loadMenuEdits();
        finaleOnEnable();

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

        // tab list entry: icons + name in the team colour. Eliminated players are grey and struck through.
        TextColor nameColor = side == Side.NONE ? NamedTextColor.WHITE : TextColor.color(side.rgb);
        boolean out = isLosersOut(player) || (isEliminated(player) && !inLosers(player));
        TextComponent.Builder entry = Component.text().append(icons);
        if (out) {
            entry.append(Component.text(player.getName(), NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
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

        // lobby, main world and Verliezerseiland each have their own chat (staff sees all of them)
        final int senderGroup = worldGroup(sender);
        event.viewers().removeIf(viewer -> viewer instanceof Player && !viewer.equals(sender)
                && worldGroup((Player) viewer) != senderGroup && !isAdmin((Player) viewer));
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
                if (isAdmin(viewerPlayer)) {
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
     * borders drop Noord and Zuid cannot send each other private messages either. Staff may always.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWhisper(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        if (isAdmin(sender)) {
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
                ItemStack[] stash = player.getInventory().getContents().clone();
                for (int i = 0; i < stash.length; i++) {
                    if (isLobbyItem(stash[i]) || isLosersItem(stash[i])) {
                        stash[i] = null; // the stars belong to the lobby, never to the real inventory
                    }
                }
                data.set("inv", Arrays.asList(stash));
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
            // the LowkeySMP star only says "you are dead", the red star leads to the Verliezerseiland
            removeLobbyItems(player);
            giveLobbyItem(player);
            if (!losersEnded && !isLosersOut(player)) {
                giveLosersItem(player);
            }
        } else {
            giveLobbyItem(player);
        }
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
     * /lowkey menu [nieuw|klassiek] : opent het beheerpaneel in het nieuwe of het klassieke ontwerp (per admin onthouden)
     * /lowkey tijdlijn : alle dagen van 7 tot 21 nov met wat je moet doen, plus testen en herstellen
     * /lowkey end <open|close> : opent of sluit het End (bij openen: titel + bericht voor iedereen)
     * /lowkey checklist : de checklist met alles wat je moet doen voor je de grenzen dropt
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
        if (sender instanceof Player && args.length == 2 && args[0].equalsIgnoreCase("menu")) {
            String choice = args[1].toLowerCase(Locale.ROOT);
            if (choice.equals("klassiek") || choice.equals("classic")) {
                setMenuDesign((Player) sender, false);
            } else if (choice.equals("nieuw") || choice.equals("new")) {
                setMenuDesign((Player) sender, true);
            } else if (choice.equals("edit") || choice.equals("bewerk") || choice.equals("bewerken")) {
                toggleMenuEditMode((Player) sender);
            } else if (choice.equals("geavanceerd") || choice.equals("advanced")) {
                setMenuAdvanced((Player) sender, true);
                sender.sendMessage(Component.text("Geavanceerde modus staat aan.", NamedTextColor.GREEN));
            } else if (choice.equals("simpel") || choice.equals("simple")) {
                setMenuAdvanced((Player) sender, false);
                sender.sendMessage(Component.text("Simpele modus staat aan.", NamedTextColor.GREEN));
            }
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
        if (args.length >= 1 && (args[0].equalsIgnoreCase("tijdlijn") || args[0].equalsIgnoreCase("timeline"))) {
            if (sender instanceof Player) {
                openTimelineMenu((Player) sender);
            } else {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
            }
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
        if (args.length == 1 && args[0].equalsIgnoreCase("checklist")) {
            if (sender instanceof Player) {
                openChecklistMenu((Player) sender);
            } else {
                sender.sendMessage(Component.text("Dit commando kan alleen in-game.", NamedTextColor.RED));
            }
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
                "Gebruik: /lowkey grace <speler> <minuten>  |  /lowkey graceall [minuten]  |  /lowkey team <speler> <noord|zuid|geen>  |  /lowkey crew <speler> <aan|uit>  |  /lowkey nether <open|close>  |  /lowkey server <open|close>  |  /lowkey border <open|close>  |  /lowkey revive <speler>  |  /lowkey donate  |  /lowkey website  |  /lowkey launch [reset]  |  /lowkey lobby <create|tp|setspawn|delete>  |  /lowkey setspawn <noord|zuid>  |  /lowkey main  |  /lowkey mainworld <open|close>  |  /lowkey seed <seed|random|cancel>  |  /lowkey pin <pincode>  |  /lowkey countdown <spawn|remove|set|size>  |  /lowkey mace <status|aan|uit|cooldown|cooldownreset|give|reset|test>  |  /lowkey checklist  |  /lowkey event <start|test|stop|camera|teams|reset>  |  /lowkey losers <create|tp|setspawn|open|close|return|returnall|delete>  |  /lowkey end <open|close>  |  /lowkey tijdlijn  |  /lowkey say <bericht>",
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
        buttonActions.put(dialogAction, action);
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

    void showMenu(Player admin, String title, Component body, List<ActionButton> buttons, ActionButton exit) {
        showMenuKeyed(admin, title, title, body, buttons, exit);
    }

    /** The key says which screen this is (for the menu editor), the title is what is shown. */
    void showMenuKeyed(Player admin, String key, String title, Component body, List<ActionButton> buttons,
                       ActionButton exit) {
        if (newDesign(admin)) {
            showThemedMenuKeyed(admin, key, title, body, buttons, exit, true, false);
            return;
        }
        EditedScreen es = applyMenuEdits(admin, key, title, body, buttons, 150, 2);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, es.title))
                        .body(List.of(DialogBody.plainMessage(es.body)))
                        .build())
                .type(DialogType.multiAction(es.buttons, exit, es.columns)));
        admin.showDialog(dialog);
    }

    /** The main menu: the new Home or the classic menu, whatever this admin chose. */
    void openMenu(Player admin) {
        if (newDesign(admin)) {
            openHome(admin);
        } else {
            openClassicMenu(admin);
        }
    }

    /** The classic main menu (the old design). */
    private void openClassicMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_COUNTDOWN, "Tijdlijn", NamedTextColor.GREEN),
                "Alle dagen van 7 tot 21 nov met wat je moet doen.", 150, () -> openTimelineMenu(admin)));
        buttons.add(button(iconLabel(M_SERVER, "Server", NamedTextColor.WHITE),
                "Launch en server open of dicht.", 150, () -> openServerMenu(admin)));
        buttons.add(button(iconLabel(M_WORLD, "Wereld", NamedTextColor.WHITE),
                "Nether, grenzen, lobby en spawnpunten.", 150, () -> openWorldMenu(admin)));
        buttons.add(button(iconLabel(M_PLAYERS, "Spelers", NamedTextColor.WHITE),
                "Team, crew, grace en dood-status.", 150, () -> openPlayersMenu(admin)));
        buttons.add(button(iconLabel(M_MESSAGES, "Berichten", NamedTextColor.WHITE),
                "Eigen bericht, donatiebericht en websitebericht.", 150, () -> openMessagesMenu(admin)));
        buttons.add(button(iconLabel(M_MACE, "Mace", NamedTextColor.WHITE),
                "De ene mace: cooldown, houder en reset.", 150, () -> mace.openMenu(admin)));
        buttons.add(button(iconLabel(M_BORDERS, "Checklist", NamedTextColor.WHITE),
                "Alles wat je moet doen voor je de grenzen dropt.", 150, () -> openChecklistMenu(admin)));
        buttons.add(button(iconLabel(M_TEAM, "Team event", NamedTextColor.WHITE),
                "Verdeel iedereen over Noord en Zuid met een show.", 150, () -> openEventMenu(admin)));
        buttons.add(button(iconLabel(M_REVIVE, "Verliezerseiland", NamedTextColor.WHITE),
                "De aparte wereld voor iedereen die dood is.", 150, () -> openLosersMenu(admin)));
        if (loggedIn.contains(admin.getUniqueId()) && !isOwner(admin)) {
            buttons.add(button(Component.text("Uitloggen", NamedTextColor.RED),
                    "Log uit en verlies je toegang tot /lowkey en de commands.", 150, () -> logout(admin, true)));
        }
        buttons.add(button(iconLabel(M_BORDERS, "Finale", NamedTextColor.LIGHT_PURPLE),
                "De krimpende border, de winnaarsshow en de instellingen.", 150, () -> openFinaleMenu(admin)));
        buttons.add(button(iconLabel(M_MESSAGES, "Menu bewerken", NamedTextColor.YELLOW),
                "Pas alle menu's aan in de game zelf.", 150, () -> {
                    menuEditMode.add(admin.getUniqueId());
                    openMenu(admin);
                }));
        buttons.add(button(iconLabel(M_BACK, "Nieuw menu", NamedTextColor.GREEN),
                "Schakel naar het nieuwe menu-ontwerp.", 150, () -> {
                    setMenuDesign(admin, true);
                    openMenu(admin);
                }));
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
                statusLine("Online", String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE));
        showMenu(admin, "LowkeySMP Beheer", status, buttons, close);
    }

    // ---- section: Server

    private void openServerMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        if (newDesign(admin)) {
            openNewWorldMenu(admin);
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

        if (mainOpen) {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld sluiten", NamedTextColor.RED),
                    "Iedereen behalve admins gaat naar de lobby. Handig om iets aan te passen.", 150, () -> confirm(admin,
                            "Main wereld sluiten?",
                            "Iedereen behalve admins gaat naar de lobby en kan er niet uit tot je de main wereld opent.",
                            "Spelers houden hun spullen en komen later terug op hun laatste plek.",
                            Component.text("Ja, sluiten", NamedTextColor.RED),
                            () -> {
                                closeMainWorld(admin);
                                openServerMenu(admin);
                            },
                            () -> openServerMenu(admin))));
        } else {
            buttons.add(button(iconLabel(M_MAIN, "Main wereld openen", NamedTextColor.GREEN),
                    "Spelers in de lobby kunnen er weer in met hun item. Niemand wordt verplaatst.", 150, () -> {
                        openMainWorld(admin);
                        openServerMenu(admin);
                    }));
        }

        buttons.add(button(iconLabel(M_COUNTDOWN, "Countdown", NamedTextColor.WHITE),
                "Hologram met de aftelling naar de start.", 150, () -> openCountdownMenu(admin)));

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
                        launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Main wereld", mainOpen ? "open" : "gesloten",
                        mainOpen ? NamedTextColor.GREEN : NamedTextColor.RED));
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

    // ---- section: Wereld (an overview with three small screens: Nether en grenzen, Lobby, Spawnpunten)

    private void openWorldMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        if (newDesign(admin)) {
            openNewWorldMenu(admin);
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_BORDERS, "Nether en grenzen", NamedTextColor.WHITE),
                "Nether openen of sluiten en de grenzen droppen.", 150, () -> openAccessMenu(admin)));
        buttons.add(button(iconLabel(M_LOBBY, "Lobby", NamedTextColor.WHITE),
                "Lobby aanmaken, spawn, bescherming en verwijderen.", 150, () -> openLobbyMenu(admin)));
        buttons.add(button(iconLabel(M_SPAWN_N, "Spawnpunten", NamedTextColor.WHITE),
                "Spawn van Noord en Zuid en naar het main eiland.", 150, () -> openSpawnMenu(admin)));

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

    /** Wereld > Nether en grenzen */
    private void openAccessMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        if (netherOpen) {
            buttons.add(button(iconLabel(M_NETHER, "Nether sluiten", NamedTextColor.RED),
                    "Sluit de Nether weer. Wie erin zit kan er nog uit.", 150, () -> {
                        setNether(false);
                        openAccessMenu(admin);
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
                                openAccessMenu(admin);
                            },
                            () -> openAccessMenu(admin))));
        }
        buttons.add(endToggle(admin, () -> openAccessMenu(admin)));
        if (bordersDropped) {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen dicht", NamedTextColor.RED),
                    "Noord en Zuid zien daarna weer alleen hun eigen chat.", 150, () -> {
                        setBordersDropped(false);
                        openAccessMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_BORDERS, "Grenzen droppen", NamedTextColor.GREEN),
                    "Opent eerst de checklist, zodat je niks vergeet.", 150, () -> openChecklistMenu(admin)));
        }
        Component body = joinLines(
                statusLine("Nether", netherOpen ? "open" : "dicht",
                        netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("End", endOpen ? "open" : "dicht",
                        endOpen ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                statusLine("Checklist", (BORDER_STEPS.size() - missingChecklistSteps().size()) + " van " + BORDER_STEPS.size(),
                        missingChecklistSteps().isEmpty() ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Nether en grenzen", body, buttons, backButton("Terug naar wereld.", () -> openWorldMenu(admin)));
    }

    /** Wereld > Lobby */
    private void openLobbyMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        if (lobbyCreated && lobbyWorld() != null) {
            buttons.add(button(iconLabel(M_LOBBY, "Naar de lobby", NamedTextColor.WHITE),
                    "Teleporteert jou naar de lobby.", 150, () -> teleportAdminToLobby(admin)));
            buttons.add(button(iconLabel(M_LOBBY, "Lobby spawn zetten", NamedTextColor.WHITE),
                    "Zet het lobby spawnpunt op jouw plek. Je moet in de lobby staan.", 150, () -> {
                        setLobbySpawn(admin);
                        openLobbyMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_LOBBY, "Lobby aanmaken", NamedTextColor.GREEN),
                    "Maakt de lege lobby wereld met een klein platform.", 150, () -> {
                        createLobby(admin);
                        openLobbyMenu(admin);
                    }));
        }
        if (lobbyProtect) {
            buttons.add(button(iconLabel(M_LOBBY, "Bescherming uit", NamedTextColor.RED),
                    "Zet sneeuw, vuur, water, explosies en groei in de lobby weer aan, bijvoorbeeld om te bouwen.", 150, () -> {
                        setLobbyProtect(false);
                        openLobbyMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_LOBBY, "Bescherming aan", NamedTextColor.GREEN),
                    "Zet sneeuw, vuur, water, explosies en groei in de lobby uit.", 150, () -> {
                        setLobbyProtect(true);
                        openLobbyMenu(admin);
                    }));
        }
        if (lobbyExists()) {
            buttons.add(button(iconLabel(M_LOBBY, "Lobby verwijderen", NamedTextColor.RED),
                    "Wist de hele lobby wereld, ook oude resten.", 150,
                    () -> openLobbyDeleteConfirm(admin, () -> openLobbyMenu(admin))));
        }
        Component body = joinLines(
                statusLine("Lobby", lobbyCreated ? "aangemaakt" : (lobbyExists() ? "oude rest" : "nog niet"),
                        lobbyCreated ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Bescherming", lobbyProtect ? "aan" : "uit",
                        lobbyProtect ? NamedTextColor.GREEN : NamedTextColor.RED));
        showMenu(admin, "Lobby", body, buttons, backButton("Terug naar wereld.", () -> openWorldMenu(admin)));
    }

    /** Wereld > Spawnpunten */
    private void openSpawnMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_SPAWN_N, "Spawn Noord zetten", NamedTextColor.RED),
                "Zet het spawnpunt van Noord op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.NOORD);
                    openSpawnMenu(admin);
                }));
        buttons.add(button(iconLabel(M_SPAWN_Z, "Spawn Zuid zetten", NamedTextColor.BLUE),
                "Zet het spawnpunt van Zuid op jouw plek.", 150, () -> {
                    setTeamSpawn(admin, Side.ZUID);
                    openSpawnMenu(admin);
                }));
        buttons.add(button(iconLabel(M_MAIN, "Naar main eiland", NamedTextColor.WHITE),
                "Teleporteert jou naar de gamewereld (laatste plek of team spawn).", 150,
                () -> enterGame(admin, false, true)));
        Component body = joinLines(
                statusLine("Spawn Noord", spawnNoord != null ? "ingesteld" : "niet ingesteld",
                        spawnNoord != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Zuid", spawnZuid != null ? "ingesteld" : "niet ingesteld",
                        spawnZuid != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        showMenu(admin, "Spawnpunten", body, buttons, backButton("Terug naar wereld.", () -> openWorldMenu(admin)));
    }

    // ---- section: Spelers

    private void openPlayersMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        if (newDesign(admin)) {
            openNewPlayersMenu(admin);
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
        buttons.add(button(iconLabel(M_GRACE, "Grace voor iedereen", NamedTextColor.WHITE),
                "Zet de grace van alle spelers weer op de standaard tijd, ook van spelers die nu offline zijn.", 150,
                () -> {
                    resetGraceForEveryone(admin, graceMillis / 60_000L);
                    openPlayersMenu(admin);
                }));
        buttons.add(button(iconLabel(M_REVIVE, "Dood-status weg", NamedTextColor.WHITE),
                "Verwijdert de uitgeschakeld-status van een speler (vooral voor testen).", 150,
                () -> pickPlayer(admin, "Dood-status verwijderen", target -> {
                    clearEliminated(target);
                    admin.sendMessage(Component.text(
                            "Dood-status van " + target.getName() + " is verwijderd.", NamedTextColor.GREEN));
                    openPlayersMenu(admin);
                })));
        buttons.add(button(iconLabel(M_LOBBY, "LowkeySMP ster geven", NamedTextColor.WHITE),
                "Geeft een speler in de lobby de LowkeySMP nether star (hotbar slot 5).", 150,
                () -> pickPlayer(admin, "LowkeySMP ster geven", target -> {
                    giveLobbyItemTo(admin, target);
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

    // ---- shared screens

    /** A yes / no screen. "Annuleren" runs `back`. */
    void confirm(Player admin, String title, String line1, String line2, Component yesLabel,
                 Runnable yes, Runnable back) {
        if (newDesign(admin)) {
            Component text = joinLines(Component.text(line1, NamedTextColor.WHITE),
                    Component.text(line2, NamedTextColor.GRAY));
            Dialog themed = Dialog.create(builder -> builder.empty()
                    .base(DialogBase.builder(themedTitle(title))
                            .body(List.of(headerCard(Material.BARRIER, text)))
                            .build())
                    .type(DialogType.confirmation(
                            button(yesLabel, "Bevestigen", 190, yes),
                            button(Component.text("Annuleren", NamedTextColor.GRAY), "Terug.", 190, back))));
            admin.showDialog(themed);
            return;
        }
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
        pickPlayer(admin, title, next, () -> openPlayersMenu(admin));
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

    private void pickTeam(Player admin, Player target) {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(teamButton(admin, target, Side.NOORD, "Noord", NamedTextColor.RED));
        buttons.add(teamButton(admin, target, Side.ZUID, "Zuid", NamedTextColor.BLUE));
        buttons.add(teamButton(admin, target, Side.NONE, "Geen team", NamedTextColor.GRAY));
        ActionButton back = backButton("Terug naar spelers.", () -> openPlayersMenu(admin));

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Team voor " + target.getName()))
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
                .base(DialogBase.builder(dlgTitle(admin, "Crew badge voor " + target.getName()))
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
                .base(DialogBase.builder(dlgTitle(admin, "Grace voor " + target.getName()))
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

    // ------------------------------------------------------------------ border checklist

    /** One step of the checklist: what it is called, and the command to run for it. */
    private static final class CheckStep {
        final String id;
        final String title;
        final String command;

        CheckStep(String id, String title, String command) {
            this.id = id;
            this.title = title;
            this.command = command;
        }
    }

    /**
     * Everything that has to be done before the borders drop. A WorldGuard flag is removed by running the flag command
     * WITHOUT a value behind it. Add or change steps here.
     */
    private static final List<CheckStep> BORDER_STEPS = List.of(
            new CheckStep("block-break", "block-break flag weg", "/rg flag teamgrens block-break"),
            new CheckStep("block-place", "block-place flag weg", "/rg flag teamgrens block-place"),
            new CheckStep("entry", "entry flag weg", "/rg flag teamgrens entry"),
            new CheckStep("enderpearl", "enderpearl flag weg", "/rg flag teamgrens enderpearl"),
            new CheckStep("chorus", "chorus-fruit-teleport flag weg", "/rg flag teamgrens chorus-fruit-teleport"),
            new CheckStep("controle", "Controleren dat er niks meer staat", "/rg info teamgrens"));

    private final Set<String> checklistDone = new HashSet<>();

    private File checklistFile() {
        return new File(getDataFolder(), "checklist.yml");
    }

    private void loadChecklist() {
        checklistDone.clear();
        YamlConfiguration y = YamlConfiguration.loadConfiguration(checklistFile());
        for (CheckStep step : BORDER_STEPS) {
            if (y.getBoolean("done." + step.id, false)) {
                checklistDone.add(step.id);
            }
        }
    }

    private void saveChecklist() {
        YamlConfiguration y = new YamlConfiguration();
        for (CheckStep step : BORDER_STEPS) {
            y.set("done." + step.id, checklistDone.contains(step.id));
        }
        try {
            getDataFolder().mkdirs();
            y.save(checklistFile());
        } catch (IOException e) {
            getLogger().warning("Kon checklist.yml niet opslaan: " + e.getMessage());
        }
    }

    private void toggleChecklistStep(String id) {
        if (!checklistDone.remove(id)) {
            checklistDone.add(id);
        }
        saveChecklist();
    }

    private List<String> missingChecklistSteps() {
        List<String> missing = new ArrayList<>();
        for (CheckStep step : BORDER_STEPS) {
            if (!checklistDone.contains(step.id)) {
                missing.add(step.title);
            }
        }
        return missing;
    }

    /** The checklist: tick every step, then press "Grenzen droppen" at the bottom. */
    private void openChecklistMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        List<ActionButton> buttons = new ArrayList<>();
        for (CheckStep step : BORDER_STEPS) {
            boolean done = checklistDone.contains(step.id);
            Component label = Component.text()
                    .append(Component.text(done ? "[x] " : "[ ] ", done ? NamedTextColor.GREEN : NamedTextColor.GRAY))
                    .append(Component.text(step.title, done ? NamedTextColor.GREEN : NamedTextColor.WHITE))
                    .build();
            String tooltip = "Doe: " + step.command + (step.command.startsWith("/rg flag") ? " (zonder waarde)" : "")
                    + ". Klik om af te vinken.";
            buttons.add(button(label, tooltip, 300, () -> {
                toggleChecklistStep(step.id);
                openChecklistMenu(admin);
            }));
        }
        buttons.add(button(iconLabel(M_MESSAGES, "Commands in chat", NamedTextColor.AQUA),
                "Zet alle commands klikbaar in de chat. Daarna sluit dit scherm.", 300, () -> sendChecklistCommands(admin)));
        buttons.add(button(iconLabel(M_BORDERS, "Grenzen droppen", NamedTextColor.GREEN),
                "Kan pas als alle stappen afgevinkt zijn.", 300, () -> tryDropBorders(admin)));
        buttons.add(button(Component.text("Checklist leegmaken", NamedTextColor.RED),
                "Haalt alle vinkjes weg.", 300, () -> {
                    checklistDone.clear();
                    saveChecklist();
                    openChecklistMenu(admin);
                }));

        int done = BORDER_STEPS.size() - missingChecklistSteps().size();
        Component body = joinLines(
                statusLine("Voortgang", done + " van " + BORDER_STEPS.size(),
                        done == BORDER_STEPS.size() ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                        bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                Component.text("Klik een stap aan als je hem gedaan hebt.", NamedTextColor.GRAY));
        ActionButton back = backButton("Terug naar het hoofdmenu.", () -> openMenu(admin));
        EditedScreen es = applyMenuEdits(admin, "Border checklist", "Border checklist", body, buttons, 300, 1);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, es.title))
                        .body(List.of(DialogBody.plainMessage(es.body)))
                        .build())
                .type(DialogType.multiAction(es.buttons, back, es.columns)));
        admin.showDialog(dialog);
    }

    /** Every command of the checklist as a clickable line in the chat. */
    private void sendChecklistCommands(Player admin) {
        admin.sendMessage(Component.text("Klik een command om hem in je chatbalk te zetten. Bij de flags laat je de "
                + "waarde weg, dan wordt de flag verwijderd.", NamedTextColor.GRAY));
        for (CheckStep step : BORDER_STEPS) {
            admin.sendMessage(Component.text(step.command, NamedTextColor.AQUA)
                    .clickEvent(ClickEvent.suggestCommand(step.command))
                    .hoverEvent(HoverEvent.showText(Component.text("Klik om in je chatbalk te zetten", NamedTextColor.GRAY))));
        }
        admin.sendMessage(Component.text("Open de checklist weer met /lowkey checklist.", NamedTextColor.GRAY));
    }

    private void tryDropBorders(Player admin) {
        if (bordersDropped) {
            admin.sendMessage(Component.text("De grenzen zijn al open.", NamedTextColor.YELLOW));
            openChecklistMenu(admin);
            return;
        }
        List<String> missing = missingChecklistSteps();
        if (!missing.isEmpty()) {
            admin.sendMessage(Component.text("Nog niet klaar: " + String.join(", ", missing) + ".", NamedTextColor.RED));
            openChecklistMenu(admin);
            return;
        }
        confirm(admin,
                "Grenzen droppen?",
                "Alle stappen staan op klaar. Noord en Zuid krijgen weer een gedeelde chat.",
                "Je kunt de grenzen later weer dichtzetten.",
                Component.text("Ja, droppen", NamedTextColor.GREEN),
                () -> {
                    setBordersDropped(true);
                    openChecklistMenu(admin);
                },
                () -> openChecklistMenu(admin));
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

    // ------------------------------------------------------------------ the season timeline (7 to 21 nov)

    private static final int SEASON_FIRST_DAY = 7;
    private static final int SEASON_LAST_DAY = 21;

    /** What happens on a day of the season, or null when there is nothing to do. */
    private String seasonEvent(int day) {
        switch (day) {
            case 7:
                return "Server open: launch en team verdeling";
            case 11:
                return "Verliezerseiland start";
            case 13:
                return "IJsmuur smelt";
            case 14:
                return "Nether open";
            case 15:
                return "End open";
            case 18:
                return "Finale Verliezerseiland";
            case 21:
                return "Hoofdfinale";
            default:
                return null;
        }
    }

    private boolean seasonDone(int day) {
        switch (day) {
            case 7:
                return launched;
            case 11:
                return losersOpen || losersEnded;
            case 13:
                return bordersDropped;
            case 14:
                return netherOpen;
            case 15:
                return endOpen;
            case 18:
                return losersEnded;
            case 21:
                return finaleState == FinaleState.ENDED;
            default:
                return false;
        }
    }

    /** One vertical list, the dates on the left. Click a day for the things to do on that day. */
    private void openTimelineMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        LocalDate today = LocalDate.now(LOWKEY_ZONE);
        List<ActionButton> buttons = new ArrayList<>();
        for (int day = SEASON_FIRST_DAY; day <= SEASON_LAST_DAY; day++) {
            final int d = day;
            LocalDate date = LocalDate.of(2026, 11, day);
            String event = seasonEvent(day);
            NamedTextColor dateColor = today.equals(date) ? NamedTextColor.GREEN
                    : (today.isAfter(date) ? NamedTextColor.DARK_GRAY : NamedTextColor.WHITE);
            TextComponent.Builder label = Component.text();
            label.append(Component.text((day < 10 ? " " : "") + day + " nov", dateColor, TextDecoration.BOLD));
            label.append(Component.text("   "));
            if (event == null) {
                label.append(Component.text("niets te doen", NamedTextColor.DARK_GRAY));
            } else {
                boolean done = seasonDone(day);
                label.append(Component.text(done ? "[x] " : "[ ] ", done ? NamedTextColor.GREEN : NamedTextColor.GRAY));
                label.append(Component.text(event, done ? NamedTextColor.GREEN : NamedTextColor.WHITE));
            }
            buttons.add(button(label.build(), event == null ? "Niets te doen op deze dag."
                    : "Open de acties voor deze dag.", 300, () -> openDayMenu(admin, d)));
        }
        buttons.add(button(iconLabel(M_REVIVE, "Testen en herstellen", NamedTextColor.AQUA),
                "Reviven, teams en launch resetten, main wereld sluiten.", 300, () -> openRepairMenu(admin)));

        Component body = joinLines(
                Component.text("Seizoen 1, 7 tot 21 november", NamedTextColor.GRAY),
                statusLine("Vandaag", today.format(DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("nl-BE"))),
                        NamedTextColor.WHITE));
        ActionButton back = backButton("Terug naar het hoofdmenu.", () -> openMenu(admin));
        EditedScreen es = applyMenuEdits(admin, "Tijdlijn", "Tijdlijn", body, buttons, 300, 1);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, es.title))
                        .body(List.of(DialogBody.plainMessage(es.body)))
                        .build())
                .type(DialogType.multiAction(es.buttons, back, es.columns)));
        admin.showDialog(dialog);
    }

    /** The things you can do on one day of the timeline. */
    private void openDayMenu(Player admin, int day) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openDayMenu(admin, day);
        List<ActionButton> buttons = new ArrayList<>();
        Component body;
        String event = seasonEvent(day);

        if (day == 7) {
            if (!launched) {
                buttons.add(button(iconLabel(M_LAUNCH, "Server launchen", NamedTextColor.GREEN),
                        "Noord gaat naar de spawn van Noord, Zuid naar die van Zuid.", 150,
                        () -> openLaunchConfirm(admin, again)));
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
            buttons.add(button(iconLabel(M_COUNTDOWN, "Countdown", NamedTextColor.WHITE),
                    "Hologram met de aftelling naar de start.", 150, () -> openCountdownMenu(admin)));
            buttons.add(button(iconLabel(M_TEAM, "Team event", NamedTextColor.WHITE),
                    "Verdeel iedereen over Noord en Zuid met een show.", 150, () -> openEventMenu(admin)));
            buttons.add(button(iconLabel(M_PLAYERS, "Teamverdeling", NamedTextColor.WHITE),
                    "Alle spelers met hun team.", 150, () -> openTeamListMenu(admin)));
            int withTeam = 0;
            for (Player p : getServer().getOnlinePlayers()) {
                if (getSide(p) != Side.NONE) {
                    withTeam++;
                }
            }
            body = joinLines(
                    statusLine("Launch", launched ? "gelanceerd" : "nog niet",
                            launched ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                    statusLine("Spawn Noord", spawnNoord != null ? "ingesteld" : "niet ingesteld",
                            spawnNoord != null ? NamedTextColor.GREEN : NamedTextColor.RED),
                    statusLine("Spawn Zuid", spawnZuid != null ? "ingesteld" : "niet ingesteld",
                            spawnZuid != null ? NamedTextColor.GREEN : NamedTextColor.RED),
                    statusLine("Online met een team", withTeam + " van " + getServer().getOnlinePlayers().size(),
                            NamedTextColor.WHITE),
                    Component.text("Spelers zonder team blijven in de lobby.", NamedTextColor.GRAY));
        } else if (day == 11) {
            if (!losersOpen) {
                buttons.add(button(iconLabel(M_REVIVE, "Eiland openen", NamedTextColor.GREEN),
                        "Uitgeschakelde spelers kunnen de rode ster gebruiken.", 150, () -> confirm(admin,
                                "Verliezerseiland openen?",
                                "Alle uitgeschakelde spelers krijgen een melding en kunnen de rode ster gebruiken.",
                                "Je kunt het later weer sluiten.",
                                Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    openLosers(admin);
                                    again.run();
                                },
                                again)));
            } else {
                buttons.add(button(iconLabel(M_REVIVE, "Eiland sluiten", NamedTextColor.RED),
                        "Nieuwe spelers kunnen er niet meer heen.", 150, () -> {
                            closeLosers(admin);
                            again.run();
                        }));
            }
            buttons.add(button(iconLabel(M_REVIVE, "Verliezerseiland menu", NamedTextColor.WHITE),
                    "Alles over het eiland.", 150, () -> openLosersMenu(admin)));
            body = joinLines(
                    statusLine("Eiland", losersCreated ? "aangemaakt" : "nog niet",
                            losersCreated ? NamedTextColor.GREEN : NamedTextColor.RED),
                    statusLine("Status", losersEnded ? "afgelopen" : (losersOpen ? "open" : "dicht"),
                            losersOpen ? NamedTextColor.GREEN : NamedTextColor.YELLOW));
        } else if (day == 13) {
            buttons.add(button(iconLabel(M_BORDERS, "Checklist", NamedTextColor.GREEN),
                    "Alles wat je moet doen voor je de grenzen dropt.", 150, () -> openChecklistMenu(admin)));
            if (bordersDropped) {
                buttons.add(button(iconLabel(M_BORDERS, "Grenzen dicht", NamedTextColor.RED),
                        "Noord en Zuid zien daarna weer alleen hun eigen chat.", 150, () -> {
                            setBordersDropped(false);
                            again.run();
                        }));
            }
            body = joinLines(
                    statusLine("Grenzen", bordersDropped ? "open" : "dicht",
                            bordersDropped ? NamedTextColor.GREEN : NamedTextColor.RED),
                    statusLine("Checklist", (BORDER_STEPS.size() - missingChecklistSteps().size()) + " van "
                            + BORDER_STEPS.size(), missingChecklistSteps().isEmpty() ? NamedTextColor.GREEN
                            : NamedTextColor.YELLOW));
        } else if (day == 14) {
            buttons.add(netherToggle(admin, again));
            body = statusLine("Nether", netherOpen ? "open" : "dicht",
                    netherOpen ? NamedTextColor.GREEN : NamedTextColor.RED);
        } else if (day == 15) {
            buttons.add(endToggle(admin, again));
            body = statusLine("End", endOpen ? "open" : "dicht", endOpen ? NamedTextColor.GREEN : NamedTextColor.RED);
        } else if (day == 18) {
            buttons.add(button(iconLabel(M_REVIVE, "Iedereen die leeft terug", NamedTextColor.GREEN),
                    "Iedereen die nog leeft op het eiland gaat terug. Daarna sluit het eiland en verdwijnt de rode ster.",
                    150, () -> openLosersFinishConfirm(admin)));
            buttons.add(button(iconLabel(M_REVIVE, "Speler terugsturen", NamedTextColor.WHITE),
                    "Kies zelf wie weer levend terug in de main wereld gaat.", 150, () -> openLosersPick(admin)));
            buttons.add(button(iconLabel(M_REVIVE, "Verliezerseiland menu", NamedTextColor.WHITE),
                    "Alles over het eiland.", 150, () -> openLosersMenu(admin)));
            body = joinLines(
                    statusLine("Status", losersEnded ? "afgelopen" : (losersOpen ? "open" : "dicht"),
                            losersEnded ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                    statusLine("Nog levend op het eiland", String.valueOf(aliveOnIsland().size()), NamedTextColor.WHITE));
        } else if (day == 21) {
            buttons.add(button(iconLabel(M_BORDERS, "Finale", NamedTextColor.LIGHT_PURPLE),
                    "Start, pauzeer en stel de krimpende border in.", 150, () -> openFinaleMenu(admin)));
            buttons.add(button(iconLabel(M_REVIVE, "Winnaarsshow testen", NamedTextColor.AQUA),
                    "Title en vuurwerk rond jou, zonder iets van de finale te raken.", 150,
                    () -> winnerShow(admin, true)));
            body = finaleStatusBody();
        } else {
            body = Component.text("Niets te doen.", NamedTextColor.GRAY);
        }
        String title = (day) + " nov" + (event == null ? "" : ": " + event);
        showMenu(admin, title, body, buttons, backButton("Terug naar de tijdlijn.", () -> openTimelineMenu(admin)));
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
            return button(iconLabel(M_NETHER, "End sluiten", NamedTextColor.RED),
                    "Sluit het End weer. Wie erin zit kan er nog uit.", 150, () -> {
                        setEnd(false);
                        again.run();
                    });
        }
        return button(iconLabel(M_NETHER, "End openen", NamedTextColor.GREEN),
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
        finaleCloseDims = s.getBoolean("finale.close-dimensions", true);
        finaleAutoTie = s.getBoolean("finale.auto-tie", true);
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
                            finaleCloseDims ? "De Nether en het End gaan automatisch dicht."
                                    : "De Nether en het End blijven zoals ze zijn.",
                            Component.text("Ja, starten", NamedTextColor.GREEN),
                            () -> {
                                finaleStart(admin);
                                again.run();
                            },
                            again)));
        } else if (finaleState == FinaleState.RUNNING) {
            buttons.add(button(iconLabel(M_BORDERS, "Pauzeren", NamedTextColor.YELLOW),
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
            buttons.add(button(iconLabel(M_BACK, "Stoppen en border terugzetten", NamedTextColor.RED),
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
        buttons.add(button(iconLabel(M_BORDERS, "Doel en grootte", NamedTextColor.WHITE),
                "Het midden en de eindgrootte van de border.", 150, () -> openFinaleTargetDialog(admin)));
        buttons.add(button(iconLabel(M_BORDERS, "Doel is mijn positie", NamedTextColor.WHITE),
                "Zet het midden van de eindborder op waar jij staat.", 150, () -> {
                    finaleTargetSet = true;
                    finaleTargetX = admin.getLocation().getBlockX() + 0.5;
                    finaleTargetZ = admin.getLocation().getBlockZ() + 0.5;
                    saveState();
                    admin.sendMessage(Component.text("Het doel staat op x " + finaleNum(finaleTargetX) + ", z "
                            + finaleNum(finaleTargetZ) + ".", NamedTextColor.GREEN));
                    again.run();
                }));
        buttons.add(button(iconLabel(M_COUNTDOWN, "Tijden en schade", NamedTextColor.WHITE),
                "Duur, rondes, pauzes en de schade buiten de border.", 150, () -> openFinaleTimeDialog(admin)));
        buttons.add(stateButton("Nether en End sluiten bij start", finaleCloseDims, "aan", "uit",
                "Sluit de Nether en het End automatisch als de finale start.", () -> {
                    finaleCloseDims = !finaleCloseDims;
                    saveState();
                    again.run();
                }));
        buttons.add(stateButton("Gelijkspel automatisch oplossen", finaleAutoTie, "aan", "uit",
                "Aan: de laatste die doodging wint. Uit: jij kiest de winnaar in een menu.", () -> {
                    finaleAutoTie = !finaleAutoTie;
                    saveState();
                    again.run();
                }));
        buttons.add(button(iconLabel(M_REVIVE, "Winnaarsshow testen", NamedTextColor.AQUA),
                "Title en vuurwerk rond jou. Raakt niemand anders en niets van de finale.", 150, () -> {
                    winnerShow(admin, true);
                }));

        showMenu(admin, "Finale", finaleStatusBody(), buttons,
                backButton("Terug naar de tijdlijn.", () -> openTimelineMenu(admin)));
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
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, "Finale doel"))
                        .body(List.of(DialogBody.plainMessage(Component.text(
                                "Waar de border naartoe krimpt. Geldt voor de volgende start.", NamedTextColor.GRAY))))
                        .inputs(List.of(x, z, size))
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_BORDERS, "Opslaan", NamedTextColor.GREEN),
                                Component.text("Sla het doel op."), 150, save),
                        backButton("Terug naar de finale.", () -> openFinaleMenu(admin)))));
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

    // ------------------------------------------------------------------ menu editor (change the menus in the game)

    /** Everything an admin changed in the menus, saved in menu-edits.yml. */
    private YamlConfiguration menuEdits = new YamlConfiguration();
    /** Admins who are in edit mode right now (not saved, so it is never left on after a restart). */
    private final Set<UUID> menuEditMode = new HashSet<>();
    /** What every menu button does, so edit mode can run the original action. */
    private final Map<DialogAction, Runnable> buttonActions = Collections.synchronizedMap(new WeakHashMap<>());
    /** The order the buttons of a screen were last shown in (for moving them up and down). */
    private final Map<String, List<String>> menuLastKeys = new HashMap<>();
    private final Random menuRandom = new Random();

    /** The icons the resource pack has: {id, character, name}. New pictures need a new resource pack. */
    private static final String[][] MENU_ICONS = {
            {"LAUNCH", M_LAUNCH, "Launch"}, {"SERVER", M_SERVER, "Server"}, {"WORLD", M_WORLD, "Wereld"},
            {"PLAYERS", M_PLAYERS, "Spelers"}, {"MESSAGES", M_MESSAGES, "Berichten"}, {"NETHER", M_NETHER, "Nether"},
            {"BORDERS", M_BORDERS, "Grenzen"}, {"LOBBY", M_LOBBY, "Lobby"}, {"SPAWN_N", M_SPAWN_N, "Spawn noord"},
            {"SPAWN_Z", M_SPAWN_Z, "Spawn zuid"}, {"MAIN", M_MAIN, "Main"}, {"TEAM", M_TEAM, "Team"},
            {"CREW", M_CREW, "Crew"}, {"GRACE", M_GRACE, "Grace"}, {"REVIVE", M_REVIVE, "Revive"},
            {"SAY", M_SAY, "Say"}, {"DONATE", M_DONATE, "Donatie"}, {"BACK", M_BACK, "Terug"},
            {"COUNTDOWN", M_COUNTDOWN, "Klok"}, {"MACE", M_MACE, "Mace"}
    };

    private static final String[][] MENU_COLORS = {
            {"white", "Wit"}, {"gray", "Grijs"}, {"dark_gray", "Donkergrijs"}, {"red", "Rood"}, {"dark_red", "Donkerrood"},
            {"gold", "Goud"}, {"yellow", "Geel"}, {"green", "Groen"}, {"dark_green", "Donkergroen"}, {"aqua", "Aqua"},
            {"dark_aqua", "Donker aqua"}, {"blue", "Blauw"}, {"light_purple", "Roze"}, {"dark_purple", "Paars"}
    };

    private static final String[][] MENU_CONDITIONS = {
            {"server-open", "Server is open"}, {"server-closed", "Server is dicht"},
            {"main-open", "Main wereld is open"}, {"main-closed", "Main wereld is dicht"},
            {"nether-open", "Nether is open"}, {"nether-closed", "Nether is dicht"},
            {"end-open", "End is open"}, {"end-closed", "End is dicht"},
            {"launched", "Gelanceerd"}, {"not-launched", "Nog niet gelanceerd"},
            {"borders-dropped", "Grenzen zijn open"}, {"borders-closed", "Grenzen zijn dicht"},
            {"finale-running", "Finale is bezig"}, {"finale-idle", "Finale is niet bezig"},
            {"owner", "Alleen de owner"}
    };

    /** One button of a screen while the screen is being put together. */
    private static final class MenuEntry {
        String key;
        ActionButton button;
        Runnable run;
        boolean custom;
        boolean hidden;
        boolean conditionMet = true;
        String baseText = "";
        ConfigurationSection sec;
    }

    /** A screen after the edits: what to show. */
    private static final class EditedScreen {
        String title;
        Component body;
        final List<ActionButton> buttons = new ArrayList<>();
        int columns;
    }

    private File menuEditsFile() {
        return new File(getDataFolder(), "menu-edits.yml");
    }

    private void loadMenuEdits() {
        menuEdits = YamlConfiguration.loadConfiguration(menuEditsFile());
    }

    private void saveMenuEdits() {
        try {
            getDataFolder().mkdirs();
            menuEdits.save(menuEditsFile());
        } catch (IOException e) {
            getLogger().warning("Kon menu-edits.yml niet opslaan: " + e.getMessage());
        }
    }

    private boolean menuAdvanced(Player p) {
        return menuEdits.getBoolean("advanced." + p.getUniqueId(), false);
    }

    private void setMenuAdvanced(Player p, boolean on) {
        menuEdits.set("advanced." + p.getUniqueId(), on ? Boolean.TRUE : null);
        saveMenuEdits();
    }

    private static String ykey(String s) {
        return s.replace('.', '_').trim();
    }

    private static boolean isGlyph(char ch) {
        return ch >= '\uE000' && ch <= '\uF8FF';
    }

    private static String stripGlyphs(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!isGlyph(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString().replaceAll("\\s+", " ").trim();
    }

    /** A stable name for a button: its text without icon, [state] marks and capitals. */
    private static String buttonKey(ActionButton b) {
        String t = stripGlyphs(PLAIN.serialize(b.label()));
        t = t.replaceAll("\\[[^\\]]*\\]", "").replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
        return t.isEmpty() ? "knop" : t;
    }

    private Component menuText(String raw, boolean miniMessage) {
        if (raw == null) {
            return Component.empty();
        }
        if (miniMessage) {
            return MiniMessage.miniMessage().deserialize(raw);
        }
        return Component.text(raw);
    }

    private static String iconChar(String id) {
        if (id == null || id.isEmpty() || id.equals("NONE")) {
            return null;
        }
        for (String[] icon : MENU_ICONS) {
            if (icon[0].equals(id)) {
                return icon[1];
            }
        }
        return null;
    }

    private static TextColor menuColor(String id) {
        switch (id == null ? "" : id) {
            case "white":
                return NamedTextColor.WHITE;
            case "gray":
                return NamedTextColor.GRAY;
            case "dark_gray":
                return NamedTextColor.DARK_GRAY;
            case "red":
                return NamedTextColor.RED;
            case "dark_red":
                return NamedTextColor.DARK_RED;
            case "gold":
                return NamedTextColor.GOLD;
            case "yellow":
                return NamedTextColor.YELLOW;
            case "green":
                return NamedTextColor.GREEN;
            case "dark_green":
                return NamedTextColor.DARK_GREEN;
            case "aqua":
                return NamedTextColor.AQUA;
            case "dark_aqua":
                return NamedTextColor.DARK_AQUA;
            case "blue":
                return NamedTextColor.BLUE;
            case "light_purple":
                return NamedTextColor.LIGHT_PURPLE;
            case "dark_purple":
                return NamedTextColor.DARK_PURPLE;
            default:
                return null;
        }
    }

    private boolean menuCondition(Player admin, String cond) {
        switch (cond) {
            case "server-open":
                return !serverClosed;
            case "server-closed":
                return serverClosed;
            case "main-open":
                return mainOpen;
            case "main-closed":
                return !mainOpen;
            case "nether-open":
                return netherOpen;
            case "nether-closed":
                return !netherOpen;
            case "end-open":
                return endOpen;
            case "end-closed":
                return !endOpen;
            case "launched":
                return launched;
            case "not-launched":
                return !launched;
            case "borders-dropped":
                return bordersDropped;
            case "borders-closed":
                return !bordersDropped;
            case "finale-running":
                return finaleState == FinaleState.RUNNING || finaleState == FinaleState.PAUSED;
            case "finale-idle":
                return finaleState == FinaleState.IDLE;
            case "owner":
                return isOwner(admin);
            default:
                return true;
        }
    }

    // ---- where screens can be opened from

    private Map<String, Consumer<Player>> screenOpeners() {
        Map<String, Consumer<Player>> m = new LinkedHashMap<>();
        m.put("Beheer", this::openMenu);
        m.put("LowkeySMP Beheer", this::openMenu);
        m.put("Snelle acties", this::openQuickMenu);
        m.put("Tijdlijn", this::openTimelineMenu);
        m.put("Spelers", this::openPlayersMenu);
        m.put("Wereld en server", this::openWorldMenu);
        m.put("Wereld", this::openWorldMenu);
        m.put("Server", this::openServerMenu);
        m.put("Berichten", this::openMessagesMenu);
        m.put("Team event", this::openEventMenu);
        m.put("Verliezerseiland", this::openLosersMenu);
        m.put("Mace", p -> mace.openMenu(p));
        m.put("Border checklist", this::openChecklistMenu);
        m.put("Finale", this::openFinaleMenu);
        m.put("Testen en herstellen", this::openRepairMenu);
        m.put("Nether en grenzen", this::openAccessMenu);
        m.put("Lobby", this::openLobbyMenu);
        m.put("Spawnpunten", this::openSpawnMenu);
        m.put("Countdown", this::openCountdownMenu);
        return m;
    }

    /** Opens a built-in screen by its name, or a screen made in the editor ("custom:id"). */
    private void openScreenByKey(Player admin, String key) {
        if (key.startsWith("custom:")) {
            openCustomScreen(admin, key.substring("custom:".length()));
            return;
        }
        Consumer<Player> opener = screenOpeners().get(key);
        if (opener == null) {
            openMenu(admin);
        } else {
            opener.accept(admin);
        }
    }

    private void reopenScreen(Player admin, String screenKey) {
        openScreenByKey(admin, screenKey);
    }

    // ---- putting a screen together with all the edits

    private Runnable customActionRunnable(Player admin, String screenKey, ConfigurationSection c, String label) {
        final String command = c.getString("command", "");
        final String opens = c.getString("opens", "");
        final boolean asConsole = "console".equals(c.getString("run-as", "player"));
        final boolean confirmFirst = c.getBoolean("confirm", false);
        Runnable core = () -> {
            if (!opens.isEmpty()) {
                openScreenByKey(admin, opens);
                return;
            }
            if (command.isEmpty()) {
                admin.sendMessage(Component.text("Deze knop doet nog niets. Geef hem een actie in de menu-editor.",
                        NamedTextColor.YELLOW));
                return;
            }
            String cmd = command.startsWith("/") ? command.substring(1) : command;
            cmd = cmd.replace("%player%", admin.getName());
            if (asConsole) {
                getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
            } else {
                admin.performCommand(cmd);
            }
        };
        if (!confirmFirst) {
            return core;
        }
        return () -> confirm(admin, "Zeker weten?", label,
                opens.isEmpty() ? "Voert uit: /" + (command.startsWith("/") ? command.substring(1) : command)
                        : "Opent: " + opens,
                Component.text("Ja, uitvoeren", NamedTextColor.GREEN), core, () -> reopenScreen(admin, screenKey));
    }

    /** Applies label, icon, colour and tooltip edits to the label of a button. */
    private Component styledLabel(Component orig, ConfigurationSection s) {
        if (s == null) {
            return orig;
        }
        String labelRaw = s.getString("label", "");
        String icon = s.getString("icon", "");
        TextColor color = menuColor(s.getString("color", ""));
        if (labelRaw.isEmpty() && icon.isEmpty() && color == null) {
            return orig;
        }
        boolean mm = s.getBoolean("mm", false);
        List<Component> kids = orig.children();
        boolean structured = kids.size() == 3 && !PLAIN.serialize(kids.get(0)).isEmpty()
                && isGlyph(PLAIN.serialize(kids.get(0)).charAt(0));
        Component glyphPart = null;
        Component textPart;
        if (structured) {
            glyphPart = kids.get(0);
            textPart = kids.get(2);
        } else {
            textPart = orig;
        }
        if (!labelRaw.isEmpty()) {
            Component t = menuText(labelRaw, mm);
            TextColor base = color != null ? color : textPart.color();
            textPart = base == null ? t : Component.text().color(base).append(t).build();
        } else if (color != null) {
            textPart = textPart.color(color);
        }
        if (!icon.isEmpty()) {
            String ch = iconChar(icon);
            glyphPart = ch == null ? null : glyph(ch);
        }
        if (glyphPart == null) {
            return textPart;
        }
        return Component.text().append(glyphPart).append(Component.space()).append(textPart).build();
    }

    private ActionButton withWidth(ActionButton b, int width) {
        return ActionButton.create(b.label(), b.tooltip(), width, b.action());
    }

    private static int widthForColumns(int columns) {
        if (columns <= 1) {
            return 380;
        }
        if (columns == 2) {
            return 190;
        }
        return columns == 3 ? 125 : 95;
    }

    /**
     * Takes the buttons a screen was built with and returns what to show: the edits applied (hidden buttons gone,
     * texts, icons and order changed, own buttons added). In edit mode every button opens its editor instead.
     */
    private EditedScreen applyMenuEdits(Player admin, String screenKey, String title, Component body,
                                        List<ActionButton> original, int defaultWidth, int defaultColumns) {
        final boolean editing = menuEditMode.contains(admin.getUniqueId());
        final String sk = ykey(screenKey);
        ConfigurationSection screen = menuEdits.getConfigurationSection("screens." + sk);
        EditedScreen result = new EditedScreen();
        result.title = title;
        result.body = body;
        result.columns = defaultColumns;
        if (screen == null && !editing) {
            result.buttons.addAll(original);
            return result;
        }
        if (screen != null) {
            boolean smm = screen.getBoolean("mm", false);
            String t = screen.getString("title", "");
            if (!t.isEmpty()) {
                result.title = t;
            }
            String replace = screen.getString("body", "");
            if (!replace.isEmpty()) {
                result.body = menuText(replace, smm);
            }
            String extra = screen.getString("body-extra", "");
            if (!extra.isEmpty()) {
                result.body = Component.text().append(result.body).append(Component.newline())
                        .append(menuText(extra, smm)).build();
            }
            int cols = screen.getInt("columns", 0);
            if (cols >= 1 && cols <= 4) {
                result.columns = cols;
            }
        }
        ConfigurationSection btnSec = screen == null ? null : screen.getConfigurationSection("buttons");
        ConfigurationSection customSec = screen == null ? null : screen.getConfigurationSection("custom");

        Map<String, MenuEntry> entries = new LinkedHashMap<>();
        for (ActionButton b : original) {
            String base = buttonKey(b);
            String key = base;
            int n = 2;
            while (entries.containsKey(key)) {
                key = base + " #" + n;
                n++;
            }
            MenuEntry e = new MenuEntry();
            e.key = key;
            e.baseText = stripGlyphs(PLAIN.serialize(b.label()));
            e.run = b.action() == null ? null : buttonActions.get(b.action());
            ConfigurationSection s = btnSec == null ? null : btnSec.getConfigurationSection(ykey(key));
            e.sec = s;
            Component label = styledLabel(b.label(), s);
            Component tip = b.tooltip();
            int width = b.width();
            e.button = b;
            if (s != null) {
                boolean mm = s.getBoolean("mm", false);
                String tipRaw = s.getString("tooltip", "");
                if (!tipRaw.isEmpty()) {
                    tip = menuText(tipRaw, mm);
                }
                int w = s.getInt("width", 0);
                if (w >= 50 && w <= 400) {
                    width = w;
                }
                if (!s.getString("command", "").isEmpty() || !s.getString("opens", "").isEmpty()) {
                    e.run = customActionRunnable(admin, screenKey, s, e.baseText);
                    ActionButton tmp = button(label, "", width, e.run);
                    e.button = ActionButton.create(tmp.label(), tip, tmp.width(), tmp.action());
                } else {
                    e.button = ActionButton.create(label, tip, width, b.action());
                }
                e.hidden = s.getBoolean("hidden", false);
                String cond = s.getString("show-if", "");
                e.conditionMet = cond.isEmpty() || menuCondition(admin, cond);
            }
            entries.put(key, e);
        }
        if (customSec != null) {
            for (String id : customSec.getKeys(false)) {
                ConfigurationSection c = customSec.getConfigurationSection(id);
                if (c == null) {
                    continue;
                }
                MenuEntry e = new MenuEntry();
                e.key = "@" + id;
                e.custom = true;
                e.sec = c;
                e.baseText = c.getString("label", "Knop");
                e.run = customActionRunnable(admin, screenKey, c, e.baseText);
                boolean mm = c.getBoolean("mm", false);
                Component text = menuText(e.baseText, mm);
                TextColor color = menuColor(c.getString("color", ""));
                text = Component.text().color(color == null ? NamedTextColor.WHITE : color).append(text).build();
                String ch = iconChar(c.getString("icon", ""));
                Component label = ch == null ? text
                        : Component.text().append(glyph(ch)).append(Component.space()).append(text).build();
                int width = c.getInt("width", defaultWidth);
                if (width < 50 || width > 400) {
                    width = defaultWidth;
                }
                ActionButton tmp = button(label, "", width, e.run);
                e.button = ActionButton.create(tmp.label(), menuText(c.getString("tooltip", ""), mm), tmp.width(),
                        tmp.action());
                e.hidden = c.getBoolean("hidden", false);
                String cond = c.getString("show-if", "");
                e.conditionMet = cond.isEmpty() || menuCondition(admin, cond);
                entries.put(e.key, e);
            }
        }

        List<String> keys = new ArrayList<>(entries.keySet());
        List<String> order = screen == null ? new ArrayList<>() : screen.getStringList("order");
        if (!order.isEmpty()) {
            List<String> sorted = new ArrayList<>();
            for (String k : order) {
                if (entries.containsKey(k) && !sorted.contains(k)) {
                    sorted.add(k);
                }
            }
            for (String k : keys) {
                if (!sorted.contains(k)) {
                    sorted.add(k);
                }
            }
            keys = sorted;
        }
        List<String> shown = new ArrayList<>();
        for (String k : keys) {
            MenuEntry e = entries.get(k);
            boolean visible = !e.hidden && e.conditionMet;
            if (!editing) {
                if (visible) {
                    result.buttons.add(e.button);
                }
                continue;
            }
            shown.add(k);
            result.buttons.add(editEntryButton(admin, screenKey, e));
        }
        if (editing) {
            menuLastKeys.put(admin.getUniqueId() + "|" + sk, shown);
            result.title = "Bewerken: " + result.title;
            result.buttons.add(button(iconLabel(M_MESSAGES, "Dit scherm bewerken", NamedTextColor.YELLOW),
                    "Titel, tekst en nieuwe knoppen.", defaultWidth, () -> openScreenEditor(admin, screenKey)));
            result.buttons.add(button(iconLabel(M_BACK, "Bewerkmodus uitzetten", NamedTextColor.RED),
                    "Klik weer normaal op de knoppen.", defaultWidth, () -> {
                        menuEditMode.remove(admin.getUniqueId());
                        reopenScreen(admin, screenKey);
                    }));
        }
        return result;
    }

    /** In edit mode a button opens its editor. The description of the original stays visible. */
    private ActionButton editEntryButton(Player admin, String screenKey, MenuEntry e) {
        String note = e.hidden ? "(verborgen) " : (!e.conditionMet ? "(voorwaarde) " : "");
        Component marker = Component.text("\u00BB " + note, note.isEmpty() ? NamedTextColor.YELLOW : NamedTextColor.DARK_GRAY);
        Component label = Component.text().append(marker).append(e.button.label()).build();
        final String key = e.key;
        final boolean custom = e.custom;
        final Runnable run = e.run;
        final String baseText = e.baseText;
        ActionButton tmp = button(label, "", e.button.width(),
                () -> openButtonEditor(admin, screenKey, key, custom, run, baseText));
        return ActionButton.create(tmp.label(), e.button.tooltip(), tmp.width(), tmp.action());
    }

    // ---- helpers for the editor dialogs

    private ConfigurationSection menuScreenSection(String screenKey, boolean create) {
        String path = "screens." + ykey(screenKey);
        ConfigurationSection s = menuEdits.getConfigurationSection(path);
        if (s == null && create) {
            s = menuEdits.createSection(path);
        }
        return s;
    }

    private String menuBtnPath(String screenKey, String bk) {
        return bk.startsWith("@")
                ? "screens." + ykey(screenKey) + ".custom." + ykey(bk.substring(1))
                : "screens." + ykey(screenKey) + ".buttons." + ykey(bk);
    }

    private ConfigurationSection menuBtnSection(String screenKey, String bk, boolean create) {
        String path = menuBtnPath(screenKey, bk);
        ConfigurationSection s = menuEdits.getConfigurationSection(path);
        if (s == null && create) {
            s = menuEdits.createSection(path);
        }
        return s;
    }

    private void menuBtnSet(String screenKey, String bk, String field, Object value) {
        ConfigurationSection s = menuBtnSection(screenKey, bk, value != null);
        if (s == null) {
            return;
        }
        s.set(field, value);
        saveMenuEdits();
    }

    private static String menuValue(String raw) {
        return raw == null ? "" : raw.trim();
    }

    private void showEditorDialog(Player admin, String title, Component body, List<ActionButton> buttons,
                                  ActionButton exit) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, title))
                        .body(List.of(DialogBody.plainMessage(body)))
                        .build())
                .type(DialogType.multiAction(buttons, exit, 2)));
        admin.showDialog(dialog);
    }

    /** One dialog with text fields. fields: {key, label, initial, maxLength}. */
    private void openTextDialog(Player admin, String title, String hint, List<String[]> fields,
                                Consumer<Map<String, String>> onSave, Runnable back) {
        List<DialogInput> inputs = new ArrayList<>();
        for (String[] f : fields) {
            inputs.add(DialogInput.text(f[0], Component.text(f[1]))
                    .width(300)
                    .initial(f[2] == null ? "" : f[2])
                    .maxLength(Integer.parseInt(f[3]))
                    .build());
        }
        DialogAction save = DialogAction.customClick((view, audience) -> {
            if (!(audience instanceof Player)) {
                return;
            }
            Player clicker = (Player) audience;
            if (!isAdmin(clicker)) {
                return;
            }
            final Map<String, String> values = new HashMap<>();
            for (String[] f : fields) {
                String v = view.getText(f[0]);
                values.put(f[0], v == null ? "" : v);
            }
            getServer().getScheduler().runTask(this, () -> onSave.accept(values));
        }, CLICK_OPTIONS);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dlgTitle(admin, title))
                        .body(List.of(DialogBody.plainMessage(Component.text(hint, NamedTextColor.GRAY))))
                        .inputs(inputs)
                        .build())
                .type(DialogType.confirmation(
                        ActionButton.create(iconLabel(M_BORDERS, "Opslaan", NamedTextColor.GREEN),
                                Component.text("Sla dit op."), 150, save),
                        backButton("Terug zonder opslaan.", back))));
        admin.showDialog(dialog);
    }

    // ---- the button editor

    private void openButtonEditor(Player admin, String sk, String bk, boolean custom, Runnable run, String baseText) {
        if (!admin.isOnline()) {
            return;
        }
        final boolean adv = menuAdvanced(admin);
        ConfigurationSection s = menuBtnSection(sk, bk, false);
        final boolean hidden = s != null && s.getBoolean("hidden", false);
        final Runnable again = () -> openButtonEditor(admin, sk, bk, custom, run, baseText);
        List<ActionButton> buttons = new ArrayList<>();
        if (run != null) {
            buttons.add(button(iconLabel(M_LAUNCH, "Uitvoeren", NamedTextColor.GREEN),
                    "Doe wat deze knop normaal doet.", 190, run));
        }
        buttons.add(button(iconLabel(M_MESSAGES, "Tekst aanpassen", NamedTextColor.WHITE),
                "De naam en de uitleg van deze knop.", 190, () -> openButtonTextDialog(admin, sk, bk, baseText, again)));
        buttons.add(button(iconLabel(M_MAIN, "Icoon kiezen", NamedTextColor.WHITE),
                "Kies een icoon uit de resourcepack.", 190, () -> openIconPicker(admin, sk, bk, again)));
        buttons.add(button(iconLabel(M_TEAM, "Kleur kiezen", NamedTextColor.WHITE),
                "De kleur van de tekst.", 190, () -> openColorPicker(admin, sk, bk, again)));
        buttons.add(button(iconLabel(M_BACK, "Omhoog", NamedTextColor.WHITE),
                "Zet de knop een plek hoger.", 190, () -> {
                    menuMove(admin, sk, bk, -1);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_BACK, "Omlaag", NamedTextColor.WHITE),
                "Zet de knop een plek lager.", 190, () -> {
                    menuMove(admin, sk, bk, 1);
                    again.run();
                }));
        buttons.add(stateButton("Verborgen", hidden, "ja", "nee",
                "Een verborgen knop is alleen in de bewerkmodus te zien.", () -> {
                    menuBtnSet(sk, bk, "hidden", hidden ? null : Boolean.TRUE);
                    again.run();
                }));
        if (custom || adv) {
            buttons.add(button(iconLabel(M_LAUNCH, "Actie", NamedTextColor.AQUA),
                    "Wat de knop doet: een scherm openen of een commando uitvoeren.", 190,
                    () -> openActionEditor(admin, sk, bk, custom, baseText, again)));
        }
        if (adv) {
            buttons.add(button(iconLabel(M_SERVER, "Breedte", NamedTextColor.AQUA),
                    "De breedte van de knop (50 tot 400).", 190, () -> openWidthDialog(admin, sk, bk, again)));
            buttons.add(button(iconLabel(M_SERVER, "Voorwaarde", NamedTextColor.AQUA),
                    "Toon de knop alleen als dit klopt, bijvoorbeeld als de server open is.", 190,
                    () -> openConditionPicker(admin, sk, bk, again)));
        }
        if (custom) {
            buttons.add(button(iconLabel(M_BACK, "Knop verwijderen", NamedTextColor.RED),
                    "Haalt deze eigen knop weg.", 190, () -> confirm(admin, "Knop verwijderen?",
                            baseText, "Deze eigen knop verdwijnt helemaal.",
                            Component.text("Ja, verwijderen", NamedTextColor.RED), () -> {
                                menuEdits.set(menuBtnPath(sk, bk), null);
                                ConfigurationSection sc = menuScreenSection(sk, false);
                                if (sc != null) {
                                    List<String> order = new ArrayList<>(sc.getStringList("order"));
                                    order.remove(bk);
                                    sc.set("order", order);
                                }
                                saveMenuEdits();
                                reopenScreen(admin, sk);
                            }, again)));
        } else if (s != null) {
            buttons.add(button(iconLabel(M_BACK, "Terugzetten", NamedTextColor.RED),
                    "Haalt alle aanpassingen van deze knop weg.", 190, () -> {
                        menuEdits.set(menuBtnPath(sk, bk), null);
                        saveMenuEdits();
                        again.run();
                    }));
        }
        Component body = joinLines(
                statusLine("Knop", baseText, NamedTextColor.WHITE),
                statusLine("Soort", custom ? "eigen knop" : "ingebouwde knop", NamedTextColor.GRAY),
                statusLine("Modus", adv ? "geavanceerd" : "simpel", adv ? NamedTextColor.AQUA : NamedTextColor.GREEN));
        showEditorDialog(admin, "Knop bewerken", body, buttons,
                backButton("Terug naar het scherm.", () -> reopenScreen(admin, sk)));
    }

    private void openButtonTextDialog(Player admin, String sk, String bk, String baseText, Runnable back) {
        boolean adv = menuAdvanced(admin);
        ConfigurationSection s = menuBtnSection(sk, bk, false);
        String curLabel = s != null && !s.getString("label", "").isEmpty() ? s.getString("label", "") : baseText;
        String curTip = s == null ? "" : s.getString("tooltip", "");
        List<String[]> fields = new ArrayList<>();
        fields.add(new String[] {"label", "Naam van de knop", curLabel, "100"});
        fields.add(new String[] {"tooltip", "Uitleg", curTip, "200"});
        String hint = adv ? "Je mag MiniMessage gebruiken: <red>, <bold>, <gradient:#c084fc:#f472b6>, <br>."
                : "Gewone tekst. Laat een veld leeg om het origineel terug te zetten.";
        openTextDialog(admin, "Tekst aanpassen", hint, fields, values -> {
            String label = menuValue(values.get("label"));
            String tip = menuValue(values.get("tooltip"));
            menuBtnSet(sk, bk, "label", label.isEmpty() || label.equals(baseText) ? null : label);
            menuBtnSet(sk, bk, "tooltip", tip.isEmpty() ? null : tip);
            menuBtnSet(sk, bk, "mm", adv ? Boolean.TRUE : null);
            back.run();
        }, back);
    }

    private void openIconPicker(Player admin, String sk, String bk, Runnable back) {
        List<ActionButton> list = new ArrayList<>();
        list.add(button(Component.text("Standaard"), "Het oorspronkelijke icoon.", 150, () -> {
            menuBtnSet(sk, bk, "icon", null);
            back.run();
        }));
        list.add(button(Component.text("Geen icoon"), "Alleen tekst.", 150, () -> {
            menuBtnSet(sk, bk, "icon", "NONE");
            back.run();
        }));
        for (String[] icon : MENU_ICONS) {
            final String id = icon[0];
            list.add(button(iconLabel(icon[1], icon[2], NamedTextColor.WHITE), "Gebruik dit icoon.", 150, () -> {
                menuBtnSet(sk, bk, "icon", id);
                back.run();
            }));
        }
        showEditorDialog(admin, "Icoon kiezen",
                Component.text("Dit zijn de iconen in de resourcepack. Nieuwe plaatjes vragen een nieuwe resourcepack.",
                        NamedTextColor.GRAY),
                list, backButton("Terug.", back));
    }

    private void openColorPicker(Player admin, String sk, String bk, Runnable back) {
        List<ActionButton> list = new ArrayList<>();
        list.add(button(Component.text("Standaard"), "De oorspronkelijke kleur.", 150, () -> {
            menuBtnSet(sk, bk, "color", null);
            back.run();
        }));
        for (String[] color : MENU_COLORS) {
            final String id = color[0];
            TextColor c = menuColor(id);
            list.add(button(Component.text(color[1], c == null ? NamedTextColor.WHITE : c),
                    "Gebruik deze kleur.", 150, () -> {
                        menuBtnSet(sk, bk, "color", id);
                        back.run();
                    }));
        }
        showEditorDialog(admin, "Kleur kiezen", Component.text("Kies de kleur van de tekst.", NamedTextColor.GRAY),
                list, backButton("Terug.", back));
    }

    private void menuMove(Player admin, String screenKey, String bk, int dir) {
        String lastKey = admin.getUniqueId() + "|" + ykey(screenKey);
        List<String> seq = menuLastKeys.get(lastKey);
        if (seq == null) {
            admin.sendMessage(Component.text("Open het scherm eerst opnieuw in de bewerkmodus.", NamedTextColor.YELLOW));
            return;
        }
        seq = new ArrayList<>(seq);
        int i = seq.indexOf(bk);
        int j = i + dir;
        if (i < 0 || j < 0 || j >= seq.size()) {
            admin.sendMessage(Component.text("Daar kan de knop niet verder naartoe.", NamedTextColor.GRAY));
            return;
        }
        Collections.swap(seq, i, j);
        ConfigurationSection sc = menuScreenSection(screenKey, true);
        sc.set("order", seq);
        saveMenuEdits();
        menuLastKeys.put(lastKey, seq);
    }

    // ---- advanced options of a button

    private void openWidthDialog(Player admin, String sk, String bk, Runnable back) {
        ConfigurationSection s = menuBtnSection(sk, bk, false);
        String cur = s == null || s.getInt("width", 0) == 0 ? "" : String.valueOf(s.getInt("width", 0));
        List<String[]> fields = new ArrayList<>();
        fields.add(new String[] {"breedte", "Breedte (50 tot 400, leeg = standaard)", cur, "3"});
        openTextDialog(admin, "Breedte", "In het nieuwe ontwerp zijn de grote knoppen altijd even breed. Dit werkt in "
                + "het klassieke ontwerp en in schermen met meer kolommen.", fields, values -> {
                    String raw = menuValue(values.get("breedte"));
                    if (raw.isEmpty()) {
                        menuBtnSet(sk, bk, "width", null);
                    } else {
                        try {
                            int w = Integer.parseInt(raw);
                            if (w < 50 || w > 400) {
                                admin.sendMessage(Component.text("De breedte moet tussen 50 en 400 liggen.",
                                        NamedTextColor.RED));
                            } else {
                                menuBtnSet(sk, bk, "width", w);
                            }
                        } catch (NumberFormatException e) {
                            admin.sendMessage(Component.text("Geef een getal op.", NamedTextColor.RED));
                        }
                    }
                    back.run();
                }, back);
    }

    private void openConditionPicker(Player admin, String sk, String bk, Runnable back) {
        List<ActionButton> list = new ArrayList<>();
        list.add(button(Component.text("Altijd tonen"), "Geen voorwaarde.", 190, () -> {
            menuBtnSet(sk, bk, "show-if", null);
            back.run();
        }));
        for (String[] cond : MENU_CONDITIONS) {
            final String id = cond[0];
            list.add(button(Component.text(cond[1]), "Toon de knop alleen als dit klopt.", 190, () -> {
                menuBtnSet(sk, bk, "show-if", id);
                back.run();
            }));
        }
        showEditorDialog(admin, "Voorwaarde",
                Component.text("In de bewerkmodus zie je de knop altijd, ook als de voorwaarde niet klopt.",
                        NamedTextColor.GRAY), list, backButton("Terug.", back));
    }

    private void openActionEditor(Player admin, String sk, String bk, boolean custom, String baseText, Runnable back) {
        final boolean adv = menuAdvanced(admin);
        ConfigurationSection s = menuBtnSection(sk, bk, false);
        String command = s == null ? "" : s.getString("command", "");
        String opens = s == null ? "" : s.getString("opens", "");
        boolean asConsole = s != null && "console".equals(s.getString("run-as", "player"));
        boolean confirmFirst = s != null && s.getBoolean("confirm", false);
        final Runnable again = () -> openActionEditor(admin, sk, bk, custom, baseText, back);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_WORLD, "Opent een scherm", NamedTextColor.WHITE),
                "Kies welk scherm de knop opent.", 190, () -> openScreenPicker(admin, sk, bk, again)));
        buttons.add(button(iconLabel(M_SAY, "Voert een commando uit", NamedTextColor.WHITE),
                "Typ een commando, bijvoorbeeld /lowkey donate.", 190, () -> {
                    List<String[]> fields = new ArrayList<>();
                    fields.add(new String[] {"commando", "Commando (%player% wordt jouw naam)", command, "200"});
                    openTextDialog(admin, "Commando", "Het commando start met of zonder /.", fields, values -> {
                        String cmd = menuValue(values.get("commando"));
                        menuBtnSet(sk, bk, "command", cmd.isEmpty() ? null : cmd);
                        if (!cmd.isEmpty()) {
                            menuBtnSet(sk, bk, "opens", null);
                        }
                        again.run();
                    }, again);
                }));
        if (adv) {
            buttons.add(stateButton("Uitvoeren als console", asConsole, "ja", "nee",
                    "Ja: de console voert het commando uit. Nee: jij.", () -> {
                        menuBtnSet(sk, bk, "run-as", asConsole ? null : "console");
                        again.run();
                    }));
            buttons.add(stateButton("Eerst bevestigen", confirmFirst, "ja", "nee",
                    "Vraag eerst of je het zeker weet.", () -> {
                        menuBtnSet(sk, bk, "confirm", confirmFirst ? null : Boolean.TRUE);
                        again.run();
                    }));
        }
        if (!custom) {
            buttons.add(button(iconLabel(M_BACK, "Originele actie terug", NamedTextColor.RED),
                    "De knop doet weer wat hij oorspronkelijk deed.", 190, () -> {
                        menuBtnSet(sk, bk, "command", null);
                        menuBtnSet(sk, bk, "opens", null);
                        menuBtnSet(sk, bk, "run-as", null);
                        menuBtnSet(sk, bk, "confirm", null);
                        again.run();
                    }));
        }
        String now = !opens.isEmpty() ? "opent " + opens : (!command.isEmpty() ? "/" + command.replaceFirst("^/", "")
                : (custom ? "nog niets" : "de oorspronkelijke actie"));
        showEditorDialog(admin, "Actie", joinLines(statusLine("Knop", baseText, NamedTextColor.WHITE),
                statusLine("Nu", now, NamedTextColor.WHITE)), buttons, backButton("Terug.", back));
    }

    private void openScreenPicker(Player admin, String sk, String bk, Runnable back) {
        List<ActionButton> list = new ArrayList<>();
        for (String name : screenOpeners().keySet()) {
            if (name.equals("LowkeySMP Beheer") || name.equals("Wereld")) {
                continue;
            }
            list.add(button(Component.text(name), "Open dit scherm.", 190, () -> {
                menuBtnSet(sk, bk, "opens", name);
                menuBtnSet(sk, bk, "command", null);
                back.run();
            }));
        }
        ConfigurationSection screens = menuEdits.getConfigurationSection("screens");
        if (screens != null) {
            for (String key : screens.getKeys(false)) {
                ConfigurationSection sc = screens.getConfigurationSection(key);
                if (sc == null || !sc.getBoolean("custom-screen", false)) {
                    continue;
                }
                final String target = "custom:" + sc.getString("id", "");
                list.add(button(Component.text(sc.getString("title", "Nieuw scherm"), NamedTextColor.LIGHT_PURPLE),
                        "Open dit eigen scherm.", 190, () -> {
                            menuBtnSet(sk, bk, "opens", target);
                            menuBtnSet(sk, bk, "command", null);
                            back.run();
                        }));
            }
        }
        showEditorDialog(admin, "Scherm kiezen", Component.text("Welk scherm moet deze knop openen?",
                NamedTextColor.GRAY), list, backButton("Terug.", back));
    }

    // ---- the screen editor

    private void openScreenEditor(Player admin, String sk) {
        if (!admin.isOnline()) {
            return;
        }
        final boolean adv = menuAdvanced(admin);
        final boolean customScreen = sk.startsWith("custom:");
        final Runnable again = () -> openScreenEditor(admin, sk);
        ConfigurationSection sc = menuScreenSection(sk, false);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(button(iconLabel(M_MESSAGES, "Titel en tekst", NamedTextColor.WHITE),
                "Pas de titel en de tekst bovenaan aan.", 190, () -> openScreenTextDialog(admin, sk, again)));
        buttons.add(button(iconLabel(M_LAUNCH, "Knop toevoegen", NamedTextColor.GREEN),
                "Maak een eigen knop op dit scherm.", 190, () -> {
                    List<String[]> fields = new ArrayList<>();
                    fields.add(new String[] {"label", "Naam van de knop", "", "100"});
                    fields.add(new String[] {"tooltip", "Uitleg", "", "200"});
                    openTextDialog(admin, "Knop toevoegen", adv ? "MiniMessage mag." : "Gewone tekst.", fields,
                            values -> {
                                String label = menuValue(values.get("label"));
                                if (label.isEmpty()) {
                                    admin.sendMessage(Component.text("Geef de knop een naam.", NamedTextColor.RED));
                                    again.run();
                                    return;
                                }
                                String id = Long.toString(System.currentTimeMillis(), 36)
                                        + Integer.toString(menuRandom.nextInt(1296), 36);
                                String bk = "@" + id;
                                menuBtnSet(sk, bk, "label", label);
                                String tip = menuValue(values.get("tooltip"));
                                menuBtnSet(sk, bk, "tooltip", tip.isEmpty() ? null : tip);
                                menuBtnSet(sk, bk, "mm", adv ? Boolean.TRUE : null);
                                openButtonEditor(admin, sk, bk, true, null, label);
                            }, again);
                }));
        buttons.add(stateButton("Geavanceerde modus", adv, "aan", "uit",
                "Aan: breedte, voorwaarden, MiniMessage, eigen schermen en meer.", () -> {
                    setMenuAdvanced(admin, !adv);
                    again.run();
                }));
        if (adv) {
            buttons.add(button(iconLabel(M_WORLD, "Nieuw scherm", NamedTextColor.GREEN),
                    "Maak een helemaal eigen scherm. Open het met een knop.", 190, () -> {
                        List<String[]> fields = new ArrayList<>();
                        fields.add(new String[] {"title", "Titel van het scherm", "", "60"});
                        openTextDialog(admin, "Nieuw scherm", "Daarna voeg je er knoppen aan toe.", fields, values -> {
                            String title = menuValue(values.get("title"));
                            if (title.isEmpty()) {
                                admin.sendMessage(Component.text("Geef het scherm een titel.", NamedTextColor.RED));
                                again.run();
                                return;
                            }
                            String id = Long.toString(System.currentTimeMillis(), 36)
                                    + Integer.toString(menuRandom.nextInt(1296), 36);
                            ConfigurationSection created = menuScreenSection("custom:" + id, true);
                            created.set("custom-screen", true);
                            created.set("id", id);
                            created.set("title", title);
                            saveMenuEdits();
                            menuEditMode.add(admin.getUniqueId());
                            openCustomScreen(admin, id);
                        }, again);
                    }));
            buttons.add(button(iconLabel(M_WORLD, "Mijn schermen", NamedTextColor.AQUA),
                    "Alle schermen die je zelf gemaakt hebt.", 190, () -> openMyScreens(admin, again)));
            buttons.add(button(iconLabel(M_BACK, "Alles terugzetten", NamedTextColor.RED),
                    "Wist alle aanpassingen aan alle menu's.", 190, () -> confirm(admin, "Alles terugzetten?",
                            "Alle aanpassingen en eigen schermen verdwijnen.",
                            "De menu's zien er weer uit zoals ze in de code staan.",
                            Component.text("Ja, alles wissen", NamedTextColor.RED), () -> {
                                menuEdits.set("screens", null);
                                saveMenuEdits();
                                admin.sendMessage(Component.text("Alle menu-aanpassingen zijn gewist.",
                                        NamedTextColor.GREEN));
                                openMenu(admin);
                            }, again)));
        }
        if (customScreen) {
            buttons.add(button(iconLabel(M_BACK, "Scherm verwijderen", NamedTextColor.RED),
                    "Haalt dit eigen scherm helemaal weg.", 190, () -> confirm(admin, "Scherm verwijderen?",
                            "Dit eigen scherm met al zijn knoppen verdwijnt.",
                            "Knoppen die dit scherm openen doen daarna niets meer.",
                            Component.text("Ja, verwijderen", NamedTextColor.RED), () -> {
                                menuEdits.set("screens." + ykey(sk), null);
                                saveMenuEdits();
                                openMenu(admin);
                            }, again)));
        } else if (sc != null) {
            buttons.add(button(iconLabel(M_BACK, "Dit scherm terugzetten", NamedTextColor.RED),
                    "Wist alle aanpassingen van dit scherm.", 190, () -> confirm(admin, "Scherm terugzetten?",
                            "Alle aanpassingen aan dit scherm verdwijnen.",
                            "Het scherm ziet er weer uit zoals in de code.",
                            Component.text("Ja, terugzetten", NamedTextColor.RED), () -> {
                                menuEdits.set("screens." + ykey(sk), null);
                                saveMenuEdits();
                                reopenScreen(admin, sk);
                            }, again)));
        }
        Component body = joinLines(
                statusLine("Scherm", sk, NamedTextColor.WHITE),
                statusLine("Modus", adv ? "geavanceerd" : "simpel", adv ? NamedTextColor.AQUA : NamedTextColor.GREEN));
        showEditorDialog(admin, "Scherm bewerken", body, buttons,
                backButton("Terug naar het scherm.", () -> reopenScreen(admin, sk)));
    }

    private void openScreenTextDialog(Player admin, String sk, Runnable back) {
        final boolean adv = menuAdvanced(admin);
        ConfigurationSection sc = menuScreenSection(sk, false);
        List<String[]> fields = new ArrayList<>();
        fields.add(new String[] {"title", "Titel (leeg = standaard)", sc == null ? "" : sc.getString("title", ""), "60"});
        fields.add(new String[] {"extra", "Extra tekst onder de status", sc == null ? "" : sc.getString("body-extra", ""),
                "300"});
        if (adv) {
            fields.add(new String[] {"body", "Statustekst vervangen (leeg = live status)",
                    sc == null ? "" : sc.getString("body", ""), "300"});
            fields.add(new String[] {"columns", "Kolommen (1 tot 4, leeg = standaard)",
                    sc == null || sc.getInt("columns", 0) == 0 ? "" : String.valueOf(sc.getInt("columns", 0)), "1"});
        }
        openTextDialog(admin, "Titel en tekst", adv ? "MiniMessage mag in de tekstvelden, <br> is een nieuwe regel."
                : "Gewone tekst.", fields, values -> {
                    ConfigurationSection target = menuScreenSection(sk, true);
                    String title = menuValue(values.get("title"));
                    String extra = menuValue(values.get("extra"));
                    target.set("title", title.isEmpty() ? null : title);
                    target.set("body-extra", extra.isEmpty() ? null : extra);
                    if (adv) {
                        String body = menuValue(values.get("body"));
                        target.set("body", body.isEmpty() ? null : body);
                        String cols = menuValue(values.get("columns"));
                        if (cols.isEmpty()) {
                            target.set("columns", null);
                        } else {
                            try {
                                int c = Integer.parseInt(cols);
                                target.set("columns", c >= 1 && c <= 4 ? c : null);
                            } catch (NumberFormatException e) {
                                admin.sendMessage(Component.text("Kolommen moet een getal van 1 tot 4 zijn.",
                                        NamedTextColor.RED));
                            }
                        }
                    }
                    target.set("mm", adv ? Boolean.TRUE : null);
                    saveMenuEdits();
                    back.run();
                }, back);
    }

    private void openMyScreens(Player admin, Runnable back) {
        List<ActionButton> list = new ArrayList<>();
        ConfigurationSection screens = menuEdits.getConfigurationSection("screens");
        if (screens != null) {
            for (String key : screens.getKeys(false)) {
                ConfigurationSection sc = screens.getConfigurationSection(key);
                if (sc == null || !sc.getBoolean("custom-screen", false)) {
                    continue;
                }
                final String id = sc.getString("id", "");
                list.add(button(Component.text(sc.getString("title", "Nieuw scherm"), NamedTextColor.LIGHT_PURPLE),
                        "Open dit scherm.", 190, () -> {
                            menuEditMode.add(admin.getUniqueId());
                            openCustomScreen(admin, id);
                        }));
            }
        }
        showEditorDialog(admin, "Mijn schermen", Component.text(list.isEmpty() ? "Je hebt nog geen eigen schermen."
                : "Kies een scherm om te openen.", NamedTextColor.GRAY), list, backButton("Terug.", back));
    }

    /** A screen made in the editor: a title, a text and only the buttons put on it. */
    private void openCustomScreen(Player admin, String id) {
        if (!admin.isOnline()) {
            return;
        }
        String key = "custom:" + id;
        ConfigurationSection sc = menuScreenSection(key, false);
        if (sc == null || !sc.getBoolean("custom-screen", false)) {
            admin.sendMessage(Component.text("Dit scherm bestaat niet meer.", NamedTextColor.RED));
            openMenu(admin);
            return;
        }
        showMenuKeyed(admin, key, sc.getString("title", "Nieuw scherm"), Component.empty(), new ArrayList<>(),
                backButton("Terug naar Home.", () -> openMenu(admin)));
    }

    /** Switch edit mode on or off for this admin (command /lowkey menu edit). */
    private void toggleMenuEditMode(Player admin) {
        if (menuEditMode.remove(admin.getUniqueId())) {
            admin.sendMessage(Component.text("Bewerkmodus staat uit.", NamedTextColor.GREEN));
        } else {
            menuEditMode.add(admin.getUniqueId());
            admin.sendMessage(Component.text("Bewerkmodus staat aan. Klik op een knop om hem aan te passen. "
                    + "Onderaan elk scherm kun je het scherm zelf bewerken.", NamedTextColor.GREEN));
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
        checklistDone.clear();
        saveChecklist();
        saveState();
        sender.sendMessage(Component.text("Alle schakelaars staan weer als aan het begin: niet gelanceerd, main wereld "
                + "open, Nether, End en grenzen dicht, Verliezerseiland dicht, checklist leeg. Niemand is verplaatst en "
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
        buttons.add(button(iconLabel(M_REVIVE, "Terug uit Verliezerseiland", NamedTextColor.WHITE),
                "Kies een speler die uit is en zet hem terug in de main wereld.", 150, () -> openLosersPick(admin)));
        buttons.add(button(iconLabel(M_REVIVE, "Alle teams resetten", NamedTextColor.RED),
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
        buttons.add(button(iconLabel(M_MACE, "Mace cooldown wissen", NamedTextColor.WHITE),
                "Haalt de lopende cooldown van de mace weg, handig na een test op een bot.", 150, () -> {
                    mace.resetCooldown(admin);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_BACK, "Alles terugzetten", NamedTextColor.RED),
                "Zet launch, Nether, End, grenzen, Verliezerseiland en checklist terug naar het begin.", 150,
                () -> confirm(admin,
                        "Alle schakelaars terugzetten?",
                        "Launch, Nether, End, grenzen, Verliezerseiland en de checklist gaan terug naar het begin.",
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
        showMenu(admin, "Testen en herstellen", body, buttons, backButton("Terug naar de tijdlijn.",
                () -> openTimelineMenu(admin)));
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
            buttons.add(button(iconLabel(M_REVIVE, "Naar het eiland", NamedTextColor.WHITE),
                    "Teleporteert jou naar het Verliezerseiland.", 150, () -> teleportAdminToLosers(admin)));
            buttons.add(button(iconLabel(M_REVIVE, "Spawn zetten", NamedTextColor.WHITE),
                    "Zet het spawnpunt op jouw plek. Je moet op het eiland staan.", 150, () -> {
                        setLosersSpawn(admin);
                        openLosersMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_REVIVE, "Eiland aanmaken", NamedTextColor.GREEN),
                    "Maakt een normale survival wereld met altijd onweer en nacht.", 150, () -> {
                        createLosers(admin, null);
                        openLosersMenu(admin);
                    }));
        }
        if (losersOpen) {
            buttons.add(button(iconLabel(M_REVIVE, "Eiland sluiten", NamedTextColor.RED),
                    "Nieuwe spelers kunnen er niet meer heen. Wie er is blijft er.", 150, () -> {
                        closeLosers(admin);
                        openLosersMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_REVIVE, "Eiland openen", NamedTextColor.GREEN),
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
        buttons.add(button(iconLabel(M_REVIVE, "Iedereen die leeft terug", NamedTextColor.GREEN),
                "Iedereen die nog leeft op het eiland gaat terug. Daarna sluit het eiland en verdwijnt de rode ster.", 150,
                () -> openLosersFinishConfirm(admin)));
        if (losersCreated || losersWorld() != null) {
            buttons.add(button(iconLabel(M_REVIVE, "Eiland verwijderen", NamedTextColor.RED),
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
        showMenu(admin, "Verliezerseiland", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
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
    private static final double EV_COL_X = 3.9;           // distance of each team column from the middle
    private static final double EV_TOP_Y = -0.2;          // height of the first row (0 = camera height)
    private static final double EV_CELL_W = 1.6;          // width of one head + name cell
    private static final double EV_ROW_MAX = 1.05;        // biggest distance between two rows
    private static final double EV_ROWS_H = 3.9;          // all rows together stay inside this height
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
        Title title = Title.title(eventLogos(), subtitle,
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
            buttons.add(button(iconLabel(M_TEAM, "Event afbreken", NamedTextColor.RED),
                    "Stopt het event meteen en zet iedereen weer vrij.", 150, () -> {
                        stopEvent(admin);
                        openEventMenu(admin);
                    }));
        } else {
            buttons.add(button(iconLabel(M_TEAM, "Event starten", NamedTextColor.GREEN),
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
            buttons.add(button(iconLabel(M_TEAM, "Testen", NamedTextColor.WHITE),
                    "Oefenen met nep-spelers. Er verandert geen enkel team.", 150, () -> openEventTestDialog(admin)));
        }
        buttons.add(button(iconLabel(M_SPAWN_N, "Camera zetten", NamedTextColor.WHITE),
                "Zet de camera op jouw ogen en kijkrichting. Je moet in de lobby staan.", 150, () -> {
                    setEventCameraHere(admin);
                    openEventMenu(admin);
                }));
        buttons.add(button(iconLabel(M_PLAYERS, "Teamverdeling", NamedTextColor.WHITE),
                "Alle spelers met hun team.", 150, () -> openTeamListMenu(admin)));
        buttons.add(button(iconLabel(M_REVIVE, "Alle teams resetten", NamedTextColor.RED),
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
        showMenu(admin, "Team event", body, buttons, backButton("Terug naar het hoofdmenu.", () -> openMenu(admin)));
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

    // ------------------------------------------------------------------ menu designs (new / classic)

    /** per admin: "new" (default) or "classic", saved in menu.yml */
    private final Map<UUID, String> menuDesigns = new HashMap<>();

    private File menuFile() {
        return new File(getDataFolder(), "menu.yml");
    }

    private void loadMenuDesigns() {
        menuDesigns.clear();
        YamlConfiguration y = YamlConfiguration.loadConfiguration(menuFile());
        ConfigurationSection section = y.getConfigurationSection("design");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            try {
                menuDesigns.put(UUID.fromString(key), section.getString(key, "new"));
            } catch (IllegalArgumentException ignored) {
                // not a uuid, skip it
            }
        }
    }

    private void saveMenuDesigns() {
        YamlConfiguration y = new YamlConfiguration();
        for (Map.Entry<UUID, String> entry : menuDesigns.entrySet()) {
            y.set("design." + entry.getKey(), entry.getValue());
        }
        try {
            getDataFolder().mkdirs();
            y.save(menuFile());
        } catch (IOException e) {
            getLogger().warning("Kon menu.yml niet opslaan: " + e.getMessage());
        }
    }

    /** True when this admin uses the new menu design (the default). */
    boolean newDesign(Player player) {
        return !"classic".equals(menuDesigns.get(player.getUniqueId()));
    }

    private void setMenuDesign(Player player, boolean useNew) {
        menuDesigns.put(player.getUniqueId(), useNew ? "new" : "classic");
        saveMenuDesigns();
    }

    /** "LOWKEY » Title" with a gradient on the name. */
    private static Component themedTitle(String title) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize("<gradient:#c084fc:#f472b6><bold>LOWKEY</bold></gradient> <dark_gray>\u00BB</dark_gray> <white>"
                + mm.escapeTags(title) + "</white>");
    }

    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    /** Title for any dialog: the new gradient breadcrumb in the new design, the old purple title in the classic one. */
    Component dlgTitle(Player admin, String text) {
        return newDesign(admin) ? themedTitle(text) : menuTitle(text);
    }

    /** A big icon on the left with text beside it (the header of every screen). */
    private DialogBody headerCard(Material icon, Component text) {
        return DialogBody.item(new ItemStack(icon))
                .description(DialogBody.plainMessage(text, 300))
                .showDecorations(false)
                .showTooltip(false)
                .width(48)
                .height(48)
                .build();
    }

    /** A small icon with one line of text beside it (a status row). */
    private DialogBody miniCard(Material icon, Component text) {
        return DialogBody.item(new ItemStack(icon))
                .description(DialogBody.plainMessage(text, 320))
                .showDecorations(false)
                .showTooltip(false)
                .width(20)
                .height(20)
                .build();
    }

    private DialogBody statusCard(Material icon, String name, boolean on, String onText, String offText) {
        return miniCard(icon, Component.text()
                .append(Component.text(name, NamedTextColor.WHITE, TextDecoration.BOLD))
                .append(Component.text("   "))
                .append(Component.text((on ? onText : offText).toUpperCase(Locale.ROOT),
                        on ? NamedTextColor.GREEN : NamedTextColor.RED, TextDecoration.BOLD))
                .build());
    }

    /** The icon of a screen, shown big at the top of it. */
    private static Material screenIcon(String title) {
        switch (title) {
            case "Spelers":
                return Material.PLAYER_HEAD;
            case "Wereld en server":
                return Material.GRASS_BLOCK;
            case "Snelle acties":
                return Material.LEVER;
            case "Mace":
                return Material.MACE;
            case "Berichten":
                return Material.WRITABLE_BOOK;
            case "Team event":
                return Material.FIREWORK_ROCKET;
            case "Verliezerseiland":
                return Material.WITHER_SKELETON_SKULL;
            case "Nether en grenzen":
                return Material.NETHERRACK;
            case "Lobby":
                return Material.NETHER_STAR;
            case "Spawnpunten":
                return Material.COMPASS;
            case "Countdown":
                return Material.CLOCK;
            case "Testen en herstellen":
                return Material.TOTEM_OF_UNDYING;
            case "Titel testen":
                return Material.OAK_SIGN;
            case "Finale":
                return Material.DRAGON_HEAD;
            case "Gelijkspel":
                return Material.TOTEM_OF_UNDYING;
            default:
                return Material.BOOK;
        }
    }

    /** A wide button: the title, and the explanation in grey right behind it. */
    private ActionButton cardButton(ActionButton b) {
        Component tip = b.tooltip();
        String label = PLAIN.serialize(b.label());
        String desc = tip == null ? "" : PLAIN.serialize(tip);
        int room = 56 - label.length();
        if (room < 12) {
            desc = "";
        } else if (desc.length() > room) {
            desc = desc.substring(0, room - 3).trim() + "...";
        }
        Component label2 = Component.text()
                .append(b.label())
                .append(desc.isEmpty() ? Component.empty() : Component.text("   " + desc, NamedTextColor.GRAY))
                .build();
        return ActionButton.create(label2, b.tooltip(), 380, b.action());
    }

    /**
     * New design for every screen: a big icon with the status text beside it, then wide buttons with the explanation
     * on each button, and a Home button at the end. Compact screens use a grid of switches instead.
     */
    private void showThemedMenu(Player admin, String title, Component body, List<ActionButton> buttons,
                                ActionButton exit, boolean withHome, boolean compact) {
        showThemedMenuKeyed(admin, title, title, body, buttons, exit, withHome, compact);
    }

    private void showThemedMenuKeyed(Player admin, String key, String title, Component body,
                                     List<ActionButton> buttons, ActionButton exit, boolean withHome,
                                     boolean compact) {
        EditedScreen es = applyMenuEdits(admin, key, title, body, buttons, compact ? 190 : 150, compact ? 2 : 1);
        boolean cards = !compact && es.columns == 1;
        int homeWidth = cards ? 380 : (compact ? 190 : widthForColumns(es.columns));
        List<ActionButton> all = new ArrayList<>();
        for (ActionButton b : es.buttons) {
            if (cards) {
                all.add(cardButton(b));
            } else if (!compact) {
                all.add(withWidth(b, widthForColumns(es.columns)));
            } else {
                all.add(b);
            }
        }
        if (withHome) {
            ActionButton home = button(iconLabel(M_WORLD, "Home", NamedTextColor.LIGHT_PURPLE),
                    "Terug naar het beginscherm.", homeWidth, () -> openMenu(admin));
            all.add(cards ? cardButton(home) : home);
        }
        List<DialogBody> bodies = new ArrayList<>();
        bodies.add(headerCard(screenIcon(key), es.body));
        final int columns = es.columns;
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(themedTitle(es.title)).body(bodies).build())
                .type(DialogType.multiAction(all, exit, columns)));
        admin.showDialog(dialog);
    }

    /** A switch that shows its state: [OPEN] Nether. Click to flip it. */
    private ActionButton stateButton(String name, boolean on, String onText, String offText, String tooltip,
                                     Runnable action) {
        Component label = Component.text()
                .append(Component.text("[" + (on ? onText : offText).toUpperCase(Locale.ROOT) + "] ",
                        on ? NamedTextColor.GREEN : NamedTextColor.RED, TextDecoration.BOLD))
                .append(Component.text(name, NamedTextColor.WHITE))
                .build();
        return button(label, tooltip, 190, action);
    }

    private static Component stateWord(String name, boolean on) {
        return Component.text(name, on ? NamedTextColor.GREEN : NamedTextColor.RED);
    }

    // ---- new design: Home

    private void openHome(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        LocalDate today = LocalDate.now(LOWKEY_ZONE);
        LocalDate first = LocalDate.of(2026, 11, SEASON_FIRST_DAY);
        LocalDate last = LocalDate.of(2026, 11, SEASON_LAST_DAY);
        String todayLine;
        if (today.isBefore(first)) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(today, first);
            todayLine = "Nog " + days + (days == 1 ? " dag" : " dagen") + " tot Server Open";
        } else if (!today.isAfter(last)) {
            String event = seasonEvent(today.getDayOfMonth());
            todayLine = "Vandaag: " + (event == null ? "niets te doen" : event);
        } else {
            todayLine = "Seizoen 1 is afgelopen";
        }
        int noord = 0;
        int zuid = 0;
        int geen = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            Side side = getSide(p);
            if (side == Side.NOORD) {
                noord++;
            } else if (side == Side.ZUID) {
                zuid++;
            } else {
                geen++;
            }
        }
        Component people = Component.text()
                .append(Component.text("Online ", NamedTextColor.GRAY))
                .append(Component.text(String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE))
                .append(Component.text("   Noord ", TextColor.color(Side.NOORD.rgb)))
                .append(Component.text(String.valueOf(noord), NamedTextColor.WHITE))
                .append(Component.text("   Zuid ", TextColor.color(Side.ZUID.rgb)))
                .append(Component.text(String.valueOf(zuid), NamedTextColor.WHITE))
                .append(Component.text("   Geen ", NamedTextColor.GRAY))
                .append(Component.text(String.valueOf(geen), NamedTextColor.WHITE))
                .build();
        Component headline = MiniMessage.miniMessage()
                .deserialize("<gradient:#c084fc:#f472b6><bold>LOWKEY BEHEER</bold></gradient>");
        Component header = joinLines(
                headline,
                Component.text(todayLine, NamedTextColor.WHITE, TextDecoration.BOLD),
                people,
                eventRunning ? Component.text("Team event is bezig", NamedTextColor.YELLOW) : Component.empty());

        List<DialogBody> bodies = new ArrayList<>();
        bodies.add(headerCard(Material.NETHER_STAR, header));
        bodies.add(statusCard(Material.BEACON, "Server", !serverClosed, "open", "dicht"));
        bodies.add(statusCard(Material.FIREWORK_ROCKET, "Launch", launched, "gelanceerd", "nog niet"));
        bodies.add(statusCard(Material.GRASS_BLOCK, "Main wereld", mainOpen, "open", "dicht"));
        bodies.add(statusCard(Material.NETHERRACK, "Nether", netherOpen, "open", "dicht"));
        bodies.add(statusCard(Material.END_STONE, "End", endOpen, "open", "dicht"));
        bodies.add(statusCard(Material.BLUE_ICE, "Grenzen (ijsmuur)", bordersDropped, "open", "dicht"));
        bodies.add(statusCard(Material.WITHER_SKELETON_SKULL, "Verliezerseiland", losersOpen, "open", "dicht"));

        List<ActionButton> raw = new ArrayList<>();
        raw.add(button(iconLabel(M_LAUNCH, "Snelle acties", NamedTextColor.GREEN),
                "Alle schakelaars op een plek: launch, Nether, End, grenzen en meer.", 380, () -> openQuickMenu(admin)));
        raw.add(button(iconLabel(M_COUNTDOWN, "Seizoen", NamedTextColor.WHITE),
                "Alle dagen van 7 tot 21 nov met wat je op elke dag moet doen.", 380, () -> openTimelineMenu(admin)));
        raw.add(button(iconLabel(M_PLAYERS, "Spelers", NamedTextColor.WHITE),
                "Teams, crew, grace, reviven en de teamverdeling.", 380, () -> openPlayersMenu(admin)));
        raw.add(button(iconLabel(M_WORLD, "Wereld en server", NamedTextColor.WHITE),
                "Launch, main wereld, Nether, End, grenzen, lobby en spawnpunten.", 380, () -> openWorldMenu(admin)));
        raw.add(button(iconLabel(M_TEAM, "Team event", NamedTextColor.WHITE),
                "Verdeel iedereen over Noord en Zuid met een show.", 380, () -> openEventMenu(admin)));
        raw.add(button(iconLabel(M_REVIVE, "Verliezerseiland", NamedTextColor.WHITE),
                "De aparte wereld voor iedereen die dood is.", 380, () -> openLosersMenu(admin)));
        raw.add(button(iconLabel(M_MESSAGES, "Berichten", NamedTextColor.WHITE),
                "Eigen bericht, donatiebericht en websitebericht.", 380, () -> openMessagesMenu(admin)));
        raw.add(button(iconLabel(M_MACE, "Mace", NamedTextColor.WHITE),
                "De ene mace: cooldown, houder en reset.", 380, () -> mace.openMenu(admin)));
        raw.add(button(iconLabel(M_BORDERS, "Checklist", NamedTextColor.WHITE),
                "Alles wat je moet doen voor je de grenzen dropt.", 380, () -> openChecklistMenu(admin)));
        raw.add(button(iconLabel(M_BORDERS, "Finale", NamedTextColor.LIGHT_PURPLE),
                "De krimpende border, de winnaarsshow en de instellingen.", 380, () -> openFinaleMenu(admin)));
        raw.add(button(iconLabel(M_REVIVE, "Testen en herstellen", NamedTextColor.AQUA),
                "Reviven, teams en launch resetten, alles terugzetten.", 380, () -> openRepairMenu(admin)));
        raw.add(button(iconLabel(M_MESSAGES, "Menu bewerken", NamedTextColor.YELLOW),
                "Pas alle menu's aan in de game zelf: teksten, iconen, volgorde en eigen knoppen.", 380, () -> {
                    menuEditMode.add(admin.getUniqueId());
                    openMenu(admin);
                }));
        raw.add(button(iconLabel(M_BACK, "Klassiek menu", NamedTextColor.GRAY),
                "Schakel terug naar het oude menu-ontwerp.", 380, () -> {
                    setMenuDesign(admin, false);
                    openMenu(admin);
                }));
        if (loggedIn.contains(admin.getUniqueId()) && !isOwner(admin)) {
            raw.add(button(Component.text("Uitloggen", NamedTextColor.RED),
                    "Log uit en verlies je toegang tot /lowkey en de commands.", 380, () -> logout(admin, true)));
        }
        EditedScreen es = applyMenuEdits(admin, "Beheer", "Beheer", header, raw, 380, 1);
        bodies.set(0, headerCard(Material.NETHER_STAR, es.body));
        List<ActionButton> all = new ArrayList<>();
        for (ActionButton b : es.buttons) {
            all.add(es.columns == 1 ? cardButton(b) : withWidth(b, widthForColumns(es.columns)));
        }
        ActionButton close = ActionButton.create(Component.text("Sluiten", NamedTextColor.GRAY),
                Component.text("Sluit dit menu."), widthForColumns(es.columns), null);
        final int homeColumns = es.columns;
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(themedTitle(es.title)).body(bodies).build())
                .type(DialogType.multiAction(all, close, homeColumns)));
        admin.showDialog(dialog);
    }

    // ---- new design: quick actions (every switch, with its state, on one screen)

    private void openQuickMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openQuickMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();

        buttons.add(stateButton("Server", !serverClosed, "open", "dicht",
                "Sluit de server voor iedereen behalve closed-access, of maak hem weer open.", () -> {
                    if (serverClosed) {
                        setServerClosed(false);
                        admin.sendMessage(Component.text("De server is weer open voor iedereen.", NamedTextColor.GREEN));
                        again.run();
                    } else {
                        confirm(admin,
                                "Server sluiten?",
                                "Iedereen behalve " + String.join(", ", getConfig().getStringList("closed-access"))
                                        + " wordt gekickt.",
                                "De server blijft dicht tot je hem weer opent.",
                                Component.text("Ja, sluiten", NamedTextColor.RED),
                                () -> {
                                    if (!hasClosedAccess(admin.getName())) {
                                        admin.sendMessage(Component.text("Je staat zelf niet in 'closed-access' in "
                                                + "config.yml, dan zou je jezelf buitensluiten.", NamedTextColor.RED));
                                    } else {
                                        setServerClosed(true);
                                    }
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Launch", launched, "gelanceerd", "nog niet",
                "Noord gaat naar de spawn van Noord, Zuid naar die van Zuid.", () -> {
                    if (!launched) {
                        openLaunchConfirm(admin, again);
                    } else {
                        confirm(admin,
                                "Launch resetten?",
                                "Het lobby-item zegt daarna weer dat de server nog niet open is.",
                                "Spelers in de gamewereld blijven daar.",
                                Component.text("Ja, resetten", NamedTextColor.RED),
                                () -> {
                                    resetLaunch(admin);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Main wereld", mainOpen, "open", "dicht",
                "Sluiten stuurt iedereen behalve admins naar de lobby.", () -> {
                    if (mainOpen) {
                        confirm(admin,
                                "Main wereld sluiten?",
                                "Iedereen behalve admins gaat naar de lobby en kan er niet uit tot je hem opent.",
                                "Spelers houden hun spullen en komen later terug op hun laatste plek.",
                                Component.text("Ja, sluiten", NamedTextColor.RED),
                                () -> {
                                    closeMainWorld(admin);
                                    again.run();
                                },
                                again);
                    } else {
                        openMainWorld(admin);
                        again.run();
                    }
                }));
        buttons.add(stateButton("Nether", netherOpen, "open", "dicht",
                "Opent of sluit de Nether, met titel en bericht bij openen.", () -> {
                    if (netherOpen) {
                        setNether(false);
                        again.run();
                    } else {
                        confirm(admin, "Nether openen?", "Iedereen krijgt de titel en het bericht dat de Nether open is.",
                                "Je kunt hem later weer sluiten.", Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    setNether(true);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("End", endOpen, "open", "dicht",
                "Opent of sluit het End, met titel en bericht bij openen.", () -> {
                    if (endOpen) {
                        setEnd(false);
                        again.run();
                    } else {
                        confirm(admin, "End openen?", "Iedereen krijgt de titel en het bericht dat het End open is.",
                                "Je kunt het later weer sluiten.", Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    setEnd(true);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Grenzen", bordersDropped, "open", "dicht",
                "Droppen gaat eerst langs de checklist.", () -> {
                    if (bordersDropped) {
                        setBordersDropped(false);
                        again.run();
                    } else {
                        openChecklistMenu(admin);
                    }
                }));
        buttons.add(stateButton("Verliezerseiland", losersOpen, "open", "dicht",
                "Uitgeschakelde spelers kunnen de rode ster gebruiken als het open is.", () -> {
                    if (!losersCreated) {
                        admin.sendMessage(Component.text("Maak eerst het Verliezerseiland aan (menu Verliezerseiland).",
                                NamedTextColor.RED));
                        again.run();
                    } else if (losersOpen) {
                        closeLosers(admin);
                        again.run();
                    } else {
                        confirm(admin, "Verliezerseiland openen?",
                                "Alle uitgeschakelde spelers krijgen een melding en kunnen de rode ster gebruiken.",
                                "Je kunt het later weer sluiten.", Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    openLosers(admin);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Team event", eventRunning, "bezig", "uit",
                "Een bezig event stop je hier meteen. Starten doe je in het menu Team event.", () -> {
                    if (eventRunning) {
                        stopEvent(admin);
                        again.run();
                    } else {
                        openEventMenu(admin);
                    }
                }));

        Component body = joinLines(
                Component.text("Klik op een schakelaar om hem om te zetten.", NamedTextColor.GRAY),
                Component.text("Groen is aan, rood is uit.", NamedTextColor.DARK_GRAY));
        showThemedMenu(admin, "Snelle acties", body, buttons, backButton("Terug naar Home.", () -> openMenu(admin)),
                true, true);
    }

    // ---- new design: Spelers (one screen, everything about players)

    private void openNewPlayersMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openPlayersMenu(admin);
        int noord = 0;
        int zuid = 0;
        int geen = 0;
        int out = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            Side side = getSide(p);
            if (side == Side.NOORD) {
                noord++;
            } else if (side == Side.ZUID) {
                zuid++;
            } else {
                geen++;
            }
            if (isEliminated(p) || isLosersOut(p)) {
                out++;
            }
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
        buttons.add(button(iconLabel(M_GRACE, "Grace voor iedereen", NamedTextColor.WHITE),
                "Zet de grace van alle spelers weer op de standaard tijd, ook van spelers die nu offline zijn.", 150,
                () -> {
                    resetGraceForEveryone(admin, graceMillis / 60_000L);
                    again.run();
                }));
        buttons.add(button(iconLabel(M_REVIVE, "Speler reviven", NamedTextColor.GREEN),
                "Zet een uitgeschakelde speler weer levend, waar hij ook is.", 150,
                () -> pickPlayer(admin, "Speler reviven", target -> {
                    revivePlayer(admin, target);
                    again.run();
                }, again)));
        buttons.add(button(iconLabel(M_REVIVE, "Terug uit het eiland", NamedTextColor.WHITE),
                "Kies een speler die uit is en zet hem terug in de main wereld.", 150, () -> openLosersPick(admin)));
        buttons.add(button(iconLabel(M_LOBBY, "LowkeySMP ster geven", NamedTextColor.WHITE),
                "Geeft een speler in de lobby de LowkeySMP nether star (hotbar slot 5).", 150,
                () -> pickPlayer(admin, "LowkeySMP ster geven", target -> {
                    giveLobbyItemTo(admin, target);
                    again.run();
                })));
        buttons.add(button(iconLabel(M_PLAYERS, "Teamverdeling", NamedTextColor.WHITE),
                "Alle spelers met hun team, ook wie offline is.", 150, () -> openTeamListMenu(admin)));
        buttons.add(button(iconLabel(M_REVIVE, "Alle teams resetten", NamedTextColor.RED),
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
        Component body = joinLines(
                statusLine("Online", String.valueOf(getServer().getOnlinePlayers().size()), NamedTextColor.WHITE),
                statusLine("Noord", String.valueOf(noord), TextColor.color(Side.NOORD.rgb)),
                statusLine("Zuid", String.valueOf(zuid), TextColor.color(Side.ZUID.rgb)),
                statusLine("Geen team", String.valueOf(geen), NamedTextColor.GRAY),
                statusLine("Uitgeschakeld", String.valueOf(out), out > 0 ? NamedTextColor.RED : NamedTextColor.GRAY));
        showMenu(admin, "Spelers", body, buttons, backButton("Terug naar Home.", () -> openMenu(admin)));
    }

    // ---- new design: Wereld en server (what used to be Server and Wereld, in one screen)

    private void openNewWorldMenu(Player admin) {
        if (!admin.isOnline()) {
            return;
        }
        Runnable again = () -> openWorldMenu(admin);
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(stateButton("Launch", launched, "gelanceerd", "nog niet",
                "Noord gaat naar de spawn van Noord, Zuid naar die van Zuid.", () -> {
                    if (!launched) {
                        openLaunchConfirm(admin, again);
                    } else {
                        confirm(admin, "Launch resetten?",
                                "Het lobby-item zegt daarna weer dat de server nog niet open is.",
                                "Spelers in de gamewereld blijven daar.", Component.text("Ja, resetten", NamedTextColor.RED),
                                () -> {
                                    resetLaunch(admin);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Main wereld", mainOpen, "open", "dicht",
                "Sluiten stuurt iedereen behalve admins naar de lobby.", () -> {
                    if (mainOpen) {
                        confirm(admin, "Main wereld sluiten?",
                                "Iedereen behalve admins gaat naar de lobby en kan er niet uit tot je hem opent.",
                                "Spelers houden hun spullen en komen later terug op hun laatste plek.",
                                Component.text("Ja, sluiten", NamedTextColor.RED),
                                () -> {
                                    closeMainWorld(admin);
                                    again.run();
                                },
                                again);
                    } else {
                        openMainWorld(admin);
                        again.run();
                    }
                }));
        buttons.add(stateButton("Server", !serverClosed, "open", "dicht",
                "Sluit de server voor iedereen behalve closed-access, of maak hem weer open.", () -> {
                    if (serverClosed) {
                        setServerClosed(false);
                        again.run();
                    } else {
                        confirm(admin, "Server sluiten?",
                                "Iedereen behalve " + String.join(", ", getConfig().getStringList("closed-access"))
                                        + " wordt gekickt.",
                                "De server blijft dicht tot je hem weer opent.",
                                Component.text("Ja, sluiten", NamedTextColor.RED),
                                () -> {
                                    if (!hasClosedAccess(admin.getName())) {
                                        admin.sendMessage(Component.text("Je staat zelf niet in 'closed-access' in "
                                                + "config.yml, dan zou je jezelf buitensluiten.", NamedTextColor.RED));
                                    } else {
                                        setServerClosed(true);
                                    }
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Nether", netherOpen, "open", "dicht",
                "Opent of sluit de Nether, met titel en bericht bij openen.", () -> {
                    if (netherOpen) {
                        setNether(false);
                        again.run();
                    } else {
                        confirm(admin, "Nether openen?", "Iedereen krijgt de titel en het bericht dat de Nether open is.",
                                "Je kunt hem later weer sluiten.", Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    setNether(true);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("End", endOpen, "open", "dicht",
                "Opent of sluit het End, met titel en bericht bij openen.", () -> {
                    if (endOpen) {
                        setEnd(false);
                        again.run();
                    } else {
                        confirm(admin, "End openen?", "Iedereen krijgt de titel en het bericht dat het End open is.",
                                "Je kunt het later weer sluiten.", Component.text("Ja, openen", NamedTextColor.GREEN),
                                () -> {
                                    setEnd(true);
                                    again.run();
                                },
                                again);
                    }
                }));
        buttons.add(stateButton("Grenzen", bordersDropped, "open", "dicht",
                "Droppen gaat eerst langs de checklist.", () -> {
                    if (bordersDropped) {
                        setBordersDropped(false);
                        again.run();
                    } else {
                        openChecklistMenu(admin);
                    }
                }));
        buttons.add(button(iconLabel(M_COUNTDOWN, "Countdown", NamedTextColor.WHITE),
                "Hologram met de aftelling naar de start.", 150, () -> openCountdownMenu(admin)));
        buttons.add(button(iconLabel(M_LOBBY, "Lobby", NamedTextColor.WHITE),
                "Lobby aanmaken, spawn, bescherming en verwijderen.", 150, () -> openLobbyMenu(admin)));
        buttons.add(button(iconLabel(M_SPAWN_N, "Spawnpunten", NamedTextColor.WHITE),
                "Spawn van Noord en Zuid en naar het main eiland.", 150, () -> openSpawnMenu(admin)));
        buttons.add(button(iconLabel(M_MAIN, "Naar main eiland", NamedTextColor.WHITE),
                "Teleporteert jou naar de gamewereld (laatste plek of team spawn).", 150,
                () -> enterGame(admin, false, true)));
        Component body = joinLines(
                statusLine("Lobby", lobbyCreated ? "aangemaakt" : (lobbyExists() ? "oude rest" : "nog niet"),
                        lobbyCreated ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Noord", spawnNoord != null ? "ingesteld" : "niet ingesteld",
                        spawnNoord != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                statusLine("Spawn Zuid", spawnZuid != null ? "ingesteld" : "niet ingesteld",
                        spawnZuid != null ? NamedTextColor.GREEN : NamedTextColor.YELLOW),
                Component.text("Klik op een schakelaar om hem om te zetten.", NamedTextColor.DARK_GRAY));
        showMenu(admin, "Wereld en server", body, buttons, backButton("Terug naar Home.", () -> openMenu(admin)));
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
        if (args.length == 2 && args[0].equalsIgnoreCase("menu")) {
            List<String> menuOptions = new ArrayList<>(Arrays.asList("nieuw", "klassiek", "edit", "geavanceerd", "simpel"));
            menuOptions.removeIf(o -> !o.startsWith(args[1].toLowerCase(Locale.ROOT)));
            return menuOptions;
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
            options.add("checklist");
            options.add("event");
            options.add("losers");
            options.add("tijdlijn");
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
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Cooldown instellen", NamedTextColor.WHITE),
                "Stel de cooldown van de mace in (in minuten).", 150, () -> openCooldownDialog(admin)));
        if (enabled) {
            buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Mace uitzetten", NamedTextColor.RED),
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
            buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Cooldown wissen", NamedTextColor.WHITE),
                    "Haalt de lopende cooldown van de mace weg.", 150, () -> {
                        resetCooldown(admin);
                        openMenu(admin);
                    }));
        }
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Titel testen", NamedTextColor.WHITE),
                "Laat de titel alleen aan jou zien.", 150, () -> openTitleTest(admin)));
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Mace geven", NamedTextColor.WHITE),
                "Geeft de mace aan een speler (alleen als hij nog niet bestaat).", 150,
                () -> core.pickPlayer(admin, "Mace geven", target -> {
                    giveMace(admin, target);
                    openMenu(admin);
                }, () -> openMenu(admin))));
        buttons.add(core.button(core.iconLabel(LowkeyCore.M_MACE, "Mace resetten", NamedTextColor.RED),
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
