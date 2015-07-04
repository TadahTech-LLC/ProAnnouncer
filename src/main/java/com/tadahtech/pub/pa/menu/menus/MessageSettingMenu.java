package com.tadahtech.pub.pa.menu.menus;

import com.tadahtech.pub.pa.announcement.Announcement;
import com.tadahtech.pub.pa.conversation.OneReplyConvo;
import com.tadahtech.pub.pa.menu.Button;
import com.tadahtech.pub.pa.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class MessageSettingMenu extends Menu {

    private Announcement announcement;
    private ItemStack actionBar, actionBarMessage, title, titleMessages, messages, permission;

    public MessageSettingMenu(Announcement announcement) {
        super("Edit: " + announcement.getName());
        this.announcement = announcement;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[45];
        //TODO: Finish Settings

        return buttons;
    }

    private class ActionBarConvo extends OneReplyConvo {

        public ActionBarConvo(String prompt, Player player) {
            super(prompt, player);
        }

        @Override
        public void reply(String s) {
            s = s.replace("_", " ");
            announcement.setActionBarMessage(s);
        }
    }

    private class TitleMessage extends OneReplyConvo {

        public TitleMessage(String prompt, Player player) {
            super(prompt, player);
        }

        @Override
        public void reply(String s) {
            s = s.replace("_", " ");
            announcement.setTitleMessage(s.split(","));
        }
    }

    private class AddMessageConvo extends OneReplyConvo {

        public AddMessageConvo(String prompt, Player player) {
            super(prompt, player);
        }

        @Override
        public void reply(String s) {
            s = s.replace("_", " ");
            announcement.addMessage(s);
        }
    }

    private class PermissionConvo extends OneReplyConvo {

        public PermissionConvo(String prompt, Player player) {
            super(prompt, player);
        }

        @Override
        public void reply(String s) {
            announcement.setPermission(s);
        }
    }
}
