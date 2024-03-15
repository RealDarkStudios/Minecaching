package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.impl.item.MultiStateMenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;

public class LogTypeMenuItem extends MultiStateMenuItem {
    public LogTypeMenuItem(MenuItemState... items) {
        super(items);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setLogType(LogType.get(getCurrentState().id()));
        event.setUpdate(true);
    }
}
