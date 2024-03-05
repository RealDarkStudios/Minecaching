package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MaintainerCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_ARG_COUNT);
            LocalizedMessages.send(sender, MessageKeys.Usage.MAINTAINER, label);
            return true;
        }

        String cacheID = args[0];
        Minecache cache = MinecachingAPI.get().getMinecache(cacheID);

        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, cacheID);
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
