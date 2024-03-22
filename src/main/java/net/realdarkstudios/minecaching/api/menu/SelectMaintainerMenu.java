package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.PaginationMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.menu.item.data.SelectMaintainerMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.data.SelectMaintainerOfflineMenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SelectMaintainerMenu extends PaginationMenu {
    private List<MenuItem> menuItems = List.of();
    private final Minecache cache;

    public SelectMaintainerMenu(Player player, Minecache cache, JavaPlugin plugin, MCMenu parent) {
        super(MessageKeys.Menu.Data.MAINTAINER_TITLE, plugin, parent, cache.id());
        this.cache = cache;

        update(player);
    }

    @Override
    public List<MenuItem> getItems(Player player) {
        return menuItems;
    }

    @Override
    public void update(Player player) {
        List<MenuItem> items = new ArrayList<>();

        for (PlayerDataObject pdo: MinecachingAPI.get().getAllKnownPlayers()) {
            if (pdo != null && !pdo.getUniqueID().equals(cache.originalAuthor()) && !pdo.getUniqueID().equals(MCUtils.EMPTY_UUID) && !pdo.getUniqueID().equals(cache.owner()))
                items.add(Bukkit.getOnlineMode() ? new SelectMaintainerMenuItem(cache, pdo) : new SelectMaintainerOfflineMenuItem(cache, pdo));
        }

        if (items.isEmpty()) {
            close(player);
            LocalizedMessages.send(player, MessageKeys.Command.Misc.NO_SUITABLE_MAINTAINERS);
        }

        this.menuItems = items;

        super.update(player);
    }
}
