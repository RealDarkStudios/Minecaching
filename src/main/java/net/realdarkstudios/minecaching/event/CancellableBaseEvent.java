package net.realdarkstudios.minecaching.event;

import org.bukkit.event.Cancellable;

public abstract class CancellableBaseEvent extends BaseEvent implements Cancellable {
    /**
     * An extension of {@link BaseEvent} than adds a default isCancelled() and setCancelled() implementation.
     *
     * @see BaseEvent
     * @since 0.2.0.4
     */
    private boolean cancelled;

    /**
     * {@inheritDoc}
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
