package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.api.event.MCDebugEventHandler;
import net.realdarkstudios.minecaching.api.event.MCEventHandler;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenuHolder;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minecaching extends JavaPlugin {
    private static Minecaching plugin;
    private static AutoUpdater.Version version;
    private static MinecachingAPI api;

    @Override
    public void onEnable() {
        plugin = this;
        version = AutoUpdater.Version.fromString(getDescription().getVersion());
        api = new MinecachingAPI();

        api.init();

        // Server version check
        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            api.load(false);
            MinecachingAPI.tWarning(MessageKeys.Plugin.VERSION_WARNING);
            onDisable();
        } else {
            // Load plugin data
            api.load(true);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                MinecachingAPI.getUpdater().updateBranch(Config.getInstance().getUpdateBranch());
                MinecachingAPI.getUpdater().checkForUpdate();
            }, 0L,36000L);

            // Register commands
            MinecachingAPI.tInfo(MessageKeys.Plugin.REGISTERING_COMMANDS);
            getCommand("addcache").setExecutor(new AddCacheCommand());
            getCommand("archivecache").setExecutor(new ArchiveCacheCommand());
            getCommand("disablecache").setExecutor(new DisableCacheCommand());
            getCommand("editcache").setExecutor(new EditCacheCommand());
            getCommand("listcaches").setExecutor(new ListCachesCommand());
            getCommand("delcache").setExecutor(new DeleteCacheCommand());
            getCommand("mcadmin").setExecutor(new MCAdminCommand());
            getCommand("mcstats").setExecutor(new MCStatsCommand());
            getCommand("publishcache").setExecutor(new PublishCacheCommand());
            getCommand("locatecache").setExecutor(new LocateCacheCommand());
            getCommand("logcache").setExecutor(new LogCacheCommand());
            getCommand("logbook").setExecutor(new LogbookCommand());
            getCommand("maintainer").setExecutor(new MaintainerCommand());

            // Debug Events check
            if (Config.getInstance().debugEvents()) MinecachingAPI.tInfo(MessageKeys.Command.Admin.DEBUG_EVENTS_ON, Config.getInstance().getDebugEventsLevel());
            else MinecachingAPI.tInfo(MessageKeys.Command.Admin.DEBUG_EVENTS_OFF);

            // Must register regardless of if Debug Events are enabled, in case someone decides to enable it while running.
            // A check is performed in MCDebugEventHandler#sendDebugMessage
            getServer().getPluginManager().registerEvents(new MCDebugEventHandler(), this);
            getServer().getPluginManager().registerEvents(new MCEventHandler(), this);

            MinecachingAPI.tInfo(MessageKeys.Plugin.ENABLED, getVersionString());
        }
    }

    @Override
    public void onDisable() {
        MinecachingAPI.tInfo(MessageKeys.Plugin.DISABLING);

        // Close all open MCMenus
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() != null) {
                Inventory inv = player.getOpenInventory().getTopInventory();
                if (inv.getHolder() instanceof MCMenuHolder) player.closeInventory();
            }
        }

        // Performs auto-update if there is a newer plugin version and the config AUTO_UPDATE option is true
        if (MinecachingAPI.getUpdater().hasUpdate() && Config.getInstance().autoUpdate()) {
            MinecachingAPI.getUpdater().applyUpdate();
        } else MinecachingAPI.tInfo(MessageKeys.Plugin.Update.AUTO_DISABLED_DOWNLOAD);

        // Save all plugin data
        String disabledMsg = MessageKeys.Plugin.DISABLED.console(getVersionString());
        api.save();
        getLogger().info(disabledMsg);
    }

    public static Minecaching getInstance() {
        return plugin;
    }

    public static AutoUpdater.Version getVersion() {
        return version;
    }

    public static String getVersionString() {
        return version.toString();
    }

    public static MinecachingAPI getAPI() {
        return api;
    }
}
