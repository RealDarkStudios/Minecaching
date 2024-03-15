package net.realdarkstudios.minecaching.api.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.commands.ArchiveCacheCommand;
import org.bukkit.command.CommandSender;

public class MinecacheDisabledEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is disabled
     *
     * @since 0.2.2.2
     * @see ArchiveCacheCommand
     */
    private final CommandSender disabler;

    /**
     * Create a new MinecacheDisabledEvent
     *
     * @param minecache The {@link Minecache} that was disabled
     * @param disabler The {@link CommandSender} that disabled the cache.
     */
    public MinecacheDisabledEvent(Minecache minecache, CommandSender disabler) {
        this.minecache = minecache;
        this.disabler = disabler;
    }

    public CommandSender getDisabler() {
        return disabler;
    }
}
