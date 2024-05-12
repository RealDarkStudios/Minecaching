package net.realdarkstudios.minecaching.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.commons.misc.IBaseCommand;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.commons.util.MessageKeys;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class MCCommand implements IBaseCommand {
    /**
     * Checks if a cache is valid and sends a message to the sender if not
     * @param sender The {@link CommandSender} to yell at if it doesn't exist
     * @param cache The cache from the id
     * @param id The id for the cache to check
     * @return {@code true} if the cache exists, {@code false} otherwise.
     * @since 0.3.1.0
     */
    boolean cacheCheck(CommandSender sender, Minecache cache, String id) {
        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.CANT_FIND_CACHE, id);
            return false;
        }

        return true;
    }

    boolean cacheCheckNoAlert(Minecache cache) {
        return !cache.equals(Minecache.EMPTY);
    }

    /**
     * Checks if the sender is a player or not
     * @param sender The {@link CommandSender} to check
     * @return {@code true} if it is a player, {@code false} otherwise
     * @since 0.3.1.0
     */
    boolean playerCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            LocalizedMessages.send(sender, MessageKeys.Error.NON_CONSOLE_COMMAND);
            return false;
        }

        return true;
    }

    /**
     * Returns true if the sender has the permission
     * @param sender The {@link  CommandSender} to check the permissions of
     * @param permission The {@link Permission} to check for
     * @return {@code true} if sender has the permission, {@code false} otherwise
     */
    boolean hasPerm(CommandSender sender, Permission permission) {
        return sender.hasPermission(permission);
    }

    /**
     * Returns true if the sender has either the `first` or `second` permission
     * @param sender The {@link  CommandSender} to check the permissions of
     * @param first The first {@link Permission} to check for
     * @param second The second {@link Permission} to check for
     * @return {@code true} if sender has either permission, {@code false} otherwise
     */
    boolean hasEitherPerm(CommandSender sender, Permission first, Permission second) {
        return hasPerm(sender, first) || hasPerm(sender, second);
    }

    /**
     * @see LocalizedMessages#send(CommandSender, LocalizedMessages.Key, Object...)
     * @since 0.4.0.0
     */
    void send(CommandSender sender, LocalizedMessages.Key key, Object... formatArgs) {
        LocalizedMessages.sendWithPrefix(sender, key, formatArgs);
    }

    /**
     * @see LocalizedMessages#sendMessages
     * @since 0.4.0.0
     */
    void send(CommandSender sender, String... messages) {
        LocalizedMessages.sendMessages(sender, messages);
    }

    /**
     * @see LocalizedMessages#sendComponents(CommandSender, TextComponent...)
     * @since 0.4.0.0
     */
    void send(CommandSender sender, TextComponent... components) {
        LocalizedMessages.sendComponents(sender, components);
    }

    /**
     * @since 0.4.0.0
     */
    void info(LocalizedMessages.Key key, Object... formatArgs) {
        MinecachingAPI.tInfo(key, formatArgs);
    }

    /**
     * @since 0.4.0.0
     */
    void warning(LocalizedMessages.Key key, Object... formatArgs) {
        MinecachingAPI.tWarning(key, formatArgs);
    }

    /**
     * @since 0.4.0.0
     */
    void info(String... messages) {
        MinecachingAPI.info(messages);
    }

    /**
     * @since 0.4.0.0
     */
    void warning(String... messages) {
        MinecachingAPI.warning(messages);
    }
}
