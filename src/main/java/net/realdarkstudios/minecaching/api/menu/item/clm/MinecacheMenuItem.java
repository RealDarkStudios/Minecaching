package net.realdarkstudios.minecaching.api.menu.item.clm;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinecacheMenuItem extends CacheMenuItem {
    private final CacheListMenu menu;
    private final Minecache cache;
    private final ItemStack display;

    public MinecacheMenuItem(CacheListMenu menu, Minecache cache) {
        super(MCMessageKeys.Menu.List.ITEM_CACHE.translate(cache.id(), cache.name()), cache);

        display = switch (cache.type()) {
            case TRADITIONAL -> new ItemStack(Material.GREEN_TERRACOTTA);
            case MYSTERY -> new ItemStack(Material.BLUE_TERRACOTTA);
            case MULTI -> new ItemStack(Material.YELLOW_TERRACOTTA);
            case INVALID -> new ItemStack(Material.BEDROCK);
        };

        this.cache = cache;
        this.menu = menu;
    }

    @Override
    public ItemStack getIcon(Player player) {
        return applyText(display);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        CacheDataMenu dataMenu = new CacheDataMenu(MCMessageKeys.Menu.Data.TITLE, cache, Minecaching.getInstance(),
                MinecachingAPI.get().getPlayerData(event.getPlayer()));
        dataMenu.setParent(menu);

        dataMenu.open(event.getPlayer());
    }
}
