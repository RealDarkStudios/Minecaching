package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.Utils;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.event.minecache.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MCDebugEventHandler implements Listener {
    /**
     * Minecaching's debug event handler.
     * <p>
     * Depending on whether DEBUG_EVENTS is true or not, events will output a message according to the DEBUG_EVENTS_LEVEL.
     * Both of these settings are controlled by the {@link Config}
     *
     * @since 0.2.0.0
     */

    @EventHandler
    public void onMinecacheFound(MinecacheFoundEvent event) {
        sendDebugMessage(event.getPlayer().getDisplayName() + " found " + event.getCache().id(), "cacheId - " + event.getCache().id() + ", player - " + event.getPlayer().getDisplayName(), "isFTF - " + event.isFTF());
    }

    @EventHandler
    public void onMinecacheCreated(MinecacheCreatedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was created", "cacheId - " + event.getCache().id() + ", cacheAuthor - " + event.getAuthor().getDisplayName(), "cacheType - " + event.getType().toString() + ", hidden - " + event.getHidden());
    }

    @EventHandler
    public void onMinecacheEdited(MinecacheEditedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was edited", "cacheId - " + event.getCache().id() + ", editor - " + Utils.commandSenderName(event.getEditor()), "");
    }

    @EventHandler
    public void onMinecacheDeleted(MinecacheDeletedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was deleted", "cacheId - " + event.getCache().id(), "cacheAuthor - " + event.getAuthor().getName());
    }

    @EventHandler
    public void onMinecachePublished(MinecachePublishedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was published", "cacheId - " + event.getCache().id() + ", verifier - " + Utils.commandSenderName(event.getVerifier()), "");
    }

    @EventHandler
    public void onMinecacheArchived(MinecacheArchivedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was archived", "cacheId - " + event.getCache().id() + ", archiver - " + Utils.commandSenderName(event.getArchiver()), "");
    }

    @EventHandler
    public void onMinecacheDisabled(MinecacheDisabledEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was disabled", "cacheId - " + event.getCache().id() + ", disabled - " + Utils.commandSenderName(event.getDisabler()), "");
    }

    @EventHandler
    public void onStartLocatingMinecache(StartLocatingMinecacheEvent event) {
        sendDebugMessage(event.getPlayer().getDisplayName() + " start locating " + event.getCache().id(), "cacheId - " + event.getCache().id() + ", player - " + event.getPlayer().getDisplayName(), "plrLocation - (" + event.getPlrLocation().getBlockX() + ", " + event.getPlrLocation().getBlockY() + ", " + event.getPlrLocation().getBlockZ() + "), distance - " + event.getDistance());
    }

    @EventHandler
    public void onStopLocatingMinecache(StopLocatingMinecacheEvent event) {
        sendDebugMessage(event.getPlayer().getDisplayName() + " stopped locating " + event.getCache().id(), "cacheId - " + event.getCache().id() + ", player - " + event.getPlayer().getDisplayName() + ", fromCancel - " + event.isLocateCancelled(), "plrLocation - (" + event.getPlrLocation().getBlockX() + ", " + event.getPlrLocation().getBlockY() + ", " + event.getPlrLocation().getBlockZ() + "), distance - " + event.getDistance());
    }

    @EventHandler
    public void onLogCreatedEvent(LogCreatedEvent event) {
        sendDebugMessage("Log " + event.getLogId() + " was created", "cacheId - " + event.getCache().id() + ", logId - " + event.getLogId() + ", author - " + Utils.uuidName(event.getAuthor()), "type - " + event.getType().getId());
    }

    private void sendDebugMessage(String basicMsg, String importantEventData, String otherEventData) {
        if (!Config.getInstance().debugEvents()) return;

        switch (Config.getInstance().getDebugEventsLevel()) {
            case 1: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg + "\nEvent Data: " + importantEventData ); break;
            case 2: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg + "\nEvent Data: " + importantEventData + (otherEventData.isEmpty() ? "" :", " + otherEventData)); break;
            default: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg);
        }
    }
}
