package net.realdarkstudios.minecaching.commands;

import net.realdarkstudios.minecaching.Minecaching;
import net.realdarkstudios.minecaching.data.Config;
import net.realdarkstudios.minecaching.data.Minecache;
import net.realdarkstudios.minecaching.data.MinecacheStorage;
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

public class FindCacheCommand implements CommandExecutor, TabExecutor {
    int taskID;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player plr)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to find minecaches!");
            return true;
        }

        if (args.length < 1) {
            sender.spigot().sendMessage(MCPluginMessages.INCORRECT_USAGE);
            return false;
        }

        String id = args[0];
        Minecache cache = MinecacheStorage.getInstance().getMinecacheByID(id);

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
            plr.sendMessage(ChatColor.AQUA + "You are within ~" + Config.getInstance().getMaxLodestoneDistance() + " blocks of the cache!");
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

        int dist = Config.getInstance().getFindLodestoneDistance();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Minecaching.getInstance(), () -> {
            if (plr.getLocation().distance(cacheLocationC) < dist) {
                cancelTask(cache, lodeLocation, plr);
            } else cacheLocationC.setY(plr.getLocation().getY());
        }, 0L, 1L);

        return true;
    }

    private void cancelTask(Minecache cache, Location cacheLocation, Player plr) {
        Bukkit.getScheduler().cancelTask(taskID);
        Block cacheB = cacheLocation.getBlock();
        cacheB.setType(cache.blockType());
        for (int slot = 0; slot < plr.getInventory().getSize() - 5; slot++) {
            ItemStack item = plr.getInventory().getItem(slot);
            if (item != null && item.getItemMeta().getDisplayName().equals(cache.id() + ": " + cache.name()) && item.getItemMeta().getLore().equals(List.of("A compass pointing to the location of a cache."))) {
                plr.getInventory().setItem(slot, null);
            }
        }
        plr.sendMessage(ChatColor.AQUA + "You are now within ~" + Config.getInstance().getMaxLodestoneDistance() + " blocks of the cache!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 0 ? MinecacheStorage.getInstance().getIDArray() : args.length == 1 ? MinecacheStorage.getInstance().getIDArray().stream().filter(s -> s.contains(args[0])).toList() : List.of();
    }
}
