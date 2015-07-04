package com.tadahtech.pub.pa.announcement;

import com.tadahtech.pub.pa.PlayerInfo;
import com.tadahtech.pub.pa.Players;
import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.Settings;
import com.tadahtech.pub.pa.api.PreAnnounceEvent;
import com.tadahtech.pub.pa.csc.MultiServerHandler;
import com.tadahtech.pub.pa.utils.PacketUtil;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Timothy Andis
 */
public class Announcement {

    private List<String> messages;
    private boolean overrideIgnore, title, actionBar, autoInsert, tryPerMessagePrefix, global;
    private String permission, actionBarMessage, prefix, name;
    private String[] titleMessage;
    private List<String> servers;
    private static final String[] ADVERBS = {
      "awesome",
      "smelly",
      "my best friend.",
      "my only friend",
      "lonely",
      "extremely lucky! Here! Take a stick!"
    };

    private static Map<String, Announcement> allMessages = new HashMap<>();

    public Announcement(String name, List<String> messages, boolean overrideIgnore, boolean title, boolean actionBar, boolean autoInsert, boolean tryPerMessagePrefix, String permission, String actionBarMessage, String prefix, String[] titleMessage, boolean global, List<String> servers) {
        this.name = name;
        this.overrideIgnore = overrideIgnore;
        this.title = title;
        this.actionBar = actionBar;
        this.autoInsert = autoInsert;
        this.tryPerMessagePrefix = tryPerMessagePrefix;
        this.permission = permission;
        this.actionBarMessage = actionBarMessage;
        this.prefix = prefix;
        this.titleMessage = titleMessage;
        this.messages = messages;
        this.global = global;
        this.servers = servers;
        allMessages.put(name.replace("_", " "), this);
    }

