package net.realdarkstudios.minecaching.api.menu;

import net.md_5.bungee.api.ChatColor;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.item.log.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class LogMenu extends MCMenu {
    private final Minecache cache;
    private final PlayerDataObject author;

    public LogMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super(MessageKeys.Menu.Log.TITLE, MenuSize.THREE_ROW, plugin, cache.id());
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
                new MenuItemState("found", MessageKeys.Menu.Log.TYPE_FOUND.translate(cache.id()), new ItemStack(Material.YELLOW_TERRACOTTA), List.of()),
                new MenuItemState("dnf", MessageKeys.Menu.Log.TYPE_DNF.translate(cache.id()), new ItemStack(Material.BLUE_TERRACOTTA), List.of()),
                new MenuItemState("note", MessageKeys.Menu.Log.TYPE_NOTE.translate(cache.id()), new ItemStack(Material.CYAN_TERRACOTTA), List.of()),
                new MenuItemState("flag", MessageKeys.Menu.Log.TYPE_FLAG.translate(cache.id()), new ItemStack(Material.RED_TERRACOTTA), List.of())
        ).fromId(author.getLogType().getId()));
        setItem(22, author.getLogType().equals(LogType.FOUND) ? new LogCodeMenuItem(
                MessageKeys.Menu.Log.ITEM_CODE.translate(code ? author.getLogCode(): "???"),
                new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()) : EMPTY_SLOT_ITEM);
        setItem(24, new LogMessageMenuItem(
                customMessage ? MessageKeys.Menu.Log.ITEM_CUSTOM_MESSAGE.translate(author.getLogMessage().length()) : MessageKeys.Menu.Log.ITEM_MESSAGE.translate(),
                new ItemStack(customMessage ? Material.GREEN_WOOL : typeDNF || typeFound ? Material.YELLOW_WOOL: Material.RED_WOOL),
                List.of(customMessage ? author.getLogMessage() : getDefaultMessage())));
        setItem(26, new LogCreateMenuItem(MessageKeys.Menu.SAVE.translateWithOtherStyle(
                ready ? MessageKeys.Menu.SAVE.styleOptions() : new LocalizedMessages.StyleOptions().setColor(ChatColor.RED)),
                new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK),
                List.of(MessageKeys.Menu.SAVE_LORE.translate())));

        super.update(player);
    }

    private String getDefaultMessage() {
        return switch (author.getLogType()) {
            case FOUND -> MessageKeys.Menu.Log.MESSAGE_FOUND.translate();
            case DNF -> MessageKeys.Menu.Log.MESSAGE_DNF.translate();
            default -> MessageKeys.Menu.Log.MESSAGE_OTHER.translate();
        };
    }
}
