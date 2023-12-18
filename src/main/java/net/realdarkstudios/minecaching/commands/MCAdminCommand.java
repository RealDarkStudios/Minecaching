package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.data.Config;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import net.realdarkstudios.minecaching.data.PlayerStorage;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MCAdminCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.spigot().sendMessage(MCPluginMessages.INCORRECT_USAGE);

            return false;
        }

        if (!sender.hasPermission("minecaching.admin")) {
            sender.spigot().sendMessage(MCPluginMessages.NO_PERMISSION);
            return true;
        }

        String subcommand = args[0];

        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("minecaching.admin.reload")) {
                sender.spigot().sendMessage(MCPluginMessages.NO_PERMISSION);
                return true;
            }

            Minecaching.getInstance().getLogger().info("Reloading...");
            Config.getInstance().load();
            PlayerStorage.getInstance().load();
            MinecacheStorage.getInstance().load();
            Minecaching.getInstance().getLogger().info("Reloaded");
            sender.sendMessage("Reloaded Minecaching");
        } else if (subcommand.equals("version")) {
            sender.sendMessage("Minecaching Version: " + Minecaching.getInstance().getDescription().getVersion());
            sender.sendMessage("Config Version: " + Config.getInstance().getConfigVersion());
            sender.sendMessage("Minecache Data Version: " + Config.getInstance().getMinecacheVersion());
            sender.sendMessage("Player Data Version: " + Config.getInstance().getPlayerVersion());
        } else return false;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return (sender instanceof Player plr) && plr.getUniqueId() == UUID.fromString("cc7f192c-e46b-49ad-86f7-f95eb2cf8b83") ? List.of("version", "reload", "getBlockData", "getBlockState") : List.of("reload", "version");
        } else return List.of();
    }
}
