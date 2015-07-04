package com.tadahtech.pub.pa.data;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.Players;
import org.bukkit.entity.Player;

/**
 * Created by Timothy Andis
 */
public interface StorageManager {

    public Players players = ProAnnouncer.getInstance().getPlayers();

    public void load(Player player);

    public void save(Player player);

}
