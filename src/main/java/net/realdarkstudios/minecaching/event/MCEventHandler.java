package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class MCEventHandler implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Minecaching.getInstance().getLogger().info(event.getAction().name());
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Minecaching.getInstance(), "attachedMinecacheId"), PersistentDataType.STRING)) {
            if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().hasItemMeta() && Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Minecaching.getInstance(), "attachedMinecacheId"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }
}
