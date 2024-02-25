package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.minecaching.util.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

public class Log {
    public static final Log EMPTY = new Log(null, null, Utils.EMPTY_UUID, LogType.INVALID, LocalDateTime.now(), "", true, false);

    private final String cacheID, logID;
    private String log;
    private final UUID author;
    private LogType type;
    private final LocalDateTime time;
    private final boolean invalidated, isFTF;

    public Log(String cacheID, String logID, UUID author, LogType type, LocalDateTime time, String log, boolean invalidated, boolean isFTF) {
        this.cacheID = cacheID;
        this.logID = logID;
        this.author = author;
        this.type = type;
        this.time = time;
        this.log = log;
        this.invalidated = invalidated;
        this.isFTF = isFTF;
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
        return invalidated ? LogType.INVALID : type;
    }

    public LocalDateTime time() {
        return time;
    }

    public String log() {
        return log;
    }

    public boolean isFTF() {
        return isFTF;
    }

    public void setLog(String log) {
        this.log = log;
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

        return new Log(cacheID, key, lAuthor, lType, lTime, lLog, isInvalidated, MinecachingAPI.get().getMinecache(cacheID).ftf().equals(lAuthor));
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key + ".author", author.toString());
        yaml.set(key + ".type", type.getId());
        yaml.set(key + ".log", log);
        yaml.set(key + ".time", time.toString());
    }
}
