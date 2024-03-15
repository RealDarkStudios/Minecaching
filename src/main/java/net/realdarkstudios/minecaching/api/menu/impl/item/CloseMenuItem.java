package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CloseMenuItem extends MenuItem {
    public CloseMenuItem() {
        super(MessageKeys.Menu.CLOSE.translate(), new ItemStack(Material.RED_CONCRETE), List.of());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
    }
}
