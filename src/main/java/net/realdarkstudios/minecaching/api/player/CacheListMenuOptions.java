package net.realdarkstudios.minecaching.api.player;

import net.realdarkstudios.minecaching.api.menu.CacheListMenu;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class containing the default options for the {@link CacheListMenu}
 */
public class CacheListMenuOptions {
    public static final CacheListMenuOptions DEFAULT_OPTIONS = new CacheListMenuOptions();

    // default values
    private final ArrayList<MinecacheType> enabledTypes;
    private final ArrayList<MinecacheStatus> enabledStatuses;
    private int minFinds, withinBlocks;
    private boolean oldestFirst, newestFirst, mostFavoritesFirst, ftfsOnly, favoritesOnly;

    public CacheListMenuOptions() {
        this.enabledTypes = new ArrayList<>(Arrays.stream(MinecacheType.values()).toList());
        this.enabledStatuses = new ArrayList<>(Arrays.stream(MinecacheStatus.values()).toList());
        this.minFinds = 0;
        this.withinBlocks = Integer.MAX_VALUE;
        this.oldestFirst = false;
        this.newestFirst = false;
        this.mostFavoritesFirst = true;
        this.ftfsOnly = false;
        this.favoritesOnly = false;
    }

    public CacheListMenuOptions(List<MinecacheType> enabledTypes, List<MinecacheStatus> enabledStatuses, int minFinds, int withinBlocks, boolean oldestFirst, boolean newestFirst, boolean mostFavoritesFirst, boolean ftfsOnly, boolean favoritesOnly) {
        this.enabledTypes = new ArrayList<>(enabledTypes);
        this.enabledStatuses = new ArrayList<>(enabledStatuses);
        this.minFinds = minFinds;
        this.withinBlocks = withinBlocks;

        if ((oldestFirst && newestFirst) || (oldestFirst && mostFavoritesFirst) || (newestFirst && mostFavoritesFirst)) {
            this.oldestFirst = false;
            this.newestFirst = true;
            this.mostFavoritesFirst = false;
        }
        else {
            this.oldestFirst = oldestFirst;
            this.newestFirst = newestFirst;
            this.mostFavoritesFirst = mostFavoritesFirst;
        }
        this.ftfsOnly = ftfsOnly;
        this.favoritesOnly = favoritesOnly;
    }

    public List<MinecacheType> getEnabledTypes() {
        return enabledTypes;
    }

    public List<MinecacheStatus> getEnabledStatuses() {
        return enabledStatuses;
    }

    public int getMinFinds() {
        return minFinds;
    }

    public int getWithinBlocks() {
        return withinBlocks;
    }

    public boolean oldestFirst() {
        return oldestFirst;
    }

    public boolean newestFirst() {
        return newestFirst;
    }

    public boolean mostFavoritesFirst() {
        return mostFavoritesFirst;
    }

    public boolean ftfsOnly() {
        return ftfsOnly;
    }

    public boolean favoritesOnly() {
        return favoritesOnly;
    }

    public boolean typeEnabled(MinecacheType type) {
        return enabledTypes.contains(type);
    }

    public CacheListMenuOptions toggleType(MinecacheType type) {
        if (typeEnabled(type)) enabledTypes.remove(type);
        else enabledTypes.add(type);
        return this;
    }

    public boolean statusEnabled(MinecacheStatus status) {
        return enabledStatuses.contains(status);
    }

    public CacheListMenuOptions toggleStatus(MinecacheStatus status) {
        if (statusEnabled(status)) enabledStatuses.remove(status);
        else enabledStatuses.add(status);
        return this;
    }

    public CacheListMenuOptions setMinFinds(int minFinds) {
        this.minFinds = minFinds;
        return this;
    }

    public CacheListMenuOptions setWithinBlocks(int withinBlocks) {
        this.withinBlocks = withinBlocks;
        return this;
    }

