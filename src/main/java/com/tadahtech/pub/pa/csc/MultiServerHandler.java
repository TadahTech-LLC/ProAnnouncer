package com.tadahtech.pub.pa.csc;

import com.tadahtech.pub.pa.ProAnnouncer;

import java.util.List;

/**
 * Created by Timothy Andis
 */
public interface MultiServerHandler {

    static String CHANNEL = "ProAnnouncer";
    static ProAnnouncer PLUGIN = ProAnnouncer.getInstance();

    public void broadcast(List<String> servers, String base, boolean title, String[] titleMessage, boolean actionBar, String actionBarMessage, String permission);

    public int getAllPlayers();
}
