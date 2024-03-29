package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MinecacheStorage {
    private File file;
    private YamlConfiguration yaml;

    private MinecacheStorage() {
    }

    private ArrayList<Minecache> minecaches = null;
    private HashMap<String, Minecache> idToMinecache = null;
    public void load() {
        file = new File(Minecaching.getInstance().getDataFolder(), "minecaches.yml");

        if (!file.exists()) {
            Minecaching.getInstance().saveResource("minecaches.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMaps();

        MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.LOADED_CACHES, minecaches.size());
    }

    public void updateMaps() {
        ArrayList<Minecache> caches = new ArrayList<>(yaml.getKeys(false).size());
        HashMap<String, Minecache> idMap = new HashMap<>(yaml.getKeys(false).size());

        for (String key : yaml.getKeys(false)) {
            Minecache cache = Minecache.fromYaml(yaml, key);

            caches.add(cache);
            idMap.put(key, cache);
        }

        caches.sort(Minecache::compareByTime);

        this.minecaches = caches;
        this.idToMinecache = idMap;
    }
    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateNonConflictingCacheID() throws SizeLimitExceededException {
        for (int i = 0; i < 250; i++) {
            // Success rate should be about 99.9999149465%
            // There is about a 0.0000850535% chance this will fail due to ALL 250 combinations already being used.
            String possibleID = MCUtils.generateCacheID((int) (Math.floor((double) i / 50) + 5));
            if (checkID(possibleID)) return possibleID;
        }

        throw new SizeLimitExceededException("Could not generate a non-conflicting cache id after 250 tries. Consider your self lucky... or unlucky?");
    }

    private boolean checkID(String id) {
        return !idToMinecache.containsKey(id);
    }

    public boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        try {
            int tries = 0;

            while (idToMinecache.containsKey(minecache.id()) && isNewCache) {
                minecache.setID(MCUtils.generateCacheID((int) (Math.floor((double) tries / 50) + 5)));
                tries++;
            }

            minecache.toYaml(yaml, minecache.id());

            save();
            updateMaps();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteMinecache(Minecache minecache) {
        try {
            yaml.set(minecache.id(), null);
            save();
            updateMaps();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Minecache getMinecacheByID(String id) {
        return idToMinecache.get(id) != null ? idToMinecache.get(id) : Minecache.EMPTY;
    }

    public ArrayList<Minecache> getMinecaches() {
        return minecaches;
    }

    public ArrayList<String> getIDArray() {
        ArrayList<String> ids = new ArrayList<>();

        for (Minecache minecache: minecaches) {
            ids.add(minecache.id());
        }

        return ids;
    }

    public static MinecacheStorage getInstance() {
        if (MinecachingAPI.get().hasInitialized()) return MinecachingAPI.getCacheStorage();
        else return new MinecacheStorage();
    }

    public boolean isFTF(UUID plr, Minecache minecache) {
        return minecache.ftf().equals(MCUtils.EMPTY_UUID);
    }

    public void updateData() {
        try {
            updateMaps();

            Minecaching.getInstance().saveResource("minecaches.yml", true);

            for (Minecache cache : minecaches) {
                cache.toYaml(yaml, cache.id());
            }

            MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.UPDATE_SUCCEEDED,  "Minecache Data", Config.getInstance().getMinecacheDataVersion(), MinecachingAPI.MINECACHE_DATA_VERSION);

            Config.getInstance().setMinecacheDataVersion(MinecachingAPI.MINECACHE_DATA_VERSION);
            Config.getInstance().save();

            save();
            updateMaps();
        } catch (Exception e) {
            MinecachingAPI.tWarning(MCMessageKeys.Plugin.Data.UPDATE_FAILED,  "Minecache Data", Config.getInstance().getMinecacheDataVersion(), MinecachingAPI.MINECACHE_DATA_VERSION);
        }
    }
}
