package com.tadahtech.pub.pa;

import com.tadahtech.pub.pa.announcement.Announcement;
import com.tadahtech.pub.pa.data.StorageManager;
import com.tadahtech.pub.pa.data.file.FileManager;
import com.tadahtech.pub.pa.data.sql.SQLManager;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class Settings {

    private boolean enabled, random, persistInfo, tryPerMessagePrefix = true;
    private StorageManager storageManager;
    private List<Announcement> announcements, sentAnnouncements;
    public static String GLOBAL_PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "ProAnnouncer" + ChatColor.GRAY + "] " + ChatColor.YELLOW;
    private int interval;
    private Economy economy;

    private FileConfiguration config;
    private String adminName, playerName;

    public Settings(FileConfiguration config) {
        this.config = config;
        setUpEcon();
        load();
    }

    private void setUpEcon() {
        RegisteredServiceProvider<Economy> rsp = ProAnnouncer.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            ProAnnouncer.getInstance().getLogger().warning("Heyp! I couldn't find a hook with an Economy plugin, $balance$ will show as 0");
            return;
        }
        this.economy = rsp.getProvider();
    }

    public void load() {
        this.enabled = config.getBoolean("enabled", true);
        this.random = config.getBoolean("random", false);
        this.persistInfo = config.getBoolean("persistData", true);
        this.interval = config.getInt("interval", 10) * 20;
        this.adminName = config.getString("guis.admin");
        this.playerName = config.getString("guis.player");
        if (config.getString("prefix") != null) {
            tryPerMessagePrefix = false;
        }
        String sm = config.getString("storage.type");
        switch (sm.toUpperCase()) {
            case "SQL": {
                String host = config.getString("sql.host");
                String pass = config.getString("sql.pass");
                String db = config.getString("sql.db");
                String user = config.getString("sql.user");
                int port = config.getInt("sql.port");
                this.storageManager = new SQLManager(host, db, user, pass, port);
                break;
            }
            case "FILE":
                boolean oneFile = config.getBoolean("file.oneFile");
                File dir = ProAnnouncer.getInstance().getDataFolder();
                this.storageManager = new FileManager(new File(dir, "players"), oneFile);
                break;
            default:
                System.out.println("Unknown Storage type: " + sm.toUpperCase() + "! Allowed: SQL, FILE");
                break;
        }
        this.sentAnnouncements = new ArrayList<>();
        this.announcements = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("messages");
        for (String s : section.getKeys(false)) {
            ConfigurationSection message = section.getConfigurationSection(s);
            List<String> messages = message.getStringList("messages");
            String prefix = message.getString("prefix", GLOBAL_PREFIX);
            String permission = message.getString("permission", "announcer.receive");
            boolean overrideIgnore = message.getBoolean("overrideIgnore", false);
            boolean title = message.getBoolean("title");
            boolean actionBar = message.getBoolean("actionBar");
            boolean autoInsert = message.getBoolean("autoInsert");
            boolean global = message.getBoolean("global");
            boolean tryPerMessagePrefix = this.tryPerMessagePrefix;
            List<String> servers = message.getStringList("servers");
            String actionBarMessage = message.getString("actionBarMessage");
            List<String> titleRaw = message.getStringList("titleMessage");
            String[] titleMessage = new String[0];
            if (titleRaw != null) {
                //Title and Subtitle only.
                if (titleRaw.size() > 2) {
                    List<String> clone = titleRaw;
                    //Gotta avoid the CME
                    for (int i = 2; i < titleRaw.size(); i++) {
                        clone.remove(i);
                    }
                    titleRaw = clone;
                }
                titleMessage = titleRaw.toArray(new String[titleRaw.size()]);
            }
            Announcement newAnnouncement = new Announcement(s, messages, overrideIgnore, title, actionBar, autoInsert, tryPerMessagePrefix, permission, actionBarMessage, prefix, titleMessage, global, servers);
            this.announcements.add(newAnnouncement);
        }
    }

    public void save() {
        this.config.set("enabled", this.enabled);
        this.config.set("random", this.random);
        this.config.set("persistData", this.persistInfo);
        this.config.set("interval", this.interval);
        for (Announcement announcement : this.announcements) {
            ConfigurationSection section = config.getConfigurationSection("messages").getConfigurationSection(announcement.getName());
            List<String> messages = announcement.getMessages();
            section.set("messages", messages);
            boolean override = announcement.isOverrideIgnore();
            boolean actionBar = announcement.isActionBar();
            boolean title = announcement.isTitle();
            String[] titleMessage = announcement.getTitleMessage();
            String actionBarMessage = announcement.getActionBarMessage();
            String permission = announcement.getPermission();
            section.set("overrideIgnore", override);
            section.set("title", title);
            section.set("actionBar", actionBar);
            section.set("actionBarMessage", actionBarMessage);
            section.set("permission", permission);
            section.set("titleMessage", new ArrayList<>(Arrays.asList(titleMessage)));
        }
        ProAnnouncer.getInstance().saveConfig();
    }

    public void reload() {
        save();
        load();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public int getInterval() {
        return interval;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public List<Announcement> getSentAnnouncements() {
        return sentAnnouncements;
    }

    public boolean isPersistInfo() {
        return persistInfo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isRandom() {
        return random;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getPlayerName() {
        return playerName;
    }
}
