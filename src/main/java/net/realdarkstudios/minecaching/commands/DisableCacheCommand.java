package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecacheArchivedEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DisableCacheCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MCMessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MCMessageKeys.Usage.DISABLE, label);
            return true;
        }

        Minecache cache = MinecachingAPI.get().getMinecache(args[0]);

        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.CANT_FIND_CACHE, cache.id());
            return false;
        } else if (cache.status().equals(MinecacheStatus.DISABLED) || cache.status().equals(MinecacheStatus.ARCHIVED) || cache.status().equals(MinecacheStatus.INVALID)) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.Misc.CANT_DISABLE);
        } else {
            MinecacheArchivedEvent event = new MinecacheArchivedEvent(cache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                LocalizedMessages.send(sender, MCMessageKeys.Error.Misc.DISABLE);
                return true;
            }

            String reason = MCMessageKeys.Command.Log.ARCHIVE_DEFAULT_MESSAGE.translate();
            if (args.length > 1) {
                StringBuilder rsn = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    rsn.append(args[i]).append(" ");
                }
                reason = rsn.toString().trim();
            }

            MinecachingAPI.get().disableMinecache(sender instanceof Player plr ? plr.getUniqueId() : MCUtils.EMPTY_UUID, cache, reason);
            LocalizedMessages.send(sender, MCMessageKeys.Command.Misc.DISABLE, cache.id());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> !(m.status().equals(MinecacheStatus.INVALID) || m.status().equals(MinecacheStatus.ARCHIVED) || m.status().equals(MinecacheStatus.DISABLED)));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m: fMinecaches) {
            if (m.id().contains(args[0]) && (sender.isOp() || (sender instanceof Player plr && m.owner().equals(plr.getUniqueId())))) {
                mcIDs.add(m.id());
            }
        }

        return args.length == 1 ? mcIDs : List.of();
    }
}
