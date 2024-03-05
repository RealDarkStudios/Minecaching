package net.realdarkstudios.minecaching.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.misc.Localization;

/**
 * A builder for {@link TextComponent}s
 * @since before 0.1.4.3-2
 * @deprecated Since 0.3.1.0 | Use {@link net.md_5.bungee.api.chat.ComponentBuilder}.
 * For localized messages, see {@link LocalizedMessages} and {@link LocalizedMessages.StyleOptions}
 */
@Deprecated(since = "0.3.1.0", forRemoval = true)
public class TextComponentBuilder {
    private final String message;
    private boolean bold = false, italic = false, underlined = false, strikethrough = false, obfuscated = false;
    private ChatColor color;
    private ClickEvent clickEvent;

    /**
     * Creates a {@link TextComponentBuilder} with the given message
     * @param msg The message
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder(String msg) {
        this.message = msg;
    }

    /**
     * Static method to create a {@link TextComponentBuilder} from a {@link TextComponent}.
     * @param from The TextComponent to use
     * @return The TextComponentBuilder
     * @since 0.3.0.5
     */
    public static TextComponentBuilder fromTextComponent(TextComponent from) {
        TextComponentBuilder b = new TextComponentBuilder(from.getText());
        if (from.hasFormatting()) {
            if (from.isBold()) b.bold();
            if (from.isItalic()) b.italic();
            if (from.isUnderlined()) b.underline();
            if (from.isStrikethrough()) b.strikethrough();
            if (from.isObfuscated()) b.obfuscate();
        }
        if (from.getColor() != null) b.color(from.getColor());
        if (from.getClickEvent() != null) b.clickEvent(from.getClickEvent().getAction(), from.getClickEvent().getValue());

        return b;
    }

    /**
     * Static method to create a {@link TextComponentBuilder} from a translation path. Uses the Minecaching localization
     * @param key The translation path
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     * @return The TextComponentBuilder
     * @since 0.2.2.1-SNAPSHOT-1
     */
    public static TextComponentBuilder fromTranslation(String key, Object... substitutions) {
        return new TextComponentBuilder(MinecachingAPI.getLocalization().getTranslation(key, substitutions));
    }

    /**
     * Static method to create a {@link TextComponentBuilder} from a translation path.
     * @param key The translation path
     * @param localization The localization to use
     * @param substitutions The substitutions (or format args). Uses {@link String#format(String, Object...)}
     * @return The TextComponentBuilder
     * @since 0.3.0.5
     */
    public static TextComponentBuilder fromTranslation(String key, Localization localization, Object... substitutions) {
        return new TextComponentBuilder(localization.getTranslation(key, substitutions));
    }

    /**
     * Sets the color of this {@link TextComponentBuilder}
     * @param color The color to set
     * @return This TextComponentBuilder
     */
    public TextComponentBuilder color(ChatColor color) {
        this.color = color;
        return this;
    }

    /**
     * Bolds this {@link TextComponentBuilder}
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder bold() {
        this.bold = true;
        return this;
    }

    /**
     * Italicizes this {@link TextComponentBuilder}
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder italic() {
        this.italic = true;
        return this;
    }

    /**
     * Underlines this {@link TextComponentBuilder}
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder underline() {
        this.underlined = true;
        return this;
    }

    /**
     * Applies strikethrough to this {@link TextComponentBuilder}
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder strikethrough() {
        this.strikethrough = true;
        return this;
    }

    /**
     * Obfuscates this {@link TextComponentBuilder}
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder obfuscate() {
        this.obfuscated = true;
        return this;
    }

    /**
     * Adds a click event to this {@link TextComponentBuilder}
     * @param action The {@link ClickEvent.Action} to set.
     * @param value The {@link ClickEvent} value
     * @return This TextComponentBuilder
     * @since pre-0.1.4.3-2
     */
    public TextComponentBuilder clickEvent(ClickEvent.Action action, String value) {
        this.clickEvent = new ClickEvent(action, value);
        return this;
    }

    /**
     * Builds this {@link TextComponentBuilder}
     * @return The TextComponent
     * @since pre-0.1.4.3-2
     */
    public TextComponent build() {
        TextComponent component = new TextComponent(this.message);
        component.setColor(this.color);
        component.setBold(this.bold);
        component.setItalic(this.italic);
        component.setUnderlined(this.underlined);
        component.setStrikethrough(this.strikethrough);
        component.setObfuscated(this.obfuscated);
        component.setClickEvent(clickEvent);
        return component;
    }
}
