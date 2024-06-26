package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.LogbookGenerator;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LogbookCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!playerCheck(sender)) return true;
        Player plr = (Player) sender;

        if (args.length < 1) {
            LocalizedMessages.send(sender, MCMessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MCMessageKeys.Usage.LOGBOOK, label);

            return true;
        }

        int bookNum;
        String id = args[0];

        try {
            bookNum = Integer.parseInt(args[1]);
        } catch (Exception e) {
            bookNum = 1;
        }

        if (plr.getInventory().firstEmpty() == -1) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.Misc.NO_SLOTS);
        } else plr.getInventory().addItem(new LogbookGenerator(MinecachingAPI.get().getMinecache(id)).getLogbook(bookNum));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return switch (args.length) {
            case 0 -> MinecachingAPI.get().getAllKnownCacheIDs();
            case 1 -> MinecachingAPI.get().getFilteredCacheIDs(s -> s.contains(args[0]));
            default -> List.of();
        };
    }
}
