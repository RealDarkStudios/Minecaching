package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.data.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AddCacheCommand implements CommandExecutor, TabExecutor {
    // addcache <x> <y> <z> <name>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            sender.sendMessage(ChatColor.RED + "You must execute this command as a player!");
            return true;
        }

        Minecache cache = PlayerStorage.getInstance().getTempMinecache(plr.getUniqueId());
        if (args.length < 1 && cache != null) {
            plr.sendMessage(ChatColor.RED + "You are already creating a cache!");
            return true;
        } else if (args.length < 1) {
            cache = Minecache.EMPTY;
            cache.setID(Utils.generateID(5));
            plr.sendMessage(ChatColor.LIGHT_PURPLE + "Starting creation of new cache...");
            PlayerStorage.getInstance().setTempMinecache(plr.getUniqueId(), cache);
            return true;
        }

        if (cache == null) {
            plr.sendMessage(ChatColor.RED + "You haven't started making a cache yet!");
            plr.sendMessage(ChatColor.RED + "/addcache");
            return true;
        }

        String subCommand = args[0];


        switch (subCommand) {
            case "cancel" -> {
                cache = null;
                plr.sendMessage(ChatColor.RED + "Cancelled the creation of this minecache!");
            }
            case "name" -> {
                if (args.length < 2) {
                    plr.sendMessage(ChatColor.RED + "Incorrect Usage!");
                    plr.sendMessage(ChatColor.RED + "/addcache name <name>");
                    return true;
                } else {
                    StringBuilder name = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        name.append(args[i]).append(" ");
                    }
                    cache.setName(name.toString().trim());
                    plr.sendMessage(ChatColor.LIGHT_PURPLE + "Set name of " + cache.id() + " to \"" + cache.name() + "\"");
                }
            }
            case "lodecoords" -> {
                cache.setLodeLocation(plr.getLocation());
                plr.sendMessage(String.format("%sSet lodestone coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, plr.getLocation().getWorld().getName(), plr.getLocation().getBlockX(), plr.getLocation().getBlockY(), plr.getLocation().getBlockZ()));
            }
            case "coords" -> {
                cache.setLocation(plr.getLocation());
                plr.sendMessage(String.format("%sSet cache coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, plr.getLocation().getWorld().getName(), plr.getLocation().getBlockX(), plr.getLocation().getBlockY(), plr.getLocation().getBlockZ()));
            }
            case "save" -> {
                if (cache.name() == null) {
                    plr.sendMessage(ChatColor.RED + "No name has been given to this cache!");
                    plr.sendMessage(ChatColor.RED + "/addcache name");
                } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                    plr.sendMessage(ChatColor.RED + "You need to set the coordinates where the cache is at!");
                    plr.sendMessage(ChatColor.RED + "/addcache coords");
                } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
                    plr.sendMessage(ChatColor.RED + "You need to set the coordinates where the lodestone is at!");
                    plr.sendMessage(ChatColor.RED + "/addcache lodecoords");
                } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                    plr.sendMessage(ChatColor.RED + "The lodestone coordinates are too far away! They must be under " + Config.getInstance().getMaxLodestoneDistance() + "blocks away!");
                } else {
                    cache.setStatus(MinecacheStatus.NEEDS_REVIEWED).setAuthor(plr.getUniqueId()).setType(MinecacheType.TRADITIONAL).setBlockType(cache.lodeLocation().getBlock().getType()).setHidden(LocalDateTime.now()).setFTF(Utils.EMPTY_UUID);
                    MinecacheStorage.getInstance().saveMinecache(cache, true);
                    PlayerStorage.getInstance().setTempMinecache(plr.getUniqueId(), null);
                    plr.sendMessage(ChatColor.LIGHT_PURPLE + "Created " + cache.id() + ": " + cache.name());
                    PlayerStorage.getInstance().setPlayerHides(plr, PlayerStorage.getInstance().getPlayerHides(plr) + 1);
                    return true;
                }
            }
            case "data" -> sender.sendMessage(String.format("ID: %s, Name: %s, Lode Coords: (%d, %d, %d), Cache Coords: (%d, %d, %d)", cache.id(), cache.name(), cache.lx(), cache.ly(), cache.lz(), cache.x(), cache.y(), cache.z()));
        }

        PlayerStorage.getInstance().setTempMinecache(plr.getUniqueId(), cache);
        return true;

