package com.tadahtech.pub.pa.commands.sub.admin;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.commands.sub.SubCommand;
import com.tadahtech.pub.pa.menu.menus.AdminMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Timothy Andis
 */
public class ProAdminCommand implements SubCommand {

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getPermission() {
        return "pa.admin";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Manage your server's ProAnnouncer Settings through a GUI";
    }

    @Override
    public String[] getAliases() {
        return new String[]{
          "a",
          "ad",
          "adminPanel"
        };
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new AdminMenu(ProAnnouncer.getInstance().getSettings()).open(player);
    }
}
