package com.tadahtech.pub.pa.menu.menus;

import com.tadahtech.pub.pa.PlayerInfo;
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
public class PlayerOptionMenu extends Menu {

    private final ItemStack base = new ItemStack(Material.INK_SACK);
    private final ItemStack icon = new ItemStack(Material.CHEST);

    private PlayerInfo info;

    public PlayerOptionMenu(String name, PlayerInfo info) {
        super(name);
        this.info = info;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[36];
        for (int i = 0; i < 9; i++) {
            buttons[i] = create(ItemBuilder.wrap(new ItemStack(Material.STAINED_GLASS_PANE))
              .name(" ")
              .data(DyeColor.BLACK.getWoolData())
              .build());
            buttons[i + 27] = buttons[i];
            if (i == 0 || i == 8) {
                for (int a = 9; a < 36; a += 9) {
                    buttons[i + a] = buttons[i];
                }
            }
        }

        boolean seeAction = info.seeActionBar();
        boolean seeGeneral = info.seeGeneral();
        boolean seeTitle = info.seeTitle();

        ItemStack[] actionBar = getActionBar();
        ItemStack[] general = getGeneral();
        ItemStack[] title = getTitle();

        buttons[11] = new Button(actionBar[1], player -> {
            info.setReceiveActionBar(!seeAction);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " Action Bar Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });
        buttons[20] = new Button(actionBar[0], player -> {
            info.setReceiveActionBar(!seeAction);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " Action Bar Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });

        buttons[13] = new Button(general[1], player -> {
            info.setReceiveGeneral(!seeGeneral);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " General Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });
        buttons[22] = new Button(general[0], player -> {
            info.setReceiveGeneral(!seeGeneral);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " General Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });

        buttons[15] = new Button(title[1], player -> {
            info.setReceiveTitle(!seeTitle);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " Title Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });
        buttons[24] = new Button(title[0], player -> {
            info.setReceiveTitle(!seeTitle);
            String message = (info.seeActionBar() ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled")
              + ChatColor.GOLD + " Title Messages.";
            player.sendMessage(Settings.GLOBAL_PREFIX + message);
            update(player);
        });
        return buttons;
    }

    public ItemStack[] getActionBar() {
        boolean seeAction = info.seeActionBar();
        ItemBuilder actionIcon = ItemBuilder.wrap(icon.clone());
        ItemBuilder actionBuilder = ItemBuilder.wrap(base.clone());
        actionBuilder.data(seeAction ? DyeColor.LIME.getDyeData() : DyeColor.GRAY.getDyeData());
        String actionName = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Action Bar";
        String actionLore = (seeAction ? ChatColor.RED + "Disable" : ChatColor.GREEN + "Enable");
        actionBuilder.name(actionName);
        actionBuilder.lore(actionLore);
        actionIcon.lore(actionLore);
        actionIcon.name(actionName);
        return new ItemStack[]{actionBuilder.build(), actionIcon.build()};
    }

    public ItemStack[] getGeneral() {
        boolean seeAction = info.seeGeneral();
        ItemBuilder actionIcon = ItemBuilder.wrap(icon.clone());
        ItemBuilder actionBuilder = ItemBuilder.wrap(base.clone());
        actionBuilder.data(seeAction ? DyeColor.LIME.getDyeData() : DyeColor.GRAY.getDyeData());
        String actionName = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "General Messages";
        String actionLore = (seeAction ? ChatColor.RED + "Disable" : ChatColor.GREEN + "Enable");
        actionBuilder.name(actionName);
        actionBuilder.lore(actionLore);
        actionIcon.lore(actionLore);
        actionIcon.name(actionName);
        return new ItemStack[]{actionBuilder.build(), actionIcon.build()};
    }

    public ItemStack[] getTitle() {
        boolean seeAction = info.seeTitle();
        ItemBuilder actionIcon = ItemBuilder.wrap(icon.clone());
        ItemBuilder actionBuilder = ItemBuilder.wrap(base.clone());
        actionBuilder.data(seeAction ? DyeColor.LIME.getDyeData() : DyeColor.GRAY.getDyeData());
        String actionName = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Title Messages";
        String actionLore = (seeAction ? ChatColor.RED + "Disable" : ChatColor.GREEN + "Enable");
        actionBuilder.name(actionName);
        actionBuilder.lore(actionLore);
        actionIcon.lore(actionLore);
        actionIcon.name(actionName);
        return new ItemStack[]{actionBuilder.build(), actionIcon.build()};
    }
}
