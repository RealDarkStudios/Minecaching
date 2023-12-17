package net.realdarkstudios.minecaching.data;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

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
            String cName = yaml.getString(key + ".name");
            String type = yaml.getString(key + ".type");
            String author = yaml.getString(key + ".author");
            UUID cAuthor;
            String world = yaml.getString(key + ".world");
            World cWorld;
            String FTF = yaml.getString(key + ".ftf");
            UUID cFTF;
            int cX = yaml.getInt(key + ".x");
            int cY = yaml.getInt(key + ".y");
            int cZ = yaml.getInt(key + ".z");
            int cLX = yaml.getInt(key + ".lx");
            int cLY = yaml.getInt(key + ".ly");
            int cLZ = yaml.getInt(key + ".lz");
            String status = yaml.getString(key + ".status");
            LocalDateTime cHidden;
            String blockType = yaml.getString(key + ".blocktype");
            Material cBlockType;
            int cFinds = yaml.getInt(key + ".finds");
            boolean isInvalidated = false;

            MinecacheType cType;
            if (type == null) { cType = MinecacheType.TRADITIONAL; } else { cType = MinecacheType.get(type); }
            MinecacheStatus cStatus;
            if (status == null) { cStatus = MinecacheStatus.NEEDS_REVIEWED; } else { cStatus = MinecacheStatus.get(status); }
            try { cHidden = LocalDateTime.parse(yaml.getString(key + ".hidden")); } catch (Exception e) { cHidden = LocalDateTime.now(); isInvalidated = true; }
            try { cAuthor = UUID.fromString(author); } catch (Exception e) { cAuthor = Utils.EMPTY_UUID; isInvalidated = true; }
            try { cFTF = UUID.fromString(FTF); } catch (Exception e) { cFTF = Utils.EMPTY_UUID; isInvalidated = true; }
            try { cWorld = Bukkit.createWorld(new WorldCreator(world)); } catch (Exception e) { cWorld = null; isInvalidated = true; }
            try { cBlockType = Material.getMaterial(blockType); } catch (Exception e) { cBlockType = Material.AIR; isInvalidated = true; }
            if (cName == null || !key.startsWith("MC-") || key.length() < 8 || cFinds < 0) { isInvalidated = true; }
            Config cfg = Config.getInstance();
            if (cX > cfg.getMaxX() || cX < cfg.getMinX() || cY > cfg.getMaxY() || cY < cfg.getMinY() || cZ > cfg.getMaxZ() || cZ < cfg.getMinZ()
                || cLX > cfg.getMaxX() || cLX < cfg.getMinX() || cLY > cfg.getMaxY() || cLY < cfg.getMinY() || cLZ > cfg.getMaxZ() || cLZ < cfg.getMinZ()) {
                Minecaching.getInstance().getLogger().warning(String.format("%s is outside of the boundaries set in the config! The cache has been invalidated!", key));
                isInvalidated = true;
            }
            if (new Location(cWorld, cLX, cLY, cLZ).distance(new Location(cWorld, cX, cY, cZ)) > Config.getInstance().getMaxLodestoneDistance()) {
                Minecaching.getInstance().getLogger().warning("The lodestone coordinates are too far away! The cache has been invalidated");
                isInvalidated = true;
            }

            Minecache cache = new Minecache(key, cType, cName, cAuthor, cWorld, cX, cY, cZ, cLX, cLY, cLZ, cFTF, cStatus, cHidden, cBlockType, cFinds, isInvalidated);

            caches.add(cache);
            idMap.put(key, cache);
            locationMap.put(new Location(cWorld, cX, cY, cZ), cache);
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

        String key = minecache.id();
        yaml.set(key + ".type", minecache.type().toString());
        yaml.set(key + ".name", minecache.name());
        yaml.set(key + ".author", minecache.author().toString());
        yaml.set(key + ".ftf", minecache.ftf().toString());
        yaml.set(key + ".world", minecache.world().getName());
        yaml.set(key + ".x", minecache.x());
        yaml.set(key + ".y", minecache.y());
        yaml.set(key + ".z", minecache.z());
        yaml.set(key + ".lx", minecache.lx());
        yaml.set(key + ".ly", minecache.ly());
        yaml.set(key + ".lz", minecache.lz());
        yaml.set(key + ".status", minecache.status().getId());
        yaml.set(key + ".hidden", minecache.hidden().toString());
        yaml.set(key + ".blocktype", minecache.blockType().toString());
        yaml.set(key + ".finds", minecache.finds());

        updateMaps();
        save();
    }

    public void deleteMinecache(Minecache minecache) {
        yaml.set(minecache.id(), null);
        updateMaps();
        save();
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

    public boolean playerFindMinecache(Player plr, Minecache minecache) {
        boolean isFTF = false;
        if (minecache.ftf().equals(Utils.EMPTY_UUID)) {
            minecache.setFTF(plr.getUniqueId());
            isFTF = true;
        }

        updateMaps();
        save();

        return isFTF;
    }
}
