package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCacheSetNameMenuItem extends MenuItem {
    public CreateCacheSetNameMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Menu.Create.SET_NAME);

        super.onItemClick(event);
    }
}
