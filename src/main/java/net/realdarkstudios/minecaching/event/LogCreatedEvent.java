package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.LogType;
import net.realdarkstudios.minecaching.api.Minecache;
import org.bukkit.entity.Player;

public class LogCreatedEvent extends BaseEvent {
    private final Minecache minecache;
    private final String logId;
    private final LogType type;
    private final Player author;
    public LogCreatedEvent(Minecache minecache, String logId, LogType type, Player author) {
        this.minecache = minecache;
        this.logId = logId;
        this.type = type;
        this.author = author;
    }

    public Minecache getCache() {
        return minecache;
    }

    public Player getAuthor() {
        return author;
    }

    public String getLogId() {
        return logId;
    }

    public LogType getType() {
        return type;
    }
}
