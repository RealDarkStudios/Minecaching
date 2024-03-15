package net.realdarkstudios.minecaching.api.menu.item.edit;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.EditCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditCacheMenuItem extends MenuItem {
    private final Minecache cache;

    public EditCacheMenuItem(Minecache cache) {
        this(MessageKeys.Menu.Data.ITEM_EDIT.translate(), new ItemStack(Material.PURPLE_CONCRETE), List.of(), cache);
    }

    public EditCacheMenuItem(String nameKey, ItemStack stack, List<String> lore, Minecache cache) {
        super(nameKey, stack, lore);
        this.cache = cache;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        pdo.setEditingCache(cache);

        EditCacheMenu menu = MCMenus.get().getEditCacheMenu(pdo, cache);
        menu.open(event.getPlayer());
    }
}
