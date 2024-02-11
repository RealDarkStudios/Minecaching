package net.realdarkstudios.minecaching.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MCMessages {
    // Provides default messages when no translated one exists
    public static final TextComponent NO_PERMISSION = TextComponentBuilder.fromTranslation("permission.none")
            .color(ChatColor.DARK_RED)
            .bold()
            .build();

    public static final TextComponent INCORRECT_USAGE = TextComponentBuilder.fromTranslation("error.usage")
            .color(ChatColor.RED)
            .build();

    public static TextComponent noPermission(String source) {
        if (MinecachingAPI.getLocalization().hasTranslation("permission.none." + source)) return TextComponentBuilder.fromTranslation("permission.none." + source).color(ChatColor.DARK_RED).bold().build();
        else return NO_PERMISSION;
    }

    public static TextComponent noPermission(String source, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("permission.none." + source)) return TextComponentBuilder.fromTranslation("permission.none." + source, substitutions).color(ChatColor.DARK_RED).bold().build();
        else return NO_PERMISSION;
    }

    public static void noPermissions(CommandSender sender) {
        sender.spigot().sendMessage(noPermission("\\"));
    }

    public static void noPermission(CommandSender sender, String source) {
        sender.spigot().sendMessage(noPermission(source));
    }

    public static void noPermission(CommandSender sender, String source, Object... substitutions) {
        sender.spigot().sendMessage(noPermission(source, substitutions));
    }

    public static String incorrectUsage(String source) {
        if (MinecachingAPI.getLocalization().hasTranslation("error.usage." + source)) return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error.usage." + source);
        else return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error.usage");
    }

    public static String incorrectUsage(String source, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("error.usage." + source)) return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error.usage." + source, substitutions);
        else return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error.usage");
    }

    public static void incorrectUsage(CommandSender sender) {
        sender.sendMessage(incorrectUsage("\\"));
    }

    public static void incorrectUsage(CommandSender sender, String source) {
        sender.sendMessage(incorrectUsage(source));
    }

    public static void incorrectUsage(CommandSender sender, String source, Object... substitutions) {
        sender.sendMessage(incorrectUsage(source, substitutions));
    }

    public static String usage(String source, Command command) {
        if (MinecachingAPI.getLocalization().hasTranslation("usage." + source)) return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("usage." + source);
        else return ChatColor.RED + command.getUsage();
    }

    public static String usage(String source, Command command, Object... substitutions) {
        if (MinecachingAPI.getLocalization().hasTranslation("usage." + source)) return ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("usage." + source, substitutions);
        else return ChatColor.RED + command.getUsage();
    }

    public static void usage(CommandSender sender, String source, Command command) {
        sender.sendMessage(usage(source, command));
    }

    public static void usage(CommandSender sender, String source, Command command, Object... substitutions) {
        sender.sendMessage(usage(source, command, substitutions));
    }

    public static void sendErrorMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error"));
    }

    public static void sendErrorMsg(CommandSender sender, String key) {
        sender.sendMessage(ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error." + key));
    }

    public static void sendErrorMsg(CommandSender sender, String key, Object... substitutions) {
        sender.sendMessage(ChatColor.RED + MinecachingAPI.getLocalization().getTranslation("error." + key, substitutions));
    }

    public static void sendMsg(CommandSender sender, String key) {
        sender.sendMessage(MinecachingAPI.getLocalization().getTranslation(key));
    }

    public static void sendMsg(CommandSender sender, String key, Object... substitutions) {
        sender.sendMessage(MinecachingAPI.getLocalization().getTranslation(key, substitutions));
    }
}
