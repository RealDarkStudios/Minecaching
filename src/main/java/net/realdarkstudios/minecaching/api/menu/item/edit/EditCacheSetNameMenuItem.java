package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditCacheSetNameMenuItem extends MenuItem {
    public EditCacheSetNameMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Menu.Edit.SET_NAME);

        super.onItemClick(event);
    }
}
