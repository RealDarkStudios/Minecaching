package net.realdarkstudios.minecaching.commands;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.util.LogbookGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class LogbookCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length < 1) {
            plr.sendMessage(ChatColor.RED + "Please enter an ID!");
            return true;
        }

        int bookNum;
        String id = args[0];

        try {
            bookNum = Integer.parseInt(args[1]);
        } catch (Exception e) {
            bookNum = 1;
        }

        MinecachingAPI.info("BookNum: " + bookNum);


        if (plr.getInventory().firstEmpty() == -1) {
            plr.sendMessage(ChatColor.RED + "No available slots!");
        } else plr.getInventory().addItem(new LogbookGenerator(MinecachingAPI.get().getMinecache(id)).getLogbook(bookNum));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return switch (args.length) {
            case 0 -> MinecachingAPI.get().getAllKnownCacheIDs();
            case 1 -> MinecachingAPI.get().getFilteredCacheIDs(s -> s.contains(args[0]));
            default -> List.of();
        };
    }
}
