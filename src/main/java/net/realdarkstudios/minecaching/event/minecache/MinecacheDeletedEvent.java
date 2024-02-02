package net.realdarkstudios.minecaching.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.commands.DeleteCacheCommand;
import org.bukkit.OfflinePlayer;

public class MinecacheDeletedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is deleted
     *
     * @see DeleteCacheCommand
     * @since 0.2.0.2
     */
    private final OfflinePlayer author;

    /**
     * Create a new MinecacheDeletedEvent
     *
     * @param minecache The deleted {@link Minecache}
     * @param author The {@link OfflinePlayer} that owns that Minecache
     */
    public MinecacheDeletedEvent(Minecache minecache, OfflinePlayer author) {
        this.minecache = minecache;
        this.author = author;
    }

    public OfflinePlayer getAuthor() {
        return author;
    }
}
