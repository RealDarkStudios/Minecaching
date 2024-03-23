package net.realdarkstudios.minecaching.api.menu.impl.item;

import me.scarsz.mojang.Head;
import me.scarsz.mojang.Mojang;
import me.scarsz.mojang.exception.ProfileFetchException;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SkullMenuItem extends MenuItem {
    private final UUID uuid;
    public SkullMenuItem(String name, UUID uuid, List<String> lore) {
        super(name, Head.getPlayerSkullItem(), lore);
        this.uuid = uuid;
    }

    public ItemStack getSkull(OfflinePlayer player) {
        ItemStack item;
        if (uuid != null) {
            if (uuid.equals(MCUtils.EMPTY_UUID)) {
                item = new ItemStack(Material.PLAYER_HEAD);
            } else {
                try {
                    if (Mojang.fetch(uuid) == null) throw new ProfileFetchException(uuid.toString(), new Exception("Could not fetch data for UUID " + uuid));
                    item = !Bukkit.getOnlineMode() || uuid.equals(MCUtils.EMPTY_UUID) ? new ItemStack(Material.PLAYER_HEAD) : Head.create(uuid);
                } catch (ProfileFetchException e) {
                    MinecachingAPI.warning(e.getMessage());
                    item = new ItemStack(Material.PLAYER_HEAD);
                }
            }
        } else {
            item = Head.create(player);
        }

        return applyText(item);
    }

    @Override
    public ItemStack getIcon(Player player) {
        return getSkull(player);
    }
}
