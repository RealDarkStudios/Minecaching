package net.realdarkstudios.minecaching.api;

import org.bukkit.ChatColor;

public enum MinecacheType {
    TRADITIONAL("TRADITIONAL", ChatColor.DARK_GREEN),
    MYSTERY("MYSTERY", ChatColor.BLUE),
    MULTI("MULTI", ChatColor.GOLD),
    INVALID("INVALID", ChatColor.DARK_RED);

    private String id;
    private ChatColor color;

    MinecacheType(String id, ChatColor color) {
        this.id = id;
        this.color = color;
    }

    public static MinecacheType get(String type) {
        return switch (type) {
          case "TRADITIONAL" -> TRADITIONAL;
          case "MYSTERY" -> MYSTERY;
          case "MULTI" -> MULTI;
          default -> INVALID;
        };
    }

    public String getId() {
        return id;
    }

    public ChatColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return id.replace("_", "");
    }
}
