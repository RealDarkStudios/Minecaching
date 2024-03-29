package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LogbookStorage {
    private HashMap<String, LogbookDataObject> logStorage;
    private List<String> logIDs = new ArrayList<>();

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
        return deleteLogbook(log.id());
    }

    public boolean deleteLogbook(Minecache cache) {
        return deleteLogbook(cache.id());
    }

    public boolean deleteLogbook(String id){
        try {
            LogbookStorage.getInstance().save();

            boolean success = getLogbook(id).delete();
            MinecachingAPI.tInfo(success ? MCMessageKeys.Plugin.LOGBOOK_DELETED : MCMessageKeys.Plugin.LOGBOOK_FAILED_DELETE, id);
            logIDs.removeAll(Collections.singleton(id));

            LogbookStorage.getInstance().updateMaps();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LogbookDataObject createLogbook(Minecache cache) {
        return createLogbook(cache.id());
    }

    public LogbookDataObject createLogbook(String id) {
        logIDs.add(id);

        save();
        updateMaps();

        return getLogbook(id);
    }

    public boolean hasLogbook(Minecache cache) {
        return hasLogbook(cache.id());
    }

    public boolean hasLogbook(String id) {
        return logStorage.containsKey(id);
    }

    public LogbookDataObject getLogbook(Minecache cache) {
        return getLogbook(cache.id());
    }

    public LogbookDataObject getLogbook(String id) {
        return logStorage.get(id);
    }

    public LogbookDataObject getOrCreateLogbook(Minecache cache) {
        return getOrCreateLogbook(cache.id());
    }

    public LogbookDataObject getOrCreateLogbook(String id) {
        if (hasLogbook(id)) return getLogbook(id);
        else return createLogbook(id);
    }

    public List<LogbookDataObject> getLogbooks() {
        return logStorage.values().stream().toList();
    }

    public static LogbookStorage getInstance() {
        if (MinecachingAPI.get().hasInitialized()) return MinecachingAPI.getLogbookStorage();
        else return new LogbookStorage();
    }

    public void updateData() {
        try {
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.attemptUpdate();
                }
            }

            MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.UPDATE_SUCCEEDED,  "Logbook Data", Config.getInstance().getLogbookDataVersion(), MinecachingAPI.LOGBOOK_DATA_VERSION);

            Config.getInstance().setLogbookDataVersion(MinecachingAPI.LOGBOOK_DATA_VERSION);
            if (logStorage != null) {
                for (LogbookDataObject log : logStorage.values()) {
                    log.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            MinecachingAPI.tWarning(MCMessageKeys.Plugin.Data.UPDATE_FAILED,  "Logbook Data", Config.getInstance().getLogbookDataVersion(), MinecachingAPI.LOGBOOK_DATA_VERSION);
        }
    }
}
