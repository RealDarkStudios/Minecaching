package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.data.*;
import net.realdarkstudios.minecaching.api.menu.item.edit.EditCacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.misc.OpenConfirmationMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CacheDataMenu extends MCMenu {
    protected Minecache cache;

    public CacheDataMenu(String titleKey, Minecache cache, JavaPlugin plugin) {
        super(titleKey, MenuSize.THREE_ROW, plugin, cache.id());
        this.cache = cache;

        setItem(0, new GoBackMenuItem(translation("menu.goback"), new ItemStack(Material.RED_CONCRETE), List.of()));
        setItem(4, new CacheMenuItem(itemTranslation("preview", cache.id(), cache.name()), cache));
        setItem(8, new OpenConfirmationMenuItem(new DeleteCacheMenuItem(itemTranslation("delete", cache.id()), new ItemStack(Material.BLACK_CONCRETE), List.of("This WILL delete this cache FOREVER!"), cache), this));
        setItem(18, new EditCacheMenuItem(cache));
        switch(cache.status()) {
            case REVIEWING -> {
                //publish
                setItem(22, new OpenConfirmationMenuItem(new PublishCacheMenuItem(cache), this));
                setItem(26, new LocateCacheMenuItem(cache));
            }
            case PUBLISHED -> {
                //disable, archive
                setItem(21, new OpenConfirmationMenuItem(new DisableCacheMenuItem(cache), this));
                setItem(23, new OpenConfirmationMenuItem(new ArchiveCacheMenuItem(cache), this));
                setItem(26, new LocateCacheMenuItem(cache));
            }
            case DISABLED -> {
                //publish, archive
                setItem(21, new OpenConfirmationMenuItem(new PublishCacheMenuItem(cache), this));
                setItem(23, new OpenConfirmationMenuItem(new ArchiveCacheMenuItem(cache), this));
            }
        }
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.data.item." + id, substitutions);
    }
}
