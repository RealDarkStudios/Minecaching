package net.realdarkstudios.minecaching.api;

public enum LogType  {
    FOUND("found", "Found on"),
    DNF("dnf", "Did not find on"),
    NOTE("note", "Added note on"),
    REQUEST_REVIEW("request_review", "Requested owner attention on"),
    MAINTENANCE("maintenance", "Did maintenance on"),
    DISABLE("disable", "Disabled on"),
    ARCHIVE("archive", "Archived on"),
    INVALID("invalid", "Did something on");

    private String id, msg;

    LogType(String id, String msg) {
        this.id = id;
        this.msg = msg;
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

    @Override
    public String toString() {
        return id;
    }

}
