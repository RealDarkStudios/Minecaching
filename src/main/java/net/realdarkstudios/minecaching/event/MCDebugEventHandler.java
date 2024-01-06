package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MCDebugEventHandler implements Listener {
    @EventHandler
    public void onMinecacheFound(MinecacheFoundEvent event) {
        sendDebugMessage(event.getPlayer().getDisplayName() + " found " + event.getCache().id(), "cacheId - " + event.getCache().id() + ", player - " + event.getPlayer().getDisplayName(), "isFTF - " + event.isFTF());
    }

    @EventHandler
    public void onMinecacheCreated(MinecacheCreatedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was created", "cacheId - " + event.getCache().id() + ", cacheAuthor - " + event.getAuthor().getDisplayName(), "cacheType - " + event.getType().toString() + ", hidden - " + event.getHidden());
    }

    @EventHandler
    public void onMinecacheDeleted(MinecacheDeletedEvent event) {
        sendDebugMessage("Minecache " + event.getCache().id() + " was deleted", "cacheId - " + event.getCache().id(), "cacheAuthor - " + event.getAuthor().getDisplayName());
    }

    private void sendDebugMessage(String basicMsg, String importantEventData, String otherEventData) {
        if (!Config.getInstance().getDebugEvents()) return;

        switch (Config.getInstance().getDebugEventsLevel()) {
            case 1: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg + "\nEvent Data: " + importantEventData ); break;
            case 2: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg + "\nEvent Data: " + importantEventData + ", " + otherEventData); break;
            default: Minecaching.getInstance().getLogger().info("DEBUG: " + basicMsg);
        }
    }
}
