package net.realdarkstudios.minecaching.api.player;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStorage {
    private HashMap<UUID, PlayerDataObject> playerStorage = new HashMap<>();

    private PlayerStorage() {
    }

    public void load() {
        File file = new File(Minecaching.getInstance().getDataFolder() + "/player/");
        if (!file.exists()) file.mkdirs();

        File[] plrFiles = file.listFiles(File::isFile);
        if (plrFiles == null || plrFiles.length == 0) MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.PLAYER_LIST_EMPTY);

        HashMap<UUID, PlayerDataObject> players = new HashMap<>();
        for (File plrFile: plrFiles) {
            String key = plrFile.getName().replace(".yml", "");
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.PARSE_UUID, key);
                continue;
            }

            PlayerDataObject plr = PlayerDataObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;

        for (PlayerDataObject plr : playerStorage.values()) {
            plr.load();
        }

        MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.LOADED_PLAYERS, playerStorage.size());
    }

    public void save() {
        if (playerStorage != null) {
            for (PlayerDataObject plr : playerStorage.values()) {
                plr.save();
            }
        }
    }

    public void updateMaps() {
        HashMap<UUID, PlayerDataObject> players = new HashMap<>();
        File file = new File(Minecaching.getInstance().getDataFolder() + "/player/");
        if (!file.exists()) file.mkdirs();

        File[] plrFiles = file.listFiles(File::isFile);
        if (plrFiles == null || plrFiles.length == 0) MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.PLAYER_LIST_EMPTY);

        for (File plrFile: plrFiles) {
            String key = plrFile.getName().replace(".yml", "");
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (Exception e) {
                MinecachingAPI.tWarning(MCMessageKeys.Error.Misc.PARSE_UUID, key);
                continue;
            }

            PlayerDataObject plr = PlayerDataObject.get(uuid);

            plr.load();
            players.put(uuid, plr);
        }

        this.playerStorage = players;
    }

    public int totalFinds() {
        int finds = 0;

        for (PlayerDataObject pdo: getPlayers()) {
            finds += pdo.getFinds().size();
        }

        return finds;
    }

    public boolean deletePlayerData(PlayerDataObject plr) {
        return deletePlayerData(plr.getUniqueID());
    }

    public boolean deletePlayerData(OfflinePlayer plr) {
        return deletePlayerData(plr.getUniqueId());
    }

    public boolean deletePlayerData(UUID uuid) {
        try {

            playerStorage.get(uuid).delete();

            save();
            updateMaps();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PlayerDataObject createPlayerData(OfflinePlayer plr) {
        return createPlayerData(plr.getUniqueId());
    }

    public PlayerDataObject createPlayerData(UUID uuid) {
        PlayerDataObject pdo = PlayerDataObject.get(uuid);
        MinecachingAPI.tInfo(MCMessageKeys.Plugin.NEW_PLAYER_DATA, uuid);

        save();
        updateMaps();

        return pdo;
    }

    public boolean hasPlayerData(OfflinePlayer plr) {
        return hasPlayerData(plr.getUniqueId());
    }

    public boolean hasPlayerData(UUID uuid) {
        return playerStorage.containsKey(uuid);
    }

    public PlayerDataObject getPlayerData(OfflinePlayer plr) {
        return getPlayerData(plr.getUniqueId());
    }

    public PlayerDataObject getPlayerData(UUID uuid) {
        return playerStorage.get(uuid);
    }

    public PlayerDataObject getOrCreatePlayerData(OfflinePlayer plr) {
        return getOrCreatePlayerData(plr.getUniqueId());
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
        if (MinecachingAPI.get().hasInitialized()) return MinecachingAPI.getPlayerStorage();
        else return new PlayerStorage();
    }

    public void updateData() {
        try {
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.updateData();
                }
            }

            MinecachingAPI.tWarning(MCMessageKeys.Plugin.Data.UPDATE_SUCCEEDED, "Player Data", Config.getInstance().getPlayerDataVersion(), MinecachingAPI.PLAYER_DATA_VERSION);

            Config.getInstance().setPlayerDataVersion(MinecachingAPI.PLAYER_DATA_VERSION);
            if (playerStorage != null) {
                for (PlayerDataObject plr : playerStorage.values()) {
                    plr.save();
                }
            }
            Config.getInstance().save();
        } catch (Exception e) {
            MinecachingAPI.tWarning(MCMessageKeys.Plugin.Data.UPDATE_FAILED, "Player Data", Config.getInstance().getPlayerDataVersion(), MinecachingAPI.PLAYER_DATA_VERSION);
        }
    }
}
