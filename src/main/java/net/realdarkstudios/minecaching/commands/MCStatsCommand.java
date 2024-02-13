package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.player.PlayerStorage;
import net.realdarkstudios.minecaching.util.MCMessages;
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
        int totalFinds = PlayerStorage.getInstance().totalFinds();
        PlayerDataObject mostFindsPDO = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getFinds().size())).get(MinecachingAPI.get().getAllKnownPlayers().size() - 1);
        PlayerDataObject mostHidesPDO = MinecachingAPI.get().getSortedPlayers(Comparator.comparingInt(p -> p.getHides().size())).get(MinecachingAPI.get().getAllKnownPlayers().size() - 1);

        MCMessages.sendMsg(sender, "mcstats.statsheader");
        MCMessages.sendMsg(sender, "mcstats.caches", MinecachingAPI.get().getAllKnownCaches().size());
        MCMessages.sendMsg(sender, "mcstats.players", MinecachingAPI.get().getAllKnownPlayers().size());
        MCMessages.sendMsg(sender, "mcstats.finds", totalFinds);
        MCMessages.sendMsg(sender, "mcstats.mostfinds", mostFindsPDO.getPlayer().getName(), mostFindsPDO.getFinds().size());
        MCMessages.sendMsg(sender, "mcstats.mosthides", mostHidesPDO.getPlayer().getName(), mostHidesPDO.getHides().size());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
