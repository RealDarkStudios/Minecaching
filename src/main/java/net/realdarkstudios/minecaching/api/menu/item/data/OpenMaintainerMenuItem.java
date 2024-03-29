package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.menu.SelectMaintainerMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenMaintainerMenuItem extends MenuItem {
    private final Minecache cache;
    private final MCMenu parent;
    public OpenMaintainerMenuItem(Minecache cache, MCMenu parentMenu) {
        super(MCMessageKeys.Menu.Data.ITEM_MAINTAINER.translate(), new ItemStack(Material.YELLOW_CONCRETE), List.of(MCMessageKeys.Menu.Data.ITEM_MAINTAINER_LORE.translate()));
        this.cache = cache;
        this.parent = parentMenu;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        SelectMaintainerMenu menu = new SelectMaintainerMenu(event.getPlayer(), cache, Minecaching.getInstance(), parent);
        menu.open(event.getPlayer());
    }
}
