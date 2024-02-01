package net.realdarkstudios.minecaching.api.misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

public class LocalizationProvider {
    private static final LocalizationProvider INSTANCE = new LocalizationProvider();
    private final Gson gson = new Gson();
    private Locale serverLocale;
    private JsonObject json;
    private final HashMap<Plugin, Localization> pluginMap = new HashMap<>();
    private LocalizationProvider() {
    }

    /**
     * Generates the {@link Localization} for this {@link Plugin} with the server locale defined in Minecaching's Config
     * @param plugin The plugin to generate the Localization for
     * @return A Localization which can be used for translation
     * @since 0.2.2.0
     */
    public Localization load(Plugin plugin) {
        if (pluginMap.containsKey(plugin)) return get(plugin);
        else return load(plugin, Config.getInstance().getServerLocale());
    }

    /**
     * Generates the {@link Localization} for this {@link Plugin} using the specified {@link Locale}
     * @param plugin The plugin to generate the Localization for
     * @param locale The Locale to use
     * @return A Localization which can be used for translation
     * @since 0.2.2.0
     */
    public Localization load(Plugin plugin, Locale locale) {
        try {
            this.json = loadJson(plugin, locale);
            MinecachingAPI.info("Loaded language: " + json.get("locale.name").toString() + " for plugin " + plugin.getName());
        } catch (Exception e) {
            MinecachingAPI.warning("The Server Locale defined in config.yml is invalid or not supported! \nDefaulting to en-US.json! \nlang/" + locale.toLanguageTag() + ".json does not exist!");
            try {
                this.json = loadJson(plugin, Locale.US);
                MinecachingAPI.info("Loaded language: " + json.get("locale.name").toString() + " for plugin " + plugin.getName());
            } catch (Exception e1) {
                MinecachingAPI.warning("Unable to set a language!");
                Minecaching.getInstance().onDisable();
            }
        }

        Localization localization = new Localization(json);
        pluginMap.put(plugin, localization);
        return localization;
    }

    /**
     * Loads the JSON String from "(plugin resource folder)/lang/(locale).json" Ex: /lang/en-US.json
     * @param plugin The plugin to load the JSON from
     * @param locale The locale to load the JSON from
     * @return A JsonObject which can be used by the {@link Localization}
     * @throws IOException If no JSON could be read or if the file does not exist
     */
    private JsonObject loadJson(Plugin plugin, Locale locale) throws IOException {
        InputStreamReader reader = new InputStreamReader(plugin.getResource("lang/" + locale.toLanguageTag() + ".json"));
        if (!reader.ready()) {
            reader.close();
            throw new IOException();
        }
        return gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Gets the {@link Localization} for the given {@link Plugin}
     * @param plugin The plugin to get the Localization for
     * @return The requested Localization
     */
    public Localization get(Plugin plugin) {
        return pluginMap.get(plugin);
    }

    /**
     * Gets the {@link LocalizationProvider} instance
     * @return The LocalizationProvider instance
     */
    public static LocalizationProvider getInstance() {
        return INSTANCE;
    }
}
