package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.Config;
import net.realdarkstudios.minecaching.api.LocalizationProvider;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MCAdminCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MCPluginMessages.incorrectUsage(sender);
            MCPluginMessages.usage(sender, "mcadmin", command, label);
            return true;
        }

        if (!sender.hasPermission("minecaching.admin")) {
            MCPluginMessages.noPermission(sender,"mcadmin");
            return true;
        }

        String subcommand = args[0];

        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("minecaching.admin.reload")) {
                MCPluginMessages.noPermission(sender, "mcadmin.reload");
                return true;
            }

            MinecachingAPI.tInfo("plugin.reloading");
            MinecachingAPI.get().load(!(args.length >= 2 && args[1].equalsIgnoreCase("false")));
            MinecachingAPI.tInfo("plugin.reloaded");
            MCPluginMessages.sendMsg(sender, "plugin.reloaded");
        } else if (subcommand.equals("version")) {
            if (!sender.hasPermission("minecaching.admin.version")) {
                MCPluginMessages.noPermission(sender, "admin.version");
                return true;
            }

            MCPluginMessages.sendMsg(sender, "mcadmin.version.mcversion",Minecaching.getInstance().getDescription().getVersion(), Bukkit.getBukkitVersion().split("-")[0]);
            MCPluginMessages.sendMsg(sender, "mcadmin.version.serverlang", LocalizationProvider.getInstance().getTranslation("locale.name"));
            MCPluginMessages.sendMsg(sender, "mcadmin.version.configversion", Config.getInstance().getConfigVersion());
            MCPluginMessages.sendMsg(sender, "mcadmin.version.mcdataversion", Config.getInstance().getMinecacheDataVersion());
            MCPluginMessages.sendMsg(sender, "mcadmin.version.plrdataversion", Config.getInstance().getPlayerDataVersion());
            MCPluginMessages.sendMsg(sender, "mcadmin.version.logbookdataversion", Config.getInstance().getLogbookDataVersion());
            if (Config.getInstance().getDebugEvents()) MCPluginMessages.sendMsg(sender, "mcadmin.version.debugevents.on", Config.getInstance().getDebugEventsLevel());
            else MCPluginMessages.sendMsg(sender, "mcadmin.version.debugevents.off");
        } else return false;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "version");
        } else return List.of();
    }
}
