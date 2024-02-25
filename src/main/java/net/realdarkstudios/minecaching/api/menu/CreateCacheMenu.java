package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.item.create.*;
import net.realdarkstudios.minecaching.util.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.CloseMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CreateCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public CreateCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("menu.creating.title", MenuSize.FOUR_ROW, plugin);
        this.author = author;
        this.cache = cache;

        update(author.getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getCreatingCache();
        clearAllSlots();
        fillEmptySlots();

        boolean name = stringCheck(cache.name());
        boolean code = stringCheck(cache.code());
        boolean navLoc = coordCheck(cache.navLocation());
        boolean loc = coordCheck(cache.location());

        boolean ready = name && code && navLoc && loc;

        setItem(8, new CloseMenuItem(translation("menu.close"), new ItemStack(Material.RED_CONCRETE), List.of()));

        if (author.getCreatingCache().id().equals("NULL")) {
            setItem(13, new CreateCacheMenuItem(itemTranslation("create"), new ItemStack(Material.GREEN_CONCRETE), List.of()));
        } else {

            setItem(0, new CreateCacheResetMenuItem(itemTranslation("reset"), new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
            setItem(4, new CreateCachePreviewItem(cache));
            setItem(21, new CreateCacheNavCoordMenuItem(navLoc ? itemTranslation("nav", Utils.formatLocation(dataTranslation("preview.navcoords"), cache.navLocation())) : itemTranslation("navcoords"),
                    new ItemStack(navLoc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(23, new CreateCacheCoordMenuItem(loc ? itemTranslation("loc", Utils.formatLocation(dataTranslation("preview.coords"), cache.location())) : itemTranslation("loccoords"),
                    new ItemStack(loc ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(27, new CreateCacheTypeMenuItem(
                    new MenuItemState("traditional", ChatColor.GREEN + typeTranslation("traditional"), new ItemStack(Material.GREEN_TERRACOTTA), List.of(typeTranslation("traditional.lore1"), typeTranslation("traditional.lore2"))),
                    new MenuItemState("mystery", ChatColor.DARK_BLUE + typeTranslation("mystery"), new ItemStack(Material.BLUE_TERRACOTTA), List.of(typeTranslation("mystery.lore1"), typeTranslation("mystery.lore2"))),
                    new MenuItemState("multi", ChatColor.YELLOW + typeTranslation("multi"), new ItemStack(Material.YELLOW_TERRACOTTA), List.of(typeTranslation("multi.lore1"), typeTranslation("multi.lore2")))
            ).fromId(MinecachingAPI.get().getPlayerData(player).getCreatingCache().type().getId()));
            setItem(29, new CreateCacheSetNameMenuItem(dataTranslation("cache.name", name ? cache.name() : "???"),
                    new ItemStack(name ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(31, new CreateCacheSaveMenuItem(ready ? ChatColor.GREEN + translation("menu.save") : ChatColor.RED + translation("menu.save"),
                    new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of()));
            setItem(33, new CreateCacheSetCodeMenuItem(dataTranslation("cache.code", code ? cache.code() : "???"),
                    new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(35, new CreateCacheDeleteMenuItem(translation("menu.cancel"), new ItemStack(Material.GRAY_CONCRETE), List.of()));
        }

        super.update(player);
    }

    private String typeTranslation(String id) {
        return translation("menu.creating.type." + id);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.creating.item." + id, substitutions);
    }
}
