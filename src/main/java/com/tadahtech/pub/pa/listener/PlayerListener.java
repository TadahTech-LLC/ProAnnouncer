package com.tadahtech.pub.pa.listener;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.Settings;
import com.tadahtech.pub.pa.data.StorageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Timothy Andis
 */
public class PlayerListener implements Listener {

    private StorageManager manager;

    public PlayerListener() {
        ProAnnouncer announcer = ProAnnouncer.getInstance();
        Settings settings = announcer.getSettings();
        this.manager = settings.getStorageManager();
        announcer.getServer().getPluginManager().registerEvents(this, announcer);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.load(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        manager.save(event.getPlayer());
    }
}
