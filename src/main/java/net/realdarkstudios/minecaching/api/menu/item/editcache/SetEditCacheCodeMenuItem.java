package net.realdarkstudios.minecaching.api.menu.item.editcache;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.util.TextComponentBuilder;
import org.bukkit.inventory.ItemStack;

public class SetEditCacheCodeMenuItem extends MenuItem {
    public SetEditCacheCodeMenuItem(String name, ItemStack item) {
        super(name, item);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        TextComponent msg = TextComponentBuilder.fromTranslation("addcache.menu.code").color(ChatColor.LIGHT_PURPLE).underline().clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ec code ").build();

        event.setClose(true);
        event.getPlayer().spigot().sendMessage(msg);

        super.onItemClick(event);
    }
}
