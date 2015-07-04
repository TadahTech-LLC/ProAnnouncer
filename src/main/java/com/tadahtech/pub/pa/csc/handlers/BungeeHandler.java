package com.tadahtech.pub.pa.csc.handlers;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.api.PreAnnounceEvent;
import com.tadahtech.pub.pa.csc.MultiServerHandler;
import com.tadahtech.pub.pa.utils.PacketUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class BungeeHandler implements MultiServerHandler, PluginMessageListener {

	private int playerCount = 0;

	public BungeeHandler() {
		PLUGIN.getServer().getMessenger().registerOutgoingPluginChannel(PLUGIN, "BungeeCord");
		PLUGIN.getServer().getMessenger().registerIncomingPluginChannel(PLUGIN, "BungeeCord", this);
		updatePc();
	}

	@Override
	public void broadcast(List<String> servers, String base, boolean title, String[] titleMessage, boolean actionBar, String actionBarMessage, String permission) {
		StringBuilder builder = new StringBuilder();
		builder.append(":");
		builder.append(base);
		builder.append(":");
		builder.append(title);
		builder.append(":");
		for(String s : titleMessage) {
			builder.append(s).append(",");
		}
		builder.append(":");
		builder.append(actionBar);
		builder.append(":");
		builder.append(actionBarMessage);
		builder.append(':');
		builder.append(permission);
		Player player = null;
		for(Player p : Bukkit.getOnlinePlayers()) {
			player = p;
			break;
		}
		if(player == null) {
			System.out.println("No Player on Server: " + PLUGIN.getServerName());
			return;
		}
		if(servers.isEmpty()) {
			sendInfo("ALL", player, builder.toString());
		}
		for(String server : servers) {
			sendInfo(server, player, builder.toString());
		}
	}

	@Override
	public int getAllPlayers() {
		return playerCount;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		if(!channel.equalsIgnoreCase("BungeeCord")) {
			return;
		}
		try {
			String sub = in.readUTF();
			if(!sub.equalsIgnoreCase(CHANNEL)) {
				in.readUTF();
				playerCount = in.readInt();
				return;
			}
			String msg = in.readUTF();
			if(!msg.contains(":")) {
				return;
			}
			String[] str = msg.split(":");
			String server = str[0];
			if(!server.equalsIgnoreCase(PLUGIN.getServerName()) && !server.equalsIgnoreCase("all")) {
				return;
			}
			String base = str[1];
			boolean title = Boolean.valueOf(str[2]);
			String[] titleMessage = str[3].split(",");
			boolean actionBar = Boolean.valueOf(str[4]);
			String actionBarMessage = str[5];
			String permission = str[6];
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			List<Player> toPlayers = new ArrayList<>();
			players.stream().forEach(play -> {
				if (play.hasPermission(permission) || play.isOp()) {
					toPlayers.add(play);
				}
			});
			if (toPlayers.isEmpty()) {
				return;
			}
			toPlayers.stream().forEach(play -> {
				PreAnnounceEvent event = new PreAnnounceEvent(null, player);
				ProAnnouncer.getInstance().getServer().getPluginManager().callEvent(event);
				if(event.isCancelled()) {
					return;
				}
				play.sendMessage(color(base));
				if (actionBar && actionBarMessage != null) {
					PacketUtil.sendActionBarMessage(play, color(actionBarMessage));
				}
				if (title) {
					String tit = (titleMessage[0]);
					if (titleMessage.length == 2) {
						String subtitle = (titleMessage[1]);
						PacketUtil.sendTitleToPlayer(play, color(tit), color(subtitle));
					} else {
						PacketUtil.sendTitleToPlayer(play, color(tit), null);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public void sendInfo(String server, Player player, String message) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF(CHANNEL);
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.sendPluginMessage(PLUGIN, "BungeeCord", b.toByteArray());
	}

	private void updatePc() {
		new BukkitRunnable() {
			@Override
			public void run() {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF("PlayerCount");
					out.writeUTF("ALL");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Player player = null;
				for(Player p : Bukkit.getOnlinePlayers()) {
					player = p;
					break;
				}
				if(player == null) {
					System.out.println("No Player on Server: " + PLUGIN.getServerName());
					return;
				}
				player.sendPluginMessage(PLUGIN, "BungeeCord", b.toByteArray());
			}
		}.runTaskTimerAsynchronously(ProAnnouncer.getInstance(), 20L, 5L);
	}
}
