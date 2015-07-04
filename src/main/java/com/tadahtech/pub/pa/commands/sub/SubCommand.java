package com.tadahtech.pub.pa.commands.sub;

import org.bukkit.command.CommandSender;

/**
 * Created by Timothy Andis
 */
public interface SubCommand {

    public String getName();

    public String getPermission();

    public boolean isPlayerOnly();

    public String getDescription();

    public String[] getAliases();

    public void execute(CommandSender sender, String[] args);

}
