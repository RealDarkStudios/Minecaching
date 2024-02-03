package net.realdarkstudios.minecaching.api.menu.item;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.NotificationType;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheEditedEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SaveEditCacheMenuItem extends MenuItem {
    public SaveEditCacheMenuItem(String name, ItemStack item, String... lore) {
        super(name, item, "All wool must be green to be able to save!");
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (getItem().getType().equals(Material.BEDROCK)) return;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        Minecache cache = pdo.getEditingCache();

        if (cache.name() == null) {
            MCMessages.sendErrorMsg(event.getPlayer(), "editcache.noname");
        } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
            MCMessages.sendErrorMsg(event.getPlayer(), "editcache.nocoords");
        } else if (cache.lx() == 0 && cache.ly() == 0 && cache.lz() == 0) {
            MCMessages.sendErrorMsg(event.getPlayer(), "editcache.nolodecoords");
        } else if (cache.lodeLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
            MCMessages.sendErrorMsg(event.getPlayer(), "editcache.lodetoofar");
        } else if (cache.code() == null) {
            MCMessages.sendErrorMsg(event.getPlayer(), "editcache.nocode");
        } else {
            MinecacheEditedEvent cEvent = new MinecacheEditedEvent(cache, event.getPlayer());
            Bukkit.getPluginManager().callEvent(cEvent);

            if (cEvent.isCancelled()) {
                MCMessages.sendErrorMsg(event.getPlayer(), "editcache");
                return;
            }

            if ((!event.getPlayer().getUniqueId().equals(cache.author()))) {
                MinecachingAPI.get().createNotification(cache.author(), event.getPlayer().getUniqueId(), NotificationType.EDIT, cache);
            }

            MinecachingAPI.get().saveMinecache(cache, false);
            MCMessages.sendMsg(event.getPlayer(), "editcache.save", ChatColor.LIGHT_PURPLE, cache.id(), cache.name());
            cache = Minecache.EMPTY;
            cache.setID("NULL");
        }

        event.setUpdate(true);
        event.setClose(true);
    }
}
