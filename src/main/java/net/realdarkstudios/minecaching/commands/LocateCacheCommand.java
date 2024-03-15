package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.api.Minecaching;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.misc.Config;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.LocalizedMessages;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import net.realdarkstudios.minecaching.api.event.minecache.StartLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.api.event.minecache.StopLocatingMinecacheEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocateCacheCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            LocalizedMessages.send(sender, MessageKeys.Error.NON_CONSOLE_COMMAND);
            return true;
        }

        if (args.length < 1) {
            LocalizedMessages.send(sender, MessageKeys.INCORRECT_USAGE);
            LocalizedMessages.send(sender, MessageKeys.Usage.LOCATE, label);
            return true;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = args[0];
        boolean gettingNewCompass = id.equalsIgnoreCase("compass");

        if (id.equalsIgnoreCase("coords") && !Config.getInstance().useLodestoneBasedLocating()) {
            Location lodeLocation = MinecachingAPI.get().getMinecache(MinecachingAPI.get().getPlayerData(plr).getLocatingId()).navLocation();

            LocalizedMessages.send(sender, MessageKeys.Command.Locate.COORDS, lodeLocation.getBlockX(), lodeLocation.getBlockY(), lodeLocation.getBlockZ(), label);
            return true;
        }

        if (id.equalsIgnoreCase("cancel") && !pdo.getLocatingId().equals("NULL")) {
            cancelTask(MinecachingAPI.get().getMinecache(pdo.getLocatingId()), MinecachingAPI.get().getMinecache(pdo.getLocatingId()).navLocation(), plr, true, false);
            return true;
        }

        Minecache c = MinecachingAPI.get().getMinecache(id);

        if (gettingNewCompass && !pdo.getLocatingId().equals("NULL") && Config.getInstance().useLodestoneBasedLocating()) {
            c = MinecachingAPI.get().getMinecache(pdo.getLocatingId());
            cancelTask(c, c.navLocation(), plr, false, true);
        }

        Minecache cache = c;

        if (!pdo.getLocatingId().equals("NULL") && !gettingNewCompass) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.LOCATE_ALREADY_LOCATING, id);
            return true;
        }

        if (cache.equals(Minecache.EMPTY) && !gettingNewCompass) {
            LocalizedMessages.send(sender, MessageKeys.Error.CANT_FIND_CACHE, id);
            return true;
        }

        if ((!plr.getWorld().equals(cache.world()))) {
            LocalizedMessages.send(sender, MessageKeys.Error.Misc.LOCATE_DIFFERENT_WORLD, id);
            return true;
        }

        Location lodeLocation = cache.navLocation();
        Location cacheLocationC = lodeLocation.clone();
        cacheLocationC.setY(plr.getLocation().getY());
        if (plr.getLocation().distance(cacheLocationC) < Config.getInstance().getFindLodestoneDistance()) {
            LocalizedMessages.send(sender, MessageKeys.Command.Locate.WITHIN_DISTANCE, (Math.round(cache.location().distance(lodeLocation)) + Config.getInstance().getFindLodestoneDistance()));
            pdo.setLocatingId(cache.id());
            return true;
        }

        if (!Config.getInstance().useLodestoneBasedLocating()) {
            LocalizedMessages.send(sender, MessageKeys.Command.Locate.COORDS, lodeLocation.getBlockX(), lodeLocation.getBlockY(), lodeLocation.getBlockZ(), label);
            pdo.setLocatingId(cache.id());
            return true;
        }

        if (!lodeLocation.getBlock().getType().equals(Material.LODESTONE)) {
            lodeLocation.getBlock().setType(Material.LODESTONE);
        }

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = ((CompassMeta) compass.getItemMeta());
        assert meta != null;
        meta.setLodestone(lodeLocation);
        meta.setLodestoneTracked(true);
        meta.setDisplayName(cache.id() + ": " + cache.name());
        meta.setLore(List.of(MessageKeys.Command.Locate.COMPASS_LORE.translate(cache.id())));
        meta.getPersistentDataContainer().set(new NamespacedKey(Minecaching.getInstance(), "attachedMinecacheId"), PersistentDataType.STRING, cache.id());
        compass.setItemMeta(meta);

        int emptySlotCount = 0;
        ArrayList<Integer> emptySlots = new ArrayList<>();
        for (int slot = 0; slot < plr.getInventory().getSize() - 5; slot++) {
            if (plr.getInventory().getItem(slot) == null) {
                emptySlotCount += 1;
                emptySlots.add(slot);
            }
        }

        if (emptySlotCount == 0) {
            plr.getWorld().dropItemNaturally(plr.getLocation(), compass);
        } else {
            plr.getInventory().setItem(emptySlots.get(0), compass);
        }

        LocalizedMessages.send(sender, MessageKeys.Command.Locate.LODE, cache.id(), cache.name(), label);
        pdo.setLocatingId(cache.id());

        StartLocatingMinecacheEvent event = new StartLocatingMinecacheEvent(cache, plr, plr.getLocation(), plr.getLocation().distance(cacheLocationC));
        Bukkit.getPluginManager().callEvent(event);

        int dist = Config.getInstance().getFindLodestoneDistance();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (plr.getLocation().distance(lodeLocation) < dist && !pdo.getLocatingId().equals("NULL")) {
                    cancelTask(cache, lodeLocation, plr, false, false);
                    cancel();
                }
            }
        }.runTaskTimer(Minecaching.getInstance(), 0L, 1L);

        return true;
    }

    private void cancelTask(Minecache cache, Location cacheLocation, Player plr, boolean fromCancel, boolean gettingNewCompass) {
        StopLocatingMinecacheEvent event;
        try {
            event = new StopLocatingMinecacheEvent(cache, plr, plr.getLocation(), plr.getLocation().distance(cacheLocation), fromCancel);
        } catch (IllegalArgumentException e) {
            event = new StopLocatingMinecacheEvent(cache, plr, plr.getLocation(), -1, fromCancel);
        }
        Bukkit.getPluginManager().callEvent(event);

        if (Config.getInstance().useLodestoneBasedLocating()) {
            Block cacheB = cacheLocation.getBlock();

            for (int slot = 0; slot < plr.getInventory().getSize() - 5; slot++) {
                ItemStack item = plr.getInventory().getItem(slot);
                if (item != null && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Minecaching.getInstance(), "attachedMinecacheId"), PersistentDataType.STRING)) {
                    plr.getInventory().setItem(slot, null);
                }
            }

            if (!gettingNewCompass) {
                String id = MinecachingAPI.get().getPlayerData(plr).getLocatingId();

                MinecachingAPI.get().getPlayerData(plr).setLocatingId("NULL");

                boolean anyOtherPlayersSearching = false;
                for (PlayerDataObject pdo : MinecachingAPI.get().getAllKnownPlayers()) {
                    if (pdo.getLocatingId().equals(cache.id())) {
                        anyOtherPlayersSearching = true;
                        break;
                    }
                }

                MinecachingAPI.get().getPlayerData(plr).setLocatingId(id);

                Minecaching.getInstance().getLogger().info(anyOtherPlayersSearching + "");

                if (!anyOtherPlayersSearching) cacheB.setType(cache.blockType());
            }

            if (!fromCancel && !gettingNewCompass) LocalizedMessages.send(plr, MessageKeys.Command.Locate.WITHIN_DISTANCE,
                    (Math.round(cache.location().distance(cacheLocation)) + Config.getInstance().getFindLodestoneDistance()));
            else if (!gettingNewCompass) LocalizedMessages.send(plr, MessageKeys.Command.Locate.CANCEL, cache.id());
        } else {
            LocalizedMessages.send(plr, MessageKeys.Command.Locate.CANCEL, cache.id());
            MinecachingAPI.get().getPlayerData(plr).setLocatingId("NULL");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();
        if (!MinecachingAPI.get().getPlayerData(plr).getLocatingId().equals("NULL")) return Config.getInstance().useLodestoneBasedLocating() ? List.of("cancel", "compass") : List.of("cancel", "coords");
        return args.length == 0 ? MinecachingAPI.get().getAllKnownCacheIDs() : args.length == 1 ? MinecachingAPI.get().getFilteredCacheIDs(s -> s.contains(args[0])) : List.of();
    }
}
