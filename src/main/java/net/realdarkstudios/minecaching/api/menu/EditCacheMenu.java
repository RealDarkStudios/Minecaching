package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.CloseMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EditCacheMenu extends CacheDataMenu {
    PlayerDataObject author;
    public EditCacheMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("Editing Cache", cache, plugin);
        this.author = author;

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

        boolean isReady = cache.name() != null && !cache.name().isEmpty() && !cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0))
                && !cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) && cache.code() != null && !cache.code().isEmpty();

        setItem(0, new ResetEditCacheMenuItem(ChatColor.GRAY + "Reset Cache", new ItemStack(Material.LIGHT_GRAY_CONCRETE), cache));
        setItem(4, new CachePreviewItem(cache.id() + ": " + (cache.name() != null ? cache.name() : "???"), new ItemStack(Material.SPYGLASS),
                "Type: " + cache.type().getTranslation(), Utils.formatLocation("Coords", cache.location()),
                Utils.formatLocation("Lode Coords", cache.location())));
        setItem(8, new CloseMenuItem("Exit", new ItemStack(Material.RED_CONCRETE)));
        setItem(21, new EditCacheLodeCoordMenuItem(!cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0)) ? Utils.formatLocation("Lode Coords", cache.lodeLocation()) : "Cache Lode Coordinate", new ItemStack(!cache.lodeLocation().equals(new Location(cache.lodeLocation().getWorld(), 0, 0, 0)) ? Material.GREEN_WOOL : Material.RED_WOOL )));
        setItem(23, new EditCacheCoordMenuItem(!cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) ? Utils.formatLocation("Coords", cache.location()) : "Cache Coordinates", new ItemStack(!cache.location().equals(new Location(cache.location().getWorld(), 0, 0, 0)) ? Material.GREEN_WOOL : Material.RED_WOOL )));
        setItem(27, new OpenConfirmationMenuItem(new DeleteCacheMenuItem("Delete", new ItemStack(Material.BLACK_CONCRETE), cache, "Clicking this WILL delete this Minecache forever!"), this));
        setItem(29, new SetEditCacheNameMenuItem(cache.name() != null && !cache.name().isEmpty() ? "Name: " + cache.name() : ChatColor.RESET + "Name", new ItemStack(cache.name() != null && !cache.name().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL)));
        setItem(31, new SaveEditCacheMenuItem(isReady ? ChatColor.DARK_GREEN + "Save" : ChatColor.RED + "Save", new ItemStack(isReady ? Material.LIME_CONCRETE : Material.BEDROCK)));
        setItem(33, new SetEditCacheCodeMenuItem(cache.code() != null && !cache.code().isEmpty() ? "Code: " + cache.code() : ChatColor.RESET + "Code", new ItemStack(cache.code() != null && !cache.code().isEmpty() ? Material.GREEN_WOOL : Material.RED_WOOL)));
        setItem(35, new CancelEditCacheMenuItem("Stop Editing", new ItemStack(Material.GRAY_CONCRETE), "Unsaved changes WILL be lost!"));

        super.update(player);
    }
}
