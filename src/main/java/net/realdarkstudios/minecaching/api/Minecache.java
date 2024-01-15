package net.realdarkstudios.minecaching.api;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

public class Minecache {
    public static final Minecache EMPTY = new Minecache(null, MinecacheType.TRADITIONAL, null, Utils.EMPTY_UUID, null, 0, 0, 0, 0, 0, 0, Utils.EMPTY_UUID, MinecacheStatus.INVALID, null, null, 0, null, true);

    private String id, name, code;
    private MinecacheType type;
    private UUID author, maintainer, ftf;
    private World world;
    private int x, y, z, lx, ly, lz, finds;
    private MinecacheStatus status;
    private LocalDateTime hidden;
    private Material blockType;
    private boolean invalidated;

    public Minecache(String id, MinecacheType type, String name, UUID author, World world, int x, int y, int z, int lx, int ly, int lz, UUID ftf, MinecacheStatus status, LocalDateTime hidden, Material blockType, int finds, String code, boolean invalidated) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.author = author;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
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

    public Minecache setLodeLocation(Location loc) {
        this.lx = loc.getBlockX();
        this.ly = loc.getBlockY();
        this.lz = loc.getBlockZ();
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

    public int lx() {
        return lx;
    }

    public int ly() {
        return ly;
    }

    public int lz() {
        return lz;
    }

    public Location location() {
        return new Location(world, x, y, z);
    }
    public Location lodeLocation() {
        return new Location(world, lx, ly, lz);
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
        int cLX = yaml.getInt(key + ".lx");
        int cLY = yaml.getInt(key + ".ly");
        int cLZ = yaml.getInt(key + ".lz");
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
        if (status == null) { cStatus = MinecacheStatus.NEEDS_REVIEWED; } else { cStatus = MinecacheStatus.get(status); }
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
                || cLX > cfg.getMaxX() || cLX < cfg.getMinX() || cLY > cfg.getMaxY() || cLY < cfg.getMinY() || cLZ > cfg.getMaxZ() || cLZ < cfg.getMinZ()) {
            Minecaching.getInstance().getLogger().warning(String.format("%s is outside of the boundaries set in the config! The cache has been invalidated!", key));
            isInvalidated = true;
        }
        if (new Location(cWorld, cLX, cLY, cLZ).distance(new Location(cWorld, cX, cY, cZ)) > Config.getInstance().getMaxLodestoneDistance()) {
            Minecaching.getInstance().getLogger().warning("The lodestone coordinates are too far away! The cache has been invalidated");
            isInvalidated = true;
        }

        return new Minecache(key, cType, cName, cAuthor, cWorld, cX, cY, cZ, cLX, cLY, cLZ, cFTF, cStatus, cHidden, cBlockType, cFinds, cCode, isInvalidated);
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
        yaml.set(key + ".lx", lx());
        yaml.set(key + ".ly", ly());
        yaml.set(key + ".lz", lz());
        yaml.set(key + ".status", status().getId());
        yaml.set(key + ".hidden", hidden() != null ? hidden().toString() : LocalDateTime.now().toString());
        yaml.set(key + ".blocktype", blockType() != null ? blockType().toString() : "AIR");
        yaml.set(key + ".finds", finds());
        yaml.set(key + ".code", code());
    }

    public static int compareByTime(Minecache m1, Minecache m2) {
        return m1.hidden().compareTo(m2.hidden());
    }
}
