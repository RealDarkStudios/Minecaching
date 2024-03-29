package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.item.data.DeleteCacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.edit.*;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.misc.OpenConfirmationMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EditCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public EditCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super(MCMessageKeys.Menu.Edit.TITLE, MenuSize.FOUR_ROW, plugin, cache.id());
        this.author = author;
        this.cache = cache;

        update(author.getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getEditingCache();

        if (player.getUniqueId().equals(cache.owner()) || player.hasPermission("minecaching.admin.edit")) {
            MinecachingAPI.get().getPlayerData(player).setEditingCache(cache);
        } else {
            LocalizedMessages.send(player, MCMessageKeys.Permission.NO_PERMISSION_EDITCACHE);
            MinecachingAPI.get().getPlayerData(player).setEditingCache(Minecache.EMPTY);
            close(player);
        }

        clearAllSlots();
        fillEmptySlots();

        boolean ready = stringCheck(cache.name()) && stringCheck(cache.code()) && coordCheck(cache.navLocation()) && coordCheck(cache.location());

        setItem(0, new EditCacheResetMenuItem(MCMessageKeys.Menu.Edit.ITEM_RESET.translate(),
                new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of(), cache));
        setItem(4, new CacheMenuItem(MCMessageKeys.Menu.Edit.ITEM_PREVIEW.translate(
                cache.id(), stringCheck(cache.name()) ? cache.name() : "???"), cache));
        setItem(8, new OpenConfirmationMenuItem(new DeleteCacheMenuItem(cache), this));
        setItem(21, new EditCacheNavCoordMenuItem(coordCheck(cache.navLocation()) ?
                MCMessageKeys.Menu.Edit.ITEM_NAVIGATION_LOCATION.translate(MCUtils.formatLocation(
                        MCMessageKeys.Menu.Data.PREVIEW_NAVIGATION_COORDS.translate(), cache.navLocation())) :
                MCMessageKeys.Menu.Edit.ITEM_NAVIGATION_COORDS.translate(),
                new ItemStack(coordCheck(cache.navLocation()) ? Material.GREEN_WOOL : Material.RED_WOOL),
                List.of()));
        setItem(23, new EditCacheCoordMenuItem(coordCheck(cache.location()) ?
                MCMessageKeys.Menu.Edit.ITEM_LOCATION.translate(MCUtils.formatLocation(
                        MCMessageKeys.Menu.Data.PREVIEW_COORDS.translate(), cache.location())) :
                MCMessageKeys.Menu.Edit.ITEM_LOCATION_COORDS.translate(),
                new ItemStack(coordCheck(cache.location()) ? Material.GREEN_WOOL : Material.RED_WOOL),
                List.of()));
        setItem(29, new EditCacheSetNameMenuItem(MCMessageKeys.Menu.Data.CACHE_NAME.translate(stringCheck(cache.name()) ? cache.name() : "???"),
                new ItemStack(stringCheck(cache.name()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(31, new EditCacheSaveMenuItem(MCMessageKeys.Menu.SAVE.translateWithOtherStyle(
                ready ? MCMessageKeys.Menu.SAVE.styleOptions() : new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of(MCMessageKeys.Menu.SAVE_LORE.translate())));
        setItem(33, new EditCacheSetCodeMenuItem(MCMessageKeys.Menu.Data.CACHE_CODE.translate(stringCheck(cache.code()) ? cache.code() : "???"),
                new ItemStack(stringCheck(cache.code()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(35, new EditCacheCancelMenuItem(MCMessageKeys.Menu.Edit.ITEM_STOP_EDITING.translate(),
                new ItemStack(Material.GRAY_CONCRETE),
                List.of(MCMessageKeys.Menu.Edit.ITEM_STOP_EDITING_LORE.translate())));

        super.update(player);
    }
}
