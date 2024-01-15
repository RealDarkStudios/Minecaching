package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;

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

    void load() {
        updateMaps();

        if (logStorage != null) {
            for (LogbookDataObject log : logStorage.values()) {
                log.load();
            }
        }
    }

    void save() {
        if (logStorage != null) {
            for (LogbookDataObject log : logStorage.values()) {
                log.save();
            }
        }
    }

    void updateMaps() {
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

    boolean deleteLogbook(LogbookDataObject log) {
        try {
            logIDs.removeAll(Collections.singleton(log.id()));

            save();
            updateMaps();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean deleteLogbook(Minecache cache) {
        try {
            logIDs.removeAll(Collections.singleton(cache.id()));

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean deleteLogbook(String id){
        try {
            logIDs.removeAll(Collections.singleton(id));

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    LogbookDataObject createLogbook(Minecache cache) {
        logIDs.add(cache.id());

        save();
        updateMaps();

        return getLogbook(cache);
    }

    LogbookDataObject createLogbook(String id) {
        logIDs.add(id);

        save();
        updateMaps();

        return getLogbook(id);
    }

    boolean hasLogbook(Minecache cache) {
        return logStorage.containsKey(cache.id());
    }

    boolean hasLogbook(String id) {
        return logStorage.containsKey(id);
    }

    LogbookDataObject getLogbook(Minecache cache) {
        return logStorage.get(cache.id());
    }

    LogbookDataObject getLogbook(String id) {
        return logStorage.get(id);
    }

    LogbookDataObject getOrCreateLogbook(Minecache cache) {
        if (hasLogbook(cache)) return getLogbook(cache);
        else return createLogbook(cache);
    }

    LogbookDataObject getOrCreateLogbook(String id) {
        if (hasLogbook(id)) return getLogbook(id);
        else return createLogbook(id);
    }

    List<LogbookDataObject> getLogbooks() {
        return logStorage.values().stream().toList();
    }

    public static LogbookStorage getInstance() {
        return INSTANCE;
    }

    void attemptUpdate() {
        try {
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.attemptUpdate();
                }
            }

            Minecaching.getInstance().getLogger().info("Logbook data update succeeded, updated from v" + Config.getInstance().getLogbookDataVersion() + " to v" + MinecachingAPI.getLogbookDataVersion());

            Config.getInstance().setLogbookDataVersion(MinecachingAPI.getLogbookDataVersion());
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Logbook data update failed!");
        }
    }
}
