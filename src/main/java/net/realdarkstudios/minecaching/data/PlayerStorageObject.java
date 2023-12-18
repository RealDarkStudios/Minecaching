package net.realdarkstudios.minecaching.data;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class PlayerStorageObject {
    private final UUID uniqueID;
    private boolean banned;
    private List<String> finds, ftfs, hides;
    private Minecache newCache;
    private YamlConfiguration yaml;
    private File file;

    public PlayerStorageObject(UUID uniqueID, YamlConfiguration yaml, File file, boolean useEmptyMinecache) {
        this.uniqueID = uniqueID;
        this.banned = yaml.getBoolean("banned");
        this.ftfs = (List<String>) yaml.getList("ftfs");
        this.hides = (List<String>) yaml.getList("hides");
        this.finds = (List<String>) yaml.getList("finds");
        this.newCache = useEmptyMinecache ? Minecache.EMPTY : Minecache.fromYaml(yaml, "cache");
        this.yaml = yaml;
        this.file = file;
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public List<String> getFinds() {
        return finds;
    }

    public List<String> getHides() {
        return hides;
    }

    public List<String> getFTFs() {
        return ftfs;
    }

    public Minecache getCache() {
        return newCache;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public void addFind(String id) {
        if (!this.finds.contains(id)) this.finds.add(id);
    }

    public void removeFind(String id) {
        this.finds.remove(id);
    }

    public void addHide(String id) {
        if (!this.hides.contains(id)) this.hides.add(id);
    }

    public void removeHide(String id) {
        this.hides.remove(id);
    }

    public void addFTF(String id) {
        if (!this.ftfs.contains(id)) this.ftfs.add(id);
    }

    public void removeFTF(String id) {
        this.ftfs.remove(id);
    }

    public void setCache(Minecache newCache) {
        this.newCache = newCache;
    }

    public void findMinecache(Minecache minecache) {
        boolean newFtf = MinecacheStorage.getInstance().playerFindMinecache(uniqueID, minecache);
        addFind(minecache.id());
        if (newFtf) addFTF(minecache.id());
    }

    private void update() {
        this.ftfs = yaml.getList("ftfs").isEmpty() ? (List<String>) yaml.getList("ftfs") : List.of();
        this.hides = yaml.getList("hides").isEmpty() ? (List<String>) yaml.getList("hides") : List.of();
        this.finds = yaml.getList("finds").isEmpty() ? (List<String>) yaml.getList("finds") : List.of();
        this.newCache = Minecache.fromYaml(yaml, "cache");

        newCache.setID(yaml.getString("cache_id") == null ? "NULL" : yaml.getString("cache_id"));
    }

    public void saveData() {
        yaml.set("ftfs", this.ftfs);
        yaml.set("hides", this.hides);
        yaml.set("finds", this.finds);
        yaml.set("cache_id", this.newCache.id());
        newCache.toYaml(yaml, "cache");

        save();
        update();
    }

    public void load() {
        yaml = YamlConfiguration.loadConfiguration(file);
        yaml.options().parseComments(true);

        get(uniqueID);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        update();
    }



    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean attemptUpdate() {
        update();

        get(uniqueID);

        saveData();
        return true;
    }

    public static PlayerStorageObject get(UUID uuid) {
        return get(uuid, false);
    }

    public static PlayerStorageObject get(UUID uuid, boolean useEmptyMinecache) {
        File plrFile = new File(Minecaching.getInstance().getDataFolder() + "/player/" + uuid + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(plrFile);

        if (!plrFile.exists()) {
            try {
                plrFile.createNewFile();
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Failed to make per-player file " + uuid + ".yml");
            }
        }

        yaml.set("ftfs", List.of());
        yaml.set("hides", List.of());
        yaml.set("finds", List.of());
        Minecache.EMPTY.toYaml(yaml, "cache");

        return new PlayerStorageObject(uuid, yaml, plrFile, useEmptyMinecache);
    }
}
