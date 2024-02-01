package net.realdarkstudios.minecaching.api.player;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

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

    public void updateMaps() {
        HashMap<UUID, PlayerDataObject> players = new HashMap<>();

        if (yaml.getList("PLAYERS") == null) MinecachingAPI.warning("Player list is empty!");
        else {
            for (String key: (List<String>) yaml.get("PLAYERS")) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(key);
                } catch (Exception e) {
                    MinecachingAPI.tWarning("error.plugin.parseuuid", key);
                    continue;
                }

                PlayerDataObject plr = PlayerDataObject.get(uuid);

                plr.load();
                players.put(uuid, plr);
            }
        }

        this.playerStorage = players;
    }

    public boolean deletePlayerData(PlayerDataObject plr) {
        try {
            List<String> plrs = yaml.get("PLAYERS") == null ? new ArrayList<>() : (List<String>) yaml.get("PLAYERS");
            plrs.removeAll(Collections.singleton(plr.getUniqueID().toString()));

            yaml.set("PLAYERS", plrs);

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deletePlayerData(Player plr) {
        try {
            List<String> plrs = yaml.get("PLAYERS") == null ? new ArrayList<>() : (List<String>) yaml.get("PLAYERS");
            plrs.removeAll(Collections.singleton(plr.getUniqueId().toString()));

            yaml.set("PLAYERS", plrs);

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deletePlayerData(UUID uuid) {
        try {
            List<String> plrs = yaml.get("PLAYERS") == null ? new ArrayList<>() : (List<String>) yaml.get("PLAYERS");
            plrs.removeAll(Collections.singleton(uuid.toString()));

            yaml.set("PLAYERS", plrs);

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PlayerDataObject createPlayerData(Player plr) {
        List<String> plrs = yaml.get("PLAYERS") == null ? new ArrayList<>() : (List<String>) yaml.get("PLAYERS");
        plrs.add(plr.getUniqueId().toString());
        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();

        return PlayerDataObject.get(plr.getUniqueId());
    }

    public PlayerDataObject createPlayerData(UUID uuid) {
        List<String> plrs = yaml.get("PLAYERS") == null ? new ArrayList<>() : (List<String>) yaml.get("PLAYERS");
        plrs.add(uuid.toString());
        yaml.set("PLAYERS", plrs);

        save();
        updateMaps();

        return PlayerDataObject.get(uuid);
    }

    public boolean hasPlayerData(Player plr) {
        return playerStorage.containsKey(plr.getUniqueId());
    }

    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.containsKey(uuid);
    }

    public PlayerDataObject getPlayerData(Player plr) {
        return playerStorage.get(plr.getUniqueId());
    }

    public PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    public PlayerDataObject getOrCreatePlayerData(Player plr) {
        if (hasPlayerData(plr)) return getPlayerData(plr);
        else return createPlayerData(plr);
    }

    public PlayerDataObject getOrCreatePlayerData(UUID uuid) {
        if (hasPlayerData(uuid)) return getPlayerData(uuid);
        else return createPlayerData(uuid);
    }

    public List<PlayerDataObject> getPlayers() {
        return playerStorage.values().stream().toList();
    }

    public boolean deleteMinecache(Minecache cache) {
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

            MinecachingAPI.tWarning("plugin.data.update.succeed", "Player Data", Config.getInstance().getPlayerDataVersion(), MinecachingAPI.getPlayerDataVersion());

            Config.getInstance().setPlayerDataVersion(MinecachingAPI.getPlayerDataVersion());
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            MinecachingAPI.tWarning("plugin.data.update.fail", "Player Data", Config.getInstance().getPlayerDataVersion(), MinecachingAPI.getPlayerDataVersion());
        }
    }
}
