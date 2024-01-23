package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.PublishCacheCommand;
import org.bukkit.command.CommandSender;

public class MinecachePublishedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is verified
     *
     * @since 0.2.0.5
     * @see PublishCacheCommand
     */
    private final CommandSender verifier;

    /**
     * Create a new MinecacheVerifiedEvent
     *
     * @param minecache The {@link Minecache} that was verified
     * @param verifier The {@link CommandSender} that verified the cache.
     */
    public MinecachePublishedEvent(Minecache minecache, CommandSender verifier) {
        this.minecache = minecache;
        this.verifier = verifier;
    }

    public CommandSender getVerifier() {
        return verifier;
    }
}
