package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogPreviewMenuItem extends MenuItem {
    public LogPreviewMenuItem(Minecache cache, PlayerDataObject pdo) {
        super(pdo.getLogType().getMenuMessageKey().translate(cache.id()), new ItemStack(Material.SPYGLASS),
                List.of(MessageKeys.Menu.Data.PREVIEW_MESSAGE.translate(
                        pdo.getLogMessage().isEmpty() ? getDefaultMessage(pdo) : pdo.getLogMessage())));
    }

    private static String getDefaultMessage(PlayerDataObject pdo) {
        return switch (pdo.getLogType()) {
            case FOUND -> MessageKeys.Menu.Log.MESSAGE_FOUND.translate();
            case DNF -> MessageKeys.Menu.Log.MESSAGE_DNF.translate();
            default -> MessageKeys.Menu.Log.MESSAGE_OTHER.translate();
        };
    }
}
