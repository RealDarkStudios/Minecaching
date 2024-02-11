package net.realdarkstudios.minecaching.api.menu.item;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.util.TextComponentBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetAddCacheNameMenuItem extends MenuItem {
    public SetAddCacheNameMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        TextComponent msg = TextComponentBuilder.fromTranslation("addcache.menu.name").color(ChatColor.LIGHT_PURPLE).underline().clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac name ").build();

        event.setClose(true);
        event.getPlayer().spigot().sendMessage(msg);

        super.onItemClick(event);
    }
}
