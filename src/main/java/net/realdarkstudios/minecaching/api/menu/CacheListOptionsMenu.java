package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.BooleanMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.GoBackAndUpdateMenuItem;
import net.realdarkstudios.minecaching.api.menu.item.clm.*;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CacheListOptionsMenu extends MCMenu {
    public CacheListOptionsMenu(PlayerDataObject pdo, String titleKey, JavaPlugin plugin, MCMenu parent, Object... substitutions) {
        super(titleKey, MenuSize.FOUR_ROW, plugin, parent, substitutions);

        update(pdo.getPlayer().getPlayer());
    }

    @Override
    public void update(Player player) {
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(player);

        setItem(0, new CLMTraditionalTypeMenuItem(pdo, itemTranslation("type.traditional"), new ItemStack(Material.GREEN_TERRACOTTA), List.of()));
        setItem(1, new CLMMysteryTypeMenuItem(pdo, itemTranslation("type.mystery"), new ItemStack(Material.BLUE_TERRACOTTA), List.of()));
        setItem(2, new CLMMultiTypeMenuItem(pdo, itemTranslation("type.multi"), new ItemStack(Material.YELLOW_TERRACOTTA), List.of()));
        setItem(4, new BooleanMenuItem(
                new CLMToggleNewestMenuItem(itemTranslation("newest"), new ItemStack(Material.LIME_DYE), List.of()),
                new CLMToggleNewestMenuItem(itemTranslation("newest"), new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().newestFirst()));
        setItem(13, new BooleanMenuItem(
                new CLMToggleOldestMenuItem(itemTranslation("oldest"), new ItemStack(Material.LIME_DYE), List.of()),
                new CLMToggleOldestMenuItem(itemTranslation("oldest"), new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().oldestFirst()));
        setItem(31, new BooleanMenuItem(
                new CLMNotOnlyFTFsMenuItem(itemTranslation("ftfsonly"), new ItemStack(Material.LIME_DYE), List.of()),
                new CLMOnlyFTFsMenuItem(itemTranslation("ftfsonly"), new ItemStack(Material.GRAY_DYE), List.of())
        ).set(pdo.getCLMOptions().ftfsOnly()));
        setItem(7, new ResetCLMOptionsMenuItem(itemTranslation("reset"), new ItemStack(Material.LIGHT_GRAY_CONCRETE), List.of()));
        setItem(8, new GoBackAndUpdateMenuItem(translation("menu.goback"), new ItemStack(Material.RED_CONCRETE), List.of(), getParent()));
        setItem(18, new CLMPublishedStatusMenuItem(pdo, itemTranslation("status.published"), new ItemStack(Material.GREEN_TERRACOTTA), List.of()));
        setItem(19, new CLMReviewingStatusMenuItem(pdo, itemTranslation("status.reviewing"), new ItemStack(Material.YELLOW_TERRACOTTA), List.of()));
        setItem(20, new CLMNeedsMaintenanceStatusMenuItem(pdo, itemTranslation("status.needs_maintenance"), new ItemStack(Material.MAGENTA_TERRACOTTA), List.of()));
        setItem(27, new CLMDisabledStatusMenuItem(pdo, itemTranslation("status.disabled"), new ItemStack(Material.LIGHT_GRAY_TERRACOTTA), List.of()));
        setItem(28, new CLMArchivedStatusMenuItem(pdo, itemTranslation("status.archived"), new ItemStack(Material.CYAN_TERRACOTTA), List.of()));

        super.update(player);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.clmoptions.item." + id, substitutions);
    }
}
