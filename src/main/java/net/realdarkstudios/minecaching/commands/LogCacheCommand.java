package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.log.Log;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.log.NotificationType;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.minecache.MinecacheFoundEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
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
            MCMessages.sendErrorMsg(sender, "execute.console");
            return true;
        }

        if (args.length < 1) {
            MCMessages.incorrectUsage(sender, "argcount");
            MCMessages.usage(sender, "logcache", command, label);
            return true;
        }

        String type = args[0];
        LogType logType = LogType.get(type);

        if (logType.equals(LogType.INVALID) || logType.equals(LogType.PUBLISH) || logType.equals(LogType.DISABLE) || logType.equals(LogType.ARCHIVE)) {
            MCMessages.incorrectUsage(sender, "logcache.logtype");
            MCMessages.usage(sender, "logcache", command, label);
            return true;
        }

        if (logType.equals(LogType.FOUND) && args.length < 2) {
            MCMessages.incorrectUsage(sender, "argcount");
            MCMessages.usage(sender, "logcache.found", command, label);
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = pdo.getLocatingId();

        if (id.equals("NULL")) {
            MCMessages.sendErrorMsg(sender, "logcache.notlocating");
            MCMessages.usage(sender, "locatecache", Minecaching.getInstance().getCommand("locatecache"), "locatecache");
            return true;
        }

        Minecache cache = MinecachingAPI.get().getMinecache(id);

        switch (cache.status()) {
            case REVIEWING -> {
                MCMessages.sendErrorMsg(sender, "logcache.needs_reviewed");
                return true;
            }
            case ARCHIVED -> {
                MCMessages.sendErrorMsg(sender, "logcache.archived");
                return true;
            }
            case DISABLED -> {
                MCMessages.sendErrorMsg(sender, "logcache.disabled");
                return true;
            }
            case INVALID -> {
                MCMessages.sendErrorMsg(sender, "logcache.invalid");
                return true;
            }
            default -> {}
        }

        StringBuilder logMessage = new StringBuilder();
        for (int i = logType.equals(LogType.FOUND) ? 2 : 1; i < args.length; i++) {
            logMessage.append(args[i]).append(" ");
        }

        if (logMessage.length() > 200) {
            MCMessages.sendErrorMsg(sender, "logcache.longlog", logMessage.length());
            return true;
        }

        String logMsg = logMessage.toString().trim();

        if (cache.type().equals(MinecacheType.TRADITIONAL) || cache.type().equals(MinecacheType.MYSTERY)) {
            if (!logType.equals(LogType.FOUND) || cache.code().equals(args[1])) {
                if (plr.getLocation().distance(cache.location()) < 25) {
                    boolean isFTF = pdo.isFTF(cache);
                    if (logType.equals(LogType.FOUND)) {
                        // Emit MinecacheFoundEvent
                        MinecacheFoundEvent foundEvent = new MinecacheFoundEvent(cache, plr, pdo.isFTF(cache));
                        Bukkit.getPluginManager().callEvent(foundEvent);

                        if (foundEvent.isCancelled()) {
                            MCMessages.sendErrorMsg(sender, "logcache");
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

                        MCMessages.sendMsg(sender, "logcache.find", ChatColor.GREEN, cache.id(), cache.name());
                        MCMessages.sendMsg(sender, isFTF ? "logcache.findcount.ftf" : "logcache.findcount", ChatColor.GREEN, pdo.getFinds().size(), isFTF ? pdo.getFTFs().size() : null);
                    } else {
                        MCMessages.sendMsg(sender, "logcache.log", ChatColor.GREEN, cache.id(), cache.name());
                    }

                    if (logType.equals(LogType.FLAG) && !plr.getUniqueId().equals(cache.author())) MinecachingAPI.get().createNotification(cache.author(), plr.getUniqueId(), NotificationType.FLAG, cache);

                    Log log = Utils.createLog(plr, cache, logType, logMsg, isFTF);
                } else {
                    MCMessages.sendErrorMsg(sender, "logcache.distance");
                }
            } else {
                MCMessages.sendErrorMsg(sender, "logcache.code");
            }
        } else {
            MCMessages.sendErrorMsg(sender, "logcache.unsupported");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();
        return switch (args.length) {
            case 0 -> List.of("found", "dnf", "note", "flag", "maintenance");
            case 1 -> switch (args[0]) {
                case "found", "dnf", "note", "flag", "maintenance" -> List.of();
                default -> List.of("found", "dnf", "note", "flag", "maintenance").stream().filter(s -> s.contains(args[0])).toList();
            };
            default -> List.of();
        };
    }
}
