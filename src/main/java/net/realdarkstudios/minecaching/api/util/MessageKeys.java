package net.realdarkstudios.minecaching.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.jetbrains.annotations.NotNull;

public class MessageKeys {
    // Base paths for Keys that can't use a previous key (such as errors using the base error)
    private static final String addCache = "addcache";
    private static final String editCache = "editcache";
    private static final String listCaches = "listcaches";
    private static final String locateCache = "locatecache";
    private static final String logCache = "logcache";
    private static final String admin = "mcadmin";
    private static final String stats = "mcstats";
    private static final String plugin = "plugin";
    private static final String menu = "menu";
    private static final String minecache = "minecache";
    private static final String permission = "permission";
    private static final String usage = "usage";

    public static class Error {
        public static class Create {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key ALREADY_CREATING = key(".alreadycreating");
            public static final LocalizedMessages.Key NO_CACHE = key(".nocache");
            public static final LocalizedMessages.Key INVALID_TYPE = key(".invalidtype");
            public static final LocalizedMessages.Key NO_NAME = key(".noname");
            public static final LocalizedMessages.Key NO_COORDS = key(".nocoords");
            public static final LocalizedMessages.Key NO_NAV_COORDS = key(".nolodecoords");
            public static final LocalizedMessages.Key NO_CODE = key(".nocode");
            public static final LocalizedMessages.Key NAV_COORDS_TOO_FAR = key(".lodetoofar");
            public static final LocalizedMessages.Key TIME = key(".time");
            public static final LocalizedMessages.Key TOO_CLOSE = key(".tooclose");
            public static final LocalizedMessages.Key OUT_OF_BOUNDS = key(".outofbounds");

            private static LocalizedMessages.Key key(String path) { return MessageKeys.errorKey("error.addcache" + path); }
        }

        public static class Edit {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key ALREADY_EDITING = key(".alreadyediting");
            public static final LocalizedMessages.Key NO_CACHE = key(".nocache");
            public static final LocalizedMessages.Key NO_NAME = key(".noname");
            public static final LocalizedMessages.Key NO_COORDS = key(".nocoords");
            public static final LocalizedMessages.Key NO_NAV_COORDS = key(".nolodecoords");
            public static final LocalizedMessages.Key NO_CODE = key(".nocode");
            public static final LocalizedMessages.Key NAV_COORDS_TOO_FAR = key(".lodetoofar");

            private static LocalizedMessages.Key key(String path) { return MessageKeys.errorKey("error.editcache" + path); }
        }

        public static class Log {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key LOG_OWNED_CACHE = key(".logowncache");
            public static final LocalizedMessages.Key EMPTY_MESSAGE = key(".emptymessage");
            public static final LocalizedMessages.Key EMPTY_CODE = key(".emptycode");
            public static final LocalizedMessages.Key NOTE_FLAG_EMPTY = key(".noteflagempty");
            public static final LocalizedMessages.Key NOT_LOCATING = key(".notlocating");
            public static final LocalizedMessages.Key NEEDS_REVIEWED = key(".needs_reviewed");
            public static final LocalizedMessages.Key ARCHIVED = key(".archived");
            public static final LocalizedMessages.Key DISABLED = key(".disabled");
            public static final LocalizedMessages.Key INVALID = key(".invalid");
            public static final LocalizedMessages.Key LONG_LOG = key(".longlog");
            public static final LocalizedMessages.Key DISTANCE = key(".distance");
            public static final LocalizedMessages.Key CODE = key(".code");
            public static final LocalizedMessages.Key UNSUPPORTED = key(".unsupported");

            private static LocalizedMessages.Key key(String path) { return MessageKeys.errorKey("error.logcache" + path); }
        }

