package com.tadahtech.pub.pa.message;

import com.tadahtech.pub.pa.commands.sub.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Timothy Andis
 */
public enum Message {

    NO_PERMISSION(ChatColor.RED + "You don't have permission for this"),
    INVALID_ARGUMENTS(ChatColor.RED + "Invalid arguments for $command$");

    private String message;

    private Message(String message) {
        this.message = message;
    }

    public void send(CommandSender sender, SubCommand subCommand) {
        String message = this.message;
        message = message.replace("$command$", subCommand.getName());
        message = message.replace("$permission$", subCommand.getPermission());
        sender.sendMessage(message);
    }


}
