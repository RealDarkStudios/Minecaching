package net.realdarkstudios.minecaching.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStatus;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import net.realdarkstudios.minecaching.data.MinecacheType;
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
    //caches [page]
    //10 a page
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int page;
        try {
            page = args.length == 0 ? 0 : Math.max(Integer.parseInt(args[0]) - 1, 0);
        } catch (NumberFormatException e) {
            sender.sendMessage(String.format("Incorrect Usage (Expected a number, but got '%s'!)", args[0]));
            return false;
        }

        MinecacheStorage storage = MinecacheStorage.getInstance();
        List<Minecache> caches = storage.getMinecaches().stream().filter(m -> !(sender instanceof Player plr) || m.world().equals(plr.getWorld())).toList();
        int numCaches = caches.size();

        while (numCaches < page * 10 + 1) {
            page--;
        }

        TextComponent msg;
        if (numCaches != 0) {
            msg = new TextComponentBuilder(String.format("Showing %d - %d of %d\n", (page * 10) + 1, Math.min((page + 1) * 10, numCaches), numCaches)).build();

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
                ChatColor primaryColor = cache.status().equals(MinecacheStatus.ACTIVE) || cache.status().equals(MinecacheStatus.NEEDS_REVIEWED) || cache.type().equals(MinecacheType.INVALID) ? typeColor : statusColor;
                TextComponent entry = new TextComponentBuilder(String.format("%s%d. [%s] [%s%s%s] %s: \"%s\" (Finds: %s) ", primaryColor, (page * 10) + i + 1, cache.invalidated() ? MinecacheType.INVALID : cache.type().toString(), statusColor, cache.invalidated() ? MinecacheStatus.INVALID : cache.status(), primaryColor, cache.id(), cache.name(), cache.finds())).build();
                TextComponent findEntry = new TextComponentBuilder("Find\n").color(ChatColor.AQUA.asBungee()).underline().clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/findcache " + cache.id()).build();
                msg.addExtra(entry);
                if (!cache.status().equals(MinecacheStatus.INVALID) && !cache.type().equals(MinecacheType.INVALID)) msg.addExtra(findEntry);
                else msg.addExtra("\n");
            }
        } else {
            msg = new TextComponentBuilder("No caches to show!").color(ChatColor.RED.asBungee()).build();
        }

        sender.spigot().sendMessage(msg);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
