package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.log.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.Localization;
import net.realdarkstudios.minecaching.api.misc.LocalizationProvider;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.player.PlayerStorage;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class MinecachingAPI {
    private static final MinecachingAPI api = new MinecachingAPI();
    /**
     * Defines the expected Config Data Version
     */
    private static final int CONFIG_DATA_VERSION = 8;
    /**
     * Defines the expected Minecache Data Version
     */
    private static final int MINECACHE_DATA_VERSION = 3;
    /**
     * Defines the expected Player Data Version
     */
    private static final int PLAYER_DATA_VERSION = 4;
    /**
     * Defines the expected Logbook Data Version
     */
    private static final int LOGBOOK_DATA_VERSION = 1;
    /**
     * Defines the Minecaching Localization Provider
     */
    private static Localization MINECACHING_LOCALIZATION;

    private MinecachingAPI() {
    }

    /**
     * Gets the Config Data Version
     * @return the expected Config Data Version
     * @since 0.2.1.0
     */
    public static int getConfigDataVersion() {
        return CONFIG_DATA_VERSION;
    }

    /**
     * Gets the Minecache Data Version
     * @return the expected Minecache Data Version
     * @since 0.2.1.0
     */
    public static int getMinecacheDataVersion() {
        return MINECACHE_DATA_VERSION;
    }

    /**
     * Gets the Player Data Version
     * @return the expected Player Data Version
     * @since 0.2.1.0
     */
    public static int getPlayerDataVersion() {
        return PLAYER_DATA_VERSION;
    }

    /**
     * Gets the Logbook Data Version
     * @return the expected Logbook Data Version
     * @since 0.2.1.0
     */
    public static int getLogbookDataVersion() {
        return LOGBOOK_DATA_VERSION;
    }

    public static Localization getLocalization() {
        return MINECACHING_LOCALIZATION;
    }

    /**
     * Logs all the messages at the INFO level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void info(String... messages) {
        for (String msg : messages) {
            Minecaching.getInstance().getLogger().info(msg);
        }
    }

    public static void tInfo(String key) {
        Minecaching.getInstance().getLogger().info(MINECACHING_LOCALIZATION.getTranslation(key));
    }

    public static void tInfo(String key, Object... substitutions) {
        Minecaching.getInstance().getLogger().info(MINECACHING_LOCALIZATION.getTranslation(key, substitutions));
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 0.2.1.0
     */
    public static void warning(String... messages) {
        for (String msg : messages) {
            Minecaching.getInstance().getLogger().warning(msg);
        }
    }

    public static void tWarning(String key) {
        Minecaching.getInstance().getLogger().warning(MINECACHING_LOCALIZATION.getTranslation(key));
    }

    public static void tWarning(String key, Object... substitutions) {
        Minecaching.getInstance().getLogger().warning(MINECACHING_LOCALIZATION.getTranslation(key, substitutions));
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
        return PlayerStorage.getInstance().getOrCreatePlayerData(uuid);
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data
     * @return The list of PlayerDataObjects for all players that have data.
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getAllKnownPlayers() {
        return PlayerStorage.getInstance().getPlayers();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and filters it by the predicate
     * @param predicate The method by which to filter out player data
     * @return The filtered list of PlayerDataObjects for all players that have data.
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getFilteredPlayers(Predicate<PlayerDataObject> predicate) {
        return PlayerStorage.getInstance().getPlayers().stream().filter(predicate).toList();
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
        return PlayerStorage.getInstance().hasPlayerData(uuid);
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
        return PlayerStorage.getInstance().deletePlayerData(uuid);
    }

    /**
     * Gets the Minecache for the given cache ID
     * @param cacheId The ID of the cache to get
     * @return The {@link Minecache} associated with the cache ID
     * @since 0.2.0.0
     */
    public Minecache getMinecache(String cacheId) {
        return cacheId.equals("NULL") ? Minecache.EMPTY : MinecacheStorage.getInstance().getMinecacheByID(cacheId);
    }

    /**
     * Gets the list of all known Minecaches
     * @return The list of all Minecaches
     * @since 0.2.0.0
     */
    public List<Minecache> getAllKnownCaches() {
        return MinecacheStorage.getInstance().getMinecaches();
    }

    /**
     * Gets the list of all known Minecache IDs
     * @return A list of all Minecache IDs
     * @since 0.2.0.0
     */
    public List<String> getAllKnownCacheIDs() {
        return MinecacheStorage.getInstance().getIDArray();
    }

    /**
     * Gets the list of all invalid Minecaches
     * @return A list of all invalid Minecaches
     * @since 0.2.0.0
     */
    public List<Minecache> getAllInvalidCaches() {
        return getAllKnownCaches().stream().filter(m -> m.type().equals(MinecacheType.INVALID)).toList();
    }

    /**
     * Gets the list of all known caches and filters it by the predicate
     * @param predicate The method by which to filter out caches
     * @return A list of all caches that met the predicate
     * @since 0.2.0.0
     */
    public List<Minecache> getFilteredCaches(Predicate<Minecache> predicate) {
        return getAllKnownCaches().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of all known cache IDs and filters it by the predicate
     * @param predicate The method by which to filter out cache IDs
     * @return A list of all cache IDs that met the predicate
     * @since 0.2.0.0
     */
    public List<String> getFilteredCacheIDs(Predicate<String> predicate) {
        return getAllKnownCacheIDs().stream().filter(predicate).toList();
    }

    /**
     * Saves a minecache to the file system
     * @param minecache The cache to save
     * @param isNewCache If the cache is a new cache. For /addcache, this is true, while for /editcache, this is false
     * @return {@code true} if the cache successfully saved
     * @since 0.2.0.0
     */
    public boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        return MinecacheStorage.getInstance().saveMinecache(minecache, isNewCache);
    }

    /**
     * Deletes a cache from the file system
     * @param minecache The cache to delete
     * @return {@code true} if the cache was successfully deleted
     * @since 0.2.0.0
     */
    public boolean deleteMinecache(Minecache minecache, UUID initiator) {
        if (!minecache.author().equals(initiator)) createNotification(minecache.author(), initiator, NotificationType.DELETION, minecache);
        return (PlayerStorage.getInstance().deleteMinecache(minecache) && MinecacheStorage.getInstance().deleteMinecache(minecache));
    }

    /**
     * Publishes a Minecache
     * @param minecache The cache to publish
     * @return {@code true} if the cache was successfully published
     * @since 0.2.0.5
     */
    public boolean publishMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.PUBLISHED);

        Utils.createLog(player, minecache, LogType.PUBLISH, reason, false);

        if (!minecache.author().equals(player)) createNotification(minecache.author(), player, NotificationType.PUBLISH, minecache);
        return saveMinecache(minecache, false);
    }

    /**
     * Archives a Minecache
     * @param minecache The cache to archive
     * @return {@code true} if the cache was successfully archived
     * @since 0.2.2.2
     */
    public boolean archiveMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.ARCHIVED);

        Utils.createLog(player, minecache, LogType.ARCHIVE, reason, false);

        if (!minecache.author().equals(player)) createNotification(minecache.author(), player, NotificationType.ARCHIVAL, minecache);
        return saveMinecache(minecache, false);
    }

    /**
     * Disables a Minecache
     * @param minecache The cache to disable
     * @return {@code true} if the cache was successfully disabled
     * @since 0.2.2.2
     */
    public boolean disableMinecache(UUID player, Minecache minecache, String reason) {
        minecache.setStatus(MinecacheStatus.DISABLED);

        Utils.createLog(player, minecache, LogType.DISABLE, reason, false);

        if (!minecache.author().equals(player)) createNotification(minecache.author(), player, NotificationType.DISABLE, minecache);
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
        return LogbookStorage.getInstance().getOrCreateLogbook(id);
    }

    /**
     * Gets all the known Logbooks
     * @return The list of all Logbooks
     * @since 0.2.1.0
     */
    public List<LogbookDataObject> getAllKnownLogbooks() {
        return LogbookStorage.getInstance().getLogbooks();
    }

    /**
     * Gets all the known Logbooks, filtered by the predicate
     * @param predicate The method by which to filter out Logbooks
     * @return The list of all filtered Logbooks
     * @since 0.2.1.0
     */
    public List<LogbookDataObject> getFilteredLogbooks(Predicate<LogbookDataObject> predicate) {
        return LogbookStorage.getInstance().getLogbooks().stream().filter(predicate).toList();
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
        return LogbookStorage.getInstance().hasLogbook(id);
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
        return LogbookStorage.getInstance().deleteLogbook(id);
    }

    /**
     * Creates and adds a notification to a Player
     * @param uuid The {@link UUID} of the Player
     * @param initiator The UUID of the initiator
     * @param type The {@link NotificationType} of the notification
     * @param cache The {@link Minecache} that was affected
     * @return {@code true} if succeeded, {@code false} if not
     * @since o.2.2.2
     */
    public boolean createNotification(UUID uuid, UUID initiator, NotificationType type, Minecache cache) {
        try {
            PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(uuid);

            if (pdo.getPlayer().isOnline()) MCMessages.sendMsg(pdo.getPlayer().getPlayer(), type.getTranslationKey(), ChatColor.GRAY, cache.id(), Utils.uuidName(initiator));
            else pdo.addNotification(new Notification(Utils.generateRandomString(5), initiator, type, cache, LocalDateTime.now()));
            return true;
        } catch (Exception e) {
            return false;
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
        tInfo("plugin.save", "Config");
        Config.getInstance().save();
        tInfo("plugin.save", "Minecache Data");
        MinecacheStorage.getInstance().save();
        tInfo("plugin.save", "Player Data");
        PlayerStorage.getInstance().save();
        tInfo("plugin.save", "Logbook Data");
        LogbookStorage.getInstance().save();
    }

    /**
     * Loads all plugin data
     *
     * @param attemptUpdates Whether to attempt updates (if needed) or not
     * @since 0.2.1.2
     * @see MinecachingAPI#save()
     * @see MinecachingAPI#update()
     */
    public void load(boolean attemptUpdates) {
        Minecaching minecaching = Minecaching.getInstance();

        File playerFolder = new File(minecaching.getDataFolder() + "/player/");
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File logFolder = new File(minecaching.getDataFolder() + "/logbook/");
        if (!logFolder.exists()) logFolder.mkdirs();

        Config.getInstance().load();
        MINECACHING_LOCALIZATION = LocalizationProvider.getInstance().load(minecaching);

        tInfo("plugin.load", "Config");
        if (Config.getInstance().getConfigVersion() < CONFIG_DATA_VERSION) {
            tWarning("plugin.data.update", "Config");
            if (attemptUpdates) Config.getInstance().attemptUpdate();
            else tWarning("plugin.data.update.notattempting", "Config");
        }

        AutoUpdater.startCheck();

        MinecachingAPI.tInfo("plugin.load", "Localization");

        tInfo("plugin.load", "Minecache Data");
        MinecacheStorage.getInstance().load();
        if (Config.getInstance().getMinecacheDataVersion() < MINECACHE_DATA_VERSION) {
            tWarning("plugin.data.update", "Minecache Data");
            if (attemptUpdates) MinecacheStorage.getInstance().attemptUpdate();
            else tWarning("plugin.data.update.notattempting", "Minecache Data");
        }

        tInfo("plugin.load", "Player Data");
        PlayerStorage.getInstance().load();
        if (Config.getInstance().getPlayerDataVersion() < PLAYER_DATA_VERSION) {
            tWarning("plugin.data.update", "Player Data");
            if (attemptUpdates) PlayerStorage.getInstance().attemptUpdate();
            else tWarning("plugin.data.update.notattempting", "Player Data");
        }

        tInfo("plugin.load", "Logbook Data");
        LogbookStorage.getInstance().load();
        if (Config.getInstance().getLogbookDataVersion() < LOGBOOK_DATA_VERSION) {
            tWarning("plugin.data.update", "Logbook Data");
            if (attemptUpdates) LogbookStorage.getInstance().attemptUpdate();
            else tWarning("plugin.data.update.notattempting", "Logbook Data");
        }
    }

    public void checkForUpdate() {

    }

    /**
     * Gets the instance of the API
     * @return the API instance
     * @since 0.2.0.0
     */
    public static MinecachingAPI get() {
        return api;
    }
}
