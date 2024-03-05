package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCacheDeleteMenuItem extends MenuItem {
    public CreateCacheDeleteMenuItem(String name, ItemStack item, List<String> lore) {
        super(MessageKeys.Menu.CANCEL.translate(), new ItemStack(Material.GRAY_CONCRETE), List.of());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCreatingCache(Minecache.EMPTY.setID("NULL"));
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
