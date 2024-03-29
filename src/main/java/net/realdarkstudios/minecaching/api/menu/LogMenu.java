package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.commons.menu.MCMenu;
import net.realdarkstudios.commons.menu.item.MenuItemState;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.menu.item.log.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LogMenu extends MCMenu {
    private final Minecache cache;
    private final PlayerDataObject author;

    public LogMenu(Minecache cache, PlayerDataObject author) {
        super(MCMessageKeys.Menu.Log.TITLE, MenuSize.THREE_ROW, Minecaching.getInstance(), cache.id());
        this.cache = cache;
        this.author = author;

        update(author.getPlayer());
    }

    @Override
    public void update(Player player) {
        clearAllSlots();
        fillEmptySlots();

        boolean typeFound = author.getLogType().equals(LogType.FOUND);
        boolean typeDNF = author.getLogType().equals(LogType.DNF);
        boolean typeNote = author.getLogType().equals(LogType.NOTE);
        boolean typeFlagged = author.getLogType().equals(LogType.FLAG);
        boolean typeInvalid = author.getLogType().equals(LogType.INVALID);
        boolean customMessage = stringCheck(author.getLogMessage());
        boolean code = stringCheck(author.getLogCode());
        boolean ready = !(typeFound || typeInvalid || typeFlagged || typeNote) || (typeFound && code) || ((typeFlagged || typeNote) && customMessage);

        setItem(4, new LogPreviewMenuItem(cache, author));
        setItem(8, new LogCancelMenuItem());
        setItem(18, new FavoriteCacheItem(cache, author));
        setItem(20, new LogTypeMenuItem(
                new MenuItemState("found", MCMessageKeys.Menu.Log.TYPE_FOUND.translate(cache.id()), new ItemStack(Material.YELLOW_TERRACOTTA), List.of()),
                new MenuItemState("dnf", MCMessageKeys.Menu.Log.TYPE_DNF.translate(cache.id()), new ItemStack(Material.BLUE_TERRACOTTA), List.of()),
                new MenuItemState("note", MCMessageKeys.Menu.Log.TYPE_NOTE.translate(cache.id()), new ItemStack(Material.CYAN_TERRACOTTA), List.of()),
                new MenuItemState("flag", MCMessageKeys.Menu.Log.TYPE_FLAG.translate(cache.id()), new ItemStack(Material.RED_TERRACOTTA), List.of())
        ).fromId(author.getLogType().getId()));
        setItem(22, author.getLogType().equals(LogType.FOUND) ? new LogCodeMenuItem(
                MCMessageKeys.Menu.Log.ITEM_CODE.translate(code ? author.getLogCode(): "???"),
                new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()) : EMPTY_SLOT_ITEM);
        setItem(24, new LogMessageMenuItem(
                customMessage ? MCMessageKeys.Menu.Log.ITEM_CUSTOM_MESSAGE.translate(author.getLogMessage().length()) : MCMessageKeys.Menu.Log.ITEM_MESSAGE.translate(),
                new ItemStack(customMessage ? Material.GREEN_WOOL : typeDNF || typeFound ? Material.YELLOW_WOOL: Material.RED_WOOL),
                List.of(customMessage ? author.getLogMessage() : getDefaultMessage())));
        setItem(26, new LogCreateMenuItem(MCMessageKeys.Menu.SAVE.translateWithOtherStyle(
                ready ? MCMessageKeys.Menu.SAVE.styleOptions() : new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK),
                List.of(MCMessageKeys.Menu.SAVE_LORE.translate())));

        super.update(player);
    }

    private String getDefaultMessage() {
        return switch (author.getLogType()) {
            case FOUND -> MCMessageKeys.Menu.Log.MESSAGE_FOUND.translate();
            case DNF -> MCMessageKeys.Menu.Log.MESSAGE_DNF.translate();
            default -> MCMessageKeys.Menu.Log.MESSAGE_OTHER.translate();
        };
    }
}
