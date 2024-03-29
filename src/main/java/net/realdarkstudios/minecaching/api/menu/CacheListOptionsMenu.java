package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.BooleanMenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.item.clm.*;
import net.realdarkstudios.minecaching.api.menu.item.misc.GoBackAndUpdateMenuItem;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CacheListOptionsMenu extends MCMenu {
    public CacheListOptionsMenu(PlayerDataObject pdo, LocalizedMessages.Key titleKey, JavaPlugin plugin, MCMenu parent, Object... formatArgs) {
        super(titleKey, MenuSize.FOUR_ROW, plugin, parent, formatArgs);

        update(pdo.getPlayer());
    }

    @Override
    public void update(Player player) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(player);

        boolean trad = pdo.getCLMOptions().typeEnabled(MinecacheType.TRADITIONAL);
        boolean myst = pdo.getCLMOptions().typeEnabled(MinecacheType.MYSTERY);
        boolean mult = pdo.getCLMOptions().typeEnabled(MinecacheType.MULTI);
        boolean pub = pdo.getCLMOptions().statusEnabled(MinecacheStatus.PUBLISHED);
        boolean rev = pdo.getCLMOptions().statusEnabled(MinecacheStatus.REVIEWING);
        boolean nm = pdo.getCLMOptions().statusEnabled(MinecacheStatus.NEEDS_MAINTENANCE);
        boolean dis = pdo.getCLMOptions().statusEnabled(MinecacheStatus.DISABLED);
        boolean arc = pdo.getCLMOptions().statusEnabled(MinecacheStatus.ARCHIVED);

        setItem(0, new CLMTraditionalTypeMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_TYPE_TRADITIONAL, trad),
                styledStack(new ItemStack(Material.GREEN_TERRACOTTA), trad), List.of()));
        setItem(1, new CLMMysteryTypeMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_TYPE_MYSTERY, myst),
                styledStack(new ItemStack(Material.BLUE_TERRACOTTA), myst), List.of()));
        setItem(2, new CLMMultiTypeMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_TYPE_MULTI, mult),
                styledStack(new ItemStack(Material.YELLOW_TERRACOTTA), mult), List.of()));
        setItem(4, new BooleanMenuItem(
                new CLMNotOnlyFTFsMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_FTFS_ONLY, true),
                        new ItemStack(Material.LIME_DYE), List.of()),
                new CLMOnlyFTFsMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_FTFS_ONLY, false),
                        new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().ftfsOnly()));
        setItem(5, new BooleanMenuItem(
                new CLMNotOnlyFavoritesMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_FAVORITES_ONLY, true),
                        new ItemStack(Material.LIME_DYE), List.of()),
                new CLMOnlyFavoritesMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_FAVORITES_ONLY, false),
                        new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().favoritesOnly()));
        setItem(7, new ResetCLMOptionsMenuItem(MCMessageKeys.Menu.CLMOptions.RESET.translate(),
                new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
        setItem(8, new GoBackAndUpdateMenuItem(new ItemStack(Material.RED_CONCRETE), List.of(), getParent()));
        setItem(18, new CLMPublishedStatusMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_STATUS_PUBLISHED, pub),
                styledStack(new ItemStack(Material.GREEN_TERRACOTTA), pub), List.of()));
        setItem(19, new CLMReviewingStatusMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_STATUS_REVIEWING, rev),
                styledStack(new ItemStack(Material.YELLOW_TERRACOTTA), rev), List.of()));
        setItem(20, new CLMNeedsMaintenanceStatusMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_STATUS_NEEDS_MAINTENANCE, nm),
                styledStack(new ItemStack(Material.MAGENTA_TERRACOTTA), nm), List.of()));
        setItem(27, new CLMDisabledStatusMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_STATUS_DISABLED, dis),
                styledStack(new ItemStack(Material.LIGHT_GRAY_TERRACOTTA), dis), List.of()));
        setItem(28, new CLMArchivedStatusMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_STATUS_ARCHIVED, arc),
                styledStack(new ItemStack(Material.CYAN_TERRACOTTA), arc), List.of()));
        setItem(33, new BooleanMenuItem(
                new CLMToggleMostFavoritesMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_MOST_FAVORITES, true),
                        new ItemStack(Material.LIME_DYE), List.of()),
                new CLMToggleMostFavoritesMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_MOST_FAVORITES, false),
                        new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().mostFavoritesFirst()));
        setItem(34, new BooleanMenuItem(
                new CLMToggleNewestMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_NEWEST, true),
                        new ItemStack(Material.LIME_DYE), List.of()),
                new CLMToggleNewestMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_NEWEST, false),
                        new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().newestFirst()));
        setItem(35, new BooleanMenuItem(
                new CLMToggleOldestMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_OLDEST, true),
                        new ItemStack(Material.LIME_DYE), List.of()),
                new CLMToggleOldestMenuItem(styledName(MCMessageKeys.Menu.CLMOptions.ITEM_OLDEST, false),
                        new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().oldestFirst()));

        super.update(player);
    }

    private String styledName(LocalizedMessages.Key key, boolean b, Object... formatArgs) {
        return new LocalizedMessages.StyleOptions().setColor(b ? ChatColor.GREEN : ChatColor.RED).applyStyle(key.getMessage(formatArgs)).toLegacyText();
    }

    private ItemStack styledStack(ItemStack stack, boolean b) {
        return b ? stack : new ItemStack(Material.BEDROCK);
    }
}
