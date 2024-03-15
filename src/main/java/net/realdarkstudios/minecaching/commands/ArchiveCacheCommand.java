package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheArchivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArchiveCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.ARCHIVE, label);
            return true;
        }

        String reason = MessageKeys.Command.Log.ARCHIVE_DEFAULT_MESSAGE.translate();
        if (args.length > 1) {
            StringBuilder rsn = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                rsn.append(args[i]).append(" ");
            }
            reason = rsn.toString().trim();
        }

        Minecache cache = MinecachingAPI.get().getMinecache(args[0]);

        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, cache.id());
            return false;
        } else if (cache.status().equals(MinecacheStatus.ARCHIVED) || cache.status().equals(MinecacheStatus.INVALID)) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.ARCHIVE_CANT_ARCHIVE);
        } else {
            MinecacheArchivedEvent event = new MinecacheArchivedEvent(cache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                LocalizedMessages.send(sender, MessageKeys.Error.Misc.ARCHIVE);
                return true;
            }

            MinecachingAPI.get().archiveMinecache(sender instanceof Player plr ? plr.getUniqueId() : MCUtils.EMPTY_UUID, cache, reason);
            LocalizedMessages.send(sender, MessageKeys.Command.Misc.ARCHIVE, cache.id());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> !(m.status().equals(MinecacheStatus.INVALID) || m.status().equals(MinecacheStatus.ARCHIVED)));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m: fMinecaches) {
            if (m.id().contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && m.owner().equals(plr.getUniqueId())))) {
                mcIDs.add(m.id());
            }
        }

        return args.length == 1 ? mcIDs : List.of();
    }
}
