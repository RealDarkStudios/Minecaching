package net.realdarkstudios.minecaching.api.menu.item.log;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogCancelMenuItem extends MenuItem {
    public LogCancelMenuItem() {
        super(MCMessageKeys.Menu.CANCEL.translateWithOtherStyle(new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                new ItemStack(Material.RED_CONCRETE), List.of());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pdo.removeFavorite(pdo.getLocatingId());
        pdo.setLocatingId("NULL");
        event.setClose(true);
    }
}
