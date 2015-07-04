package com.tadahtech.pub.pa.menu.menus;

import com.tadahtech.pub.pa.menu.Button;
import com.tadahtech.pub.pa.menu.Menu;
import com.tadahtech.pub.pa.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public abstract class ConfirmationMenu extends Menu {

    private Menu menu;
    private static ItemStack confirm = ItemBuilder.wrap(new ItemStack(Material.EMERALD_BLOCK))
      .name(ChatColor.GREEN.toString() + ChatColor.BOLD + "Confirm")
      .build();
    private static ItemStack deny = ItemBuilder.wrap(new ItemStack(Material.REDSTONE_BLOCK))
      .name(ChatColor.RED.toString() + ChatColor.BOLD + "Cancel")
      .build();

    public ConfirmationMenu(Menu menu) {
        super("Confirm");
        this.menu = menu;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[9];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new Button(confirm, (player) -> {
                onClick(true, player);
            });
            buttons[i + 5] = new Button(deny, (player) -> {
                onClick(false, player);
            });
        }
        return buttons;
    }

    public abstract void onClick(boolean confirm, Player player);
}
