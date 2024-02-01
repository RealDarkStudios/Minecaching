package net.realdarkstudios.minecaching.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.commands.LocateCacheCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class StartLocatingMinecacheEvent extends MinecacheEvent {
    /**
     * Called when a player starts locating a {@link Minecache}
     *
     * @see LocateCacheCommand
     * @since 0.2.0.4
     */
    private final Minecache minecache;
    private final Player player;
    private final Location plrLocation;
    private final double distance;

    /**
     * Create a new StartLocatingMinecacheEvent
     *
     * @param minecache The {@link Minecache} being located
     * @param player The {@link Player} locating this minecache
     * @param plrLocation The {@link Location} of the player
     * @param distance The distance between the player and the Minecache, in a straight line
     */
    public StartLocatingMinecacheEvent(Minecache minecache, Player player, Location plrLocation, double distance) {
        this.minecache = minecache;
        this.player = player;
        this.plrLocation = plrLocation;
        this.distance = distance;
    }

    public Minecache getCache() {
        return minecache;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getPlrLocation() {
        return plrLocation;
    }

    public double getDistance() {
        return distance;
    }
}
