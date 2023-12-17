package net.realdarkstudios.minecaching.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentBuilder {
    private final String message;
    private boolean bold, italic, underlined, strikethrough, obfuscated = false;
    private ChatColor color;
    private ClickEvent clickEvent;

    public TextComponentBuilder(String msg) {
        this.message = msg;
    }

    public TextComponentBuilder color(ChatColor color) {
        this.color = color;
        return this;
    }

    public TextComponentBuilder bold() {
        this.bold = true;
        return this;
    }

    public TextComponentBuilder italic() {
        this.italic = true;
        return this;
    }

    public TextComponentBuilder underline() {
        this.underlined = true;
        return this;
    }

    public TextComponentBuilder strikethrough() {
        this.strikethrough = true;
        return this;
    }

    public TextComponentBuilder obfuscated() {
        this.obfuscated = true;
        return this;
    }

    public TextComponentBuilder clickEvent(ClickEvent.Action action, String value) {
        this.clickEvent = new ClickEvent(action, value);
        return this;
    }

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
