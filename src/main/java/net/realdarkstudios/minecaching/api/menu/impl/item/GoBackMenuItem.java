package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class GoBackMenuItem extends MenuItem {
    public GoBackMenuItem(String name, ItemStack item) {
        super(name, item);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setGoBack(true);

        super.onItemClick(event);
    }
}
