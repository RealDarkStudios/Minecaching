package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.MinecacheEditedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
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
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "editcache", command, label);
            return true;
        } else if (args.length == 1 && (cache == null || cache.id().equals("NULL"))) {
            String id = args[0];

            if (MinecachingAPI.get().getMinecache(id).equals(Minecache.EMPTY)) {
                MCMessages.sendErrorMsg(sender, "cantfind", id);
                return true;
            }

            MCMessages.sendMsg(sender, "editcache.edit", ChatColor.LIGHT_PURPLE, id);
            plrdata.setEditingCache(MinecachingAPI.get().getMinecache(id));
            return true;
        } else if (args.length == 1 && args[0].startsWith("MC-") && !cache.id().equals("NULL")) {
            MCMessages.sendErrorMsg(sender, "editcache.alreadyediting", cache.id());
            return true;
        }
        
        String subCommand = args[0];
        
        switch (subCommand) {
            case "cancel" -> {
                MCMessages.sendMsg(sender, "editcache.cancel", ChatColor.LIGHT_PURPLE, cache.id());
                cache = Minecache.EMPTY;
                cache.setID("NULL");
            }
            case "name" -> {
                if (args.length < 2) {
                    MCMessages.incorrectUsage(sender);
                    MCMessages.usage(sender, "editcache.name", command, label);
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
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4 && sender instanceof Player plr) {
                    x = Utils.validateCoordinate(args[1], plr, "x");
                    y = Utils.validateCoordinate(args[2], plr, "y");
                    z = Utils.validateCoordinate(args[3], plr, "z");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], "x");
                    y = Utils.validateCoordinate(args[2], "y");
                    z = Utils.validateCoordinate(args[3], "z");
                } else {
                    MCMessages.incorrectUsage(sender, "argcount");
                    MCMessages.usage(sender, "editcache.lodecoords", command, label);
                    return true;
                }

                if (!Utils.validateLocation(sender, x, y, z)) return true;

                cache.setLodeLocation(new Location(cache.world(), x, y, z));
                MCMessages.sendMsg(sender, "editcache.lodecoords", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z);
            }
            case "coords" -> {
                int x, y, z;
                if (args.length == 1&& sender instanceof Player plr) {
                    x = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockX()), plr, "x");
                    y = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockY()), plr, "y");
                    z = Utils.validateCoordinate(String.valueOf(plr.getLocation().getBlockZ()), plr, "z");
                } else if (args.length == 4 && sender instanceof Player plr) {
                    x = Utils.validateCoordinate(args[1], plr, "x");
                    y = Utils.validateCoordinate(args[2], plr, "y");
                    z = Utils.validateCoordinate(args[3], plr, "z");
                } else if (args.length == 4) {
                    x = Utils.validateCoordinate(args[1], "x");
                    y = Utils.validateCoordinate(args[2], "y");
                    z = Utils.validateCoordinate(args[3], "z");
                } else {
                    MCMessages.incorrectUsage(sender, "argcount");
                    MCMessages.usage(sender, "editcache.coords", command, label);
                    return true;
                }

                if (!Utils.validateLocation(sender, x, y, z)) return true;

                cache.setLocation(new Location(cache.world(), x, y, z));
                MCMessages.sendMsg(sender, "editcache.coords", ChatColor.LIGHT_PURPLE, cache.world().getName(), x, y, z);

            }
            case "code" -> {
                if (args.length < 2) {
                    MCMessages.incorrectUsage(sender);
                    MCMessages.usage(sender, "editcache.code", command, label);
                    return true;
                } else {
                    cache.setCode(args[1].trim());
                    MCMessages.sendMsg(sender, "editcache.code", ChatColor.LIGHT_PURPLE, cache.code());
                }
            }
            case "save" -> {
                if (cache.name() == null) {
                    MCMessages.sendErrorMsg(sender, "editcache.noname");
                    MCMessages.usage(sender, "editcache.name", command, label);
                } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                    MCMessages.sendErrorMsg(sender, "editcache.nocoords");
                    MCMessages.usage(sender, "editcache.coords", command, label);
                } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
                    MCMessages.sendErrorMsg(sender, "editcache.nolodecoords");
                    MCMessages.usage(sender, "editcache.lodecoords", command, label);
                } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                    MCMessages.sendErrorMsg(sender, "editcache.lodetoofar");
                } else if (cache.code() == null) {
                    MCMessages.sendErrorMsg(sender, "editcache.nocode");
                    MCMessages.usage(sender, "editcache.code", command, label);
                }  else {
                    MinecacheEditedEvent event = new MinecacheEditedEvent(cache, sender);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        MCMessages.sendErrorMsg(sender, "editcache");
                        return true;
                    }

                    MinecachingAPI.get().saveMinecache(cache, false);
                    MCMessages.sendMsg(sender, "editcache.save", ChatColor.LIGHT_PURPLE, cache.id(), cache.name());
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
