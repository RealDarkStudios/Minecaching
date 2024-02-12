package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.PaginationMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.menu.item.MinecacheMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.clm.OpenCLMOptionsMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CacheListMenu extends PaginationMenu {
    private List<MenuItem> menuItems = new ArrayList<>();

    public CacheListMenu(String name, JavaPlugin plugin, Player plr) {
        super(name, plugin, null);

        setItem(6, new OpenCLMOptionsMenuItem(itemTranslation("options"), new ItemStack(Material.GRAY_DYE), List.of(), this));

        update(plr);
    }

    @Override
    public List<MenuItem> getItems(Player plr) {
        return menuItems;
    }

    @Override
    public void update(Player plr) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);
        List<Minecache> filteredCaches = pdo.filterCLMenuCaches(plr);

        if (filteredCaches.isEmpty()) {
            close(plr);
            MCMessages.sendMsg(plr, "listcaches.nocaches", ChatColor.RED);
        }

        ArrayList<MenuItem> menuItemsN = new ArrayList<>();

        for (Minecache cache: filteredCaches) {
            if (cache != null && !cache.equals(Minecache.EMPTY)) menuItemsN.add(new MinecacheMenuItem(this, cache));
        }

        this.menuItems = menuItemsN;

        super.update(plr);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.list.item." + id, substitutions);
    }
}
