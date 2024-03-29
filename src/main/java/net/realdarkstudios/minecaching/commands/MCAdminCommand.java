package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.menu.CacheDataMenu;
import net.realdarkstudios.minecaching.api.menu.LogMenu;
import net.realdarkstudios.minecaching.api.menu.MCMenus;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.minecache.MinecacheType;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.misc.StatsScoreOptions;
import net.realdarkstudios.minecaching.api.util.MCMessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MCAdminCommand extends MCCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN);
            return true;
        }

        if (!sender.hasPermission("minecaching.admin")) {
            LocalizedMessages.send(sender, MCMessageKeys.Permission.NO_PERMISSION_ADMIN);
            return true;
        }

        String subcommand = args[0];

        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("minecaching.admin.reload")) {
                LocalizedMessages.send(sender, MCMessageKeys.Permission.NO_PERMISSION_ADMIN_RELOAD);
                return true;
            }

            MinecachingAPI.tInfo(MCMessageKeys.Plugin.RELOADING);
            MinecachingAPI.get().load(!(args.length >= 2 && args[1].equalsIgnoreCase("false")));
            MinecachingAPI.tInfo(MCMessageKeys.Plugin.RELOADED);
            LocalizedMessages.send(sender, MCMessageKeys.Plugin.RELOADED);
        } else if (subcommand.equals("data")) {
            if (!sender.hasPermission("minecaching.admin.data")) {
                LocalizedMessages.send(sender, MCMessageKeys.Permission.NO_PERMISSION_ADMIN_DATA);
                return true;
            }

            int v = MinecachingAPI.getUpdater().getLastCheckResult();

            // Version
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.OVERVIEW_HEADER);
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.PLUGIN_VERSION, Minecaching.getInstance().getDescription().getVersion(), Bukkit.getBukkitVersion().split("-")[0]);
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CHECKING_VERSION, v == 0 ? "UP TO DATE" : v == 1 ? "AHEAD" : v == -1 ? "BEHIND" : "ERROR");
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.SERVER_LANGUAGE, MCMessageKeys.Misc.LOCALE_NAME);
            if (Config.getInstance().debugEvents()) LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.DEBUG_EVENTS_ON, Config.getInstance().getDebugEventsLevel());
            else LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.DEBUG_EVENTS_OFF);

            // Dev
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.DEV_HEADER);
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONFIG_VERSION, Config.getInstance().getConfigVersion());
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.MINECACHE_DATA_VERSION, Config.getInstance().getMinecacheDataVersion());
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.PLAYER_DATA_VERSION, Config.getInstance().getPlayerDataVersion());
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.LOGBOOK_DATA_VERSION, Config.getInstance().getLogbookDataVersion());

            if (v == -1) LocalizedMessages.send(sender, Config.getInstance().autoUpdate() ? MCMessageKeys.Plugin.Update.AVAILABE_AUTO : MCMessageKeys.Plugin.Update.AVAILABLE,
                    MinecachingAPI.getUpdater().getNewestVersion());
        } else if (subcommand.equals("openmenu") && args.length > 1 && sender instanceof Player plr) {
            switch (args[1]) {
                case "create" -> {
                    MCMenus.get().getCreateCacheMenu(MinecachingAPI.get().getPlayerData(plr)).open(plr);
                }
                case "edit" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        MCMenus.get().getEditCacheMenu(MinecachingAPI.get().getPlayerData(plr), MinecachingAPI.get().getMinecache(args[2])).open(plr);
                    }
                }
                case "data" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        CacheDataMenu menu = new CacheDataMenu(MCMessageKeys.Menu.Data.TITLE, MinecachingAPI.get().getMinecache(args[2]), Minecaching.getInstance(), MinecachingAPI.get().getPlayerData(plr));
                        menu.open(plr);
                    }
                }
                case "log" -> {
                    if (args.length >= 3 && !MinecachingAPI.get().getMinecache(args[2]).equals(Minecache.EMPTY)) {
                        LogMenu menu = new LogMenu(MinecachingAPI.get().getMinecache(args[2]), MinecachingAPI.get().getPlayerData(plr));
                        menu.open(plr);
                    }
                }
            }
        } else if (subcommand.equals("correctstats")) {
            MinecachingAPI.get().correctStats();
            LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CORRECTED_STATS);
        } else if (subcommand.equals("conf")) {
            switch (args[1]) {
                case "autoUpdate" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "autoUpdate", "<true|false>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "autoUpdate", "false");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "autoUpdate",
                                Config.getInstance().autoUpdate());
                        break;
                    }

                    boolean autoUpdate = Boolean.parseBoolean(args[2]);

                    Config.getInstance().setAutoUpdate(autoUpdate);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "autoUpdate", autoUpdate);
                }
                case "autoUpdateBranch" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "autoUpdateBranch", "<release|snapshot>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "autoUpdateBranch", "release");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "autoUpdateBranch",
                                Config.getInstance().getUpdateBranch());
                        break;
                    }

                    // We can pass args[2] directly in because the input is handled in Config#setUpdateBranch
                    Config.getInstance().setUpdateBranch(args[2]);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "autoUpdateBranch", Config.getInstance().getUpdateBranch());

                }
                case "debugEvents" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "debugEvents", "<true|false>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "debugEvents", "false");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "debugEvents",
                                Config.getInstance().debugEvents());
                        break;
                    }

                    boolean debugEvents = Boolean.parseBoolean(args[2]);

                    Config.getInstance().setDebugEvents(debugEvents);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "debugEvents", debugEvents);
                }
                case "debugEventsLevel" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "debugEventsLevel", "<0|1|2>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "autoUpdate", "0");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "debugEventsLevel",
                                Config.getInstance().getDebugEventsLevel());
                        break;
                    }

                    int level;

                    try {
                        level = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[2]);
                        return true;
                    }

                    Config.getInstance().setDebugEventsLevel(Math.clamp(0, 2, level));
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "debugEventsLevel", level);
                }
                case "cacheBounds" -> {
                    if (args.length >= 3 && args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "cacheBounds",
                                "\n  Min X: -30000000\n  Min Y: -64\n  Min Z: -30000000\n  Max X: 30000000\n  Max Y: 319\n  Max Z: 30000000");
                        break;
                    } else if (args.length >= 3 && args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "cacheBounds",
                                String.format("\n  Min X: %d\n  Min Y: %d\n  Min Z: %d\n  Max X: %d\n  Max Y: %d\n  Max Z: %d",
                                        Config.getInstance().getMinX(), Config.getInstance().getMinY(), Config.getInstance().getMaxZ(),
                                        Config.getInstance().getMaxX(), Config.getInstance().getMaxY(), Config.getInstance().getMaxZ()));
                        break;
                    }

                    if (args.length < 4) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "cacheBounds",
                                "<minX|minY|minZ|maxX|maxY|maxZ> <coord>");
                    }


                    int coord;

                    try {
                        coord = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[3]);
                        return true;
                    }

                    BoundingBox box = Config.getInstance().getCacheBounds();
                    boolean sendMsg = true;

                    switch (args[2]) {
                        case "minX" -> Config.getInstance().setCacheBounds(box.resize(coord, box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ()));
                        case "minY" -> Config.getInstance().setCacheBounds(box.resize(box.getMinX(), coord, box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ()));
                        case "minZ" -> Config.getInstance().setCacheBounds(box.resize(box.getMinX(), box.getMinY(), coord, box.getMaxX(), box.getMaxY(), box.getMaxZ()));
                        case "maxX" -> Config.getInstance().setCacheBounds(box.resize(box.getMinX(), box.getMinY(), box.getMinZ(), coord, box.getMaxY(), box.getMaxZ()));
                        case "maxY" -> Config.getInstance().setCacheBounds(box.resize(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), coord, box.getMaxZ()));
                        case "maxZ" -> Config.getInstance().setCacheBounds(box.resize(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), coord));
                        default -> {
                            LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "cacheBounds",
                                    "<minX|minY|minZ|maxX|maxY|maxZ> <coord>");
                            sendMsg = false;
                        }
                    }

                    if (sendMsg) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "cacheBounds." + args[2], coord);
                    }
                }
                case "minCacheDist" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "minCacheDist", "<blocks>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "minCacheDist", "25");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "minCacheDist", Config.getInstance().getMinCacheDistance());
                        break;
                    }

                    int blocks;

                    try {
                        blocks = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[2]);
                        return true;
                    }

                    Config.getInstance().setMinCacheDistance(blocks);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "minCacheDist", blocks);
                }
                case "maxLodeDist" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "maxLodeDist", "<blocks>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "maxLodeDist", "50");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "maxLodeDist", Config.getInstance().getMaxLodestoneDistance());
                        break;
                    }

                    int blocks;

                    try {
                        blocks = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[2]);
                        return true;
                    }

                    Config.getInstance().setMaxLodestoneDistance(blocks);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "maxLodeDist", blocks);
                }
                case "findLodeDist" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "findLodeDist", "<blocks>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "findLodeDist", "25");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "findLodeDist", Config.getInstance().getFindLodestoneDistance());
                        break;
                    }

                    int blocks;

                    try {
                        blocks = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[2]);
                        return true;
                    }

                    Config.getInstance().setFindLodestoneDistance(blocks);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "findLodeDist", blocks);
                }
                case "enableType" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "enableType",
                                "<traditional|mystery|multi>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "types",
                                "\n  traditional\n  mystery\n  multi");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        StringBuilder str = new StringBuilder();

                        for (String entry: Config.getInstance().getEnabledTypes()) {
                            str.append("\n  ").append(entry);
                        }

                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "types", str.toString());
                        break;
                    }

                    boolean sendMsg = true;

                    switch (args[2]) {
                        case "traditional" -> Config.getInstance().modifyType(MinecacheType.TRADITIONAL, true);
                        case "mystery" -> Config.getInstance().modifyType(MinecacheType.MYSTERY, true);
                        case "multi" -> Config.getInstance().modifyType(MinecacheType.MULTI, true);
                        default -> {
                            LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "enableType",
                                    "<traditional|mystery|multi>");
                            sendMsg = false;
                        }
                    }

                    if (sendMsg) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "type." + args[2], true);
                    }
                }
                case "disableType" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "disableType",
                                "<traditional|mystery|multi>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "types",
                                "\n  traditional\n  mystery\n  multi");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        StringBuilder str = new StringBuilder();

                        for (String entry: Config.getInstance().getEnabledTypes()) {
                            str.append("\n  ").append(entry);
                        }

                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "types", str.toString());
                        break;
                    }

                    boolean sendMsg = true;

                    switch (args[2]) {
                        case "traditional" -> Config.getInstance().modifyType(MinecacheType.TRADITIONAL, false);
                        case "mystery" -> Config.getInstance().modifyType(MinecacheType.MYSTERY, false);
                        case "multi" -> Config.getInstance().modifyType(MinecacheType.MULTI, false);
                        default -> {
                            LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "disableType",
                                    "<traditional|mystery|multi>");
                            sendMsg = false;
                        }
                    }

                    if (sendMsg) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "type." + args[2], false);
                    }
                }
                case "cacheCreateCooldown" -> {
                    if (args.length < 3) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "cacheCreateCooldown", "<seconds>");
                    }

                    if (args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "cacheCreateCooldown", "300");
                        break;
                    } else if (args[2].equalsIgnoreCase("current")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "cacheCreateCooldown", Config.getInstance().getCacheCreateCooldown());
                        break;
                    }

                    int millis;

                    try {
                        millis = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[2]);
                        return true;
                    }

                    Config.getInstance().setCacheCreateCooldown(millis);
                    LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "createCacheCooldown", millis);
                }
                case "statsScoreOptions" -> {
                    if (args.length >= 3 && args[2].equalsIgnoreCase("default")) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_DEFAULT, "statsScoreOptions",
                                "\n  favorite: 1\n  find: 1\n  ftf: 10\n  hideAD: 0\n  hideR: 2\n  hideNM: 3\n  hideP: 5");
                        break;
                    } else if (args.length >= 3 && args[2].equalsIgnoreCase("current")) {
                        StatsScoreOptions sso = Config.getInstance().getStatsScoreOptions();

                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_CURRENT, "statsScoreOptions",
                                String.format("\n  favorite: %d\n  find: %d\n  ftf: %d\n  hideAD: %d\n  hideR: %d\n  hideNM: %d\n  hideP: %d",
                                        sso.favorite(), sso.find(), sso.ftf(), sso.hideAD(), sso.hideR(), sso.hideNM(), sso.hideP()));
                        break;
                    }

                    if (args.length < 4) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_ARG_COUNT);
                        LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "statsScoreOptions",
                                "<favorite|find|ftf|hideAD|hideNM|hideR|hideP> <score>");
                    }
                    int score;

                    try {
                        score = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        LocalizedMessages.send(sender, MCMessageKeys.Error.FAILED_TO_PARSE_NUMBER, args[3]);
                        return true;
                    }

                    boolean sendMsg = true;

                    switch (args[2]) {
                        case "favorite" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setFavorite(score));
                        case "find" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setFind(score));
                        case "ftf" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setFTF(score));
                        case "hideAD" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setHideAD(score));
                        case "hideNM" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setHideNM(score));
                        case "hideR" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setHideR(score));
                        case "hideP" -> Config.getInstance().setStatsScoreOptions(Config.getInstance().getStatsScoreOptions().setHideP(score));
                        default -> {
                            LocalizedMessages.send(sender, MCMessageKeys.Error.INCORRECT_USAGE);
                            LocalizedMessages.send(sender, MCMessageKeys.Usage.ADMIN_CONF, label, "statsScoreOptions", "<favorite|find|ftf|hideAD|hideNM|hideR|hideP> <score>");
                            sendMsg = false;
                        }
                    }

                    if (sendMsg) {
                        LocalizedMessages.send(sender, MCMessageKeys.Command.Admin.CONF_SET, "statsScoreOptions." + args[2], score);
                    }
                }
            }
        } else return false;

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> enable = new ArrayList<>(List.of("traditional", "mystery", "multi"));
        enable.removeAll(Config.getInstance().getEnabledTypes());
        enable.addAll(List.of("current", "default"));
        List<String> disable = Config.getInstance().getEnabledTypes();
        disable.addAll(List.of("current", "default"));

        if (args.length == 1) {
            return List.of("reload", "data", "correctstats", "conf").stream().filter(s -> s.startsWith(args[0])).toList();
        } else if (args[0].equals("conf")) {
            return switch (args.length) {
                case 2 -> Stream.of("autoUpdate", "autoUpdateBranch", "debugEvents", "debugEventsLevel", "cacheBounds", "minCacheDist",
                        "maxLodeDist", "findLodeDist", "enableType", "disableType", "cacheCreateCooldown", "statsScoreOptions")
                        .filter(s -> s.startsWith(args[1])).toList();
                case 3 -> (switch (args[1]) {
                            case "autoUpdate", "debugEvents" -> List.of("current", "default", "true", "false");
                            case "autoUpdateBranch" -> List.of("current", "default", "release", "snapshot");
                            case "debugEventsLevel" -> List.of("current", "default", "0", "1", "2");
                            case "cacheBounds" -> List.of("current", "default", "minX", "minY", "minZ", "maxX", "maxY", "maxZ");
                            case "enableType" -> enable;
                            case "disableType" -> disable;
                            case "statsScoreOptions" -> List.of("current", "default", "hideAD", "hideNM", "hideR", "hideP", "find", "favorite", "ftf");
                            default -> List.of("current", "default");
                        }).stream().filter(s -> s.startsWith(args[2])).toList();
                default -> List.of();
            };
        } else return List.of();
    }
}
