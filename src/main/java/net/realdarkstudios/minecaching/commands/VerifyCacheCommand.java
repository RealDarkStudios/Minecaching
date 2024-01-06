package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheStatus;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.MinecacheVerifiedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class VerifyCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Minecache minecache = MinecachingAPI.get().getMinecache(args[0]);

        if (minecache.equals(Minecache.EMPTY)) {
            sender.sendMessage(ChatColor.RED + "Please input a valid Minecache!");
            return false;
        } else if (!minecache.status().equals(MinecacheStatus.NEEDS_REVIEWED)) {
            sender.sendMessage(ChatColor.RED + "This cache can't be verified because it already is or is disabled/archived");
        } else {
            MinecacheVerifiedEvent event = new MinecacheVerifiedEvent(minecache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                sender.sendMessage(ChatColor.RED + args[0] + " could not be verified for some reason!");
                return true;
            }

            MinecachingAPI.get().verifyMinecache(minecache);
            sender.sendMessage(ChatColor.GREEN + "Success! Verified " + args[0]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> m.status().equals(MinecacheStatus.NEEDS_REVIEWED));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m : fMinecaches) {
            mcIDs.add(m.id());
        }

        return sender.isOp() ? mcIDs : List.of();
    }
}
