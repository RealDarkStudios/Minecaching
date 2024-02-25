package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItemState;
import net.realdarkstudios.minecaching.api.menu.item.log.*;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class LogMenu extends MCMenu {
    private final Minecache cache;
    private final PlayerDataObject author;

    public LogMenu(Minecache cache, PlayerDataObject author, JavaPlugin plugin) {
        super("menu.log.title", MenuSize.THREE_ROW, plugin, cache.id());
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
        setItem(8, new LogCancelMenuItem(translation("menu.cancel"), new ItemStack(Material.RED_CONCRETE), List.of()));
        setItem(20, new LogTypeMenuItem(
                new MenuItemState("found", ChatColor.YELLOW + typeTranslation("found"), new ItemStack(Material.YELLOW_TERRACOTTA), List.of()),
                new MenuItemState("dnf", ChatColor.BLUE + typeTranslation("dnf"), new ItemStack(Material.BLUE_TERRACOTTA), List.of()),
                new MenuItemState("note", ChatColor.GRAY + typeTranslation("note"), new ItemStack(Material.CYAN_TERRACOTTA), List.of()),
                new MenuItemState("flag", ChatColor.RED + typeTranslation("flag"), new ItemStack(Material.RED_TERRACOTTA), List.of())
        ).fromId(author.getLogType().getId()));
        setItem(22, author.getLogType().equals(LogType.FOUND) ? new LogCodeMenuItem(itemTranslation("code", code ? author.getLogCode(): "???"),
                new ItemStack(code ? Material.GREEN_WOOL : Material.RED_WOOL), List.of()) : EMPTY_SLOT_ITEM);
        setItem(24, new LogMessageMenuItem(itemTranslation(customMessage ? "message.custom" : "message", author.getLogMessage().length()),
                new ItemStack(customMessage ? Material.GREEN_WOOL : typeDNF || typeFound ? Material.YELLOW_WOOL: Material.RED_WOOL),
                List.of(customMessage ? author.getLogMessage() : translation("menu.log.message." + author.getLogType().getId()))));
        setItem(26, new LogCreateMenuItem(ready ? ChatColor.GREEN + translation("menu.save") : ChatColor.RED + translation("menu.save"),
                new ItemStack(ready ? Material.LIME_CONCRETE : Material.BEDROCK), List.of(translation("menu.save.lore"))));

        super.update(player);
    }

    private String typeTranslation(String id) {
        return translation("menu.log.type." + id);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return translation("menu.log.item." + id, substitutions);
    }
}
