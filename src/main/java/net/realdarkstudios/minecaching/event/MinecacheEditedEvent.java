package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.commands.EditCacheCommand;
import org.bukkit.command.CommandSender;

public class MinecacheEditedEvent extends MinecacheEvent {
    /**
     * Called when a {@link Minecache} is edited.
     *
     * @since 0.2.0.6
     * @see EditCacheCommand
     */
    private final CommandSender editor;

    /**
     * Creates a new MinecacheEditedEvent
     *
     * @param minecache The {@link Minecache} that was edited
     * @param editor The {@link CommandSender} editing this cache
     */
    public MinecacheEditedEvent(Minecache minecache, CommandSender editor) {
        this.minecache = minecache;
        this.editor = editor;
    }

    public CommandSender getEditor() {
        return editor;
    }
}
