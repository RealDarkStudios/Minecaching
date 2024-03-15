package net.realdarkstudios.minecaching.api.menu.item.misc;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GoBackAndUpdateMenuItem extends GoBackMenuItem {
    private final MCMenu parent;

    public GoBackAndUpdateMenuItem(ItemStack item, List<String> lore, MCMenu parent) {
        super(item, lore);
        this.parent = parent;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {

        parent.update(event.getPlayer());
        event.setGoBack(true);

        super.onItemClick(event);
    }
}
