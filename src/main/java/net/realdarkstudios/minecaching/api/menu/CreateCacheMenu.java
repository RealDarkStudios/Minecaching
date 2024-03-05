package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.menu.item.create.*;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.CloseMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class CreateCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public CreateCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super(MessageKeys.Menu.Create.TITLE, MenuSize.FOUR_ROW, plugin);
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
        boolean navLoc = coordCheck(cache.navLocation());
        boolean loc = coordCheck(cache.location());
        boolean cachedist = loc && cacheDistanceCheck(cache.location());
        boolean lodedist = loc && navLoc && distanceCheck(cache.location(), cache.navLocation(), Config.getInstance().getMaxLodestoneDistance());

        boolean ready = name && code && navLoc && loc && cachedist && lodedist && timeCheck;

        setItem(8, new CloseMenuItem());

        if (author.getCreatingCache().id().equals("NULL")) {
            setItem(13, new CreateCacheMenuItem(MessageKeys.Menu.Create.ITEM_CREATE.translate(),
                    new ItemStack(Material.GREEN_CONCRETE), List.of()));
        } else {

            setItem(0, new CreateCacheResetMenuItem(MessageKeys.Menu.Create.ITEM_RESET.translate(),
                    new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
            setItem(4, new CreateCachePreviewItem(cache));
            setItem(21, new CreateCacheNavCoordMenuItem(navLoc ? MessageKeys.Menu.Create.ITEM_NAVIGATION_LOCATION.translate(
                    MCUtils.formatLocation(MessageKeys.Menu.Data.PREVIEW_NAVIGATION_COORDS.translate(), cache.navLocation())) :
                    MessageKeys.Menu.Create.ITEM_NAVIGATION_COORDS.translate(),
                    new ItemStack(navLoc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(23, new CreateCacheCoordMenuItem(loc ? MessageKeys.Menu.Create.ITEM_LOCATION.translate(
                    MCUtils.formatLocation(MessageKeys.Menu.Data.PREVIEW_COORDS.translate(), cache.location())) :
                    MessageKeys.Menu.Create.ITEM_LOCATION_COORDS.translate() ,
                    new ItemStack(loc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(27, new CreateCacheTypeMenuItem(
                    new MenuItemState("traditional", MessageKeys.Menu.Create.TYPE_TRADITIONAL.translate(),
                            new ItemStack(Material.GREEN_TERRACOTTA),
                            List.of(MessageKeys.Menu.Create.TYPE_TRADITIONAL_LORE_1.translate(),
                                    MessageKeys.Menu.Create.TYPE_TRADITIONAL_LORE_2.translate())),
                    new MenuItemState("mystery", MessageKeys.Menu.Create.TYPE_MYSTERY.translate(),
                            new ItemStack(Material.BLUE_TERRACOTTA),
                            List.of(MessageKeys.Menu.Create.TYPE_MYSTERY_LORE_1.translate(),
                                    MessageKeys.Menu.Create.TYPE_MYSTERY_LORE_2.translate())),
                    new MenuItemState("multi", MessageKeys.Menu.Create.TYPE_MULTI.translate(),
                            new ItemStack(Material.YELLOW_TERRACOTTA),
                            List.of(MessageKeys.Menu.Create.TYPE_MULTI_LORE_1.translate(),
                                    MessageKeys.Menu.Create.TYPE_MULTI_LORE_2.translate()))
            ).fromId(MinecachingAPI.get().getPlayerData(player).getCreatingCache().type().getId()));
            setItem(29, new CreateCacheSetNameMenuItem(MessageKeys.Menu.Data.CACHE_NAME.translate(name ? cache.name() : "???"),
                    new ItemStack(name ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(31, new CreateCacheSaveMenuItem(MessageKeys.Menu.SAVE.translateWithOtherStyle(
                    ready ? MessageKeys.Menu.SAVE.styleOptions() : new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                    new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of(MessageKeys.Menu.SAVE_LORE.translate(),
                    !ready && !timeCheck ? MessageKeys.Error.Create.TIME.translate(
                            LocalDateTime.now().until(author.getCacheCooldownExpireTime(), ChronoUnit.MINUTES)) : "",
                    !ready && !cachedist && loc ? MessageKeys.Error.Create.TOO_CLOSE.translate(
                            Config.getInstance().getMinCacheDistance()) : "",
                    !ready && !lodedist && loc && navLoc ? MessageKeys.Error.Create.NAV_COORDS_TOO_FAR.translate() : "")));
            setItem(33, new CreateCacheSetCodeMenuItem(MessageKeys.Menu.Data.CACHE_CODE.translate(code ? cache.code() : "???"),
                    new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(35, new CreateCacheDeleteMenuItem(MessageKeys.Menu.CANCEL.translate(), new ItemStack(Material.GRAY_CONCRETE), List.of()));
        }

        super.update(player);
    }

    private static boolean cacheDistanceCheck(Location loc) {
        Minecache closestCache = MinecachingAPI.get().getSortedCaches(Comparator.comparingDouble(c -> c.location().distanceSquared(loc))).get(0);
        // Subtract 1 to account for the distance being exactly the minCacheDistance, which in this function would return false because distanceCheck would return true
        return !distanceCheck(closestCache.location(), loc, Config.getInstance().getMinCacheDistance() - 1);
    }

    private static boolean distanceCheck(Location a, Location b, int maxDist) {
        return a.distance(b) <= maxDist;
    }

    private boolean timeCheck(PlayerDataObject pdo) {
        return pdo.getCacheCooldownExpireTime().compareTo(LocalDateTime.now()) < 1;
    }
}
