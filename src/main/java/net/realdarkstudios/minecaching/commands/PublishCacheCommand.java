package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.event.minecache.MinecachePublishedEvent;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PublishCacheCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.PUBLISH, label);

            return true;
        }

        String reason;

        if (args.length == 1) reason = "Published.";
        else {
            StringBuilder rsn = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                rsn.append(args[i]).append(" ");
            }
            reason = rsn.toString().trim();
        }

        Minecache cache = MinecachingAPI.get().getMinecache(args[0]);

        if (cache.equals(Minecache.EMPTY)) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE);
            return false;
        } else if (!(cache.status().equals(MinecacheStatus.REVIEWING) || cache.status().equals(MinecacheStatus.DISABLED))) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.PUBLISH_CANT_PUBLISH);
        } else {
            MinecachePublishedEvent event = new MinecachePublishedEvent(cache, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                LocalizedMessages.send(sender, MessageKeys.Error.Misc.PUBLISH);
                return true;
            }

            MinecachingAPI.get().publishMinecache(sender instanceof Player plr ? plr.getUniqueId() : MCUtils.EMPTY_UUID, cache, reason);
            LocalizedMessages.send(sender, MessageKeys.Command.Misc.PUBLISH, cache.id());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<Minecache> fMinecaches = MinecachingAPI.get().getFilteredCaches(m -> (m.status().equals(MinecacheStatus.REVIEWING) || m.status().equals(MinecacheStatus.DISABLED)) && m.id().contains(args.length > 0 ? args[0] : ""));
        ArrayList<String> mcIDs = new ArrayList<>();

        for (Minecache m : fMinecaches) {
            mcIDs.add(m.id());
        }

        return sender.isOp() && args.length == 1 ? mcIDs : List.of();
    }
}
