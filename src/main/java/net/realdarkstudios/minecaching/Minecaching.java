package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.commands.*;
import net.realdarkstudios.minecaching.data.Config;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
import net.realdarkstudios.minecaching.data.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Minecaching extends JavaPlugin {
    private final String VERSION = getDescription().getVersion();
    public int CONFIG_DATA_VERSION = 3;
    public int MINECACHE_DATA_VERSION = 3;
    public int PLAYER_DATA_VERSION = 2;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(String.format("Minecaching v%s is enabling...", VERSION));

        File playerFolder = new File(getDataFolder() + "/player/");
        if (!playerFolder.exists()) playerFolder.mkdirs();

        getLogger().info("Checking server version...");
        getLogger().info("Server Version: " + Bukkit.getBukkitVersion());
        String[] serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        if (!(Integer.parseInt(serverVersion[1]) >= 16)) {
            getLogger().info("This plugin requires you to be on 1.16 or above!");
            // Load Config and MCStorage so there's no warning about this.yaml not existing
            Config.getInstance().load();
            MinecacheStorage.getInstance().load();
            PlayerStorage.getInstance().load();
            onDisable();
        } else {
            getLogger().info("Registering commands...");
            getCommand("addcache").setExecutor(new AddCacheCommand());
            getCommand("editcache").setExecutor(new EditCacheCommand());
            getCommand("listcaches").setExecutor(new ListCachesCommand());
            getCommand("delcache").setExecutor(new DeleteCacheCommand());
            getCommand("mcadmin").setExecutor(new MCAdminCommand());
            getCommand("verifycache").setExecutor(new VerifyCacheCommand());
            getCommand("locatecache").setExecutor(new LocateCacheCommand());
            getCommand("findcache").setExecutor(new FindCacheCommand());
            getServer().getPluginManager().registerEvents(new MCEventHandler(), this);
            getLogger().info("Loading config...");
            Config.getInstance().load();
            if (Config.getInstance().getConfigVersion() < CONFIG_DATA_VERSION) {
                getLogger().warning("Config Version out of date!");
                Config.getInstance().attemptUpdate();
            }
            getLogger().info("Loading minecaches...");
            MinecacheStorage.getInstance().load();
            if (Config.getInstance().getMinecacheVersion() < MINECACHE_DATA_VERSION) {
                getLogger().warning("Minecache Version out of date!");
                MinecacheStorage.getInstance().attemptUpdate();
            }
            getLogger().info("Loading player data...");
            PlayerStorage.getInstance().load();
            if (Config.getInstance().getPlayerVersion() < PLAYER_DATA_VERSION) {
                getLogger().warning("Player Version out of date!");
                PlayerStorage.getInstance().attemptUpdate();
            }
            getLogger().info("Minecaching has been enabled!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Minecaching is disabling...");
        getLogger().info("Saving config...");
        Config.getInstance().save();
        getLogger().info("Saving minecaches...");
        MinecacheStorage.getInstance().save();
        getLogger().info("Saving player data...");
        PlayerStorage.getInstance().save();
        getLogger().info("Minecaching has been disabled");
    }

    public static Minecaching getInstance() {
        return getPlugin(Minecaching.class);
    }
}
