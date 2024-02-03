package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class EditCacheCoordMenuItem extends MenuItem {
    public EditCacheCoordMenuItem(String name, ItemStack item, String... lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(pdo.getEditingCache().setLocation(event.getPlayer().getLocation()));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
