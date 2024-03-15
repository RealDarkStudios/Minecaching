package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class CreateCacheSaveMenuItem extends MenuItem {
    public CreateCacheSaveMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (getItem().getType().equals(Material.BEDROCK)) return;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        Minecache cache = pdo.getCreatingCache();

        if (!timeCheck(pdo)) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.TIME, LocalDateTime.now().until(pdo.getCacheCooldownExpireTime(), ChronoUnit.MINUTES));
        } else if (cache.name() == null) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_NAME);
        } else if (cache.code() == null || cache.code().isEmpty()) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_CODE);
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_COORDS);
        } else if (!cacheDistanceCheck(cache.location())) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.TOO_CLOSE);
        } else if (cache.nx() == 0 && cache.ny() == 0 && cache.nz() == 0) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NO_NAV_COORDS);
        } else if (!distanceCheck(cache.location(), cache.navLocation(), Config.getInstance().getMaxLodestoneDistance())) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.NAV_COORDS_TOO_FAR);
        } else {
            cache.setStatus(MinecacheStatus.REVIEWING).setAuthor(event.getPlayer().getUniqueId()).setBlockType(cache.navLocation().getBlock().getType()).setHidden(LocalDateTime.now()).setFTF(MCUtils.EMPTY_UUID);

            MinecacheCreatedEvent cEvent = new MinecacheCreatedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Create.GENERAL);
            }

            MinecachingAPI.get().saveMinecache(cache, true);

            pdo.addHide(cache.id());
            pdo.setCreatingCache(Minecache.EMPTY.setID("NULL"));
            pdo.setCacheCooldownExpire(LocalDateTime.now().plusMinutes(15));

            MinecachingAPI.get().save();
            MinecachingAPI.get().update();

            LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Create.SAVE, cache.id(), cache.name());
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

    private boolean timeCheck(PlayerDataObject pdo) {
        return pdo.getCacheCooldownExpireTime().compareTo(LocalDateTime.now()) < 1;
    }
}
