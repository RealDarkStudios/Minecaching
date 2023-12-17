package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

public class MCEventHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void blockBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        HashMap<Location, Minecache> locations = MinecacheStorage.getInstance().getLocations();

        for (Location cacheLocation: locations.keySet()) {
            if (cacheLocation.distance(loc) < 1) {
                Minecache cache = locations.get(cacheLocation);
                cache.setBlockType(Material.AIR);
                cache.setInvalidated(true);
            }
        }
    }
}
