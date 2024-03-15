package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.api.event.minecache.MinecachePublishedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PublishCacheMenuItem extends MenuItem {
    private final Minecache cache;
    
    public PublishCacheMenuItem(Minecache cache) {
        this(MessageKeys.Menu.Data.ITEM_PUBLISH.translate(cache.id()), new ItemStack(Material.GREEN_CONCRETE), List.of(), cache);
    }

    public PublishCacheMenuItem(String name, ItemStack stack, List<String> lore, Minecache cache) {
        super(name, stack, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        MinecachePublishedEvent pEvent = new MinecachePublishedEvent(cache, event.getPlayer());
        Bukkit.getPluginManager().callEvent(pEvent);

        if (pEvent.isCancelled()) {
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Misc.PUBLISH);
            return;
        }

        MinecachingAPI.get().publishMinecache(event.getPlayer().getUniqueId(), cache);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Misc.PUBLISH, cache.id());

        event.setClose(true);
    }
}
