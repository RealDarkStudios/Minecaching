package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCacheMenuItem extends MenuItem {
    public CreateCacheMenuItem(String name, ItemStack item, List<String> lore) {
        super(ChatColor.GREEN + name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCreatingCache(Minecache.EMPTY.setID(Utils.generateCacheID(5)));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
