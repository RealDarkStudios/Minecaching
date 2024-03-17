package net.realdarkstudios.minecaching.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCachesCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (playerCheck(sender)) {
            Player plr = (Player) sender;

            CacheListMenu menu = new CacheListMenu(MessageKeys.Menu.List.TITLE, Minecaching.getInstance(), plr);
            menu.open(plr);
            
            return true;
        }

        int page;
        try {
            page = args.length == 0 ? 0 : Math.max(Integer.parseInt(args[0]) - 1, 0);
        } catch (NumberFormatException e) {
            LocalizedMessages.send(sender, MessageKeys.Error.FAILED_TO_PARSE_NUMBER);
            LocalizedMessages.send(sender, MessageKeys.Usage.LIST, label);
            return true;
        }

        // No need to check for the world since only the console will get here
        List<Minecache> caches = MinecachingAPI.get().getAllKnownCaches();
        int numCaches = caches.size();

        while (numCaches < page * 10 + 1) {
            page--;
        }

        TextComponent msg;
        if (numCaches != 0) {
            msg = MessageKeys.Command.List.PAGE.translateComponent((page * 10) + 1, Math.min((page + 1) * 10, numCaches), numCaches);

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
                msg.addExtra(MessageKeys.Command.List.ENTRY.translateComponentWithOtherStyle(new LocalizedMessages.StyleOptions().setColor(
                        primaryColor.asBungee()), (page * 10) + i + 1, cache.invalidated() ? MinecacheType.INVALID.toString() :
                                cache.type().getId().substring(0, 4).toUpperCase(), statusColor, cache.invalidated() ? MinecacheStatus.INVALID :
                                cache.status(), primaryColor, cache.id(), cache.name(), cache.finds()));
            }
        } else {
            msg = MessageKeys.Command.List.NO_CACHES.translateComponent();
        }

        sender.spigot().sendMessage(msg);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
