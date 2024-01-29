package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MCEventHandler implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer().getUniqueId());

                if (!pdo.getNotifications().isEmpty()) {
                    MCMessages.sendMsg(event.getPlayer(), "plugin.notification.alert", ChatColor.GOLD);
                    for (Notification notification : pdo.getNotifications()) {
                        if (notification.getType() == NotificationType.INVALID && notification.getInitiator().equals(Utils.EMPTY_UUID) && notification.getCache().equals(Minecache.EMPTY)) continue;
                        MCMessages.sendMsg(event.getPlayer(), notification.getType().getTranslationKey(), ChatColor.GOLD, notification.getCache().id(), Utils.uuidName(notification.getInitiator()));
                    }
                }

                pdo.purgeNotifications();
            }
        }.runTaskLater(Minecaching.getInstance(), 40L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Minecaching.getInstance().getLogger().info(event.getAction().name());
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
