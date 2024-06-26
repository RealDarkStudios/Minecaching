package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheCreatedEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.misc.Config;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.TIME,
                    LocalDateTime.now().until(pdo.getCacheCooldownExpireTime(), ChronoUnit.MINUTES));
        } else if (cache.name() == null) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_NAME);
        } else if (cache.code() == null || cache.code().isEmpty()) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_CODE);
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_COORDS);
        } else if (!isInBoundsCheck(cache.location())) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.OUT_OF_BOUNDS, MCUtils.formatLocation(cache.location()),
                    MCUtils.formatLocation(Config.getInstance().getMinLocation()), MCUtils.formatLocation(Config.getInstance().getMaxLocation()));
        } else if (!cacheDistanceCheck(cache.location())) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.TOO_CLOSE,
                    Config.getInstance().getMinCacheDistance());
        } else if (cache.nx() == 0 && cache.ny() == 0 && cache.nz() == 0) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NO_NAV_COORDS);
        } else if (!distanceCheck(cache.location(), cache.navLocation(), Config.getInstance().getMaxLodestoneDistance())) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.NAV_COORDS_TOO_FAR);
        } else {
            cache.setStatus(MinecacheStatus.REVIEWING)
                    .setAuthor(event.getPlayer().getUniqueId())
                    .setBlockType(cache.location().getBlock().getType())
                    .setHidden(LocalDateTime.now())
                    .setFTF(MCUtils.EMPTY_UUID);

            MinecacheCreatedEvent cEvent = new MinecacheCreatedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Create.GENERAL);
            }

            MinecachingAPI.get().saveMinecache(cache, true);

            if (Config.getInstance().experimentalFeatures()) {
                MinecachingAPI.info("Placing " + cache.id() + " chest at " + MCUtils.formatLocation(cache.world().getName(), cache.location()));
                Block containerBlock = cache.location().getBlock();
                containerBlock.setType(Material.CHEST);
                Chest chestData = (Chest) containerBlock.getState();
                chestData.getPersistentDataContainer().set(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING, cache.id());
                chestData.setCustomName(cache.id() + ": " + cache.name());
                chestData.update();
            }

            pdo.addHide(cache.id());
            pdo.setCreatingCache(Minecache.EMPTY.setID("NULL"));
            pdo.setCacheCooldownExpire(LocalDateTime.now().plusSeconds(Config.getInstance().getCacheCreateCooldown()));

            MinecachingAPI.get().save();
            MinecachingAPI.get().update();

            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Create.SAVE, cache.id(), cache.name());
        }

        event.setClose(true);
    }

    public static boolean cacheDistanceCheck(Location loc) {
        if (MinecachingAPI.get().getAllKnownCaches().isEmpty()) return true;
        ArrayList<Minecache> sameWorldCaches = MinecachingAPI.get().getFilteredCaches(c -> c.world() == loc.getWorld());
        if (sameWorldCaches.isEmpty()) return true;
        sameWorldCaches.sort(Comparator.comparingDouble(c -> c.location().distanceSquared(loc)));
        Minecache closestCache = sameWorldCaches.get(0);
        // Subtract 1 to account for the distance being exactly the minCacheDistance, which in this function would return false because distanceCheck would return true
        return !distanceCheck(closestCache.location(), loc, Config.getInstance().getMinCacheDistance() - 1);
    }

    public static boolean distanceCheck(Location a, Location b, int maxDist) {
        return a.distance(b) <= maxDist;
    }

    private static boolean isInBoundsCheck(Location loc) {
        return Config.getInstance().getCacheBounds().contains(loc.toVector(), loc.toVector());
    }

    private boolean timeCheck(PlayerDataObject pdo) {
        return pdo.getCacheCooldownExpireTime().compareTo(LocalDateTime.now()) < 1;
    }
}
