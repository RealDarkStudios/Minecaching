package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class LogbookStorageObject {
    private final String id;
    private YamlConfiguration yaml;
    private File file;
    private HashMap<String, Log> logs = new HashMap<>();

    public LogbookStorageObject(String id, YamlConfiguration yaml, File file) {
        this.id = id;
        this.yaml = yaml;
        this.file = file;
    }

    public String id() {
        return id;
    }

    public void load() {
        yaml = YamlConfiguration.loadConfiguration(file);
        yaml.options().parseComments(true);

        get(id);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        update();
    }

    private void update() {
        HashMap<String, String> logMap = new HashMap<>();

        for (String key: yaml.getKeys(false)) {

        }
    }

    public void saveData() {
        save();
        update();
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean attemptUpdate() {
        update();

        get(id);

        saveData();
        return true;
    }

    public static LogbookStorageObject get(Minecache cache) {
        return get(cache.id());
    }

    public static LogbookStorageObject get(String id) {
        File logFile = new File(Minecaching.getInstance().getDataFolder() + "/logs/" + id + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(logFile);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Failed to make log file " + id + ".yml");
            }
        }

        return new LogbookStorageObject(id, yaml, logFile);
    }
}
