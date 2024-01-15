package net.realdarkstudios.minecaching.api;

import org.bukkit.ChatColor;

public enum LogType  {
    FOUND("found", "Found on", ChatColor.GOLD),
    DNF("dnf", "DNF on", ChatColor.BLUE),
    NOTE("note", "Note on", ChatColor.GRAY),
    REQUEST_REVIEW("request_review", "Request review on", ChatColor.RED),
    MAINTENANCE("maintenance", "Maintain on", ChatColor.GREEN),
    DISABLE("disable", "Disable on", ChatColor.DARK_RED),
    ARCHIVE("archive", "Archive on", ChatColor.GRAY),
    PUBLISH("publish", "Publish on", ChatColor.DARK_GREEN),
    INVALID("invalid", "Action on", ChatColor.DARK_RED);

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
            case "requestreview", "request_review" -> REQUEST_REVIEW;
            case "maintenance" -> MAINTENANCE;
            case "disable" -> DISABLE;
            case "archive" -> ARCHIVE;
            case "publish" -> PUBLISH;
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
        return id;
    }

    public String toLogFormat() {
        return color + msg + ChatColor.RESET;
    }
}
