package net.realdarkstudios.minecaching.api.menu.item.log;

import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogPreviewMenuItem extends MenuItem {
    public LogPreviewMenuItem(Minecache cache, PlayerDataObject pdo) {
        super(pdo.getLogType().getMenuMessage() + " " + cache.id(), new ItemStack(Material.SPYGLASS),
                List.of(translation("menu.data.item.preview.message", pdo.getLogMessage().isEmpty() ? translation("menu.log.message." + pdo.getLogType().getId()) : pdo.getLogMessage())));
    }
}
