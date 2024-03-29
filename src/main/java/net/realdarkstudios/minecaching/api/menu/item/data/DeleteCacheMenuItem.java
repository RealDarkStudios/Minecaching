package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeleteCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public DeleteCacheMenuItem(Minecache cache) {
        super(MCMessageKeys.Menu.Data.ITEM_DELETE.translate(cache.id()), new ItemStack(Material.BLACK_CONCRETE), List.of(MCMessageKeys.Menu.Data.ITEM_DELETE_LORE.translate()));
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        MinecachingAPI.get().deleteMinecache(cache, event.getPlayer().getUniqueId());
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(Minecache.EMPTY);
        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Misc.DELETE, cache.id());

        //TODO: Add other cache deletion checks
        event.setClose(true);

        super.onItemClick(event);
    }
}
