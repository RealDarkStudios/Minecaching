package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import net.realdarkstudios.minecaching.api.util.MessageKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfirmationMenu extends MCMenu {
    private final MenuItem confirmItem;

    public ConfirmationMenu(PlayerDataObject initiator, MenuItem confirmItem, JavaPlugin plugin, MCMenu parent) {
        super(MessageKeys.Menu.CONFIRMATION, MenuSize.THREE_ROW, plugin, parent);
        this.confirmItem = confirmItem;

        update(initiator.getPlayer());
    }

    @Override
    public void update(Player player) {
        setItem(11, confirmItem);
        setItem(15, new GoBackMenuItem(new ItemStack(Material.RED_CONCRETE), List.of()));

        super.update(player);
    }
}
