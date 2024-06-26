package net.realdarkstudios.minecaching.api;

import com.google.common.collect.HashMultimap;
import net.realdarkstudios.commons.CommonsAPI;
import net.realdarkstudios.commons.util.Localization;
import net.realdarkstudios.commons.util.LocalizationProvider;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.commons.util.RDSLogHelper;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.log.LogbookDataObject;
import net.realdarkstudios.minecaching.api.log.LogbookStorage;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.misc.*;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.player.PlayerStorage;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class MinecachingAPI {
    private boolean hasInitialized = false;
    /**
     * Defines the expected Config Data Version
     */
    public static final int CONFIG_DATA_VERSION = 11;
    /**
     * Defines the expected Minecache Data Version
     */
    public static final int MINECACHE_DATA_VERSION = 6;
    /**
     * Defines the expected Player Data Version
     */
    public static final int PLAYER_DATA_VERSION = 6;
    /**
     * Defines the expected Logbook Data Version
     */
    public static final int LOGBOOK_DATA_VERSION = 1;
    /**
     * Defines the Minecaching Localization Provider
     */
    private static Localization minecachingLocalization;
    private static Config config;
    private static MinecacheStorage cacheStorage;
    private static PlayerStorage playerStorage;
    private static LogbookStorage logbookStorage;
    private static LocalizationProvider localizationProvider;
    private static RDSLogHelper logHelper;
    private static MCAutoUpdater autoUpdater;

    MinecachingAPI() {
    }

    public static RDSLogHelper getLogger() {
        return logHelper;
    }

    public static Localization getLocalization() {
        return minecachingLocalization;
    }

    public static Config getConfig() {
        return config;
    }

    public static MinecacheStorage getCacheStorage() {
        return cacheStorage;
    }

    public static PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    public static LogbookStorage getLogbookStorage() {
        return logbookStorage;
    }

    public static LocalizationProvider getLocalizationProvider() {
        return localizationProvider;
    }

    public static MCAutoUpdater getUpdater() {
        return autoUpdater;
    }

    /**
     * Logs all the messages at the INFO level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void info(String... messages) {
        logHelper.info(messages);
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the INFO level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MCMessageKeys
     * @since 0.3.1.0
     */
    public static void tInfo(LocalizedMessages.Key key, Object... formatArgs) {
        info(key.console(formatArgs));
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void warning(String... messages) {
        logHelper.warning(messages);
    }


    /**
     * Logs the message using the {@link LocalizedMessages} system at the WARNING level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MCMessageKeys
     * @since 0.3.1.0
     */
    public static void tWarning(LocalizedMessages.Key key, Object... formatArgs) {
        warning(key.console(formatArgs));
    }

    /**
     * Gets the player data for the given player
     * @param player The {@link Player} to get the data for
     * @return The {@link PlayerDataObject} associated with the player
     * @since 0.2.0.0
     */
    public PlayerDataObject getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Gets the player data for the given UUID
     * @param uuid The {@link UUID} of the player to get the data for
     * @return The {@link PlayerDataObject} associated with the UUID
     * @since 0.2.0.0
     */
    public PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.getOrCreatePlayerData(uuid);
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data
     * @return The list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getAllKnownPlayers() {
        return playerStorage.getPlayers();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and filters it by the predicate
     * @param predicate The method by which to filter out player data
     * @return The filtered list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getFilteredPlayers(Predicate<PlayerDataObject> predicate) {
        return getAllKnownPlayers().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and sorts it by the comparator
     * @param comparator The method by which to sort player data
     * @return The sorted list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.3.0.3
     */
    public List<PlayerDataObject> getSortedPlayers(Comparator<PlayerDataObject> comparator) {
        return getAllKnownPlayers().stream().sorted(comparator).toList();
    }

    /**
     * Checks if the player data for the given player exists
     * @param player The player to check for
     * @return true if the player data does exist, false otherwise
     * @since 0.2.0.0
     */
    public boolean hasPlayerData(Player player) {
        return hasPlayerData(player.getUniqueId());
    }

    /**
     * Checks if the player data for the given UUID exists
     * @param uuid The UUID of the player to check for
     * @return true if the player data does exist, false otherwise
     * @since 0.2.0.0
     */
    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.hasPlayerData(uuid);
    }

    /**
     * Deletes player data from the file system
     * @param pdo The {@link PlayerDataObject} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(PlayerDataObject pdo) {
        return deletePlayerData(pdo.getUniqueID());
    }

    /**
     * Deletes player data from the file system
     * @param player The {@link Player} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(Player player) {
        return deletePlayerData(player.getUniqueId());
    }

    /**
     * Deletes player data from the file system
     * @param uuid The {@link UUID} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deletePlayerData(UUID uuid) {
        return playerStorage.deletePlayerData(uuid);
    }

    /**
     * Gets the Minecache for the given cache ID
     * @param cacheId The ID of the cache to get
     * @return The {@link Minecache} associated with the cache ID
     * @since 0.2.0.0
     */
    public Minecache getMinecache(String cacheId) {
        return cacheId.equals("NULL") ? Minecache.EMPTY : cacheStorage.getMinecacheByID(cacheId);
    }

    /**
     * Gets the list of all known Minecaches
     * @return The list of all Minecaches
     * @since 0.2.0.0
     */
    public ArrayList<Minecache> getAllKnownCaches() {
        return cacheStorage.getMinecaches();
    }

    /**
     * Gets the list of all known Minecache IDs
     * @return The list of all Minecache IDs
     * @since 0.2.0.0
     */
    public ArrayList<String> getAllKnownCacheIDs() {
        return cacheStorage.getIDArray();
    }

    /**
     * Gets the list of all invalid Minecaches
     * @return The list of all invalid Minecaches
     * @since 0.2.0.0
     */
    public ArrayList<Minecache> getAllInvalidCaches() {
        return new ArrayList<>(getAllKnownCaches().stream().filter(m -> m.type().equals(MinecacheType.INVALID)).toList());
    }

    /**
     * Gets the list of all known caches and filters it by the predicate
     * @param predicate The method by which to filter out caches
     * @return The filtered list of Minecaches.
     * @since 0.2.0.0
     */
    public ArrayList<Minecache> getFilteredCaches(Predicate<Minecache> predicate) {
        return new ArrayList<>(getAllKnownCaches().stream().filter(predicate).toList());
    }

    /**
     * Gets the list of all known cache IDs and filters it by the predicate
     * @param predicate The method by which to filter out cache IDs
     * @return The filtered list of Minecache IDs.
     * @since 0.2.0.0
     */
    public ArrayList<String> getFilteredCacheIDs(Predicate<String> predicate) {
        return new ArrayList<>(getAllKnownCacheIDs().stream().filter(predicate).toList());
    }

    /**
     * Gets the list of all known caches and sorts it by the comparator
     * @param comparator The method by which to sort caches
     * @return The sorted list of Minecaches.
     * @since 0.3.0.3
     */
    public ArrayList<Minecache> getSortedCaches(Comparator<Minecache> comparator) {
        return new ArrayList<>(getAllKnownCaches().stream().sorted(comparator).toList());
    }

    /**
     * Gets the list of all known cache IDs and sorts it by the comparator
     * @param comparator The method by which to sort cache IDs
     * @return The sorted list of Minecache IDs.
     * @since 0.3.0.3
     */
    public ArrayList<String> getSortedCacheIDs(Comparator<String> comparator) {
        return new ArrayList<>(getAllKnownCacheIDs().stream().sorted(comparator).toList());
    }


    /**
     * Saves a minecache to the file system
     * @param minecache The cache to save
     * @param isNewCache If the cache is a new cache. For /addcache, this is true, while for /editcache, this is false
     * @return {@code true} if the cache successfully saved
     * @since 0.2.0.0
     */
    public boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        return cacheStorage.saveMinecache(minecache, isNewCache);
    }

    /**
     * Deletes a cache from the file system
     * @param minecache The cache to delete
     * @param initiator The {@link UUID} of the player who deleted this cache.
     * @return {@code true} if the cache was successfully deleted
     * @since 0.2.0.0
     */
    public boolean deleteMinecache(Minecache minecache, UUID initiator) {
        if (!minecache.owner().equals(initiator)) createNotification(minecache.owner(), initiator, NotificationType.DELETION, minecache);
        boolean success = (playerStorage.deleteMinecache(minecache) && cacheStorage.deleteMinecache(minecache));
        deleteLogbook(minecache);
        return success;
    }

    /**
     * Publishes a Minecache
     * @param player The {@link UUID} of the player who published this cache.
     * @param minecache The cache to publish
     * @return {@code true} if the cache was successfully published
     * @since 0.3.1.0
     */
    public boolean publishMinecache(UUID player, Minecache minecache) {
        return publishMinecache(player, minecache, MCMessageKeys.Command.Log.PUBLISH_DEFAULT_MESSAGE.translate(
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"))));
    }

    /**
     * Publishes a Minecache
     * @param player The {@link UUID} of the player who published this cache.
     * @param minecache The cache to publish
     * @return {@code true} if the cache was successfully published
     * @since 0.2.0.5
     */
    public boolean publishMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.PUBLISHED);

        MCUtils.createLog(player, minecache, LogType.PUBLISH, reason, false);

        if (!minecache.owner().equals(player)) createNotification(minecache.owner(), player, NotificationType.PUBLISH, minecache);
        return saveMinecache(minecache, false);
    }

    /**
     * Archives a minecache
     * @param player The {@link UUID} of the player who archived this cache
     * @param minecache The {@link Minecache} to be archived
     * @return {@code true} if the cache was successfully archived.
     * @since 0.3.1.0
     */
    public boolean archiveMinecache(UUID player, Minecache minecache) {
        return archiveMinecache(player, minecache, MCMessageKeys.Command.Log.ARCHIVE_DEFAULT_MESSAGE.translate());
    }

    /**
     * Archives a Minecache with a custom message
     * @param player The {@link UUID} of the player who archived this cache.
     * @param minecache The cache to archive
     * @return {@code true} if the cache was successfully archived
     * @since 0.2.2.2
     */
    public boolean archiveMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.ARCHIVED);

        MCUtils.createLog(player, minecache, LogType.ARCHIVE, reason, false);

        if (!minecache.owner().equals(player)) createNotification(minecache.owner(), player, NotificationType.ARCHIVAL, minecache);
        return saveMinecache(minecache, false);
    }

    /**
     * Disables a Minecache
     * @param player The {@link UUID} of the player who disabled this cache.
     * @param minecache The cache to disable
     * @return {@code true} if the cache was successfully disabled
     * @since 0.3.1.0
     */
    public boolean disableMinecache(UUID player, Minecache minecache) {
        return disableMinecache(player, minecache, MCMessageKeys.Command.Log.DISABLE_DEFAULT_MESSAGE.translate());
    }

    /**
     * Disables a Minecache
     * @param player The {@link UUID} of the player who disabled this cache.
     * @param minecache The cache to disable
     * @return {@code true} if the cache was successfully disabled
     * @since 0.2.2.2
     */
    public boolean disableMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.DISABLED);

        MCUtils.createLog(player, minecache, LogType.DISABLE, reason, false);

        if (!minecache.owner().equals(player)) createNotification(minecache.owner(), player, NotificationType.DISABLE, minecache);
        return saveMinecache(minecache, false);
    }

    /**
     * Gets the Logbook for the given cache
     * @param cache The {@link Minecache} of the Logbook
     * @return The {@link LogbookDataObject} associated with this cache
     * @since 0.2.1.0
     */
    public LogbookDataObject getLogbook(Minecache cache) {
        return getLogbook(cache.id());
    }

    /**
     * Gets the Logbook for the given id
     * @param id The ID of the Logbook
     * @return The {@link LogbookDataObject} associated with this ID
     * @since 0.2.1.0
     */
    public LogbookDataObject getLogbook(String id) {
        return logbookStorage.getOrCreateLDO(id);
    }

    /**
     * Gets all the known Logbooks
     * @return The list of all Logbooks
     * @since 0.2.1.0
     */
    public List<LogbookDataObject> getAllKnownLogbooks() {
        return logbookStorage.getAllKnownLDOs();
    }

    /**
     * Gets all the known Logbooks, filtered by the predicate
     * @param predicate The method by which to filter out Logbooks
     * @return The list of all filtered Logbooks
     * @since 0.2.1.0
     */
    public List<LogbookDataObject> getFilteredLogbooks(Predicate<LogbookDataObject> predicate) {
        return getAllKnownLogbooks().stream().filter(predicate).toList();
    }

    /**
     * Checks if the Logbook for the given cache exists
     * @param cache The {@link Minecache} to check for
     * @return {@code true} if the Logbook does exist, {@code false} otherwise
     * @since 0.2.1.0
     */
    public boolean hasLogbook(Minecache cache) {
        return hasLogbook(cache.id());
    }

    /**
     * Checks if the Logbook for the given ID exists
     * @param id The ID to check for
     * @return {@code true} if the Logbook does exist, {@code false} otherwise
     * @since 0.2.1.0
     */
    public boolean hasLogbook(String id) {
        return logbookStorage.hasLDO(id);
    }

    /**
     * Deletes the Logbook from the file system
     * @param ldo The {@link LogbookDataObject} associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deleteLogbook(LogbookDataObject ldo) {
        return deleteLogbook(ldo.id());
    }

    /**
     * Deletes the Logbook from the file system
     * @param cache The {@link Minecache} associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deleteLogbook(Minecache cache) {
        return deleteLogbook(cache.id());
    }

    /**
     * Deletes the Logbook from the file system
     * @param id The ID associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 0.2.1.0
     */
    public boolean deleteLogbook(String id) {
        return logbookStorage.deleteLDO(id);
    }

    /**
     * Creates and adds a notification to a Player
     * @param uuid The {@link UUID} of the Player
     * @param initiator The UUID of the initiator
     * @param type The {@link NotificationType} of the notification
     * @param cache The {@link Minecache} that was affected
     * @return {@code true} if succeeded, {@code false} if not
     * @since 0.2.2.2
     */
    public boolean createNotification(UUID uuid, UUID initiator, NotificationType type, Minecache cache) {
        try {
            PlayerDataObject pdo = getPlayerData(uuid);

            if (pdo.isOnline()) LocalizedMessages.send(pdo.getPlayer(), type.getTranslationKey(), ChatColor.GRAY, cache.id(), MCUtils.uuidName(initiator));
            else pdo.addNotification(new Notification(MCUtils.generateRandomString(5), initiator, type, cache, LocalDateTime.now()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Corrects server statistics by checking all {@link Minecache}s and {@link PlayerDataObject}s.
     * <p></p>
     * Accounts for hides, ftfs, caches where the owner finds them, deleted caches, and favorites.
     * Note that due to the Minecache/PlayerDataObject implementation, there is no way to check for extra/lost finds/favorites as PlayerDataObject is the sole truth for finds/favorites (and by extension, the player file.
     * <p></p>
     * This implementation:<br>
     *   - Adds a hide to all cache authors (if not already present, {@link PlayerDataObject#addHide(String)} takes care of it) <br>
     *   - Adds a FTF to all cache FTFs<br>
     *   - Checks for deleted caches<br>
     *   - Checks for found caches that the player authors<br>
     *   - Checks for hides<br>
     *   - Checks for FTFs<br>
     *   - Checks for more favorites that allowed (finds / 10, rounded down)<br>
     *   - Checks for favorite caches that don't have any favorites<br>
     * @since 0.3.0.5
     */
    public void correctStats() {
        List<Minecache> caches = getAllKnownCaches();
        List<PlayerDataObject> players = getAllKnownPlayers();
        HashMultimap<PlayerDataObject, String> findsToRemove = HashMultimap.create();
        HashMultimap<PlayerDataObject, String> hidesToRemove = HashMultimap.create();
        HashMultimap<PlayerDataObject, String> ftfsToRemove = HashMultimap.create();
        HashMultimap<PlayerDataObject, String> favoritesToRemove = HashMultimap.create();

        caches.forEach(c -> getPlayerData(c.owner()).addHide(c.id()));
        caches.forEach(c -> getPlayerData(c.ftf()).addFTF(c.id()));

        try {
            for (PlayerDataObject p: players) {
                for (String fi: p.getFinds()) {
                    if (getMinecache(fi).equals(Minecache.EMPTY) || getMinecache(fi).owner().equals(p.getUniqueID())) {
                        findsToRemove.put(p, fi);
                    }
                }
                for (String h: p.getHides()) {
                    if (!getMinecache(h).owner().equals(p.getUniqueID())) {
                         hidesToRemove.put(p, h);
                    }
                }
                for (String ftf: p.getFTFs()) {
                    if (!getMinecache(ftf).ftf().equals(p.getUniqueID())) {
                        ftfsToRemove.put(p, ftf);
                    }
                }
                if (p.getFavorites().size() > (p.getFinds().size() / 10)) {
                    int amountToRemove = p.getFavorites().size() - (p.getFinds().size() / 10);

                    for (int i = 0; i < amountToRemove; i++) {
                        p.removeFavorite(p.getFavorites().get(p.getFavorites().size() - 1));
                    }
                } else {
                    for (String fa : p.getFavorites()) {
                        if (!(getMinecache(fa).favorites() >= 1)) {
                            favoritesToRemove.put(p, fa);
                        }
                    }
                }
            }

            for (Map.Entry<PlayerDataObject, String> entry: findsToRemove.entries()) {
                entry.getKey().removeFind(entry.getValue());
            }

            for (Map.Entry<PlayerDataObject, String> entry: hidesToRemove.entries()) {
                entry.getKey().removeHide(entry.getValue());
            }

            for (Map.Entry<PlayerDataObject, String> entry: ftfsToRemove.entries()) {
                entry.getKey().removeFTF(entry.getValue());
            }

            for (Map.Entry<PlayerDataObject, String> entry: favoritesToRemove.entries()) {
                entry.getKey().removeFavorite(entry.getValue());
            }

            MinecachingAPI.tInfo(MCMessageKeys.Command.Admin.CORRECTED_STATS);
        } catch (Exception e) {
            MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.CORRECT_STATS);
        }
    }

    /**
     * Updates all the internal maps
     * @since 0.2.0.0
     */
    public void update() {
        MinecacheStorage.getInstance().updateMaps();
        PlayerStorage.getInstance().updateMaps();
        LogbookStorage.getInstance().updateMaps();
    }

    /**
     * Saves all plugin data
     * @since 0.2.1.2
     * @see MinecachingAPI#load(boolean)
     * @see MinecachingAPI#update()
     */
    public void save() {
        tInfo(MCMessageKeys.Plugin.SAVE, "Config");
        config.save();
        localizationProvider.clear();
        tInfo(MCMessageKeys.Plugin.SAVE, "Minecache Data");
        cacheStorage.save();
        tInfo(MCMessageKeys.Plugin.SAVE, "Player Data");
        playerStorage.save();
        tInfo(MCMessageKeys.Plugin.SAVE, "Logbook Data");
        logbookStorage.save();
    }

    /**
     * Loads all Minecaching Plugin data
     *
     * @param attemptUpdates Whether to attempt updates (if needed) or not
     * @since 0.2.1.2
     * @see MinecachingAPI#save()
     * @see MinecachingAPI#update()
     */
    public void load(boolean attemptUpdates) {
        File playerFolder = new File(Minecaching.getInstance().getDataFolder() + "/player/");
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File logFolder = new File(Minecaching.getInstance().getDataFolder() + "/logbook/");
        if (!logFolder.exists()) logFolder.mkdirs();

        config.load();

        minecachingLocalization = CommonsAPI.get().registerLocalization(Minecaching.getInstance(), Config.getInstance().getServerLocale(), "§8[§6MC§8]§7»§r ");

        // Load Message Keys for later use
        MCMessageKeys.init();

        logHelper = minecachingLocalization.getLogHelper();

        tInfo(MCMessageKeys.Plugin.LOAD, "Config");
        if (config.getConfigVersion() < CONFIG_DATA_VERSION) {
            tWarning(MCMessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Config");
            if (attemptUpdates) config.updateData();
            else tWarning(MCMessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Config");
        }

        autoUpdater.updateBranch(config.getUpdateBranch());
        if (config.experimentalFeatures()) {
            MinecachingAPI.tInfo(MCMessageKeys.Plugin.EXPERIMENTAL);
        }

        tInfo(MCMessageKeys.Plugin.LOAD, "Localization");

        tInfo(MCMessageKeys.Plugin.LOAD, "Minecache Data");
        cacheStorage.load();
        if (config.getMinecacheDataVersion() < MINECACHE_DATA_VERSION) {
            tWarning(MCMessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Minecache Data");
            if (attemptUpdates) cacheStorage.updateData();
            else tWarning(MCMessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Minecache Data");
        }

        tInfo(MCMessageKeys.Plugin.LOAD, "Player Data");
        playerStorage.load();
        if (config.getPlayerDataVersion() < PLAYER_DATA_VERSION) {
            tWarning(MCMessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Player Data");
            if (attemptUpdates) playerStorage.updateData();
            else tWarning(MCMessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Player Data");
        }

        tInfo(MCMessageKeys.Plugin.LOAD, "Logbook Data");
        logbookStorage.load();
        if (config.getLogbookDataVersion() < LOGBOOK_DATA_VERSION) {
            tWarning(MCMessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Logbook Data");
            if (attemptUpdates) logbookStorage.updateData();
            else tWarning(MCMessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Logbook Data");
        }

        correctStats();

        tInfo(MCMessageKeys.Plugin.LOAD, "Permissions");
        MCPermissions.init();
    }

    /**
     * Initializes the Minecaching API elements
     */
    @ApiStatus.Internal
    void init() {
        //logger = Minecaching.getInstance().getLogger();
        //Use logHelper.getLogger() instead
        config = Config.getInstance();
        localizationProvider = CommonsAPI.get().getLocalizationProvider();
        cacheStorage = MinecacheStorage.getInstance();
        playerStorage = PlayerStorage.getInstance();
        logbookStorage = LogbookStorage.getInstance();
        autoUpdater = new MCAutoUpdater();
        hasInitialized = true;
    }

    /**
     * Checks if the Minecaching API has been initialized
     * @return {@code true} if initialized, {@code false} otherwise
     */
    public boolean hasInitialized() {
        return hasInitialized;
    }

    /**
     * Gets the instance of the API
     * @return the API instance
     * @since 0.2.0.0
     */
    public static MinecachingAPI get() {
        return Minecaching.getAPI();
    }

    public static CommonsAPI getCommonsAPI() {
        return CommonsAPI.get();
    }
}
