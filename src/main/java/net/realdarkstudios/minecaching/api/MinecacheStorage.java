package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
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
    void load() {
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

    void updateMaps() {
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
        this.idToMinecache = Utils.sortByMinecache(idMap);
        this.mcLocations = locationMap;
    }
    void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean saveMinecache(Minecache minecache, boolean isNewCache) {
        try {
            int tries = 0;

            while (idToMinecache.containsKey(minecache.id()) && isNewCache) {
                minecache.setID(Utils.generateID((int) (Math.floor((double) tries / 50) + 5)));
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

    boolean deleteMinecache(Minecache minecache) {
        try {
            yaml.set(minecache.id(), null);
            save();
            updateMaps();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    Minecache getMinecacheByID(String id) {
        return idToMinecache.get(id) != null ? idToMinecache.get(id) : Minecache.EMPTY;
    }

    ArrayList<Minecache> getMinecaches() {
        return minecaches;
    }

    ArrayList<String> getIDArray() {
        ArrayList<String> ids = new ArrayList<>();

        for (Minecache minecache: minecaches) {
            ids.add(minecache.id());
        }

        return ids;
    }

    HashMap<Location, Minecache> getLocations() {
        return mcLocations;
    }

    public static MinecacheStorage getInstance() {
        return INSTANCE;
    }

    boolean isFTF(UUID plr, Minecache minecache) {
        return minecache.ftf().equals(Utils.EMPTY_UUID);
    }

    void attemptUpdate() {
        try {
            updateMaps();

            Minecaching.getInstance().saveResource("minecaches.yml", true);

            for (Minecache cache : minecaches) {
                cache.toYaml(yaml, cache.id());
            }

            Minecaching.getInstance().getLogger().info("Minecache data update succeeded, updated from v" + Config.getInstance().getMinecacheDataVersion() + " to v" + MinecachingAPI.getMinecacheDataVersion());

            Config.getInstance().setMinecacheDataVersion(MinecachingAPI.getMinecacheDataVersion());
            Config.getInstance().save();

            save();
            updateMaps();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Minecache data update failed!");
        }
    }
}
