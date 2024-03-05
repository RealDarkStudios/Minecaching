package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.EditCacheMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.NotificationType;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.event.minecache.MinecacheEditedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class EditCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid;
        if (!(sender instanceof Player plr)) {
            uuid = MCUtils.EMPTY_UUID;
        } else {
            uuid = plr.getUniqueId();
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(uuid);

        if (args.length < 1 && pdo.getEditingCache().id().equals("NULL")) {
            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.EDIT, label);
            return true;
        }

        if (sender instanceof Player plr) {
            Minecache cache = MinecachingAPI.get().getMinecache(args.length > 0 ? args[0] : "NULL");
            if (cache.equals(Minecache.EMPTY)) {
                EditCacheMenu menu = MCMenus.get().getEditCacheMenu(pdo, pdo.getEditingCache());
                menu.open(plr);
                if (args.length < 1) return true;

                switch (args[0]) {
                    case "name" -> {
                        if (args.length < 2) {
                            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAME, label);
                            return true;
                        } else {
                            StringBuilder name = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                name.append(args[i]).append(" ");
                            }
                            pdo.setEditingCache(pdo.getEditingCache().setName(name.toString().replace("&", "§").trim()));
                            menu.open(plr);
                            menu.update(plr);
                        }
                    }
                    case "code" -> {
                        if (args.length < 2) {
                            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_CODE, label);
                            return true;
                        } else {
                            pdo.setEditingCache(pdo.getEditingCache().setCode(args[1].trim()));
                            menu.open(plr);
                            menu.update(plr);
                        }
                    }
                    default -> LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, cache.id());
                }

                return true;
            }

            if (pdo.getUniqueID().equals(cache.owner()) || pdo.getPlayer().hasPermission("minecaching.admin.edit")) {
                pdo.setEditingCache(cache);

                EditCacheMenu menu = MCMenus.get().getEditCacheMenu(pdo, cache);
                menu.open(plr);
            } else {
                LocalizedMessages.send(sender, MessageKeys.Permission.NO_PERMISSION_EDITCACHE);
            }
        } else {
            Minecache cache = pdo.getEditingCache();
            if (args.length < 1) {
                LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                LocalizedMessages.send(sender, MessageKeys.Usage.EDIT, label);
                return true;
            } else if (args.length == 1 && (cache == null || cache.id().equals("NULL"))) {
                String id = args[0];

                if (MinecachingAPI.get().getMinecache(id).equals(Minecache.EMPTY)) {
                    LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, id);
                    return true;
                }

                LocalizedMessages.send(sender, MessageKeys.Command.Edit.EDIT, id);
                pdo.setEditingCache(MinecachingAPI.get().getMinecache(id));
                return true;
            } else if (args.length == 1 && args[0].startsWith("MC-") && !cache.id().equals("NULL")) {
                LocalizedMessages.send(sender, MessageKeys.Error.Edit.ALREADY_EDITING, cache.id());
                return true;
            }

            String subCommand = args[0];

            switch (subCommand) {
                case "cancel" -> {
                    LocalizedMessages.send(sender, MessageKeys.Command.Edit.CANCEL, cache.id());
                    cache = Minecache.EMPTY;
                    cache.setID("NULL");
                }
                case "name" -> {
                    if (args.length < 2) {

                        LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAME, label);
                        return true;
                    } else {
                        StringBuilder name = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            name.append(args[i]).append(" ");
                        }
                        cache.setName(name.toString().trim());
                        LocalizedMessages.send(sender, MessageKeys.Command.Edit.NAME, cache.name());
                    }
                }
                case "lodecoords" -> {
                    int x, y, z;
                    if (args.length == 4) {
                        x = MCUtils.interpretCoordinate(args[1], "x");
                        y = MCUtils.interpretCoordinate(args[2], "y");
                        z = MCUtils.interpretCoordinate(args[3], "z");
                    } else {
                        LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAVIGATION_COORDS, label);
                        return true;
                    }

                    if (MCUtils.locationInvalid(sender, x, y, z)) return true;

                    cache.setNavLocation(new Location(cache.world(), x, y, z));
                    LocalizedMessages.send(sender, MessageKeys.Command.Edit.NAV_COORDS, cache.world().getName(), x, y, z);
                }
                case "coords" -> {
                    int x, y, z;
                    if (args.length == 4) {
                        x = MCUtils.interpretCoordinate(args[1], "x");
                        y = MCUtils.interpretCoordinate(args[2], "y");
                        z = MCUtils.interpretCoordinate(args[3], "z");
                    } else {
                        LocalizedMessages.send(sender, MessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_COORDS, label);
                        return true;
                    }

                    if (MCUtils.locationInvalid(sender, x, y, z)) return true;

                    cache.setLocation(new Location(cache.world(), x, y, z));
                    LocalizedMessages.send(sender, MessageKeys.Command.Edit.COORDS, cache.world().getName(), x, y, z);

                }
                case "code" -> {
                    if (args.length < 2) {
                        LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_CODE, label);
                        return true;
                    } else {
                        cache.setCode(args[1].trim());
                        LocalizedMessages.send(sender, MessageKeys.Command.Edit.CODE, cache.code());
                    }
                }
                case "save" -> {
                    if (cache.name() == null) {
                        LocalizedMessages.send(sender, MessageKeys.Error.Edit.NO_NAME);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAME, label);
                    } else if (cache.code() == null) {
                        LocalizedMessages.send(sender, MessageKeys.Error.Edit.NO_CODE);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_COORDS, label);
                    } else if (cache.x() == 0 && cache.y() == 0 && cache.z() == 0) {
                        LocalizedMessages.send(sender, MessageKeys.Error.Edit.NO_COORDS);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_COORDS, label);
                    } else if (cache.nx() == 0 && cache.ny() == 0 && cache.nz() == 0) {
                        LocalizedMessages.send(sender, MessageKeys.Error.Edit.NO_NAV_COORDS);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAVIGATION_COORDS, label);
                    } else if (cache.navLocation().distance(cache.location()) > Config.getInstance().getMaxLodestoneDistance()) {
                        LocalizedMessages.send(sender, MessageKeys.Error.Edit.NAV_COORDS_TOO_FAR);
                        LocalizedMessages.send(sender, MessageKeys.Usage.EDIT_NAVIGATION_COORDS, label);
                    } else {
                        MinecacheEditedEvent event = new MinecacheEditedEvent(cache, sender);
                        Bukkit.getPluginManager().callEvent(event);

                        if (event.isCancelled()) {
                            LocalizedMessages.send(sender, MessageKeys.Error.Edit.GENERAL);
                            return true;
                        }

                        MinecachingAPI.get().createNotification(cache.owner(), MCUtils.EMPTY_UUID, NotificationType.EDIT, cache);

                        MinecachingAPI.get().saveMinecache(cache, false);
                        LocalizedMessages.send(sender, MessageKeys.Command.Edit.SAVE, cache.id(), cache.name());
                        cache = Minecache.EMPTY;
                        cache.setID("NULL");
                    }
                }
            }

            pdo.setEditingCache(cache);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player plr) {
            if (MinecachingAPI.get().getPlayerData(plr).getEditingCache().id().equals("NULL")) return List.of();
            else return List.of("name", "code");
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(MCUtils.EMPTY_UUID);

        if (args.length == 0 && (pdo.getEditingCache() == null || pdo.getEditingCache().id().equals("NULL")))
            return MinecachingAPI.get().getAllKnownCacheIDs();
        else if (args.length == 1 && (pdo.getEditingCache() == null || pdo.getEditingCache().id().equals("NULL"))) {
            return MinecachingAPI.get().getAllKnownCacheIDs();
        } else {
            if (args.length == 1) {
                return (pdo.getEditingCache() == null || pdo.getEditingCache().id().equals("NULL")) ? List.of() :
                        Stream.of("cancel", "code", "name", "lodecoords", "coords", "save").filter(s -> s.contains(args[0])).toList();
            } else return List.of();
        }
    }
}
