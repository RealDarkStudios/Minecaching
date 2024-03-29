package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheEditedEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_NAME);
        } else if (cache.code() == null) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_CODE);
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_COORDS);
        } else if (cache.nx() == 0 && cache.ny() == 0 && cache.nz() == 0) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_NAV_COORDS);
        } else if (cache.navLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NAV_COORDS_TOO_FAR);
        } else {
            MinecacheEditedEvent cEvent = new MinecacheEditedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Edit.GENERAL);
                return;
            }

            if ((!event.getPlayer().getUniqueId().equals(cache.owner()))) {
                MinecachingAPI.get().createNotification(cache.owner(), event.getPlayer().getUniqueId(), NotificationType.EDIT, cache);
            }

            if (Config.getInstance().experimentalFeatures()) {
                Minecache oldCache = MinecachingAPI.get().getMinecache(cache.id());

                MinecachingAPI.info("Updating " + cache.id() + " chest at " + MCUtils.formatLocation(cache.world().getName(), cache.location()));
                Block containerBlock = cache.location().getBlock();

                // Handles changed coords
                if (!oldCache.location().equals(cache.location())) {
                    oldCache.location().getBlock().setType(oldCache.blockType());
                    cache.setBlockType(containerBlock.getType());
                }

                containerBlock.setType(Material.CHEST);
                Chest chestData = (Chest) containerBlock.getState();
                chestData.getPersistentDataContainer().set(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING, cache.id());
                chestData.setCustomName(cache.id() + ": " + cache.name());
                chestData.update();
            }

            MinecachingAPI.get().saveMinecache(cache, false);
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Edit.SAVE, cache.id(), cache.name());
            pdo.setEditingCache(Minecache.EMPTY.setID("NULL"));
        }

        event.setClose(true);
    }

    public static boolean cacheDistanceCheck(Location loc) {
        if (MinecachingAPI.get().getAllKnownCaches().isEmpty()) return true;
        Minecache closestCache = MinecachingAPI.get().getSortedCaches(Comparator.comparingDouble(c -> c.location().distanceSquared(loc))).get(0);
        // Subtract 1 to account for the distance being exactly the minCacheDistance, which in this function would return false because distanceCheck would return true
        return !distanceCheck(closestCache.location(), loc, Config.getInstance().getMinCacheDistance() - 1);
    }

    public static boolean distanceCheck(Location a, Location b, int maxDist) {
        return a.distance(b) <= maxDist;
    }

}
