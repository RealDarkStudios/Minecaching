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

    void deleteLog(LogbookDataObject log) {
        logIDs.removeAll(Collections.singleton(log.id()));

        save();
        updateMaps();
    }

    void deleteLog(Minecache cache) {
        logIDs.removeAll(Collections.singleton(cache.id()));

        save();
        updateMaps();
    }

    void deleteLog(String id) {
        logIDs.removeAll(Collections.singleton(id));

        save();
        updateMaps();
    }

    LogbookDataObject createLog(Minecache cache) {
        logIDs.add(cache.id());

        save();
        updateMaps();

        return getLog(cache);
    }

    LogbookDataObject createLog(String id) {
        logIDs.add(id);

        save();
        updateMaps();

        return getLog(id);
    }

    boolean hasLog(Minecache cache) {
        return logStorage.containsKey(cache.id());
    }

    boolean hasLog(String id) {
        return logStorage.containsKey(id);
    }

    LogbookDataObject getLog(Minecache cache) {
        return logStorage.get(cache.id());
    }

    LogbookDataObject getLog(String id) {
        return logStorage.get(id);
    }

    LogbookDataObject getOrCreateLog(Minecache cache) {
        if (hasLog(cache)) return getLog(cache);
        else return createLog(cache);
    }

    LogbookDataObject getOrCreateLog(String id) {
        if (hasLog(id)) return getLog(id);
        else return createLog(id);
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

            Minecaching.getInstance().getLogger().info("Logbook data update succeeded, updated from v" + Config.getInstance().getLogbookDataVersion() + " to v" + Minecaching.getInstance().PLAYER_DATA_VERSION);

            Config.getInstance().setLogbookDataVersion(Minecaching.getInstance().LOGBOOK_DATA_VERSION);
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
