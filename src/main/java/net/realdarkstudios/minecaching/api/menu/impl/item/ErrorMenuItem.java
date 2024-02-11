package net.realdarkstudios.minecaching.api.menu.impl.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ErrorMenuItem extends MenuItem {
    public ErrorMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public String getName() {
        return ChatColor.RED + super.getName();
    }
}
