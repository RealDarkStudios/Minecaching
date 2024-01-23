package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.*;
import net.realdarkstudios.minecaching.event.StartLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.event.StopLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.util.MCMessages;
import org.bukkit.*;
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
            MCMessages.sendErrorMsg(sender, "execute.console");
            return true;
        }

        if (args.length < 1) {
            MCMessages.incorrectUsage(sender);
            MCMessages.usage(sender, "locatecache", command, label);
            return false;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = args[0];
        boolean gettingNewCompass = id.equalsIgnoreCase("compass");

        if (id.equalsIgnoreCase("coords") && !Config.getInstance().useLodestoneBasedLocating()) {
            Location lodeLocation = MinecachingAPI.get().getMinecache(MinecachingAPI.get().getPlayerData(plr).getLocatingId()).lodeLocation();
            plr.sendMessage(String.format("%sHead to (%d, %d, %d)!", ChatColor.AQUA, lodeLocation.getBlockX(), lodeLocation.getBlockY(), lodeLocation.getBlockZ()));
            return true;
        }

        if (id.equalsIgnoreCase("cancel") && !pdo.getLocatingId().equals("NULL")) {
            cancelTask(MinecachingAPI.get().getMinecache(pdo.getLocatingId()), MinecachingAPI.get().getMinecache(pdo.getLocatingId()).lodeLocation(), plr, true, false);
            return true;
        }

        Minecache c = MinecachingAPI.get().getMinecache(id);

        if (gettingNewCompass && !pdo.getLocatingId().equals("NULL") && Config.getInstance().useLodestoneBasedLocating()) {
            c = MinecachingAPI.get().getMinecache(pdo.getLocatingId());
            cancelTask(c, c.lodeLocation(), plr, false, true);
        }

        Minecache cache = c;

        if (!pdo.getLocatingId().equals("NULL") && !gettingNewCompass) {
            MCMessages.sendErrorMsg(sender, "locatecache.alreadylocating", id);
            return true;
        }

        if (cache.equals(Minecache.EMPTY) && !gettingNewCompass) {
            MCMessages.sendErrorMsg(sender, "cantfind", id);
            return true;
        }

        if ((!plr.getWorld().equals(cache.world()))) {
            MCMessages.sendErrorMsg(sender, "locatecache.differentworld", id);
            return true;
        }

        Location lodeLocation = cache.lodeLocation();
        Location cacheLocationC = lodeLocation.clone();
        cacheLocationC.setY(plr.getLocation().getY());
        if (plr.getLocation().distance(cacheLocationC) < Config.getInstance().getFindLodestoneDistance()) {
            MCMessages.sendMsg(sender, "locatecache.withindistance", ChatColor.AQUA, (Math.round(cache.location().distance(lodeLocation)) + Config.getInstance().getFindLodestoneDistance()));
            pdo.setLocatingId(cache.id());
            return true;
        }

        if (!Config.getInstance().useLodestoneBasedLocating()) {
            MCMessages.sendMsg(sender, "locatecache.coords", ChatColor.AQUA, lodeLocation.getBlockX(), lodeLocation.getBlockY(), lodeLocation.getBlockZ(), label);
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
        meta.setLore(List.of(LocalizationProvider.getInstance().getTranslation("locatecache.compass.lore", cache.id())));
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

        MCMessages.sendMsg(sender, "locatecache.lode", ChatColor.AQUA, cache.id(), cache.name(), label);
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
        StopLocatingMinecacheEvent event = new StopLocatingMinecacheEvent(cache, plr, plr.getLocation(), plr.getLocation().distance(cacheLocation), fromCancel);
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

            if (!fromCancel && !gettingNewCompass) MCMessages.sendMsg(plr, "locatecache.withindistance", ChatColor.AQUA, (Math.round(cache.location().distance(cacheLocation)) + Config.getInstance().getFindLodestoneDistance()));
            else if (!gettingNewCompass) MCMessages.sendMsg(plr, "locatecache.cancel", ChatColor.AQUA, cache.id());
        } else {
            MCMessages.sendMsg(plr, "locatecache.cancel", ChatColor.AQUA, cache.id());
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
