package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LogbookStorage {
    private final static LogbookStorage INSTANCE = new LogbookStorage();
    private HashMap<String, LogbookDataObject> logStorage;
    private List<String> logIDs;

    private LogbookStorage() {
    }

    public void load() {
        updateMaps();

        if (logStorage != null) {
            for (LogbookDataObject log : logStorage.values()) {
                log.load();
            }
        }
    }

    public void save() {
        if (logStorage != null) {
            for (LogbookDataObject log : logStorage.values()) {
                log.save();
            }
        }
    }

    public void updateMaps() {
        HashMap<String, LogbookDataObject> logs = new HashMap<>();
        ArrayList<String> lIDs = new ArrayList<>();

        for (String key: MinecacheStorage.getInstance().getIDArray()) {
            LogbookDataObject log = LogbookDataObject.get(key);

            log.load();
            logs.put(key, log);
            lIDs.add(log.id());
        }

        this.logStorage = logs;
    }

    public boolean deleteLogbook(LogbookDataObject log) {
        try {
            logIDs.removeAll(Collections.singleton(log.id()));

            save();
            updateMaps();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteLogbook(Minecache cache) {
        try {
            logIDs.removeAll(Collections.singleton(cache.id()));

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteLogbook(String id){
        try {
            logIDs.removeAll(Collections.singleton(id));

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LogbookDataObject createLogbook(Minecache cache) {
        logIDs.add(cache.id());

        save();
        updateMaps();

        return getLogbook(cache);
    }

    public LogbookDataObject createLogbook(String id) {
        logIDs.add(id);

        save();
        updateMaps();

        return getLogbook(id);
    }

    public boolean hasLogbook(Minecache cache) {
        return logStorage.containsKey(cache.id());
    }

    public boolean hasLogbook(String id) {
        return logStorage.containsKey(id);
    }

    public LogbookDataObject getLogbook(Minecache cache) {
        return logStorage.get(cache.id());
    }

    public LogbookDataObject getLogbook(String id) {
        return logStorage.get(id);
    }

    public LogbookDataObject getOrCreateLogbook(Minecache cache) {
        if (hasLogbook(cache)) return getLogbook(cache);
        else return createLogbook(cache);
    }

    public LogbookDataObject getOrCreateLogbook(String id) {
        if (hasLogbook(id)) return getLogbook(id);
        else return createLogbook(id);
    }

    public List<LogbookDataObject> getLogbooks() {
        return logStorage.values().stream().toList();
    }

    public static LogbookStorage getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.attemptUpdate();
                }
            }

            MinecachingAPI.tInfo("plugin.data.update.succeed",  "Logbook Data", Config.getInstance().getLogbookDataVersion(), MinecachingAPI.getLogbookDataVersion());

            Config.getInstance().setLogbookDataVersion(MinecachingAPI.getLogbookDataVersion());
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            MinecachingAPI.tWarning("plugin.data.update.fail",  "Logbook Data", Config.getInstance().getLogbookDataVersion(), MinecachingAPI.getLogbookDataVersion());
        }
    }
}
