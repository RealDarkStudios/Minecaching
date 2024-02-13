package net.realdarkstudios.minecaching.api.menu.item.data;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecachePublishedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PublishCacheMenuItem extends MenuItem {
    private final Minecache cache;
    
    public PublishCacheMenuItem(Minecache cache) {
        this(ChatColor.DARK_GREEN + translation("menu.data.item.publish", cache.id()), new ItemStack(Material.GREEN_CONCRETE), List.of(), cache);
    }

    public PublishCacheMenuItem(String nameKey, ItemStack stack, List<String> lore, Minecache cache) {
        super(nameKey, stack, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        MinecachePublishedEvent pEvent = new MinecachePublishedEvent(cache, event.getPlayer());
        Bukkit.getPluginManager().callEvent(pEvent);

        if (pEvent.isCancelled()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "publishcache");
            return;
        }

        MinecachingAPI.get().publishMinecache(event.getPlayer().getUniqueId(), cache, "Published.");
        MCMessages.sendMsg(event.getPlayer(), "publishcache.publish", ChatColor.GREEN, cache.id());

        event.setClose(true);
    }
}
