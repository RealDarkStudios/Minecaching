package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MCAdminCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.ADMIN);
            return true;
        }

        if (!sender.hasPermission("minecaching.admin")) {
            LocalizedMessages.send(sender, MessageKeys.Permission.NO_PERMISSION_ADMIN);
            return true;
        }

        String subcommand = args[0];

        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("minecaching.admin.reload")) {
                LocalizedMessages.send(sender, MessageKeys.Permission.NO_PERMISSION_ADMIN_RELOAD);
                return true;
            }

            MinecachingAPI.tInfo(MessageKeys.Plugin.RELOADING);
            MinecachingAPI.get().load(!(args.length >= 2 && args[1].equalsIgnoreCase("false")));
            MinecachingAPI.tInfo(MessageKeys.Plugin.RELOADED);
            LocalizedMessages.send(sender, MessageKeys.Plugin.RELOADED);
        } else if (subcommand.equals("data")) {
            if (!sender.hasPermission("minecaching.admin.data")) {
                LocalizedMessages.send(sender, MessageKeys.Permission.NO_PERMISSION_ADMIN_DATA);
                return true;
            }

            int v = AutoUpdater.getLastCheckResult();

            // Version
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.OVERVIEW_HEADER);
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.PLUGIN_VERSION, Minecaching.getInstance().getDescription().getVersion(), Bukkit.getBukkitVersion().split("-")[0]);
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.CHECKING_VERSION, v == 0 ? "UP-TO-DATE" : v == 1 ? "AHEAD" : v == -1 ? "BEHIND" : "ERROR");
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.SERVER_LANGUAGE, MessageKeys.Misc.LOCALE_NAME);
            if (Config.getInstance().debugEvents()) LocalizedMessages.send(sender, MessageKeys.Command.Admin.DEBUG_EVENTS_ON, Config.getInstance().getDebugEventsLevel());
            else LocalizedMessages.send(sender, MessageKeys.Command.Admin.DEBUG_EVENTS_OFF);

            // Dev
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.DEV_HEADER);
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.CONFIG_VERSION, Config.getInstance().getConfigVersion());
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.MINECACHE_DATA_VERSION, Config.getInstance().getMinecacheDataVersion());
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.PLAYER_DATA_VERSION, Config.getInstance().getPlayerDataVersion());
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.LOGBOOK_DATA_VERSION, Config.getInstance().getLogbookDataVersion());

            if (v == -1) LocalizedMessages.send(sender, Config.getInstance().autoUpdate() ? MessageKeys.Plugin.Update.AVAILABE_AUTO : MessageKeys.Plugin.Update.AVAILABLE,
                    AutoUpdater.getNewestVersion());
        } else if (subcommand.equals("openmenu") && args.length > 1 && sender instanceof Player plr) {
            switch (args[1]) {
                case "create" -> {
                    MCMenus.get().getCreateCacheMenu(MinecachingAPI.get().getPlayerData(plr)).open(plr);
                }
                case "edit" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        MCMenus.get().getEditCacheMenu(MinecachingAPI.get().getPlayerData(plr), MinecachingAPI.get().getMinecache(args[2])).open(plr);
                    }
                }
                case "data" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        CacheDataMenu menu = new CacheDataMenu(MessageKeys.Menu.Data.TITLE, MinecachingAPI.get().getMinecache(args[2]), Minecaching.getInstance(), MinecachingAPI.get().getPlayerData(plr));
                        menu.open(plr);
                    }
                }
                case "log" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        LogMenu menu = new LogMenu(MinecachingAPI.get().getMinecache(args[2]), MinecachingAPI.get().getPlayerData(plr));
                        menu.open(plr);
                    }
                }
            }
        } else if (subcommand.equals("correctstats")) {
            MinecachingAPI.get().correctStats();
            LocalizedMessages.send(sender, MessageKeys.Command.Admin.CORRECTED_STATS);
        } else return false;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "data", "correctstats");
        } else return List.of();
    }
}
