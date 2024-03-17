package net.realdarkstudios.minecaching.api.event;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenuHolder;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.Notification;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MCEventHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest chest) {
            if (chest.getPersistentDataContainer().has(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING)) {
                String id = chest.getPersistentDataContainer().get(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING);
                MinecachingAPI.get().getFilteredCaches(c -> c.id().equals(id))
                        .stream().filter(c -> c.location().equals(event.getBlock().getLocation()))
                        .forEach(c -> {
                            MinecachingAPI.info("Block Break (Cache Chest)");
                            sendBlockBreakComponent(event.getPlayer(), c.id());
                            event.setCancelled(true);
                        });
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Objects.equals(event.getHand(), EquipmentSlot.HAND) &&  event.hasBlock() && event.getClickedBlock().getState() instanceof Chest chest) {
            if (chest.getPersistentDataContainer().has(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING)) {
                String id = chest.getPersistentDataContainer().get(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING);
                assert id != null;
                PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(p);
                Minecache c = MinecachingAPI.get().getMinecache(id);
                if (!p.isSneaking() && c.location().equals(chest.getLocation())) {
                    event.setCancelled(true);
                    // open log menu
                    pdo.setLogCode(c.code());
                    LogMenu menu = new LogMenu(c, pdo);
                    menu.open(p);
                } else if (c.location().equals(chest.getLocation())) {
                    event.setUseInteractedBlock(Event.Result.ALLOW);
                    event.setUseItemInHand(Event.Result.DENY);
                }
                else event.setUseInteractedBlock(Event.Result.DEFAULT);
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && Objects.equals(event.getHand(), EquipmentSlot.HAND) && event.hasBlock() && event.getClickedBlock().getState() instanceof Chest chest) {
            if (chest.getPersistentDataContainer().has(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING)) {
                String id = chest.getPersistentDataContainer().get(MCUtils.LINKED_CACHE_KEY, PersistentDataType.STRING);
                assert id != null;
                Minecache c = MinecachingAPI.get().getMinecache(id);
                if (c.location().equals(chest.getLocation())) {
                    event.setUseInteractedBlock(Event.Result.ALLOW);
                    event.setUseItemInHand(Event.Result.DENY);
                }
                else event.setUseInteractedBlock(Event.Result.DEFAULT);
            }
        }
    }

    private void sendBlockBreakComponent(Player player, String id) {
        TextComponent toSend = MessageKeys.Error.Misc.CACHE_BLOCK.translateComponentWithOtherStyle(new LocalizedMessages.StyleOptions().setColor(ChatColor.RED), id);
        toSend.addExtra(MessageKeys.Error.Misc.CACHE_BLOCK_CLICK_HERE.translateComponentWithOtherStyle(LocalizedMessages.StyleOptions.ERROR
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/deletecache openmenu " + id)));

        LocalizedMessages.sendComponent(player, toSend);
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                if (AutoUpdater.hasUpdate() && event.getPlayer().hasPermission("minecaching.admin")) {
                    LocalizedMessages.send(event.getPlayer(), Config.getInstance().autoUpdate() ? MessageKeys.Plugin.Update.AVAILABE_AUTO :
                                    MessageKeys.Plugin.Update.AVAILABLE, AutoUpdater.getNewestVersion());
                }

                PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer().getUniqueId());
                pdo.updateData();

                if (!pdo.getNotifications().isEmpty()) {
                    LocalizedMessages.send(event.getPlayer(), MessageKeys.Plugin.Notification.NOTIFICATION_ALERT);
                    for (Notification notification : pdo.getNotifications()) {
                        if (notification.getType() == NotificationType.INVALID && notification.getInitiator().equals(MCUtils.EMPTY_UUID) && notification.getCache().equals(Minecache.EMPTY)) continue;
                        LocalizedMessages.send(event.getPlayer(), notification.getType().getTranslationKey(), notification.getCache().id(), MCUtils.uuidName(notification.getInitiator()));
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

        if (event.getWhoClicked() instanceof Player && event.getInventory().getHolder() instanceof MCMenuHolder) {
            event.setCancelled(true);
            ((MCMenuHolder) event.getInventory().getHolder()).getMenu().onInventoryClick(event);
        }
    }

    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().hasItemMeta() && Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Minecaching.getInstance(), "attachedMinecacheId"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    private static List<Player> onlineOPPlayerList = List.of();

    static {
        // Handle reloading MCMenus when a player is deopped (to avoid possibly executing something only ops should be able to do)

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Minecaching.getInstance(), () -> {
            List<Player> opList = new ArrayList<>();

            for (Player p: Bukkit.getOnlinePlayers()) {
                if (p.isOp()) opList.add(p);

                if (!p.isOp() && p.isOnline() && onlineOPPlayerList.contains(p)) {
                    if (p.getOpenInventory() != null) {
                        Inventory inv = p.getOpenInventory().getTopInventory();
                        if (inv.getHolder() instanceof MCMenuHolder) p.closeInventory();
                    }
                    LocalizedMessages.send(p, MessageKeys.Menu.PERM_CHANGE);
                }
            }

            onlineOPPlayerList = opList;
        }, 1L, 1L);
    }
}
