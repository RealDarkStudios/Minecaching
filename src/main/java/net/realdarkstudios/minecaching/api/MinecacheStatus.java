package net.realdarkstudios.minecaching.api;

import org.bukkit.ChatColor;

public enum MinecacheStatus {
    PUBLISHED("PUBLISHED", "P", ChatColor.GREEN),
    NEEDS_MAINTENANCE("NEEDS_MAINTENANCE", "M", ChatColor.LIGHT_PURPLE),
    DISABLED("DISABLED", "D", ChatColor.RED),
    ARCHIVED("ARCHIVED", "A", ChatColor.GRAY),
    NEEDS_REVIEWED("NEEDS_REVIEWED", "N", ChatColor.YELLOW),
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
            case "PUBLISHED", "ACTIVE" -> PUBLISHED;
            case "NEEDS_MAINTENANCE" -> NEEDS_MAINTENANCE;
            case "DISABLED" -> DISABLED;
            case "ARCHIVED" -> ARCHIVED;
            case "NEEDS_REVIEWED" -> NEEDS_REVIEWED;
            default -> INVALID;
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
