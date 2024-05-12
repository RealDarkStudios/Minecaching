package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HintCommand extends MCCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!playerCheck(sender)) return true;

        Player plr = (Player) sender;
        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);
        Minecache locating = pdo.getLocatingCache();

        if (!cacheCheckNoAlert(locating)) {
            send(sender, MCMessageKeys.Error.Misc.NOT_LOCATING);
            return true;
        }

        if (locating.hint() == null || locating.hint().equals(MCMessageKeys.Misc.Cache.NO_HINT.getRawMessage()) || locating.hint().isEmpty())
            send(sender, MCMessageKeys.Misc.Cache.NO_HINT);
        else send(sender, MCMessageKeys.Command.Misc.HINT, locating.hint());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
