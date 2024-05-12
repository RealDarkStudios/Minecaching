package net.realdarkstudios.minecaching.api.player;

import me.scarsz.mojang.Mojang;
import me.scarsz.mojang.exception.ProfileFetchException;
import net.realdarkstudios.commons.menu.item.SkullMenuItem;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.log.LogType;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStorage;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.Notification;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class PlayerDataObject {
    private final UUID uniqueID;
    private boolean banned;
    private ArrayList<String> finds, ftfs, hides, favorites;
    private ArrayList<Notification> notifications;
    private String locatingId, logMessage, logCode;
    private Minecache creatingCache, editingCache;
    private YamlConfiguration yaml;
    private File file;
    private CacheListMenuOptions clmOptions;
    private LogType logType;
    private LocalDateTime cacheCooldownExpire;
    private ItemStack skullItemStack;

    public PlayerDataObject(UUID uniqueID, YamlConfiguration yaml, File file) {
        this.uniqueID = uniqueID;
        this.yaml = yaml;
        this.file = file;
    }

    public String getUsername() {
        if (getUniqueID().equals(MCUtils.EMPTY_UUID)) return "[CONSOLE]";

        try {
            if (getOfflinePlayer().hasPlayedBefore() && getOfflinePlayer().getName() != null) return getOfflinePlayer().getName();
            else return getGameProfile().getName();
        } catch (Exception e) {
            return getGameProfile().getName();
        }
    }

    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueID);
    }

    private Mojang.GameProfile getGameProfile() {
        try {
            return Mojang.fetch(uniqueID);
        } catch (ProfileFetchException e) {
            return new Mojang.GameProfile(uniqueID, getOfflinePlayer().getName(), List.of(), "");
        }
    }

    @Nullable
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
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

    public List<String> getFavorites() {
        return favorites;
    }
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void purgeNotifications() {
        this.notifications = new ArrayList<>();
        saveData();
    }

    public ItemStack getSkullItemStack() {
        return skullItemStack;
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

    public Minecache getLocatingCache() {
        return MinecachingAPI.get().getMinecache(locatingId);
    }

    public void setLocatingId(String locatingId) {
        this.locatingId = locatingId;
        saveData();
    }

    public void setLocating(Minecache locating) {
        this.locatingId = locating.id();
        saveData();
    }

    public void addFind(String id) {
        if (!this.finds.contains(id)) this.finds.add(id);
        saveData();
    }

    public void removeFind(String id) {
        this.finds.removeAll(Collections.singleton(id));
        saveData();
    }

    public void addHide(String id) {
        if (!this.hides.contains(id)) this.hides.add(id);
        saveData();
    }

    public void removeHide(String id) {
        this.hides.removeAll(Collections.singleton(id));
        saveData();
    }

    public void addFTF(String id) {
        if (!this.ftfs.contains(id)) this.ftfs.add(id);
        saveData();
    }

    public void removeFTF(String id) {
        this.ftfs.removeAll(Collections.singleton(id));
        saveData();
    }

    public void addFavorite(String id) {
        if (!this.favorites.contains(id)) this.favorites.add(id);
        saveData();
    }

    public void removeFavorite(String id) {
        this.favorites.removeAll(Collections.singleton(id));
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

    public CacheListMenuOptions getCLMOptions() {
        return clmOptions;
    }

    public void setCLMOptions(CacheListMenuOptions clmOptions) {
        this.clmOptions = clmOptions;
        saveData();
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
        saveData();
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
        saveData();
    }

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
        saveData();
    }

    public void setCacheCooldownExpire(LocalDateTime cacheCooldownExpire) {
        this.cacheCooldownExpire = cacheCooldownExpire;
        saveData();
    }

    public LocalDateTime getCacheCooldownExpireTime() {
        return cacheCooldownExpire != null ? cacheCooldownExpire : LocalDateTime.now();
    }

    public List<Minecache> filterCLMenuCaches(Player player) {
        List<Minecache> allCaches = MinecachingAPI.get().getAllKnownCaches();
        List<Minecache> finalCaches = new ArrayList<>();

        for (Minecache cache: allCaches) {
            if (clmOptions.favoritesOnly() && !favorites.contains(cache.id()))  {
                continue;
            }
            if (!clmOptions.getEnabledTypes().contains(cache.type())) {
                continue;
            }
            if (!clmOptions.getEnabledStatuses().contains(cache.status())) {
                continue;
            }
            if (clmOptions.ftfsOnly() && !cache.ftf().equals(MCUtils.EMPTY_UUID)) {
                continue;
            }
            if (cache.finds() < clmOptions.getMinFinds()) {
                continue;
            }
            if (!cache.world().equals(player.getWorld())) {
                continue;
            }
            if (clmOptions.getWithinBlocks() > cache.navLocation().distance(player.getLocation())) {
                continue;
            }

            finalCaches.add(cache);
        }

        if (clmOptions.newestFirst()) finalCaches.sort(Comparator.comparing(Minecache::hidden).reversed());
        else if (clmOptions.oldestFirst()) finalCaches.sort(Comparator.comparing(Minecache::hidden));
        else finalCaches.sort(Comparator.comparing(Minecache::favorites).reversed());

        return finalCaches;
    }

    public boolean isFTF(Minecache minecache) {
        return MinecacheStorage.getInstance().isFTF(uniqueID, minecache);
    }

    private void update() {
        ArrayList<String> yFtfs = new ArrayList<>(), yHides = new ArrayList<>(), yFinds = new ArrayList<>(), yFavs = new ArrayList<>();

        if (yaml.getList("ftfs") != null) yFtfs.addAll((Collection<? extends String>) yaml.getList("ftfs"));
        if (yaml.getList("hides") != null) yHides.addAll((Collection<? extends String>) yaml.getList("hides"));
        if (yaml.getList("finds") != null) yFinds.addAll((Collection<? extends String>) yaml.getList("finds"));
        if (yaml.getList("favorites") != null) yFavs.addAll((Collection<? extends String>) yaml.getList("favorites"));
        if (!yaml.contains("clm_options")) CacheListMenuOptions.DEFAULT_OPTIONS.toYaml(yaml, "clm_options");

        this.ftfs = yFtfs;
        this.hides = yHides;
        this.finds = yFinds;
        this.favorites = yFavs;
        this.locatingId = yaml.getString("locating_id") == null ? "NULL" : yaml.getString("locating_id");
        this.creatingCache = Minecache.fromYaml(yaml, "creating");
        this.editingCache = Minecache.fromYaml(yaml, "editing");
        this.clmOptions = CacheListMenuOptions.fromYaml(yaml, "clm_options");
        this.logType = LogType.get(yaml.getString("log_type") == null ? "found" : yaml.getString("log_type"));
        this.logMessage = yaml.getString("log_message") == null ? "" : yaml.getString("log_message");
        this.logCode = yaml.getString("log_code") == null ? "" : yaml.getString("log_code");

        ArrayList<Notification> yNotifs = new ArrayList<>();
        if (yaml.contains("notifications")) {
            for (String notificationID: yaml.getKeys(false).stream().filter(s -> s.startsWith("notifications.")).toList()) {
                yNotifs.add(Notification.fromYaml(yaml, notificationID));
            }
        }
        this.notifications = yNotifs;
        this.cacheCooldownExpire = yaml.contains("cache_cooldown_expire") ? LocalDateTime.parse(yaml.getString("cache_cooldown_expire")) : LocalDateTime.now();

        creatingCache.setID(yaml.getString("creating_id") == null ? "NULL" : yaml.getString("creating_id"));
        editingCache.setID(yaml.getString("editing_id") == null ? "NULL" : yaml.getString("editing_id"));

        try {
            if (uniqueID.equals(MCUtils.EMPTY_UUID)) {
                this.skullItemStack = new ItemStack(Material.PLAYER_HEAD);
            } else if (Mojang.fetch(uniqueID) == null) throw new ProfileFetchException(uniqueID.toString(), new Exception());
            else this.skullItemStack = new SkullMenuItem(getUsername(), getUniqueID(), List.of()).getSkull(getOfflinePlayer());
        } catch (ProfileFetchException e) {
            this.skullItemStack = new ItemStack(Material.PLAYER_HEAD);
        }
    }

    public void saveData() {
        yaml.set("ftfs", this.ftfs);
        yaml.set("hides", this.hides);
        yaml.set("finds", this.finds);
        yaml.set("favorites", this.favorites);
        yaml.set("locating_id", this.locatingId);
        yaml.set("creating_id", this.creatingCache.id());
        creatingCache.toYaml(yaml, "creating");
        yaml.set("editing_id", this.editingCache.id());
        editingCache.toYaml(yaml, "editing");
        clmOptions.toYaml(yaml, "clm_options");
        yaml.set("log_type", this.logType.getId());
        yaml.set("log_message", this.logMessage);
        yaml.set("log_code", this.logCode);

        if (!this.notifications.isEmpty()) {
            for (Notification notification : this.notifications) {
                notification.toYaml(yaml, "notifications." + notification.getId());
            }
        } else yaml.set("notifications", null);

        if (hasNotification("initiator")) yaml.set("notifications.initiator", null);
        if (hasNotification("type")) yaml.set("notifications.type", null);
        if (hasNotification("cache_id")) yaml.set("notifications.cache_id", null);
        if (hasNotification("time")) yaml.set("notifications.time", null);

        yaml.set("cache_cooldown_expire", this.cacheCooldownExpire.toString());

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
        save();
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateData() {
        try {
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
        } catch (Exception e) {
            return false;
        }
    }

    public static PlayerDataObject get(UUID uuid) {
        File plrFile = new File(Minecaching.getInstance().getDataFolder() + "/player/" + uuid + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(plrFile);

        if (!plrFile.exists()) {
            try {
                plrFile.createNewFile();

                yaml.set("ftfs", List.of());
                yaml.set("hides", List.of());
                yaml.set("finds", List.of());
                yaml.set("favorites", List.of());
                yaml.set("locating_id", "NULL");
                Minecache.EMPTY.toYaml(yaml, "creating");
                Minecache.EMPTY.toYaml(yaml, "editing");
                yaml.set("creating_id", "NULL");
                yaml.set("editing_id", "NULL");
                yaml.set("cache_cooldown_expire", LocalDateTime.now().toString());
                CacheListMenuOptions.DEFAULT_OPTIONS.toYaml(yaml, "clm_options");
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.CREATE_FILE, uuid + ".yml");
            }
        } else if (Config.getInstance().getPlayerDataVersion() < MinecachingAPI.PLAYER_DATA_VERSION) {
            try {
                if (!plrFile.canWrite()) throw new Exception();
                plrFile.createNewFile();
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.UPDATE_FILE, uuid + ".yml");
            }
        }

        return new PlayerDataObject(uuid, yaml, plrFile);
    }

    boolean delete() {
        return file.delete();
    }
}
