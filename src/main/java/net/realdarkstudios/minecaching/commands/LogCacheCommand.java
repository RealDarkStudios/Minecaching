package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.LogPickMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LogCacheCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!playerCheck(sender)) return true;
        Player plr = (Player) sender;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);
        if (pdo.getLocatingId().equals("NULL")) {
            LogPickMenu pickMenu = new LogPickMenu(Minecaching.getInstance(), plr);
            pickMenu.open(plr);
            return true;
        }
        Minecache cache = MinecachingAPI.get().getMinecache(pdo.getLocatingId());

        LogMenu menu = new LogMenu(cache, pdo);
        if (args.length > 0 && args[0].equals("message")) {
            if (args.length < 2) {
                LocalizedMessages.send(sender, MCMessageKeys.Error.Log.EMPTY_MESSAGE);
            } else {
                StringBuilder msg = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }
                pdo.setLogMessage(msg.toString().trim());
                menu.open(plr);
                menu.update(plr);
            }
        } else if (args.length > 0 && args[0].equals("code")) {
            if (args.length < 2) {
                LocalizedMessages.send(sender, MCMessageKeys.Error.Log.EMPTY_CODE);

            } else {
                String code = args[1];
                pdo.setLogCode(code);
                menu.open(plr);
                menu.update(plr);
            }
        } else menu.open(plr);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();

        if (MinecachingAPI.get().getPlayerData(plr).getLocatingId().equals("NULL")) return List.of();
        else return List.of("message", "code").stream().filter(s -> s.contains(args[0])).toList();
    }
}
