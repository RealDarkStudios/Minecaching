package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CLMMysteryTypeMenuItem extends MenuItem {
    public CLMMysteryTypeMenuItem(PlayerDataObject pdo, String name, ItemStack item, List<String> lore) {
        super((pdo.getCLMOptions().typeEnabled(MinecacheType.MYSTERY) ? ChatColor.GREEN : ChatColor.RED) + name,
                (pdo.getCLMOptions().typeEnabled(MinecacheType.TRADITIONAL)) ? item : new ItemStack(Material.BEDROCK), lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pdo.setCLMOptions(pdo.getCLMOptions().toggleType(MinecacheType.MYSTERY));
        event.setUpdate(true);
    }
}
