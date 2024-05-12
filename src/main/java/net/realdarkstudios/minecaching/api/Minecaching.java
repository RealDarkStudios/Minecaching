package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.commons.menu.MCMenuHolder;
import net.realdarkstudios.commons.misc.IRDSPlugin;
import net.realdarkstudios.commons.util.Version;
import net.realdarkstudios.minecaching.api.event.MCDebugEventHandler;
import net.realdarkstudios.minecaching.api.event.MCEventHandler;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Minecaching extends JavaPlugin implements IRDSPlugin {
    private static Minecaching plugin;
    private static Version version;
    private static MinecachingAPI api;
    private static long uptimeStart;

    private static String disablingMsg;
    private static String disabledMsg;
    private static String autoDisabledDownloadMsg;
    @Override
    public void onEnable() {
        uptimeStart = System.currentTimeMillis();

        if (Bukkit.getServer().getPluginManager().getPlugin("RDSCommons") == null) {
            getLogger().warning("RDSCommons is not installed!");
            onDisable();
        }

        plugin = this;
        version = Version.fromString(getDescription().getVersion());
        api = new MinecachingAPI();

        api.init();

        // Server version check
        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            api.load(false);
            MinecachingAPI.tWarning(MCMessageKeys.Plugin.VERSION_WARNING);
            onDisable();
        } else {
            // Load plugin data
            api.load(true);

            disablingMsg = MCMessageKeys.Plugin.DISABLING.console();
            disabledMsg = MCMessageKeys.Plugin.DISABLED.console();
            autoDisabledDownloadMsg = MCMessageKeys.Plugin.Update.AUTO_DISABLED_DOWNLOAD.console(getVersionString());

            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                MinecachingAPI.getUpdater().updateBranch(Config.getInstance().getUpdateBranch());
                MinecachingAPI.getUpdater().checkForUpdate();
            }, 0L,36000L);

            // Register commands
            MinecachingAPI.tInfo(MCMessageKeys.Plugin.REGISTERING_COMMANDS);
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
            getCommand("cachehint").setExecutor(new HintCommand());

            // Debug Events check
            if (Config.getInstance().debugEvents()) MinecachingAPI.tInfo(MCMessageKeys.Command.Admin.DEBUG_EVENTS_ON, Config.getInstance().getDebugEventsLevel());
            else MinecachingAPI.tInfo(MCMessageKeys.Command.Admin.DEBUG_EVENTS_OFF);

            // Must register regardless of if Debug Events are enabled, in case someone decides to enable it while running.
            // A check is performed in MCDebugEventHandler#sendDebugMessage
            getServer().getPluginManager().registerEvents(new MCDebugEventHandler(), this);
            getServer().getPluginManager().registerEvents(new MCEventHandler(), this);

            MinecachingAPI.tInfo(MCMessageKeys.Plugin.ENABLED, getVersionString());
        }
    }

    @Override
    public void onDisable() {
        if (Bukkit.getServer().getPluginManager().getPlugin("RDSCommons") != null) {
            MinecachingAPI.info(disablingMsg);

            // Close all open MCMenus
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory() != null) {
                    Inventory inv = player.getOpenInventory().getTopInventory();
                    if (inv.getHolder() instanceof MCMenuHolder) player.closeInventory();
                }
            }

            // Performs auto-update if there is a newer plugin version and the config AUTO_UPDATE option is true
            if (MinecachingAPI.getUpdater().hasUpdate() && Config.getInstance().autoUpdate()) {
                MinecachingAPI.getUpdater().applyUpdate();
            } else MinecachingAPI.info(autoDisabledDownloadMsg);


            // Save all plugin data
            api.save();
            getLogger().info(disabledMsg);
        } else {
            // Performs auto-update if there is a newer plugin version and the config AUTO_UPDATE option is true
            if (MinecachingAPI.getUpdater().hasUpdate() && Config.getInstance().autoUpdate()) {
                MinecachingAPI.getUpdater().applyUpdate();
            } else MinecachingAPI.info("No update available (AUTO_UPDATE might be false in config)");

            // Save all plugin data
            api.save();
            getLogger().info("Minecaching has been disabled!");
        }
    }

    public static long getUptimeStart() {
        return uptimeStart;
    }

    @NotNull
    @Override
    public Version getVersion() {
        return version;
    }

    @NotNull
    @Override
    public String getVersionString() {
        return version.toString();
    }

    public static Minecaching getInstance() {
        return plugin;
    }

    public static MinecachingAPI getAPI() {
        return api;
    }
}
