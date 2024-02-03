package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class NumberIncreaseItem extends MenuItem {
    protected int num;

    public NumberIncreaseItem(String name, ItemStack item, int num) {
        super(name, item);
        this.num = num;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        num++;

        event.setUpdate(true);

        super.onItemClick(event);
    }
}
