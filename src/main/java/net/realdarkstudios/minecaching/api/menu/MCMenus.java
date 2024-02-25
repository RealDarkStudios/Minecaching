package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;

import java.util.HashMap;

public class MCMenus {
    private static final MCMenus INSTANCE = new MCMenus();
    private final HashMap<PlayerDataObject, CreateCacheMenu> addCacheMenus = new HashMap<>();
    private final HashMap<PlayerDataObject, EditCacheMenu> editCacheMenus = new HashMap<>();

    private MCMenus() {
    }

    public CreateCacheMenu getAddCacheMenu(PlayerDataObject pdo) {
        if (addCacheMenus.containsKey(pdo)) return addCacheMenus.get(pdo);
        else {
            CreateCacheMenu menu = new CreateCacheMenu(Minecache.EMPTY, pdo, Minecaching.getInstance());
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

    public static MCMenus get() {
        return INSTANCE;
    }
}
