package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.commons.event.MenuItemClickEvent;
import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheFoundEvent;
import net.realdarkstudios.minecaching.api.log.Log;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
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
        String logMessage = pdo.getLogMessage().isBlank() ? getDefaultMessage(pdo) : pdo.getLogMessage().trim();
        Minecache cache = MinecachingAPI.get().getMinecache(pdo.getLocatingId());

        if (cache.type().equals(MinecacheType.TRADITIONAL) || cache.type().equals(MinecacheType.MYSTERY)) {
            if (!logType.equals(LogType.FOUND) || cache.code().equals(pdo.getLogCode())) {
                if (!logType.equals(LogType.FOUND) || event.getPlayer().getLocation().distance(cache.location()) < 25) {
                    if (!(logType.equals(LogType.NOTE) || logType.equals(LogType.FLAG)) && event.getPlayer().getUniqueId().equals(cache.owner()) || event.getPlayer().getUniqueId().equals(cache.originalAuthor())) {
                        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.LOG_BY_OWNER);
                        return;
                    }

                    if ((logType.equals(LogType.FLAG) || logType.equals(LogType.NOTE)) && pdo.getLogMessage().isEmpty()) {
                        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.NOTE_OR_FLAG_MSG_EMPTY);
                        return;
                    }

                    boolean isFTF = pdo.isFTF(cache);
                    if (logType.equals(LogType.FOUND)) {
                        // Emit MinecacheFoundEvent
                        MinecacheFoundEvent foundEvent = new MinecacheFoundEvent(cache, event.getPlayer(), pdo.isFTF(cache));
                        Bukkit.getPluginManager().callEvent(foundEvent);

                        if (foundEvent.isCancelled()) {
                            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.GENERAL);
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

                        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Log.FIND, cache.id(), cache.name());
                        LocalizedMessages.send(event.getPlayer(), (isFTF ? MCMessageKeys.Command.Log.FIND_COUNT_WITH_FTFS : MCMessageKeys.Command.Log.FIND_COUNT),
                                pdo.getFinds().size(), isFTF ? pdo.getFTFs().size() : null);
                    } else {
                        LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Command.Log.LOG, cache.id(), cache.name());
                    }

                    if (logType.equals(LogType.FLAG) && !pdo.getUniqueID().equals(cache.owner())) MinecachingAPI.get().createNotification(cache.owner(), pdo.getUniqueID(), NotificationType.FLAG, cache);

                    Log log = MCUtils.createLog(pdo.getUniqueID(), cache, logType, logMessage, isFTF);

                    pdo.setLocatingId("NULL");
                    pdo.setLogType(LogType.FOUND);
                    pdo.setLogMessage("");
                    pdo.setLogCode("");
                    return;
                } else {
                    LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.TOO_FAR);
                }
            } else {
                LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.INCORRECT_CODE);
                pdo.setLogCode("");
            }
        } else {
            LocalizedMessages.send(event.getPlayer(), MCMessageKeys.Error.Log.UNSUPPORTED);
        }

        pdo.removeFavorite(pdo.getLocatingId());

        event.setClose(true);
    }

    private static String getDefaultMessage(PlayerDataObject pdo) {
        return switch (pdo.getLogType()) {
            case FOUND -> MCMessageKeys.Menu.Log.MESSAGE_FOUND.translate();
            case DNF -> MCMessageKeys.Menu.Log.MESSAGE_DNF.translate();
            default -> MCMessageKeys.Menu.Log.MESSAGE_OTHER.translate();
        };
    }
}
