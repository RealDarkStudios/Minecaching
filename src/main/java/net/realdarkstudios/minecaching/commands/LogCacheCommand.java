package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.MinecacheFoundEvent;
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
            sender.sendMessage(ChatColor.RED + "You must be a player to find caches!");
            return true;
        }

        if (args.length < 2) {
            plr.sendMessage(ChatColor.RED + "Incorrect number of arguments!");
            plr.sendMessage(ChatColor.RED + "Use /" + label + " found <code> [logmessage]");
            plr.sendMessage(ChatColor.RED + "or /" + label + " <type> [logmessage]");
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = pdo.getLocatingId();

        if (id.equals("NULL")) {
            plr.sendMessage(ChatColor.RED + "You are not looking for a cache!");
            plr.sendMessage(ChatColor.RED + "Use /locate <id> ");
            return true;
        }

        String type = args[0];
        LogType logType = LogType.get(type);

        if (logType.equals(LogType.INVALID)) {
            Utils.sendPlrErrorMessage(plr, "Invalid Log Type!");
        }

        String code = args[1];

        Minecache cache = MinecachingAPI.get().getMinecache(id);

        switch (cache.status()) {
            case NEEDS_REVIEWED -> {
                plr.sendMessage(ChatColor.RED + "This cache is waiting to be reviewed!");
                return true;
            }
            case ARCHIVED -> {
                plr.sendMessage(ChatColor.RED + "This cache is archived!");
                return true;
            }
            case DISABLED -> {
                plr.sendMessage(ChatColor.RED + "This cache is disabled! It may be back soon, though.");
                return true;
            }
            case INVALID -> {
                plr.sendMessage(ChatColor.RED + "This cache is currently invalid!");
                return true;
            }
            default -> {}
        }

        StringBuilder logMessage = new StringBuilder();
        for (int i = logType.equals(LogType.FOUND) ? 2 : 1; i < args.length; i++) {
            logMessage.append(args[i]).append(" ");
        }

        if (logMessage.length() > 200) {
            plr.sendMessage(ChatColor.RED + "Log message cannot be longer than 200 characters!");
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
                            plr.sendMessage(ChatColor.RED + "Could not log this minecache!");
                            return true;
                        }

                        pdo.addFind(cache.id());
                        if (isFTF) {
                            pdo.addFTF(cache.id());
                            MinecachingAPI.get().saveMinecache(cache.setFTF(plr.getUniqueId()), false);
                        }

                        MinecachingAPI.get().saveMinecache(cache.setFTF(plr.getUniqueId()), false);

                        MinecachingAPI.get().save();
                        MinecachingAPI.get().update();

                        plr.sendMessage(ChatColor.GREEN + "Congratulations! You found " + cache.id() + ": " + cache.name());
                        if (isFTF) {
                            plr.sendMessage(ChatColor.GREEN + "You were also the first one to find this cache. Your new FTF total is " + pdo.getFTFs().size());
                        }
                        plr.sendMessage(ChatColor.GREEN + "You now have " + pdo.getFinds().size() + " finds");
                    }

                    Log log = Utils.createLog(plr, cache, logType, logMsg, isFTF);

                    pdo.setLocatingId("NULL");
                    pdo.saveData();

                    switch (logType) {
                        case DNF -> plr.sendMessage("Logged a DNF for " + cache.id());
                        case NOTE -> plr.sendMessage("Logged a Note on " + cache.id());
                        default -> plr.sendMessage("Logged!");
                    }
                } else {
                    plr.sendMessage(ChatColor.RED + "You must be within 25 blocks of the cache!");
                }
            } else {
                plr.sendMessage(ChatColor.RED + "Incorrect code!");
            }
        } else {
            plr.sendMessage(ChatColor.RED + "Only Traditional and Mystery caches supported at this time");
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
