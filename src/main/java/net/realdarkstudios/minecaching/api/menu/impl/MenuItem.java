package net.realdarkstudios.minecaching.api.menu.impl;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MenuItem {
    private final String name;
    private final ItemStack item;
    private final List<String> description;

    public MenuItem(String name, ItemStack item) {
        this.name = name;
        this.item = item;
        this.description = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getIcon() {
        return applyText(item.clone());
    }

    public List<String> getDescription() {
        return description;
    }

    public void onItemClick(MenuItemClickEvent event) {
        // Does nothing by default
    }

    private ItemStack applyText(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (!description.isEmpty()) meta.setLore(description);
        stack.setItemMeta(meta);
        return stack;
    }
}
