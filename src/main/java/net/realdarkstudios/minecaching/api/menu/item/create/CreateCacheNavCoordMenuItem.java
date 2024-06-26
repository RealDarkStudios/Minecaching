package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCacheNavCoordMenuItem extends MenuItem {
    public CreateCacheNavCoordMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCreatingCache(pdo.getCreatingCache().setNavLocation(event.getPlayer().getLocation()));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
