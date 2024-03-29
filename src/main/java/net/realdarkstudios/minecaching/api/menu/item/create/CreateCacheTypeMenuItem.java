package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItemState;
import net.realdarkstudios.commons.menu.item.MultiStateMenuItem;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;

public class CreateCacheTypeMenuItem extends MultiStateMenuItem {
    public CreateCacheTypeMenuItem(MenuItemState... items) {
        super(items);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCreatingCache(pdo.getCreatingCache().setType(MinecacheType.get(getCurrentState().id())));
        event.setUpdate(true);
    }
}
