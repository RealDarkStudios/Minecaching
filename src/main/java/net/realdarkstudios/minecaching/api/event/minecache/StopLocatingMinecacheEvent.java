package net.realdarkstudios.minecaching.api.event.minecache;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.commands.LocateCacheCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class StopLocatingMinecacheEvent extends MinecacheEvent {
    /**
     * Called when a player stops locating a {@link Minecache}
     *
     * @see LocateCacheCommand
     * @since 0.2.0.4
     */
    private final Player player;
    private final Location plrLocation;
    private final double distance;
    private final boolean locateCancelled;

    /**
     * Create a new StopLocatingMinecacheEvent
     *
     * @param minecache The {@link Minecache} that was being located
     * @param player The {@link Player} that was locating this minecache
     * @param plrLocation The {@link Location} of the player
     * @param distance The distance between the player and the Minecache, in a straight line
     */
    public StopLocatingMinecacheEvent(Minecache minecache, Player player, Location plrLocation, double distance, boolean locateCancelled) {
        this.minecache = minecache;
        this.player = player;
        this.plrLocation = plrLocation;
        this.distance = distance;
        this.locateCancelled = locateCancelled;
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

    public boolean isLocateCancelled() {
        return locateCancelled;
    }
}
