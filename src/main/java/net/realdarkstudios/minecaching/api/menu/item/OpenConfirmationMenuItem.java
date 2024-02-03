package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.ConfirmationMenu;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;

public class OpenConfirmationMenuItem extends MenuItem {
    private final MenuItem confirmItem;
    private final MCMenu parent;

    public OpenConfirmationMenuItem(MenuItem confirmItem, MCMenu parent) {
        super(confirmItem.getName(), confirmItem.getItem());
        this.confirmItem = confirmItem;
        this.parent = parent;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        ConfirmationMenu confirmationMenu = new ConfirmationMenu(MinecachingAPI.get().getPlayerData(event.getPlayer()), confirmItem, Minecaching.getInstance(), parent);
        confirmationMenu.open(event.getPlayer());
    }
}
