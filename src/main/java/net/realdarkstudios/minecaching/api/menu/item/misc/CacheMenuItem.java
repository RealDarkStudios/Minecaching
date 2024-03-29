package net.realdarkstudios.minecaching.api.menu.item.misc;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CacheMenuItem extends MenuItem {
    public CacheMenuItem(String name, Minecache cache) {
        super(name, new ItemStack(Material.SPYGLASS), List.of(
                MCMessageKeys.Menu.Data.PREVIEW_TYPE.translateWithOtherStyle(getTypeStyle(cache.type()), cache.type().getTranslation()),
                MCMessageKeys.Menu.Data.PREVIEW_STATUS.translateWithOtherStyle(getStatusStyle(cache.status()), cache.status().getTranslation()),
                MCUtils.formatLocation(MCMessageKeys.Menu.Data.PREVIEW_NAVIGATION_COORDS.translate(), cache.navLocation()),
                cache.hasMaintainer() ? MCMessageKeys.Menu.Data.PREVIEW_MAINTAINER.translate(MCUtils.uuidName(cache.maintainer())) : "",
                MCMessageKeys.Menu.Data.PREVIEW_AUTHOR.translate(MCUtils.uuidName(cache.originalAuthor())),
                MCMessageKeys.Menu.Data.PREVIEW_HIDDEN.translate(cache.hidden().format(DateTimeFormatter.ofPattern("MM/dd/yy hh:mma"))),
                MCMessageKeys.Menu.Data.PREVIEW_FINDS.translate(cache.finds()),
                MCMessageKeys.Menu.Data.PREVIEW_FAVORITES.translate(cache.favorites())));
    }

    private static LocalizedMessages.StyleOptions getTypeStyle(MinecacheType type) {
        return switch (type) {
            case TRADITIONAL -> new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GREEN);
            case MYSTERY -> new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_BLUE);
            case MULTI -> new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD);
            default -> LocalizedMessages.StyleOptions.NONE;
        };
    }

    private static LocalizedMessages.StyleOptions getStatusStyle(MinecacheStatus status) {
        return switch (status) {
            case PUBLISHED -> new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN);
            case NEEDS_MAINTENANCE -> new LocalizedMessages.StyleOptions().setColor(ChatColor.RED);
            case DISABLED -> new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY);
            case ARCHIVED -> new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY);
            case REVIEWING -> new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW);
            default -> LocalizedMessages.StyleOptions.NONE;
        };
    }
}
