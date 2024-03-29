package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.api.event.minecache.StartLocatingMinecacheEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LocateCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public LocateCacheMenuItem(Minecache cache) {
        super(MessageKeys.Menu.Data.ITEM_LOCATE.translate(cache.id()), new ItemStack(Material.LIGHT_BLUE_CONCRETE), List.of());
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        // only coordinate support at this time, will add lodestone later
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Locate.COORDS,
                cache.navLocation().getBlockX(), cache.navLocation().getBlockY(), cache.navLocation().getBlockZ(), "locate");
        pdo.setLocatingId(cache.id());

        StartLocatingMinecacheEvent lEvent = new StartLocatingMinecacheEvent(cache, event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation().distance(cache.location()));
        Bukkit.getPluginManager().callEvent(lEvent);

        event.setClose(true);

        super.onItemClick(event);
    }
}
