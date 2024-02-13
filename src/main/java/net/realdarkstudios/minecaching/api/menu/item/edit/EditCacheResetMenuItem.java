package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditCacheResetMenuItem extends MenuItem {
    private final Minecache cache;

    public EditCacheResetMenuItem(String name, ItemStack item, List<String> lore, Minecache cache) {
        super(ChatColor.BOLD + "" + ChatColor.GRAY + name, item, lore);
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
