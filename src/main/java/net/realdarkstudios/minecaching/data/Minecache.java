package net.realdarkstudios.minecaching.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.time.LocalDateTime;
import java.util.UUID;

public class Minecache {
    public static final Minecache EMPTY = new Minecache(null, MinecacheType.INVALID, null, null, null, 0, 0, 0, 0, 0, 0, null, MinecacheStatus.INVALID, null, null, 0, false);

    private String id, name;
    private MinecacheType type;
    private UUID author, maintainer, ftf;
    private World world;
    private int x, y, z, lx, ly, lz, finds;
    private MinecacheStatus status;
    private LocalDateTime hidden;
    private Material blockType;
    private boolean invalidated;

    public Minecache(String id, MinecacheType type, String name, UUID author, World world, int x, int y, int z, int lx, int ly, int lz, UUID ftf, MinecacheStatus status, LocalDateTime hidden, Material blockType, int finds, boolean invalidated) {
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
}
