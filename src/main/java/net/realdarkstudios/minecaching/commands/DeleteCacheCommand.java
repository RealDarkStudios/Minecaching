package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCacheCommand implements CommandExecutor, TabExecutor {
    // delcache <id>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Incorrect Usage (Wrong amount of arguments!)");
            return false;
        }

        String id = args[0];
        if (MinecacheStorage.getInstance().getMinecacheByID(id).equals(Minecache.EMPTY)) {
            sender.sendMessage(ChatColor.RED + "Did not find minecache with ID " + id);
            return true;
        }

        if (sender instanceof Player plr && !plr.isOp() && plr.getUniqueId().equals(MinecacheStorage.getInstance().getMinecacheByID(id).author())) {
            MinecacheStorage.getInstance().deleteMinecache(MinecacheStorage.getInstance().getMinecacheByID(id));
            sender.sendMessage(String.format("%sSuccess! Deleted Minecache with ID %s", ChatColor.GREEN, id));
        } else if (sender.isOp()) {
            MinecacheStorage.getInstance().deleteMinecache(MinecacheStorage.getInstance().getMinecacheByID(id));
            sender.sendMessage(String.format("%sSuccess! Deleted Minecache with ID %s", ChatColor.GREEN, id));
        }else {
            sender.sendMessage(ChatColor.RED + "You can't delete other player's Minecaches!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return MinecacheStorage.getInstance().getIDArray();
        else if (args.length > 1) return List.of();

        ArrayList<String> toReturn = new ArrayList<>();

        for (String id: MinecacheStorage.getInstance().getIDArray()) {
            if (id.contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && MinecacheStorage.getInstance().getMinecacheByID(id).author().equals(plr.getUniqueId())))) {
                toReturn.add(id);
            }
        }

        return toReturn;
    }
}
