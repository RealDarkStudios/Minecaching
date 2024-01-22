package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheStatus;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.MinecacheVerifiedEvent;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VerifyCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MCPluginMessages.incorrectUsage(sender);
            MCPluginMessages.usage(sender, "verifycache", command, label);
            return true;
        }

        Minecache minecache = MinecachingAPI.get().getMinecache(args[0]);

        if (minecache.equals(Minecache.EMPTY)) {
            MCPluginMessages.sendErrorMsg(sender, "cantfind", minecache.id());
            return false;
        } else if (!minecache.status().equals(MinecacheStatus.NEEDS_REVIEWED)) {
            MCPluginMessages.sendErrorMsg(sender, "verifycache.cantverify");
        } else {
            MinecacheVerifiedEvent event = new MinecacheVerifiedEvent(minecache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                MCPluginMessages.sendErrorMsg(sender, "verifycache");
                return true;
            }

            MinecachingAPI.get().verifyMinecache(sender instanceof Player plr ? plr.getUniqueId() : Utils.EMPTY_UUID, minecache);
            MCPluginMessages.sendMsg(sender, "verifycache.verify", ChatColor.GREEN, minecache.id());
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
