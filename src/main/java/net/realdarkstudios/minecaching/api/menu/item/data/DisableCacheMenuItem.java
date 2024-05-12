package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheDisabledEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DisableCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public DisableCacheMenuItem(Minecache cache) {
        this(MCMessageKeys.Menu.Data.ITEM_DISABLE.translate(cache.id()), new ItemStack(Material.GRAY_CONCRETE), List.of(), cache);
    }

    public DisableCacheMenuItem(String name, ItemStack stack, List<String> lore, Minecache cache) {
        super(name, stack, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        MinecacheDisabledEvent pEvent = new MinecacheDisabledEvent(cache, event.getPlayer());
        Bukkit.getPluginManager().callEvent(pEvent);

        if (pEvent.isCancelled()) {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Misc.CANT_DISABLE);
            return;
        }

        MinecachingAPI.get().disableMinecache(event.getPlayer().getUniqueId(), cache);
        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Misc.DISABLE, cache.id());

        event.setClose(true);
    }
}
