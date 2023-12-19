package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.data.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class FindCacheCommand implements CommandExecutor, TabExecutor {
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

        Minecache cache = MinecacheStorage.getInstance().getMinecacheByID(id);

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
                    PlayerStorageObject plrdata = PlayerStorage.getInstance().getOrCreatePlayerData(plr);
                    boolean isFtf = plrdata.findMinecache(cache);
                    plr.sendMessage(ChatColor.GREEN + "Congratulations! You found " + cache.id() + ": " + cache.name());
                    if (isFtf)
                        plr.sendMessage(ChatColor.GREEN + "You were also the first one to find this cache. Your new FTF total is" + plrdata.getFTFs().size());
                    plr.sendMessage(ChatColor.GREEN + "You now have " + plrdata.getFinds() + " finds");
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
        return args.length == 0 ? MinecacheStorage.getInstance().getIDArray() : args.length == 1 ? MinecacheStorage.getInstance().getIDArray().stream().filter(s -> s.contains(args[0])).toList() : List.of();

    }
}
