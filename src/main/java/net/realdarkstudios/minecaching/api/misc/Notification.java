package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.util.Utils;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    public static final Notification EMPTY = new Notification("100000", Utils.EMPTY_UUID, NotificationType.INVALID, Minecache.EMPTY, LocalDateTime.now());

    private final String id;
    private final UUID initiator;
    private final NotificationType type;
    private final Minecache minecache;
    private final LocalDateTime time;
    public Notification(String id, UUID initiator, NotificationType type, Minecache minecache, LocalDateTime time) {
        this.id = id;
        this.initiator = initiator;
        this.type = type;
        this.minecache = minecache;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public UUID getInitiator() {
        return initiator;
    }

    public NotificationType getType() {
        return type;
    }

    public Minecache getCache() {
        return minecache;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public static Notification fromYaml(YamlConfiguration yaml, String key) {
        boolean isInvalidated = false;
        String initiator = yaml.getString(key + ".initiator");
        UUID nInitiator;
        String type = yaml.getString(key + ".type");
        LocalDateTime nTime;
        String cacheID = yaml.getString(key + ".cache_id");
        Minecache nCache;
        NotificationType nType;
        if (type == null) { nType = NotificationType.INVALID; isInvalidated = true; } else { nType = NotificationType.get(type); }
        if (cacheID == null) { nCache = Minecache.EMPTY; isInvalidated = true; } else { nCache = MinecachingAPI.get().getMinecache(cacheID); }
        try { nInitiator = UUID.fromString(initiator); } catch (Exception e) { nInitiator = Utils.EMPTY_UUID; isInvalidated = true; }
        try { nTime = LocalDateTime.parse(yaml.getString(key + ".time")); } catch (Exception e) { nTime = LocalDateTime.now(); isInvalidated = true; }
        String[] nID = key.split("\\.");

        return new Notification(nID[nID.length - 1], nInitiator, nType, nCache, nTime);
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key + ".initiator", initiator.toString());
        yaml.set(key + ".type", type.getId());
        yaml.set(key + ".cache_id", minecache.id());
        yaml.set(key + ".time", time.toString());
    }
}
