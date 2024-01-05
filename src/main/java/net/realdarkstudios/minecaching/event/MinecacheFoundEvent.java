package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinecacheFoundEvent extends Event {
    private final Player player;
    private final Minecache minecache;
    private final boolean isFTF;
    private static final HandlerList handlers = new HandlerList();

    public MinecacheFoundEvent(Player player, Minecache minecache, boolean isFTF) {
        this.player = player;
        this.minecache = minecache;
        this.isFTF = isFTF;
    }

    public Player getPlayer() {
        return player;
    }

    public Minecache getCache() {
        return minecache;
    }

    public boolean isFTF() {
        return isFTF;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
