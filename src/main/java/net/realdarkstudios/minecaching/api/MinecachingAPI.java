package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class MinecachingAPI {
    private static final MinecachingAPI api = new MinecachingAPI();
    /**
     * Defines the expected Config Data Version
     */
    private static final int CONFIG_DATA_VERSION = 6;
    /**
     * Defines the expected Minecache Data Version
     */
    private static final int MINECACHE_DATA_VERSION = 3;
    /**
     * Defines the expected Player Data Version
     */
    private static final int PLAYER_DATA_VERSION = 3;
    /**
     * Defines the expected Logbook Data Version
     */
    private static final int LOGBOOK_DATA_VERSION = 1;

    private MinecachingAPI() {
    }

    /**
     * Gets the Config Data Version
     * @return the expected Config Data Version
     * @since 2.0.1.0
     */
    public static int getConfigDataVersion() {
        return CONFIG_DATA_VERSION;
    }

    /**
     * Gets the Minecache Data Version
     * @return the expected Minecache Data Version
     * @since 2.0.1.0
     */
    public static int getMinecacheDataVersion() {
        return MINECACHE_DATA_VERSION;
    }

    /**
     * Gets the Player Data Version
     * @return the expected Player Data Version
     * @since 2.0.1.0
     */
    public static int getPlayerDataVersion() {
        return PLAYER_DATA_VERSION;
    }

    /**
     * Gets the Logbook Data Version
     * @return the expected Logbook Data Version
     * @since 2.0.1.0
     */
    public static int getLogbookDataVersion() {
        return LOGBOOK_DATA_VERSION;
    }

    /**
     * Logs all the messages at the INFO level
     * @param messages The messages to log
     * @since 2.0.1.0
     */
    public static void info(String... messages) {
        for (String msg : messages) {
            Minecaching.getInstance().getLogger().info(msg);
        }
    }

    /**
     * Logs all the messages at the WARN level
     * @param messages The messages to log
     * @since 2.0.1.0
     */
    public static void warning(String... messages) {
        for (String msg : messages) {
            Minecaching.getInstance().getLogger().warning(msg);
        }
    }

    /**
     * Gets the player data for the given player
     * @param player The {@link Player} to get the data for
     * @return The {@link PlayerDataObject} associated with the player
     * @since 2.0.0.0
     */
    public PlayerDataObject getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Gets the player data for the given UUID
     * @param uuid The {@link UUID} of the player to get the data for
     * @return The {@link PlayerDataObject} associated with the UUID
     * @since 2.0.0.0
     */
    public PlayerDataObject getPlayerData(UUID uuid) {
        return PlayerStorage.getInstance().getOrCreatePlayerData(uuid);
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data
     * @return The list of PlayerDataObjects for all players that have data.
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 2.0.1.0
     */
    public List<PlayerDataObject> getAllKnownPlayers() {
        return PlayerStorage.getInstance().getPlayers();
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data and filters it by the predicate
     * @param predicate The method by which to filter out player data
     * @return The filtered list of PlayerDataObjects for all players that have data.
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 2.0.1.0
     */
    public List<PlayerDataObject> getFilteredPlayers(Predicate<PlayerDataObject> predicate) {
        return PlayerStorage.getInstance().getPlayers().stream().filter(predicate).toList();
    }

    /**
     * Checks if the player data for the given player exists
     * @param player The player to check for
     * @return true if the player data does exist, false otherwise
     * @since 2.0.0.0
     */
    public boolean hasPlayerData(Player player) {
        return hasPlayerData(player.getUniqueId());
    }

    /**
     * Checks if the player data for the given UUID exists
     * @param uuid The UUID of the player to check for
     * @return true if the player data does exist, false otherwise
     * @since 2.0.0.0
     */
    public boolean hasPlayerData(UUID uuid) {
        return PlayerStorage.getInstance().hasPlayerData(uuid);
    }

    /**
     * Deletes player data from the file system
     * @param pdo The {@link PlayerDataObject} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deletePlayerData(PlayerDataObject pdo) {
        return deletePlayerData(pdo.getUniqueID());
    }

    /**
     * Deletes player data from the file system
     * @param player The {@link Player} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deletePlayerData(Player player) {
        return deletePlayerData(player.getUniqueId());
    }

    /**
     * Deletes player data from the file system
     * @param uuid The {@link UUID} associated with the player data you want to delete
     * @return {@code true} if it succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deletePlayerData(UUID uuid) {
        return PlayerStorage.getInstance().deletePlayerData(uuid);
    }

    /**
     * Gets the Minecache for the given cache ID
     * @param cacheId The ID of the cache to get
     * @return The {@link Minecache} associated with the cache ID
     * @since 2.0.0.0
     */
    public Minecache getMinecache(String cacheId) {
        return cacheId.equals("NULL") ? Minecache.EMPTY : MinecacheStorage.getInstance().getMinecacheByID(cacheId);
    }

    /**
     * Gets the list of all known Minecaches
     * @return The list of all Minecaches
     * @since 2.0.0.0
     */
    public List<Minecache> getAllKnownCaches() {
        return MinecacheStorage.getInstance().getMinecaches();
    }

    /**
     * Gets the list of all known Minecache IDs
     * @return A list of all Minecache IDs
     * @since 2.0.0.0
     */
    public List<String> getAllKnownCacheIDs() {
        return MinecacheStorage.getInstance().getIDArray();
    }

    /**
     * Gets the list of all invalid Minecaches
     * @return A list of all invalid Minecaches
     * @since 2.0.0.0
     */
    public List<Minecache> getAllInvalidCaches() {
        return getAllKnownCaches().stream().filter(m -> m.type().equals(MinecacheType.INVALID)).toList();
    }

    /**
     * Gets the list of all known caches and filters it by the predicate
     * @param predicate The method by which to filter out caches
     * @return A list of all caches that met the predicate
     * @since 2.0.0.0
     */
    public List<Minecache> getFilteredCaches(Predicate<Minecache> predicate) {
        return getAllKnownCaches().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of all known cache IDs and filters it by the predicate
     * @param predicate The method by which to filter out cache IDs
     * @return A list of all cache IDs that met the predicate
     * @since 2.0.0.0
     */
    public List<String> getFilteredCacheIDs(Predicate<String> predicate) {
        return getAllKnownCacheIDs().stream().filter(predicate).toList();
    }

    /**
     * Saves a minecache to the file system
     * @param minecache The cache to save
     * @param isNewCache If the cache is a new cache. For /addcache, this is true, while for /editcache, this is false
     * @return {@code true} if the cache successfully saved
     * @since 2.0.0.0
     */
    public boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        return MinecacheStorage.getInstance().saveMinecache(minecache, isNewCache);
    }

    /**
     * Deletes a cache from the file system
     * @param minecache The cache to delete
     * @return {@code true} if the cache was successfully deleted
     * @since 2.0.0.0
     */
    public boolean deleteMinecache(Minecache minecache) {
        return (PlayerStorage.getInstance().deleteMinecache(minecache) && MinecacheStorage.getInstance().deleteMinecache(minecache));
    }

    /**
     * Verifies a cache
     * @param minecache The cache to verify
     * @return {@code true} if the cache was successfully verified
     * @since 2.0.0.5
     */
    public boolean verifyMinecache(UUID player, Minecache minecache) {
        minecache.setStatus(MinecacheStatus.VERIFIED);

        Utils.createLog(player, minecache, LogType.PUBLISH, "Published", false);

        return saveMinecache(minecache, false);
    }

    /**
     * Gets the Logbook for the given cache
     * @param cache The {@link Minecache} of the Logbook
     * @return The {@link LogbookDataObject} associated with this cache
     * @since 2.0.1.0
     */
    public LogbookDataObject getLogbook(Minecache cache) {
        return getLogbook(cache.id());
    }

    /**
     * Gets the Logbook for the given id
     * @param id The ID of the Logbook
     * @return The {@link LogbookDataObject} associated with this ID
     * @since 2.0.1.0
     */
    public LogbookDataObject getLogbook(String id) {
        return LogbookStorage.getInstance().getOrCreateLogbook(id);
    }

    /**
     * Gets all the known Logbooks
     * @return The list of all Logbooks
     * @since 2.0.1.0
     */
    public List<LogbookDataObject> getAllKnownLogbooks() {
        return LogbookStorage.getInstance().getLogbooks();
    }

    /**
     * Gets all the known Logbooks, filtered by the predicate
     * @param predicate The method by which to filter out Logbooks
     * @return The list of all filtered Logbooks
     * @since 2.0.1.0
     */
    public List<LogbookDataObject> getFilteredLogbooks(Predicate<LogbookDataObject> predicate) {
        return LogbookStorage.getInstance().getLogbooks().stream().filter(predicate).toList();
    }

    /**
     * Checks if the Logbook for the given cache exists
     * @param cache The {@link Minecache} to check for
     * @return {@code true} if the Logbook does exist, {@code false} otherwise
     * @since 2.0.1.0
     */
    public boolean hasLogbook(Minecache cache) {
        return hasLogbook(cache.id());
    }

    /**
     * Checks if the Logbook for the given ID exists
     * @param id The ID to check for
     * @return {@code true} if the Logbook does exist, {@code false} otherwise
     * @since 2.0.1.0
     */
    public boolean hasLogbook(String id) {
        return LogbookStorage.getInstance().hasLogbook(id);
    }

    /**
     * Deletes the Logbook from the file system
     * @param ldo The {@link LogbookDataObject} associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deleteLogbook(LogbookDataObject ldo) {
        return deleteLogbook(ldo.id());
    }

    /**
     * Deletes the Logbook from the file system
     * @param cache The {@link Minecache} associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deleteLogbook(Minecache cache) {
        return deleteLogbook(cache.id());
    }

    /**
     * Deletes the Logbook from the file system
     * @param id The ID associated with the Logbook you want to delete
     * @return {@code true} if succeeded, {@code false} if not
     * @since 2.0.1.0
     */
    public boolean deleteLogbook(String id) {
        return LogbookStorage.getInstance().deleteLogbook(id);
    }

    /**
     * Updates all the internal maps
     * @since 2.0.0.0
     */
    public void update() {
        MinecacheStorage.getInstance().updateMaps();
        PlayerStorage.getInstance().updateMaps();
        LogbookStorage.getInstance().updateMaps();
    }

    /**
     * Saves all plugin data
     * @since 2.0.1.2
     * @see MinecachingAPI#load(boolean)
     * @see MinecachingAPI#update()
     */
    public void save() {
        Minecaching.getInstance().getLogger().info("Saving config...");
        Config.getInstance().save();
        Minecaching.getInstance().getLogger().info("Saving minecache data...");
        MinecacheStorage.getInstance().save();
        Minecaching.getInstance().getLogger().info("Saving player data...");
        PlayerStorage.getInstance().save();
        Minecaching.getInstance().getLogger().info("Saving logbook data...");
        LogbookStorage.getInstance().save();
    }

    /**
     * Loads all plugin data
     *
     * @param attemptUpdates Whether to attempt updates (if needed) or not
     * @since 2.0.1.2
     * @see MinecachingAPI#save()
     * @see MinecachingAPI#update()
     */
    public void load(boolean attemptUpdates) {
        Minecaching minecaching = Minecaching.getInstance();
        
        minecaching.getLogger().info("Loading config...");
        Config.getInstance().load();
        if (Config.getInstance().getConfigVersion() < CONFIG_DATA_VERSION) {
            minecaching.getLogger().warning("Config Version out of date!");
            if (attemptUpdates) Config.getInstance().attemptUpdate();
            else minecaching.getLogger().warning("Not attempting update!");
        }

        minecaching.getLogger().info("Loading minecaches...");
        MinecacheStorage.getInstance().load();
        if (Config.getInstance().getMinecacheDataVersion() < MINECACHE_DATA_VERSION) {
            minecaching.getLogger().warning("Minecache Data Version out of date!");
            if (attemptUpdates) MinecacheStorage.getInstance().attemptUpdate();
            else minecaching.getLogger().warning("Not attempting update!");
        }

        minecaching.getLogger().info("Loading player data...");
        PlayerStorage.getInstance().load();
        if (Config.getInstance().getPlayerDataVersion() < PLAYER_DATA_VERSION) {
            minecaching.getLogger().warning("Player Data Version out of date!");
            if (attemptUpdates) PlayerStorage.getInstance().attemptUpdate();
            else minecaching.getLogger().warning("Not attempting update!");
        }

        minecaching.getLogger().info("Loading logbook data...");
        LogbookStorage.getInstance().load();
        if (Config.getInstance().getLogbookDataVersion() < LOGBOOK_DATA_VERSION) {
            minecaching.getLogger().warning("Logbook Data Version out of date!");
            if (attemptUpdates) LogbookStorage.getInstance().attemptUpdate();
            else minecaching.getLogger().warning("Not attempting update!");
        }
    }

    /**
     * Gets the instance of the API
     * @return the API instance
     * @since 2.0.0.0
     */
    public static MinecachingAPI get() {
        return api;
    }
}
