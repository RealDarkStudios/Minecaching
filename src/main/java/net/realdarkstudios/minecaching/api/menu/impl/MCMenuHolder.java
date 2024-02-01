package net.realdarkstudios.minecaching.api.menu.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MCMenuHolder implements InventoryHolder {
    private MCMenu menu;
    private Inventory inventory;

    public MCMenuHolder(MCMenu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

    public MCMenu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
