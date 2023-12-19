package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import net.realdarkstudios.minecaching.data.PlayerStorage;
import org.bukkit.ChatColor;
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
            sender.sendMessage("Incorrect Usage!");
            return false;
        }

        String id = args[0].trim();
        Minecache cache = MinecacheStorage.getInstance().getMinecacheByID(id);
        if (cache.equals(Minecache.EMPTY)) {
            sender.sendMessage(ChatColor.RED + "Did not find minecache with ID " + id);
            return true;
        }

        if ((sender instanceof Player plr && !plr.getUniqueId().equals(cache.author())) && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You can't delete other player's Minecaches!");
            return true;
        }

        PlayerStorage.getInstance().deleteMinecache(cache);
        MinecacheStorage.getInstance().deleteMinecache(cache);
        sender.sendMessage(String.format("%sSuccess! Deleted Minecache with ID %s", ChatColor.GREEN, id));
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
