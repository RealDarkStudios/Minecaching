package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerDataObject {
    private final UUID uniqueID;
    private boolean banned;
    private ArrayList<String> finds, ftfs, hides;
    private Minecache newCache, editingCache;
    private YamlConfiguration yaml;
    private File file;

    public PlayerDataObject(UUID uniqueID, YamlConfiguration yaml, File file, boolean useEmptyMinecache) {
        this.uniqueID = uniqueID;
        this.banned = yaml.getBoolean("banned");
        ArrayList<String> yFtfs = new ArrayList<>(), yHides = new ArrayList<>(), yFinds = new ArrayList<>();

        if (yaml.getList("ftfs") != null) yFtfs.addAll((Collection<? extends String>) yaml.getList("ftfs"));
        if (yaml.getList("hides") != null) yHides.addAll((Collection<? extends String>) yaml.getList("hides"));
        if (yaml.getList("finds") != null) yFinds.addAll((Collection<? extends String>) yaml.getList("finds"));

        this.ftfs = yFtfs;
        this.hides = yHides;
        this.finds = yFinds;
        this.newCache = useEmptyMinecache ? Minecache.EMPTY : Minecache.fromYaml(yaml, "cache");
        this.editingCache = useEmptyMinecache ? Minecache.EMPTY : Minecache.fromYaml(yaml, "editing");
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
    public Minecache getEditingCache() {
        return editingCache;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public void addFind(String id) {
        if (!this.finds.contains(id)) this.finds.add(id);
        saveData();
    }

    public void removeFind(String id) {
        this.finds.remove(id);
        saveData();
    }

    public void addHide(String id) {
        if (!this.hides.contains(id)) this.hides.add(id);
        saveData();
    }

    public void removeHide(String id) {
        this.hides.remove(id);
        saveData();
    }

    public void addFTF(String id) {
        if (!this.ftfs.contains(id)) this.ftfs.add(id);
        saveData();
    }

    public void removeFTF(String id) {
        this.ftfs.remove(id);
        saveData();
    }

    public void setCache(Minecache newCache) {
        this.newCache = newCache;
        saveData();
    }

    public void setEditingCache(Minecache editingCache) {
        this.editingCache = editingCache;
        saveData();
    }

    public boolean isFTF(Minecache minecache) {
        return MinecacheStorage.getInstance().isFTF(uniqueID, minecache);
    }

    private void update() {
        ArrayList<String> yFtfs = new ArrayList<>(), yHides = new ArrayList<>(), yFinds = new ArrayList<>();

        if (yaml.getList("ftfs") != null) yFtfs.addAll((Collection<? extends String>) yaml.getList("ftfs"));
        if (yaml.getList("hides") != null) yHides.addAll((Collection<? extends String>) yaml.getList("hides"));
        if (yaml.getList("finds") != null) yFinds.addAll((Collection<? extends String>) yaml.getList("finds"));

        this.ftfs = yFtfs;
        this.hides = yHides;
        this.finds = yFinds;
        this.newCache = Minecache.fromYaml(yaml, "cache");
        this.editingCache = Minecache.fromYaml(yaml, "editing");

        newCache.setID(yaml.getString("cache_id") == null ? "NULL" : yaml.getString("cache_id"));
        editingCache.setID(yaml.getString("editing_id") == null ? "NULL" : yaml.getString("editing_id"));
    }

    public void saveData() {
        yaml.set("ftfs", this.ftfs);
        yaml.set("hides", this.hides);
        yaml.set("finds", this.finds);
        yaml.set("cache_id", this.newCache.id());
        newCache.toYaml(yaml, "cache");
        yaml.set("editing_id", this.editingCache.id());
        editingCache.toYaml(yaml, "editing");

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

    public static PlayerDataObject get(UUID uuid) {
        return get(uuid, false);
    }

    public static PlayerDataObject get(UUID uuid, boolean useEmptyMinecache) {
        File plrFile = new File(Minecaching.getInstance().getDataFolder() + "/player/" + uuid + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(plrFile);

        if (!plrFile.exists()) {
            try {
                plrFile.createNewFile();

                yaml.set("ftfs", List.of());
                yaml.set("hides", List.of());
                yaml.set("finds", List.of());
                Minecache.EMPTY.toYaml(yaml, "cache");
                Minecache.EMPTY.toYaml(yaml, "editing");
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Failed to make per-player file " + uuid + ".yml");
            }
        }

        return new PlayerDataObject(uuid, yaml, plrFile, useEmptyMinecache);
    }
}
