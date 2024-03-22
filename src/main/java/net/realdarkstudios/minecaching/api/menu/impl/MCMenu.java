package net.realdarkstudios.minecaching.api.menu.impl;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.item.ErrorMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.MenuItemClickEvent;
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
import org.checkerframework.common.value.qual.IntRange;

import java.util.List;
import java.util.UUID;

/**
 * The base Menu class. This handles the dimensions, title, and slots, as well as opening, closing, and updating the menu.
 *
 * @see MCMenu#open(Player)
 * @see MCMenu#close(Player)
 * @see MCMenu#update(Player)
 * @see MCMenu#setItem(int, MenuItem)
 * @see MCMenu#setParent(MCMenu)
 * @since 0.3.0.0
 */
public abstract class MCMenu {
    private JavaPlugin plugin;
    private LocalizedMessages.Key titleKey;
    private String name;
    private MenuSize size;
    private MenuItem[] items;
    private MCMenu parent;

    /**
     * An empty slot item, with a gray stained glass pane and no title
     */
    public static final MenuItem EMPTY_SLOT_ITEM = new ErrorMenuItem("", new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData()), List.of());

    /**
     * Creates an error item, a bedrock item with a custom name
     * @param name The name of this item
     * @return The error item
     */
    public static MenuItem errorItem(String name) {
        return new ErrorMenuItem(name, new ItemStack(Material.BEDROCK), List.of());
    }

    /**
     * Creates a new MCMenu
     * @param titleKey The {@link LocalizedMessages.Key} to use for the title
     * @param size The {@link MenuSize} for this menu, {@link MenuSize#ONE_ROW} would have 9 slots (hotbar), while {@link MenuSize#SIX_ROW} would have 54 (double chest)
     * @param plugin The plugin associated with this menu. Used mainly for {@link MCMenu#onInventoryClick(InventoryClickEvent)}
     * @param parent The menu that 'owns' this menu, will be used if a {@link net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem} is in this menu
     * @param formatArgs Substitutions for the title
     */
    public MCMenu(LocalizedMessages.Key titleKey, MenuSize size, JavaPlugin plugin, MCMenu parent, Object... formatArgs) {
        this.plugin = plugin;
        this.titleKey = titleKey;
        this.name = titleKey.console(formatArgs);
        this.size = size;
        this.items = new MenuItem[size.getSlotCount()];
        this.parent = parent;
    }

    /**
     * Creates a new MCMenu
     * @param titleKey The {@link LocalizedMessages.Key} to use for the title
     * @param size The {@link MenuSize} for this menu, {@link MenuSize#ONE_ROW} would have 9 slots (hotbar), while {@link MenuSize#SIX_ROW} would have 54 (double chest)
     * @param plugin The plugin associated with this menu. Used mainly for {@link MCMenu#onInventoryClick(InventoryClickEvent)}
     * @param formatArgs Substitutions for the title
     */
    public MCMenu(LocalizedMessages.Key titleKey, MenuSize size, JavaPlugin plugin, Object... formatArgs) {
        this(titleKey, size, plugin, null, formatArgs);
    }

    /**
     * Gets the title path of this menu
     * @return The title path
     */
    public LocalizedMessages.Key getTitleKey() {
        return titleKey;
    }

    /**
     * Gets the name of this menu
     * @return The name
     */
    public String getName() {
        return name.equals("Translation Not Found") ? titleKey.path() : name;
    }

    /**
     * Gets the size of this menu
     * @return The size
     */
    public MenuSize getSize() {
        return size;
    }

    /**
     * Gets the slot count of this menu
     * @return The slot count
     */
    public int getSlotCount() {
        return size.getSlotCount();
    }

    /**
     * Checks if this menu has a parent
     * @return {@code true} if this menu has a parent, {@code false} otherwise
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Gets the parent of this menu
     * @return The parent ment
     */
    public MCMenu getParent() {
        return parent;
    }

    /**
     * Sets the parent of this menu
     * @param parent The parent menu
     */
    public void setParent(MCMenu parent) {
        this.parent = parent;
    }

    /**
     * Sets an item in the specified slot index
     * @param slot The index of the slot to set (0-indexed)
     * @param item The {@link MenuItem} to put into this slot
     * @return This menu
     */
    public MCMenu setItem(@IntRange(from=0, to=53) int slot, MenuItem item) {
        if (slot <= size.getSlotCount() - 1) items[slot] = item;
        else MinecachingAPI.tWarning(MessageKeys.Menu.ITEM_OUT_OF_BOUNDS, slot, size.getSlotCount());
        return this;
    }

    /**
     * Clears all the slots in this menu
     * @return This menu
     */
    public MCMenu clearAllSlots() {
        for (int i = 0; i < size.getSlotCount(); i++) {
            items[i] = null;
        }
        return this;
    }

    /**
     * Fills the empty slots with the specified item
     * @param item The {@link MenuItem} to fill empty slots with
     * @return This menu
     */
    public MCMenu fillEmptySlots(MenuItem item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) items[i] = item;
        }
        return this;
    }

    /**
     * Fills the empty slots with {@link MCMenu#EMPTY_SLOT_ITEM}
     * @return This menu
     */
    public MCMenu fillEmptySlots() {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    /**
     * Opens the menu for the given player
     * @param player The {@link Player} to open this menu for
     */
    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(new MCMenuHolder(this, Bukkit.createInventory(player, size.getSlotCount())), size.getSlotCount(), getName());
        apply(inv, player);
        player.openInventory(inv);
    }

    /**
     * Closes the menu for the given player
     * @param player The {@link Player} to close this menu for
     */
    public void close(Player player) {
        player.closeInventory();
    }

    /**
     * Updates the menu for the given player
     * @param player The {@link Player} to update this menu for
     */
    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() instanceof MCMenuHolder && ((MCMenuHolder) inv.getHolder()).getMenu().equals(this)) {
                apply(inv, player);
                player.updateInventory();
            }
        }
    }

    /**
     * Sets the items of the inventory
     * @param inventory The {@link Inventory} to set the icons of
     * @param player The {@link Player} to update the icons for
     */
    private void apply(Inventory inventory, Player player) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) inventory.setItem(i, items[i].getIcon(player));
            else inventory.setItem(i, null);
        }
    }

    /**
     * Called when a slot in the inventory is clicked
     * @param event The {@link InventoryClickEvent} that was passed
     */
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
                            if (p1 != null) {
                                parent.open(p1);
                                parent.update(p1);
                            }
                        }, 3);
                    }
                }
            }
        }
    }

    /**
     * Deletes this menu
     */
    public void delete() {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    /**
     * Checks if the given coordinates is not equal to the default (0, 0, 0)
     * @param loc The {@link Location} to check
     * @return {@code true} if the location is not the default, {@code false} otherwise
     */
    protected boolean coordCheck(Location loc) {
        return !loc.equals(new Location(loc.getWorld(), 0, 0, 0));
    }

    /**
     * Checks if the given string is not null or empty
     * @param str The {@link String} to check
     * @return {@code true} if the string is not null or empty, {@code false} otherwise
     */
    protected boolean stringCheck(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * An {@link Enum} with the different available menu sizes.
     * <ul>
     * <li>{@link MenuSize#ONE_ROW}: 9 slots (hotbar)</li>
     * <li>{@link MenuSize#TWO_ROW}: 18 slots</li>
     * <li>{@link MenuSize#THREE_ROW}: 27 slots (normal chest)</li>
     * <li>{@link MenuSize#FOUR_ROW}: 36 slots (inventory + hotbar (no gap))</li>
     * <li>{@link MenuSize#FIVE_ROW}: 45 slots</li>
     * <li>{@link MenuSize#ONE_ROW}: 54 slots (double chest)</li>
     * </ul>
     */
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

        /**
         * Gets the slot count of the menu size
         * @return The slot count
         */
        public int getSlotCount() {
            return slotCount;
        }
    }
}