//        String cID = Utils.generateID(5), cName;
//        UUID cAuthor = plr.getUniqueId();
//        int x, y, z;
//
//        if (args[0].contains("~")) {
//            if (args[0].length() == 1) {
//                x = plr.getLocation().getBlockX();
//            } else {
//                try {
//                    x = plr.getLocation().getBlockX() + Integer.parseInt(args[0].substring(1));
//                } catch (Exception e) {
//                    sender.sendMessage(ChatColor.RED + "X coordinate is invalid!");
//                    return false;
//                }
//            }
//        } else {
//            try {
//                x = Integer.parseInt(args[0]);
//            } catch (NumberFormatException e) {
//                sender.sendMessage(ChatColor.RED + "X coordinate is invalid!");
//                return false;
//            }
//        }
//
//        if (args[1].contains("~")) {
//            if (args[1].length() == 1) {
//                y = plr.getLocation().getBlockY();
//            } else {
//                try {
//                    y = plr.getLocation().getBlockY() + Integer.parseInt(args[1].substring(1));
//                } catch (Exception e) {
//                    sender.sendMessage(ChatColor.RED + "Y coordinate is invalid!");
//                    return false;
//                }
//            }
//        } else {
//            try {
//                y = Integer.parseInt(args[1]);
//            } catch (NumberFormatException e) {
//                sender.sendMessage(ChatColor.RED + "Y coordinate is invalid!");
//                return false;
//            }
//        }
//
//        if (args[2].contains("~")) {
//            if (args[2].length() == 1) {
//                z = plr.getLocation().getBlockZ();
//            } else {
//                try {
//                    z = plr.getLocation().getBlockZ() + Integer.parseInt(args[2].substring(1));
//                } catch (Exception e) {
//                    sender.sendMessage(ChatColor.RED + "Z coordinate is invalid!");
//                    return false;
//                }
//            }
//        } else {
//            try {
//                z = Integer.parseInt(args[2]);
//            } catch (NumberFormatException e) {
//                sender.sendMessage(ChatColor.RED + "Z coordinate is invalid!");
//                return false;
//            }
//        }
//
//        String stype = args[3];
//        MinecacheType type = MinecacheType.get(stype.toUpperCase());
//
//        Config cfg = Config.getInstance();
//        if (type.equals(MinecacheType.INVALID)) {
//            sender.sendMessage(String.format("%s\"%s\" is not a valid type!", ChatColor.RED, stype));
//            return true;
//        } else if (!cfg.getEnabledTypes().contains(type.getId())) {
//            sender.sendMessage(String.format("%sThis server has disabled the %s cache type!", ChatColor.RED, stype));
//        }
//
//        if (x > cfg.getMaxX() || x < cfg.getMinX() || y > cfg.getMaxY() || y < cfg.getMinY() || z > cfg.getMaxZ() || z < cfg.getMinZ()) {
//            sender.sendMessage(ChatColor.RED + "This minecache is outside the boundaries! Please contact and administrator if you think this is incorrect!");
//            return true;
//        }
//
//        StringBuilder name = new StringBuilder();
//        for (int i = 4; i < args.length; i++) {
//            name.append(args[i]).append(" ");
//        }
//
//        cName = name.toString().trim();
//
//        World world = plr.getWorld();
//        Location cacheLocation = new Location(world, x, y, z);
//        Material containerType = args[5] == null ? Material.BARREL : Material.getMaterial(args[5]);
//
//        sender.sendMessage(String.format("%Created %s Minecache \"%s\" with ID %s at (%d, %d, %d) by %s", ChatColor.GREEN, stype, cName, cID, x, y, z, plr.getDisplayName()));
//
//        Minecache newCache = new Minecache(cID, type, cName, cAuthor, world, x, y, z, Optional.empty(), MinecacheStatus.NEEDS_REVIEWED, LocalDateTime.now(), cacheLocation.getBlock().getType(), containerType, 0, false);
//        MinecacheStorage.getInstance().saveMinecache(newCache, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            return new ArrayList<>();
        }

        Block target = plr.getTargetBlock(null, 5);

        return switch (args.length) {
            case 1 -> List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ()));
            case 2 -> List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ()));
            case 3 -> List.of("~", target.getZ() + "");
            case 4 -> Stream.of("Traditional", "Multi", "Mystery").filter((s -> s.contains(args[3]))).toList();
            case 5 -> List.of("BARREL", "SHULKER_BOX");
            default -> List.of();
        };
    }
}