    public void announce() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<Player> toPlayers = new ArrayList<>();
        players.stream().forEach(player -> {
            if (player.hasPermission(permission) || player.isOp()) {
                toPlayers.add(player);
            }
        });
        if (toPlayers.isEmpty() && !global) {
            return;
        }
        if (global) {
            for (String s : messages) {
                if (global) {
                    MultiServerHandler handler = ProAnnouncer.getInstance().getMultiServerHandler();
                    if (handler == null) {
                        System.out.println("No MSM Found, Not sending messages");
                        return;
                    }
                    handler.broadcast(servers, s, title, titleMessage, actionBar, actionBarMessage, permission);
                }
            }
        }
        toPlayers.stream().forEach(player -> {
            for (String s : this.messages) {
                build(s, player);
            }
        });
    }

    private String build(String s, Player player) {
        PlayerInfo info = Players.getHandler().get(player);

        PreAnnounceEvent event = new PreAnnounceEvent(this, player);

        ProAnnouncer.getInstance().getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return null;
        }

        String base = s;
        base = edit(base);
        base = color(base);
        base = base.replace("$!include$", "");
        base = base.replace("$split$", "");
        base = base.replace("$player$", player.getName());

        if (!autoInsert) {
            String pref = (this.tryPerMessagePrefix ? (this.prefix == null ? Settings.GLOBAL_PREFIX : this.prefix) : Settings.GLOBAL_PREFIX);
            base = base.replace("$prefix$", pref);
        } else {
            String pref = (this.tryPerMessagePrefix ? (this.prefix == null ? Settings.GLOBAL_PREFIX : this.prefix) : Settings.GLOBAL_PREFIX);
            base = pref + base.replace("$prefix$", "");
        }
        if (base.contains("$randomAdverb$")) {
            double value = new Random().nextInt(100) + 1;
            String adverb;
            boolean lucky = false;
            if (value <= 99) {
                adverb = ADVERBS[new Random().nextInt(ADVERBS.length - 1)];
            } else {
                adverb = ADVERBS[ADVERBS.length - 1];
                lucky = true;
            }
            base = base.replace("$randomAdverb$", adverb);
            if (lucky) {
                player.getInventory().addItem(new ItemStack(Material.STICK));
            }
        }
        Economy economy = ProAnnouncer.getInstance().getSettings().getEconomy();
        if (economy == null) {
            base = base.replace("$balance$", "0");
        } else {
            base = base.replace("$balance$", String.valueOf(economy.getBalance(player)));
        }
        int size = Bukkit.getOnlinePlayers().size();
        if (base.contains("$global")) {
            try {
                int all = ProAnnouncer.getInstance().getMultiServerHandler().getAllPlayers();
                base = base.replace("$globalPlayers$ players", all + (all == 1 ? " player" : " players"));
            } catch (NullPointerException e) {
                ProAnnouncer.getInstance().getLogger().info("Failed to retrieve global playercount! Skipping this message!");
                return null;
            }
        }
        base = base.replace("$serverPlayers$ players", size + (size == 1 ? " player" : " players"));
        base = base.replace("$!include$", "");
        base = base.replace("$split$", "");
        base = color(base);
        if (info.seeGeneral()) {
            player.sendMessage(base);
        }
        if (actionBar && actionBarMessage != null) {
            if (info.seeActionBar()) {
                PacketUtil.sendActionBarMessage(player, color(actionBarMessage));
            }
        }
        if (title && titleMessage != null) {
            String title = color(titleMessage[0]);
            if (titleMessage.length == 2) {
                String subtitle = color(titleMessage[1]);
                if (info.seeTitle()) {
                    PacketUtil.sendTitleToPlayer(player, title, subtitle);
                }
            } else {
                if (info.seeTitle()) {
                    PacketUtil.sendTitleToPlayer(player, title, null);
                }
            }
        }
        return base;
    }

    private String edit(String s) {
        if (ChatColor.stripColor(s).startsWith("$copy")) {
            String[] str = s.replace("$", "").split("@");
            if (str.length == 2) {
                try {
                    int slot = Integer.parseInt(str[1]) - 1;
                    return messages.get(slot);
                } catch (IndexOutOfBoundsException e) {
                    ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like we tried getting a message that doesn't quite exist! Try again!");
                } catch (NumberFormatException e) {
                    ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like the number " + str[1] + " was not a valid integer!");
                }
            } else if (str.length == 3) {
                try {
                    int slot = Integer.parseInt(str[1]) - 1;
                    String messageRaw = str[2];
                    Announcement announcement = allMessages.get(messageRaw.replace("_", " "));
                    return announcement.messages.get(slot);
                } catch (NullPointerException e) {
                    ProAnnouncer.getInstance().getLogger().warning("Heyo! The announcement " + str[2] + " doesn't seem to exist...");
                } catch (IndexOutOfBoundsException e) {
                    ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like that number (" + str[1] + ") isn't a valid message in the Announcement " + str[2].replace("_", " "));
                } catch (NumberFormatException e) {
                    ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like the number " + str[1] + " was not a valid integer!");
                }
            }
        }
        return s;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isOverrideIgnore() {
        return overrideIgnore;
    }

    public boolean isTitle() {
        return title;
    }

    public boolean isActionBar() {
        return actionBar;
    }

    public String getPermission() {
        return permission;
    }

    public String getActionBarMessage() {
        return actionBarMessage;
    }

    public String[] getTitleMessage() {
        return titleMessage;
    }

    public String getName() {
        return name;
    }

    public void setActionBarMessage(String actionBarMessage) {
        this.actionBarMessage = actionBarMessage;
    }

    public void setTitleMessage(String[] titleMessage) {
        this.titleMessage = titleMessage;
    }

    public void addMessage(String s) {
        if (s.contains("@")) {
            String[] str = s.split("@");
            try {
                int slot = Integer.parseInt(str[1]) - 1;
                String messageRaw = str[0];
                this.messages.set(slot, messageRaw);
            } catch (NullPointerException e) {
                ProAnnouncer.getInstance().getLogger().warning("Heyo! The announcement " + str[2] + " doesn't seem to exist...");
            } catch (IndexOutOfBoundsException e) {
                ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like that number (" + str[1] + ") isn't a valid message in the Announcement " + str[2].replace("_", " "));
            } catch (NumberFormatException e) {
                ProAnnouncer.getInstance().getLogger().warning("Heyo! Looks like the number " + str[1] + " was not a valid integer!");
            }
            return;
        }
        this.messages.add(s);
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
