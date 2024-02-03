package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.plugin.java.JavaPlugin;

public class CacheDataMenu extends MCMenu {
    protected Minecache cache;

    public CacheDataMenu(String title, Minecache cache, JavaPlugin plugin) {
        super(title, MenuSize.FOUR_ROW, plugin);
        this.cache = cache;
    }
}
