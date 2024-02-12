package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CLMToggleNewestMenuItem extends MenuItem {
    public CLMToggleNewestMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCLMOptions(pdo.getCLMOptions().setNewestFirst());
        event.setUpdate(true);

        super.onItemClick(event);
    }
}
