package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.MinecacheCreatedEvent;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.Bukkit;
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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            MCPluginMessages.sendErrorMsg(sender, "execute.console");
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        Minecache cache = pdo.getCache();
        if (args.length < 1 && (cache != null && !cache.id().equals("NULL"))) {
            MCPluginMessages.sendErrorMsg(sender, "addcache.alreadycreating");
            return true;
        } else if (args.length < 1) {
            MCPluginMessages.sendMsg(sender, "addcache.create", ChatColor.LIGHT_PURPLE);
            pdo.setCache(Minecache.EMPTY.setID(Utils.generateID(5)));
            return true;
        }

        if (cache == null || cache.id().equals("NULL")) {
            MCPluginMessages.sendErrorMsg(sender, "addcache.nocache", label);
            MCPluginMessages.usage(sender, "addcache", command, label);
            return true;
        }

        String subCommand = args[0];

        switch (subCommand) {
            case "cancel" -> {
                MCPluginMessages.sendMsg(sender, "addcache.cancel", ChatColor.LIGHT_PURPLE, cache.id());
                cache = Minecache.EMPTY;
                cache.setID("NULL");
            }
            case "type" -> {
                if (args.length < 2) {
                    MCPluginMessages.incorrectUsage(sender);
                    MCPluginMessages.usage(sender, "addcache.type", command, label);
                    return true;
                }
                MinecacheType type = MinecacheType.get(args[1].toUpperCase());
                if (type.equals(MinecacheType.INVALID)) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.invalidtype");
                    return true;
                } else cache.setType(type);
                MCPluginMessages.sendMsg(sender, "addcache.type", ChatColor.LIGHT_PURPLE, args[1]);
            }
            case "name" -> {
                if (args.length < 2) {
                    MCPluginMessages.incorrectUsage(sender);
                    MCPluginMessages.usage(sender, "addcache.name", command, label);
                    return true;
                } else {
                    StringBuilder name = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        name.append(args[i]).append(" ");
                    }
                    cache.setName(name.toString().trim());
                    MCPluginMessages.sendMsg(sender, "addcache.name", ChatColor.LIGHT_PURPLE, cache.name());
                }
            }
            case "lodecoords" -> {
                int x, y, z;
                if (args.length == 1) {
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], plr, "x");
                    y = Utils.validateCoordinate(args[2], plr, "y");
                    z = Utils.validateCoordinate(args[3], plr, "z");}
                else {
                    MCPluginMessages.incorrectUsage(sender, "argcount", command);
                    MCPluginMessages.usage(sender, "addcache.lodecoords", command, label, label);
                    return true;
                }

                if (!Utils.validateLocation(plr, x, y, z)) return true;

                cache.setLodeLocation(new Location(plr.getWorld(), x, y, z));
                MCPluginMessages.sendMsg(sender, "addcache.lodecoords", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z);
            }
            case "coords" -> {
                int x, y, z;
                if (args.length == 1) {
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], plr, "x");
                    y = Utils.validateCoordinate(args[2], plr, "y");
                    z = Utils.validateCoordinate(args[3], plr, "z");}
                else {
                    MCPluginMessages.incorrectUsage(sender, "argcount");
                    MCPluginMessages.usage(sender, "addcache.coords", command, label, label);
                    return true;
                }

                if (!Utils.validateLocation(plr, x, y, z)) return true;

                cache.setLocation(new Location(plr.getWorld(), x, y, z));
                MCPluginMessages.sendMsg(sender, "addcache.coords", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z);
            }
            case "code" -> {
                if (args.length < 2) {
                    MCPluginMessages.incorrectUsage(sender);
                    MCPluginMessages.usage(sender, "addcache.code", command, label);
                    return true;
                } else {
                    cache.setCode(args[1].trim());
                    MCPluginMessages.sendMsg(sender, "addcache.code", ChatColor.LIGHT_PURPLE, cache.code());
                }
            }
            case "save" -> {
                if (cache.name() == null) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.noname");
                    MCPluginMessages.usage(sender, "addcache.name", command, label);
                } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.nocoords");
                    MCPluginMessages.usage(sender, "addcache.coords", command, label);
                } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.nolodecoords");
                    MCPluginMessages.usage(sender, "addcache.lodecoords", command, label);
                } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.lodetoofar");
                } else if (cache.code() == null || cache.code().isEmpty()) {
                    MCPluginMessages.sendErrorMsg(sender, "addcache.nocode");
                    MCPluginMessages.usage(sender, "addcache.code", command, label);
                } else {
                    cache.setStatus(MinecacheStatus.NEEDS_REVIEWED).setAuthor(plr.getUniqueId()).setBlockType(cache.lodeLocation().getBlock().getType()).setHidden(LocalDateTime.now()).setFTF(Utils.EMPTY_UUID);

                    MinecacheCreatedEvent event = new MinecacheCreatedEvent(cache, plr);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        MCPluginMessages.sendErrorMsg(sender, "addcache");
                    }

                    MinecachingAPI.get().saveMinecache(cache, true);

                    pdo.addHide(cache.id());
                    pdo.setCache(Minecache.EMPTY.setID("NULL"));

                    MinecachingAPI.get().save();
                    MinecachingAPI.get().update();

                    MCPluginMessages.sendMsg(sender, "addcache.save", ChatColor.LIGHT_PURPLE, cache.id(), cache.name());

                    return true;
                }
            }
            case "data" -> sender.sendMessage(String.format("ID: %s, Name: %s, Type: %s, Lode Coords: (%d, %d, %d), Cache Coords: (%d, %d, %d), Code: %s", cache.id(), cache.name(), cache.type().getId(), cache.lx(), cache.ly(), cache.lz(), cache.x(), cache.y(), cache.z(), cache.code()));
        }

        pdo.setCache(cache);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            return new ArrayList<>();
        }

        Block target = plr.getTargetBlock(null, 5);
        PlayerDataObject plrdata = MinecachingAPI.get().getPlayerData(plr);

        return switch (args.length) {
            case 1 -> plrdata.getCache() == null || plrdata.getCache().id().equals("NULL") ? List.of() : Stream.of("cancel", "code", "name", "lodecoords", "coords", "save", "data", "type").filter(s -> s.contains(args[0])).toList();
            case 2 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ())) : args[0].equals("type") ? List.of("Traditional", "Multi", "Mystery") : List.of();
            case 3 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ())) : List.of();
            case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", target.getZ() + "") : List.of();
            default -> List.of();
        };
    }


}
