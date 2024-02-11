package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.util.Utils;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.minecache.MinecacheArchivedEvent;
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

public class ArchiveCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "archivecache", command, label);
            return true;
        }

        String reason;

        if (args.length == 1) reason = "Archived. Please note that this cache can no longer be found by other cachers.";
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
        } else if (minecache.status().equals(MinecacheStatus.ARCHIVED) || minecache.status().equals(MinecacheStatus.INVALID)) {
            MCMessages.sendErrorMsg(sender, "archivecache.cantarchive");
        } else {
            MinecacheArchivedEvent event = new MinecacheArchivedEvent(minecache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                MCMessages.sendErrorMsg(sender, "archivecache");
                return true;
            }

            MinecachingAPI.get().archiveMinecache(sender instanceof Player plr ? plr.getUniqueId() : Utils.EMPTY_UUID, minecache, reason);
            MCMessages.sendMsg(sender, "archivecache.archive", ChatColor.GREEN, minecache.id());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> !(m.status().equals(MinecacheStatus.INVALID) || m.status().equals(MinecacheStatus.ARCHIVED)));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m: fMinecaches) {
            if (m.id().contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && m.author().equals(plr.getUniqueId())))) {
                mcIDs.add(m.id());
            }
        }

        return args.length == 1 ? mcIDs : List.of();
    }
}
