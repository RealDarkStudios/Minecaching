package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;

import java.util.HashMap;

public class MCMenus {
    private static final MCMenus INSTANCE = new MCMenus();
    private final HashMap<PlayerDataObject, AddCacheMenu> addCacheMenus = new HashMap<>();
    private final HashMap<PlayerDataObject, EditCacheMenu> editCacheMenus = new HashMap<>();
    private final HashMap<PlayerDataObject, CacheListMenu> cacheListMenus = new HashMap<>();

    private MCMenus() {
    }

    public AddCacheMenu getAddCacheMenu(PlayerDataObject pdo) {
        if (addCacheMenus.containsKey(pdo)) return addCacheMenus.get(pdo);
        else {
            AddCacheMenu menu = new AddCacheMenu(Minecache.EMPTY, pdo, Minecaching.getInstance());
            addCacheMenus.put(pdo, menu);
            return menu;
        }
    }

    public void releaseAddCacheMenu(PlayerDataObject pdo) {
        addCacheMenus.remove(pdo);
    }

    public EditCacheMenu getEditCacheMenu(PlayerDataObject pdo, Minecache editingCache) {
        if (editCacheMenus.containsKey(pdo)) return editCacheMenus.get(pdo);
        else {
            EditCacheMenu menu = new EditCacheMenu(editingCache, pdo, Minecaching.getInstance());
            editCacheMenus.put(pdo, menu);
            return menu;
        }
    }

    public void releaseEditCacheMenu(PlayerDataObject pdo) {
        editCacheMenus.remove(pdo);
    }

    public CacheListMenu getCacheListMenu(PlayerDataObject pdo) {
        if (cacheListMenus.containsKey(pdo)) return cacheListMenus.get(pdo);
        else {
            CacheListMenu menu = new CacheListMenu("Server Caches", Minecaching.getInstance());
            cacheListMenus.put(pdo, menu);
            return menu;
        }
    }

    public void releaseCacheListMenu(PlayerDataObject pdo) {
        cacheListMenus.remove(pdo);
    }

    public static MCMenus get() {
        return INSTANCE;
    }
}
