package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;

public class MinecacheMenuItem extends MenuItem {
    private final CacheDataMenu menu;
    private final Minecache cache;

    public MinecacheMenuItem(CacheListMenu menu, Minecache cache) {
        super(cache.id() + ": " + cache.name(), new ItemStack(switch (cache.type()) {
            case TRADITIONAL -> Material.GREEN_TERRACOTTA;
            case MYSTERY -> Material.BLUE_TERRACOTTA;
            case MULTI -> Material.YELLOW_TERRACOTTA;
            case INVALID -> Material.BEDROCK;
        }), "Type: " + cache.type().getTranslation(),
                "Status: " + cache.status().getTranslation(),
                Utils.formatLocation("Coordinates", cache.lodeLocation()),
                "Hidden: " + cache.hidden().format(DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a")),
                "Finds: " + cache.finds());

        CacheDataMenu dataMenu = new CacheDataMenu(cache.id() + " Info", cache, Minecaching.getInstance());
        dataMenu.setParent(menu);

        this.menu = dataMenu;
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        menu.open(event.getPlayer());

        //TODO: Open Minecache Context Menu
    }
}
