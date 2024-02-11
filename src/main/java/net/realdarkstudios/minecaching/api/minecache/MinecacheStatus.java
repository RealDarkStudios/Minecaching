package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.ChatColor;

public enum MinecacheStatus {
    PUBLISHED("published", "P", ChatColor.GREEN),
    NEEDS_MAINTENANCE("needs_maintenance", "M", ChatColor.LIGHT_PURPLE),
    DISABLED("disabled", "D", ChatColor.RED),
    ARCHIVED("archived", "A", ChatColor.GRAY),
    REVIEWING("reviewing", "R", ChatColor.YELLOW),
    INVALID("invalid", "I", ChatColor.DARK_RED);

    private final String id, character;
    private final ChatColor color;

    MinecacheStatus(String id, String character, ChatColor color) {
        this.id = id;
        this.character = character;
        this.color = color;
    }

    public static MinecacheStatus get(String status) {
        return switch (status.toLowerCase()) {
            case "published", "active" -> PUBLISHED;
            case "needs_maintenance" -> NEEDS_MAINTENANCE;
            case "disabled" -> DISABLED;
            case "archived" -> ARCHIVED;
            case "reviewing" -> REVIEWING;
            default -> INVALID;
        };
    }

    public String getTranslation() {
        return MinecachingAPI.getLocalization().getTranslation("minecache.status." + id);
    }

    public ChatColor getColor() {
        return color;
    }

    public String getCharacter() {
        return character;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return character;
    }
}
