package net.realdarkstudios.minecaching.api.menu.item.data;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeleteCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public DeleteCacheMenuItem(Minecache cache) {
        super(MessageKeys.Menu.Data.ITEM_DELETE.translate(cache.id()), new ItemStack(Material.BLACK_CONCRETE), List.of(MessageKeys.Menu.Data.ITEM_DELETE_LORE.translate()));
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        MinecachingAPI.get().deleteMinecache(cache, event.getPlayer().getUniqueId());
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(Minecache.EMPTY);
        LocalizedMessages.send(event.getPlayer(), MessageKeys.Command.Misc.DISABLE);

        //TODO: Add other cache deletion checks
        event.setClose(true);

        super.onItemClick(event);
    }
}
