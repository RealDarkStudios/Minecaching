package net.realdarkstudios.minecaching.api.minecache;

import net.realdarkstudios.minecaching.util.Utils;
import net.realdarkstudios.minecaching.api.misc.Config;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

public class Minecache {
    public static final Minecache EMPTY = new Minecache("NULL", MinecacheType.TRADITIONAL, null, Utils.EMPTY_UUID, null, 0, 0, 0, 0, 0, 0, Utils.EMPTY_UUID, MinecacheStatus.INVALID, null, null, 0, null, true);

    private String id, name, code;
    private MinecacheType type;
    private UUID author, maintainer, ftf;
    private World world;
    private int x, y, z, nx, ny, nz, finds;
    private MinecacheStatus status;
    private LocalDateTime hidden;
    private Material blockType;
    private boolean invalidated;

    public Minecache(String id, MinecacheType type, String name, UUID author, World world, int x, int y, int z, int nx, int ny, int nz, UUID ftf, MinecacheStatus status, LocalDateTime hidden, Material blockType, int finds, String code, boolean invalidated) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.author = author;
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
        this.invalidated = invalidated;
        this.code = code;
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

    public Minecache setInvalidated(boolean invalidate) {
        this.invalidated = invalidate;
        return this;
    }

    public Minecache setCode(String code) {
        this.code = code;
        return this;
    }

    public String id() {
       return id;
    }

    public MinecacheType type() {
        return type;
    }

    public String name() {
        return name;
    }

    public UUID author() {
        return author;
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
        return status;
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

    public boolean invalidated() {
        return invalidated;
    }

    public String code() {
        return code;
    }

    public static Minecache fromYaml(YamlConfiguration yaml, String key) {
        String cName = yaml.getString(key + ".name");
        String type = yaml.getString(key + ".type");
        String author = yaml.getString(key + ".author");
        UUID cAuthor;
        String world = yaml.getString(key + ".world");
        World cWorld;
        String FTF = yaml.getString(key + ".ftf");
        UUID cFTF;
        int cX = yaml.getInt(key + ".x");
        int cY = yaml.getInt(key + ".y");
        int cZ = yaml.getInt(key + ".z");
        int cNX, cNY, cNZ;

        if (yaml.contains(".lx")) {
            yaml.set(key + ".nx", yaml.getInt(key + ".lx"));
            yaml.set(key + ".lx", null);
        }
        if (yaml.contains(".ly")) {
            yaml.set(key + ".ny", yaml.getInt(key + ".ly"));
            yaml.set(key + ".ly", null);
        }
        if (yaml.contains(".lz")) {
            yaml.set(key + ".nz", yaml.getInt(key + ".lz"));
            yaml.set(key + ".lz", null);
        }

        cNX = yaml.getInt(key + ".nx");
        cNY = yaml.getInt(key + ".ny");
        cNZ = yaml.getInt(key + ".nz");
        String status = yaml.getString(key + ".status");
        LocalDateTime cHidden;
        String blockType = yaml.getString(key + ".blocktype");
        Material cBlockType;
        int cFinds = yaml.getInt(key + ".finds");
        boolean isInvalidated = false;
        String cCode = yaml.getString(key + ".code");

        MinecacheType cType;
        if (type == null) { cType = MinecacheType.TRADITIONAL; } else { cType = MinecacheType.get(type); }
        MinecacheStatus cStatus;
        if (status == null) { cStatus = MinecacheStatus.REVIEWING; } else { cStatus = MinecacheStatus.get(status); }
        try { cHidden = LocalDateTime.parse(yaml.getString(key + ".hidden")); } catch (Exception e) { cHidden = LocalDateTime.now(); isInvalidated = true; }
        try { cAuthor = UUID.fromString(author); } catch (Exception e) { cAuthor = Utils.EMPTY_UUID; isInvalidated = true; }
        try { cFTF = UUID.fromString(FTF); } catch (Exception e) { cFTF = Utils.EMPTY_UUID; isInvalidated = true; }
        try { cWorld = Bukkit.createWorld(new WorldCreator(world)); } catch (Exception e) { cWorld = null; isInvalidated = true; }
        try { cBlockType = Material.getMaterial(blockType); } catch (Exception e) { cBlockType = Material.AIR; isInvalidated = true; }
        if (cName == null || !key.startsWith("MC-") || key.length() < 8 || cFinds < 0) { isInvalidated = true; }
        if (cCode == null) { cCode = ""; isInvalidated = true; }
        if (cWorld == null) { cWorld = Bukkit.getWorlds().get(0); isInvalidated = true; }
        Config cfg = Config.getInstance();
        if (cX > cfg.getMaxX() || cX < cfg.getMinX() || cY > cfg.getMaxY() || cY < cfg.getMinY() || cZ > cfg.getMaxZ() || cZ < cfg.getMinZ()
                || cNX > cfg.getMaxX() || cNX < cfg.getMinX() || cNY > cfg.getMaxY() || cNY < cfg.getMinY() || cNZ > cfg.getMaxZ() || cNZ < cfg.getMinZ()) {
            isInvalidated = true;
        }
        if (new Location(cWorld, cNX, cNY, cNZ).distance(new Location(cWorld, cX, cY, cZ)) > Config.getInstance().getMaxLodestoneDistance()) {
            isInvalidated = true;
        }

        return new Minecache(key, cType, cName, cAuthor, cWorld, cX, cY, cZ, cNX, cNY, cNZ, cFTF, cStatus, cHidden, cBlockType, cFinds, cCode, isInvalidated);
    }

    public void toYaml(YamlConfiguration yaml, String key) {
        yaml.set(key + ".type", type().toString());
        yaml.set(key + ".name", name());
        yaml.set(key + ".author", author().toString());
        yaml.set(key + ".ftf", ftf().toString());
        yaml.set(key + ".world", world() != null ? world().getName() : "world");
        yaml.set(key + ".x", x());
        yaml.set(key + ".y", y());
        yaml.set(key + ".z", z());
        yaml.set(key + ".nx", nx());
        yaml.set(key + ".ny", ny());
        yaml.set(key + ".nz", nz());
        yaml.set(key + ".status", status().getId());
        yaml.set(key + ".hidden", hidden() != null ? hidden().toString() : LocalDateTime.now().toString());
        yaml.set(key + ".blocktype", blockType() != null ? blockType().toString() : "AIR");
        yaml.set(key + ".finds", finds());
        yaml.set(key + ".code", code());

        if (yaml.contains(key + "lx")) yaml.set("lx", null);
        if (yaml.contains(key + "ly")) yaml.set("ly", null);
        if (yaml.contains(key + "lz")) yaml.set("lz", null);
    }

    public static int compareByTime(Minecache m1, Minecache m2) {
        return m1.hidden().compareTo(m2.hidden());
    }
}