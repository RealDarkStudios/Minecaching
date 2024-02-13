package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.joml.Math;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class Config {
    private final static Config INSTANCE = new Config();

    private File file;
    private YamlConfiguration yaml;

    private Config() {
    }

    public void load() {
        file = new File(Minecaching.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            Minecaching.getInstance().saveResource("config.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getConfigVersion() {
        return yaml.getInt("CONFIG_VERSION");
    }

    public int getMinecacheDataVersion() {
        return yaml.getInt("MINECACHE_DATA_VERSION");
    }

    public void setMinecacheDataVersion(int version) {
        yaml.set("MINECACHE_DATA_VERSION", version);
    }

    public int getPlayerDataVersion() {
        return yaml.getInt("PLAYER_DATA_VERSION");
    }

    public void setPlayerDataVersion(int version) {
        yaml.set("PLAYER_DATA_VERSION", version);
    }

    public int getLogbookDataVersion() {
        return yaml.getInt("LOGBOOK_DATA_VERSION");
    }

    public void setLogbookDataVersion(int version) {
        yaml.set("LOGBOOK_DATA_VERSION", version);
    }

    public Locale getServerLocale() {
        return Locale.forLanguageTag(yaml.getString("LOCALE"));
    }

    public boolean autoUpdate() {
        return yaml.getBoolean("AUTO_UPDATE");
    }

    public String getUpdateBranch() {
        return yaml.getString("UPDATE_BRANCH", "release").equals("snapshot") ? "snapshot" : "release";
    }

    public boolean experimentalFeatures() {
        return yaml.getBoolean("EXPERIMENTAL_FEATURES");
    }

    public boolean debugEvents() {
        return yaml.getBoolean("DEBUG_EVENTS");
    }

    public int getDebugEventsLevel() {
        return Math.clamp(0, 2, yaml.getInt("DEBUG_EVENTS_LEVEL"));
    }

    public int getMaxX() {
        return yaml.getInt("MAX_X");
    }

    public int getMinX() {
        return yaml.getInt("MIN_X");
    }

    public int getMaxY() {
        return yaml.getInt("MAX_Y");
    }

    public int getMinY() {
        return yaml.getInt("MIN_Y");
    }

    public int getMaxZ() {
        return yaml.getInt("MAX_Z");
    }

    public int getMinZ() {
        return yaml.getInt("MIN_Z");
    }

    public int getMaxLodestoneDistance() {
        return yaml.getInt("MAX_LODESTONE_DISTANCE");
    }
    public int getFindLodestoneDistance() {
        return yaml.getInt("FIND_LODESTONE_DISTANCE");
    }
    public boolean useLodestoneBasedLocating() {
        return yaml.getBoolean("USE_LODESTONE_BASED_LOCATING");
    }

    public List<?> getEnabledTypes() {
        return yaml.getList("ENABLED_TYPES");
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            int configVersion = getConfigVersion();
            int minecacheDataVersion = configVersion < 4 ? yaml.getInt("MINECACHE_VERSION") : getMinecacheDataVersion();
            int playerDataVersion = configVersion < 4 ? yaml.getInt("PLAYER_VERSION") : getPlayerDataVersion();
            int logbookDataVersion = configVersion < 4 ? 0 : getLogbookDataVersion();

            Locale serverLocale = configVersion < 7 ? Locale.US : getServerLocale();

            boolean autoUpdate = configVersion >=8 && autoUpdate();
            String updateBranch = getUpdateBranch();
            boolean experimentalFeatures = configVersion >=8 && experimentalFeatures();
            boolean debugEvents = configVersion >= 5 && debugEvents();
            int debugEventsLevel = configVersion < 5 ? 0 : getDebugEventsLevel();

            int maxY = getMaxY();
            int minY = getMinY();
            int maxX = getMaxX();
            int minX = getMinX();
            int maxZ = getMaxZ();
            int minZ = getMinZ();

            int maxLodestoneDistance = getMaxLodestoneDistance();
            int findLodestoneDistance = configVersion < 3 ? 25 : getFindLodestoneDistance();
            boolean useLodestoneBasedLocating = configVersion >= 6 && useLodestoneBasedLocating();

            List<?> enabledTypes = getEnabledTypes();

            Minecaching.getInstance().saveResource("config.yml", true);
            load();

            yaml.set("MINECACHE_DATA_VERSION", minecacheDataVersion);
            yaml.set("PLAYER_DATA_VERSION", playerDataVersion);
            yaml.set("LOGBOOK_DATA_VERSION", logbookDataVersion);
            yaml.set("LOCALE", serverLocale.toLanguageTag());
            yaml.set("AUTO_UPDATE", autoUpdate);
            yaml.set("UPDATE_BRANCH", updateBranch);
            yaml.set("EXPERIMENTAL_FEATURES", experimentalFeatures);
            yaml.set("DEBUG_EVENTS", debugEvents);
            yaml.set("DEBUG_EVENTS_LEVEL", debugEventsLevel);
            yaml.set("MAX_Y", maxY);
            yaml.set("MIN_Y", minY);
            yaml.set("MAX_X", maxX);
            yaml.set("MIN_X", minX);
            yaml.set("MAX_Z", maxZ);
            yaml.set("MIN_Z", minZ);
            yaml.set("MAX_LODESTONE_DISTANCE", maxLodestoneDistance);
            yaml.set("FIND_LODESTONE_DISTANCE", findLodestoneDistance);
            yaml.set("USE_LODESTONE_BASED_LOCATING", useLodestoneBasedLocating);
            yaml.set("ENABLED_TYPES", enabledTypes);
            yaml.set("CONFIG_VERSION", MinecachingAPI.getConfigDataVersion());
            MinecachingAPI.tInfo("plugin.data.update.succeed", "Config", configVersion, MinecachingAPI.getConfigDataVersion());
            save();
        } catch (Exception e) {
            MinecachingAPI.tInfo("plugin.data.update.failed", "Config", getConfigVersion(), MinecachingAPI.getConfigDataVersion());
        }
    }
}