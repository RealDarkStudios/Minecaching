package net.realdarkstudios.minecaching.api.misc;

import net.realdarkstudios.minecaching.api.Minecaching;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class MCPermissions {
    private static final HashMap<String, Permission> permissions = new HashMap<>();

    public static final Permission ALL = create("*", PermissionDefault.FALSE);


    // BYPASS
    public static final Permission BYPASS_ALL = create("bypass.*");
    public static final Permission BYPASS_DELETE_OTHERS = create("bypass.delete_others");
    public static final Permission BYPASS_EDIT_OTHERS = create("bypass.edit_others");

    // ADMIN
    public static final Permission ADMIN_ALL = create("admin.*");
    public static final Permission ADMIN_CONFIG = create("admin.config");
    public static final Permission ADMIN_FORCE_STAT_UPDATE = create("admin.force_stat_update");
    public static final Permission ADMIN_RELOAD = create("admin.reload");
    public static final Permission ADMIN_DATA = create("admin.server_data");


    // COMMAND
    public static final Permission COMMAND_ALL = create("command.*");
    public static final Permission COMMAND_ADMIN = create("command.admin", PermissionDefault.OP);
    public static final Permission COMMAND_ARCHIVE = create("command.archive", PermissionDefault.OP);
    public static final Permission COMMAND_CREATE = create("command.create", PermissionDefault.TRUE);
    public static final Permission COMMAND_DELETE = create("command.delete", PermissionDefault.TRUE);
    public static final Permission COMMAND_DISABLE = create("command.disable", PermissionDefault.OP);
    public static final Permission COMMAND_EDIT = create("command.edit", PermissionDefault.TRUE);
    public static final Permission COMMAND_HINT = create("command.hint", PermissionDefault.TRUE);
    public static final Permission COMMAND_LIST = create("command.list", PermissionDefault.TRUE);
    public static final Permission COMMAND_LOCATE = create("command.locate", PermissionDefault.TRUE);
    public static final Permission COMMAND_LOG = create("command.log", PermissionDefault.TRUE);
    public static final Permission COMMAND_LOGBOOK = create("command.logbook", PermissionDefault.TRUE);
    public static final Permission COMMAND_MAINTAINER = create("command.maintainer", PermissionDefault.TRUE);
    public static final Permission COMMAND_PUBLISH = create("command.publish", PermissionDefault.OP);
    public static final Permission COMMAND_STATS = create("command.stats", PermissionDefault.TRUE);

    // MISC
    public static final Permission MISC_ALL = create("misc.*");
    public static final Permission MISC_SECRET = create("misc.secret", PermissionDefault.FALSE, "Just an example key, does nothing special (or does it?)");


    private static Permission create(String key, PermissionDefault def, String description) {
        Permission perm = new Permission("minecaching." + key, description, def, null);
        permissions.put(key, perm);
        return perm;
    }

    private static Permission create(String key) {
        return create(key, PermissionDefault.OP, "");
    }

    private static Permission create(String key, PermissionDefault def) {
        return create(key, def, "");
    }

    private static Permission create(String key, String description) {
        return create(key, PermissionDefault.OP, description);
    }

    public static void init() {
        // Add all permissions under ALL
        permissions.values().forEach(p -> {
            if (!p.equals(ALL)) {
                p.addParent(ALL, true);
            }
        });

        addParent(BYPASS_DELETE_OTHERS, BYPASS_ALL, true);
        addParent(BYPASS_EDIT_OTHERS, BYPASS_ALL, true);

        addParent(ADMIN_CONFIG, ADMIN_ALL, true);
        addParent(ADMIN_FORCE_STAT_UPDATE, ADMIN_ALL, true);
        addParent(ADMIN_RELOAD, ADMIN_ALL, true);
        addParent(ADMIN_DATA, ADMIN_ALL, true);

        addParent(COMMAND_ADMIN, COMMAND_ALL, true);
        addParent(COMMAND_ARCHIVE, COMMAND_ALL, true);
        addParent(COMMAND_CREATE, COMMAND_ALL, true);
        addParent(COMMAND_DELETE, COMMAND_ALL, true);
        addParent(COMMAND_DISABLE, COMMAND_ALL, true);
        addParent(COMMAND_EDIT, COMMAND_ALL, true);
        addParent(COMMAND_HINT, COMMAND_ALL, true);
        addParent(COMMAND_LIST, COMMAND_ALL, true);
        addParent(COMMAND_LOCATE, COMMAND_ALL, true);
        addParent(COMMAND_LOG, COMMAND_ALL, true);
        addParent(COMMAND_LOGBOOK, COMMAND_ALL, true);
        addParent(COMMAND_MAINTAINER, COMMAND_ALL, true);
        addParent(COMMAND_PUBLISH, COMMAND_ALL, true);
        addParent(COMMAND_STATS, COMMAND_ALL, true);

        addParent(MISC_SECRET, MISC_ALL, false);

        PluginManager pluginManager = Minecaching.getInstance().getServer().getPluginManager();

        for (Permission perm: permissions.values()) {
            if (pluginManager.getPermissions().contains(perm)) pluginManager.removePermission(perm);
        }

        permissions.values().forEach(pluginManager::addPermission);
    }

    private static void addParent(Permission perm, Permission parent, boolean value) {
        perm.addParent(parent, value);
    }
}
