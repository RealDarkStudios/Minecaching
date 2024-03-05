package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;

public enum NotificationType {
    EDIT("edit"),
    ARCHIVAL("archival"),
    DELETION("deletion"),
    FLAG("flag"),
    DISABLE("disable"),
    PUBLISH("publish"),
    INVALID("invalid")
;
    private final String id;

    NotificationType(String id) {
        this.id = id;
    }

    public static NotificationType get(String type) {
        return switch (type) {
            case "edit" -> EDIT;
            case "archival" -> ARCHIVAL;
            case "deletion" -> DELETION;
            case "flag" -> FLAG;
            case "disable" -> DISABLE;
            case "publish" -> PUBLISH;
            default -> INVALID;
        };
    }

    public String getId() {
        return id;
    }

    public LocalizedMessages.Key getTranslationKey() {
        return switch (this) {
            case ARCHIVAL -> MessageKeys.Plugin.Notification.NOTIFICATION_ARCHIVAL;
            case DELETION -> MessageKeys.Plugin.Notification.NOTIFICATION_DELETION;
            case DISABLE -> MessageKeys.Plugin.Notification.NOTIFICATION_DISABLE;
            case EDIT -> MessageKeys.Plugin.Notification.NOTIFICATION_EDIT;
            case FLAG -> MessageKeys.Plugin.Notification.NOTIFICATION_FLAG;
            case PUBLISH -> MessageKeys.Plugin.Notification.NOTIFICATION_PUBLISH;
            default -> MessageKeys.ERROR;
        };
    }
}
