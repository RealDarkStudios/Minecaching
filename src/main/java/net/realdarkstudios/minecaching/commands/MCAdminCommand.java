package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MCAdminCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "mcadmin", command, label);
            return true;
        }

        if (!sender.hasPermission("minecaching.admin")) {
            MCMessages.noPermission(sender,"mcadmin");
            return true;
        }

        String subcommand = args[0];

        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("minecaching.admin.reload")) {
                MCMessages.noPermission(sender, "mcadmin.reload");
                return true;
            }

            MinecachingAPI.tInfo("plugin.reloading");
            MinecachingAPI.get().load(!(args.length >= 2 && args[1].equalsIgnoreCase("false")));
            MinecachingAPI.tInfo("plugin.reloaded");
            MCMessages.sendMsg(sender, "plugin.reloaded");
        } else if (subcommand.equals("data")) {
            if (!sender.hasPermission("minecaching.admin.data")) {
                MCMessages.noPermission(sender, "mcadmin.data");
                return true;
            }

            int v = AutoUpdater.getLastCheckResult();

            // Version
            MCMessages.sendMsg(sender, "mcadmin.data.verheader");
            MCMessages.sendMsg(sender, "mcadmin.data.mcversion", Minecaching.getInstance().getDescription().getVersion(), Bukkit.getBukkitVersion().split("-")[0]);
            MCMessages.sendMsg(sender, "mcadmin.data.checkingversion", v == 0 ? "UP-TO-DATE" : v == 1 ? "AHEAD" : v == -1 ? "BEHIND" : "ERROR");
            MCMessages.sendMsg(sender, "mcadmin.data.serverlang", MinecachingAPI.getLocalization().getTranslation("locale.name"));
            if (Config.getInstance().debugEvents()) MCMessages.sendMsg(sender, "mcadmin.data.debugevents.on", Config.getInstance().getDebugEventsLevel());
            else MCMessages.sendMsg(sender, "mcadmin.data.debugevents.off");

            // Dev
            MCMessages.sendMsg(sender, "mcadmin.data.devheader");
            MCMessages.sendMsg(sender, "mcadmin.data.configversion", Config.getInstance().getConfigVersion());
            MCMessages.sendMsg(sender, "mcadmin.data.mcdataversion", Config.getInstance().getMinecacheDataVersion());
            MCMessages.sendMsg(sender, "mcadmin.data.plrdataversion", Config.getInstance().getPlayerDataVersion());
            MCMessages.sendMsg(sender, "mcadmin.data.logbookdataversion", Config.getInstance().getLogbookDataVersion());

            if (v == -1) MCMessages.sendMsg(sender, Config.getInstance().autoUpdate() ? "plugin.update.auto" : "plugin.update", ChatColor.RED, AutoUpdater.getNewestVersion());
        } else return false;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "data");
        } else return List.of();
    }
}
