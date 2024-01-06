package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.VerifyCacheCommand;
import org.bukkit.command.CommandSender;

public class MinecacheVerifiedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is verified
     *
     * @since 2.0.0.5
     * @see VerifyCacheCommand
     */
    private final CommandSender verifier;

    /**
     * Create a new MinecacheVerifiedEvent
     *
     * @param minecache The {@link Minecache} that was verified
     * @param verifier The {@link CommandSender} that verified the cache.
     */
    public MinecacheVerifiedEvent(Minecache minecache, CommandSender verifier) {
        this.minecache = minecache;
        this.verifier = verifier;
    }

    public CommandSender getVerifier() {
        return verifier;
    }
}
