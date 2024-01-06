package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import org.bukkit.command.CommandSender;

public class MinecacheVerifiedEvent extends MinecacheEvent {
    private final CommandSender verifier;

    public MinecacheVerifiedEvent(Minecache minecache, CommandSender verifier) {
        this.minecache = minecache;
        this.verifier = verifier;
    }

    public CommandSender getVerifier() {
        return verifier;
    }
}
