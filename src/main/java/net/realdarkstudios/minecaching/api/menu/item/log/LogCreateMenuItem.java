package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.Log;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.event.MenuItemClickEvent;
import net.realdarkstudios.minecaching.event.minecache.MinecacheFoundEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import net.realdarkstudios.minecaching.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogCreateMenuItem extends MenuItem {

    public LogCreateMenuItem(String name, ItemStack item, List<String> lore) {
        super(name, item, lore);
    }

    @Override
    public void onItemClick(MenuItemClickEvent event) {
        if (getItem().getType().equals(Material.BEDROCK)) return;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(event.getPlayer());
        LogType logType = pdo.getLogType();
        String logMessage = pdo.getLogMessage().isBlank() ? translation("menu.log.message." + logType.getId()) : pdo.getLogMessage().trim();
        Minecache cache = MinecachingAPI.get().getMinecache(pdo.getLocatingId());

        if (cache.type().equals(MinecacheType.TRADITIONAL) || cache.type().equals(MinecacheType.MYSTERY)) {
            if (!logType.equals(LogType.FOUND) || cache.code().equals(pdo.getLogCode())) {
                if (!logType.equals(LogType.FOUND) || event.getPlayer().getLocation().distance(cache.location()) < 25) {
                    if (!(logType.equals(LogType.NOTE) || logType.equals(LogType.FLAG)) && event.getPlayer().getUniqueId().equals(cache.author())) {
                        MCMessages.sendErrorMsg(event.getPlayer(), "logcache.logowncache");
                        return;
                    }

                    if ((logType.equals(LogType.FLAG) || logType.equals(LogType.NOTE)) && pdo.getLogMessage().isEmpty()) {
                        MCMessages.sendErrorMsg(event.getPlayer(), "logcache.noteflagempty");
                        return;
                    }

                    boolean isFTF = pdo.isFTF(cache);
                    if (logType.equals(LogType.FOUND)) {
                        // Emit MinecacheFoundEvent
                        MinecacheFoundEvent foundEvent = new MinecacheFoundEvent(cache, event.getPlayer(), pdo.isFTF(cache));
                        Bukkit.getPluginManager().callEvent(foundEvent);

                        if (foundEvent.isCancelled()) {
                            MCMessages.sendErrorMsg(event.getPlayer(), "logcache");
                            super.onItemClick(event);
                            return;
                        }

                        pdo.addFind(cache.id());
                        if (isFTF) {
                            pdo.addFTF(cache.id());
                            MinecachingAPI.get().saveMinecache(cache.setFTF(pdo.getUniqueID()).setFinds(cache.finds() + 1), false);
                        } else MinecachingAPI.get().saveMinecache(cache.setFinds(pdo.getFinds().contains(cache.id()) ? cache.finds() : cache.finds() + 1), false);

                        MinecachingAPI.get().save();
                        MinecachingAPI.get().update();

                        MCMessages.sendMsg(event.getPlayer(), "logcache.find", ChatColor.GREEN, cache.id(), cache.name());
                        MCMessages.sendMsg(event.getPlayer(), isFTF ? "logcache.findcount.ftf" : "logcache.findcount", ChatColor.GREEN, pdo.getFinds().size(), isFTF ? pdo.getFTFs().size() : null);
                    } else {
                        MCMessages.sendMsg(event.getPlayer(), "logcache.log", ChatColor.GREEN, cache.id(), cache.name());
                    }

                    if (logType.equals(LogType.FLAG) && !pdo.getUniqueID().equals(cache.author())) MinecachingAPI.get().createNotification(cache.author(), pdo.getUniqueID(), NotificationType.FLAG, cache);

                    Log log = Utils.createLog(pdo.getUniqueID(), cache, logType, logMessage, isFTF);
                } else {
                    MCMessages.sendErrorMsg(event.getPlayer(), "logcache.distance");
                }
            } else {
                MCMessages.sendErrorMsg(event.getPlayer(), "logcache.code");
            }
        } else {
            MCMessages.sendErrorMsg(event.getPlayer(), "logcache.unsupported");
        }

        pdo.setLocatingId("NULL");
        pdo.setLogType(LogType.FOUND);
        pdo.setLogMessage("");
        pdo.setLogCode("");

        event.setClose(true);
    }
}
