package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.commands.*;
import net.realdarkstudios.minecaching.api.Config;
import net.realdarkstudios.minecaching.api.LogbookStorage;
import net.realdarkstudios.minecaching.api.MinecacheStorage;
import net.realdarkstudios.minecaching.api.PlayerStorage;
import net.realdarkstudios.minecaching.event.MCEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Minecaching extends JavaPlugin {
    private final String VERSION = getDescription().getVersion();
    public int CONFIG_DATA_VERSION = 4;
    public int MINECACHE_DATA_VERSION = 3;
    public int PLAYER_DATA_VERSION = 2;
    public int LOGBOOK_DATA_VERSION = 1;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(String.format("Minecaching v%s is enabling...", VERSION));
        getServer().getPluginManager().registerEvents(new MCEventHandler(), this);

        File playerFolder = new File(getDataFolder() + "/player/");
        if (!playerFolder.exists()) playerFolder.mkdirs();

        File logFolder = new File(getDataFolder() + "/logs/");
        if (!logFolder.exists()) logFolder.mkdirs();

        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            getLogger().info("This plugin requires you to be on 1.16 or above!");
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            Config.getInstance().load();
            MinecacheStorage.getInstance().load();
            PlayerStorage.getInstance().load();
            LogbookStorage.getInstance().load();
            onDisable();
        } else {
            getLogger().info("Loading config...");
            Config.getInstance().load();
            if (Config.getInstance().getConfigVersion() < CONFIG_DATA_VERSION) {
                getLogger().warning("Config Version out of date!");
                Config.getInstance().attemptUpdate();
            }

            getLogger().info("Loading minecaches...");
            MinecacheStorage.getInstance().load();
            if (Config.getInstance().getMinecacheDataVersion() < MINECACHE_DATA_VERSION) {
                getLogger().warning("Minecache Data Version out of date!");
                MinecacheStorage.getInstance().attemptUpdate();
            }

            getLogger().info("Loading player data...");
            PlayerStorage.getInstance().load();
            if (Config.getInstance().getPlayerDataVersion() < PLAYER_DATA_VERSION) {
                getLogger().warning("Player Data Version out of date!");
                PlayerStorage.getInstance().attemptUpdate();
            }

            getLogger().info("Loading logbook data...");
            LogbookStorage.getInstance().load();
            if (Config.getInstance().getLogbookDataVersion() < LOGBOOK_DATA_VERSION) {
                getLogger().warning("Logbook Data Version out of date!");
                LogbookStorage.getInstance().attemptUpdate();
            }

            getLogger().info("Registering commands...");
            getCommand("addcache").setExecutor(new AddCacheCommand());
            getCommand("editcache").setExecutor(new EditCacheCommand());
            getCommand("listcaches").setExecutor(new ListCachesCommand());
            getCommand("delcache").setExecutor(new DeleteCacheCommand());
            getCommand("mcadmin").setExecutor(new MCAdminCommand());
            getCommand("verifycache").setExecutor(new VerifyCacheCommand());
            getCommand("locatecache").setExecutor(new LocateCacheCommand());
            getCommand("logcache").setExecutor(new LogCacheCommand());

            getLogger().info("Minecaching has been enabled!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Minecaching is disabling...");
        getLogger().info("Saving config...");
        Config.getInstance().save();
        getLogger().info("Saving minecache data...");
        MinecacheStorage.getInstance().save();
        getLogger().info("Saving player data...");
        PlayerStorage.getInstance().save();
        getLogger().info("Saving logbook data...");
        LogbookStorage.getInstance().save();
        getLogger().info("Minecaching has been disabled");
    }

    public static Minecaching getInstance() {
        return getPlugin(Minecaching.class);
    }
}
