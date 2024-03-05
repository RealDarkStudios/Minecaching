package net.realdarkstudios.minecaching.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


/**
 * A collection of utility methods to send messages while supporting the {@link net.realdarkstudios.minecaching.api.misc.Localization} system
 * @deprecated Since 0.3.1.0 | Please use the new {@link LocalizedMessages} system
 */
@Deprecated(since = "0.3.1.0", forRemoval = true)
public class MCMessages {
    /**
     * A default No Permission message. Used when a translated one is not found
     */
    public static final TextComponent NO_PERMISSION = TextComponentBuilder.fromTranslation("permission.none")
            .color(ChatColor.DARK_RED)
            .bold()
            .build();

    /**
     * A default Incorrect Usage message. Used when a translated one is not found
     */
    public static final TextComponent INCORRECT_USAGE = TextComponentBuilder.fromTranslation("error.usage")
            .color(ChatColor.RED)
            .build();

    /**
     * A default Error message. Used when a translated one is not found
     */
    public static final TextComponent ERROR = TextComponentBuilder.fromTranslation("error")
            .color(ChatColor.RED)
            .build();

    /**
     * Gets the translated No Permission message from the given source with the substitutions, or returns {@link MCMessages#NO_PERMISSION} if there is not a translation defined
     * @param source The source of the permission, without the "permission.none." part (ex. a source of "mcadmin" would use the "permission.none.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     * @return The translated {@link TextComponent}
     */
    public static TextComponent noPermission(String source, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("permission.none." + source)) return TextComponentBuilder.fromTranslation("permission.none." + source, substitutions).color(ChatColor.DARK_RED).bold().build();
        else return NO_PERMISSION;
    }

    /**
     * Sends the {@link CommandSender} the default {@link MCMessages#NO_PERMISSION} message
     * @param sender The CommandSender to send the {@link TextComponent} to
     */
    public static void noPermission(CommandSender sender) {
        send(sender, NO_PERMISSION);
    }

    /**
     * Sends the {@link CommandSender} the No Permission message from the specified source with substitutions
     * @param sender The CommandSender to send the {@link TextComponent} to
     * @param source The source of the permission, without the "permission.none." part (ex. a source of "mcadmin" would use the "permission.none.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     */
    public static void noPermission(CommandSender sender, String source, Object... substitutions) {
        send(sender, noPermission(source, substitutions));
    }

    /**
     * Gets the translated Incorrect Usage message from the given source with the substitutions, or returns {@link MCMessages#INCORRECT_USAGE} if there is not a translation defined
     * @param source The source of the usage, without the "error.usage." part (ex. a source of "mcadmin" would use the "error.usage.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     * @return The translated {@link TextComponent}
     */
    public static TextComponent incorrectUsage(String source, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("error.usage." + source)) return TextComponentBuilder.fromTranslation("error.usage." + source, substitutions).color(ChatColor.RED).build();
        else return INCORRECT_USAGE;
    }

    /**
     * Sends the {@link CommandSender} the default {@link MCMessages#INCORRECT_USAGE} message
     * @param sender The CommandSender to send the {@link TextComponent} to
     */
    public static void incorrectUsage(CommandSender sender) {
        send(sender, INCORRECT_USAGE);
    }

    /**
     * Sends the {@link CommandSender} the Incorrect Usage message from the specified source with substitutions
     * @param sender The CommandSender to send the {@link TextComponent} to
     * @param source The source of the usage, without the "error.usage." part (ex. a source of "mcadmin" would use the "error.usage.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     */
    public static void incorrectUsage(CommandSender sender, String source, Object... substitutions) {
        send(sender, incorrectUsage(source, substitutions));
    }

    /**
     * Gets the translated Usage message from the given source with the substitutions, or returns {@link Command#getUsage()} if there is not a translation defined
     * @param source The source of the usage, without the "usage." part (ex. a source of "mcadmin" would use the "usage.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     * @return The translated {@link TextComponent}
     */
    public static TextComponent usage(String source, Command command, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("usage." + source)) return TextComponentBuilder.fromTranslation("usage." + source, substitutions).color(ChatColor.RED).build();
        else return new TextComponentBuilder(command.getUsage()).color(ChatColor.RED).build();
    }

    /**
     * Sends the {@link CommandSender} the Usage message from the specified source with substitutions
     * @param sender The CommandSender to send the {@link TextComponent} to
     * @param source The source of the usage, without the "usage." part (ex. a source of "mcadmin" would use the "usage.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     */
    public static void usage(CommandSender sender, String source, Command command, Object... substitutions) {
        send(sender, usage(source, command, substitutions));
    }

    /**
     * Sends the {@link CommandSender} the default {@link MCMessages#ERROR} message
     * @param sender The CommandSender to send the {@link TextComponent} to
     */
    public static void sendErrorMsg(CommandSender sender) {
        send(sender, ERROR);
    }

    /**
     * Sends the {@link CommandSender} the Error message from the specified source with substitutions
     * @param sender The CommandSender to send the {@link TextComponent} to
     * @param source The source of the error, without the "error." part (ex. a source of "mcadmin" would use the "usage.mcadmin" translation)
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     */
    public static void sendErrorMsg(CommandSender sender, String source, Object... substitutions) {
        send(sender, ChatColor.RED + translation("error." + source, "error", substitutions));
    }

    /**
     * Sends the {@link CommandSender} the message from the specified source with substitutions
     * @param sender The CommandSender to send the message to
     * @param source The source of the message, using "error" as the default if this source does not exist
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     */
    public static void sendMsg(CommandSender sender, String source, Object... substitutions) {
        send(sender, translation(source, "error", substitutions));
    }

    /**
     * Sends the {@link CommandSender} the specifed message
     * @param sender The CommandSender to send the message to
     * @param message The message
     */
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    /**
     * Sends the {@link CommandSender} the specified TextComponent
     * @param sender The CommandSender to send the TextComponent to
     * @param message The {@link TextComponent} to send
     */
    public static void send(CommandSender sender, TextComponent message) {
        sender.spigot().sendMessage(message);
    }

    /**
     * Checks for and returns a translated message, first using the source, then the backup source, and if neither exist, "plugin.localization.missing" gets returned
     * @param source The source to use
     * @param backupSource The backup source to use
     * @param substitutions The substitutions
     * @return The translated string
     */
    private static String translation(String source, String backupSource, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation(source)) return MinecachingAPI.getLocalization().getTranslation(source, substitutions);
        else return MinecachingAPI.getLocalization().getTranslation(backupSource);
    }
}
