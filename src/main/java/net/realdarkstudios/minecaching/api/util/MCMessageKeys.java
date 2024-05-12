package net.realdarkstudios.minecaching.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.realdarkstudios.commons.util.LocalizedMessages;
import net.realdarkstudios.commons.util.MessageKeys;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MCMessageKeys {
    private static HashMap<String, LocalizedMessages.Key> keyMap = new HashMap<>();
    public static final LocalizedMessages.StyleOptions DEFAULT_COLOR = new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD);

    public static class Error {
        public static class Create {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key ALREADY_CREATING = key("already_creating");
            public static final LocalizedMessages.Key INVALID_TYPE = key("invalid_type");
            public static final LocalizedMessages.Key TIME = key("time");

            /* MODIFY KEYS */

            public static final LocalizedMessages.Key NO_CACHE = Modify.NO_CACHE;
            public static final LocalizedMessages.Key NO_CODE = Modify.NO_CODE;
            public static final LocalizedMessages.Key NO_COORDS = Modify.NO_COORDS;
            public static final LocalizedMessages.Key NO_NAME = Modify.NO_NAME;
            public static final LocalizedMessages.Key NO_NAV_COORDS = Modify.NO_NAV_COORDS;
            public static final LocalizedMessages.Key NAV_COORDS_TOO_FAR = Modify.NAV_COORDS_TOO_FAR;
            public static final LocalizedMessages.Key OUT_OF_BOUNDS = Modify.OUT_OF_BOUNDS;
            public static final LocalizedMessages.Key TOO_CLOSE = Modify.TOO_CLOSE;

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("error.create", path, LocalizedMessages.StyleOptions.ERROR);
            }
        }

        public static class Edit {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key ALREADY_EDITING = key("already_editing");

            /* MODIFY KEYS */

            public static final LocalizedMessages.Key NO_CACHE = Modify.NO_CACHE;
            public static final LocalizedMessages.Key NO_CODE = Modify.NO_CODE;
            public static final LocalizedMessages.Key NO_COORDS = Modify.NO_COORDS;
            public static final LocalizedMessages.Key NO_NAME = Modify.NO_NAME;
            public static final LocalizedMessages.Key NO_NAV_COORDS = Modify.NO_NAV_COORDS;
            public static final LocalizedMessages.Key NAV_COORDS_TOO_FAR = Modify.NAV_COORDS_TOO_FAR;
            public static final LocalizedMessages.Key OUT_OF_BOUNDS = Modify.OUT_OF_BOUNDS;
            public static final LocalizedMessages.Key TOO_CLOSE = Modify.TOO_CLOSE;

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("error.edit", path, LocalizedMessages.StyleOptions.ERROR);
            }
        }

        public static class Log {
            public static final LocalizedMessages.Key GENERAL = key("");
            public static final LocalizedMessages.Key EMPTY_CODE = key("empty_code");
            public static final LocalizedMessages.Key EMPTY_MESSAGE = key("empty_message");
            public static final LocalizedMessages.Key INCORRECT_CODE = key("incorrect_code");
            public static final LocalizedMessages.Key INVALID_TYPE = key("invalid_type");

            public static final LocalizedMessages.Key LONG_LOG = key("long_log");
            public static final LocalizedMessages.Key LOG_BY_OWNER = key("log_by_owner");
            public static final LocalizedMessages.Key NOTE_OR_FLAG_MSG_EMPTY = key("note_or_flag_msg_empty");
            public static final LocalizedMessages.Key NOT_LOCATING = key("not_locating");
            public static final LocalizedMessages.Key NEEDS_REVIEWED = key("needs_reviewed");
            public static final LocalizedMessages.Key STATUS_ARCHIVED = key("status_archived");
            public static final LocalizedMessages.Key STATUS_DISABLED = key("status_disabled");
            public static final LocalizedMessages.Key STATUS_INVALID = key("status_invalid");
            public static final LocalizedMessages.Key TOO_FAR = key("too_far");
            public static final LocalizedMessages.Key UNSUPPORTED = key("unsupported");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("error.log", path, LocalizedMessages.StyleOptions.ERROR);
            }
        }

        public static class Misc {
            // ARCHIVE
            public static final LocalizedMessages.Key ARCHIVE = key("archive");
            public static final LocalizedMessages.Key CANT_ARCHIVE = key("cant_archive");

            // DELETE
            public static final LocalizedMessages.Key DELETE = key("delete");
            public static final LocalizedMessages.Key DELETE_OTHERS = key("delete_others_caches");

            // DISABLE
            public static final LocalizedMessages.Key DISABLE = key("disable");
            public static final LocalizedMessages.Key CANT_DISABLE = key("cant_disable");

            // LOCATING
            public static final LocalizedMessages.Key ALREADY_LOCATING = key("already_locating");
            public static final LocalizedMessages.Key DIFFERENT_WORLD = key("different_world");

            // PUBLISH
            public static final LocalizedMessages.Key PUBLISH = key("publish");
            public static final LocalizedMessages.Key CANT_PUBLISH = key("cant_publish");

            // COORD LIMITS
            public static final LocalizedMessages.Key ABOVE_COORD_LIMIT = key("above_coord_limit");
            public static final LocalizedMessages.Key BELOW_COORD_LIMIT = key("below_coord_limit");

            // MISC
            public static final LocalizedMessages.Key CACHE_BLOCK = key("cache_block");
            public static final LocalizedMessages.Key CACHE_BLOCK_CLICK_HERE = key("cache_block_click_here");
            public static final LocalizedMessages.Key CORRECT_STATS = key("correct_stats");
            public static final LocalizedMessages.Key MAINTAINER_NOT_FOUND = key("maintainer_not_found");
            public static final LocalizedMessages.Key NOT_LOCATING = key("not_locating");
            public static final LocalizedMessages.Key NO_SLOTS = key("no_slots");
            public static final LocalizedMessages.Key PLAYER_LIST_EMPTY = key("player_list_empty");

            // PLUGIN
            public static final LocalizedMessages.Key CREATE_FILE = key("create_file");
            public static final LocalizedMessages.Key UPDATE_FILE = key("update_file");
            public static final LocalizedMessages.Key PARSE_UUID = key("parse_uuid");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("error.misc", path, LocalizedMessages.StyleOptions.ERROR);
            }
        }

        public static class Modify {
            public static final LocalizedMessages.Key NO_CACHE = key("no_cache");
            public static final LocalizedMessages.Key NO_CODE = key("no_code");
            public static final LocalizedMessages.Key NO_COORDS = key("no_coords");
            public static final LocalizedMessages.Key NO_NAME = key("no_name");
            public static final LocalizedMessages.Key NO_NAV_COORDS = key("no_nav_coords");
            public static final LocalizedMessages.Key NAV_COORDS_TOO_FAR = key("nav_too_far");
            public static final LocalizedMessages.Key OUT_OF_BOUNDS = key("out_of_bounds");
            public static final LocalizedMessages.Key TOO_CLOSE = key("too_close");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("error.modify", path, LocalizedMessages.StyleOptions.ERROR);
            }
        }

        public static final LocalizedMessages.Key CANT_FIND_CACHE = key("cant_find_cache");

        private static LocalizedMessages.Key key(String path) {
            return MCMessageKeys.keyHelper("error", path, LocalizedMessages.StyleOptions.ERROR);
        }
    }

    public static class Command {
        public static class Admin {
            public static final LocalizedMessages.Key OVERVIEW_HEADER = key("data.overview_header");
            public static final LocalizedMessages.Key DEV_HEADER = key("data.dev_info_header");
            public static final LocalizedMessages.Key JAR_HEADER = key("data.jar_info_header");
            public static final LocalizedMessages.Key PLUGIN_VERSION = key("data.version");
            public static final LocalizedMessages.Key LAST_MODIFIED = key("data.last_modified");
            public static final LocalizedMessages.Key COMMONS_VERSION = key("data.commons_version");
            public static final LocalizedMessages.Key CHECKING_VERSION = key("data.checking_version");
            public static final LocalizedMessages.Key SERVER_LANGUAGE = key("data.server_language");
            public static final LocalizedMessages.Key CONFIG_VERSION = key("data.config_data_version");
            public static final LocalizedMessages.Key MINECACHE_DATA_VERSION = key("data.minecache_data_version");
            public static final LocalizedMessages.Key PLAYER_DATA_VERSION = key("data.player_data_version");
            public static final LocalizedMessages.Key LOGBOOK_DATA_VERSION = key("data.logbook_data_version");
            public static final LocalizedMessages.Key DEBUG_EVENTS_ON = key("data.debug_events.on");
            public static final LocalizedMessages.Key DEBUG_EVENTS_OFF = key("data.debug_events.off");
            public static final LocalizedMessages.Key HASH = key("data.hash");
            public static final LocalizedMessages.Key UPTIME = key("data.uptime");
            public static final LocalizedMessages.Key GENERATING_FULL_REPORT = key("data.generating_full_report");
            public static final LocalizedMessages.Key GENERATED_FULL_REPORT = key("data.generated_full_report");
            public static final LocalizedMessages.Key REPORT_GENERATED_TIME = key("data.report_generated_time");
            public static final LocalizedMessages.Key PLAYER_ENTRY = key("data.player_entry");
            public static final LocalizedMessages.Key CACHE_ENTRY = key("data.cache_entry");
            public static final LocalizedMessages.Key CORRECTED_STATS = key("corrected_stats");
            public static final LocalizedMessages.Key CONF_DEFAULT = key("conf.default");
            public static final LocalizedMessages.Key CONF_CURRENT = key("conf.current");
            public static final LocalizedMessages.Key CONF_SET = key("conf.set");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.admin", path, DEFAULT_COLOR);
            }
        }

        public static class Create {
            public static final LocalizedMessages.Key CANCEL = Modify.CANCEL;
            public static final LocalizedMessages.Key CREATE = key("");
            public static final LocalizedMessages.Key SAVE = Modify.SAVE;
            public static final LocalizedMessages.Key CODE = Modify.CODE;
            public static final LocalizedMessages.Key COORDS = Modify.COORDS;
            public static final LocalizedMessages.Key NAV_COORDS = Modify.NAV_COORDS;
            public static final LocalizedMessages.Key NAME = Modify.NAME;
            public static final LocalizedMessages.Key TYPE = key("type");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.create", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            }
        }

        public static class Edit {
            public static final LocalizedMessages.Key EDIT = key("edit");
            public static final LocalizedMessages.Key CANCEL = Modify.CANCEL;
            public static final LocalizedMessages.Key SAVE = Modify.SAVE;
            public static final LocalizedMessages.Key CODE = Modify.CODE;
            public static final LocalizedMessages.Key COORDS = Modify.COORDS;
            public static final LocalizedMessages.Key NAV_COORDS = Modify.NAV_COORDS;
            public static final LocalizedMessages.Key NAME = Modify.NAME;

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.edit", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            }
        }

        public static class List {
            public static final LocalizedMessages.Key PAGE = key("page");
            public static final LocalizedMessages.Key ENTRY = key("entry");
            public static final LocalizedMessages.Key FIND = key("find");
            public static final LocalizedMessages.Key NO_CACHES = key("no_caches", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));

            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("command.list", path, styleOptions);
            }
        }

        public static class Locate {
            public static final LocalizedMessages.Key WITHIN_DISTANCE = key("within_distance");
            public static final LocalizedMessages.Key COORDS = key("coords");
            public static final LocalizedMessages.Key LODE = key("lode");
            public static final LocalizedMessages.Key COMPASS_LORE = key("compass.lore");
            public static final LocalizedMessages.Key CANCEL = key("cancel");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.locate", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.AQUA));
            }
        }

        public static class Log {
            public static final LocalizedMessages.Key FIND = key("find");
            public static final LocalizedMessages.Key FIND_COUNT = key("find_count");
            public static final LocalizedMessages.Key FIND_COUNT_WITH_FTFS = key("find_count_with_ftfs");
            public static final LocalizedMessages.Key LOG = key("log");
            public static final LocalizedMessages.Key ARCHIVE_DEFAULT_MESSAGE = key("message.archive");
            public static final LocalizedMessages.Key DISABLE_DEFAULT_MESSAGE = key("message.disable");
            public static final LocalizedMessages.Key PUBLISH_DEFAULT_MESSAGE = key("message.publish");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.log", path, DEFAULT_COLOR);
            }
        }

        public static class Modify {
            public static final LocalizedMessages.Key CANCEL = key("cancel");
            public static final LocalizedMessages.Key SAVE = key("save");
            public static final LocalizedMessages.Key CODE = key("code");
            public static final LocalizedMessages.Key COORDS = key("coords");
            public static final LocalizedMessages.Key NAV_COORDS = key("lodecoords");
            public static final LocalizedMessages.Key NAME = key("name");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.modify", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            }
        }

        public static class Stats {
            public static final LocalizedMessages.Key HEADER = key("header");
            public static final LocalizedMessages.Key CACHES = key("caches");
            public static final LocalizedMessages.Key PLAYERS = key("players");
            public static final LocalizedMessages.Key FINDS = key("finds");
            public static final LocalizedMessages.Key NEWEST_CACHES = key("newest_caches");
            public static final LocalizedMessages.Key FAVORITE_CACHE = key("favorite_cache");
            public static final LocalizedMessages.Key MOST_FINDS = key("most_finds");
            public static final LocalizedMessages.Key MOST_FTFS = key("most_ftfs");
            public static final LocalizedMessages.Key MOST_HIDES = key("most_hides");
            public static final LocalizedMessages.Key MOST_ACCOMPLISHED = key("most_accomplished");
            public static final LocalizedMessages.Key NONE = key("none");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("command.stats", path, DEFAULT_COLOR);
            }
        }

        public static class Misc {
            public static final LocalizedMessages.Key ARCHIVE = key("archive", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key DELETE = key("delete", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key DISABLE = key("disable", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key HINT = key("hint");
            public static final LocalizedMessages.Key PUBLISH = key("publish", LocalizedMessages.StyleOptions.SUCCESS);
            public static final LocalizedMessages.Key NO_SUITABLE_MAINTAINERS = key("no_suitable_maintainers", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            public static final LocalizedMessages.Key SECRET = key("secret");

            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("command.misc", path, styleOptions);
            }
        }
    }

    public static class Plugin {
        public static class Data {
            public static final LocalizedMessages.Key LOADED_PLAYERS = key("loaded_players");
            public static final LocalizedMessages.Key LOADED_CACHES = key("loaded_caches");
            public static final LocalizedMessages.Key ATTEMPTING_UPDATE = key("update");
            public static final LocalizedMessages.Key NOT_ATTEMPTING_UPDATE = key("update.not_attempting");
            public static final LocalizedMessages.Key UPDATE_FAILED = key("update.fail");
            public static final LocalizedMessages.Key UPDATE_SUCCEEDED = key("update.succeed");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("plugin.data", path, DEFAULT_COLOR);
            }
        }

        public static class Update {
            public static final LocalizedMessages.Key AVAILABLE = MessageKeys.Update.AVAILABLE;
            public static final LocalizedMessages.Key AVAILABE_AUTO = MessageKeys.Update.AVAILABE_AUTO;
            public static final LocalizedMessages.Key LATEST = MessageKeys.Update.LATEST;
            public static final LocalizedMessages.Key FAIL_TO_CHECK = MessageKeys.Update.FAIL_TO_CHECK;
            public static final LocalizedMessages.Key FAIL = MessageKeys.Update.FAIL;
            public static final LocalizedMessages.Key GETTING = MessageKeys.Update.GETTING;
            public static final LocalizedMessages.Key DOWNLOADED = MessageKeys.Update.DOWNLOADED;
            public static final LocalizedMessages.Key APPLIED = MessageKeys.Update.APPLIED;
            public static final LocalizedMessages.Key STATUS_AHEAD = MessageKeys.Update.STATUS_AHEAD;
            public static final LocalizedMessages.Key STATUS_BEHIND = MessageKeys.Update.STATUS_BEHIND;
            public static final LocalizedMessages.Key STATUS_UP_TO_DATE = MessageKeys.Update.STATUS_UP_TO_DATE;
            public static final LocalizedMessages.Key AUTO_DISABLED = MessageKeys.Update.AUTO_DISABLED;
            public static final LocalizedMessages.Key AUTO_DISABLED_DOWNLOAD = MessageKeys.Update.AUTO_DISABLED_DOWNLOAD;
            public static final LocalizedMessages.Key NO_VERSIONS_AVAILABLE = MessageKeys.Update.NO_VERSIONS_AVAILABLE;
        }

        public static class Log {
            public static final LocalizedMessages.Key FOUND = key("found");
            public static final LocalizedMessages.Key DNF = key("dnf");
            public static final LocalizedMessages.Key NOTE = key("note");
            public static final LocalizedMessages.Key FLAG = key("flag");
            public static final LocalizedMessages.Key MAINTAIN = key("maintain");
            public static final LocalizedMessages.Key DISABLE = key("disable");
            public static final LocalizedMessages.Key ARCHIVE = key("archive");
            public static final LocalizedMessages.Key PUBLISH = key("publish");
            public static final LocalizedMessages.Key INVALID = key("invalid");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("plugin.log", path, DEFAULT_COLOR);
            }
        }

        public static class Notification {
            public static final LocalizedMessages.Key ALERT = key("alert");
            public static final LocalizedMessages.Key EDIT = key("edit");
            public static final LocalizedMessages.Key ARCHIVAL = key("archival");
            public static final LocalizedMessages.Key DELETION = key("deletion");
            public static final LocalizedMessages.Key FLAG = key("flag");
            public static final LocalizedMessages.Key DISABLE = key("disable");
            public static final LocalizedMessages.Key PUBLISH = key("publish");
            public static final LocalizedMessages.Key INVALID = key("invalid");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("plugin.notification", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            }
        }

        public static final LocalizedMessages.Key VERSION_WARNING = key("1_16_warning");
        public static final LocalizedMessages.Key DISABLED = key("disabled");
        public static final LocalizedMessages.Key DISABLING = key("disabling");
        public static final LocalizedMessages.Key ENABLED = key("enabled");
        public static final LocalizedMessages.Key EXPERIMENTAL = key("experimental");
        public static final LocalizedMessages.Key LOAD = key("load");
        public static final LocalizedMessages.Key LOGBOOK_DELETED = key("logbook_deleted");
        public static final LocalizedMessages.Key LOGBOOK_FAILED_TO_DELETE = key("logbook_failed_to_delete");
        public static final LocalizedMessages.Key NEW_PLAYER_DATA = key("new_player");
        public static final LocalizedMessages.Key REGISTERING_COMMANDS = key("registering_commands");
        public static final LocalizedMessages.Key RELOADED = key("reloaded");
        public static final LocalizedMessages.Key RELOADING = key("reloading");
        public static final LocalizedMessages.Key SAVE = key("save");

        private static LocalizedMessages.Key key(String path) {
            return MCMessageKeys.keyHelper("plugin", path, DEFAULT_COLOR);
        }
    }

    public static class Menu {
        public static class CLMOptions {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key ITEM_TYPE_TRADITIONAL = key("item.type.traditional");
            public static final LocalizedMessages.Key ITEM_TYPE_MYSTERY = key("item.type.mystery");
            public static final LocalizedMessages.Key ITEM_TYPE_MULTI = key("item.type.multi");
            public static final LocalizedMessages.Key ITEM_STATUS_PUBLISHED = key("item.status.published");
            public static final LocalizedMessages.Key ITEM_STATUS_NEEDS_MAINTENANCE = key("item.status.needs_maintenance");
            public static final LocalizedMessages.Key ITEM_STATUS_DISABLED = key("item.status.disabled");
            public static final LocalizedMessages.Key ITEM_STATUS_ARCHIVED = key("item.status.archived");
            public static final LocalizedMessages.Key ITEM_STATUS_REVIEWING = key("item.status.reviewing");
            public static final LocalizedMessages.Key ITEM_NEWEST = key("item.newest");
            public static final LocalizedMessages.Key ITEM_OLDEST = key("item.oldest");
            public static final LocalizedMessages.Key ITEM_MOST_FAVORITES = key("item.most_favorites");
            public static final LocalizedMessages.Key ITEM_FTFS_ONLY = key("item.ftfs_only");
            public static final LocalizedMessages.Key ITEM_FAVORITES_ONLY = key("item.favorites_only");
            public static final LocalizedMessages.Key RESET = key("item.reset");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("menu.clmoptions", path, DEFAULT_COLOR);
            }
        }

        public static class Create {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key ITEM_CREATE = key("item.create", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key ITEM_RESET = Modify.ITEM_RESET;
            public static final LocalizedMessages.Key ITEM_PREVIEW = Modify.ITEM_PREVIEW;
            public static final LocalizedMessages.Key ITEM_LOCATION = Modify.ITEM_LOCATION;
            public static final LocalizedMessages.Key ITEM_LOCATION_COORDS = Modify.ITEM_LOCATION_COORDS;
            public static final LocalizedMessages.Key ITEM_NAVIGATION_LOCATION = Modify.ITEM_NAVIGATION_LOCATION;
            public static final LocalizedMessages.Key ITEM_NAVIGATION_COORDS = Modify.ITEM_NAVIGATION_COORDS;
            public static final LocalizedMessages.Key TYPE_TRADITIONAL = key("type.traditional", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GREEN));
            public static final LocalizedMessages.Key TYPE_TRADITIONAL_LORE_1 = key("type.traditional.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key TYPE_TRADITIONAL_LORE_2 = key("type.traditional.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN));
            public static final LocalizedMessages.Key TYPE_MYSTERY = key("type.mystery", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_BLUE));
            public static final LocalizedMessages.Key TYPE_MYSTERY_LORE_1 = key("type.mystery.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_MYSTERY_LORE_2 = key("type.mystery.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_MULTI = key("type.multi", new LocalizedMessages.StyleOptions().setColor(ChatColor.GOLD));
            public static final LocalizedMessages.Key TYPE_MULTI_LORE_1 = key("type.multi.lore1", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key TYPE_MULTI_LORE_2 = key("type.multi.lore2", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key SET_CODE = key("setcode",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac code "));
            public static final LocalizedMessages.Key SET_NAME = key("setname",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac name "));
            public static final LocalizedMessages.Key SET_HINT = key("sethint",
                    new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac hint "));
            
            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.creating", path, styleOptions);
            }
        }

        public static class Data {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key MAINTAINER_TITLE = key("maintainer_title");
            public static final LocalizedMessages.Key ITEM_ARCHIVE = key("item.archive", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY));
            public static final LocalizedMessages.Key ITEM_DELETE = key("item.delete", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_RED).setBold(true));
            public static final LocalizedMessages.Key ITEM_DELETE_LORE = key("item.delete.lore", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED).setUnderline(true));
            public static final LocalizedMessages.Key ITEM_DISABLE = key("item.disable", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
            public static final LocalizedMessages.Key ITEM_EDIT = key("item.edit", new LocalizedMessages.StyleOptions().setColor(ChatColor.LIGHT_PURPLE));
            public static final LocalizedMessages.Key ITEM_LOCATE = key("item.locate", new LocalizedMessages.StyleOptions().setColor(ChatColor.AQUA));
            public static final LocalizedMessages.Key ITEM_PUBLISH = key("item.publish", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GREEN));
            public static final LocalizedMessages.Key ITEM_MAINTAINER = key("item.maintainer", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key ITEM_MAINTAINER_LORE = key("item.maintainer_lore", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            public static final LocalizedMessages.Key PREVIEW = key("item.preview");
            public static final LocalizedMessages.Key PREVIEW_MESSAGE = key("item.preview.message");
            public static final LocalizedMessages.Key PREVIEW_TYPE = key("item.preview.type");
            public static final LocalizedMessages.Key PREVIEW_CODE = key("item.preview.code");
            public static final LocalizedMessages.Key PREVIEW_STATUS = key("item.preview.status");
            public static final LocalizedMessages.Key PREVIEW_COORDS = key("item.preview.coords");
            public static final LocalizedMessages.Key PREVIEW_AUTHOR = key("item.preview.author");
            public static final LocalizedMessages.Key PREVIEW_MAINTAINER = key("item.preview.maintainer");
            public static final LocalizedMessages.Key PREVIEW_NAVIGATION_COORDS = key("item.preview.nav_coords");
            public static final LocalizedMessages.Key PREVIEW_HIDDEN = key("item.preview.hidden");
            public static final LocalizedMessages.Key PREVIEW_FINDS = key("item.preview.finds");
            public static final LocalizedMessages.Key PREVIEW_FAVORITES = key("item.preview.favorites");
            public static final LocalizedMessages.Key CACHE_NAME = key("item.cache.name");
            public static final LocalizedMessages.Key CACHE_CODE = key("item.cache.code");
            public static final LocalizedMessages.Key CACHE_HINT = key("item.cache.hint");
            public static final LocalizedMessages.Key SET_MAINTAINER = key("set_maintainer", LocalizedMessages.StyleOptions.SUCCESS);

            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.data", path, styleOptions);
            }
        }

        public static class Edit {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key ITEM_RESET = Modify.ITEM_RESET;
            public static final LocalizedMessages.Key ITEM_PREVIEW = Modify.ITEM_PREVIEW;
            public static final LocalizedMessages.Key ITEM_LOCATION = Modify.ITEM_LOCATION;
            public static final LocalizedMessages.Key ITEM_LOCATION_COORDS = Modify.ITEM_LOCATION_COORDS;
            public static final LocalizedMessages.Key ITEM_NAVIGATION_LOCATION = Modify.ITEM_NAVIGATION_LOCATION;
            public static final LocalizedMessages.Key ITEM_NAVIGATION_COORDS = Modify.ITEM_NAVIGATION_COORDS;
            public static final LocalizedMessages.Key ITEM_STOP_EDITING = key("item.stop_editing", new LocalizedMessages.StyleOptions().setColor(ChatColor.DARK_GRAY));
            public static final LocalizedMessages.Key ITEM_STOP_EDITING_LORE = key("item.stop_editing.lore");
            public static final LocalizedMessages.Key SET_CODE = commandKey("set_code", "/ec code");
            public static final LocalizedMessages.Key SET_NAME = commandKey("set_name", "/ec name");

            private static LocalizedMessages.@NotNull Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.editing", path, styleOptions);
            }

            private static LocalizedMessages.Key commandKey(String path, String command) {
                return MCMessageKeys.commandKey(path, ChatColor.LIGHT_PURPLE, command);
            }
        }

        public static class List {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key PREVIOUS = MessageKeys.Menu.PAGINATION_PREVIOUS;
            public static final LocalizedMessages.Key REFRESH = MessageKeys.Menu.REFRESH;
            public static final LocalizedMessages.Key NEXT = MessageKeys.Menu.PAGINATION_NEXT;
            public static final LocalizedMessages.Key ITEM_OPTIONS = key("item.options");
            public static final LocalizedMessages.Key ITEM_CACHE = key("item.cache");

            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.list", path, styleOptions);
            }
        }

        public static class Log {
            public static final LocalizedMessages.Key TITLE = key("title");
            public static final LocalizedMessages.Key PICK_TITLE = key("pick.title");
            public static final LocalizedMessages.Key ITEM_CODE = key("item.code");
            public static final LocalizedMessages.Key ITEM_FAVORITE = key("item.favorite", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key ITEM_UNFAVORITE = key("item.unfavorite", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key ITEM_MESSAGE = key("item.message");
            public static final LocalizedMessages.Key ITEM_CUSTOM_MESSAGE = key("item.message.custom");
            public static final LocalizedMessages.Key MESSAGE_FOUND = key("message.found", LocalizedMessages.StyleOptions.NONE);
            public static final LocalizedMessages.Key MESSAGE_DNF = key("message.dnf", LocalizedMessages.StyleOptions.NONE);
            public static final LocalizedMessages.Key MESSAGE_NOTE = key("message.note", LocalizedMessages.StyleOptions.NONE);
            public static final LocalizedMessages.Key MESSAGE_OTHER = key("message.flag", LocalizedMessages.StyleOptions.NONE);
            public static final LocalizedMessages.Key TYPE_FOUND = key("type.found", new LocalizedMessages.StyleOptions().setColor(ChatColor.YELLOW));
            public static final LocalizedMessages.Key TYPE_DNF = key("type.dnf", new LocalizedMessages.StyleOptions().setColor(ChatColor.BLUE));
            public static final LocalizedMessages.Key TYPE_NOTE = key("type.note", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY));
            public static final LocalizedMessages.Key TYPE_FLAG = key("type.flag", new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            public static final LocalizedMessages.Key SET_CODE = commandKey("set_log_code", "/log code");
            public static final LocalizedMessages.Key SET_MESSAGE = commandKey("set_message", "/log message");
            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.log", path, styleOptions);
            }

            private static LocalizedMessages.Key commandKey(String path, String command) {
                return MCMessageKeys.commandKey(path, ChatColor.LIGHT_PURPLE, command);
            }
        }

        public static class Modify {
            public static final LocalizedMessages.Key ITEM_RESET = key("item.reset", new LocalizedMessages.StyleOptions().setColor(ChatColor.GRAY).setBold(true));
            public static final LocalizedMessages.Key ITEM_PREVIEW = key("item.preview");
            public static final LocalizedMessages.Key ITEM_LOCATION = key("item.loc");
            public static final LocalizedMessages.Key ITEM_LOCATION_COORDS = key("item.loc_coords");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_LOCATION = key("item.nav");
            public static final LocalizedMessages.Key ITEM_NAVIGATION_COORDS = key("item.nav_coords");

            private static LocalizedMessages.Key key(String path) {
                return key(path, DEFAULT_COLOR);
            }

            private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
                return MCMessageKeys.keyHelper("menu.modify", path, styleOptions);
            }
        }

        public static final LocalizedMessages.Key CONFIRMATION = key("confirmation.title");
        public static final LocalizedMessages.Key CANCEL = MessageKeys.Menu.CANCEL;
        public static final LocalizedMessages.Key CLOSE = MessageKeys.Menu.CLOSE;
        public static final LocalizedMessages.Key GO_BACK = MessageKeys.Menu.GO_BACK;
        public static final LocalizedMessages.Key SAVE = key("save", new LocalizedMessages.StyleOptions().setColor(ChatColor.GREEN).setBold(true));
        public static final LocalizedMessages.Key SAVE_LORE = key("save.lore");
        public static final LocalizedMessages.Key ITEM_ERROR = key("item.error");
        public static final LocalizedMessages.Key ITEM_OUT_OF_BOUNDS = MessageKeys.Error.MENU_ITEM_OUT_OF_BOUNDS;
        public static final LocalizedMessages.Key PERM_CHANGE = MessageKeys.Menu.PERM_CHANGED;

        private static LocalizedMessages.Key key(String path) {
            return key(path, DEFAULT_COLOR);
        }

        private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
            return MCMessageKeys.keyHelper("menu", path, styleOptions);
        }
    }

        public static class Permission {
            public static final LocalizedMessages.Key NO_PERMISSION = key("none");
            public static final LocalizedMessages.Key NO_PERMISSION_EDITCACHE = key("none.editcache");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN = key("none.mcadmin");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_RELOAD = key("none.mcadmin.reload");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_FORCE_STAT_UPDATE = key("none.mcadmin.force_stat_update");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_DATA = key("none.mcadmin.data");
            public static final LocalizedMessages.Key NO_PERMISSION_ADMIN_CONFIG = key("none.mcadmin.config");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("permission", path);
            }
        }

        public static class Usage {
            public static final LocalizedMessages.Key CREATE = key("addcache");
            public static final LocalizedMessages.Key CREATE_OPTIONS = key("addcache.options");
            public static final LocalizedMessages.Key CREATE_CANCEL = key("addcache.cancel");
            public static final LocalizedMessages.Key CREATE_CODE = key("addcache.code");
            public static final LocalizedMessages.Key CREATE_COORDS = key("addcache.coords");
            public static final LocalizedMessages.Key CREATE_NAVIGATION_COORDS = key("addcache.lodecoords");
            public static final LocalizedMessages.Key CREATE_NAME = key("addcache.name");
            public static final LocalizedMessages.Key ADD_SAVE = key("addcache.save");
            public static final LocalizedMessages.Key ADD_TYPE = key("addcache.type");
            public static final LocalizedMessages.Key ARCHIVE = key("archivecache");
            public static final LocalizedMessages.Key DELETE = key("deletecache");
            public static final LocalizedMessages.Key DISABLE = key("disablecache");
            public static final LocalizedMessages.Key EDIT = key("editcache");
            public static final LocalizedMessages.Key EDIT_OPTIONS = key("editcache.options");
            public static final LocalizedMessages.Key EDIT_CANCEL = key("editcache.cancel");
            public static final LocalizedMessages.Key EDIT_CODE = key("editcache.code");
            public static final LocalizedMessages.Key EDIT_COORDS = key("editcache.coords");
            public static final LocalizedMessages.Key EDIT_NAVIGATION_COORDS = key("editcache.lodecoords");
            public static final LocalizedMessages.Key EDIT_NAME = key("editcache.name");
            public static final LocalizedMessages.Key EDIT_SAVE = key("editcache.save");
            public static final LocalizedMessages.Key HINT = key("cachehint");
            public static final LocalizedMessages.Key LIST = key("listcaches");
            public static final LocalizedMessages.Key LOCATE = key("locatecache");
            public static final LocalizedMessages.Key LOCATE_COORD_OPTIONS = key("locatecache.options.coord");
            public static final LocalizedMessages.Key LOCATE_LODE_OPTIONS = key("locatecache.options.lode");
            public static final LocalizedMessages.Key LOGBOOK = key("logbook");
            public static final LocalizedMessages.Key LOG = key("logcache");
            public static final LocalizedMessages.Key LOG_FOUND = key("logcache.found");
            public static final LocalizedMessages.Key MAINTAINER = key("maintainer");
            public static final LocalizedMessages.Key ADMIN = key("mcadmin");
            public static final LocalizedMessages.Key ADMIN_CONFIG = key("mcadmin.config");
            public static final LocalizedMessages.Key ADMIN_CONFIG_VALUE = key("mcadmin.config_value");
            public static final LocalizedMessages.Key ADMIN_SAY_KEY = key("mcadmin.saykey");
            public static final LocalizedMessages.Key STATS = key("mcstats");
            public static final LocalizedMessages.Key PUBLISH = key("publishcache");

            private static LocalizedMessages.Key key(String path) {
                return MCMessageKeys.keyHelper("usage", path, new LocalizedMessages.StyleOptions().setColor(ChatColor.RED));
            }
        }

        public static class Misc {
            public static class Cache {
                public static final LocalizedMessages.Key NO_HINT = key("no_hint");
                public static final LocalizedMessages.Key TYPE_TRADITIONAL = key("type.traditional");
                public static final LocalizedMessages.Key TYPE_MYSTERY = key("type.mystery");
                public static final LocalizedMessages.Key TYPE_MULTI = key("type.multi");
                public static final LocalizedMessages.Key TYPE_INVALID = key("type.invalid");
                public static final LocalizedMessages.Key STATUS_PUBLISHED = key("status.published");
                public static final LocalizedMessages.Key STATUS_NEEDS_MAINTENANCE = key("status.needs_maintenance");
                public static final LocalizedMessages.Key STATUS_DISABLED = key("status.disabled");
                public static final LocalizedMessages.Key STATUS_ARCHIVED = key("status.archived");
                public static final LocalizedMessages.Key STATUS_REVIEWING = key("status.reviewing");
                public static final LocalizedMessages.Key STATUS_INVALID = key("status.invalid");
                public static final LocalizedMessages.Key YAML = key("yaml_error");
                public static final LocalizedMessages.Key YCODE_0 = key("yaml_code_0");
                public static final LocalizedMessages.Key YCODE_1 = key("yaml_code_1");
                public static final LocalizedMessages.Key YCODE_2 = key("yaml_code_2");
                public static final LocalizedMessages.Key YCODE_3 = key("yaml_code_3");
                public static final LocalizedMessages.Key YCODE_4 = key("yaml_code_4");
                public static final LocalizedMessages.Key YCODE_5 = key("yaml_code_5");
                public static final LocalizedMessages.Key YCODE_OTHER = key("yaml_code_other");

                private static LocalizedMessages.Key key(String path) {
                    return MCMessageKeys.keyHelper("minecache", path);
                }
            }

            public static final LocalizedMessages.Key LOCALE_NAME = key("locale.name");
    }

    // Makes some common keys more accessible
    public static final LocalizedMessages.Key ERROR = MessageKeys.Error.GENERIC;
    public static final LocalizedMessages.Key INCORRECT_USAGE = MessageKeys.Error.INCORRECT_USAGE;
    public static final LocalizedMessages.Key INCORRECT_ARG_COUNT = MessageKeys.Error.INCORRECT_ARG_COUNT;
    public static final LocalizedMessages.Key FAILED_TO_PARSE_NUMBER = MessageKeys.Error.FAILED_TO_PARSE_NUMBER;

    private static LocalizedMessages.Key key(String path) {
        return key(path, DEFAULT_COLOR);
    }

    private static LocalizedMessages.Key errorKey(String path) {
        return key(path, LocalizedMessages.StyleOptions.ERROR);
    }

    private static LocalizedMessages.Key key(String path, LocalizedMessages.StyleOptions styleOptions) {
        return new LocalizedMessages.Key(MinecachingAPI.getLocalization(), path, styleOptions);
    }

    private static LocalizedMessages.Key commandKey(String path, ChatColor color, String command) {
        return keyHelper("menu.command", path,
                new LocalizedMessages.StyleOptions().setColor(color).setUnderline(true).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    private static LocalizedMessages.Key keyHelper(String section, String path) {
        return keyHelper(section, path, DEFAULT_COLOR);
    }

    private static LocalizedMessages.Key keyHelper(String section, String path, LocalizedMessages.StyleOptions styleOptions) {
        String finalPath = path.isEmpty() ? section : section + "." + path;
        LocalizedMessages.Key key = key(finalPath, styleOptions);
        keyMap.put(finalPath, key);
        return key;
    }

    public static LocalizedMessages.Key fromKey(String key) {
        return keyMap.getOrDefault(key, MCMessageKeys.ERROR);
    }

    public static void init() {
    }
}
