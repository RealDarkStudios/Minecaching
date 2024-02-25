package net.realdarkstudios.minecaching.api.menu.item.create;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.util.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCachePreviewItem extends MenuItem {
    public CreateCachePreviewItem(Minecache cache) {
        super(translation("menu.creating.item.preview", cache.id(), stringCheck(cache.name()) ? cache.name() : "???"), new ItemStack(Material.SPYGLASS), List.of(
                translation("menu.data.item.preview.type", cache.type().getTranslation()),
                translation("menu.data.item.preview.code", stringCheck(cache.code()) ? cache.code() : "???"),
                Utils.formatLocation(translation("menu.data.item.preview.coords"), cache.location()),
                Utils.formatLocation(translation("menu.data.item.preview.navcoords"), cache.navLocation())));
    }

    private static boolean stringCheck(String str) {
        return str != null && !str.isEmpty();
    }
}