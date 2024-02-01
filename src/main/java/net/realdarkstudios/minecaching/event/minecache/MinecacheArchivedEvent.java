package net.realdarkstudios.minecaching.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.commands.ArchiveCacheCommand;
import org.bukkit.command.CommandSender;

public class MinecacheArchivedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is archived
     *
     * @since 0.2.2.2
     * @see ArchiveCacheCommand
     */
    private final CommandSender archiver;

    /**
     * Create a new MinecacheArchivedEvent
     *
     * @param minecache The {@link Minecache} that was achived
     * @param archiver The {@link CommandSender} that archived the cache.
     */
    public MinecacheArchivedEvent(Minecache minecache, CommandSender archiver) {
        this.minecache = minecache;
        this.archiver = archiver;
    }

    public CommandSender getArchiver() {
        return archiver;
    }
}
