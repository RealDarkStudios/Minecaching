package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.player.PlayerStorage;
import net.realdarkstudios.minecaching.util.MCMessages;
import net.realdarkstudios.minecaching.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Comparator;
import java.util.List;

public class MCStatsCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Stats
        List<PlayerDataObject> mostFinds = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFinds().size()));
        List<PlayerDataObject> mostHides = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getHides().size()));
        List<PlayerDataObject> mostFTFs = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFTFs().size()));
        List<PlayerDataObject> mostAccomplished = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFinds().size() + 2 * p.getHides().size() + 3 * p.getFTFs().size()));
        List<Minecache> newestCaches = MinecachingAPI.get().getSortedCaches(Comparator.comparing(Minecache::hidden));
        Minecache newestCache = newestCaches.get(newestCaches.size() - 1);
        Minecache newestCache2 = newestCaches.get(newestCaches.size() - 2);
        Minecache newestCache3 = newestCaches.get(newestCaches.size() - 3);
        PlayerDataObject mostFindsPDO = validateTopPDO(mostFinds);
        PlayerDataObject mostHidesPDO = validateTopPDO(mostHides);
        PlayerDataObject mostFTFsPDO = validateTopPDO(mostFTFs);
        PlayerDataObject mostAccomplishedPDO = validateTopPDO(mostAccomplished);

        MCMessages.sendMsg(sender, "mcstats.statsheader");
        MCMessages.sendMsg(sender, "mcstats.caches", MinecachingAPI.get().getAllKnownCaches().size());
        MCMessages.sendMsg(sender, "mcstats.players", MinecachingAPI.get().getAllKnownPlayers().size());
        MCMessages.sendMsg(sender, "mcstats.finds", PlayerStorage.getInstance().totalFinds());
        MCMessages.sendMsg(sender, "mcstats.newestcaches", newestCache.id(), newestCache2.id(), newestCache3.id());
        MCMessages.sendMsg(sender, "mcstats.mostfinds", mostFindsPDO.getUsername(), mostFindsPDO.getFinds().size());
        MCMessages.sendMsg(sender, "mcstats.mostftfs", mostFTFsPDO.getUsername(), mostFTFsPDO.getFTFs().size());
        MCMessages.sendMsg(sender, "mcstats.mosthides", mostHidesPDO.getUsername(), mostHidesPDO.getHides().size());
        MCMessages.sendMsg(sender, "mcstats.accomplished", mostAccomplishedPDO.getUsername(), mostAccomplishedPDO.getFinds().size() + 2 * mostAccomplishedPDO.getHides().size() + 3 * mostAccomplishedPDO.getFTFs().size());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }

    private PlayerDataObject validateTopPDO(List<PlayerDataObject> pdos) {
        return pdos.get(pdos.size() - 1).getUniqueID().equals(Utils.EMPTY_UUID) ? pdos.get(pdos.size() - 2) : pdos.get(pdos.size() - 1);
    }
}
