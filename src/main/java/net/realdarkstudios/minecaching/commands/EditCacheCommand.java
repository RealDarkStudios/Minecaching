package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.data.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid;
        if (!(sender instanceof Player plr)) {
            uuid = Utils.EMPTY_UUID;
        } else {
            uuid = plr.getUniqueId();
        }

        PlayerStorageObject plrdata = PlayerStorage.getInstance().getOrCreatePlayerData(uuid);

        Minecache cache = plrdata.getEditingCache();
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Incorrect Usage!");
            sender.sendMessage(ChatColor.RED + "/editcache <id>");
            return false;
        } else if (args.length == 1 && (cache == null || cache.id().equals("NULL"))) {
            String id = args[0];
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Editing " + id);

            if (MinecacheStorage.getInstance().getMinecacheByID(id).equals(Minecache.EMPTY)) {
                sender.sendMessage(ChatColor.RED + "Did not find minecache with ID " + id);
                return true;
            }
            
            plrdata.setEditingCache(MinecacheStorage.getInstance().getMinecacheByID(id));
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "You are already editing " + plrdata.getEditingCache().id());
        }
        
        String subCommand = args[0];
        
        switch (subCommand) {
            case "cancel" -> {
                sender.sendMessage(ChatColor.RED + "Cancelled editing " + cache.id());
                cache = Minecache.EMPTY;
                cache.setID("NULL");
            }
            case "name" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Incorrect Usage!");
                    sender.sendMessage(ChatColor.RED + "/addcache name <name>");
                    return true;
                } else {
                    StringBuilder name = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        name.append(args[i]).append(" ");
                    }
                    cache.setName(name.toString().trim());
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Set name of " + cache.id() + " to \"" + cache.name() + "\"");
                }
            }
            case "lodecoords" -> {
                int x, y, z;
                if (args.length == 1 && sender instanceof Player plr) {
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "lx");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "ly");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "lz");
                } else if (args.length == 4 && sender instanceof Player plr) {
                    x = Utils.validateCoordinate(args[1], plr, "lx");
                    y = Utils.validateCoordinate(args[2], plr, "ly");
                    z = Utils.validateCoordinate(args[3], plr, "lz");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], "lx");
                    y = Utils.validateCoordinate(args[2], "ly");
                    z = Utils.validateCoordinate(args[3], "lz");
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid number of arguments!");
                    sender.sendMessage(ChatColor.RED + "/addcache lodecoords OR /addcache lodecoords <x> <y> <z>");
                    return true;
                }

                if (!Utils.validateLocation(sender, x, y, z)) return true;

                cache.setLodeLocation(new Location(cache.world(), x, y, z));
                sender.sendMessage(String.format("%sSet lodestone coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z));
            }
            case "coords" -> {
                int x, y, z;
                if (args.length == 1&& sender instanceof Player plr) {
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4&& sender instanceof Player plr) {
                    x = Utils.validateCoordinate(args[1], plr, "x");
                    y = Utils.validateCoordinate(args[2], plr, "y");
                    z = Utils.validateCoordinate(args[3], plr, "z");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], "x");
                    y = Utils.validateCoordinate(args[2], "y");
                    z = Utils.validateCoordinate(args[3], "z");
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid number of arguments!");
                    sender.sendMessage(ChatColor.RED + "/addcache coords OR /addcache coords <x> <y> <z>");
                    return true;
                }

                if (!Utils.validateLocation(sender, x, y, z)) return true;

                cache.setLocation(new Location(cache.world(), x, y, z));
                sender.sendMessage(String.format("%sSet coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z));

            }
            case "save" -> {
                if (cache.name() == null) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have a name!");
                    sender.sendMessage(ChatColor.RED + "/addcache name");
                } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have the coordinates!");
                    sender.sendMessage(ChatColor.RED + "/addcache coords");
                } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have the lodestone coordinates!");
                    sender.sendMessage(ChatColor.RED + "/addcache lodecoords");
                } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                    sender.sendMessage(ChatColor.RED + "The lodestone coordinates are too far away! They must be under " + Config.getInstance().getMaxLodestoneDistance() + " blocks away!");
                } else {
                    MinecacheStorage.getInstance().saveMinecache(cache, false);
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Saved " + cache.id() + ": " + cache.name());
                    cache = Minecache.EMPTY;
                    cache.setID("NULL");
                }
            }
        }

        plrdata.setEditingCache(cache);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid;
        if (!(sender instanceof Player plr)) {
            uuid = Utils.EMPTY_UUID;
        } else {
            uuid = plr.getUniqueId();
        }

        PlayerStorageObject plrdata = PlayerStorage.getInstance().getOrCreatePlayerData(uuid);

        if (args.length == 0 && (plrdata.getEditingCache() == null || plrdata.getEditingCache().id().equals("NULL"))) return MinecacheStorage.getInstance().getIDArray();
        else if (args.length > 1) return List.of();

        ArrayList<String> toReturn = new ArrayList<>();

        for (String id: MinecacheStorage.getInstance().getIDArray()) {
            if ((plrdata.getEditingCache() != null || !plrdata.getEditingCache().id().equals("NULL")) && id.contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && MinecacheStorage.getInstance().getMinecacheByID(id).author().equals(plr.getUniqueId())))) {
                toReturn.add(id);
            }
        }

        return toReturn;
    }
}
