package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class MCCommand implements CommandExecutor, TabExecutor {
    /**
     * Checks if a cache is valid and sends a message to the sender if not
     * @param sender The {@link CommandSender} to yell at if it doesn't exist
     * @param cache The cache from the id
     * @param id The id for the cache to check
     * @return {@code true} if the cache exists, {@code false} otherwise.
     */
    boolean cacheCheck(CommandSender sender, Minecache cache, String id) {

        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, id);
            return true;
        }

        return false;
    }

    /**
     * Checks if the sender is a player or not
     * @param sender The {@link CommandSender} to check
     * @return {@code true} if it is a player, {@code false} otherwise
     */
    boolean playerCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            LocalizedMessages.send(sender, MessageKeys.Error.NON_CONSOLE_COMMAND);
            return false;
        }

        return true;
    }
}
