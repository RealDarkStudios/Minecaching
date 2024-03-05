package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenuHolder;
import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.commands.*;
import net.realdarkstudios.minecaching.event.MCDebugEventHandler;
import net.realdarkstudios.minecaching.event.MCEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public final class Minecaching extends JavaPlugin {
    private final String VERSION = getDescription().getVersion();

    @Override
    public void onEnable() {
        // Server version check
        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            MinecachingAPI.get().load(false);
            MinecachingAPI.tWarning(MessageKeys.Plugin.VERSION_WARNING);
            onDisable();
        } else {
            // Load plugin data
            MinecachingAPI.get().load(true);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, AutoUpdater::checkForUpdate, 0L,36000L);

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

            // Debug Events check
            if (Config.getInstance().debugEvents()) MinecachingAPI.tInfo(MessageKeys.Command.Admin.DEBUG_EVENTS_ON, Config.getInstance().getDebugEventsLevel());
            else MinecachingAPI.tInfo(MessageKeys.Command.Admin.DEBUG_EVENTS_OFF);

            // Must register regardless of if Debug Events are enabled, in case someone decides to enable it while running.
            // A check is performed in MCDebugEventHandler#sendDebugMessage
            getServer().getPluginManager().registerEvents(new MCDebugEventHandler(), this);
            getServer().getPluginManager().registerEvents(new MCEventHandler(), this);

            MinecachingAPI.tInfo(MessageKeys.Plugin.ENABLED, VERSION);
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
        if (AutoUpdater.hasUpdate() && Config.getInstance().autoUpdate()) {
            AutoUpdater.checkForUpdate();
            String newVersion = AutoUpdater.getNewestVersion();
            MinecachingAPI.tInfo(MessageKeys.Plugin.Update.GETTING, newVersion);
            try {
                URL download = new URL("https://maven.digitalunderworlds.com/" + Config.getInstance().getUpdateBranch() + "s/net/realdarkstudios/Minecaching/" + newVersion + "/Minecaching-" + newVersion + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(download.openStream());
                File file = new File(Path.of(getDataFolder().toURI()).getParent().toString() + "/Minecaching-" + newVersion + ".jar");
                FileOutputStream fos = new FileOutputStream(file);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                MinecachingAPI.tInfo(MessageKeys.Plugin.Update.DOWNLOADED);

                Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
                getFileMethod.setAccessible(true);
                File curfile = (File) getFileMethod.invoke(this);
                MinecachingAPI.tInfo(MessageKeys.Plugin.Update.APPLIED);
                curfile.deleteOnExit();
            } catch (Exception e) {
                MinecachingAPI.tWarning(MessageKeys.Plugin.Update.FAIL);
                e.printStackTrace();
            }
        } else MinecachingAPI.tInfo(MessageKeys.Plugin.Update.AUTO_DISABLED_DOWNLOAD);

        // Save all plugin data
        String disabledMsg = MessageKeys.Plugin.DISABLED.translate(VERSION);
        MinecachingAPI.get().save();
        getLogger().info(disabledMsg);
    }

    public static Minecaching getInstance() {
        return getPlugin(Minecaching.class);
    }
}
