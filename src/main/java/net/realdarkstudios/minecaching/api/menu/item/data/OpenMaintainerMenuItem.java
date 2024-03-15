package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.menu.SelectMaintainerMenu;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenMaintainerMenuItem extends MenuItem {
    private final Minecache cache;
    private final MCMenu parent;
    public OpenMaintainerMenuItem(Minecache cache, MCMenu parentMenu) {
        super(MessageKeys.Menu.Data.ITEM_MAINTAINER.translate(), new ItemStack(Material.YELLOW_CONCRETE), List.of(MessageKeys.Menu.Data.ITEM_MAINTAINER_LORE.translate()));
        this.cache = cache;
        this.parent = parentMenu;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        SelectMaintainerMenu menu = new SelectMaintainerMenu(event.getPlayer(), cache, Minecaching.getInstance(), parent);
        menu.open(event.getPlayer());
    }
}
