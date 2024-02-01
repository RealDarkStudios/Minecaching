package net.realdarkstudios.minecaching.api.misc;

import com.google.gson.JsonObject;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import org.bukkit.plugin.Plugin;

import java.util.MissingFormatArgumentException;

public class Localization {
    private JsonObject json;

    /**
     * Creates a Localization based off of a {@link JsonObject}. Should never be invoked directly, instead use {@link LocalizationProvider#load(Plugin)}
     * @param json The JsonObject to load
     */
    Localization(JsonObject json) {
        this.json = json;
    }

    /**
     * Gets the translation of the given key. No format arguments.
     * @param key The key of the translation
     * @return The translated message
     */
    public String getTranslation(String key) {
        if (!hasTranslation(key)) return "Translation Not Found";
        String translation = json.get(key).toString();
        // The substring removes the quotation marks around the result
        return translation.substring(1, translation.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
    }

    /**
     * Gets the translation of the given key with format arguments
     * @param key The key of the translation
     * @param substitutions The format argument substitutions
     * @return The translated message
     */
    public String getTranslation(String key, Object... substitutions) {
        try {
            return String.format(getTranslation(key), substitutions);
        } catch (MissingFormatArgumentException e) {
            MinecachingAPI.tWarning("plugin.localization.missingformatarg", key);
            return getTranslation(key);
        }
    }

    /**
     * Checks if a given translation exists
     * @param key The key of the translation
     * @return {@code true} if an entry exists, {@code false} if not
     */
    public boolean hasTranslation(String key) {
        return json.has(key);
    }
}
