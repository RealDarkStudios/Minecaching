package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.api.misc.AutoUpdater;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.impl.MCMenuHolder;
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
        // Plugin startup logic
        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            getLogger().warning("This plugin can't run on versions below 1.16! Please update!");
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            MinecachingAPI.get().load(false);
            onDisable();
        } else {
            MinecachingAPI.get().load(true);

            getLogger().info("Registering commands...");
            getCommand("addcache").setExecutor(new AddCacheCommand());
            getCommand("archivecache").setExecutor(new ArchiveCacheCommand());
            getCommand("disablecache").setExecutor(new DisableCacheCommand());
            getCommand("editcache").setExecutor(new EditCacheCommand());
            getCommand("listcaches").setExecutor(new ListCachesCommand());
            getCommand("delcache").setExecutor(new DeleteCacheCommand());
            getCommand("mcadmin").setExecutor(new MCAdminCommand());
            getCommand("publishcache").setExecutor(new PublishCacheCommand());
            getCommand("locatecache").setExecutor(new LocateCacheCommand());
            getCommand("logcache").setExecutor(new LogCacheCommand());
            getCommand("logbook").setExecutor(new LogbookCommand());

            if (Config.getInstance().debugEvents()) MinecachingAPI.tInfo("mcadmin.version.debugevents.on", Config.getInstance().getDebugEventsLevel());
            else MinecachingAPI.tInfo("mcadmin.version.debugevents.off");

            // Must register regardless of if Debug Events are enabled, in case someone decides to enable it while running.
            // A check is performed in MCDebugEventHandler#sendDebugMessage
            getServer().getPluginManager().registerEvents(new MCDebugEventHandler(), this);
            getServer().getPluginManager().registerEvents(new MCEventHandler(), this);

            MinecachingAPI.tInfo("plugin.enabled", VERSION);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Minecaching is disabling...");
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() != null) {
                Inventory inv = player.getOpenInventory().getTopInventory();
                if (inv.getHolder() instanceof MCMenuHolder) player.closeInventory();
            }
        }

        MinecachingAPI.get().save();

        if (AutoUpdater.hasUpdate()) {
            getLogger().info("Applying update...");
            String newVersion = AutoUpdater.getNewVer();
            try {
                URL download = new URL("https://maven.digitalunderworlds.com/releases/net/realdarkstudios/Minecaching/" + newVersion + "/Minecaching-" + newVersion + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(download.openStream());
                File file = new File(Path.of(getDataFolder().toURI()).getParent().toString() + "/Minecaching-" + newVersion + ".jar");
                FileOutputStream fos = new FileOutputStream(file);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();

                Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
                getFileMethod.setAccessible(true);
                File curfile = (File) getFileMethod.invoke(this);
                getLogger().info("Update applied, this plugin file will be deleted on process exit!");
                curfile.deleteOnExit();
            } catch (Exception e) {
                MinecachingAPI.warning("Unable to complete auto update!");
                e.printStackTrace();
            }

        }
        getLogger().info("Minecaching has been disabled");
    }

    public static Minecaching getInstance() {
        return getPlugin(Minecaching.class);
    }
}
