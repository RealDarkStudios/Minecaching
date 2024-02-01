package net.realdarkstudios.minecaching.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.CancellableBaseEvent;

public abstract class MinecacheEvent extends CancellableBaseEvent {
    /**
     * A base event for Minecache-related events that contains a getCache() method
     *
     * @see CancellableBaseEvent
     * @since 0.2.0.4
     */
    Minecache minecache;

    public Minecache getCache() {
        return minecache;
    }
}
