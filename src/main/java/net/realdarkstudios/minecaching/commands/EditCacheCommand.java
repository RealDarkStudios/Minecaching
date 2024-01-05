package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class EditCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid;
        if (!(sender instanceof Player plr)) {
            uuid = Utils.EMPTY_UUID;
        } else {
            uuid = plr.getUniqueId();
        }

        PlayerDataObject plrdata = MinecachingAPI.get().getPlayerData(uuid);

        Minecache cache = plrdata.getEditingCache();
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Incorrect Usage!");
            sender.sendMessage(ChatColor.RED + "/editcache <id>");
            return true;
        } else if (args.length == 1 && (cache == null || cache.id().equals("NULL"))) {
            String id = args[0];

            if (MinecachingAPI.get().getMinecache(id).equals(Minecache.EMPTY)) {
                sender.sendMessage(ChatColor.RED + "Did not find minecache with ID " + id);
                return true;
            }

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Editing " + id);
            plrdata.setEditingCache(MinecachingAPI.get().getMinecache(id));
            return true;
        } else if (args.length == 1 && args[0].startsWith("MC-") && !cache.id().equals("NULL")) {
            sender.sendMessage(ChatColor.RED + "You are already editing " + plrdata.getEditingCache().id());
            return true;
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
                    sender.sendMessage(ChatColor.RED + "/editcache name <name>");
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
                    sender.sendMessage(ChatColor.RED + "/editcache lodecoords OR /editcache lodecoords <x> <y> <z>");
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
                    sender.sendMessage(ChatColor.RED + "/editcache coords OR /editcache coords <x> <y> <z>");
                    return true;
                }

                if (!Utils.validateLocation(sender, x, y, z)) return true;

                cache.setLocation(new Location(cache.world(), x, y, z));
                sender.sendMessage(String.format("%sSet coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z));

            }
            case "code" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Incorrect Usage!");
                    sender.sendMessage(ChatColor.RED + "/editcache code <code>");
                    return true;
                } else {
                    cache.setCode(args[1].trim());
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Set code to \"" + cache.code() + "\"");
                }
            }
            case "save" -> {
                if (cache.name() == null) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have a name!");
                    sender.sendMessage(ChatColor.RED + "/editcache name");
                } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have the coordinates!");
                    sender.sendMessage(ChatColor.RED + "/addcache coords");
                } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
                    sender.sendMessage(ChatColor.RED + "This cache doesn't have the lodestone coordinates!");
                    sender.sendMessage(ChatColor.RED + "/addcache lodecoords");
                } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                    sender.sendMessage(ChatColor.RED + "The lodestone coordinates are too far away! They must be under " + Config.getInstance().getMaxLodestoneDistance() + " blocks away!");
                } else if (cache.code() == null) {
                    sender.sendMessage(ChatColor.RED + "You must set a code!");
                    sender.sendMessage(ChatColor.RED + "/editcache code");
                }  else {
                    MinecachingAPI.get().saveMinecache(cache, false);
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

        PlayerDataObject plrdata = MinecachingAPI.get().getPlayerData(uuid);

        if (args.length == 0 && (plrdata.getEditingCache() == null || plrdata.getEditingCache().id().equals("NULL"))) return MinecachingAPI.get().getAllKnownCacheIDs();
        else if (args.length == 1 && (plrdata.getEditingCache() == null || plrdata.getEditingCache().id().equals("NULL"))) {

            ArrayList<String> toReturn = new ArrayList<>();

            for (String id : MinecachingAPI.get().getAllKnownCacheIDs()) {
                if ((plrdata.getEditingCache() != null || !plrdata.getEditingCache().id().equals("NULL")) && id.contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && MinecachingAPI.get().getMinecache(id).author().equals(plr.getUniqueId())))) {
                    toReturn.add(id);
                }
            }

            return toReturn;
        } else {
            boolean isPlr = sender instanceof Player;

            Block target = isPlr ? ((Player) sender).getTargetBlock(null, 5) : null;

            return switch (args.length) {
                case 1 -> plrdata.getEditingCache() == null || plrdata.getEditingCache().id().equals("NULL") ? List.of() : Stream.of("cancel", "code", "name", "lodecoords", "coords", "save").filter(s -> s.contains(args[0])).toList();
                case 2 -> args[0].equals("lodecoords") || args[0].equals("coords") && isPlr ? List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ())) : List.of();
                case 3 -> args[0].equals("lodecoords") || args[0].equals("coords") && isPlr ? List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ())) : List.of();
                case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") && isPlr ? List.of("~", target.getZ() + "") : List.of();
                default -> List.of();
            };
        }
    }
}
