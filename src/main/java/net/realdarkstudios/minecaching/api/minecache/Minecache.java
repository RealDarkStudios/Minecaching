package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.commons.misc.IYamlSerializable;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.naming.SizeLimitExceededException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Minecache implements IYamlSerializable {
    public static final Minecache EMPTY = new Minecache("NULL", MinecacheType.TRADITIONAL, null, MCUtils.EMPTY_UUID, null, 0, 0, 0, 0, 0, 0, MCUtils.EMPTY_UUID, MinecacheStatus.INVALID, null, null, 0, null, 0, null, true);

    private String id, name, code, hint;
    private MinecacheType type;
    private UUID author, maintainer, ftf;
    private World world;
    private int x, y, z, nx, ny, nz, finds, favorites;
    private MinecacheStatus status;
    private LocalDateTime hidden;
    private Material blockType;
    private boolean invalidated;

    public Minecache(String id, MinecacheType type, String name, UUID author, World world, int x, int y, int z, int nx, int ny, int nz, UUID ftf, MinecacheStatus status,
                     LocalDateTime hidden, Material blockType, int finds, String code, int favorites, String hint, boolean invalidated) {
        this(id, type, name, author, MCUtils.EMPTY_UUID, world, x, y, z, nx, ny, nz, ftf, status, hidden, blockType, finds, code, favorites, hint, invalidated);
    }

    public Minecache(String id, MinecacheType type, String name, UUID author, UUID maintainer, World world, int x, int y, int z, int nx, int ny, int nz, UUID ftf,
                     MinecacheStatus status, LocalDateTime hidden, Material blockType, int finds, String code, int favorites, String hint, boolean invalidated) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.author = author;
        this.maintainer = maintainer;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.ftf = ftf;
        this.status = status;
        this.hidden = hidden;
        this.blockType = blockType;
        this.finds = finds;
        this.code = code;
        this.favorites = favorites;
        this.hint = hint;
        this.invalidated = invalidated;
    }

    public Minecache setID(String newID) {
        this.id = newID;
        return this;
    }

    public Minecache setType(MinecacheType newType) {
        this.type = newType;
        return this;
    }

    public Minecache setName(String newName) {
        this.name = newName;
        return this;
    }

    public Minecache setAuthor(UUID author) {
        this.author = author;
        return this;
    }

    public Minecache setMaintaner(UUID maintainer) {
        this.maintainer = maintainer;
        return this;
    }

    public Minecache setLocation(Location loc) {
        this.world = loc.getWorld();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        return this;
    }

    public Minecache setNavLocation(Location loc) {
        this.nx = loc.getBlockX();
        this.ny = loc.getBlockY();
        this.nz = loc.getBlockZ();
        return this;
    }

    public Minecache setStatus(MinecacheStatus newStatus) {
        this.status = newStatus;
        return this;
    }

    public Minecache setHidden(LocalDateTime hidden) {
        this.hidden = hidden;
        return this;
    }

    public Minecache setFTF(UUID uuid) {
        this.ftf = uuid;
        return this;
    }

    public Minecache setBlockType(Material newBlockType) {
        this.blockType = newBlockType;
        return this;
    }

    public Minecache setFinds(int newFinds) {
        this.finds = newFinds;
        return this;
    }

    public Minecache setCode(String code) {
        this.code = code;
        return this;
    }

    public Minecache setFavorites(int favorites) {
        this.favorites = favorites;
        return this;
    }

    public Minecache setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public Minecache setInvalidated(boolean invalidate) {
        this.invalidated = invalidate;
        return this;
    }

    public String id() {
       return id;
    }

    public MinecacheType type() {
        return invalidated ? MinecacheType.INVALID : type;
    }

    public String name() {
        return name != null ? name : "";
    }

    public UUID owner() {
        return maintainer.equals(MCUtils.EMPTY_UUID) ? author : maintainer;
    }

    public UUID originalAuthor() {
        return author;
    }

    public UUID maintainer() {
        return maintainer;
    }

    public boolean hasMaintainer() {
        return !maintainer.equals(MCUtils.EMPTY_UUID);
    }

    public World world() {
        return world;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public int nx() {
        return nx;
    }

    public int ny() {
        return ny;
    }

    public int nz() {
        return nz;
    }

    public Location location() {
        return new Location(world, x, y, z);
    }
    public Location navLocation() {
        return new Location(world, nx, ny, nz);
    }

    public UUID ftf() {
        return ftf;
    }

    public MinecacheStatus status() {
        return invalidated ? MinecacheStatus.INVALID : status;
    }

    public LocalDateTime hidden() {
        return hidden;
    }

    public Material blockType() {
        return blockType;
    }

    public int finds() {
        return finds;
    }

    public String code() {
        return code != null ? code : "";
    }

    public int favorites() {
        return favorites;
    }

    public String hint() {
        return hint;
    }

    public boolean invalidated() {
        return invalidated;
    }

    @NotNull
    public static Minecache fromYaml(YamlConfiguration yaml, String key) {
        boolean isInvalidated = false;
        int invalidatedCode = -1;
        boolean ignoreNormalChecks = key.equals("creating") || key.equals("editing");

        String cName = yaml.getString(key + ".name", "");
        String type = yaml.getString(key + ".type", "invalid");
        String author = yaml.getString(key + ".author", "");
        UUID cAuthor;
        String maintainer = yaml.getString(key + ".maintainer", MCUtils.EMPTY_UUID_STRING);
        UUID cMaintainer;
        String world = yaml.getString(key + ".world", "");
        World cWorld;
        String FTF = yaml.getString(key + ".ftf", MCUtils.EMPTY_UUID_STRING);
        UUID cFTF;
        int cX = yaml.getInt(key + ".x");
        int cY = yaml.getInt(key + ".y");
        int cZ = yaml.getInt(key + ".z");
        int cNX, cNY, cNZ;

        // Convert .lx/y/z -> .nx/y/z as part of the rename from lode coords to nav coords
        if (yaml.contains(key + ".lx")) {
            yaml.set(key + ".nx", yaml.getInt(key + ".lx"));
            yaml.set(key + ".lx", null);
        }
        if (yaml.contains(key + ".ly")) {
            yaml.set(key + ".ny", yaml.getInt(key + ".ly"));
            yaml.set(key + ".ly", null);
        }
        if (yaml.contains(key + ".lz")) {
            yaml.set(key + ".nz", yaml.getInt(key + ".lz"));
            yaml.set(key + ".lz", null);
        }

        cNX = yaml.getInt(key + ".nx");
        cNY = yaml.getInt(key + ".ny");
        cNZ = yaml.getInt(key + ".nz");
        String status = yaml.getString(key + ".status", "invalid");
        String hidden = yaml.getString(key + ".hidden", "");
        LocalDateTime cHidden;
        String blockType = yaml.getString(key + ".blocktype", "AIR");
        Material cBlockType;
        int cFinds = yaml.getInt(key + ".finds", 0);
        String cCode = yaml.getString(key + ".code", "");
        int cFavorites = yaml.getInt(key + ".favorites", 0);
        String cHint = yaml.getString(key + ".hint", MCMessageKeys.Misc.Cache.NO_HINT.getMessage());

        MinecacheType cType;
        if (type.equals("invalid")) { cType = MinecacheType.INVALID; } else { cType = MinecacheType.get(type); }
        MinecacheStatus cStatus;
        if (status.equals("invalid")) { cStatus = MinecacheStatus.INVALID; } else { cStatus = MinecacheStatus.get(status); }
        try { cHidden = LocalDateTime.parse(hidden); } catch (Exception e) { cHidden = LocalDateTime.now(); yaml.set(key + ".hidden", cHidden.toString()); }
        try { cAuthor = UUID.fromString(author); } catch (Exception e) { cAuthor = MCUtils.EMPTY_UUID; if (!ignoreNormalChecks) { isInvalidated = true; invalidatedCode = 0; } }
        try { cMaintainer = UUID.fromString(maintainer); } catch (Exception e) { cMaintainer = MCUtils.EMPTY_UUID; yaml.set(key + ".maintainer", cMaintainer.toString()); }
        try { cFTF = UUID.fromString(FTF); } catch (Exception e) { cFTF = MCUtils.EMPTY_UUID; yaml.set(key + ".ftf", cFTF.toString()); }
        try { cWorld = Bukkit.createWorld(new WorldCreator(world)); } catch (Exception e) { cWorld = null; isInvalidated = true; }
        try { cBlockType = Material.getMaterial(blockType); } catch (Exception e) { cBlockType = Material.AIR; yaml.set(key + ".blocktype", cBlockType.toString()); }
        if (cName.isEmpty()) { yaml.set(key + ".name", ""); if (!ignoreNormalChecks) { isInvalidated = true; invalidatedCode = 1; } }
        if ((!key.startsWith("MC-") || key.length() < 8) && !ignoreNormalChecks) try { transferCache(yaml, key, MinecacheStorage.getInstance().generateNonConflictingCacheID()); } catch (SizeLimitExceededException e) {throw new RuntimeException(e); }
        if (cFinds < 0) { cFavorites = 0; }
        if (cFavorites < 0) { cFavorites = 0; }
        if (cCode.isEmpty() && !ignoreNormalChecks) { cCode = ""; isInvalidated = true; invalidatedCode = 2; }
        if (cWorld == null) { cWorld = Bukkit.getWorlds().get(0); if (!ignoreNormalChecks) { isInvalidated = true; invalidatedCode = 3; } }

        Config cfg = Config.getInstance();
        if (cX > cfg.getMaxX() || cX < cfg.getMinX() || cY > cfg.getMaxY() || cY < cfg.getMinY() || cZ > cfg.getMaxZ() || cZ < cfg.getMinZ()
                || cNX > cfg.getMaxX() || cNX < cfg.getMinX() || cNY > cfg.getMaxY() || cNY < cfg.getMinY() || cNZ > cfg.getMaxZ() || cNZ < cfg.getMinZ() && !ignoreNormalChecks) {
            isInvalidated = true; invalidatedCode = 4;
        }
        if (new Location(cWorld, cNX, cNY, cNZ).distance(new Location(cWorld, cX, cY, cZ)) > Config.getInstance().getMaxLodestoneDistance() && !ignoreNormalChecks) {
            isInvalidated = true; invalidatedCode = 5;
        }

        if (isInvalidated && !ignoreNormalChecks) {
            MinecachingAPI.tWarning(MCMessageKeys.Misc.Cache.YAML, key, switch (invalidatedCode) {
                case 0 -> MCMessageKeys.Misc.Cache.YCODE_0.translate();
                case 1 -> MCMessageKeys.Misc.Cache.YCODE_1.translate();
                case 2 -> MCMessageKeys.Misc.Cache.YCODE_2.translate();
                case 3 -> MCMessageKeys.Misc.Cache.YCODE_3.translate();
                case 4 -> MCMessageKeys.Misc.Cache.YCODE_4.translate();
                case 5 -> MCMessageKeys.Misc.Cache.YCODE_5.translate();
                default -> MCMessageKeys.Misc.Cache.YCODE_OTHER.translate();
            });
        }

        return new Minecache(key, cType, cName, cAuthor, cMaintainer, cWorld, cX, cY, cZ, cNX, cNY, cNZ, cFTF, cStatus, cHidden, cBlockType, cFinds, cCode, cFavorites, cHint, isInvalidated);
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        // use type and status variables to prevent saving type as invalid (which type() and status() may return)
        yaml.set(key + ".type", type.toString());
        yaml.set(key + ".name", name());
        yaml.set(key + ".author", originalAuthor().toString());
        yaml.set(key + ".maintainer", maintainer().toString());
        yaml.set(key + ".ftf", ftf().toString());
        yaml.set(key + ".world", world() != null ? world().getName() : "world");
        yaml.set(key + ".x", x());
        yaml.set(key + ".y", y());
        yaml.set(key + ".z", z());
        yaml.set(key + ".nx", nx());
        yaml.set(key + ".ny", ny());
        yaml.set(key + ".nz", nz());
        yaml.set(key + ".status", status.getId());
        yaml.set(key + ".hidden", hidden() != null ? hidden().toString() : LocalDateTime.now().toString());
        yaml.set(key + ".blocktype", blockType() != null ? blockType().toString() : "AIR");
        yaml.set(key + ".finds", finds());
        yaml.set(key + ".code", code());
        yaml.set(key + ".favorites", favorites());
        yaml.set(key + ".hint", hint());

        if (yaml.contains(key + ".lx")) yaml.set(".lx", null);
        if (yaml.contains(key + ".ly")) yaml.set(".ly", null);
        if (yaml.contains(key + ".lz")) yaml.set(".lz", null);
    }

    private static void transferCache(YamlConfiguration yaml, String currKey, String newKey) {
        yaml.set(newKey, yaml.get(currKey));
        yaml.set(currKey, null);
    }

    public static int compareByTime(Minecache m1, Minecache m2) {
        return m1.hidden().compareTo(m2.hidden());
    }
}
