package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogMessageMenuItem extends MenuItem {
    public LogMessageMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Menu.Log.SET_MESSAGE);

        super.onItemClick(event);
    }
}
