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
    private boolean oldestFirst, newestFirst, ftfsOnly;

    public CacheListMenuOptions() {
        enabledTypes = new ArrayList<>(Arrays.stream(MinecacheType.values()).toList());
        enabledStatuses = new ArrayList<>(Arrays.stream(MinecacheStatus.values()).toList());
        minFinds = 0;
        withinBlocks = Integer.MAX_VALUE;
        oldestFirst = false;
        newestFirst = true;
        ftfsOnly = false;
    }

    public CacheListMenuOptions(List<MinecacheType> enabledTypes, List<MinecacheStatus> enabledStatuses, int minFinds, int withinBlocks, boolean oldestFirst, boolean newestFirst, boolean ftfsOnly) {
        this.enabledTypes = new ArrayList<>(enabledTypes);
        this.enabledStatuses = new ArrayList<>(enabledStatuses);
        this.minFinds = minFinds;
        this.withinBlocks = withinBlocks;

        if (oldestFirst && newestFirst) {
            this.oldestFirst = false;
            this.newestFirst = false;
        }
        else {
            this.oldestFirst = oldestFirst;
            this.newestFirst = newestFirst;
        }
        this.ftfsOnly = ftfsOnly;
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

    public boolean ftfsOnly() {
        return ftfsOnly;
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
        return this;
    }

    public CacheListMenuOptions setOldestFirst() {
        this.newestFirst = false;
        this.oldestFirst = true;
        return this;
    }

    public CacheListMenuOptions setFtfsOnly(boolean ftfsOnly) {
        this.ftfsOnly = ftfsOnly;
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
        boolean clmOldestFirst = yaml.getBoolean(key + ".oldest_first");
        boolean clmFtfsOnly = yaml.getBoolean(key + ".ftfs_only");
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

        return new CacheListMenuOptions(clmTypes, clmStatuses, clmMinFinds, clmWithinDistance, clmOldestFirst, clmNewestFirst, clmFtfsOnly);
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
        yaml.set(key + ".ftfs_only", ftfsOnly);
    }
}
