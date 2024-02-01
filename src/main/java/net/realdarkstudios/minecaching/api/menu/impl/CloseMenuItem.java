package net.realdarkstudios.minecaching.api.menu.impl;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CloseMenuItem extends MenuItem {
    public CloseMenuItem(String name, ItemStack item) {
        super(ChatColor.RED + "Close", new ItemStack(Material.RED_CONCRETE));
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
    }
}
