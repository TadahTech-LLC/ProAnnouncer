package com.tadahtech.pub.pa.csc.handlers;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.api.PreAnnounceEvent;
import com.tadahtech.pub.pa.csc.MultiServerHandler;
import com.tadahtech.pub.pa.utils.PacketUtil;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class LilyPadHandler implements MultiServerHandler {

    private Connect connect;
    private static int COUNT = 0;

    public LilyPadHandler() {
        this.connect = PLUGIN.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        this.connect.registerEvents(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void broadcast(List<String> servers, String base, boolean title, String[] titleMessage, boolean actionBar, String actionBarMessage, String permission) {
        StringBuilder builder = new StringBuilder();
        builder.append(":");
        builder.append(base);
        builder.append(":");
        builder.append(title);
        builder.append(":");
        for (String s : titleMessage) {
            builder.append(s).append(",");
        }
        builder.append(":");
        builder.append(actionBar);
        builder.append(":");
        builder.append(actionBarMessage);
        builder.append(':');
        builder.append(permission);
        try {
            MessageRequest messageRequest;
            if (servers.isEmpty()) {
                messageRequest = new MessageRequest(Collections.EMPTY_LIST, CHANNEL, builder.toString());
            } else {
                messageRequest = new MessageRequest(servers, CHANNEL, builder.toString());
            }
            connect.request(messageRequest);
        } catch (UnsupportedEncodingException | RequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAllPlayers() {
        COUNT = Bukkit.getOnlinePlayers().size();
        try {
            COUNT = connect.request(new GetPlayersRequest()).await(50L).getCurrentPlayers();
        } catch (RequestException | InterruptedException e) {
            e.printStackTrace();
        }
        return COUNT;
    }

    @EventListener
    public void onMessage(MessageEvent event) {
        String channel = event.getChannel();
        if (!channel.equalsIgnoreCase(CHANNEL)) {
            return;
        }
        String msg;
        try {
            msg = event.getMessageAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        String[] str = msg.split(":");
        if (event.getSender().equalsIgnoreCase(PLUGIN.getServerName())) {
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
        toPlayers.stream().forEach(player -> {
            PreAnnounceEvent preAnnounceEvent = new PreAnnounceEvent(null, player);
            ProAnnouncer.getInstance().getServer().getPluginManager().callEvent(preAnnounceEvent);
            if (preAnnounceEvent.isCancelled()) {
                return;
            }
            player.sendMessage(color(base));
            if (actionBar && actionBarMessage != null) {
                PacketUtil.sendActionBarMessage(player, color(actionBarMessage));
            }
            if (title) {
                String tit = (titleMessage[0]);
                if (titleMessage.length == 2) {
                    String subtitle = (titleMessage[1]);
                    PacketUtil.sendTitleToPlayer(player, color(tit), color(subtitle));
                } else {
                    PacketUtil.sendTitleToPlayer(player, color(tit), null);
                }
            }
        });
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void disable() {
        this.connect.unregisterEvents(this);
    }
}
