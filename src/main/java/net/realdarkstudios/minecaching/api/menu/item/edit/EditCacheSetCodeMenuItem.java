package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditCacheSetCodeMenuItem extends MenuItem {
    public EditCacheSetCodeMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Menu.Edit.SET_CODE);

        super.onItemClick(event);
    }
}
