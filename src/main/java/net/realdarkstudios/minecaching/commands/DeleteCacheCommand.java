package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.MinecacheDeletedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "deletecache", command, label);
        }

        String id = args[0].trim();
        Minecache cache = MinecachingAPI.get().getMinecache(id);
        if (cache.equals(Minecache.EMPTY)) {
            MCMessages.sendErrorMsg(sender, "cantfind", id);
            return true;
        }

        if ((sender instanceof Player plr && !plr.getUniqueId().equals(cache.author())) && !sender.isOp()) {
            MCMessages.sendErrorMsg(sender, "deletecache.others");
            return true;
        }

        MinecacheDeletedEvent event = new MinecacheDeletedEvent(cache, Bukkit.getPlayer(cache.author()));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MCMessages.sendErrorMsg(sender, "deletecache");
            return true;
        }

        MinecachingAPI.get().deleteMinecache(cache, sender instanceof Player plr ? plr.getUniqueId() : Utils.EMPTY_UUID);
        MCMessages.sendMsg(sender, "deletecache", ChatColor.GREEN, id);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return MinecachingAPI.get().getAllKnownCacheIDs();
        else if (args.length > 1) return List.of();

        ArrayList<String> toReturn = new ArrayList<>();

        for (String id: MinecachingAPI.get().getAllKnownCacheIDs()) {
            if (id.contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && MinecachingAPI.get().getMinecache(id).author().equals(plr.getUniqueId())))) {
                toReturn.add(id);
            }
        }

        return toReturn;
    }
}
