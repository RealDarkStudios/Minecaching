package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CLMDisabledStatusMenuItem extends MenuItem {
    public CLMDisabledStatusMenuItem(PlayerDataObject pdo, String name, ItemStack item, List<String> lore) {
        super((pdo.getCLMOptions().statusEnabled(MinecacheStatus.DISABLED) ? ChatColor.GREEN : ChatColor.RED) + name,
                (pdo.getCLMOptions().statusEnabled(MinecacheStatus.DISABLED) ? item : new ItemStack(Material.BEDROCK)), lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pdo.setCLMOptions(pdo.getCLMOptions().toggleStatus(MinecacheStatus.DISABLED));
        event.setUpdate(true);
    }
}
