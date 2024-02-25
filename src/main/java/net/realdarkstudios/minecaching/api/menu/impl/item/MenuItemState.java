package net.realdarkstudios.minecaching.api.menu.impl.item;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record MenuItemState(String id, String name, ItemStack stack, List<String> lore, Object... substitutions) {
}
