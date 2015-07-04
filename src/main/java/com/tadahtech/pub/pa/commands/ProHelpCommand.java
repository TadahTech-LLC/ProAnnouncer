package com.tadahtech.pub.pa.commands;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.commands.sub.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

/**
 * Created by Timothy Andis
 */
public class ProHelpCommand implements SubCommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean isPlayerOnly() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Show this display";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Collection<SubCommand> commands = ProAnnouncer.getInstance().getProCommand().getSubCommands();
		StringBuilder builder = new StringBuilder();
		String color = ChatColor.BLUE.toString() + ChatColor.BOLD;
		String buff = color + ChatColor.STRIKETHROUGH + "=";
		for(int i = 0; i < 15; i++) {
			builder.append(buff);
		}
		String line = builder.toString();
		builder = new StringBuilder();
		builder.append(line);
		builder.append(ChatColor.RESET).append(color).append(ChatColor.STRIKETHROUGH).append("[");
		builder.append(ChatColor.RESET).append(ChatColor.GRAY).append("  ProAnnouncer  ");
		builder.append(ChatColor.RESET).append(color).append(ChatColor.STRIKETHROUGH).append("]");
		builder.append(line);
		String buffer = builder.toString();
		builder = new StringBuilder();
		builder.append("\n");
		builder.append(color).append("Version: ").append(ChatColor.GRAY).append(ProAnnouncer.getInstance().getDescription().getVersion());
		builder.append("\n");
		builder.append(color).append("Author: ").append(ChatColor.GRAY).append("TadahTech || calebbfmv || Timothy");
		builder.append("\n \n");
		for(SubCommand command : commands) {
			builder.append(color).append("/pa ").append(command.getName()).append(ChatColor.GRAY).append(" - ").append(command.getDescription());
			builder.append("\n");
		}
		sender.sendMessage(buffer);
		sender.sendMessage(builder.toString() + "\n ");
		sender.sendMessage(buffer);
	}
}
