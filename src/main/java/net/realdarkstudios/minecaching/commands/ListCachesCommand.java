package net.realdarkstudios.minecaching.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.util.MCMessages;
import net.realdarkstudios.minecaching.util.TextComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCachesCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player plr) {
            CacheListMenu menu = new CacheListMenu("menu.list.title", Minecaching.getInstance(), plr);
            menu.open(plr);
            
            return true;
        }

        int page;
        try {
            page = args.length == 0 ? 0 : Math.max(Integer.parseInt(args[0]) - 1, 0);
        } catch (NumberFormatException e) {
            MCMessages.incorrectUsage(sender, "listcaches.page", args[0]);
            MCMessages.usage(sender, "listcaches", command, label);
            return true;
        }

        List<Minecache> caches = MinecachingAPI.get().getFilteredCaches(m -> !(sender instanceof Player plr) || m.world().equals(plr.getWorld()));
        int numCaches = caches.size();

        while (numCaches < page * 10 + 1) {
            page--;
        }

        TextComponent msg;
        if (numCaches != 0) {
            msg = TextComponentBuilder.fromTranslation("listcaches.page", (page * 10) + 1, Math.min((page + 1) * 10, numCaches), numCaches).build();

            for (int i = 0; i < 10; i++) {
                Minecache cache;
                try {
                    cache = caches.get(i);
                } catch (Exception e) {
                    cache = Minecache.EMPTY;
                }

                if (cache.equals(Minecache.EMPTY)) {
                    break;
                }

                ChatColor typeColor = cache.invalidated() ? MinecacheType.INVALID.getColor() : cache.type().getColor();
                ChatColor statusColor = cache.invalidated() ? MinecacheStatus.INVALID.getColor() : cache.status().getColor();
                ChatColor primaryColor = cache.status().equals(MinecacheStatus.PUBLISHED) || cache.status().equals(MinecacheStatus.REVIEWING) || cache.type().equals(MinecacheType.INVALID) ? typeColor : statusColor;
                TextComponent entry = TextComponentBuilder.fromTranslation("listcaches.entry", primaryColor, (page * 10) + i + 1, cache.invalidated() ? MinecacheType.INVALID : cache.type().getId().substring(0, 4).toUpperCase(), statusColor, cache.invalidated() ? MinecacheStatus.INVALID : cache.status(), primaryColor, cache.id(), cache.name(), cache.finds()).build();
                TextComponent findEntry = TextComponentBuilder.fromTranslation("listcaches.find", ChatColor.AQUA, ChatColor.UNDERLINE).clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/locate " + cache.id()).build();
                msg.addExtra(entry);
                if (!cache.status().equals(MinecacheStatus.INVALID) && !cache.type().equals(MinecacheType.INVALID)) msg.addExtra(findEntry);
            }
        } else {
            msg = TextComponentBuilder.fromTranslation("listcaches.nocaches", ChatColor.RED).build();
        }

        sender.spigot().sendMessage(msg);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