        public static class Misc {
            public static final LocalizedMessages.Key ARCHIVE = key(".archivecache");
            public static final LocalizedMessages.Key ARCHIVE_CANT_ARCHIVE = key(".archivecache.cantarchive");
            public static final LocalizedMessages.Key DELETE = key(".deletecache");
            public static final LocalizedMessages.Key DELETE_OTHERS = key(".deletecache.others");
            public static final LocalizedMessages.Key DISABLE = key(".disablecache");
            public static final LocalizedMessages.Key DISABLE_CANT_DISABLE = key(".disablecache.cantdisable");
            public static final LocalizedMessages.Key LOCATE_ALREADY_LOCATING = key(".locatecache.alreadylocating");
            public static final LocalizedMessages.Key LOCATE_DIFFERENT_WORLD = key(".locatecache.differentworld");
            public static final LocalizedMessages.Key LOGBOOK_NO_SLOTS = key(".logbook.noslots");
            public static final LocalizedMessages.Key MCADMIN_CORRECTING_STATS = key(".mcadmin.cstats");
            public static final LocalizedMessages.Key PUBLISH = key(".publishcache");
            public static final LocalizedMessages.Key PUBLISH_CANT_PUBLISH = key("publishcache.cantpublish");
            public static final LocalizedMessages.Key CACHE_BLOCK = key(".cacheblock");
            public static final LocalizedMessages.Key CACHE_BLOCK_CLICK_HERE = key(".cacheblock.clickhere");
            public static final LocalizedMessages.Key MAINTAINER_NOT_FOUND = key(".maintainer.notfound");
        }

        public static final LocalizedMessages.Key GENERIC = key("");
        public static final LocalizedMessages.Key NON_CONSOLE_COMMAND = key(".execute.console");
        public static final LocalizedMessages.Key ABOVE_COORD_LIMIT = key(".coords.abovelimit");
        public static final LocalizedMessages.Key BELOW_COORD_LIMIT = key(".coords.belowlimit");
        public static final LocalizedMessages.Key CANT_FIND_CACHE = key(".cantfind");
        public static final LocalizedMessages.Key INCORRECT_USAGE = key(".usage");
        public static final LocalizedMessages.Key INCORRECT_ARG_COUNT = key(".usage.argcount");
        public static final LocalizedMessages.Key FAILED_TO_PARSE_NUMBER = key(".usage.listcaches.page");
        public static final LocalizedMessages.Key INVALID_LOG_TYPE = key(".usage.logcache.logtype");
        public static final LocalizedMessages.Key PLUGIN_CREATE_FILE = key(".plugin.createfile");
        public static final LocalizedMessages.Key PLUGIN_UPDATE_FILE = key(".plugin.updatefile");
        public static final LocalizedMessages.Key PLUGIN_PARSE_UUID = key(".plugin.parseuuid");
        public static final LocalizedMessages.Key PLAYER_LIST_EMPTY = key(".playerlistempty");

        private static LocalizedMessages.Key key(String path) {
            return key(path, LocalizedMessages.StyleOptions.ERROR);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions options) {
            return MessageKeys.key("error" + path, options);
        }
    }

    public static class Command {
        public static class Create {
            public static final LocalizedMessages.Key CANCEL = key(".cancel");
            public static final LocalizedMessages.Key CREATE = key(".create");
            public static final LocalizedMessages.Key SAVE = key(".save");
            public static final LocalizedMessages.Key CODE = key(".code");
            public static final LocalizedMessages.Key COORDS = key(".coords");
            public static final LocalizedMessages.Key NAV_COORDS = key(".lodecoords");
            public static final LocalizedMessages.Key NAME = key(".name");
            public static final LocalizedMessages.Key TYPE = key(".type");
            public static final LocalizedMessages.Key MENU_SET_NAME = key(".menu.name");
            public static final LocalizedMessages.Key MENU_SET_CODE = key(".menu.code");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(addCache + path, new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            }
        }

        public static class Edit {
            public static final LocalizedMessages.Key CANCEL = key(".cancel");
            public static final LocalizedMessages.Key EDIT = key(".edit");
            public static final LocalizedMessages.Key SAVE = key(".save");
            public static final LocalizedMessages.Key CODE = key(".code");
            public static final LocalizedMessages.Key COORDS = key(".coords");
            public static final LocalizedMessages.Key NAV_COORDS = key(".lodecoords");
            public static final LocalizedMessages.Key NAME = key(".name");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(editCache + path, new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            }
        }

        public static class List {
            public static final LocalizedMessages.Key PAGE = key(".page");
            public static final LocalizedMessages.Key ENTRY = key(".entry");
            public static final LocalizedMessages.Key FIND = key(".find");
            public static final LocalizedMessages.Key NO_CACHES = key(".nocaches", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key(listCaches + path, styleOptions);
            }
        }

