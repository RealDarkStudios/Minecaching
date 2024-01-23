package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.LogCacheCommand;
import org.bukkit.entity.Player;

public class MinecacheFoundEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is found
     *
     * @see LogCacheCommand
     * @since 0.2.0.0
     */
    private final Player player;
    private final boolean isFTF;

    /**
     * Create a new MinecacheFoundEvent
     *
     * @param minecache The {@link Minecache} that was found
     * @param player The {@link Player} that found the Minecache
     * @param isFTF If this find is the first one for this cache
     */
    public MinecacheFoundEvent(Minecache minecache, Player player, boolean isFTF) {
        this.player = player;
        this.minecache = minecache;
        this.isFTF = isFTF;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isFTF() {
        return isFTF;
    }
}
