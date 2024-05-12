package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

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

    public List<Log> getAllKnownLogs() {
        return logMap.values().stream().toList();
    }

    public List<Log> getFilteredLogs(Predicate<Log> filter) {
        return logMap.values().stream().filter(filter).toList();
    }

    public List<Log> getSortedLogs(Comparator<Log> comparator) {
        return logMap.values().stream().sorted(comparator).toList();
    }

    public boolean hasLog(String id) {
        return logMap.containsKey(id);
    }

    public Log createLog(Player player, LogType type, String message, boolean isFTF) {
        return createLog(player.getUniqueId(), type, message, isFTF);
    }

    public Log createLog(UUID uuid, LogType type, String message, boolean isFTF) {
        Log log = new Log(id, MCUtils.generateRandomString(5), uuid, type, LocalDateTime.now(), message, false, isFTF);
        log.toYaml(yaml, log.logId());

        saveData();

        return log;
    }

    public boolean deleteLog(Log log) {
        return deleteLog(log.logId());
    }

    public boolean deleteLog(String id) {
        try {
            yaml.set(getLog(id).logId(), null);
            logMap.remove(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void load() {
        yaml = YamlConfiguration.loadConfiguration(file);
        yaml.options().parseComments(true);

        getLDO(id);

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

    boolean deleteLDO() {
        File toDelete = file;
        file = new File("");
        return toDelete.delete();
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

    public void updateData() {
        update();

        getLDO(id);

        saveData();
    }

    public static LogbookDataObject getLDO(Minecache cache) {
        return getLDO(cache.id());
    }

    public static LogbookDataObject getLDO(String id) {
        File logFile = new File(Minecaching.getInstance().getDataFolder() + "/logbook/" + id + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(logFile);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.CREATE_FILE, id + ".yml");
            }
        } else if (Config.getInstance().getLogbookDataVersion() != MinecachingAPI.LOGBOOK_DATA_VERSION) {
            try {
                if (!logFile.canWrite()) throw new Exception();
                LogbookDataObject ldo = getLDO(id, yaml, logFile);
                logFile.delete();
                logFile.createNewFile();
                ldo.saveData();
                return ldo;
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.UPDATE_FILE, id + ".yml");
            }
        }

        return new LogbookDataObject(id, yaml, logFile);
    }

    private static LogbookDataObject getLDO(String id, YamlConfiguration yaml, File logFile) {
        return new LogbookDataObject(id, yaml, logFile);
    }
}
