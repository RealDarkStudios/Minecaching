package net.realdarkstudios.minecaching.api.menu.item;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheArchivedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ArchiveCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public ArchiveCacheMenuItem(Minecache cache) {
        this(ChatColor.DARK_GRAY + translation("menu.data.item.archive"), new ItemStack(Material.BLACK_CONCRETE), List.of(), cache);
    }

    public ArchiveCacheMenuItem(String name, ItemStack item, List<String> lore, Minecache cache) {
        super(name, item, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        super.onItemClick(event);

        MinecacheArchivedEvent pEvent = new MinecacheArchivedEvent(cache, event.getPlayer());
        Bukkit.getPluginManager().callEvent(pEvent);

        if (pEvent.isCancelled()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "archivecache");
            return;
        }

        MinecachingAPI.get().archiveMinecache(event.getPlayer().getUniqueId(), cache, "Archived. Please note that this cache can no longer be found by other cachers.");
        MCMessages.sendMsg(event.getPlayer(), "archivecache.archive", ChatColor.GREEN, cache.id());

        event.setClose(true);
    }
}
