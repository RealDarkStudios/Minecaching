package net.realdarkstudios.minecaching.api.menu.item.log;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.util.TextComponentBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogCodeMenuItem extends MenuItem {
    public LogCodeMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        TextComponent msg = TextComponentBuilder.fromTranslation("logcache.menu.code").color(ChatColor.LIGHT_PURPLE).underline().clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/log code " + pdo.getLogCode()).build();

        event.setClose(true);
        event.getPlayer().spigot().sendMessage(msg);

        super.onItemClick(event);
    }
}
