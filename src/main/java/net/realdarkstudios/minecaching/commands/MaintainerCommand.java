package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MaintainerCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_ARG_COUNT);
            LocalizedMessages.send(sender, MessageKeys.Usage.MAINTAINER, label);
            return true;
        }

        String cacheID = args[0];
        Minecache cache = MinecachingAPI.get().getMinecache(cacheID);

        if (cacheCheck(sender, cache, cacheID)) return true;

        if (sender instanceof Player plr) {
            CacheDataMenu menu = new CacheDataMenu(MessageKeys.Menu.Data.TITLE, cache, Minecaching.getInstance(), MinecachingAPI.get().getPlayerData(plr));
            menu.open(plr);
            return true;
        }

        // this part exists for console support

        PlayerDataObject pdo;
        try {
            pdo = MinecachingAPI.get().getPlayerData(UUID.fromString(args[1]));
        } catch (Exception e3141592) {
            try {
                pdo = MinecachingAPI.get().getFilteredPlayers(p -> p.getUsername().equals(args[1])).get(0);
            } catch (Exception e6535897) {
                LocalizedMessages.send(sender, MessageKeys.Error.Misc.MAINTAINER_NOT_FOUND, args[1]);
                return true;
            }
        }

        MinecachingAPI.get().saveMinecache(cache.setMaintaner(pdo.getUniqueID()), false);
        LocalizedMessages.send(sender, MessageKeys.Menu.Data.SET_MAINTAINER, cache.id(), pdo.getUsername());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
