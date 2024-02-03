package net.realdarkstudios.minecaching.api.menu.item.addcache;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.impl.item.MultiStateMenuItem;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;

public class CacheTypeMenuItem extends MultiStateMenuItem {
    public CacheTypeMenuItem(MenuItemState... items) {
        super(items);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setCache(pdo.getCache().setType(MinecacheType.get(getCurrentState().id())));
        event.setUpdate(true);
    }
}
