package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCachePreviewItem extends MenuItem {
    public CreateCachePreviewItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }
}
