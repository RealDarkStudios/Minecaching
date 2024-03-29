package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SelectMaintainerMenuItem extends MenuItem {
    private final Minecache cache;
    private final PlayerDataObject pdo;

    public SelectMaintainerMenuItem(Minecache cache, PlayerDataObject pdo) {
        super(pdo.getUsername(), pdo.getSkullItemStack(), List.of());
        this.cache = cache;
        this.pdo = pdo;
    }

    @Override
    public ItemStack getIcon(Player player) {
        return pdo.getSkullItemStack();
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        MinecachingAPI.get().saveMinecache(cache.setMaintaner(pdo.getUniqueID()), false);
        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Menu.Data.SET_MAINTAINER, cache.id(), name);

        event.setGoBack(true);
        super.onItemClick(event);
    }
}
