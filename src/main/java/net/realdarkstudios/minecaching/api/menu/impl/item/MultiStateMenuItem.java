package net.realdarkstudios.minecaching.api.menu.impl.item;

import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class MultiStateMenuItem extends MenuItem {
    private final MenuItemState[] states;
    private final HashMap<String, Integer> idMap = new HashMap<>();
    protected int pointer = 0;
    public MultiStateMenuItem(MenuItemState... items) {
        super(items[0].name(), items[0].stack(), items[0].lore());
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
    public List<String> getLore() {
        return getCurrentState().lore();
    }

    @Override
    public ItemStack getIcon(Player player) {
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
        if (!getLore().isEmpty()) meta.setLore(getLore());
        stack.setItemMeta(meta);
        return stack;
    }

    public MultiStateMenuItem fromId(String id) {
        pointer = idMap.getOrDefault(id, 0);
        return this;
    }
}
