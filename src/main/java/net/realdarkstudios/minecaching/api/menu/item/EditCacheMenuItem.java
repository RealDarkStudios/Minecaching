package net.realdarkstudios.minecaching.api.menu.item;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.EditCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EditCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public EditCacheMenuItem(Minecache cache) {
        super(ChatColor.LIGHT_PURPLE + "Edit Cache", new ItemStack(Material.PURPLE_CONCRETE));
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(cache);

        EditCacheMenu menu = MCMenus.get().getEditCacheMenu(pdo, cache);
        menu.open(event.getPlayer());
    }
}
