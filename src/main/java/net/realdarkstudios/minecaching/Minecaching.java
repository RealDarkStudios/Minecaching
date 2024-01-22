package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.api.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.commands.*;
import net.realdarkstudios.minecaching.event.MCDebugEventHandler;
import net.realdarkstudios.minecaching.event.MCEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Minecaching extends JavaPlugin {
    private final String VERSION = getDescription().getVersion();

    @Override
    public void onEnable() {
        // Plugin startup logic
        File playerFolder = new File(getDataFolder() + "/player/");
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File logFolder = new File(getDataFolder() + "/logbook/");
        if (!logFolder.exists()) logFolder.mkdirs();

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
            getCommand("editcache").setExecutor(new EditCacheCommand());
            getCommand("listcaches").setExecutor(new ListCachesCommand());
            getCommand("delcache").setExecutor(new DeleteCacheCommand());
            getCommand("mcadmin").setExecutor(new MCAdminCommand());
            getCommand("verifycache").setExecutor(new VerifyCacheCommand());
            getCommand("locatecache").setExecutor(new LocateCacheCommand());
            getCommand("logcache").setExecutor(new LogCacheCommand());
            getCommand("logbook").setExecutor(new LogbookCommand());

            if (Config.getInstance().getDebugEvents()) MinecachingAPI.tInfo("mcadmin.version.debugevents.on", Config.getInstance().getDebugEventsLevel());
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
        MinecachingAPI.get().save();
        getLogger().info("Minecaching has been disabled");
    }

    public static Minecaching getInstance() {
        return getPlugin(Minecaching.class);
    }
}
