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
        for (PlayerStorageObject plr: playerStorage.values()) {
            plr.load();
        }
    }

    public void save() {
        for (PlayerStorageObject plr: playerStorage.values()) {
            plr.save();
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

            YamlConfiguration fileYaml = new YamlConfiguration();
            fileYaml.options().parseComments(true);

            File plrFile = new File(Minecaching.getInstance().getDataFolder() + "player/" + key + ".yml");
            if (plrFile.exists()) {
                Minecaching.getInstance().saveResource("player/base.yml", false);
                File baseFile = new File(Minecaching.getInstance().getDataFolder() + "player/base.yml");
                boolean success = baseFile.renameTo(plrFile);
                if (!success) {
                    Minecaching.getInstance().getLogger().warning("Failed to make per-player file " + key + ".yml");
                    continue;
                }
                plrFile = baseFile;
            }

            PlayerStorageObject plr = new PlayerStorageObject(uuid, fileYaml, plrFile);

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

    public void createPlayerData(Player plr) {
        List<String> plrs = (List<String>) yaml.get("PLAYERS");
        plrs.add(plr.getUniqueId().toString());

        save();
        updateMaps();
    }

    public boolean hasPlayerData(Player plr) {
        return playerStorage.containsKey(plr.getUniqueId());
    }

    public PlayerStorageObject getPlayerData(Player plr) {
        return playerStorage.get(plr.getUniqueId());
    }

    public PlayerStorageObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    public static PlayerStorage getInstance() {
        return INSTANCE;
    }

    public void attemptUpdate() {
        try {
            for (PlayerStorageObject plr : playerStorage.values()) {
                plr.attemptUpdate();
            }

            Minecaching.getInstance().getLogger().info("Player update succeeded, updated from v" + Config.getInstance().getPlayerVersion() + "to v" + Minecaching.getInstance().PLAYER_DATA_VERSION);

            Config.getInstance().setPlayerVersion(Minecaching.getInstance().PLAYER_DATA_VERSION);
        } catch (Exception e) {
            Minecaching.getInstance().getLogger().warning("Player update failed!");
        }
    }
}
