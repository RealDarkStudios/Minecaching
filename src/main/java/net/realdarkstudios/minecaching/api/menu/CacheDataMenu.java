package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CacheDataMenu extends MCMenu {
    protected Minecache cache;

    public CacheDataMenu(String title, Minecache cache, JavaPlugin plugin) {
        super(title, MenuSize.THREE_ROW, plugin);
        this.cache = cache;

        setItem(0, new GoBackMenuItem("Back", new ItemStack(Material.RED_CONCRETE)));
        setItem(4, new CacheMenuItem(cache));
        setItem(8, new DeleteCacheMenuItem("Delete", new ItemStack(Material.BLACK_CONCRETE), cache, "This WILL delete this cache FOREVER!"));
        switch(cache.status()) {
            case REVIEWING -> {
                //publish, edit
                setItem(18, new EditCacheMenuItem(cache));
                setItem(22, new PublishCacheMenuItem(cache));
            }
            case PUBLISHED -> {
                //edit, disable, archive
                setItem(18, new EditCacheMenuItem(cache));
                setItem(21, new DisableCacheMenuItem(cache));
                setItem(23, new ArchiveCacheMenuItem(cache));
            }
            case DISABLED -> {
                //edit, publish, archive
                setItem(18, new EditCacheMenuItem(cache));
                setItem(21, new PublishCacheMenuItem(cache));
                setItem(23, new ArchiveCacheMenuItem(cache));
            }
        }
    }

    @Override
    public void update(Player player) {

    }
}
