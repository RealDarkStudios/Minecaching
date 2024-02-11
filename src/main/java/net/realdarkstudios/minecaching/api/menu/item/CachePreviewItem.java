package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CachePreviewItem extends MenuItem {
    public CachePreviewItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }
}
