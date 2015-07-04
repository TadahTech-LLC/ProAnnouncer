package com.tadahtech.pub.pa.commands.sub.player;

import com.tadahtech.pub.pa.PlayerInfo;
import com.tadahtech.pub.pa.Players;
import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.commands.sub.SubCommand;
import com.tadahtech.pub.pa.menu.menus.PlayerOptionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Timothy Andis
 */
public class ProSettingsCommand implements SubCommand {
	@Override
	public String getName() {
		return "settings";
	}

	@Override
	public String getPermission() {
		return "pa.settings";
	}

	@Override
	public boolean isPlayerOnly() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Manager your settings";
	}

	@Override
	public String[] getAliases() {
		return new String[] {
		  "s",
		  "sett",
		  "setting"
		};
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		PlayerInfo info = Players.getHandler().get(player);
		String name = ProAnnouncer.getInstance().getSettings().getPlayerName();
		new PlayerOptionMenu(name, info).open(player);
	}
}
