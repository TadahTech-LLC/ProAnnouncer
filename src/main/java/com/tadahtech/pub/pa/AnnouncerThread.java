package com.tadahtech.pub.pa;

import com.tadahtech.pub.pa.announcement.Announcement;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

/**
 * Created by Timothy Andis
 */
public class AnnouncerThread extends BukkitRunnable {

    private Settings settings;
    private Random random = new Random();
    private boolean r;
    private int total, last;
    private List<Announcement> announcements, sent;

    public AnnouncerThread(Settings settings) {
        this.settings = settings;
        this.r = settings.isRandom();
        this.announcements = settings.getAnnouncements();
        this.sent = settings.getSentAnnouncements();
        this.total = announcements.size();
        this.runTaskTimer(ProAnnouncer.getInstance(), settings.getInterval(), settings.getInterval());
    }

    @Override
    public void run() {
        if(!settings.isEnabled()) {
            return;
        }
        if(r) {
            if(announcements.size() == 0) {
                announcements = settings.getAnnouncements();
                sent.clear();
            }
            Announcement announcement;
            try {
                 announcement = announcements.get(random.nextInt(announcements.size()));
            } catch (Exception e) {
                announcements = settings.getAnnouncements();
                sent.clear();
                announcement = announcements.get(random.nextInt(announcements.size()));
            }
            sent.add(announcement);
            announcements.remove(announcement);
            //If I ever feel like making it so you can see how many messages are left.
//            settings.getSentMessages().add(message);
            announcement.announce();
            return;
        }
        Announcement announcement = announcements.get(last);

        //If I ever feel like making it so you can see how many messages are left.
//            settings.getSentMessages().add(message);
        announcement.announce();
        last++;
        if(last == total) {
            last = 0;
        }
    }

    public Settings getSettings() {
        return settings;
    }

    /**
     * Config options reload, so lets reset the clock.
     * @param settings The new settings file
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
        cancel();
        AnnouncerThread thread = new AnnouncerThread(settings);
        ProAnnouncer.getInstance().setThread(thread);
    }
}
