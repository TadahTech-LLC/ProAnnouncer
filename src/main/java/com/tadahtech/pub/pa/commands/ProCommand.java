package com.tadahtech.pub.pa.commands;

import com.tadahtech.pub.pa.commands.sub.SubCommand;
import com.tadahtech.pub.pa.message.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Timothy Andis
 */
public class ProCommand implements CommandExecutor {

	private Map<String, SubCommand> subCommands = new HashMap<>();
	private Map<String, SubCommand> subCommandAliases = new HashMap<>();

	public ProCommand() {

	}

	public Collection<SubCommand> getSubCommands() {
		return subCommands.values();
	}

	public void register(SubCommand command) {
		String[] aliases = command.getAliases();
		String name = command.getName();
		subCommands.put(name, command);
		if(aliases != null && aliases.length > 0) {
			Arrays.asList(aliases).stream().forEach(s -> subCommandAliases.put(s, command));
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ProHelpCommand helpCommand = (ProHelpCommand) subCommands.get("help");
		if(args == null || args.length == 0 || args[0].equalsIgnoreCase("help")) {
			helpCommand.execute(sender, null);
			return true;
		}
		String cmd = args[0].toLowerCase();
		SubCommand subCommand = subCommands.get(cmd);
		if(subCommand == null) {
			subCommand = subCommandAliases.get(cmd);
		}
		//No command, or alias registered.
		if(subCommand == null) {
			helpCommand.execute(sender, null);
			return true;
		}
		String[] cmdArgs = new String[args.length];
		for(int i = 1; i < args.length; i++) {
			String s = args[i];
			cmdArgs[i - 1] = s;
		}
		if(subCommand.isPlayerOnly() && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is player only!");
			return true;
		}
		if(subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
			Message.NO_PERMISSION.send(sender, subCommand);
			return true;
		}
		subCommand.execute(sender, cmdArgs);
		return true;
	}
}
