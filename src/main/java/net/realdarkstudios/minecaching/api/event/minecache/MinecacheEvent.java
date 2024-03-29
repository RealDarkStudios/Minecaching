package net.realdarkstudios.minecaching.api.event.minecache;

import net.realdarkstudios.commons.event.CancellableBaseEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;

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
