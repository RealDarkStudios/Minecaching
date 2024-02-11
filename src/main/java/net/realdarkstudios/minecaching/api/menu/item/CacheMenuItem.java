package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CacheMenuItem extends MenuItem {
    public CacheMenuItem(String name, Minecache cache) {
        super(name, new ItemStack(Material.SPYGLASS), List.of(
                MinecachingAPI.getLocalization().getTranslation("menu.data.item.preview.type", cache.type().getTranslation()),
                MinecachingAPI.getLocalization().getTranslation("menu.data.item.preview.status", cache.status().getTranslation()),
                Utils.formatLocation(MinecachingAPI.getLocalization().getTranslation("menu.data.item.preview.coords"), cache.navLocation()),
                MinecachingAPI.getLocalization().getTranslation("menu.data.item.preview.hidden", cache.hidden().format(DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a"))),
                MinecachingAPI.getLocalization().getTranslation("menu.data.item.preview.finds", cache.finds())));
    }
}
