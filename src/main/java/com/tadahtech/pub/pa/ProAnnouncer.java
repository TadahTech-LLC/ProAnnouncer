package com.tadahtech.pub.pa;

import com.tadahtech.pub.pa.commands.ProCommand;
import com.tadahtech.pub.pa.commands.sub.SubCommand;
import com.tadahtech.pub.pa.csc.MultiServerHandler;
import com.tadahtech.pub.pa.csc.handlers.BungeeHandler;
import com.tadahtech.pub.pa.csc.handlers.LilyPadHandler;
import com.tadahtech.pub.pa.listener.MenuListener;
import com.tadahtech.pub.pa.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

/**
 * Created by Timothy Andis
 */
public class ProAnnouncer extends JavaPlugin {

	private static ProAnnouncer instance;
	private Settings settings;
	private Players players;
	private AnnouncerThread thread;
	private ProCommand proCommand;
	private MultiServerHandler multiServerHandler;
	private String server;

	public static ProAnnouncer getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		this.players = new Players();
		this.settings = new Settings(getConfig());
		this.server = getConfig().getString("server");
		getLogger().info("Settings loaded. Server name: " + server);
		String msm = getConfig().getString("msm");
		switch (msm.toLowerCase()) {
			case "lily":
			case "lilypad":
			case "pad":
				if (!getServer().getPluginManager().isPluginEnabled("LilyPad-Connect")) {
					getLogger().severe("Attempted to register with LilyPad, but no Connect was found...");
					getLogger().severe("Disabling Global Messages...");
					setMultiServerHandler(null);
					break;
				}
				this.multiServerHandler = new LilyPadHandler();
				getLogger().info("Using LilyPad for MultiServerManagement");
				break;
			case "bungee":
			case "bungeecord":
			case "cord":
				this.multiServerHandler = new BungeeHandler();
				getLogger().info("Using BungeeCord for MultiServerManagement");
				break;
			default:
				getLogger().info("No viable MSM (" + msm + ") found. Disabling global messages..");
				break;
		}
		this.thread = new AnnouncerThread(settings);
		getLogger().info("Preparing the Announcement thread." +
		  " Random Mode: " + String.valueOf(settings.isRandom()) + "," +
		  " Interval: " + settings.getInterval() / 20);
		new PlayerListener();
		new MenuListener();
		this.proCommand = new ProCommand();
		this.getCommand("pa").setExecutor(proCommand);
		registerCommands();
	}

	@Override
	public void onDisable() {
		if(this.multiServerHandler instanceof LilyPadHandler) {
			LilyPadHandler lilyPadHandler = (LilyPadHandler) multiServerHandler;
			lilyPadHandler.disable();
		}
	}

	private void registerCommands() {
		int i = 0;
		Reflections reflections = new Reflections("com.tadahtech.pub.pa.commands");
		for (Class clazz : reflections.getSubTypesOf(SubCommand.class)) {
			try {
				SubCommand subCommand = (SubCommand) clazz.newInstance();
				this.proCommand.register(subCommand);
				i++;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		getLogger().info("Registered " + i + " command" + (i > 1 ? "s" : "") + ".");
	}

	public Settings getSettings() {
		return settings;
	}

	public Players getPlayers() {
		return players;
	}

	public AnnouncerThread getThread() {
		return thread;
	}

	public void setThread(AnnouncerThread thread) {
		this.thread = thread;
	}

	public ProCommand getProCommand() {
		return proCommand;
	}

	public MultiServerHandler getMultiServerHandler() {
		return multiServerHandler;
	}

	public void setMultiServerHandler(MultiServerHandler handler) {
		this.multiServerHandler = handler;
	}

	public String getServerName() {
		return server;
	}


}
