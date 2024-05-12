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
            LogbookDataObject log = LogbookDataObject.getLDO(key);

            log.load();
            logs.put(key, log);
            lIDs.add(log.id());
        }

        this.logStorage = logs;
    }

    public boolean deleteLDO(LogbookDataObject log) {
        return deleteLDO(log.id());
    }

    public boolean deleteLDO(Minecache cache) {
        return deleteLDO(cache.id());
    }

    public boolean deleteLDO(String id){
        try {
            LogbookStorage.getInstance().save();

            boolean success = getLDO(id).deleteLDO();
            MinecachingAPI.tInfo(success ? MCMessageKeys.Plugin.LOGBOOK_DELETED : MCMessageKeys.Plugin.LOGBOOK_FAILED_TO_DELETE, id);
            logIDs.removeAll(Collections.singleton(id));

            LogbookStorage.getInstance().updateMaps();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LogbookDataObject createLDO(Minecache cache) {
        return createLDO(cache.id());
    }

    public LogbookDataObject createLDO(String id) {
        logIDs.add(id);

        save();
        updateMaps();

        return getLDO(id);
    }

    public boolean hasLDO(Minecache cache) {
        return hasLDO(cache.id());
    }

    public boolean hasLDO(String id) {
        return logStorage.containsKey(id);
    }

    public LogbookDataObject getLDO(Minecache cache) {
        return getLDO(cache.id());
    }

    public LogbookDataObject getLDO(String id) {
        return logStorage.get(id);
    }

    public LogbookDataObject getOrCreateLDO(Minecache cache) {
        return getOrCreateLDO(cache.id());
    }

    public LogbookDataObject getOrCreateLDO(String id) {
        if (hasLDO(id)) return getLDO(id);
        else return createLDO(id);
    }

    public List<LogbookDataObject> getAllKnownLDOs() {
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
                    log.updateData();
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
