package net.realdarkstudios.minecaching.event;

import org.bukkit.entity.Player;

public class MenuItemClickEvent {
    private Player player;
    private boolean close = false, goBack = false, update = false;
    public MenuItemClickEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean goBack() {
        return goBack;
    }

    public void setGoBack(boolean goBack) {
        this.goBack = goBack;
    }

    public boolean close() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public boolean update() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
