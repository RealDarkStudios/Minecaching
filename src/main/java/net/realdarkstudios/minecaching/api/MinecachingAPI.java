package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class MinecachingAPI {
    private static final MinecachingAPI api = new MinecachingAPI();
    private static final int CONFIG_DATA_VERSION = 5;
    private static final int MINECACHE_DATA_VERSION = 3;
    private static final int PLAYER_DATA_VERSION = 3;

    private static final int LOGBOOK_DATA_VERSION = 1;

    private MinecachingAPI() {
    }

    /**
     * Gets the player data for the given player
     * @param player The player to get the data for
     * @return The {@link PlayerDataObject} associated with the player
     * @since 2.0.0.0
     */
    public PlayerDataObject getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Gets the player data for the given UUID
     * @param uuid The UUID of the player to get the data for
     * @return The {@link PlayerDataObject} associated with the UUID
     * @since 2.0.0.0
     */
    public PlayerDataObject getPlayerData(UUID uuid) {
        return PlayerStorage.getInstance().getOrCreatePlayerData(uuid);
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

    public List<PlayerDataObject> getAllKnownPlayers() {
        return PlayerStorage.getInstance().getPlayers();
    }

    public List<PlayerDataObject> getFilteredPlayers(Predicate<PlayerDataObject> predicate) {
        return PlayerStorage.getInstance().getPlayers().stream().filter(predicate).toList();
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
        return getAllKnownCaches().stream().filter(m -> m.type() == MinecacheType.INVALID).toList();
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
    public boolean verifyMinecache(Minecache minecache) {
        minecache.setStatus(MinecacheStatus.VERIFIED);
        return saveMinecache(minecache, false);
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
