package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class LogbookDataObject {
    private final String id;
    private YamlConfiguration yaml;
    private File file;
    private HashMap<String, Log> logs = new HashMap<>();

    public LogbookDataObject(String id, YamlConfiguration yaml, File file) {
        this.id = id;
        this.yaml = yaml;
        this.file = file;
    }

    public String id() {
        return id;
    }

    void load() {
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

    void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void attemptUpdate() {
        update();

        get(id);

        saveData();
    }

    public static LogbookDataObject get(Minecache cache) {
        return get(cache.id());
    }

    public static LogbookDataObject get(String id) {
        File logFile = new File(Minecaching.getInstance().getDataFolder() + "/logbook/" + id + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(logFile);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Failed to make log file " + id + ".yml");
            }
        } else if (Config.getInstance().getLogbookDataVersion() != Minecaching.getInstance().LOGBOOK_DATA_VERSION) {
            try {
                logFile.delete();
                logFile.createNewFile();
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Failed to make log file " + id + ".yml during update");
            }
        }

        return new LogbookDataObject(id, yaml, logFile);
    }
}
