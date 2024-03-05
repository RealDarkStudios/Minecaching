package net.realdarkstudios.minecaching.api;

import com.google.common.collect.HashMultimap;
import net.realdarkstudios.minecaching.Minecaching;
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
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class MinecachingAPI {
    private static final MinecachingAPI api = new MinecachingAPI();
    /**
     * Defines the expected Config Data Version
     */
    private static final int CONFIG_DATA_VERSION = 10;
    /**
     * Defines the expected Minecache Data Version
     */
    private static final int MINECACHE_DATA_VERSION = 5;
    /**
     * Defines the expected Player Data Version
     */
    private static final int PLAYER_DATA_VERSION = 6;
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

    /**
     * Logs the message using the Minecaching {@link Localization} (with substitutions) at the INFO level
     * @param key The requested path
     * @param substitutions The substitutions (or format args)
     * @since 0.2.2.1
     * @deprecated Since 0.3.1.0 | Please use {@link MinecachingAPI#tInfo(LocalizedMessages.Key, Object...)} instead
     */
    @Deprecated(since = "0.3.1.0", forRemoval = true)
    public static void tInfo(String key, Object... substitutions) {
        info(MINECACHING_LOCALIZATION.getTranslation(key, substitutions));
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the INFO level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
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
        for (String msg : messages) {
            Minecaching.getInstance().getLogger().warning(msg);
        }
    }

    /**
     * Logs the message using the Minecaching {@link Localization} at the INFO level
     * @param key The requested path
     * @param substitutions The substitutions (or format args)
     * @since 0.2.2.1
     * @deprecated Since 0.3.1.0 | Please use {@link MinecachingAPI#tWarning(LocalizedMessages.Key, Object...)} instead
     */
    @Deprecated(since = "0.3.1.0", forRemoval = true)
    public static void tWarning(String key, Object... substitutions) {
        warning(MINECACHING_LOCALIZATION.getTranslation(key, substitutions));
    }

    /**
     * Logs the message using the {@link LocalizedMessages} system at the WARNING level
     * @param key The {@link LocalizedMessages.Key} to log
     * @param formatArgs The format arguments
     * @see MessageKeys
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
        return PlayerStorage.getInstance().getOrCreatePlayerData(uuid);
    }

    /**
     * Gets the list of {@link PlayerDataObject}s for all players that currently have data
     * @return The list of PlayerDataObjects for all players that have data
     * If you want a list of ALL players, use {@link Bukkit#getOfflinePlayers()} or {@link Bukkit#getOnlinePlayers()}
     * @since 0.2.1.0
     */
    public List<PlayerDataObject> getAllKnownPlayers() {
        return PlayerStorage.getInstance().getPlayers();
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
     * @return The list of all Minecache IDs
     * @since 0.2.0.0
     */
    public List<String> getAllKnownCacheIDs() {
        return MinecacheStorage.getInstance().getIDArray();
    }

    /**
     * Gets the list of all invalid Minecaches
     * @return The list of all invalid Minecaches
     * @since 0.2.0.0
     */
    public List<Minecache> getAllInvalidCaches() {
        return getAllKnownCaches().stream().filter(m -> m.type().equals(MinecacheType.INVALID)).toList();
    }

    /**
     * Gets the list of all known caches and filters it by the predicate
     * @param predicate The method by which to filter out caches
     * @return The filtered list of Minecaches.
     * @since 0.2.0.0
     */
    public List<Minecache> getFilteredCaches(Predicate<Minecache> predicate) {
        return getAllKnownCaches().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of all known cache IDs and filters it by the predicate
     * @param predicate The method by which to filter out cache IDs
     * @return The filtered list of Minecache IDs.
     * @since 0.2.0.0
     */
    public List<String> getFilteredCacheIDs(Predicate<String> predicate) {
        return getAllKnownCacheIDs().stream().filter(predicate).toList();
    }

    /**
     * Gets the list of all known caches and sorts it by the comparator
     * @param comparator The method by which to sort caches
     * @return The sorted list of Minecaches.
     * @since 0.3.0.3
     */
    public List<Minecache> getSortedCaches(Comparator<Minecache> comparator) {
        return getAllKnownCaches().stream().sorted(comparator).toList();
    }

    /**
     * Gets the list of all known cache IDs and sorts it by the comparator
     * @param comparator The method by which to sort cache IDs
     * @return The sorted list of Minecache IDs.
     * @since 0.3.0.3
     */
    public List<String> getSortedCacheIDs(Comparator<String> comparator) {
        return getAllKnownCacheIDs().stream().sorted(comparator).toList();
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
     * @param initiator The {@link UUID} of the player who deleted this cache.
     * @return {@code true} if the cache was successfully deleted
     * @since 0.2.0.0
     */
    public boolean deleteMinecache(Minecache minecache, UUID initiator) {
        if (!minecache.owner().equals(initiator)) createNotification(minecache.owner(), initiator, NotificationType.DELETION, minecache);
        boolean success = (PlayerStorage.getInstance().deleteMinecache(minecache) && MinecacheStorage.getInstance().deleteMinecache(minecache));
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
        return publishMinecache(player, minecache, MessageKeys.Command.Log.PUBLISH_DEFAULT_MESSAGE.translate(
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
        return archiveMinecache(player, minecache, MessageKeys.Command.Log.ARCHIVE_DEFAULT_MESSAGE.translate());
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
        return disableMinecache(player, minecache, MessageKeys.Command.Log.DISABLE_DEFAULT_MESSAGE.translate());
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
     * @since 0.2.2.2
     */
    public boolean createNotification(UUID uuid, UUID initiator, NotificationType type, Minecache cache) {
        try {
            PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(uuid);

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

            MinecachingAPI.tInfo(MessageKeys.Command.Admin.CORRECTED_STATS);
        } catch (Exception e) {
            MinecachingAPI.tWarning(MessageKeys.Error.Misc.MCADMIN_CORRECTING_STATS);
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
        tInfo(MessageKeys.Plugin.SAVE, "Config");
        Config.getInstance().save();
        LocalizationProvider.getInstance().clear();
        tInfo(MessageKeys.Plugin.SAVE, "Minecache Data");
        MinecacheStorage.getInstance().save();
        tInfo(MessageKeys.Plugin.SAVE, "Player Data");
        PlayerStorage.getInstance().save();
        tInfo(MessageKeys.Plugin.SAVE, "Logbook Data");
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

        // Load Message Keys for later use
        MessageKeys.init();

        tInfo(MessageKeys.Plugin.LOAD, "Config");
        if (Config.getInstance().getConfigVersion() < CONFIG_DATA_VERSION) {
            tWarning(MessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Config");
            if (attemptUpdates) Config.getInstance().attemptUpdate();
            else tWarning(MessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Config");
        }

        AutoUpdater.updateBranch(Config.getInstance().getUpdateBranch());
        if (Config.getInstance().experimentalFeatures()) {
            MinecachingAPI.tInfo(MessageKeys.Plugin.EXPERIMENTAL);
        }

        tInfo(MessageKeys.Plugin.LOAD, "Localization");

        tInfo(MessageKeys.Plugin.LOAD, "Minecache Data");
        MinecacheStorage.getInstance().load();
        if (Config.getInstance().getMinecacheDataVersion() < MINECACHE_DATA_VERSION) {
            tWarning(MessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Minecache Data");
            if (attemptUpdates) MinecacheStorage.getInstance().attemptUpdate();
            else tWarning(MessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Minecache Data");
        }

        tInfo(MessageKeys.Plugin.LOAD, "Player Data");
        PlayerStorage.getInstance().load();
        if (Config.getInstance().getPlayerDataVersion() < PLAYER_DATA_VERSION) {
            tWarning(MessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Player Data");
            if (attemptUpdates) PlayerStorage.getInstance().updateData();
            else tWarning(MessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Player Data");
        }

        tInfo(MessageKeys.Plugin.LOAD, "Logbook Data");
        LogbookStorage.getInstance().load();
        if (Config.getInstance().getLogbookDataVersion() < LOGBOOK_DATA_VERSION) {
            tWarning(MessageKeys.Plugin.Data.ATTEMPTING_UPDATE, "Logbook Data");
            if (attemptUpdates) LogbookStorage.getInstance().attemptUpdate();
            else tWarning(MessageKeys.Plugin.Data.NOT_ATTEMPTING_UPDATE, "Logbook Data");
        }

        correctStats();
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
