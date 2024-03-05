package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CreateCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
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

public class AddCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            LocalizedMessages.send(sender, MessageKeys.Error.NON_CONSOLE_COMMAND);
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        CreateCacheMenu menu = MCMenus.get().getCreateCacheMenu(pdo);
        if (args.length == 0) {
            menu.open(plr);
        } else {
            String subCmd = args[0];

            switch (subCmd) {
                case "name" -> {
                    if (args.length < 2) {
                        LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MessageKeys.Usage.CREATE_NAME, label);
                        return true;
                    } else {
                        StringBuilder name = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            name.append(args[i]).append(" ");
                        }
                        pdo.setCreatingCache(pdo.getCreatingCache().setName(name.toString().replace("&", "§").trim()));
                        menu.open(plr);
                        menu.update(plr);
                    }
                }
                case "code" -> {
                    if (args.length < 2) {
                        LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MessageKeys.Usage.CREATE_CODE, label);
                        return true;
                    } else {
                        pdo.setCreatingCache(pdo.getCreatingCache().setCode(args[1].trim()));
                        menu.open(plr);
                        menu.update(plr);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();

        if (MinecachingAPI.get().getPlayerData(plr).getCreatingCache().equals(Minecache.EMPTY)) return List.of();
        else return List.of("name", "code");
    }
}