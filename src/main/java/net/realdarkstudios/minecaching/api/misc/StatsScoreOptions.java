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

    public int calculateScore(PlayerDataObject pdo) {
        int total = 0;

        List<String> finds = pdo.getFinds();
        List<String> hides = pdo.getHides();
        List<String> ftfs = pdo.getFTFs();
        int favorites = 0;

        for (String hide: hides) {
            favorites += MinecachingAPI.get().getMinecache(hide).favorites();
        }

        total += (perFind * finds.size()) + (perFTF * ftfs.size()) + (perFavorite * favorites);

        for (String id: hides) {
            Minecache cache = MinecachingAPI.get().getMinecache(id);
            if (cache.status().equals(MinecacheStatus.PUBLISHED)) total += perHideP;
            else if (cache.status().equals(MinecacheStatus.NEEDS_MAINTENANCE)) total += perHideNM;
            else if (cache.status().equals(MinecacheStatus.REVIEWING)) total += perHideR;
            else if (cache.status().equals(MinecacheStatus.DISABLED)) total += perHideAD;
            else if (cache.status().equals(MinecacheStatus.ARCHIVED)) total += perHideAD;
            else total += 0;
        }

        return total;
    }

    public static StatsScoreOptions fromYaml(YamlConfiguration yaml, String key) {
        return new StatsScoreOptions();
    }

    public void toYaml(YamlConfiguration yaml, String key) {

    }
}
