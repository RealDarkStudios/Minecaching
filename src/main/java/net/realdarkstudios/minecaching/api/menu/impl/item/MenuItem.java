package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 *
 */
public class MenuItem {
    protected static final Sound buttonClickSound = Sound.UI_BUTTON_CLICK;
    protected final String name;
    protected final ItemStack item;
    protected final List<String> lore;

    public MenuItem(String name, ItemStack item, List<String> lore) {
        this.name = name;
        this.item = item;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getIcon(Player player) {
        return applyText(item.clone());
    }

    public List<String> getLore() {
        return lore;
    }

    public void playClickSound(Player plr) {
        plr.playSound(plr.getLocation(), buttonClickSound, .5f, 1);
    }

    public void onItemClick(MenuItemClickEvent event) {
        // Does nothing by default
    }

    protected ItemStack applyText(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(getName());
        if (!getLore().isEmpty()) meta.setLore(getLore());
        stack.setItemMeta(meta);
        return stack;
    }

    protected static String translation(String key, Object... substitutions) {
        return MinecachingAPI.getLocalization().getTranslation(key, substitutions);
    }
}
