package net.realdarkstudios.minecaching.api.menu.item.editcache;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class CancelEditCacheMenuItem extends MenuItem {
    public CancelEditCacheMenuItem(String name, ItemStack item, String... lore) {
        super(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + name, item, lore);
    }

    @Override
    public ItemStack getIcon() {
        return super.getIcon();
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(Minecache.EMPTY.setID("NULL"));
        event.setClose(true);

        MCMenus.get().releaseEditCacheMenu(pdo);

        super.onItemClick(event);
    }
}
