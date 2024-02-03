package net.realdarkstudios.minecaching.api.menu.impl.item;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record MenuItemState(String id, ItemStack stack, String name, List<String> description) {
}
