package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.LogCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class Utils {
    public static final String EMPTY_UUID_STRING = "00000000-0000-0000-0000-000000000000";
    public static final UUID EMPTY_UUID = UUID.fromString(EMPTY_UUID_STRING);

    public static String generateID(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder("MC-");
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    public static String generateRandomString(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    public static HashMap<String, Minecache> sortByMinecache(HashMap<String, Minecache> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Minecache> > list = new LinkedList<>(hm.entrySet());

        // Sort the list
        list.sort(Comparator.comparing(m -> (m.getValue().hidden())));

        // put data from sorted list to hashmap
        HashMap<String, Minecache> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Minecache> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static boolean validateLocation(CommandSender sender, int x, int y, int z) {
        Config cfg = Config.getInstance();
        if (x > cfg.getMaxX()) {
            sender.sendMessage(ChatColor.RED + "X coordinate is above the allowed limit of " + cfg.getMaxX());
            return false;
        }
        if (x < cfg.getMinX()) {
            sender.sendMessage(ChatColor.RED + "X coordinate is below the allowed limit of " + cfg.getMinX());
            return false;
        }
        if (y > cfg.getMaxY()) {
            sender.sendMessage(ChatColor.RED + "Y coordinate is above the allowed limit of " + cfg.getMaxY());
            return false;
        }
        if (y < cfg.getMinY()) {
            sender.sendMessage(ChatColor.RED + "Y coordinate is below the allowed limit of " + cfg.getMinY());
            return false;
        }
        if (z > cfg.getMaxZ()) {
            sender.sendMessage(ChatColor.RED + "Z coordinate is above the allowed limit of " + cfg.getMaxZ());
            return false;
        }
        if (z < cfg.getMinZ()) {
            sender.sendMessage(ChatColor.RED + "Z coordinate is below the allowed limit of " + cfg.getMinZ());
            return false;
        }
        return true;
    }

    public static int validateCoordinate(String coord, Player plr, String axis) {
        String a = axis.toUpperCase();

        if (coord.contains("~")) {
            if (coord.length() == 1) {
                return plr.getLocation().getBlockZ();
            } else {
                try {
                    return plr.getLocation().getBlockZ() + Integer.parseInt(coord.substring(1));
                } catch (Exception e) {
                    return (a.equals("X") ? Config.getInstance().getMaxX() : a.equals("Y") ? Config.getInstance().getMaxY() : Config.getInstance().getMaxZ()) + 1;
                }
            }
        } else {
            try {
                return Integer.parseInt(coord);
            } catch (NumberFormatException e) {
                return (a.equals("X") ? Config.getInstance().getMaxX() : a.equals("Y") ? Config.getInstance().getMaxY() : Config.getInstance().getMaxZ()) + 1;
            }
        }
    }

    public static int validateCoordinate(String coord, String axis) {
        String a = axis.toUpperCase();

        try {
            return Integer.parseInt(coord);
        } catch (NumberFormatException e) {
            return (a.equals("X") ? Config.getInstance().getMaxX() : a.equals("Y") ? Config.getInstance().getMaxY() : Config.getInstance().getMaxZ()) + 1;
        }
    }

    public static String commandSenderName(CommandSender commandSender) {
        return commandSender instanceof Player plr ? plr.getDisplayName() : "[CONSOLE]";
    }

    public static String uuidName(UUID uuid) {
        return uuid.equals(EMPTY_UUID) ? "[CONSOLE]" : Bukkit.getOfflinePlayer(uuid).getName();
    }

    public static void sendPlrMessage(Player plr, String... message) {
        sendPlrMessage(plr, ChatColor.WHITE, message);
    }

    public static void sendPlrErrorMessage(Player plr, String... message) {
        sendPlrMessage(plr, ChatColor.RED, message);
    }

    public static void sendPlrMessage(Player plr, ChatColor color, String... message) {
        ArrayList<String> msg = new ArrayList<>();

        msg.add(color.toString());
        msg.addAll(Arrays.stream(message).toList());

        plr.sendMessage(msg.toArray(new String[]{}));
    }

    public static Log createLog(Player plr, Minecache cache, LogType logType, String message, boolean isFTF) {
        LogbookDataObject logbook = MinecachingAPI.get().getLogbook(cache.id());
        Log log = logbook.createLog(plr, logType, message, isFTF);

        LogCreatedEvent logEvent = new LogCreatedEvent(cache, log.logId(), log.type(), plr);
        Bukkit.getPluginManager().callEvent(logEvent);

        return log;
    }

    public static Log createLog(UUID plr, Minecache cache, LogType logType, String message, boolean isFTF) {
        LogbookDataObject logbook = MinecachingAPI.get().getLogbook(cache.id());
        Log log = logbook.createLog(plr, logType, message, isFTF);

        LogCreatedEvent logEvent = new LogCreatedEvent(cache, log.logId(), log.type(), plr);
        Bukkit.getPluginManager().callEvent(logEvent);

        return log;
    }
}
