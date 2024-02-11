package net.realdarkstudios.minecaching.api.player;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.Notification;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
    private ArrayList<Notification> notifications;
    private String locatingId;
    private Minecache creatingCache, editingCache;
    private YamlConfiguration yaml;
    private File file;

    public PlayerDataObject(UUID uniqueID, YamlConfiguration yaml, File file, boolean useEmptyMinecache) {
        this.uniqueID = uniqueID;
        this.banned = yaml.getBoolean("banned");
        ArrayList<String> yFtfs = new ArrayList<>(), yHides = new ArrayList<>(), yFinds = new ArrayList<>();

        if (yaml.contains("ftfs")) yFtfs.addAll((Collection<? extends String>) yaml.getList("ftfs"));
        if (yaml.contains("hides")) yHides.addAll((Collection<? extends String>) yaml.getList("hides"));
        if (yaml.contains("finds")) yFinds.addAll((Collection<? extends String>) yaml.getList("finds"));

        this.ftfs = yFtfs;
        this.hides = yHides;
        this.finds = yFinds;
        this.locatingId = yaml.getString("locating_id") == null ? "NULL" : yaml.getString("locating_id");
        this.creatingCache = useEmptyMinecache ? Minecache.EMPTY : Minecache.fromYaml(yaml, "cache");
        this.editingCache = useEmptyMinecache ? Minecache.EMPTY : Minecache.fromYaml(yaml, "editing");

        ArrayList<Notification> yNotifs = new ArrayList<>();
        if (yaml.contains("notifications")) {
            for (String notificationID: yaml.getKeys(true).stream().filter(s -> s.startsWith("notifications.")).toList()) {
                yNotifs.add(Notification.fromYaml(yaml, notificationID));
            }
        }
        this.notifications = yNotifs;

        this.yaml = yaml;
        this.file = file;
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uniqueID);
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
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void purgeNotifications() {
        this.notifications = new ArrayList<>();
        saveData();
    }

    public Minecache getCreatingCache() {
        return creatingCache;
    }
    public Minecache getEditingCache() {
        return editingCache;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public String getLocatingId() {
        return locatingId;
    }

    public void setLocatingId(String locatingId) {
        this.locatingId = locatingId;
        saveData();
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

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        saveData();
    }

    public boolean hasNotification(String id) {
        return this.yaml.contains("notifications." + id);
    }

    public void setCreatingCache(Minecache creatingCache) {
        this.creatingCache = creatingCache;
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
        this.locatingId = yaml.getString("locating_id") == null ? "NULL" : yaml.getString("locating_id");
        this.creatingCache = Minecache.fromYaml(yaml, "creating");
        this.editingCache = Minecache.fromYaml(yaml, "editing");

        ArrayList<Notification> yNotifs = new ArrayList<>();
        if (yaml.contains("notifications")) {
            for (String notificationID: yaml.getKeys(true).stream().filter(s -> s.startsWith("notifications.")).toList()) {
                yNotifs.add(Notification.fromYaml(yaml, notificationID));
            }
        }
        this.notifications = yNotifs;

        creatingCache.setID(yaml.getString("creating_id") == null ? "NULL" : yaml.getString("creating_id"));
        editingCache.setID(yaml.getString("editing_id") == null ? "NULL" : yaml.getString("editing_id"));
    }

    public void saveData() {
        yaml.set("ftfs", this.ftfs);
        yaml.set("hides", this.hides);
        yaml.set("finds", this.finds);
        yaml.set("locating_id", this.locatingId);
        yaml.set("creating_id", this.creatingCache.id());
        creatingCache.toYaml(yaml, "creating");
        yaml.set("editing_id", this.editingCache.id());
        editingCache.toYaml(yaml, "editing");

        if (!this.notifications.isEmpty()) {
            for (Notification notification : this.notifications) {
                notification.toYaml(yaml, "notifications." + notification.getId());
            }
        } else yaml.set("notifications", null);

        if (hasNotification("initiator")) yaml.set("notifications.initiator", null);
        if (hasNotification("type")) yaml.set("notifications.type", null);
        if (hasNotification("cache_id")) yaml.set("notifications.cache_id", null);
        if (hasNotification("time")) yaml.set("notifications.time", null);

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

    public boolean updateData() {
        update();

        // Accounts for the renaming from cache/cache_id -> creating/creating_id
        if (yaml.contains("cache")) {
            yaml.set("creating", yaml.get("cache"));
            yaml.set("cache", null);
        }

        if (yaml.contains("cache_id")) {
            yaml.set("creating_id", yaml.getString("cache_id"));
            yaml.set("cache_id", null);
        }

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
                yaml.set("locating_id", "NULL");
                Minecache.EMPTY.toYaml(yaml, "creating");
                Minecache.EMPTY.toYaml(yaml, "editing");
                yaml.set("creating_id", "NULL");
                yaml.set("editing_id", "NULL");
            } catch (Exception e) {
                MinecachingAPI.tWarning("error.plugin.createfile", uuid + ".yml");
            }
        } else if (Config.getInstance().getPlayerDataVersion() != MinecachingAPI.getPlayerDataVersion()) {
            try {
                if (!plrFile.canWrite()) throw new Exception();
                plrFile.createNewFile();
            } catch (Exception e) {
                MinecachingAPI.tWarning("error.plugin.updatefile", uuid + ".yml");
            }
        }

        return new PlayerDataObject(uuid, yaml, plrFile, useEmptyMinecache);
    }
}
