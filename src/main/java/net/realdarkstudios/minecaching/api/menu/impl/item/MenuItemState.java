package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record MenuItemState(String id, ItemStack stack, String nameKey, List<String> loreKeys, Object... substitutions) {
    public String name() {
        return MinecachingAPI.getLocalization().getTranslation(nameKey, substitutions);
    }

    public List<String> lore() {
        ArrayList<String> lore = new ArrayList<>(loreKeys.size());

        for (String key: loreKeys) {
            lore.add(MinecachingAPI.getLocalization().getTranslation(key));
        }

        return lore;
    }
}
