package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheArchivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ArchiveCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public ArchiveCacheMenuItem(Minecache cache) {
        this(MessageKeys.Menu.Data.ITEM_ARCHIVE.translate(cache.id()), new ItemStack(Material.BLACK_CONCRETE), List.of(), cache);
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
            LocalizedMessages.send(event.getPlayer(), MessageKeys.Error.Misc.ARCHIVE);
            return;
        }

        MinecachingAPI.get().archiveMinecache(event.getPlayer().getUniqueId(), cache);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Misc.ARCHIVE, cache.id());

        event.setClose(true);
    }
}
