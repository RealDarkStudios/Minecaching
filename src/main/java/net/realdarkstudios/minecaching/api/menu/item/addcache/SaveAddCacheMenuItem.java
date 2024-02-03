package net.realdarkstudios.minecaching.api.menu.item.addcache;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheCreatedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;

public class SaveAddCacheMenuItem extends MenuItem {
    public SaveAddCacheMenuItem(String name, ItemStack item) {
        super(name, item, "All wool must be green to be able to save!");
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (getItem().getType().equals(Material.BEDROCK)) return;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        Minecache cache = pdo.getCache();

        if (cache.name() == null) {
            MCMessages.sendErrorMsg(event.getPlayer(), "addcache.noname");
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            MCMessages.sendErrorMsg(event.getPlayer(), "addcache.nocoords");
        } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
            MCMessages.sendErrorMsg(event.getPlayer(), "addcache.nolodecoords");
        } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "addcache.lodetoofar");
        } else if (cache.code() == null || cache.code().isEmpty()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "addcache.nocode");
        } else {
            cache.setStatus(MinecacheStatus.REVIEWING).setAuthor(event.getPlayer().getUniqueId()).setBlockType(cache.lodeLocation().getBlock().getType()).setHidden(LocalDateTime.now()).setFTF(Utils.EMPTY_UUID);

            MinecacheCreatedEvent cEvent = new MinecacheCreatedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                MCMessages.sendErrorMsg(event.getPlayer(), "addcache");
            }

            MinecachingAPI.get().saveMinecache(cache, true);

            pdo.addHide(cache.id());
            pdo.setCache(Minecache.EMPTY.setID("NULL"));

            MinecachingAPI.get().save();
            MinecachingAPI.get().update();

            MCMessages.sendMsg(event.getPlayer(), "addcache.save", ChatColor.LIGHT_PURPLE, cache.id(), cache.name());
        }

        event.setUpdate(true);
        event.setClose(true);
    }
}
