package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.LocalDateTime;

public class MinecacheCreatedEvent extends Event implements Cancellable {
    private final Minecache minecache;
    private final Player author;
    private final LocalDateTime hidden;
    private final MinecacheType type;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public MinecacheCreatedEvent(Minecache minecache, Player author) {
        this.minecache = minecache;
        this.author = author;
        this.hidden = minecache.hidden();
        this.type = minecache.type();
    }

    public Minecache getCache() {
        return minecache;
    }

    public Player getAuthor() {
        return author;
    }

    public LocalDateTime getHidden() {
        return hidden;
    }

    public MinecacheType getType() {
        return type;
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
