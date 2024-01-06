package net.realdarkstudios.minecaching.commands;

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
            plr.sendMessage(ChatColor.RED + "/find <id> <code>");
            return true;
        }

        String id = args[0];
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

        if (cache.type().equals(MinecacheType.TRADITIONAL) || cache.type().equals(MinecacheType.MYSTERY)) {
            if (cache.code().equals(code)) {
                if (plr.getLocation().distance(cache.location()) < 25) {
                    PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);
                    boolean isFTF = pdo.isFTF(cache);

                    // Emit MinecacheFoundEvent
                    MinecacheFoundEvent event = new MinecacheFoundEvent(cache, plr, pdo.isFTF(cache));
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        plr.sendMessage(ChatColor.RED + "Could not log this minecache!");
                        return true;
                    }

                    pdo.addFind(cache.id());
                    if (isFTF) {
                        pdo.addFTF(cache.id());
                        MinecachingAPI.get().saveMinecache(cache.setFTF(plr.getUniqueId()), false);
                    }

                    MinecachingAPI.get().saveMinecache(cache.setFTF(plr.getUniqueId()), false);

                    pdo.save();
                    MinecacheStorage.getInstance().save();
                    MinecachingAPI.get().update();

                    plr.sendMessage(ChatColor.GREEN + "Congratulations! You found " + cache.id() + ": " + cache.name());
                    if (isFTF) {
                        plr.sendMessage(ChatColor.GREEN + "You were also the first one to find this cache. Your new FTF total is " + pdo.getFTFs().size());
                    }
                    plr.sendMessage(ChatColor.GREEN + "You now have " + pdo.getFinds().size() + " finds");
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
        return args.length == 0 ? MinecachingAPI.get().getAllKnownCacheIDs() : args.length == 1 ? MinecachingAPI.get().getFilteredCacheIDs(s -> s.contains(args[0])) : List.of();

    }
}
