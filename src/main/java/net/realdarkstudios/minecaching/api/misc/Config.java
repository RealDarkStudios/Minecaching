package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.checkerframework.common.value.qual.IntRange;
import org.joml.Math;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Config {
    private File file;
    private YamlConfiguration yaml;

    Config() {
    }

    public void load() {
        file = new File(Minecaching.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            Minecaching.getInstance().saveResource("config.yml", false);
        }

        yaml = new YamlConfiguration();
        yaml.options().parseComments(true);

        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // try to ensure types are lowercased
        setEnabledTypes(getEnabledTypes());
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getConfigVersion() {
        return yaml.getInt("CONFIG_VERSION");
    }

    public int getMinecacheDataVersion() {
        return yaml.getInt("MINECACHE_DATA_VERSION");
    }

    public void setMinecacheDataVersion(int version) {
        yaml.set("MINECACHE_DATA_VERSION", version);
    }

    public int getPlayerDataVersion() {
        return yaml.getInt("PLAYER_DATA_VERSION");
    }

    public void setPlayerDataVersion(int version) {
        yaml.set("PLAYER_DATA_VERSION", version);
    }

    public int getLogbookDataVersion() {
        return yaml.getInt("LOGBOOK_DATA_VERSION");
    }

    public void setLogbookDataVersion(int version) {
        yaml.set("LOGBOOK_DATA_VERSION", version);
    }

    public Locale getServerLocale() {
        return Locale.forLanguageTag(yaml.getString("LOCALE", "en-US"));
    }

    public boolean autoUpdate() {
        return yaml.getBoolean("AUTO_UPDATE", false);
    }

    public void setAutoUpdate(boolean autoUpdate) {
        yaml.set("AUTO_UPDATE", autoUpdate);
        save();
    }

    public String getUpdateBranch() {
        return yaml.getString("UPDATE_BRANCH", "release").equals("snapshot") ? "snapshot" : "release";
    }

    public void setUpdateBranch(String branch) {
        yaml.set("UPDATE_BRANCH", branch.equals("snapshot") ? "snapshot" : branch.equals("release") ? "release" : getUpdateBranch());
        save();
    }

    public boolean experimentalFeatures() {
        return yaml.getBoolean("EXPERIMENTAL_FEATURES", false);
    }

    public boolean debugEvents() {
        return yaml.getBoolean("DEBUG_EVENTS", false);
    }

    public void setDebugEvents(boolean debugEvents) {
        yaml.set("DEBUG_EVENTS", debugEvents);
        save();
    }

    public int getDebugEventsLevel() {
        return Math.clamp(0, 2, yaml.getInt("DEBUG_EVENTS_LEVEL", 0));
    }

    public void setDebugEventsLevel(@IntRange(from = 0, to = 2) int level) {
        yaml.set("DEBUG_EVENTS_LEVEL", level);
        save();
    }

    public void setCacheBounds(BoundingBox bounds) {
        yaml.set("MIN_X", (int) bounds.getMinX());
        yaml.set("MIN_Y", (int) bounds.getMinY());
        yaml.set("MIN_Z", (int) bounds.getMinZ());
        yaml.set("MAX_X", (int) bounds.getMaxX());
        yaml.set("MAX_Y", (int) bounds.getMaxY());
        yaml.set("MAX_Z", (int) bounds.getMaxZ());
        save();
    }

    public BoundingBox getCacheBounds() {
        return BoundingBox.of(getMinLocation(), getMaxLocation());
    }

    public Vector getMinLocation() {
        return new Vector(getMinX(), getMinY(), getMinZ());
    }

    public Vector getMaxLocation() {
        return new Vector(getMaxX(), getMaxY(), getMaxZ());
    }
    public int getMaxX() {
        return yaml.getInt("MAX_X", 30000000);
    }

    public int getMinX() {
        return yaml.getInt("MIN_X", -30000000);
    }

    public int getMaxY() {
        return yaml.getInt("MAX_Y", 319);
    }

    public int getMinY() {
        return yaml.getInt("MIN_Y", -64);
    }

    public int getMaxZ() {
        return yaml.getInt("MAX_Z", 30000000);
    }

    public int getMinZ() {
        return yaml.getInt("MIN_Z", -30000000);
    }

    public int getMinCacheDistance() {
        return yaml.getInt("MIN_CACHE_DISTANCE", 25);
    }

    public void setMinCacheDistance(@IntRange(from = 0, to = 10000) int minCacheDistance) {
        yaml.set("MIN_CACHE_DISTANCE", minCacheDistance);
        save();
    }

    public int getMaxLodestoneDistance() {
        return yaml.getInt("MAX_LODESTONE_DISTANCE", 50);
    }
    public void setMaxLodestoneDistance(@IntRange(from = 0, to = 10000) int maxLodestoneDistance) {
        yaml.set("MAX_LODESTONE_DISTANCE", maxLodestoneDistance);
        save();
    }

    public int getFindLodestoneDistance() {
        return yaml.getInt("FIND_LODESTONE_DISTANCE", 25);
    }
    public void setFindLodestoneDistance(@IntRange(from = 0, to = 10000) int findLodestoneDistance) {
        yaml.set("FIND_LODESTONE_DISTANCE", findLodestoneDistance);
        save();
    }

    public boolean useLodestoneBasedLocating() {
        return yaml.getBoolean("USE_LODESTONE_BASED_LOCATING", false);
    }

    public int getCacheCreateCooldown() {
        return yaml.getInt("CACHE_CREATE_COOLDOWN", 300);
    }

    public void setCacheCreateCooldown(int cooldown) {
        yaml.set("CACHE_CREATE_COOLDOWN", cooldown);
        save();
    }

    public List<String> getEnabledTypes() {
        List<String> enabledTypes;

        try {
            enabledTypes = (List<String>) yaml.getList("ENABLED_TYPES", List.of("traditional", "mystery", "multi"));
        } catch (Exception e) {
            enabledTypes = List.of("traditional", "mystery", "multi");
        }

        enabledTypes.replaceAll(String::toLowerCase);
        enabledTypes.removeAll(List.of("default", "current"));

        return enabledTypes;
    }

    public void setEnabledTypes(List<String> types) {
        types.replaceAll(String::toLowerCase);
        types.removeAll(List.of("default", "current"));
        yaml.set("ENABLED_TYPES", types);
        save();
    }

    public void modifyType(MinecacheType type, boolean enable) {
        ArrayList<String> enabledTypes = new ArrayList<>(getEnabledTypes());

        if (enabledTypes.contains(type.getId()) && !enable) {
            enabledTypes.removeAll(Collections.singleton(type.getId()));
        } else if (!enabledTypes.contains(type.getId()) && enable) {
            enabledTypes.add(type.getId());
        }

        setEnabledTypes(enabledTypes);
        save();
    }

    public void setStatsScoreOptions(StatsScoreOptions sso) {
        sso.toYaml(yaml, "STATS_SCORE_OPTIONS");
        save();
    }

    public StatsScoreOptions getStatsScoreOptions() {
        return yaml.contains("STATS_SCORE_OPTIONS") ? StatsScoreOptions.fromYaml(yaml, "STATS_SCORE_OPTIONS") : StatsScoreOptions.DEFAULT_OPTIONS;
    }

    public void updateData() {
        try {
            int configVersion = getConfigVersion();
            int minecacheDataVersion = getMinecacheDataVersion();
            int playerDataVersion = getPlayerDataVersion();
            int logbookDataVersion = getLogbookDataVersion();

            Locale serverLocale = getServerLocale();

            boolean autoUpdate = autoUpdate();
            String updateBranch = getUpdateBranch();
            boolean experimentalFeatures = experimentalFeatures();
            boolean debugEvents = debugEvents();
            int debugEventsLevel = getDebugEventsLevel();

            int maxY = getMaxY();
            int minY = getMinY();
            int maxX = getMaxX();
            int minX = getMinX();
            int maxZ = getMaxZ();
            int minZ = getMinZ();
            int minCacheDistance = getMinCacheDistance();

            int maxLodestoneDistance = getMaxLodestoneDistance();
            int findLodestoneDistance = getFindLodestoneDistance();
            boolean useLodestoneBasedLocating = useLodestoneBasedLocating();

            int cacheCreateCooldown = getCacheCreateCooldown();

            List<String> enabledTypes = getEnabledTypes();
            enabledTypes.replaceAll(String::toLowerCase);

            StatsScoreOptions statsScoreOptions = getStatsScoreOptions();

            // Goes back to the empty file, therefore we need to get all values above
            Minecaching.getInstance().saveResource("config.yml", true);
            load();

            yaml.set("MINECACHE_DATA_VERSION", minecacheDataVersion);
            yaml.set("PLAYER_DATA_VERSION", playerDataVersion);
            yaml.set("LOGBOOK_DATA_VERSION", logbookDataVersion);
            yaml.set("LOCALE", serverLocale.toLanguageTag());
            yaml.set("AUTO_UPDATE", autoUpdate);
            yaml.set("UPDATE_BRANCH", updateBranch);
            yaml.set("EXPERIMENTAL_FEATURES", experimentalFeatures);
            yaml.set("DEBUG_EVENTS", debugEvents);
            yaml.set("DEBUG_EVENTS_LEVEL", debugEventsLevel);
            yaml.set("MAX_Y", maxY);
            yaml.set("MIN_Y", minY);
            yaml.set("MAX_X", maxX);
            yaml.set("MIN_X", minX);
            yaml.set("MAX_Z", maxZ);
            yaml.set("MIN_Z", minZ);
            yaml.set("MIN_CACHE_DISTANCE", minCacheDistance);
            yaml.set("MAX_LODESTONE_DISTANCE", maxLodestoneDistance);
            yaml.set("FIND_LODESTONE_DISTANCE", findLodestoneDistance);
            yaml.set("USE_LODESTONE_BASED_LOCATING", useLodestoneBasedLocating);
            yaml.set("CACHE_CREATE_COOLDOWN", cacheCreateCooldown);
            yaml.set("ENABLED_TYPES", enabledTypes);
            statsScoreOptions.toYaml(yaml, "STATS_SCORE_OPTIONS");
            yaml.set("CONFIG_VERSION", MinecachingAPI.CONFIG_DATA_VERSION);
            MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.UPDATE_SUCCEEDED, "Config", configVersion, MinecachingAPI.CONFIG_DATA_VERSION);
            save();
        } catch (Exception e) {
            MinecachingAPI.tInfo(MCMessageKeys.Plugin.Data.UPDATE_FAILED, "Config", getConfigVersion(), MinecachingAPI.CONFIG_DATA_VERSION);
        }
    }

    public static Config getInstance() {
        if (MinecachingAPI.get().hasInitialized()) return MinecachingAPI.getConfig();
        else return new Config();
    }
}
