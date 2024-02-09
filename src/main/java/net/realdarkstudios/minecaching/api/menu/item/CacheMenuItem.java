package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;

public class CacheMenuItem extends MenuItem {
    public CacheMenuItem(Minecache cache) {
        super(cache.id() + ": " + cache.name(), new ItemStack(Material.SPYGLASS),
       "Type: " + cache.type().getTranslation(),
             "Status: " + cache.status().getTranslation(),
             Utils.formatLocation("Coordinates", cache.lodeLocation()),
             "Hidden: " + cache.hidden().format(DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a")),
             "Finds: " + cache.finds());
    }
}
