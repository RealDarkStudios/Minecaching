package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheEditedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;

public class EditCacheSaveMenuItem extends MenuItem {
    public EditCacheSaveMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (getItem().getType().equals(Material.BEDROCK)) return;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        Minecache cache = pdo.getEditingCache();

        if (cache.name() == null) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_NAME);
        } else if (cache.code() == null) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_CODE);
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_COORDS);
        } else if (!cacheDistanceCheck(cache.location())) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.TOO_CLOSE);
        } else if (cache.nx() == 0 && cache.ny() == 0 && cache.nz() == 0) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_NAV_COORDS);
        } else if (cache.navLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NAV_COORDS_TOO_FAR);
        } else {
            MinecacheEditedEvent cEvent = new MinecacheEditedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Edit.GENERAL);
                return;
            }

            if ((!event.getPlayer().getUniqueId().equals(cache.owner()))) {
                MinecachingAPI.get().createNotification(cache.owner(), event.getPlayer().getUniqueId(), NotificationType.EDIT, cache);
            }

            MinecachingAPI.get().saveMinecache(cache, false);
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Edit.SAVE, cache.id(), cache.name());
            cache = Minecache.EMPTY;
            cache.setID("NULL");
        }

        event.setClose(true);
    }

    public static boolean cacheDistanceCheck(Location loc) {
        Minecache closestCache = MinecachingAPI.get().getSortedCaches(Comparator.comparingDouble(c -> c.location().distanceSquared(loc))).get(0);
        // Subtract 1 to account for the distance being exactly the minCacheDistance, which in this function would return false because distanceCheck would return true
        return !distanceCheck(closestCache.location(), loc, Config.getInstance().getMinCacheDistance() - 1);
    }

    public static boolean distanceCheck(Location a, Location b, int maxDist) {
        return a.distance(b) <= maxDist;
    }

}
