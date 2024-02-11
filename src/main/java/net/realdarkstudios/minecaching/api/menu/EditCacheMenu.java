package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.item.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EditCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public EditCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("menu.editing.title", MenuSize.FOUR_ROW, plugin, cache.id());
        this.author = author;
        this.cache = cache;

        update(author.getPlayer().getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getEditingCache();

        if (player.getUniqueId().equals(cache.author()) || player.hasPermission("minecaching.admin.edit")) {
            MinecachingAPI.get().getPlayerData(player).setEditingCache(cache);

        } else {
            MCMessages.noPermission(player, "editcache");
            MinecachingAPI.get().getPlayerData(player).setEditingCache(Minecache.EMPTY);
            close(player);
        }

        clearAllSlots();
        fillEmptySlots();

        boolean ready = stringCheck(cache.name()) && stringCheck(cache.code()) && coordCheck(cache.navLocation()) && coordCheck(cache.location());

        setItem(0, new ResetEditCacheMenuItem(itemTranslation("reset"), new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of(), cache));
        setItem(4, new CacheMenuItem(itemTranslation("preview", cache.id(), stringCheck(cache.name()) ? cache.name() : "???"), cache));
        setItem(21, new EditCacheNavCoordMenuItem(coordCheck(cache.navLocation()) ? itemTranslation("nav", Utils.formatLocation(dataTranslation("preview.navcoords"), cache.navLocation())) : itemTranslation("navcoords"), new ItemStack(coordCheck(cache.navLocation()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(23, new EditCacheCoordMenuItem(coordCheck(cache.location()) ? itemTranslation("loc", Utils.formatLocation(dataTranslation("preview.coords"), cache.navLocation())) : itemTranslation("loccoords"), new ItemStack(coordCheck(cache.location()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(27, new OpenConfirmationMenuItem(new DeleteCacheMenuItem(dataTranslation("delete"), new ItemStack(Material.BLACK_CONCRETE), List.of(dataTranslation("delete.lore")), cache), this));
        setItem(29, new SetEditCacheNameMenuItem(dataTranslation("cache.name", stringCheck(cache.name()) ? cache.name() : "???"), new ItemStack(cache.name() != null && !cache.name().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(31, new SaveEditCacheMenuItem(ready ? ChatColor.GREEN + translation("menu.save") : ChatColor.RED + translation("menu.save"), new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of()));
        setItem(33, new SetEditCacheCodeMenuItem(dataTranslation("cache.code", stringCheck(cache.code()) ? cache.name() : "???"), new ItemStack(cache.code() != null && !cache.code().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
        setItem(35, new CancelEditCacheMenuItem(itemTranslation("stopediting"), new ItemStack(Material.GRAY_CONCRETE), List.of(itemTranslation("stopediting.lore"))));

        super.update(player);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.editing.item." + id, substitutions);
    }
}
