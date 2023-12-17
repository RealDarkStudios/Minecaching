package net.realdarkstudios.minecaching.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class MCPluginMessages {
    public static final TextComponent NO_PERMISSION = new TextComponentBuilder("You do not have permission to perform this command!")
            .color(ChatColor.DARK_RED)
            .bold()
            .build();

    public static final TextComponent INCORRECT_USAGE = new TextComponentBuilder("Incorrect Usage!")
            .color(ChatColor.RED)
            .build();
}
