package net.realdarkstudios.minecaching.api.misc;

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

    public String getTranslationKey() {
        return "plugin.notification." + id;
    }
}
