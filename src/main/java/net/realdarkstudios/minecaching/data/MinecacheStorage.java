package net.realdarkstudios.minecaching.data;

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

    private void updateMaps() {
        ArrayList<Minecache> caches = new ArrayList<>(yaml.getKeys(false).size());
        HashMap<String, Minecache> idMap = new HashMap(yaml.getKeys(false).size());
        HashMap<Location, Minecache> locationMap = new HashMap<>(yaml.getKeys(false).size());

        for (String key : yaml.getKeys(false)) {
            Minecache cache = Minecache.fromYaml(yaml, key);

            caches.add(cache);
            idMap.put(key, cache);
            locationMap.put(new Location(cache.world(), cache.x(), cache.y(), cache.z()), cache);
        }

        caches.sort(this::compare);

        this.minecaches = caches;
        this.idToMinecache = Utils.sortByMinecache(idMap);
        this.mcLocations = locationMap;
    }

    public int compare(Minecache m1, Minecache m2) {
        return m1.hidden().compareTo(m2.hidden());
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMinecache(Minecache minecache, boolean isNewCache) {
        int tries = 0;

        while (idToMinecache.containsKey(minecache.id()) && isNewCache) {
            minecache.setID(Utils.generateID((int) (Math.floor((double) tries / 50) + 5)));
            tries++;
        }

        minecache.toYaml(yaml, minecache.id());

        save();
        updateMaps();
    }

    public void deleteMinecache(Minecache minecache) {
        yaml.set(minecache.id(), null);
        save();
        updateMaps();
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

    public boolean playerFindMinecache(UUID plr, Minecache minecache) {
        boolean isFTF = false;
        if (minecache.ftf().equals(Utils.EMPTY_UUID)) {
            minecache.setFTF(plr);
            isFTF = true;
        }

        save();
        updateMaps();

        return isFTF;
    }

    public void attemptUpdate() {
        try {
            updateMaps();

            Minecaching.getInstance().saveResource("minecaches.yml", false);

            for (Minecache cache : minecaches) {
                cache.toYaml(yaml, cache.id());
            }

            Minecaching.getInstance().getLogger().info("Minecache update succeeded, updated from v" + Config.getInstance().getMinecacheVersion() + " to v" + Minecaching.getInstance().MINECACHE_DATA_VERSION);

            Config.getInstance().setMinecacheVersion(Minecaching.getInstance().MINECACHE_DATA_VERSION);

            save();
            updateMaps();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Minecache update failed!");
        }
    }
}
