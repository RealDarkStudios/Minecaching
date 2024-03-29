package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FavoriteCacheItem extends MenuItem {
    private final Minecache cache;

    public FavoriteCacheItem(Minecache cache, PlayerDataObject pdo) {
        super((pdo.getFavorites().contains(cache.id()) ? MCMessageKeys.Menu.Log.ITEM_UNFAVORITE : MCMessageKeys.Menu.Log.ITEM_FAVORITE)
                        .translate(cache.id()),
                new ItemStack(Material.GRAY_DYE), List.of());
        this.cache = cache;
    }

    @Override
    public ItemStack getIcon(Player player) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(player);

        ItemStack stack = checkIfCanFavorite(pdo) ? new ItemStack(Material.GRAY_DYE) : new ItemStack(Material.BEDROCK);
        if (checkIfCanFavorite(pdo)) stack = !pdo.getFavorites().contains(cache.id()) ? new ItemStack(Material.GRAY_DYE) : new ItemStack(Material.LIME_DYE);
        else setClickSound(Sound.ENTITY_VILLAGER_TRADE);

        return applyText(stack);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());

        if (checkIfCanFavorite(pdo) && !pdo.getFavorites().contains(cache.id())) pdo.addFavorite(cache.id());
        else pdo.removeFavorite(cache.id());

        event.setUpdate(true);
    }

    private boolean checkIfCanFavorite(PlayerDataObject pdo) {
        return (pdo.getFavorites().size() + 1) <= (pdo.getFinds().size() / 10) || pdo.getFavorites().contains(cache.id());
    }
}
