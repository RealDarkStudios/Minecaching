package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import org.bukkit.inventory.ItemStack;

public class CachePreviewItem extends MenuItem {
    public CachePreviewItem(String name, ItemStack item, String... lore) {
        super(name, item, lore);
    }
}
