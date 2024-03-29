package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheListOptionsMenu;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenCLMOptionsMenuItem extends MenuItem {
    private final MCMenu parent;

    public OpenCLMOptionsMenuItem(String name, ItemStack stack, List<String> lore, MCMenu parent) {
        super(name, stack, lore);
        this.parent = parent;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        CacheListOptionsMenu cacheListOptionsMenu = new CacheListOptionsMenu(MinecachingAPI.get().getPlayerData(event.getPlayer()),
                MCMessageKeys.Menu.CLMOptions.TITLE, Minecaching.getInstance(), parent);
        cacheListOptionsMenu.open(event.getPlayer());
    }
}
