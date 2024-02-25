package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogCancelMenuItem extends MenuItem {
    public LogCancelMenuItem(String name, ItemStack item, List<String> lore) {
        super(ChatColor.RED + "" + ChatColor.BOLD + translation("menu.cancel"), new ItemStack(Material.RED_CONCRETE), lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pdo.setLocatingId("NULL");
        event.setClose(true);
    }
}
