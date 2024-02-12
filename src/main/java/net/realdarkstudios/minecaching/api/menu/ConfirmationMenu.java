package net.realdarkstudios.minecaching.api.menu;

import net.realdarkstudios.minecaching.api.menu.impl.MCMenu;
import net.realdarkstudios.minecaching.api.menu.impl.item.GoBackMenuItem;
import net.realdarkstudios.minecaching.api.menu.impl.item.MenuItem;
import net.realdarkstudios.minecaching.api.player.PlayerDataObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfirmationMenu extends MCMenu {
    private final MenuItem confirmItem;

    public ConfirmationMenu(PlayerDataObject initiator, MenuItem confirmItem, JavaPlugin plugin, MCMenu parent) {
        super("menu.confirmation.title", MenuSize.THREE_ROW, plugin, parent);
        this.confirmItem = confirmItem;

        update(initiator.getPlayer().getPlayer());
    }

    @Override
    public void update(Player player) {
        setItem(11, confirmItem);
        setItem(15, new GoBackMenuItem(translation("menu.goback"), new ItemStack(Material.RED_CONCRETE), List.of()));

        super.update(player);
    }

    @Override
    protected String itemTranslation(String id, Object... substitutions) {
        return null;
    }
}
