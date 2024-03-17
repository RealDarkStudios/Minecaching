package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.item.misc.CacheMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LogPickMenuItem extends CacheMenuItem {
    private Minecache cache;
    private final ItemStack display;

    public LogPickMenuItem(Minecache cache) {
        super(MessageKeys.Menu.List.ITEM_CACHE.translate(cache.id(), cache.name()), cache);

        display = switch (cache.type()) {
            case TRADITIONAL -> new ItemStack(Material.GREEN_TERRACOTTA);
            case MYSTERY -> new ItemStack(Material.BLUE_TERRACOTTA);
            case MULTI -> new ItemStack(Material.YELLOW_TERRACOTTA);
            case INVALID -> new ItemStack(Material.BEDROCK);
        };

        this.cache = cache;
    }

    @Override
    public ItemStack getIcon(Player player) {
        return applyText(display);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pdo.setLocatingId(cache.id());
        LogMenu menu = new LogMenu(cache, pdo);
        menu.open(event.getPlayer());
    }
}
