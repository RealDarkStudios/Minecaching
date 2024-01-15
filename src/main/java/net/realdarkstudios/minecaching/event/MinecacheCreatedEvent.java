package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheType;
import net.realdarkstudios.minecaching.commands.AddCacheCommand;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class MinecacheCreatedEvent extends MinecacheEvent {
    /**
     * Called when a new {@link Minecache} is created
     *
     * @see AddCacheCommand
     * @since 2.0.0.1
     */

    private final Player author;
    private final LocalDateTime hidden;
    private final MinecacheType type;

    /**
     * Create a new MinecacheCreatedEvent
     *
     * @param minecache The newly created {@link Minecache}
     * @param author The {@link Player} who made the newly created cache
     */
    public MinecacheCreatedEvent(Minecache minecache, Player author) {
        this.minecache = minecache;
        this.author = author;
        this.hidden = minecache.hidden();
        this.type = minecache.type();
    }


    public Player getAuthor() {
        return author;
    }

    public LocalDateTime getHidden() {
        return hidden;
    }

    public MinecacheType getType() {
        return type;
    }
}
