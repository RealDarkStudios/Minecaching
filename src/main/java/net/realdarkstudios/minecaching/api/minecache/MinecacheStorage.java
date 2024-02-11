package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MinecacheStorage {
    private final static MinecacheStorage INSTANCE = new MinecacheStorage();

    private File file;
    private YamlConfiguration yaml;

    private MinecacheStorage() {
    }

    private ArrayList<Minecache> minecaches = null;
    private HashMap<String, Minecache> idToMinecache = null;
    private HashMap<Location, Minecache> mcLocations = null;
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
    }

    public void updateMaps() {
        ArrayList<Minecache> caches = new ArrayList<>(yaml.getKeys(false).size());
        HashMap<String, Minecache> idMap = new HashMap(yaml.getKeys(false).size());
        HashMap<Location, Minecache> locationMap = new HashMap<>(yaml.getKeys(false).size());

        for (String key : yaml.getKeys(false)) {
            Minecache cache = Minecache.fromYaml(yaml, key);

            caches.add(cache);
            idMap.put(key, cache);
            locationMap.put(new Location(cache.world(), cache.x(), cache.y(), cache.z()), cache);
        }

        caches.sort(Minecache::compareByTime);

        this.minecaches = caches;
        this.idToMinecache = Utils.sortCachesByTime(idMap);
        this.mcLocations = locationMap;
    }
    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        try {
            int tries = 0;

            while (idToMinecache.containsKey(minecache.id()) && isNewCache) {
                minecache.setID(Utils.generateCacheID((int) (Math.floor((double) tries / 50) + 5)));
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

    public HashMap<Location, Minecache> getLocations() {
        return mcLocations;
    }

    public static MinecacheStorage getInstance() {
        return INSTANCE;
    }

    public boolean isFTF(UUID plr, Minecache minecache) {
        return minecache.ftf().equals(Utils.EMPTY_UUID);
    }

    public void attemptUpdate() {
        try {
            updateMaps();

            Minecaching.getInstance().saveResource("minecaches.yml", true);

            for (Minecache cache : minecaches) {
                cache.toYaml(yaml, cache.id());
            }

            MinecachingAPI.tInfo("plugin.data.update.succeed",  "Minecache Data", Config.getInstance().getMinecacheDataVersion(), MinecachingAPI.getMinecacheDataVersion());

            Config.getInstance().setMinecacheDataVersion(MinecachingAPI.getMinecacheDataVersion());
            Config.getInstance().save();

            save();
            updateMaps();
        } catch (Exception e) {
            MinecachingAPI.tWarning("plugin.data.update.fail",  "Minecache Data", Config.getInstance().getMinecacheDataVersion(), MinecachingAPI.getMinecacheDataVersion());
        }
    }
}
