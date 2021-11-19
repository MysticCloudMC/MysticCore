package net.mysticcloud.spigot.core.utils.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.admin.Holiday;

public class PlaceholderUtils {

	static Map<String, PlaceholderWorker> placeholders = new HashMap<>();

	public static void registerPlaceholders() {
		placeholders.put("%test%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getName();
			}
		});
	}

	public static void registerPlaceholder(String key, PlaceholderWorker worker) {
		placeholders.put("%" + key + "%", worker);
	}

	public static String replace(Player player, String string) {

		MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);

		for (Entry<String, PlaceholderWorker> e : placeholders.entrySet()) {
			if (string.contains(e.getKey())) {
				string = string.replaceAll(e.getKey(), e.getValue().run(player));
			}
		}

		if (string.contains("%lvl")) {
			if (mp.getLevel() <= 49) {
				string = string.replaceAll("%lvl", CoreUtils.colorize("&7[%level]"));
			}
			if (mp.getLevel() >= 50 && mp.getLevel() <= 150) {
				string = string.replaceAll("%lvl", CoreUtils.colorize("&a[%level]"));
			}
			if (mp.getLevel() >= 150) {
				string = string.replaceAll("%lvl", CoreUtils.colorize("&6[%level]"));
			}
		}
		string = string.replaceAll("%player", player.getName());
		string = string.replaceAll("%pl", player.getName());
		string = string.replaceAll("%world", player.getWorld().getName());
		string = string.replaceAll("%time", CoreUtils.getTime());
		string = string.replaceAll("%balance", "" + mp.getBalance());
		string = string.replaceAll("%bal", "" + mp.getBalance());
		string = string.replaceAll("%gems", "" + mp.getGems());
		string = string.replaceAll("%g", "" + mp.getGems());
		string = string.replaceAll("%level", "" + mp.getLevel());
		string = string.replaceAll("%rank", "%r%");
		string = string.replaceAll("%prefix", "%r%");
		string = string.replaceAll("%r%", CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%displayname", player.getDisplayName());
		string = string.replaceAll("%customname", player.getCustomName());
		string = string.replaceAll("%time", player.getWorld().getFullTime() + "");
		string = string.replaceAll("%fulltime", CoreUtils.getTime());
		string = string.replaceAll("%playertime", player.getPlayerTime() + "");
		string = string.replaceAll("%suffix", CoreUtils.colorize(CoreUtils.getPlayerSuffix(player)));
		string = string.replaceAll("%server", CoreUtils.getServerName());
		string = string.replaceAll("%online", Bukkit.getOnlinePlayers().size() + "");

		if (string.contains("%tag"))
			string = string.replaceAll("%tag",
					CoreUtils.getTag(player) + ChatColor.getLastColors(string.split("%tag")[0]));

		if (MysticAccountManager.getMysticPlayer(player).isNitro()) // if nitro

			string = string.replace("%nitro",
					CoreUtils.colorize("&d\u25C6") + ChatColor.getLastColors(string.split("%nitro")[0]));

		else
			string = string.replace("%nitro", "");

		string =

				emotify(string);

		if (!CoreUtils.getHoliday().equals(Holiday.NONE)) {
			string = string.replaceAll("%holiday", "&b" + CoreUtils.getHoliday().getName());
			string = string.replaceAll("%hdayline", CoreUtils.getHoliday().getScoreboardLine());
		} else {
			string = string.replaceAll("%holiday", "");
			string = string.replaceAll("%hdayline", "");
		}
		return string;
	}

	public static String replace(String player, String string) {
		if (string.contains("%lvl"))
			string = string.replaceAll("%lvl", "");

		string = string.replaceAll("%player", player);
		string = string.replaceAll("%pl", player);
		string = string.replaceAll("%world", "");
		string = string.replaceAll("%time", CoreUtils.getTime());
		string = string.replaceAll("%balance", "");
		string = string.replaceAll("%gems", "");
		string = string.replaceAll("%g", "");
		string = string.replaceAll("%level", "");
		string = string.replaceAll("%rank", "%r%");
		string = string.replaceAll("%prefix", "%r%");
		string = string.replaceAll("%r%", "");
		string = string.replaceAll("%displayname", "");
		string = string.replaceAll("%customname", "");
		string = string.replaceAll("%time", "");
		string = string.replaceAll("%realtime", CoreUtils.getTime());
		string = string.replaceAll("%playertime", "");
		string = string.replaceAll("%suffix", "");
		string = string.replaceAll("%server", CoreUtils.getServerName());

		if (string.contains("%tag"))
			string = string.replaceAll("%tag", "");
		if (string.contains("%nitro"))
			string = string.replace("%nitro", "");
		string = emotify(string);

		if (!CoreUtils.getHoliday().equals(Holiday.NONE)) {
			string = string.replaceAll("%holiday", "&b" + CoreUtils.getHoliday().getName());
			string = string.replaceAll("%hdayline", CoreUtils.getHoliday().getScoreboardLine());
		} else {
			string = string.replaceAll("%holiday", "");
			string = string.replaceAll("%hdayline", "");
		}
		return CoreUtils.colorize(string);
	}

	public static String emotify(String string) {
		String tag = string + "";
		while (tag.contains("%emoticon:")) {
			String icon = tag.split("moticon:")[1].split("%")[0];
			if (Emoticons.valueOf(icon.toUpperCase()) == null) {
				tag = tag.replaceAll("%emoticon:" + icon + "%", Emoticons.UNKNOWN.toString());
			} else {
				tag = tag.replaceAll("%emoticon:" + icon + "%", Emoticons.valueOf(icon.toUpperCase()).toString());
			}
		}
		return tag;
	}

	public static String markup(String string) {
		String tag = string + "";
		while (tag.contains("%bold:")) {
			String icon = tag.split("old:")[1].split("%")[0];
			tag = tag.replaceAll("%bold:" + icon + "%",
					ChatColor.BOLD + icon + ChatColor.getLastColors(tag.split("%bold")[0]));
		}
		while (tag.contains("%upper:")) {
			String icon = tag.split("pper:")[1].split("%")[0];
			tag = tag.replaceAll("%upper:" + icon + "%",
					icon.contains("%") ? replace("", icon).toUpperCase() : icon.toUpperCase());
		}
		return tag;
	}

}
