package com.tadahtech.pub.pa.menu.menus;

import com.tadahtech.pub.pa.ProAnnouncer;
import com.tadahtech.pub.pa.Settings;
import com.tadahtech.pub.pa.announcement.Announcement;
import com.tadahtech.pub.pa.menu.Button;
import com.tadahtech.pub.pa.menu.Menu;
import com.tadahtech.pub.pa.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class AnnouncementsMenu extends Menu {

	public AnnouncementsMenu(String name) {
		super(name);
	}

	/**
	 * Why does this look so weird in Git?
	 */

	@Override
	protected Button[] setUp() {
		Settings settings = ProAnnouncer.getInstance().getSettings();
		Button[] buttons = new Button[settings.getAnnouncements().size()];
		List<Announcement> announcements = settings.getAnnouncements();
		for (int i = 0; i < announcements.size(); i++) {
			Announcement announcement = announcements.get(i);
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemBuilder builder = ItemBuilder.wrap(itemStack);
			builder.name(ChatColor.GRAY + "Announcement: " + ChatColor.DARK_AQUA + announcement.getName());
			List<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add(ChatColor.DARK_AQUA + "Messages: ");
			for (String s : announcement.getMessages()) {
				if (ChatColor.stripColor(s).contains("$!include$")) {
					continue;
				}
				if (s.contains("$split$")) {
					String[] split = s.split("\\$split\\$");
					System.out.println(s + " :: " + Arrays.toString(split));
					lore.add(ChatColor.GRAY + "  - " + split[0]);
					lore.add(ChatColor.GRAY + "  - CONT: " + split[1]);
					continue;
				}
				lore.add(ChatColor.GRAY + "  - " + s);
			}
			lore.add(" ");
			String actionBar = announcement.isActionBar() ? announcement.getActionBarMessage() : null;
			String[] title = announcement.isTitle() ? announcement.getTitleMessage() : null;
			if (actionBar != null) {
				lore.add(ChatColor.DARK_AQUA + "Action Bar: ");
				lore.add(ChatColor.GRAY + "  - " + actionBar);
			}
			if (title != null) {
				lore.add(ChatColor.DARK_AQUA + "Title: ");
				for (String t : title) {
					lore.add(ChatColor.GRAY + "  - " + t);
				}
			}
			builder.lore(lore.toArray(new String[lore.size()]));
			buttons[i] = new Button(builder.build(), (player) -> {
				new MessageSettingMenu(announcement).open(player);
			});
		}
		return buttons;
	}
}
