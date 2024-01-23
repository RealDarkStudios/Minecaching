package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Log;
import net.realdarkstudios.minecaching.api.LogType;
import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.LogCacheCommand;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LogCreatedEvent extends BaseEvent {
    /**
     * Called when a {@link Log} is created
     *
     * @since 0.2.1.2
     * @see LogCacheCommand
     */
    private final Minecache minecache;
    private final String logId;
    private final LogType type;
    private final UUID author;
    public LogCreatedEvent(Minecache minecache, String logId, LogType type, Player author) {
        this.minecache = minecache;
        this.logId = logId;
        this.type = type;
        this.author = author.getUniqueId();
    }

    public LogCreatedEvent(Minecache minecache, String logId, LogType type, UUID author) {
        this.minecache = minecache;
        this.logId = logId;
        this.type = type;
        this.author = author;
    }

    public Minecache getCache() {
        return minecache;
    }

    public UUID getAuthor() {
        return author;
    }

    public String getLogId() {
        return logId;
    }

    public LogType getType() {
        return type;
    }
}
