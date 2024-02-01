package net.realdarkstudios.minecaching.api.log;

public enum NotificationType {
    EDIT("edit"),
    ARCHIVAL("archival"),
    DELETION("deletion"),
    FLAG("flag"),
    DISABLE("disable"),
    PUBLISH("publish"),
    INVALID("invalid")
;
    private final String id, translationKey;

    NotificationType(String id) {
        this.id = id;
        this.translationKey = "plugin.notification." + id;
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
        return translationKey;
    }
}
