package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LogbookDataObject {
    private final String id;
    private YamlConfiguration yaml;
    private File file;
    private HashMap<String, Log> logMap = new HashMap<>();

    public LogbookDataObject(String id, YamlConfiguration yaml, File file) {
        this.id = id;
        this.yaml = yaml;
        this.file = file;
    }

    public String id() {
        return id;
    }

    public Log getLog(String id) {
        return logMap.get(id);
    }

    public List<Log> getLogs() {
        return logMap.values().stream().toList();
    }

    public List<Log> getLogsSorted(Comparator<Log> comparator) {
        return logMap.values().stream().sorted(comparator).toList();
    }

    public boolean hasLog(String id) {
        return logMap.containsKey(id);
    }

    public Log createLog(Player player, LogType type, String message, boolean isFTF) {
        return createLog(player.getUniqueId(), type, message, isFTF);
    }

    public Log createLog(UUID uuid, LogType type, String message, boolean isFTF) {
        Log log = new Log(id, Utils.generateRandomString(5), uuid, type, LocalDateTime.now(), message, false, isFTF);
        log.toYaml(yaml, log.logId());

        saveData();

        return log;
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
        HashMap<String, Log> logMap = new HashMap<>();

        for (String key: yaml.getKeys(false)) {
            logMap.put(key, Log.fromYaml(yaml, key, id));
        }

        this.logMap = logMap;
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
        } else if (Config.getInstance().getLogbookDataVersion() != MinecachingAPI.getLogbookDataVersion()) {
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
