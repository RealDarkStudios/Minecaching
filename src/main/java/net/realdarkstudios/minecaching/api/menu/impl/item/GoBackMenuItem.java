package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GoBackMenuItem extends MenuItem {
    public GoBackMenuItem(ItemStack item, List<String> lore) {
        super(MessageKeys.Menu.GO_BACK.translate(), item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setGoBack(true);

        super.onItemClick(event);
    }
}
