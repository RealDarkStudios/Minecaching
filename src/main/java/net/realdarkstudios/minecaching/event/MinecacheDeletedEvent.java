package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinecacheDeletedEvent extends Event implements Cancellable {
    private final Minecache minecache;
    private final Player author;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public MinecacheDeletedEvent(Minecache minecache, Player author) {
        this.minecache = minecache;
        this.author = author;
    }

    public Minecache getCache() {
        return minecache;
    }

    public Player getAuthor() {
        return author;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
