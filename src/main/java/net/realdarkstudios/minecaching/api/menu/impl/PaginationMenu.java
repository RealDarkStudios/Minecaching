package net.realdarkstudios.minecaching.api.menu.impl;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.PaginationMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.PaginationPageItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.RefreshPaginationMenuItem;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PaginationMenu extends MCMenu {
    private final Map<UUID, AtomicInteger> pages = new HashMap<>();

    public PaginationMenu(LocalizedMessages.Key titleKey, JavaPlugin plugin, Object... formatArgs) {
        this(titleKey, plugin, null, formatArgs);
    }


    public PaginationMenu(LocalizedMessages.Key titleKey, JavaPlugin plugin, MCMenu parent, Object... formatArgs) {
        super(titleKey, MenuSize.SIX_ROW, plugin, parent, formatArgs);

        for (int i = 0; i < MenuSize.FIVE_ROW.getSlotCount(); i++) {
            setItem(i + 9, new PaginationMenuItem(this, i));
        }

        setItem(0, new PaginationPageItem(this, MessageKeys.Menu.List.PREVIOUS.translate(), -1));
        setItem(7, new RefreshPaginationMenuItem(this, MessageKeys.Menu.List.REFRESH.translate(),
                new ItemStack(Material.LIGHT_GRAY_TERRACOTTA), List.of()));
        setItem(8, new PaginationPageItem(this, MessageKeys.Menu.List.NEXT.translate(), 1));
        fillEmptySlots();
    }

    public abstract List<MenuItem> getItems(Player player);

    @Override
    public void open(Player player) {
        ensurePageDataAvailable(player);
        super.open(player);
    }

    public int getPage(Player player) {
        ensurePageDataAvailable(player);
        return pages.get(player.getUniqueId()).get();
    }

    public void setPage(Player player, int page) {
        ensurePageDataAvailable(player);
        pages.get(player.getUniqueId()).set(page);
        update(player);
    }

    private void ensurePageDataAvailable(Player player) {
        if (!pages.containsKey(player.getUniqueId())) {
            pages.put(player.getUniqueId(), new AtomicInteger());
        }
    }
}
