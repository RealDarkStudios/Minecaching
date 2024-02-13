package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeleteCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public DeleteCacheMenuItem(String name, ItemStack item, List<String> lore, Minecache cache) {
        super(ChatColor.DARK_RED + "" + ChatColor.BOLD + name, item, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        MinecachingAPI.get().deleteMinecache(cache, event.getPlayer().getUniqueId());
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(Minecache.EMPTY);

        //TODO: Add other cache deletion checks
        event.setClose(true);

        super.onItemClick(event);
    }
}
