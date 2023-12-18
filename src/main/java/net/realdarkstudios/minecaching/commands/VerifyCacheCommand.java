package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStatus;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
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
        Minecache minecache = MinecacheStorage.getInstance().getMinecacheByID(args[0]);

        if (minecache.equals(Minecache.EMPTY)) {
            sender.sendMessage(ChatColor.RED + "Please input a valid Minecache!");
            return false;
        } else if (!minecache.status().equals(MinecacheStatus.NEEDS_REVIEWED)) {
            sender.sendMessage(ChatColor.RED + "This cache can't be verified because it already is or is disabled/archived");
        } else {
            minecache.setStatus(MinecacheStatus.VERIFIED);
            MinecacheStorage.getInstance().saveMinecache(minecache, false);
            sender.sendMessage(ChatColor.GREEN + "Success! Verified " + args[0]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecacheStorage.getInstance().getMinecaches().stream().filter(m -> m.status().equals(MinecacheStatus.NEEDS_REVIEWED)).toList();
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m : fMinecaches) {
            mcIDs.add(m.id());
        }

        return sender.isOp() ? mcIDs : List.of();
    }
}
