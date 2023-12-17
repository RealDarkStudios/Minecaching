package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.data.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
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
            plr.sendMessage(ChatColor.LIGHT_PURPLE + "Starting creation of new cache...");
            PlayerStorage.getInstance().setTempMinecache(plr.getUniqueId(), Minecache.EMPTY.setID(Utils.generateID(5)));
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
            case "type" -> {
                if (args.length < 2) {
                    plr.sendMessage(ChatColor.RED + "Incorrect Usage!");
                    plr.sendMessage(ChatColor.RED + "/addcache type <type>");
                }
                MinecacheType type = MinecacheType.get(args[1].toUpperCase());
                if (type.equals(MinecacheType.INVALID)) {
                    plr.sendMessage(ChatColor.RED + "Invalid Minecache Type");
                    return true;
                } else cache.setType(type);
                plr.sendMessage(ChatColor.LIGHT_PURPLE + "Set type to " + args[1]);
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
                int x, y, z;
                if (args.length == 1) {
                    x = validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "lx");
                    y = validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "ly");
                    z = validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "lz");
                } else if (args.length == 4) {
                    x = validateCoordinate(args[1], plr, "lx");
                    y = validateCoordinate(args[2], plr, "ly");
                    z = validateCoordinate(args[3], plr, "lz");}
                else {
                    plr.sendMessage(ChatColor.RED + "Invalid number of arguments!");
                    plr.sendMessage(ChatColor.RED + "/addcache lodecoords OR /addcache lodecoords <x> <y> <z>");
                    return true;
                }

                if (!validateLocation(plr, x, y, z)) return true;

                cache.setLodeLocation(new Location(plr.getWorld(), x, y, z));
                plr.sendMessage(String.format("%sSet lodestone coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, plr.getLocation().getWorld().getName(), x, y, z));
            }
            case "coords" -> {
                int x, y, z;
                if (args.length == 1) {
                    x = validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4) {
                    x = validateCoordinate(args[1], plr, "x");
                    y = validateCoordinate(args[2], plr, "y");
                    z = validateCoordinate(args[3], plr, "z");}
                else {
                    plr.sendMessage(ChatColor.RED + "Invalid number of arguments!");
                    plr.sendMessage(ChatColor.RED + "/addcache coords OR /addcache coords <x> <y> <z>");
                    return true;
                }

                if (!validateLocation(plr, x, y, z)) return true;

                cache.setLocation(new Location(plr.getWorld(), x, y, z));
                plr.sendMessage(String.format("%sSet coords to [%s](%d, %d, %d)", ChatColor.LIGHT_PURPLE, plr.getLocation().getWorld().getName(), x, y, z));

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
                    plr.sendMessage(ChatColor.RED + "The lodestone coordinates are too far away! They must be under " + Config.getInstance().getMaxLodestoneDistance() + " blocks away!");
                } else {
                    cache.setStatus(MinecacheStatus.NEEDS_REVIEWED).setAuthor(plr.getUniqueId()).setBlockType(cache.lodeLocation().getBlock().getType()).setHidden(LocalDateTime.now()).setFTF(Utils.EMPTY_UUID);
                    MinecacheStorage.getInstance().saveMinecache(cache, true);
                    plr.sendMessage(ChatColor.LIGHT_PURPLE + "Created " + cache.id() + ": " + cache.name());
                    PlayerStorage.getInstance().setPlayerHides(plr, PlayerStorage.getInstance().getPlayerHides(plr) + 1);
                    cache = null;
                }
            }
            case "data" -> sender.sendMessage(String.format("ID: %s, Name: %s, Type: %s, Lode Coords: (%d, %d, %d), Cache Coords: (%d, %d, %d)", cache.id(), cache.name(), cache.type().getId(), cache.lx(), cache.ly(), cache.lz(), cache.x(), cache.y(), cache.z()));
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

    private boolean validateLocation(Player plr, int x, int y, int z) {
        Config cfg = Config.getInstance();
        if (x > cfg.getMaxX()) {
            plr.sendMessage(ChatColor.RED + "X coordinate is above the allowed limit of " + cfg.getMaxX());
            return false;
        }
        if (x < cfg.getMinX()) {
            plr.sendMessage(ChatColor.RED + "X coordinate is below the allowed limit of " + cfg.getMinX());
            return false;
        }
        if (y > cfg.getMaxY()) {
            plr.sendMessage(ChatColor.RED + "Y coordinate is above the allowed limit of " + cfg.getMaxY());
            return false;
        }
        if (y < cfg.getMinY()) {
            plr.sendMessage(ChatColor.RED + "Y coordinate is below the allowed limit of " + cfg.getMinY());
            return false;
        }
        if (z > cfg.getMaxZ()) {
            plr.sendMessage(ChatColor.RED + "Z coordinate is above the allowed limit of " + cfg.getMaxZ());
            return false;
        }
        if (z < cfg.getMinZ()) {
            plr.sendMessage(ChatColor.RED + "Z coordinate is below the allowed limit of " + cfg.getMinZ());
            return false;
        }
        return true;
    }

    private int validateCoordinate(String coord, Player plr, String axis) {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            return new ArrayList<>();
        }

        Block target = plr.getTargetBlock(null, 5);

//        if (PlayerStorage.getInstance().getTempMinecache(plr.getUniqueId()) == null && args.length == 0) return List.of();
//        else if (args.length == 0) return List.of("cancel", "name", "lodecoords", "coords", "save", "data");
//        else if (args.length == 1 && (args[0].equals("lodecoords") || args[0].equals("coords"))) return List.of("~", "~ ~", "~ ~ ~", target.getX() + "");
//        else return List.of();

        return switch (args.length) {
            case 1 -> PlayerStorage.getInstance().getTempMinecache(plr.getUniqueId()) == null ? List.of() : Stream.of("cancel", "name", "lodecoords", "coords", "save", "data", "type").filter(s -> s.contains(args[0])).toList();
            case 2 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ())) : args[0].equals("type") ? List.of("Traditional", "Multi", "Mystery") : List.of();
            case 3 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ())) : List.of();
            case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", target.getZ() + "") : List.of();
            // case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") ? Stream.of("Traditional", "Multi", "Mystery").filter((s -> s.contains(args[3]))).toList();
            // case 5 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("BARREL", "SHULKER_BOX");
            default -> List.of();
        };
    }
}
