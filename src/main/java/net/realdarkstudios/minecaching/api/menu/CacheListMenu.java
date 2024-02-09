package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.PaginationMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.menu.item.MinecacheMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CacheListMenu extends PaginationMenu {
    public CacheListMenu(String name, JavaPlugin plugin) {
        super(name, plugin, null);
    }

    @Override
    public List<MenuItem> getItems(Player plr) {
        List<Minecache> allCaches = MinecachingAPI.get().getAllKnownCaches();

        if (allCaches.isEmpty()) {
            close(plr);
            MCMessages.sendMsg(plr, "listcaches.nocaches", ChatColor.RED);
        }

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        for (Minecache cache: allCaches) {
            if (cache != null && !cache.equals(Minecache.EMPTY)) menuItems.add(new MinecacheMenuItem(this, cache));
        }

        return menuItems;
    }
}
