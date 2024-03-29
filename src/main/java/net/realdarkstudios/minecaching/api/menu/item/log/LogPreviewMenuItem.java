package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.commons.menu.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogPreviewMenuItem extends MenuItem {
    public LogPreviewMenuItem(Minecache cache, PlayerDataObject pdo) {
        super(pdo.getLogType().getMenuMessageKey().translate(cache.id()), new ItemStack(Material.SPYGLASS),
                List.of(MCMessageKeys.Menu.Data.PREVIEW_MESSAGE.translate(
                        pdo.getLogMessage().isEmpty() ? getDefaultMessage(pdo) : pdo.getLogMessage())));
    }

    private static String getDefaultMessage(PlayerDataObject pdo) {
        return switch (pdo.getLogType()) {
            case FOUND -> MCMessageKeys.Menu.Log.MESSAGE_FOUND.translate();
            case DNF -> MCMessageKeys.Menu.Log.MESSAGE_DNF.translate();
            default -> MCMessageKeys.Menu.Log.MESSAGE_OTHER.translate();
        };
    }
}
