package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.CloseMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.item.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AddCacheMenu extends MCMenu {
    private final PlayerDataObject author;
    private Minecache cache;

    public AddCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("Creating Cache", MenuSize.FOUR_ROW, plugin);
        this.author = author;
        this.cache = cache;

        update(author.getPlayer().getPlayer());
    }

    @Override
    public void update(Player player) {
        this.cache = author.getCache();
        clearAllSlots();
        fillEmptySlots();

        boolean isReady = cache.name() != null && !cache.name().isEmpty() && !cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0))
                && !cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) && cache.code() != null && !cache.code().isEmpty();

        setItem(8, new CloseMenuItem("Exit", new ItemStack(Material.RED_CONCRETE)));

        if (author.getCache().id().equals("NULL")) {
            setItem(13, new CreateCacheMenuItem("Create New Cache", new ItemStack(Material.GREEN_CONCRETE)));
        } else {
            setItem(0, new ResetAddCacheMenuItem("Reset Cache", new ItemStack(Material.LIGHT_GRAY_CONCRETE)));
            setItem(4, new CachePreviewItem(cache.id() + ": " + (cache.name() != null ? cache.name() : "???"), new ItemStack(Material.SPYGLASS),
                    "Type: " + cache.type().getTranslation(), Utils.formatLocation("Coords", cache.location()),
                    Utils.formatLocation("Lode Coords", cache.lodeLocation())));
            setItem(21, new AddCacheLodeCoordMenuItem(!cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0)) ? Utils.formatLocation("Lode Coords", cache.lodeLocation()) : "Cache Lode Coordinates", new ItemStack(!cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0)) ? Material.GREEN_WOOL : Material.RED_WOOL )));
            setItem(23, new AddCacheCoordMenuItem(!cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) ? Utils.formatLocation("Coords", cache.location()) : "Cache Coordinates", new ItemStack(!cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) ? Material.GREEN_WOOL : Material.RED_WOOL )));
            setItem(27, new CacheTypeMenuItem(
                    new MenuItemState("traditional", new ItemStack(Material.GREEN_TERRACOTTA), ChatColor.DARK_GREEN + "Traditional", List.of("A traditional cache.", "No mystery, no stages, no nothing.")),
                    new MenuItemState("mystery", new ItemStack(Material.BLUE_TERRACOTTA), ChatColor.DARK_BLUE + "Mystery", List.of("A mystery cache.", "Solve a mystery to unlock the coordinates.")),
                    new MenuItemState("multi", new ItemStack(Material.YELLOW_TERRACOTTA), ChatColor.YELLOW + "Multi", List.of("A multi cache.", "You must go to more than one place."))
            ).fromId(MinecachingAPI.get().getPlayerData(player).getCache().type().getId()));
            setItem(29, new SetAddCacheNameMenuItem(cache.name() != null && !cache.name().isEmpty() ? "Name: " + cache.name() : ChatColor.RESET + "Name", new ItemStack(cache.name() != null && !cache.name().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL)));
            setItem(31, new SaveAddCacheMenuItem(isReady ? ChatColor.DARK_GREEN + "Save" : ChatColor.RED + "Save", new ItemStack(isReady ? Material.LIME_CONCRETE : Material.BEDROCK)));
            setItem(33, new SetAddCacheCodeMenuItem(cache.code() != null && !cache.code().isEmpty() ? "Code: " + cache.code() : ChatColor.RESET + "Code", new ItemStack(cache.code() != null && !cache.code().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL)));
            setItem(35, new DeleteAddCacheMenuItem("Cancel", new ItemStack(Material.GRAY_CONCRETE)));
        }

        super.update(player);
    }
}
