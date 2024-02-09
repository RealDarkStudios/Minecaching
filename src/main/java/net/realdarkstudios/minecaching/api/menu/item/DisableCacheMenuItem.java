package net.realdarkstudios.minecaching.api.menu.item;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheDisabledEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisableCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public DisableCacheMenuItem(Minecache cache) {
        super(ChatColor.GRAY + "Disable " + cache.id(), new ItemStack(Material.GRAY_CONCRETE));
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        MinecacheDisabledEvent pEvent = new MinecacheDisabledEvent(cache, event.getPlayer());
        Bukkit.getPluginManager().callEvent(pEvent);

        if (pEvent.isCancelled()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "disablecache");
            return;
        }

        MinecachingAPI.get().disableMinecache(event.getPlayer().getUniqueId(), cache, "Disabled. Please note that this cache can no longer be found by other cachers, but is able to go back if republished.");
        MCMessages.sendMsg(event.getPlayer(), "disablecache.disable", ChatColor.GREEN, cache.id());

        event.setClose(true);
    }
}
