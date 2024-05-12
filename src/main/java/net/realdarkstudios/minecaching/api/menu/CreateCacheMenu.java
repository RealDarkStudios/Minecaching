package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.CloseMenuItem;
import net.realdarkstudios.commons.menu.item.MenuItemState;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.item.create.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreateCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public CreateCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super(MCMessageKeys.Menu.Create.TITLE, MenuSize.FOUR_ROW, plugin);
        this.author = author;
        this.cache = cache;

        update(author.getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getCreatingCache();
        clearAllSlots();
        fillEmptySlots();

        boolean timeCheck = timeCheck(author);
        boolean name = stringCheck(cache.name());
        boolean code = stringCheck(cache.code());
        boolean hint = stringCheck(cache.hint());
        boolean navLoc = coordCheck(cache.navLocation());
        boolean loc = coordCheck(cache.location());
        boolean cachedist = loc && cacheDistanceCheck(cache.location());
        boolean lodedist = loc && navLoc && distanceCheck(cache.location(), cache.navLocation(), Config.getInstance().getMaxLodestoneDistance());
        boolean boundsCheck = isInBoundsCheck(cache.location());

        boolean ready = name && code && navLoc && loc && cachedist && lodedist && timeCheck && boundsCheck;

        setItem(8, new CloseMenuItem());

        if (author.getCreatingCache().id().equals("NULL")) {
            setItem(13, new CreateCacheMenuItem(MCMessageKeys.Menu.Create.ITEM_CREATE.translate(),
                    new ItemStack(Material.GREEN_CONCRETE), List.of()));
        } else {
            setItem(0, new CreateCacheResetMenuItem(MCMessageKeys.Menu.Create.ITEM_RESET.translate(),
                    new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
            setItem(4, new CreateCachePreviewItem(cache));
            setItem(21, new CreateCacheNavCoordMenuItem(navLoc ? MCMessageKeys.Menu.Create.ITEM_NAVIGATION_LOCATION.translate(
                    MCUtils.formatLocation(MCMessageKeys.Menu.Data.PREVIEW_NAVIGATION_COORDS.translate(), cache.navLocation())) :
                    MCMessageKeys.Menu.Create.ITEM_NAVIGATION_COORDS.translate(),
                    new ItemStack(navLoc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(22, new CreateCacheHintMenuItem(MCMessageKeys.Menu.Data.CACHE_HINT.translate(hint ? cache.hint() : "???"),
                    new ItemStack(hint ? Material.GREEN_WOOL : Material.YELLOW_WOOL), List.of()));
            setItem(23, new CreateCacheCoordMenuItem(loc ? MCMessageKeys.Menu.Create.ITEM_LOCATION.translate(
                    MCUtils.formatLocation(MCMessageKeys.Menu.Data.PREVIEW_COORDS.translate(), cache.location())) :
                    MCMessageKeys.Menu.Create.ITEM_LOCATION_COORDS.translate() ,
                    new ItemStack(loc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(27, new CreateCacheTypeMenuItem(
                    new MenuItemState("traditional", MCMessageKeys.Menu.Create.TYPE_TRADITIONAL.translate(),
                            new ItemStack(Material.GREEN_TERRACOTTA),
                            List.of(MCMessageKeys.Menu.Create.TYPE_TRADITIONAL_LORE_1.translate(),
                                    MCMessageKeys.Menu.Create.TYPE_TRADITIONAL_LORE_2.translate())),
                    new MenuItemState("mystery", MCMessageKeys.Menu.Create.TYPE_MYSTERY.translate(),
                            new ItemStack(Material.BLUE_TERRACOTTA),
                            List.of(MCMessageKeys.Menu.Create.TYPE_MYSTERY_LORE_1.translate(),
                                    MCMessageKeys.Menu.Create.TYPE_MYSTERY_LORE_2.translate())),
                    new MenuItemState("multi", MCMessageKeys.Menu.Create.TYPE_MULTI.translate(),
                            new ItemStack(Material.YELLOW_TERRACOTTA),
                            List.of(MCMessageKeys.Menu.Create.TYPE_MULTI_LORE_1.translate(),
                                    MCMessageKeys.Menu.Create.TYPE_MULTI_LORE_2.translate()))
            ).fromId(MinecachingAPI.get().getPlayerData(player).getCreatingCache().type().getId()));
            setItem(29, new CreateCacheSetNameMenuItem(MCMessageKeys.Menu.Data.CACHE_NAME.translate(name ? cache.name() : "???"),
                    new ItemStack(name ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(31, new CreateCacheSaveMenuItem(MCMessageKeys.Menu.SAVE.translateWithOtherStyle(
                    ready ? MCMessageKeys.Menu.SAVE.styleOptions() : new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                    new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of(MCMessageKeys.Menu.SAVE_LORE.translate(),
                    !ready && !boundsCheck ? MCMessageKeys.Error.Create.OUT_OF_BOUNDS.translate(
                            MCUtils.formatLocation(cache.location()),
                            MCUtils.formatLocation(Config.getInstance().getMinLocation()),
                            MCUtils.formatLocation(Config.getInstance().getMaxLocation())) : "",
                    !ready && !timeCheck ? MCMessageKeys.Error.Create.TIME.translate(
                            LocalDateTime.now().until(author.getCacheCooldownExpireTime(), ChronoUnit.MINUTES)) : "",
                    !ready && !cachedist && loc ? MCMessageKeys.Error.Create.TOO_CLOSE.translate(
                            Config.getInstance().getMinCacheDistance()) : "",
                    !ready && !lodedist && loc && navLoc ? MCMessageKeys.Error.Create.NAV_COORDS_TOO_FAR.translate() : "")));
            setItem(33, new CreateCacheSetCodeMenuItem(MCMessageKeys.Menu.Data.CACHE_CODE.translate(code ? cache.code() : "???"),
                    new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(35, new CreateCacheDeleteMenuItem(MCMessageKeys.Menu.CANCEL.translate(), new ItemStack(Material.GRAY_CONCRETE), List.of()));
        }

        super.update(player);
    }

    private static boolean cacheDistanceCheck(Location loc) {
        if (MinecachingAPI.get().getAllKnownCaches().isEmpty()) return true;
        ArrayList<Minecache> sameWorldCaches = MinecachingAPI.get().getFilteredCaches(c -> c.world() == loc.getWorld());
        if (sameWorldCaches.isEmpty()) return true;
        sameWorldCaches.sort(Comparator.comparingDouble(c -> c.location().distanceSquared(loc)));
        Minecache closestCache = sameWorldCaches.get(0);
        // Subtract 1 to account for the distance being exactly the minCacheDistance, which in this function would return false because distanceCheck would return true
        return !distanceCheck(closestCache.location(), loc, Config.getInstance().getMinCacheDistance() - 1);
    }

    private static boolean distanceCheck(Location a, Location b, int maxDist) {
        return a.distance(b) <= maxDist;
    }

    private static boolean isInBoundsCheck(Location loc) {
        return Config.getInstance().getCacheBounds().contains(loc.toVector(), loc.toVector());
    }

    private boolean timeCheck(PlayerDataObject pdo) {
        return pdo.getCacheCooldownExpireTime().compareTo(LocalDateTime.now()) < 1;
    }
}
