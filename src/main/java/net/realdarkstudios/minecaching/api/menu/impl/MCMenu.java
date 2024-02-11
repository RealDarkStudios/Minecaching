package net.realdarkstudios.minecaching.api.menu.impl;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.ErrorMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public abstract class MCMenu {
    private JavaPlugin plugin;
    private String name, titleKey;
    private MenuSize size;
    private MenuItem[] items;
    private MCMenu parent;

    public static final MenuItem EMPTY_SLOT_ITEM = new MenuItem("", new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData()), List.of());

    public static MenuItem errorItem(String nameKey, Object... substitutions) {
        return new ErrorMenuItem(nameKey, new ItemStack(Material.BEDROCK), List.of());
    }

    public MCMenu(String titleKey, MenuSize size, JavaPlugin plugin, MCMenu parent, Object... substitutions) {
        this.plugin = plugin;
        this.titleKey = titleKey;
        this.name = MinecachingAPI.getLocalization().getTranslation(titleKey, substitutions);
        this.size = size;
        this.items = new MenuItem[size.getSlotCount()];
        this.parent = parent;
    }

    public MCMenu(String titleKey, MenuSize size, JavaPlugin plugin, Object... substitutions) {
        this(titleKey, size, plugin, null, substitutions);
    }

    public String getTitleKey() {
        return titleKey;
    }

    public String getName() {
        return name.equals("Translation Not Found") ? titleKey : name;
    }

    public String getName(Player player) {
       return getName();
    }

    public MenuSize getSize() {
        return size;
    }

    public int getSlotCount() {
        return size.getSlotCount();
    }

    public boolean hasParent() {
        return parent != null;
    }

    public MCMenu getParent() {
        return parent;
    }

    public void setParent(MCMenu parent) {
        this.parent = parent;
    }

    public MCMenu setItem(int slot, MenuItem item) {
        items[slot] = item;
        return this;
    }

    public MCMenu clearAllSlots() {
        for (int i = 0; i < size.getSlotCount(); i++) {
            items[i] = null;
        }
        return this;
    }

    public MCMenu fillEmptySlots(MenuItem item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) items[i] = item;
        }
        return this;
    }

    public MCMenu fillEmptySlots() {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(new MCMenuHolder(this, Bukkit.createInventory(player, size.getSlotCount())), size.getSlotCount(), getName());
        apply(inv, player);
        player.openInventory(inv);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() instanceof MCMenuHolder && ((MCMenuHolder) inv.getHolder()).getMenu().equals(this)) {
                apply(inv, player);
                player.updateInventory();
            }
        }
    }

    private void apply(Inventory inventory, Player player) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) inventory.setItem(i, items[i].getIcon(player));
            else inventory.setItem(i, null);
        }
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.LEFT) {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size.getSlotCount() && items[slot] != null) {
                MenuItem item = items[slot];

                Player player = (Player) event.getWhoClicked();
                MenuItemClickEvent clickEvent = new MenuItemClickEvent(player);
                item.onItemClick(clickEvent);
                item.playClickSound(player);
                if (clickEvent.update()) {
                    update(player);
                } else {
                    player.updateInventory();
                    if (clickEvent.close() || clickEvent.goBack()) {
                        UUID playerUuid = player.getUniqueId();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Player p1 = Bukkit.getPlayer(playerUuid);
                            if (p1 != null) p1.closeInventory();
                        }, 1);
                    }
                    if (clickEvent.goBack() && hasParent()) {
                        UUID playerUuid = player.getUniqueId();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Player p1 = Bukkit.getPlayer(playerUuid);
                            if (p1 != null) parent.open(p1);
                        }, 3);
                    }
                }
            }
        }
    }

    public void delete() {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    public enum MenuSize {
        ONE_ROW(9),
        TWO_ROW(18),
        THREE_ROW(27),
        FOUR_ROW(36),
        FIVE_ROW(45),
        SIX_ROW(54);

        private final int slotCount;

        MenuSize(int slotCount) {
            this.slotCount = slotCount;
        }

        public int getSlotCount() {
            return slotCount;
        }
    }

    protected boolean coordCheck(Location loc) {
        return !loc.equals(new Location(loc.getWorld(), 0, 0, 0));
    }

    protected boolean stringCheck(String str) {
        return str != null && !str.isEmpty();
    }

    abstract protected String itemTranslation(String id, Object... substitutions);

    protected String dataTranslation(String id, Object... substitutions) {
        return translation("menu.data.item." + id, substitutions);
    }

    protected String translation(String id, Object... substitutions) {
        return MinecachingAPI.getLocalization().getTranslation(id, substitutions);
    }
}

