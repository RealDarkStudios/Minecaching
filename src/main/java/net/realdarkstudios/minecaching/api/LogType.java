package net.realdarkstudios.minecaching.api;

import org.bukkit.ChatColor;

public enum LogType  {
    FOUND("found", "Found on", ChatColor.YELLOW),
    DNF("dnf", "Did not find on", ChatColor.BLUE),
    NOTE("note", "Added note on", ChatColor.GRAY),
    REQUEST_REVIEW("request_review", "Requested owner attention on", ChatColor.RED),
    MAINTENANCE("maintenance", "Did maintenance on", ChatColor.GREEN),
    DISABLE("disable", "Disabled on", ChatColor.DARK_RED),
    ARCHIVE("archive", "Archived on", ChatColor.GRAY),
    INVALID("invalid", "Did something on", ChatColor.DARK_RED);

    private final String id, msg;
    private final ChatColor color;

    LogType(String id, String msg, ChatColor color) {
        this.id = id;
        this.msg = msg;
        this.color = color;
    }

    public static LogType get(String type) {
        return switch (type) {
            case "found" -> FOUND;
            case "dnf" -> DNF;
            case "note" -> NOTE;
            case "request_review" -> REQUEST_REVIEW;
            case "maintenance" -> MAINTENANCE;
            case "disable" -> DISABLE;
            case "archive" -> ARCHIVE;
            default -> INVALID;
        };
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return msg;
    }

    public ChatColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color + id + ChatColor.RESET;
    }
}
