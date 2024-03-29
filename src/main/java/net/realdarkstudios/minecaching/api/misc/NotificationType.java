package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;

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
            case ARCHIVAL -> MCMessageKeys.Plugin.Notification.NOTIFICATION_ARCHIVAL;
            case DELETION -> MCMessageKeys.Plugin.Notification.NOTIFICATION_DELETION;
            case DISABLE -> MCMessageKeys.Plugin.Notification.NOTIFICATION_DISABLE;
            case EDIT -> MCMessageKeys.Plugin.Notification.NOTIFICATION_EDIT;
            case FLAG -> MCMessageKeys.Plugin.Notification.NOTIFICATION_FLAG;
            case PUBLISH -> MCMessageKeys.Plugin.Notification.NOTIFICATION_PUBLISH;
            default -> MCMessageKeys.ERROR;
        };
    }
}
