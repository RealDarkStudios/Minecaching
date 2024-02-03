package net.realdarkstudios.minecaching.api.menu.item.editcache;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class ResetEditCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public ResetEditCacheMenuItem(String name, ItemStack item, Minecache cache) {
        super(name, item);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(MinecachingAPI.get().getMinecache(cache.id()));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
