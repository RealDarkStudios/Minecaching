package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BooleanMenuItem extends MenuItem {
    private final MenuItem trueItem, falseItem;
    protected boolean bool = true;
    public BooleanMenuItem(MenuItem trueItem, MenuItem falseItem) {
        super(trueItem.getName(), trueItem.getItem(), trueItem.getLore());
        this.trueItem = trueItem;
        this.falseItem = falseItem;
    }

    public MenuItem getTrueItem() {
        return trueItem;
    }

    public MenuItem getFalseItem() {
        return falseItem;
    }

    public boolean getBool() {
        return bool;
    }

    public MenuItem getCurrentState() {
        return bool ? trueItem : falseItem;
    }

    @Override
    public String getName() {
        return getCurrentState().getName();
    }

    @Override
    public List<String> getLore() {
        return getCurrentState().getLore();
    }

    @Override
    public ItemStack getIcon(Player player) {
        return applyText(getCurrentState().getItem());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        bool = !bool;

        getCurrentState().onItemClick(event);
        event.setUpdate(true);

        super.onItemClick(event);
    }

    @Override
    protected ItemStack applyText(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(getName());
        if (!getLore().isEmpty()) meta.setLore(getLore());
        stack.setItemMeta(meta);
        return stack;
    }

    public BooleanMenuItem set(boolean b) {
        bool = b;
        return this;
    }
}
