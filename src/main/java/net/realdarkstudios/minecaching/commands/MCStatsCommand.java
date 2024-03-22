package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.player.PlayerStorage;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.List;

public class MCStatsCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Stats
        List<PlayerDataObject> mostFinds = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFinds().size()));
        List<PlayerDataObject> mostHides = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(this::determineValidHides));
        List<PlayerDataObject> mostFTFs = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFTFs().size()));
        List<PlayerDataObject> mostAccomplished = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(Config.getInstance().getStatsScoreOptions()::calculateScore));
        List<Minecache> favoriteCaches = MinecachingAPI.get().getSortedCaches(Comparator.comparingInt(Minecache::favorites));
        List<Minecache> newestCaches = MinecachingAPI.get().getSortedCaches(Comparator.comparing(Minecache::hidden));
        Minecache newestCache = newestCaches.get(newestCaches.size() - 1);
        Minecache newestCache2 = newestCaches.get(newestCaches.size() - 2);
        Minecache newestCache3 = newestCaches.get(newestCaches.size() - 3);
        Minecache favoriteCache = favoriteCaches.get(favoriteCaches.size() - 1);
        PlayerDataObject mostFindsPDO = validateTopPDO(mostFinds);
        PlayerDataObject mostHidesPDO = validateTopPDO(mostHides);
        PlayerDataObject mostFTFsPDO = validateTopPDO(mostFTFs);
        PlayerDataObject mostAccomplishedPDO = validateTopPDO(mostAccomplished);

        LocalizedMessages.send(sender, MessageKeys.Command.Stats.HEADER);
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.CACHES, MinecachingAPI.get().getAllKnownCaches().size());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.PLAYERS, MinecachingAPI.get().getAllKnownPlayers().size());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.FINDS, PlayerStorage.getInstance().totalFinds());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.NEWEST_CACHES, newestCache.id(), newestCache2.id(), newestCache3.id());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.FAVORITE_CACHE, favoriteCache.id(), favoriteCache.favorites());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.MOST_FINDS, mostFindsPDO.getUsername(), mostFindsPDO.getFinds().size());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.MOST_FTFS, mostFTFsPDO.getUsername(), mostFTFsPDO.getFTFs().size());
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.MOST_HIDES, mostHidesPDO.getUsername(), determineValidHides(mostHidesPDO));
        LocalizedMessages.send(sender, MessageKeys.Command.Stats.MOST_ACCOMPLISHED,
                mostAccomplishedPDO.getUsername(), Config.getInstance().getStatsScoreOptions().calculateScore(mostAccomplishedPDO));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }

    private PlayerDataObject validateTopPDO(List<PlayerDataObject> pdos) {
        return pdos.get(pdos.size() - 1).getUniqueID().equals(MCUtils.EMPTY_UUID) ? pdos.get(pdos.size() - 2) : pdos.get(pdos.size() - 1);
    }

    private int determineValidHides(PlayerDataObject pdo) {
        int totalHides = 0;

        for (Minecache cache: MinecachingAPI.get().getFilteredCaches(c -> pdo.getHides().contains(c.id()))) {
            if (cache.status().equals(MinecacheStatus.PUBLISHED) || cache.status().equals(MinecacheStatus.NEEDS_MAINTENANCE)) totalHides++;
        }

        return totalHides;
    }
}
