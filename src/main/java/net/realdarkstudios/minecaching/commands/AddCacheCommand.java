package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CreateCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AddCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            MCMessages.sendErrorMsg(sender, "execute.console");
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        CreateCacheMenu menu = MCMenus.get().getAddCacheMenu(pdo);
        if (args.length == 0) {
            menu.open(plr);
        } else {
            String subCmd = args[0];

            switch (subCmd) {
                case "name" -> {
                    if (args.length < 2) {
                        MCMessages.incorrectUsage(sender);
                        MCMessages.usage(sender, "addcache.name", command, label);
                        return true;
                    } else {
                        StringBuilder name = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            name.append(args[i]).append(" ");
                        }
                        pdo.setCreatingCache(pdo.getCreatingCache().setName(name.toString().trim()));
                        menu.open(plr);
                        menu.update(plr);
                    }
                }
                case "code" -> {
                    if (args.length < 2) {
                        MCMessages.incorrectUsage(sender);
                        MCMessages.usage(sender, "addcache.code", command, label);
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
        if (!(sender instanceof Player plr)) {
            return new ArrayList<>();
        }

        Block target = plr.getTargetBlock(null, 5);
        PlayerDataObject plrdata = MinecachingAPI.get().getPlayerData(plr);

        return switch (args.length) {
            case 1 -> plrdata.getCreatingCache() == null || plrdata.getCreatingCache().id().equals("NULL") ? List.of() : Stream.of("cancel", "code", "name", "lodecoords", "coords", "save", "data", "type").filter(s -> s.contains(args[0])).toList();
            case 2 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", "~ ~ ~", target.getX() + "", String.format("%d %d %d", target.getX(), target.getY(), target.getZ())) : args[0].equals("type") ? List.of("Traditional", "Multi", "Mystery") : List.of();
            case 3 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", "~ ~", target.getY() + "", String.format("%d %d", target.getY(), target.getZ())) : List.of();
            case 4 -> args[0].equals("lodecoords") || args[0].equals("coords") ? List.of("~", target.getZ() + "") : List.of();
            default -> List.of();
        };
    }


}
