package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

public class Log {
    private String cacheID, logID, log;
    private UUID author;
    private LogType type;
    private LocalDateTime time;
    private boolean invalidated;

    public Log(String cacheID, String logID, UUID author, LogType type, LocalDateTime time, String log, boolean invalidated) {
        this.cacheID = cacheID;
        this.logID = logID;
        this.author = author;
        this.type = type;
        this.time = time;
        this.log = log;
        this.invalidated = invalidated;
    }

    public String cacheId() {
        return cacheID;
    }

    public String logId() {
        return logID;
    }

    public UUID author() {
        return author;
    }

    public LogType type() {
        return type;
    }

    public LocalDateTime time() {
        return time;
    }

    public String log() {
        return log;
    }

    public static Log fromYaml(YamlConfiguration yaml, String key, String cacheID) {
        boolean isInvalidated = false;
        String author = yaml.getString(key + ".author");
        UUID lAuthor;
        String type = yaml.getString(key + ".type");
        LocalDateTime lTime;
        String lLog = yaml.getString(key + ".log");
        LogType lType;
        if (type == null) { lType = LogType.INVALID; isInvalidated = true; } else { lType = LogType.get(type); }
        try { lAuthor = UUID.fromString(author); } catch (Exception e) { lAuthor = Utils.EMPTY_UUID; isInvalidated = true; }
        try { lTime = LocalDateTime.parse(yaml.getString(key + ".time")); } catch (Exception e) { lTime = LocalDateTime.now(); isInvalidated = true; }

        return new Log(cacheID, key, lAuthor, lType, lTime, lLog, isInvalidated);
    }

    public void toYaml(YamlConfiguration yaml, String key) {

    }
}
