package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.GoBackMenuItem;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.item.data.*;
import net.realdarkstudios.minecaching.api.menu.item.edit.EditCacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.log.FavoriteCacheItem;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.misc.OpenConfirmationMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CacheDataMenu extends MCMenu {
    protected Minecache cache;

    public CacheDataMenu(LocalizedMessages.Key titleKey, Minecache cache, JavaPlugin plugin, PlayerDataObject pdo) {
        super(titleKey, MCMenu.MenuSize.THREE_ROW, plugin, cache.id());
        this.cache = cache;

        update(pdo.getPlayer());
    }

    @Override
    public void update(Player player) {
        clearAllSlots();
        fillEmptySlots();

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(player);
        PlayerDataObject author = MinecachingAPI.get().getPlayerData(cache.originalAuthor());
        PlayerDataObject maintainer = MinecachingAPI.get().getPlayerData(cache.maintainer());
        boolean bypassPerm = player.hasPermission("minecaching.admin.bypass.datamenu");
        boolean playerAllowed = cache.owner().equals(pdo.getUniqueID()) || cache.originalAuthor().equals(pdo.getUniqueID()) || bypassPerm;

        setItem(0, new GoBackMenuItem(new ItemStack(Material.RED_CONCRETE), List.of()));
        if (playerAllowed) setItem(1, new EditCacheMenuItem(cache));
        setItem(3, new CacheMenuItem(MCMessageKeys.Menu.Data.PREVIEW.translate(cache.id(), cache.name()), cache));
        setItem(5, new MenuItem(MCMessageKeys.Menu.Data.PREVIEW_AUTHOR.translate(author.getUsername()),
                author.getSkullItemStack(), List.of()));
        if (cache.hasMaintainer()) setItem(6, new MenuItem(MCMessageKeys.Menu.Data.PREVIEW_MAINTAINER.translate(maintainer.getUsername()),
                maintainer.getSkullItemStack(), List.of()));
        if (playerAllowed) setItem(7, new OpenConfirmationMenuItem(new OpenMaintainerMenuItem(cache, this), this));
        if (playerAllowed) setItem(8, new OpenConfirmationMenuItem(new DeleteCacheMenuItem(cache), this));
        setItem(18, new FavoriteCacheItem(cache, pdo));
        switch(cache.status()) {
            case REVIEWING -> {
                //publish
                if (player.isOp()) setItem(22, new OpenConfirmationMenuItem(new PublishCacheMenuItem(cache), this));
                setItem(26, new LocateCacheMenuItem(cache));
            }
            case PUBLISHED -> {
                //disable, archive
                if (player.isOp()) setItem(21, new OpenConfirmationMenuItem(new DisableCacheMenuItem(cache), this));
                if (player.isOp()) setItem(23, new OpenConfirmationMenuItem(new ArchiveCacheMenuItem(cache), this));
                setItem(26, new LocateCacheMenuItem(cache));
            }
            case DISABLED -> {
                //publish, archive
                if (player.isOp()) setItem(21, new OpenConfirmationMenuItem(new PublishCacheMenuItem(cache), this));
                if (player.isOp()) setItem(23, new OpenConfirmationMenuItem(new ArchiveCacheMenuItem(cache), this));
            }
        }

        super.update(player);
    }
}
