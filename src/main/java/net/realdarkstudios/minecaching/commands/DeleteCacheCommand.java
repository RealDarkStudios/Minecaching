package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheDeletedEvent;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCacheCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.DELETE, label);
            return true;
        }

        String id = args[0].trim();

        if (id.equals("openmenu")) {
            if (sender instanceof Player plr && args.length >= 2) {
                CacheDataMenu menu = new CacheDataMenu(MessageKeys.Menu.Data.TITLE, MinecachingAPI.get().getMinecache(args[1]),
                        Minecaching.getInstance(), MinecachingAPI.get().getPlayerData(plr));
                menu.open(plr);
            }
            return true;
        }

        Minecache cache = MinecachingAPI.get().getMinecache(id);
        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, cache.id());
            return true;
        }

        if ((sender instanceof Player plr && !plr.getUniqueId().equals(cache.owner())) && !sender.isOp()) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.DELETE_OTHERS);
            return true;
        }

        MinecacheDeletedEvent event = new MinecacheDeletedEvent(cache, Bukkit.getOfflinePlayer(cache.owner()));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.DELETE);
            return true;
        }

        MinecachingAPI.get().deleteMinecache(cache, sender instanceof Player plr ? plr.getUniqueId() : MCUtils.EMPTY_UUID);
        LocalizedMessages.send(sender, MessageKeys.Command.Misc.DELETE, cache.id());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return MinecachingAPI.get().getAllKnownCacheIDs();
        else if (args.length > 1) return List.of();

        ArrayList<String> toReturn = new ArrayList<>();

        for (String id: MinecachingAPI.get().getAllKnownCacheIDs()) {
            if (id.contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && MinecachingAPI.get().getMinecache(id).owner().equals(plr.getUniqueId())))) {
                toReturn.add(id);
            }
        }

        return toReturn;
    }
}