        public static class Locate {
            public static final LocalizedMessages.Key WITHIN_DISTANCE = key(".withindistance");
            public static final LocalizedMessages.Key COORDS = key(".coords");
            public static final LocalizedMessages.Key LODE = key(".lode");
            public static final LocalizedMessages.Key COMPASS_LORE = key(".compass.lore");
            public static final LocalizedMessages.Key CANCEL = key(".cancel");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(locateCache + path, new LocalizedMessages.StyleOptions().setColor(ChatColor.AQUA));
            }
        }

        public static class Log {
            public static final LocalizedMessages.Key FIND = key(".find");
            public static final LocalizedMessages.Key FIND_COUNT = key(".findcount");
            public static final LocalizedMessages.Key FIND_COUNT_WITH_FTFS = key(".findcount.ftf");
            public static final LocalizedMessages.Key LOG = key(".log");
            public static final LocalizedMessages.Key MENU_SET_MESSAGE = key(".menu.message");
            public static final LocalizedMessages.Key MENU_SET_CODE = key(".menu.code");
            public static final LocalizedMessages.Key ARCHIVE_DEFAULT_MESSAGE = key(".message.archive");
            public static final LocalizedMessages.Key DISABLE_DEFAULT_MESSAGE = key(".message.archive");
            public static final LocalizedMessages.Key PUBLISH_DEFAULT_MESSAGE = key(".message.archive");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(logCache + path);
            }
        }

        public static class Admin {
            public static final LocalizedMessages.Key OVERVIEW_HEADER = key(".data.verheader");
            public static final LocalizedMessages.Key DEV_HEADER = key(".data.devheader");
            public static final LocalizedMessages.Key PLUGIN_VERSION = key(".data.mcversion");
            public static final LocalizedMessages.Key CHECKING_VERSION = key(".data.checkingversion");
            public static final LocalizedMessages.Key SERVER_LANGUAGE = key(".data.serverlang");
            public static final LocalizedMessages.Key CONFIG_VERSION = key(".data.configversion");
            public static final LocalizedMessages.Key MINECACHE_DATA_VERSION = key(".data.mcdataversion");
            public static final LocalizedMessages.Key PLAYER_DATA_VERSION = key(".data.plrdataversion");
            public static final LocalizedMessages.Key LOGBOOK_DATA_VERSION = key(".data.logbookdataversion");
            public static final LocalizedMessages.Key DEBUG_EVENTS_ON = key(".data.debugevents.on");
            public static final LocalizedMessages.Key DEBUG_EVENTS_OFF = key(".data.debugevents.off");
            public static final LocalizedMessages.Key CORRECTED_STATS = key(".correctedstats");
            public static final LocalizedMessages.Key CONF_DEFAULT = key(".conf.default");
            public static final LocalizedMessages.Key CONF_CURRENT = key(".conf.current");
            public static final LocalizedMessages.Key CONF_SET = key(".conf.set");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(admin + path);
            }

        }

        public static class Stats {
            public static final LocalizedMessages.Key HEADER = key(".statsheader");
            public static final LocalizedMessages.Key CACHES = key(".caches");
            public static final LocalizedMessages.Key PLAYERS = key(".players");
            public static final LocalizedMessages.Key FINDS = key(".finds");
            public static final LocalizedMessages.Key NEWEST_CACHES = key(".newestcaches");
            public static final LocalizedMessages.Key FAVORITE_CACHE = key(".favoritecache");
            public static final LocalizedMessages.Key MOST_FINDS = key(".mostfinds");
            public static final LocalizedMessages.Key MOST_FTFS = key(".mostftfs");
            public static final LocalizedMessages.Key MOST_HIDES = key(".mosthides");
            public static final LocalizedMessages.Key MOST_ACCOMPLISHED = key(".accomplished");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(stats + path);
            }
        }

        public static class Misc {
            public static final LocalizedMessages.Key ARCHIVE = key("archivecache.archive", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key DELETE = key("deletecache", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key DISABLE = key("disablecache.disable", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key PUBLISH = key("publishcache.publish", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key NO_SUITABLE_MAINTAINERS = key("maintainer.nonesuitable", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        }
    }

    public static class Plugin {
        public static class Data {
            public static final LocalizedMessages.Key LOADED_PLAYERS = key(".loadedplayers");
            public static final LocalizedMessages.Key LOADED_CACHES = key(".loadedcaches");
            public static final LocalizedMessages.Key ATTEMPTING_UPDATE = key(".update");
            public static final LocalizedMessages.Key NOT_ATTEMPTING_UPDATE = key(".update.notattempting");
            public static final LocalizedMessages.Key UPDATE_FAILED = key(".update.fail");
            public static final LocalizedMessages.Key UPDATE_SUCCEEDED = key(".update.succeed");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(plugin + ".data" + path);
            }
        }

        public static class Update {
            public static final LocalizedMessages.Key AVAILABLE = key("", new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            public static final LocalizedMessages.Key AVAILABE_AUTO = key(".auto", new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            public static final LocalizedMessages.Key LATEST = key(".latest");
            public static final LocalizedMessages.Key FAIL_TO_CHECK = key(".failcheck");
            public static final LocalizedMessages.Key FAIL = key(".fail");
            public static final LocalizedMessages.Key GETTING = key(".getting");
            public static final LocalizedMessages.Key DOWNLOADED = key(".downloaded");
            public static final LocalizedMessages.Key APPLIED = key(".applied");
            public static final LocalizedMessages.Key STATUS_AHEAD = key(".ahead");
            public static final LocalizedMessages.Key STATUS_BEHIND = key(".behind");
            public static final LocalizedMessages.Key STATUS_UP_TO_DATE = key(".up_to_date");
            public static final LocalizedMessages.Key AUTO_DISABLED = key(".disabled");
            public static final LocalizedMessages.Key AUTO_DISABLED_DOWNLOAD = key(".disabled.download");
            public static final LocalizedMessages.Key NO_VERSIONS_AVAILABLE = key(".noneavailable");

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key(plugin + ".update" + path, styleOptions);
            }

        }

        public static class Log {
            public static final LocalizedMessages.Key FOUND = key(".found");
            public static final LocalizedMessages.Key DNF = key(".dnf");
            public static final LocalizedMessages.Key NOTE = key(".note");
            public static final LocalizedMessages.Key FLAG = key(".flag");
            public static final LocalizedMessages.Key MAINTAIN = key(".maintain");
            public static final LocalizedMessages.Key DISABLE = key(".disable");
            public static final LocalizedMessages.Key ARCHIVE = key(".archive");
            public static final LocalizedMessages.Key PUBLISH = key(".publish");
            public static final LocalizedMessages.Key INVALID = key(".invalid");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(plugin + ".log" + path);
            }

        }

        public static class Notification {
            public static final LocalizedMessages.Key NOTIFICATION_ALERT = key(".alert");
            public static final LocalizedMessages.Key NOTIFICATION_EDIT = key(".edit");
            public static final LocalizedMessages.Key NOTIFICATION_ARCHIVAL = key(".archival");
            public static final LocalizedMessages.Key NOTIFICATION_DELETION = key(".deletion");
            public static final LocalizedMessages.Key NOTIFICATION_FLAG = key(".flag");
            public static final LocalizedMessages.Key NOTIFICATION_DISABLE = key(".disable");
            public static final LocalizedMessages.Key NOTIFICATION_PUBLISH = key(".publish");
            public static final LocalizedMessages.Key NOTIFICATION_INVALID = key(".invalid");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key(plugin + ".notification" + path, new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            }


        }

        public static final LocalizedMessages.Key NEW_PLAYER_DATA = key(".player.new");
        public static final LocalizedMessages.Key ENABLED = key(".enabled");
        public static final LocalizedMessages.Key DISABLING = key(".disabling");
        public static final LocalizedMessages.Key DISABLED = key(".disabled");
        public static final LocalizedMessages.Key RELOADING = key(".reloading");
        public static final LocalizedMessages.Key RELOADED = key(".reloaded");
        public static final LocalizedMessages.Key REGISTERING_COMMANDS = key(".regcommands");
        public static final LocalizedMessages.Key VERSION_WARNING = key(".1_16_warning");
        public static final LocalizedMessages.Key EXPERIMENTAL = key(".experimental");
        public static final LocalizedMessages.Key LOAD = key(".load");
        public static final LocalizedMessages.Key SAVE = key(".save");
        public static final LocalizedMessages.Key LOGBOOK_FAILED_DELETE = key(".logbook.deleted.fail");
        public static final LocalizedMessages.Key LOGBOOK_DELETED = key(".logbook.deleted");
        public static final LocalizedMessages.Key TRANSLATION_MISSING = key(".localization.missing");
        public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = key(".localization.missingformatarg");
        public static final LocalizedMessages.Key LOCALIZATION_LOADED = key(".localization.loaded");

        private static LocalizedMessages.Key key(String path) {
            return MessageKeys.key(plugin + path);
        }
    }

    public static class Menu {
        public static class CLMOptions {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key ITEM_TYPE_TRADITIONAL = key(".item.type.traditional");
            public static final LocalizedMessages.Key ITEM_TYPE_MYSTERY = key(".item.type.mystery");
            public static final LocalizedMessages.Key ITEM_TYPE_MULTI = key(".item.type.multi");
            public static final LocalizedMessages.Key ITEM_STATUS_PUBLISHED = key(".item.status.published");
            public static final LocalizedMessages.Key ITEM_STATUS_NEEDS_MAINTENANCE = key(".item.status.needs_maintenance");
            public static final LocalizedMessages.Key ITEM_STATUS_DISABLED = key(".item.status.disabled");
            public static final LocalizedMessages.Key ITEM_STATUS_ARCHIVED = key(".item.status.archived");
            public static final LocalizedMessages.Key ITEM_STATUS_REVIEWING = key(".item.status.reviewing");
            public static final LocalizedMessages.Key ITEM_NEWEST = key(".item.newest");
            public static final LocalizedMessages.Key ITEM_OLDEST = key(".item.oldest");
            public static final LocalizedMessages.Key ITEM_MOST_FAVORITES = key(".item.mostfavorites");
            public static final LocalizedMessages.Key ITEM_FTFS_ONLY = key(".item.ftfsonly");
            public static final LocalizedMessages.Key ITEM_FAVORITES_ONLY = key(".item.favoritesonly");
            public static final LocalizedMessages.Key RESET = key(".item.reset");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key("menu.clmoptions" + path);
            }
        }

        public static class Create {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key ITEM_CREATE = key(".item.create", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key ITEM_RESET = key(".item.reset", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY).setBold(true));
            public static final LocalizedMessages.Key ITEM_PREVIEW = key(".item.preview");
            public static final LocalizedMessages.Key ITEM_LOCATION = key(".item.loc");
            public static final LocalizedMessages.Key ITEM_LOCATION_COORDS = key(".item.loccoords");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_LOCATION = key(".item.nav");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_COORDS = key(".item.navcoords");
            public static final LocalizedMessages.Key TYPE_TRADITIONAL = key(".type.traditional", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GREEN));
            public static final LocalizedMessages.Key TYPE_TRADITIONAL_LORE_1 = key(".type.traditional.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key TYPE_TRADITIONAL_LORE_2 = key(".type.traditional.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key TYPE_MYSTERY = key(".type.mystery", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_BLUE));
            public static final LocalizedMessages.Key TYPE_MYSTERY_LORE_1 = key(".type.mystery.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_MYSTERY_LORE_2 = key(".type.mystery.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_MULTI = key(".type.multi", new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            public static final LocalizedMessages.Key TYPE_MULTI_LORE_1 = key(".type.multi.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key TYPE_MULTI_LORE_2 = key(".type.multi.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key SET_CODE = key(".setcode",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac code "));
            public static final LocalizedMessages.Key SET_NAME = key(".setname",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac name "));

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key("menu.creating" + path, styleOptions);
            }
        }

        public static class Data {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key MAINTAINER_TITLE = key(".maintainertitle");
            public static final LocalizedMessages.Key ITEM_ARCHIVE = key(".item.archive", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY));
            public static final LocalizedMessages.Key ITEM_DELETE = key(".item.delete", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_RED).setBold(true));
            public static final LocalizedMessages.Key ITEM_DELETE_LORE = key(".item.delete.lore", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED).setUnderline(true));
            public static final LocalizedMessages.Key ITEM_DISABLE = key(".item.disable", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
            public static final LocalizedMessages.Key ITEM_EDIT = key(".item.edit", new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            public static final LocalizedMessages.Key ITEM_LOCATE = key(".item.locate", new LocalizedMessages.StyleOptions().setColor(ChatColor.AQUA));
            public static final LocalizedMessages.Key ITEM_PUBLISH = key(".item.publish", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GREEN));
            public static final LocalizedMessages.Key ITEM_MAINTAINER = key(".item.maintainer", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key ITEM_MAINTAINER_LORE = key(".item.maintainerlore", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            public static final LocalizedMessages.Key PREVIEW = key(".item.preview");
            public static final LocalizedMessages.Key PREVIEW_MESSAGE = key(".item.preview.message");
            public static final LocalizedMessages.Key PREVIEW_TYPE = key(".item.preview.type");
            public static final LocalizedMessages.Key PREVIEW_CODE = key(".item.preview.code");
            public static final LocalizedMessages.Key PREVIEW_STATUS = key(".item.preview.status");
            public static final LocalizedMessages.Key PREVIEW_COORDS = key(".item.preview.coords");
            public static final LocalizedMessages.Key PREVIEW_AUTHOR = key(".item.preview.author");
            public static final LocalizedMessages.Key PREVIEW_MAINTAINER = key(".item.preview.maintainer");
            public static final LocalizedMessages.Key PREVIEW_NAVIGATION_COORDS = key(".item.preview.navcoords");
            public static final LocalizedMessages.Key PREVIEW_HIDDEN = key(".item.preview.hidden");
            public static final LocalizedMessages.Key PREVIEW_FINDS = key(".item.preview.finds");
            public static final LocalizedMessages.Key PREVIEW_FAVORITES = key(".item.preview.favorites");
            public static final LocalizedMessages.Key CACHE_NAME = key(".item.cache.name");
            public static final LocalizedMessages.Key CACHE_CODE = key(".item.cache.code");
            public static final LocalizedMessages.Key SET_MAINTAINER = key(".setmaintainer", LocalizedMessages.StyleOptions.SUCCESS);

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key("menu.data" + path, styleOptions);
            }
        }

        public static class Edit {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key ITEM_RESET = key(".item.reset");
            public static final LocalizedMessages.Key ITEM_PREVIEW = key(".item.preview");
            public static final LocalizedMessages.Key ITEM_LOCATION = key(".item.loc");
            public static final LocalizedMessages.Key ITEM_LOCATION_COORDS = key(".item.loccoords");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_LOCATION = key(".item.nav");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_COORDS = key(".item.navcoords");
            public static final LocalizedMessages.Key ITEM_STOP_EDITING = key(".item.stopediting", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY));
            public static final LocalizedMessages.Key ITEM_STOP_EDITING_LORE = key(".item.stopediting.lore");
            public static final LocalizedMessages.Key SET_CODE = key(".setcode",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ec code "));
            public static final LocalizedMessages.Key SET_NAME = key(".setname",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ec name "));

            private static LocalizedMessages.@NotNull Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key("menu.editing" + path, styleOptions);
            }
        }

        public static class List {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key PREVIOUS = key(".prev", new LocalizedMessages.StyleOptions().setUnderline(true));
            public static final LocalizedMessages.Key REFRESH = key(".refresh", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
            public static final LocalizedMessages.Key NEXT = key(".next", new LocalizedMessages.StyleOptions().setUnderline(true));
            public static final LocalizedMessages.Key ITEM_OPTIONS = key(".item.options");
            public static final LocalizedMessages.Key ITEM_CACHE = key(".item.cache");

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key("menu.list" + path, styleOptions);
            }
        }

        public static class Log {
            public static final LocalizedMessages.Key TITLE = key(".title");
            public static final LocalizedMessages.Key PICK_TITLE = key(".pick.title");
            public static final LocalizedMessages.Key ITEM_CODE = key(".item.code");
            public static final LocalizedMessages.Key ITEM_FAVORITE = key(".item.favorite", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key ITEM_UNFAVORITE = key(".item.unfavorite", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key ITEM_MESSAGE = key(".item.message");
            public static final LocalizedMessages.Key ITEM_CUSTOM_MESSAGE = key(".item.message.custom");
            public static final LocalizedMessages.Key MESSAGE_FOUND = key(".message.found");
            public static final LocalizedMessages.Key MESSAGE_DNF = key(".message.dnf");
            public static final LocalizedMessages.Key MESSAGE_NOTE = key(".message.note");
            public static final LocalizedMessages.Key MESSAGE_OTHER = key(".message.flag");
            public static final LocalizedMessages.Key TYPE_FOUND = key(".type.found", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key TYPE_DNF = key(".type.dnf", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_NOTE = key(".type.note", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
            public static final LocalizedMessages.Key TYPE_FLAG = key(".type.flag", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            public static final LocalizedMessages.Key SET_CODE = key(".setcode",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/log code "));
            public static final LocalizedMessages.Key SET_MESSAGE = key(".setmessage",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/log message "));

            private static LocalizedMessages.Key key(String path) {
                return key(path, LocalizedMessages.StyleOptions.NONE);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MessageKeys.key("menu.log" + path, styleOptions);
            }
        }

        public static final LocalizedMessages.Key CONFIRMATION = key(menu + ".confirmation.title");
        public static final LocalizedMessages.Key CANCEL = key(menu + ".cancel", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY).setBold(true));
        public static final LocalizedMessages.Key CLOSE = key(menu + ".close", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED).setBold(true));
        public static final LocalizedMessages.Key GO_BACK = key(menu + ".goback", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
        public static final LocalizedMessages.Key SAVE = key(menu + ".save", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN).setBold(true));
        public static final LocalizedMessages.Key SAVE_LORE = key(menu + ".save.lore");
        public static final LocalizedMessages.Key ITEM_ERROR = key(menu + ".item.error");
        public static final LocalizedMessages.Key ITEM_OUT_OF_BOUNDS = key(menu + ".item.outofbounds");
        public static final LocalizedMessages.Key PERM_CHANGE = key(menu + ".permchange", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
    }

        public static class Permission {
            public static final LocalizedMessages.Key NO_PERMISSION = key(".none");
            public static final LocalizedMessages.Key NO_PERMISSION_EDITCACHE = key(".none.editcache");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN = key(".none.mcadmin");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_RELOAD = key(".none.mcadmin.reload");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_DATA = key(".none.mcadmin.data");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key("permission" + path);
            }
        }

        public static class Usage {
            public static final LocalizedMessages.Key CREATE = key(".addcache");
            public static final LocalizedMessages.Key CREATE_OPTIONS = key(".addcache.options");
            public static final LocalizedMessages.Key CREATE_CANCEL = key(".addcache.cancel");
            public static final LocalizedMessages.Key CREATE_CODE = key(".addcache.code");
            public static final LocalizedMessages.Key CREATE_COORDS = key(".addcache.coords");
            public static final LocalizedMessages.Key CREATE_NAVIGATION_COORDS = key(".addcache.lodecoords");
            public static final LocalizedMessages.Key CREATE_NAME = key(".addcache.name");
            public static final LocalizedMessages.Key ADD_SAVE = key(".addcache.save");
            public static final LocalizedMessages.Key ADD_TYPE = key(".addcache.type");
            public static final LocalizedMessages.Key ARCHIVE = key(".archivecache");
            public static final LocalizedMessages.Key DELETE = key(".deletecache");
            public static final LocalizedMessages.Key DISABLE = key(".disablecache");
            public static final LocalizedMessages.Key EDIT = key(".editcache");
            public static final LocalizedMessages.Key EDIT_OPTIONS = key(".editcache.options");
            public static final LocalizedMessages.Key EDIT_CANCEL = key(".editcache.cancel");
            public static final LocalizedMessages.Key EDIT_CODE = key(".editcache.code");
            public static final LocalizedMessages.Key EDIT_COORDS = key(".editcache.coords");
            public static final LocalizedMessages.Key EDIT_NAVIGATION_COORDS = key(".editcache.lodecoords");
            public static final LocalizedMessages.Key EDIT_NAME = key(".editcache.name");
            public static final LocalizedMessages.Key EDIT_SAVE = key(".editcache.save");
            public static final LocalizedMessages.Key LIST = key(".listcaches");
            public static final LocalizedMessages.Key LOCATE = key(".locatecache");
            public static final LocalizedMessages.Key LOCATE_COORD_OPTIONS = key(".locatecache.options.coord");
            public static final LocalizedMessages.Key LOCATE_LODE_OPTIONS = key(".locatecache.options.lode");
            public static final LocalizedMessages.Key LOGBOOK = key(".logbook");
            public static final LocalizedMessages.Key LOG = key(".logcache");
            public static final LocalizedMessages.Key LOG_FOUND = key(".logcache.found");
            public static final LocalizedMessages.Key MAINTAINER = key(".maintainer");
            public static final LocalizedMessages.Key ADMIN = key(".mcadmin");
            public static final LocalizedMessages.Key ADMIN_CONF = key(".mcadmin.conf");
            public static final LocalizedMessages.Key STATS = key(".mcstats");
            public static final LocalizedMessages.Key PUBLISH = key(".publishcache");

            private static LocalizedMessages.Key key(String path) {
                return MessageKeys.key("usage" + path, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            }
        }

        public static class Misc {
            public static class Cache {
                public static final LocalizedMessages.Key TYPE_TRADITIONAL = key(".type.traditional");
                public static final LocalizedMessages.Key TYPE_MYSTERY = key(".type.mystery");
                public static final LocalizedMessages.Key TYPE_MULTI = key(".type.multi");
                public static final LocalizedMessages.Key TYPE_INVALID = key(".type.invalid");
                public static final LocalizedMessages.Key STATUS_PUBLISHED = key(".status.published");
                public static final LocalizedMessages.Key STATUS_NEEDS_MAINTENANCE = key(".status.needs_maintenance");
                public static final LocalizedMessages.Key STATUS_DISABLED = key(".status.disabled");
                public static final LocalizedMessages.Key STATUS_ARCHIVED = key(".status.archived");
                public static final LocalizedMessages.Key STATUS_REVIEWING = key(".status.reviewing");
                public static final LocalizedMessages.Key STATUS_INVALID = key(".status.invalid");
                public static final LocalizedMessages.Key YAML = key(".yaml_error");
                public static final LocalizedMessages.Key YCODE_0 = key(".yaml_code_0");
                public static final LocalizedMessages.Key YCODE_1 = key(".yaml_code_1");
                public static final LocalizedMessages.Key YCODE_2 = key(".yaml_code_2");
                public static final LocalizedMessages.Key YCODE_3 = key(".yaml_code_3");
                public static final LocalizedMessages.Key YCODE_4 = key(".yaml_code_4");
                public static final LocalizedMessages.Key YCODE_5 = key(".yaml_code_5");
                public static final LocalizedMessages.Key YCODE_OTHER = key(".yaml_code_other");

                private static LocalizedMessages.Key key(String path) {
                    return MessageKeys.key("minecache" + path);
                }
            }

            public static final LocalizedMessages.Key LOCALE_NAME = key("locale.name");
    }

    // Makes some common keys more accessible
    public static final LocalizedMessages.Key ERROR = Error.GENERIC;
    public static final LocalizedMessages.Key INCORRECT_USAGE = Error.INCORRECT_USAGE;
    public static final LocalizedMessages.Key TRANSLATION_MISSING = Plugin.TRANSLATION_MISSING;
    public static final LocalizedMessages.Key TRANSLATION_FORMAT_ARG_MISSING = Plugin.TRANSLATION_FORMAT_ARG_MISSING;

    private static LocalizedMessages.Key key(String path) {
        return key(path, LocalizedMessages.StyleOptions.NONE);
    }

    private static LocalizedMessages.Key errorKey(String path) {
        return key(path, LocalizedMessages.StyleOptions.ERROR);
    }

    private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
        return new LocalizedMessages.Key(MinecachingAPI.getLocalization(), path, styleOptions);
    }

    public static void init() {
    }
}
