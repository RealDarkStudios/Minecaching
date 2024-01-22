package net.realdarkstudios.minecaching.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.realdarkstudios.minecaching.Minecaching;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

public class LocalizationProvider {
    private static final LocalizationProvider INSTANCE = new LocalizationProvider();
    private final Gson gson = new Gson();
    private File file;
    private Locale serverLocale;
    private JsonObject json;
    private LocalizationProvider() {
    }

    public Locale getServerLocale() {
        return serverLocale;
    }

    public void load() {
        this.serverLocale = Config.getInstance().getServerLocale();
        try {
            this.json = load(serverLocale);
            MinecachingAPI.info("Loaded language: " + json.get("locale.name").toString());
        } catch (Exception e) {
            MinecachingAPI.warning("The Server Locale defined in config.yml is invalid or not supported! \nDefaulting to en-US.json! \nlang/" + serverLocale.toLanguageTag() + ".json does not exist!");
            try {
                this.json = load(Locale.US);
                MinecachingAPI.info("Loaded language: " + json.get("locale.name").toString());
            } catch (Exception e1) {
                MinecachingAPI.warning("Unable to set a language!");
                Minecaching.getInstance().onDisable();
            }
        }
    }

    private JsonObject load(Locale locale) throws IOException {
        InputStreamReader reader = new InputStreamReader(Minecaching.getInstance().getResource("lang/" + locale.toLanguageTag() + ".json"));
        if (!reader.ready()) {
            reader.close();
            throw new IOException();
        }
        return gson.fromJson(reader, JsonObject.class);
    }

    public String getTranslation(String key) {
        if (!hasTranslation(key)) return "Translation Not Found";
        String translation = json.get(key).toString();
        // The substring removes the quotation marks around the result
        return translation.substring(1, translation.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
    }

    public String getTranslation(String key, Object... substitutions) {
        try {
            return String.format(getTranslation(key), substitutions);
        } catch (MissingFormatArgumentException e) {
            MinecachingAPI.tWarning("plugin.localization.missingformatarg", key);
            return getTranslation(key);
        }
    }

    public boolean hasTranslation(String key) {
        return json.has(key);
    }

    public static LocalizationProvider getInstance() {
        return INSTANCE;
    }
}
