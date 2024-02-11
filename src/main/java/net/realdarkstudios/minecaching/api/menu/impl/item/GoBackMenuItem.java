package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GoBackMenuItem extends MenuItem {
    public GoBackMenuItem(String name, ItemStack item, List<String> lore) {
        super(ChatColor.RED + name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setGoBack(true);

        super.onItemClick(event);
    }
}
