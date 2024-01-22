package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.MinecacheFoundEvent;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class LogCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            MCPluginMessages.sendErrorMsg(sender, "execute.console");
            return true;
        }

        if (args.length < 1) {
            MCPluginMessages.incorrectUsage(sender, "argcount");
            MCPluginMessages.usage(sender, "logcache", command, label);
            return true;
        }

        String type = args[0];
        LogType logType = LogType.get(type);

        if (logType.equals(LogType.INVALID)) {
            MCPluginMessages.incorrectUsage(sender, "logcache.logtype");
        }

        if (logType.equals(LogType.FOUND) && args.length < 2) {
            MCPluginMessages.incorrectUsage(sender, "argcount");
            MCPluginMessages.usage(sender, "logcache.found", command, label);
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = pdo.getLocatingId();

        if (id.equals("NULL")) {
            MCPluginMessages.sendErrorMsg(sender, "logcache.notlocating");
            MCPluginMessages.usage(sender, "locatecache", Minecaching.getInstance().getCommand("locatecache"), "locatecache");
            return true;
        }


        String code = args[1];

        Minecache cache = MinecachingAPI.get().getMinecache(id);

        switch (cache.status()) {
            case NEEDS_REVIEWED -> {
                MCPluginMessages.sendErrorMsg(sender, "logcache.needs_reviewed");
                return true;
            }
            case ARCHIVED -> {
                MCPluginMessages.sendErrorMsg(sender, "logcache.archived");
                return true;
            }
            case DISABLED -> {
                MCPluginMessages.sendErrorMsg(sender, "logcache.disabled");
                return true;
            }
            case INVALID -> {
                MCPluginMessages.sendErrorMsg(sender, "logcache.invalid");
                return true;
            }
            default -> {}
        }

        StringBuilder logMessage = new StringBuilder();
        for (int i = logType.equals(LogType.FOUND) ? 2 : 1; i < args.length; i++) {
            logMessage.append(args[i]).append(" ");
        }

        if (logMessage.length() > 200) {
            MCPluginMessages.sendErrorMsg(sender, "logcache.longlog", logMessage.length());
            return true;
        }

        String logMsg = logMessage.toString().trim();

        if (cache.type().equals(MinecacheType.TRADITIONAL) || cache.type().equals(MinecacheType.MYSTERY)) {
            if (cache.code().equals(code) || !logType.equals(LogType.FOUND)) {
                if (plr.getLocation().distance(cache.location()) < 25) {
                    boolean isFTF = pdo.isFTF(cache);
                    if (logType.equals(LogType.FOUND)) {

                        // Emit MinecacheFoundEvent
                        MinecacheFoundEvent foundEvent = new MinecacheFoundEvent(cache, plr, pdo.isFTF(cache));
                        Bukkit.getPluginManager().callEvent(foundEvent);

                        if (foundEvent.isCancelled()) {
                            MCPluginMessages.sendErrorMsg(sender, "logcache");
                            return true;
                        }

                        pdo.addFind(cache.id());
                        if (isFTF) {
                            pdo.addFTF(cache.id());
                            MinecachingAPI.get().saveMinecache(cache.setFTF(plr.getUniqueId()).setFinds(cache.finds() + 1), false);
                        } else MinecachingAPI.get().saveMinecache(cache.setFinds(pdo.getFinds().contains(cache.id()) ? cache.finds() : cache.finds() + 1), false);

                        pdo.setLocatingId("NULL");
                        pdo.saveData();

                        MinecachingAPI.get().save();
                        MinecachingAPI.get().update();

                        MCPluginMessages.sendMsg(sender, "logcache.find", ChatColor.GREEN, cache.id(), cache.name());
                        MCPluginMessages.sendMsg(sender, isFTF ? "logcache.findcount.ftf" : "logcache.findcount", ChatColor.GREEN, pdo.getFinds().size(), isFTF ? pdo.getFTFs().size() : null);
                    }

                    Log log = Utils.createLog(plr, cache, logType, logMsg, isFTF);
                } else {
                    MCPluginMessages.sendErrorMsg(sender, "logcache.distance");
                }
            } else {
                MCPluginMessages.sendErrorMsg(sender, "logcache.code");
            }
        } else {
            MCPluginMessages.sendErrorMsg(sender, "logcache.unsupported");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();
        return switch (args.length) {
            case 0 -> List.of("found", "dnf", "note", "requestreview", "maintenance", "archive", "disable", "publish");
            case 1 -> switch (args[0]) {
                case "found", "dnf", "note", "requestreview", "maintenance", "archive", "disable", "publish" -> List.of();
                default -> List.of("found", "dnf", "note", "requestreview", "maintenance", "archive", "disable", "publish").stream().filter(s -> s.contains(args[0])).toList();
            };
            default -> List.of();
        };
    }
}
