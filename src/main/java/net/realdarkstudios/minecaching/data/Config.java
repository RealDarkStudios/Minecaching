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
    public static Config getInstance() {
        return INSTANCE;
    }
}
