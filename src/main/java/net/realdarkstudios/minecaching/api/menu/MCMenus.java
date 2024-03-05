package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;

import java.util.HashMap;

public class MCMenus {
    private static final MCMenus INSTANCE = new MCMenus();
    private final HashMap<PlayerDataObject, CreateCacheMenu> createCacheMenus = new HashMap<>();
    private final HashMap<PlayerDataObject, EditCacheMenu> editCacheMenus = new HashMap<>();

    private MCMenus() {
    }

    public CreateCacheMenu getCreateCacheMenu(PlayerDataObject pdo) {
        if (createCacheMenus.containsKey(pdo)) return createCacheMenus.get(pdo);
        else {
            CreateCacheMenu menu = new CreateCacheMenu(Minecache.EMPTY, pdo, Minecaching.getInstance());
            createCacheMenus.put(pdo, menu);
            return menu;
        }
    }

    public void releaseCreateCacheMenu(PlayerDataObject pdo) {
        createCacheMenus.remove(pdo);
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
