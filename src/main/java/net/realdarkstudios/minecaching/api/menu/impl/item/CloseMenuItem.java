package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CloseMenuItem extends MenuItem {
    public CloseMenuItem(String name, ItemStack item, List<String> lore) {
        super(ChatColor.RED + "" + ChatColor.BOLD + translation("menu.close"), new ItemStack(Material.RED_CONCRETE), lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        event.setClose(true);
    }
}
