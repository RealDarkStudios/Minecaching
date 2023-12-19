package net.realdarkstudios.minecaching.data;

import net.realdarkstudios.minecaching.Minecaching;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStorage {

    private final static PlayerStorage INSTANCE = new PlayerStorage();

    private File file;
    private YamlConfiguration yaml;
    private HashMap<UUID, PlayerStorageObject> playerStorage;

    private PlayerStorage() {
    }

    public void load() {
        file = new File(Minecaching.getInstance().getDataFolder(), "players.yml");

        if (!file.exists()) {
            Minecaching.getInstance().saveResource("players.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMaps();

        if (playerStorage != null) {
            for (PlayerStorageObject plr : playerStorage.values()) {
                plr.load();
            }
        }
    }

    public void save() {
        if (playerStorage != null) {
            for (PlayerStorageObject plr : playerStorage.values()) {
                plr.save();
            }
        }

        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMaps() {
        HashMap<UUID, PlayerStorageObject> players = new HashMap<>();

        for (String key: (List<String>) yaml.getList("PLAYERS")) {
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Could not parse UUID " + key);
                continue;
            }

            PlayerStorageObject plr = PlayerStorageObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;
    }

    public void deletePlayerData(PlayerStorageObject plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(plr.getUniqueID().toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    public void deletePlayerData(Player plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(plr.getUniqueId().toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    public void deletePlayerData(UUID uuid) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(uuid.toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    public PlayerStorageObject createPlayerData(Player plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.add(plr.getUniqueId().toString());

        save();
        updateMaps();

        return getPlayerData(plr);
    }

    public PlayerStorageObject createPlayerData(UUID uuid) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.add(uuid.toString());

        save();
        updateMaps();

        return getPlayerData(uuid);
    }

    public boolean hasPlayerData(Player plr) {
        return playerStorage.containsKey(plr.getUniqueId());
    }

    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.containsKey(uuid);
    }

    public PlayerStorageObject getPlayerData(Player plr) {
        return playerStorage.get(plr.getUniqueId());
    }

    public PlayerStorageObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    public PlayerStorageObject getOrCreatePlayerData(Player plr) {
        if (hasPlayerData(plr)) return getPlayerData(plr);
        else return createPlayerData(plr);
    }

    public PlayerStorageObject getOrCreatePlayerData(UUID uuid) {
        if (hasPlayerData(uuid)) return getPlayerData(uuid);
        else return createPlayerData(uuid);
    }

    public void deleteMinecache(Minecache cache) {
        if (playerStorage != null) {
            for (PlayerStorageObject plr : playerStorage.values()) {
                plr.removeHide(cache.id());
                plr.removeFind(cache.id());
                plr.removeFTF(cache.id());
            }
        }
    }

    public static PlayerStorage getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            if (playerStorage != null) {
                for (PlayerStorageObject plr : playerStorage.values()) {
                    plr.attemptUpdate();
                }
            }

            Minecaching.getInstance().getLogger().info("Player update succeeded, updated from v" + Config.getInstance().getPlayerVersion() + " to v" + Minecaching.getInstance().PLAYER_DATA_VERSION);

            Config.getInstance().setPlayerVersion(Minecaching.getInstance().PLAYER_DATA_VERSION);
            if (playerStorage != null) {
                for (PlayerStorageObject plr : playerStorage.values()) {
                    plr.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Player update failed!");
        }
    }
}
