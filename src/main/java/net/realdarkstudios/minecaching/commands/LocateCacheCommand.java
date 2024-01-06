package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.api.Config;
import net.realdarkstudios.minecaching.api.Minecache;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.PlayerDataObject;
import net.realdarkstudios.minecaching.event.StartLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.event.StopLocatingMinecacheEvent;
import net.realdarkstudios.minecaching.util.MCPluginMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.List;

public class LocateCacheCommand implements CommandExecutor, TabExecutor {
    int taskID;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to locate minecaches!");
            return true;
        }

        if (args.length < 1) {
            sender.spigot().sendMessage(MCPluginMessages.INCORRECT_USAGE);
            return false;
        }

        PlayerDataObject pdo = MinecachingAPI.get().getPlayerData(plr);

        String id = args[0];

        if (id.equalsIgnoreCase("cancel") && !pdo.getLocatingId().equals("NULL")) {
            cancelTask(MinecachingAPI.get().getMinecache(pdo.getLocatingId()), MinecachingAPI.get().getMinecache(pdo.getLocatingId()).lodeLocation(), plr, true);
            return true;
        }

        Minecache cache = MinecachingAPI.get().getMinecache(id);

        if (!pdo.getLocatingId().equals("NULL")) {
            sender.sendMessage(ChatColor.RED + "You are already locating " + pdo.getLocatingId() + "!", ChatColor.RED + "If you wish to look for " + id + ", use /" + label + " cancel");
            return true;
        }

        if (cache.equals(Minecache.EMPTY)) {
            sender.sendMessage(ChatColor.RED + "Did not find minecache with ID " + id);
            return true;
        }

        if (!plr.getWorld().equals(cache.world())) {
            sender.sendMessage(ChatColor.RED + "This cache is in a different world!");
            return true;
        }

        Location lodeLocation = cache.lodeLocation();
        Location cacheLocationC = lodeLocation.clone();
        cacheLocationC.setY(plr.getLocation().getY());
        if (plr.getLocation().distance(cacheLocationC) < Config.getInstance().getFindLodestoneDistance()) {
            plr.sendMessage(ChatColor.AQUA + "You are within ~" + (Math.round(cache.location().distance(lodeLocation)) + Config.getInstance().getFindLodestoneDistance()) + " blocks of the cache!");
            return true;
        }

        lodeLocation.getBlock().setType(Material.LODESTONE);

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = ((CompassMeta) compass.getItemMeta());
        assert meta != null;
        meta.setLodestone(lodeLocation);
        meta.setLodestoneTracked(true);
        meta.setDisplayName(cache.id() + ": " + cache.name());
        meta.setLore(List.of("A compass pointing to the location of a cache."));
        compass.setItemMeta(meta);
        Minecaching.getInstance().getLogger().info(String.format("Giving %s a compass pointing to (%s) %d, %d, %d", plr.getDisplayName(), meta.getLodestone().getWorld().getName(), meta.getLodestone().getBlockX(), meta.getLodestone().getBlockY(), meta.getLodestone().getBlockZ()));

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

        plr.sendMessage(ChatColor.AQUA + "Here's a compass to " + cache.id() + ": " + cache.name());
        pdo.setLocatingId(cache.id());

        StartLocatingMinecacheEvent event = new StartLocatingMinecacheEvent(cache, plr, plr.getLocation(), plr.getLocation().distance(cacheLocationC));
        Bukkit.getPluginManager().callEvent(event);

        int dist = Config.getInstance().getFindLodestoneDistance();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Minecaching.getInstance(), () -> {
            if (plr.getLocation().distance(lodeLocation) < dist) {
                cancelTask(cache, lodeLocation, plr, false);
            }
        }, 0L, 1L);

        return true;
    }

    private void cancelTask(Minecache cache, Location cacheLocation, Player plr, boolean fromCancel) {
        StopLocatingMinecacheEvent event = new StopLocatingMinecacheEvent(cache, plr, plr.getLocation(), plr.getLocation().distance(cacheLocation), fromCancel);
        Bukkit.getPluginManager().callEvent(event);

        Bukkit.getScheduler().cancelTask(taskID);
        Block cacheB = cacheLocation.getBlock();
        cacheB.setType(cache.blockType());
        for (int slot = 0; slot < plr.getInventory().getSize() - 5; slot++) {
            ItemStack item = plr.getInventory().getItem(slot);
            if (item != null && item.getItemMeta().getDisplayName().equals(cache.id() + ": " + cache.name()) && item.getItemMeta().getLore().equals(List.of("A compass pointing to the location of a cache."))) {
                plr.getInventory().setItem(slot, null);
            }
        }

        MinecachingAPI.get().getPlayerData(plr).setLocatingId("NULL");
        if (!fromCancel) plr.sendMessage(ChatColor.AQUA + "You are now within ~" + (Math.round(cache.location().distance(cacheLocation)) + Config.getInstance().getFindLodestoneDistance()) + " blocks of the cache!");
        else plr.sendMessage(ChatColor.AQUA + "Stopped looking for " + cache.id());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) return List.of();
        if (!MinecachingAPI.get().getPlayerData(plr).getLocatingId().equals("NULL")) return List.of("cancel");
        return args.length == 0 ? MinecachingAPI.get().getAllKnownCacheIDs() : args.length == 1 ? MinecachingAPI.get().getFilteredCacheIDs(s -> s.contains(args[0])) : List.of();
    }
}
