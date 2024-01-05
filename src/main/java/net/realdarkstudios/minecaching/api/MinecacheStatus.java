package net.realdarkstudios.minecaching.api;

import org.bukkit.ChatColor;

public enum MinecacheStatus {
    VERIFIED("ACTIVE", "V", ChatColor.GREEN),
    DISABLED("DISABLED", "D", ChatColor.RED),
    ARCHIVED("ARCHIVED", "A", ChatColor.GRAY),
    NEEDS_REVIEWED("NEEDS_REVIEWED", "N", ChatColor.YELLOW),
    NEEDS_MAINTENANCE("NEEDS_MAINTENANCE", "M", ChatColor.LIGHT_PURPLE),
    INVALID("INVALID", "I", ChatColor.DARK_RED);

    private final String id, strForm;
    private final ChatColor color;

    MinecacheStatus(String id, String strForm, ChatColor color) {
        this.id = id;
        this.strForm = strForm;
        this.color = color;
    }

    public static MinecacheStatus get(String status) {
        return switch (status) {
            case "ACTIVE" -> MinecacheStatus.VERIFIED;
            case "DISABLED" -> MinecacheStatus.DISABLED;
            case "ARCHIVED" -> MinecacheStatus.ARCHIVED;
            case "NEEDS_REVIEWED" -> MinecacheStatus.NEEDS_REVIEWED;
            default -> MinecacheStatus.INVALID;
        };
    }

    public ChatColor getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return strForm;
    }
}
