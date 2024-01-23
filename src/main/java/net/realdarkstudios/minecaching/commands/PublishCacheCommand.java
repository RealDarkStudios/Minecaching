package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheStatus;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.MinecachePublishedEvent;
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

public class PublishCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "publishcache", command, label);
            return true;
        }

        String reason;

        if (args.length == 1) reason = "Published.";
        else {
            StringBuilder rsn = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                rsn.append(args[i]).append(" ");
            }
            reason = rsn.toString().trim();
        }

        Minecache minecache = MinecachingAPI.get().getMinecache(args[0]);

        if (minecache.equals(Minecache.EMPTY)) {
            MCMessages.sendErrorMsg(sender, "cantfind", minecache.id());
            return false;
        } else if (!(minecache.status().equals(MinecacheStatus.REVIEWING) || minecache.status().equals(MinecacheStatus.DISABLED))) {
            MCMessages.sendErrorMsg(sender, "publishcache.cantpublish");
        } else {
            MinecachePublishedEvent event = new MinecachePublishedEvent(minecache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                MCMessages.sendErrorMsg(sender, "publishcache");
                return true;
            }

            MinecachingAPI.get().publishMinecache(sender instanceof Player plr ? plr.getUniqueId() : Utils.EMPTY_UUID, minecache, reason);
            MCMessages.sendMsg(sender, "publishcache.publish", ChatColor.GREEN, minecache.id());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> (m.status().equals(MinecacheStatus.REVIEWING) || m.status().equals(MinecacheStatus.DISABLED)) && m.id().contains(args.length > 0 ? args[0] : ""));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m : fMinecaches) {
            mcIDs.add(m.id());
        }

        return sender.isOp() && args.length == 1 ? mcIDs : List.of();
    }
}
