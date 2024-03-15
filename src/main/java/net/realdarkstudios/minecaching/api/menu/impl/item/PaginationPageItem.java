package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.menu.impl.PaginationMenu;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PaginationPageItem extends MenuItem {

    private final PaginationMenu menu;
    private final int modifier;

    public PaginationPageItem(PaginationMenu menu, String name, int modifier) {
        super(name, new ItemStack(Material.SPRUCE_BUTTON), List.of());
        this.menu = menu;
        this.modifier = modifier;
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        int clamped = clampPageNumber(event.getPlayer(), this.menu.getPage(event.getPlayer()) + modifier);
        this.menu.setPage(event.getPlayer(), clamped);
        event.setUpdate(true);
    }

    @Override
    public ItemStack getIcon(Player player) {
        int target = this.menu.getPage(player) + modifier;
        int clamped = clampPageNumber(player, this.menu.getPage(player) + modifier);
        boolean validPage = target == clamped;

        if (target != 0 && validPage) {
            ItemStack icon = this.getItem().clone();
            icon.setAmount(target);
            return applyText(icon);
        } else {
            return applyText(new ItemStack(Material.SPRUCE_BUTTON));
        }
    }

    private int getPageCount(Player player) {
        return (this.menu.getItems(player).size() / 45) + 1;
    }

    private int clampPageNumber(Player player, int page) {
        return Math.max(0, Math.min(page, getPageCount(player) - 1));
    }

}
