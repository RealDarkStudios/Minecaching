package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.commons.misc.IYamlSerializableEnum;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum LogType implements IYamlSerializableEnum {
    FOUND("found", ChatColor.GOLD),
    DNF("dnf", ChatColor.BLUE),
    NOTE("note", ChatColor.GRAY),
    FLAG("flag", ChatColor.RED),
    MAINTENANCE("maintenance", ChatColor.GREEN),
    DISABLE("disable", ChatColor.RED),
    ARCHIVE("archive", ChatColor.GRAY),
    PUBLISH("publish", ChatColor.DARK_GREEN),
    INVALID("invalid", ChatColor.DARK_RED);

    private final String id;
    private final ChatColor color;

    LogType(String id, ChatColor color) {
        this.id = id;
        this.color = color;
    }

    public static LogType get(String type) {
        return switch (type.toLowerCase()) {
            case "found" -> FOUND;
            case "dnf" -> DNF;
            case "note" -> NOTE;
            case "requestreview", "request_review", "flag" -> FLAG;
            case "maintenance" -> MAINTENANCE;
            case "disable" -> DISABLE;
            case "archive" -> ARCHIVE;
            case "publish" -> PUBLISH;
            default -> INVALID;
        };
    }

    @NotNull
    public String getId() {
        return id;
    }

    public String getMessage() {
        return MinecachingAPI.getLocalization().getTranslation("plugin.log." + id);
    }

    public LocalizedMessages.Key getMenuMessageKey() {
        return switch (this) {
            case FOUND -> MCMessageKeys.Menu.Log.TYPE_FOUND;
            case DNF -> MCMessageKeys.Menu.Log.TYPE_DNF;
            case NOTE -> MCMessageKeys.Menu.Log.TYPE_NOTE;
            case FLAG -> MCMessageKeys.Menu.Log.TYPE_FLAG;
            default -> null;
        };
    }

    public ChatColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return id;
    }

    public String toLogFormat() {
        return color + getMessage() + ChatColor.RESET;
    }
}
