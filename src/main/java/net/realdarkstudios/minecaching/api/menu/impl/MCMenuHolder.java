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

    /**
     * Gets the {@link MCMenu} associated with this holder
     * @return The associated MCMenu
     */
    public MCMenu getMenu() {
        return menu;
    }

    /**
     * Gets the {@link Inventory} associated with this holder
     * @return The associated Inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
