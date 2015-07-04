package com.tadahtech.pub.pa.menu.menus;

import com.tadahtech.pub.pa.Settings;
import com.tadahtech.pub.pa.menu.Button;
import com.tadahtech.pub.pa.menu.Menu;
import com.tadahtech.pub.pa.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class AdminMenu extends Menu {

    public AdminMenu(Settings settings1) {
        super(settings1.getAdminName());
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[27];
        for (int i = 0; i < 9; i++) {
            buttons[i] = create(ItemBuilder.wrap(new ItemStack(Material.STAINED_GLASS_PANE))
              .name(" ")
              .data(DyeColor.BLACK.getWoolData())
              .build());
            buttons[i + 18] = buttons[i];
            for (int a = 9; a < 27; a += 9) {
                buttons[i + a] = buttons[i];
            }
        }
        ItemBuilder builder = ItemBuilder.wrap(new ItemStack(Material.NAME_TAG));
        builder.name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Messages");
        builder.lore(" ", ChatColor.RED + "View & Edit Current Messages",
          ChatColor.RED + "Add New Messages",
          ChatColor.RED + "Remove Messages"
        );
        buttons[12] = new Button(builder.build(), (player) -> {
            new AnnouncementsMenu(ChatColor.DARK_RED + "Announcements").open(player);
        });
        builder = ItemBuilder.wrap(new ItemStack(Material.WORKBENCH));
        builder.name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Click to edit Settings");
        builder.lore(" ", ChatColor.GRAY + "Edit:",
          ChatColor.RED + "  Interval", ChatColor.RED + "  Random mode", ChatColor.RED + "  Announcement Visibility");
        buttons[14] = new Button(builder.build(), (player) -> {
            //TODO: Finish Settings
        });

        return buttons;
    }
}
