package net.realdarkstudios.minecaching.data;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStorage {

    private final static PlayerStorage INSTANCE = new PlayerStorage();

    private File file;
    private YamlConfiguration yaml;

    private PlayerStorage() {
    }

    public void load() {
        file = new File(Minecaching.getInstance().getDataFolder(), "player.yml");

        if (!file.exists()) {
            Minecaching.getInstance().saveResource("player.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPlayerHides(Player plr) {
        return yaml.getInt(plr.getUniqueId() + ".hides");
    }

    public void setPlayerHides(Player plr, int hides) {
        yaml.set(plr.getUniqueId() + ".hides", hides);
    }

    public List<String> getPlayerFinds(Player plr) {
        return (List<String>) yaml.getList(plr.getUniqueId() + ".finds");
    }

    public int getFtfs(Player plr) {
        return yaml.getInt(plr.getUniqueId() + ".ftfs");
    }

    public void findMinecache(Player plr, Minecache minecache) {
        String key = plr.getUniqueId().toString();

        boolean newFtf = MinecacheStorage.getInstance().playerFindMinecache(plr, minecache);

        yaml.set(key + ".finds", getPlayerFinds(plr).add(minecache.id()));
        yaml.set(key + ".ftfs", newFtf ? getFtfs(plr) + 1 : getFtfs(plr));
    }

    private final HashMap<UUID, Minecache> tempCaches = new HashMap<>();

    // For storing minecaches during creation
    public void setTempMinecache(UUID sender, Minecache minecache) {
        tempCaches.put(sender, minecache);
    }

    public Minecache getTempMinecache(UUID sender) {
        return tempCaches.get(sender);
    }

    public static PlayerStorage getInstance() {
        return INSTANCE;
    }

}
