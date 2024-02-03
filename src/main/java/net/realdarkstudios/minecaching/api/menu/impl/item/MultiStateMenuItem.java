package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class MultiStateMenuItem extends MenuItem {
    private final MenuItemState[] states;
    private final HashMap<String, Integer> idMap = new HashMap<>();
    private int pointer = 0;
    public MultiStateMenuItem(MenuItemState... items) {
        super(items[0].name(), items[0].stack());
        this.states = items;
        int i = 0;
        for (MenuItemState state: states) {
            idMap.put(state.id(), i);
            i++;
        }
    }

    public MenuItemState getCurrentState() {
        return states[pointer];
    }

    @Override
    public String getName() {
        return getCurrentState().name();
    }

    @Override
    public List<String> getDescription() {
        return getCurrentState().description();
    }

    @Override
    public ItemStack getIcon() {
        return applyText(getCurrentState().stack());
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (pointer == states.length - 1) pointer = 0;
        else pointer++;

        event.setUpdate(true);

        super.onItemClick(event);
    }

    @Override
    protected ItemStack applyText(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(getName());
        if (!getDescription().isEmpty()) meta.setLore(getDescription());
        stack.setItemMeta(meta);
        return stack;
    }

    public MultiStateMenuItem fromId(String id) {
        pointer = idMap.get(id);
        return this;
    }
}
