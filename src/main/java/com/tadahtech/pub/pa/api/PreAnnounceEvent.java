package com.tadahtech.pub.pa.api;

import com.tadahtech.pub.pa.announcement.Announcement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Timothy Andis
 */
public class PreAnnounceEvent extends Event implements Cancellable {

    private static final HandlerList handlerlist = new HandlerList();

    private Announcement announcement;
    private Player player;
    private boolean cancelled;

    public PreAnnounceEvent(Announcement announcement, Player player) {
        this.player = player;
        this.announcement = announcement;
    }

    public Player getPlayer() {
        return player;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
