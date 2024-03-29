package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCacheMenuItem extends MenuItem {
    public CreateCacheMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCreatingCache(Minecache.EMPTY.setID(MCUtils.generateCacheID(5)));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
