package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.CloseMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.item.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AddCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public AddCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("menu.creating.title", MenuSize.FOUR_ROW, plugin);
        this.author = author;
        this.cache = cache;

        update(author.getPlayer().getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getCreatingCache();
        clearAllSlots();
        fillEmptySlots();

        boolean ready = stringCheck(cache.name()) && stringCheck(cache.code()) && coordCheck(cache.navLocation()) && coordCheck(cache.location());

        setItem(8, new CloseMenuItem(translation("menu.close"), new ItemStack(Material.RED_CONCRETE), List.of()));

        if (author.getCreatingCache().id().equals("NULL")) {
            setItem(13, new CreateCacheMenuItem(itemTranslation("create"), new ItemStack(Material.GREEN_CONCRETE), List.of()));
        } else {

            setItem(0, new ResetAddCacheMenuItem(itemTranslation("reset"), new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
            setItem(4, new CachePreviewItem(itemTranslation("preview", cache.id(), stringCheck(cache.name()) ? cache.name() : "???"), new ItemStack(Material.SPYGLASS), List.of(
                    dataTranslation("preview.type", cache.type().getTranslation()),
                    dataTranslation("preview.code", stringCheck(cache.code()) ? cache.code() : "???"),
                    Utils.formatLocation(dataTranslation("preview.coords"), cache.location()),
                    Utils.formatLocation(dataTranslation("preview.navcoords"), cache.navLocation()))));
            setItem(21, new AddCacheNavCoordMenuItem(coordCheck(cache.navLocation()) ? itemTranslation("nav", Utils.formatLocation(dataTranslation("preview.navcoords"), cache.navLocation())) : itemTranslation("navcoords"),
                    new ItemStack(coordCheck(cache.navLocation()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(23, new AddCacheCoordMenuItem(coordCheck(cache.location()) ? itemTranslation("loc", Utils.formatLocation(dataTranslation("preview.coords"), cache.location())) : itemTranslation("loccoords"),
                    new ItemStack(coordCheck(cache.location()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(27, new CacheTypeMenuItem(
                    new MenuItemState("traditional", new ItemStack(Material.GREEN_TERRACOTTA), "menu.creating.type.traditional", List.of("menu.creating.type.traditional.lore1", "menu.creating.type.traditional.lore2"), ChatColor.GREEN),
                    new MenuItemState("mystery", new ItemStack(Material.BLUE_TERRACOTTA), "menu.creating.type.mystery", List.of("menu.creating.type.mystery.lore1", "menu.creating.type.mystery.lore2"), ChatColor.DARK_BLUE),
                    new MenuItemState("multi", new ItemStack(Material.YELLOW_TERRACOTTA), "menu.creating.type.multi", List.of("menu.creating.type.multi.lore1", "menu.creating.type.multi.lore2"), ChatColor.YELLOW)
            ).fromId(MinecachingAPI.get().getPlayerData(player).getCreatingCache().type().getId()));
            setItem(29, new SetAddCacheNameMenuItem(dataTranslation("cache.name", stringCheck(cache.name()) ? cache.name() : "???"),
                    new ItemStack(stringCheck(cache.name()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(31, new SaveAddCacheMenuItem(ready ? ChatColor.GREEN + translation("menu.save") : ChatColor.RED + translation("menu.save"), new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of()));
            setItem(33, new SetAddCacheCodeMenuItem(dataTranslation("cache.code", stringCheck(cache.code()) ? cache.code() : "???"),
                    new ItemStack(stringCheck(cache.code()) ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()));
            setItem(35, new DeleteAddCacheMenuItem(translation("menu.cancel"), new ItemStack(Material.GRAY_CONCRETE), List.of()));
        }

        super.update(player);
    }

    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.creating.item." + id, substitutions);
    }
}
