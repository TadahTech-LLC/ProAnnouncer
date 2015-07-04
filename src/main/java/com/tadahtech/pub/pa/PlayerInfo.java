package com.tadahtech.pub.pa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Timothy Andis
 */
public class PlayerInfo {

    private UUID uuid;
    private boolean receiveGeneral, receiveActionBar, receiveTitle;

    public PlayerInfo(Player player) {
        this.uuid = player.getUniqueId();
        this.receiveActionBar = true;
        this.receiveGeneral = true;
        this.receiveTitle = true;
        Players.getHandler().add(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean seeGeneral() {
        return receiveGeneral;
    }

    public boolean seeActionBar() {
        return receiveActionBar;
    }

    public boolean seeTitle() {
        return receiveTitle;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setReceiveGeneral(boolean receiveGeneral) {
        this.receiveGeneral = receiveGeneral;
    }

    public void setReceiveActionBar(boolean receiveActionBar) {
        this.receiveActionBar = receiveActionBar;
    }

    public void setReceiveTitle(boolean receiveTitle) {
        this.receiveTitle = receiveTitle;
    }

    public Map<Integer, Object> toStatement() {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1,  uuid.toString());
        map.put(2, receiveGeneral ? 1 : 0);
        map.put(3, receiveActionBar ? 1 : 0);
        map.put(4, receiveTitle ? 1 : 0);
        map.put(5, receiveGeneral ? 1 : 0);
        map.put(6, receiveActionBar ? 1 : 0);
        map.put(7, receiveTitle ? 1 : 0);
        return map;
    }

    public Map<String, Boolean> toFile() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("general", receiveGeneral);
        map.put("actionBar", receiveActionBar);
        map.put("title", receiveTitle);
        return map;
    }

}
