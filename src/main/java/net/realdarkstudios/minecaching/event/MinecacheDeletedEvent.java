package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.DeleteCacheCommand;
import org.bukkit.entity.Player;

public class MinecacheDeletedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is deleted
     *
     * @see DeleteCacheCommand
     * @since 2.0.0.2
     */
    private final Player author;

    /**
     * Create a new MinecacheDeletedEvent
     *
     * @param minecache The deleted {@link Minecache}
     * @param author The {@link Player} that owns that Minecache
     */
    public MinecacheDeletedEvent(Minecache minecache, Player author) {
        this.minecache = minecache;
        this.author = author;
    }

    public Player getAuthor() {
        return author;
    }
}
