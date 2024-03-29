package net.realdarkstudios.minecaching.api.util;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.LogCreatedEvent;
import net.realdarkstudios.minecaching.api.log.Log;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.log.LogbookDataObject;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * A collection of utility methods for various parts of Minecaching
 */
public class MCUtils {
    /**
     * The placeholder {@link UUID} in string form
     */
    public static final String EMPTY_UUID_STRING = "00000000-0000-0000-0000-000000000000";
    /**
     * The placeholder {@link UUID}
     */
    public static final UUID EMPTY_UUID = UUID.fromString(EMPTY_UUID_STRING);
    public static final NamespacedKey LINKED_CACHE_KEY = new NamespacedKey(Minecaching.getInstance(), "cache");

    /**
     * Generates a cache ID of the given length
     * @param length How long the part after "MC-" should be (standard is 5)
     * @return The randomly generated cache ID
     */
    public static String generateCacheID(int length){
        return "MC-" + generateRandomString(5);
    }

    /**
     * Generates a random string ([0-9A-Z]) of the given length
     * @param length How long the string should be
     * @return The randomly generated cache ID
     */
    public static String generateRandomString(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    /**
     * Compares a {@link HashMap}  by the given comparator
     * @param hashMap the hashmap
     * @return The sorted hashmap
     */
    public static <I, V> HashMap<I, V> sortHashMap(HashMap<I, V> hashMap, Comparator<? super Map.Entry<I, V>> comparator) {
        List<Map.Entry<I, V>> list = new LinkedList<>(hashMap.entrySet());
        list.sort(comparator);

        HashMap<I, V> sorted = new LinkedHashMap<>(hashMap.size());
        for (Map.Entry<I, V> entry: list) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }

    /**
     * Checks if a given location is invalid
     * @param sender The {@link CommandSender}
     * @param x The given X coordinate
     * @param y The given Y coordinate
     * @param z The given Z coordinate
     * @return {@code true} if invalid, {@code false} otherwise
     */
    public static boolean locationInvalid(CommandSender sender, int x, int y, int z) {
        Config cfg = Config.getInstance();
        if (x > cfg.getMaxX()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.ABOVE_COORD_LIMIT, "X", x, cfg.getMaxX());
            return true;
        }
        if (x < cfg.getMinX()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.BELOW_COORD_LIMIT, "X", x, cfg.getMaxX());
            return true;
        }
        if (y > cfg.getMaxY()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.ABOVE_COORD_LIMIT, "Y", y, cfg.getMaxY());
            return true;
        }
        if (y < cfg.getMinY()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.BELOW_COORD_LIMIT, "Y", y, cfg.getMaxY());
            return true;
        }
        if (z > cfg.getMaxZ()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.ABOVE_COORD_LIMIT, "Z", z, cfg.getMaxZ());
            return true;
        }
        if (z < cfg.getMinZ()) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.BELOW_COORD_LIMIT, "Z", z, cfg.getMaxZ());
            return true;
        }
        return false;
    }

    /**
     * Interprets a coordinate from chat input with the player's position
     * @param coord The chat input
     * @param plr The player to interpret tilda-based coordinates (ex. ~20, ~-15)
     * @param axis The axis (use for checking bounds)
     * @return The interpreted coordinate
     */
    public static int interpretCoordinate(String coord, Player plr, String axis) {
        String a = axis.toUpperCase();

        if (coord.contains("~")) {
            if (coord.length() == 1) {
                return a.equals("X") ? plr.getLocation().getBlockX() : a.equals("Y") ? plr.getLocation().getBlockY() : plr.getLocation().getBlockZ();
            } else {
                try {
                    return (a.equals("X") ? plr.getLocation().getBlockX() : a.equals("Y") ? plr.getLocation().getBlockY() : plr.getLocation().getBlockZ()) + Integer.parseInt(coord.substring(1));
                } catch (Exception e) {
                    return (a.equals("X") ? Config.getInstance().getMaxX() : a.equals("Y") ? Config.getInstance().getMaxY() : Config.getInstance().getMaxZ()) + 1;
                }
            }
        } else {
            return interpretCoordinate(coord, axis);
        }
    }

    /**
     * Interprets a coordinate from chat input without the player's position
     * @param coord The chat input
     * @param axis The axis (used for checking bounds)
     * @return The interpreted coordinate
     */
    public static int interpretCoordinate(String coord, String axis) {
        String a = axis.toUpperCase();

        try {
            return Integer.parseInt(coord);
        } catch (NumberFormatException e) {
            return (a.equals("X") ? Config.getInstance().getMaxX() : a.equals("Y") ? Config.getInstance().getMaxY() : Config.getInstance().getMaxZ()) + 1;
        }
    }

    public static String formatLocation(String beginning, Vector location) {
        return String.format("%s: (%d, %d, %d)", beginning, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Formats a location into a nicer format
     * @param beginning The beginning part, where ": " gets appended after it
     * @param location The location to format
     * @return The formatted location
     */
    public static String formatLocation(String beginning, Location location) {
        return String.format("%s: (%d, %d, %d)", beginning, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static String formatLocation(Vector location) {
        return String.format("(%d, %d, %d)", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static String formatLocation(Location location) {
        return String.format("(%d, %d, %d)", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Gets the name of a player, or [CONSOLE] if it is not a player
     * @param commandSender The {@link CommandSender} to get the name of
     * @return The command sender name
     */
    public static String commandSenderName(CommandSender commandSender) {
        return commandSender instanceof Player plr ? plr.getDisplayName() : "[CONSOLE]";
    }

    /**
     * Gets the name of a player, or [CONSOLE] if it is not a player (usually for Minecaching this will be the result of using {@link MCUtils#EMPTY_UUID})
     * @param uuid The {@link UUID} to get the name of
     * @return The UUID name
     */
    public static String uuidName(UUID uuid) {
        return uuid.equals(EMPTY_UUID) ? "[CONSOLE]" : MinecachingAPI.get().hasPlayerData(uuid) ? MinecachingAPI.get().getPlayerData(uuid).getUsername() : Bukkit.getOfflinePlayer(uuid).getName();
    }

    /**
     * Creates a log with the given type and message
     * @param plr The {@link UUID} of the player who caused the log to be created (a.k.a. the initiator)
     * @param cache The affected {@link Minecache}
     * @param logType The {@link LogType}
     * @param message The message with the log
     * @param isFTF Used for {@link net.realdarkstudios.minecaching.commands.LogCacheCommand}, {@code true} if this was the result of being found first
     * @return The newly-created log
     */
    public static Log createLog(UUID plr, Minecache cache, LogType logType, String message, boolean isFTF) {
        LogbookDataObject logbook = MinecachingAPI.get().getLogbook(cache.id());
        Log log = logbook.createLog(plr, logType, message, isFTF);

        LogCreatedEvent logEvent = new LogCreatedEvent(cache, log.logId(), log.type(), plr);
        Bukkit.getPluginManager().callEvent(logEvent);

        return log;
    }
}
