package net.realdarkstudios.minecaching.event;

import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecacheStorage;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.PlayerDataObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MCEventHandler implements Listener {
    @EventHandler
    public void onMinecacheFound(MinecacheFoundEvent event) {
        PlayerDataObject pso = MinecachingAPI.get().getPlayerData(event.getPlayer());
        pso.addFind(event.getCache().id());
        if (event.isFTF()) {
            pso.addFTF(event.getCache().id());
            MinecachingAPI.get().saveMinecache(event.getCache().setFTF(event.getPlayer().getUniqueId()), false);
        }

        pso.save();
        MinecacheStorage.getInstance().save();
        MinecachingAPI.get().update();
        event.getPlayer().sendMessage("player find minecache, " + event.isFTF());
    }

    @EventHandler
    public void onMinecacheCreated(MinecacheCreatedEvent event) {
        PlayerDataObject pso = MinecachingAPI.get().getPlayerData(event.getAuthor());

        MinecachingAPI.get().saveMinecache(event.getCache(), true);

        pso.addHide(event.getCache().id());
        pso.setCache(Minecache.EMPTY.setID("NULL"));

        pso.save();
        MinecacheStorage.getInstance().save();
        MinecachingAPI.get().update();
        event.getAuthor().sendMessage("player create minecache, " + event.getAuthor().getDisplayName(), event.getType().toString());
    }
}
