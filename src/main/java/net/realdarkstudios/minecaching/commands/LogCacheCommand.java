package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.LogPickMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class LogCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            LocalizedMessages.send(sender, MessageKeys.Error.NON_CONSOLE_COMMAND);
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);
        if (pdo.getLocatingId().equals("NULL")) {
            LogPickMenu pickMenu = new LogPickMenu(Minecaching.getInstance(), plr);
            pickMenu.open(plr);
            return true;
        }
        Minecache cache = MinecachingAPI.get().getMinecache(pdo.getLocatingId());

        LogMenu menu = new LogMenu(cache, pdo, Minecaching.getInstance());
        if (args.length > 0 && args[0].equals("message")) {
            if (args.length < 2) {
                LocalizedMessages.send(sender, MessageKeys.Error.Log.EMPTY_MESSAGE);
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
                LocalizedMessages.send(sender, MessageKeys.Error.Log.EMPTY_CODE);

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
        else return List.of("message", "code");
    }
}
