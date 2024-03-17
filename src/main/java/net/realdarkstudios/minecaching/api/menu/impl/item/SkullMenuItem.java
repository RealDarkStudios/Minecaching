package net.realdarkstudios.minecaching.api.menu.impl.item;

import me.scarsz.mojang.Head;
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
            item = Head.create(uuid);
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
