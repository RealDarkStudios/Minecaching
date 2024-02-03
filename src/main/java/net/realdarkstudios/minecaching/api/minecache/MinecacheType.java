package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.ChatColor;

public enum MinecacheType {
    TRADITIONAL("traditional", ChatColor.DARK_GREEN),
    MYSTERY("mystery", ChatColor.BLUE),
    MULTI("multi", ChatColor.GOLD),
    INVALID("invalid", ChatColor.DARK_RED);

    private final String id;
    private final ChatColor color;

    MinecacheType(String id, ChatColor color) {
        this.id = id;
        this.color = color;
    }

    public static MinecacheType get(String type) {
        return switch (type.toLowerCase()) {
          case "traditional" -> TRADITIONAL;
          case "mystery" -> MYSTERY;
          case "multi" -> MULTI;
          default -> INVALID;
        };
    }

    public String getId() {
        return id;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getTranslation() {
        return MinecachingAPI.getLocalization().getTranslation("minecache.type." + id);
    }

    @Override
    public String toString() {
        return id.replace("_", "");
    }


}
