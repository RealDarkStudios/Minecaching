package net.realdarkstudios.minecaching.api;

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
    private HashMap<UUID, PlayerDataObject> playerStorage;

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
            for (PlayerDataObject plr : playerStorage.values()) {
                plr.load();
            }
        }
    }

    public void save() {
        if (playerStorage != null) {
            for (PlayerDataObject plr : playerStorage.values()) {
                plr.save();
            }
        }

        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateMaps() {
        HashMap<UUID, PlayerDataObject> players = new HashMap<>();

        for (String key: (List<String>) yaml.getList("PLAYERS")) {
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                Minecaching.getInstance().getLogger().warning("Could not parse UUID " + key);
                continue;
            }

            PlayerDataObject plr = PlayerDataObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;
    }

    void deletePlayerData(PlayerDataObject plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(plr.getUniqueID().toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    void deletePlayerData(Player plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(plr.getUniqueId().toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    void deletePlayerData(UUID uuid) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.removeAll(Collections.singleton(uuid.toString()));

        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();
    }

    PlayerDataObject createPlayerData(Player plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.add(plr.getUniqueId().toString());

        save();
        updateMaps();

        return getPlayerData(plr);
    }

    PlayerDataObject createPlayerData(UUID uuid) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.add(uuid.toString());

        save();
        updateMaps();

        return getPlayerData(uuid);
    }

    boolean hasPlayerData(Player plr) {
        return playerStorage.containsKey(plr.getUniqueId());
    }

    boolean hasPlayerData(UUID uuid) {
        return playerStorage.containsKey(uuid);
    }

    PlayerDataObject getPlayerData(Player plr) {
        return playerStorage.get(plr.getUniqueId());
    }

    PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    PlayerDataObject getOrCreatePlayerData(Player plr) {
        if (hasPlayerData(plr)) return getPlayerData(plr);
        else return createPlayerData(plr);
    }

    PlayerDataObject getOrCreatePlayerData(UUID uuid) {
        if (hasPlayerData(uuid)) return getPlayerData(uuid);
        else return createPlayerData(uuid);
    }

    boolean deleteMinecache(Minecache cache) {
        try {
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.removeHide(cache.id());
                    plr.removeFind(cache.id());
                    plr.removeFTF(cache.id());
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static PlayerStorage getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.attemptUpdate();
                }
            }

            Minecaching.getInstance().getLogger().info("Player data update succeeded, updated from v" + Config.getInstance().getPlayerDataVersion() + " to v" + Minecaching.getInstance().PLAYER_DATA_VERSION);

            Config.getInstance().setPlayerDataVersion(Minecaching.getInstance().PLAYER_DATA_VERSION);
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Player data update failed!");
        }
    }
}