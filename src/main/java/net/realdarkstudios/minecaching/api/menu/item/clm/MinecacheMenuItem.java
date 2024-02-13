package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinecacheMenuItem extends CacheMenuItem {
    private final CacheDataMenu menu;
    private final Minecache cache;
    private final ItemStack display;

    public MinecacheMenuItem(CacheListMenu menu, Minecache cache) {
        super(translation("menu.list.item.cache", cache.id(), cache.name()), cache);

        display = switch (cache.type()) {
            case TRADITIONAL -> new ItemStack(Material.GREEN_TERRACOTTA);
            case MYSTERY -> new ItemStack(Material.BLUE_TERRACOTTA);
            case MULTI -> new ItemStack(Material.YELLOW_TERRACOTTA);
            case INVALID -> new ItemStack(Material.BEDROCK);
        };

        CacheDataMenu dataMenu = new CacheDataMenu("menu.data.title", cache, Minecaching.getInstance());
        dataMenu.setParent(menu);

        this.menu = dataMenu;
        this.cache = cache;
    }

    @Override
    public ItemStack getIcon(Player player) {
        return applyText(display);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        menu.open(event.getPlayer());
    }
}
