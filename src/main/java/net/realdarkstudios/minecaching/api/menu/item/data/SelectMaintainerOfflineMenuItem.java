package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SelectMaintainerOfflineMenuItem extends MenuItem {
    private final Minecache cache;
    private final UUID uuid;
    private final String name;

    public SelectMaintainerOfflineMenuItem(Minecache cache, String name, UUID uuid) {
        super(name, new ItemStack(Material.PLAYER_HEAD), List.of());
        this.name = name;
        this.uuid = uuid;
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        MinecachingAPI.get().saveMinecache(cache.setMaintaner(uuid), false);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Menu.Data.SET_MAINTAINER, cache.id(), name);

        event.setClose(true);
        super.onItemClick(event);
    }
}