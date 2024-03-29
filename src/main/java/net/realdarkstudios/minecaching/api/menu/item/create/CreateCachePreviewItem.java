package net.realdarkstudios.minecaching.api.menu.item.create;


import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateCachePreviewItem extends MenuItem {
    public CreateCachePreviewItem(Minecache cache) {
        super(MCMessageKeys.Menu.Create.ITEM_PREVIEW.translate(cache.id(), stringCheck(cache.name()) ? cache.name() : "???"),
                new ItemStack(Material.SPYGLASS), List.of(
                MCMessageKeys.Menu.Data.PREVIEW_TYPE.translate(cache.type().getTranslation()),
                MCMessageKeys.Menu.Data.PREVIEW_CODE.translate(stringCheck(cache.code()) ? cache.code() : "???"),
                MCUtils.formatLocation(MCMessageKeys.Menu.Data.PREVIEW_COORDS.translate(), cache.location()),
                MCUtils.formatLocation(MCMessageKeys.Menu.Data.PREVIEW_NAVIGATION_COORDS.translate(), cache.navLocation())));
    }

    private static boolean stringCheck(String str) {
        return str != null && !str.isEmpty();
    }
}