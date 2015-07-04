package com.tadahtech.pub.pa;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Timothy Andis
 */
public class Players {

    private Map<UUID, PlayerInfo> infoMap = new HashMap<>();

    private static Players instance;

    public static Players getHandler() {
        if(instance == null) {
            instance = new Players();
        }
        return instance;
    }

    public Players() {

    }

    public PlayerInfo get(Player player) {
        return get(player, false);
    }

    public PlayerInfo get(Player player, boolean nullable) {
        PlayerInfo info = infoMap.get(player.getUniqueId());
        if(info == null) {
            return nullable ? null : new PlayerInfo(player);
        }
        return info;
    }

    public void logoff(Player player) {
        PlayerInfo info = get(player, true);
        if(info == null) {
            return;
        }
        infoMap.remove(player.getUniqueId());
    }

    public void newInfo(Player player, int general, int actionBar, int title) {
        boolean receiveGeneral = general == 1;
        boolean receiveActionBar = actionBar == 1;
        boolean receiveTitle = title == 1;
        this.newInfo(player, receiveGeneral, receiveActionBar, receiveTitle);
    }

    public void newInfo(Player player, boolean receiveGeneral, boolean receiveActionBar, boolean receiveTitle) {
        PlayerInfo info = new PlayerInfo(player);
        info.setReceiveGeneral(receiveGeneral);
        info.setReceiveActionBar(receiveActionBar);
        info.setReceiveTitle(receiveTitle);
        infoMap.put(player.getUniqueId(), info);
    }

    public void add(PlayerInfo info) {
        infoMap.putIfAbsent(info.getUuid(), info);
    }
}
