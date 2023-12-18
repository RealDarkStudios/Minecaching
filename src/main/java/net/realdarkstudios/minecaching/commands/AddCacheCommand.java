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

        if (!PlayerStorage.getInstance().hasPlayerData(plr)) {
            PlayerStorage.getInstance().createPlayerData(plr);
        }

        Minecache cache = PlayerStorage.getInstance().getPlayerData(plr).getCache();
        if (args.length < 1 && (cache != null && !cache.id().equals("NULL"))) {
            plr.sendMessage(ChatColor.RED + "You are already creating a cache!");
            return true;
        } else if (args.length < 1) {
            plr.sendMessage(ChatColor.LIGHT_PURPLE + "Starting creation of new cache...");
            PlayerStorage.getInstance().getPlayerData(plr).setCache(Minecache.EMPTY.setID(Utils.generateID(5)));
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
                cache = Minecache.EMPTY;
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
                    PlayerStorage.getInstance().getPlayerData(plr).addHide(cache.id());
                    cache = Minecache.EMPTY;
                    PlayerStorage.getInstance().getPlayerData(plr).setCacheId("NULL");
                }
            }
            case "data" -> sender.sendMessage(String.format("ID: %s, Name: %s, Type: %s, Lode Coords: (%d, %d, %d), Cache Coords: (%d, %d, %d)", cache.id(), cache.name(), cache.type().getId(), cache.lx(), cache.ly(), cache.lz(), cache.x(), cache.y(), cache.z()));
        }

        PlayerStorage.getInstance().getPlayerData(plr).setCache(cache);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            return new ArrayList<>();
        }

        Block target = plr.getTargetBlock(null, 5);

        return switch (args.length) {
            case 1 -> PlayerStorage.getInstance().getPlayerData(plr.getUniqueId()).getCache() == null ? List.of() : Stream.of("cancel", "name", "lodecoords", "coords", "save", "data", "type").filter(s -> s.contains(args[0])).toList();
            case 2 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ())) : args[0].equals("type") ? List.of("Traditional", "Multi", "Mystery") : List.of();
            case 3 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ())) : List.of();
            case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", target.getZ() + "") : List.of();
            default -> List.of();
        };
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
}
