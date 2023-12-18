package net.realdarkstudios.minecaching.data;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

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

    public int getMinecacheVersion() {
        return yaml.getInt("MINECACHE_VERSION");
    }

    public void setMinecacheVersion(int version) {
        yaml.set("MINECACHE_VERSION", version);
    }

    public int getPlayerVersion() {
        return yaml.getInt("PLAYER_VERSION");
    }

    public void setPlayerVersion(int version) {
        yaml.set("PLAYER_VERSION", version);
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

    public List<?> getEnabledTypes() {
        return yaml.getList("ENABLED_TYPES");
    }

    public int getFindLodestoneDistance() {
        return yaml.getInt("FIND_LODESTONE_DISTANCE");
    }
    public static Config getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            int configVersion = getConfigVersion();
            int minecacheVersion = getMinecacheVersion();
            int playerVersion = getPlayerVersion();
            int maxY = getMaxY();
            int minY = getMinY();
            int maxX = getMaxX();
            int minX = getMinX();
            int maxZ = getMaxZ();
            int minZ = getMinZ();

            int maxLodestoneDistance = getMaxLodestoneDistance();
            List<?> enabledTypes = getEnabledTypes();

            int findLodestoneDistance = getFindLodestoneDistance();

            Minecaching.getInstance().saveResource("config.yml", true);
            load();

            yaml.set("MINECACHE_VERSION", minecacheVersion);
            yaml.set("PLAYER_VERSION", playerVersion);
            yaml.set("MAX_Y", maxY);
            yaml.set("MIN_Y", minY);
            yaml.set("MAX_X", maxX);
            yaml.set("MIN_X", minX);
            yaml.set("MAX_Z", maxZ);
            yaml.set("MIN_Z", minZ);
            yaml.set("MAX_LODESTONE_DISTANCE", maxLodestoneDistance);
            yaml.set("ENABLED_TYPES", enabledTypes);
            yaml.set("FIND_LODESTONE_DISTANCE", findLodestoneDistance);
            yaml.set("CONFIG_VERSION", Minecaching.getInstance().CONFIG_DATA_VERSION);
            Minecaching.getInstance().getLogger().info("Config update succeeded, updated from v" + configVersion + " to v" + getConfigVersion());
            save();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Config update failed!");
        }
    }
}
