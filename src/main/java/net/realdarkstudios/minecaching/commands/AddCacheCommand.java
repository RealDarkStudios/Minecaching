package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CreateCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AddCacheCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!playerCheck(sender)) return true;
        Player plr = (Player) sender;

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        CreateCacheMenu menu = MCMenus.get().getCreateCacheMenu(pdo);
        if (args.length == 0) {
            menu.open(plr);
        } else {
            String subCmd = args[0];

            switch (subCmd) {
                case "name" -> {
                    if (args.length < 2) {
                        LocalizedMessages.send(sender, MCMessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.CREATE_NAME, label);
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
                        LocalizedMessages.send(sender, MCMessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.CREATE_CODE, label);
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
        else return args.length == 1 ? List.of("name", "code").stream().filter(s -> s.contains(args[0])).toList() : List.of();
    }
}