    public CacheListMenuOptions setNewestFirst() {
        this.oldestFirst = false;
        this.newestFirst = true;
        this.mostFavoritesFirst = false;
        return this;
    }

    public CacheListMenuOptions setOldestFirst() {
        this.newestFirst = false;
        this.oldestFirst = true;
        this.mostFavoritesFirst = false;
        return this;
    }

    public CacheListMenuOptions setMostFavoritesFirst() {
        this.newestFirst = false;
        this.oldestFirst = false;
        this.mostFavoritesFirst = true;
        return this;
    }

    public CacheListMenuOptions setFtfsOnly(boolean ftfsOnly) {
        this.ftfsOnly = ftfsOnly;
        return this;
    }

    public CacheListMenuOptions setFavoritesOnly(boolean favoritesOnly) {
        this.favoritesOnly = favoritesOnly;
        return this;
    }

    public static CacheListMenuOptions fromYaml(YamlConfiguration yaml, String key) {
        List<String> types = (List<String>) yaml.getList(key + ".enabled_types");
        List<String> statuses = (List<String>) yaml.getList(key + ".enabled_statuses");
        List<MinecacheType> clmTypes;
        List<MinecacheStatus> clmStatuses;
        int clmMinFinds = yaml.getInt(key + ".min_finds");
        int clmWithinDistance = yaml.getInt(key + ".within_distance");
        boolean clmNewestFirst = yaml.getBoolean(key + ".newest_first");
        boolean clmOldestFirst = yaml.getBoolean(key + ".oldest_first");;
        boolean clmFavoritesFirst = yaml.getBoolean(key + ".most_favorites_first");
        boolean clmFtfsOnly = yaml.getBoolean(key + ".ftfs_only");
        boolean clmFavoritesOnly = yaml.getBoolean(key + ".favorites_only");
        if (types == null || types.isEmpty()) { clmTypes = Arrays.stream(MinecacheType.values()).toList(); }
        else {
            List<MinecacheType> typeList = new ArrayList<>();
            for (String type : types) {
                MinecacheType addType = MinecacheType.get(type);
                if (!addType.equals(MinecacheType.INVALID)) typeList.add(addType);
            }
            clmTypes = typeList;
        }
        if (statuses == null || statuses.isEmpty()) { clmStatuses = Arrays.stream(MinecacheStatus.values()).toList(); }
        else {
            List<MinecacheStatus> statusList = new ArrayList<>();
            for (String status : statuses) {
                MinecacheStatus addStatus = MinecacheStatus.get(status);
                if (!addStatus.equals(MinecacheStatus.INVALID)) statusList.add(addStatus);
            }
            clmStatuses = statusList;
        }

        return new CacheListMenuOptions(clmTypes, clmStatuses, clmMinFinds, clmWithinDistance, clmOldestFirst, clmNewestFirst, clmFavoritesFirst,
                clmFtfsOnly, clmFavoritesOnly);
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        List<String> enabledTypesS = new ArrayList<>(enabledTypes.size()), enabledStatusesS = new ArrayList<>(enabledStatuses.size());

        for (MinecacheType type: enabledTypes) {
            enabledTypesS.add(type.getId());
        }

        for (MinecacheStatus status: enabledStatuses) {
            enabledStatusesS.add(status.getId());
        }

        yaml.set(key + ".enabled_types", enabledTypesS);
        yaml.set(key + ".enabled_statuses", enabledStatusesS);
        yaml.set(key + ".min_finds", minFinds);
        yaml.set(key + ".within_blocks", withinBlocks);
        yaml.set(key + ".newest_first", newestFirst);
        yaml.set(key + ".oldest_first", oldestFirst);
        yaml.set(key + ".most_favorites_first", mostFavoritesFirst);
        yaml.set(key + ".ftfs_only", ftfsOnly);
        yaml.set(key + ".favorites_only", favoritesOnly);
    }
}
