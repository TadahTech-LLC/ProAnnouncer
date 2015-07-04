package com.tadahtech.pub.pa.data.file;

import com.tadahtech.pub.pa.PlayerInfo;
import com.tadahtech.pub.pa.data.StorageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Timothy Andis
 */
public class FileManager implements StorageManager {

    private File dir;
    private boolean oneFile;

    public FileManager(File dir, boolean oneFile) {
        this.dir = dir;
        dir.mkdirs();
        this.oneFile = oneFile;
    }

    @Override
    public void load(Player player) {
        if(oneFile) {
            File file = new File(dir, "players.yml");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = config.getConfigurationSection("players");
            ConfigurationSection playerSection = section.getConfigurationSection(player.getUniqueId().toString());
            if(playerSection == null) {
                section.createSection(player.getUniqueId().toString());
                players.newInfo(player, true, false, false);
                save(player);
                return;
            }
            boolean general = playerSection.getBoolean("general");
            boolean actionBar = playerSection.getBoolean("actionBar");
            boolean title = playerSection.getBoolean("title");
            players.newInfo(player, general, actionBar, title);
            return;
        }
        File file = new File(dir, player.getUniqueId().toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            players.newInfo(player, true, false, false);
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean general = config.getBoolean("general", true);
        boolean actionBar = config.getBoolean("actionBar", false);
        boolean title = config.getBoolean("title", false);
        players.newInfo(player, general, actionBar, title);
    }

    @Override
    public void save(Player player) {
        PlayerInfo info = players.get(player);
        if(oneFile) {
            File file = new File(dir, "players.yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = config.getConfigurationSection("players");
            ConfigurationSection playerSection = section.getConfigurationSection(player.getUniqueId().toString());
            if(playerSection == null) {
                playerSection = section.createSection(player.getUniqueId().toString());
            }
            set("general", info.seeGeneral(), file , config, playerSection);
            set("actionBar", info.seeActionBar(), file , config, playerSection);
            set("title", info.seeTitle(), file , config, playerSection);
            return;
        }
        File file = new File(dir, player.getUniqueId().toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Map<String, Boolean> map = info.toFile();
        for(Map.Entry<String, Boolean> entry : map.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void set(String path, Object value, File file, FileConfiguration configuration, ConfigurationSection section) {
        section.set(path, value);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
