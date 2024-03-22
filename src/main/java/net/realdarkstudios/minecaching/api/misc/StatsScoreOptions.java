package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheStatus;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class StatsScoreOptions {
    public static final StatsScoreOptions DEFAULT_OPTIONS = new StatsScoreOptions();

    // default values
    private int perHideAD = 0;
    private int perFind = 1;
    private int perFavorite = 1;
    private int perHideR = 2;
    private int perHideNM = 3;
    private int perHideP = 5;
    private int perFTF = 10;

    public StatsScoreOptions() {
    }

    public StatsScoreOptions(int perFind, int perHideAD, int perHideR, int perHideNM, int perHideP, int perFavorite, int perFTF) {
        this.perFind = perFind;
        this.perHideAD = perHideAD;
        this.perHideR = perHideR;
        this.perHideNM = perHideNM;
        this.perHideP = perHideP;
        this.perFavorite = perFavorite;
        this.perFTF = perFTF;
    }

    public int favorite() {
        return perFavorite;
    }

    public int find() {
        return perFind;
    }

    public int ftf() {
        return perFTF;
    }

    public int hideAD() {
        return perHideAD;
    }

    public int hideR() {
        return perHideR;
    }

    public int hideNM() {
        return perHideNM;
    }

    public int hideP() {
        return perHideP;
    }

    public StatsScoreOptions setFavorite(int score) {
        this.perFavorite = score;
        return this;
    }

    public StatsScoreOptions setFind(int score) {
        this.perFind = score;
        return this;
    }

    public StatsScoreOptions setFTF(int score) {
        this.perFTF = score;
        return this;
    }

    public StatsScoreOptions setHideAD(int score) {
        this.perHideAD = score;
        return this;
    }

    public StatsScoreOptions setHideNM(int score) {
        this.perHideNM = score;
        return this;
    }

    public StatsScoreOptions setHideR(int score) {
        this.perHideR = score;
        return this;
    }

    public StatsScoreOptions setHideP(int score) {
        this.perHideP = score;
        return this;
    }

    public int calculateScore(PlayerDataObject pdo) {
        int total = 0;

        List<String> finds = pdo.getFinds();
        List<String> hides = pdo.getHides();
        List<String> ftfs = pdo.getFTFs();
        int favorites = 0;

        for (Minecache cache: MinecachingAPI.get().getFilteredCaches(c -> hides.contains(c.id()))) {
            favorites += cache.favorites();

            if (cache.status().equals(MinecacheStatus.PUBLISHED)) total += perHideP;
            else if (cache.status().equals(MinecacheStatus.NEEDS_MAINTENANCE)) total += perHideNM;
            else if (cache.status().equals(MinecacheStatus.REVIEWING)) total += perHideR;
            else if (cache.status().equals(MinecacheStatus.DISABLED)) total += perHideAD;
            else if (cache.status().equals(MinecacheStatus.ARCHIVED)) total += perHideAD;
            else total += 0;
        }

        total += (perFind * finds.size()) + (perFTF * ftfs.size()) + (perFavorite * favorites);

        return total;
    }

    public static StatsScoreOptions fromYaml(YamlConfiguration yaml, String key) {
        int find, favorite, ftf, hideAD, hideNM, hideR, hideP;

        find = yaml.getInt(key + ".FIND", 1);
        favorite = yaml.getInt(key + ".FAVORITE", 1);
        ftf = yaml.getInt(key + ".FTF", 10);
        hideAD = yaml.getInt(key + ".HIDE_AD", 0);
        hideNM = yaml.getInt(key + ".HIDE_NM", 2);
        hideR = yaml.getInt(key + ".HIDE_R", 3);
        hideP = yaml.getInt(key + ".HIDE_P", 5);

        return new StatsScoreOptions(find, hideAD, hideR, hideNM, hideP, favorite, ftf);
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key + ".FIND", perFind);
        yaml.set(key + ".FAVORITE", perFavorite);
        yaml.set(key + ".FTF", perFTF);
        yaml.set(key + ".HIDE_AD", perHideAD);
        yaml.set(key + ".HIDE_R", perHideR);
        yaml.set(key + ".HIDE_NM", perHideNM);
        yaml.set(key + ".HIDE_P", perHideP);
    }
}
