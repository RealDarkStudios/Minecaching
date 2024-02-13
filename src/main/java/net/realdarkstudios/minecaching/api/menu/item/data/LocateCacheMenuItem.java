package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.StartLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LocateCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public LocateCacheMenuItem(Minecache cache) {
        super(ChatColor.AQUA + translation("menu.data.item.locate", cache.id()), new ItemStack(Material.LIGHT_BLUE_CONCRETE), List.of());
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        // only coordinate support at this time, will add lodestone later
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        MCMessages.sendMsg(event.getPlayer(), "locatecache.coords", ChatColor.AQUA, cache.navLocation().getBlockX(), cache.navLocation().getBlockY(), cache.navLocation().getBlockZ(), "locate");
        pdo.setLocatingId(cache.id());

        StartLocatingMinecacheEvent lEvent = new StartLocatingMinecacheEvent(cache, event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation().distance(cache.location()));
        Bukkit.getPluginManager().callEvent(lEvent);

        event.setClose(true);

        super.onItemClick(event);
    }
}
