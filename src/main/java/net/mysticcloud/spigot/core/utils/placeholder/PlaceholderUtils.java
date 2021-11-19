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
		placeholders.put("%lvl%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
				if (mp.getLevel() <= 49) {
					return CoreUtils.colorize("&7[" + mp.getLevel() + "]");
				}
				if (mp.getLevel() >= 50 && mp.getLevel() <= 150) {
					return CoreUtils.colorize("&a[" + mp.getLevel() + "]");
				}
				if (mp.getLevel() >= 150) {
					return CoreUtils.colorize("&6[" + mp.getLevel() + "]");
				}
				return CoreUtils.colorize("&c[-1]");
			}
		});

		placeholders.put("%level%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
				return "" + mp.getLevel();
			}
		});

		PlaceholderWorker name = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getName();
			}
		};

		PlaceholderWorker balance = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
				return "" + mp.getBalance();
			}
		};
		PlaceholderWorker gems = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
				return "" + mp.getGems();
			}
		};
		PlaceholderWorker rank = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player));
			}
		};
		PlaceholderWorker dname = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getDisplayName();
			}
		};
		PlaceholderWorker cname = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getCustomName();
			}
		};

		placeholders.put("%world%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getWorld().getName();
			}
		});
		placeholders.put("%time%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return CoreUtils.getTime();
			}
		});
		placeholders.put("%playertime%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return "" + player.getPlayerTime();
			}

		});
		placeholders.put("%suffix%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return CoreUtils.colorize(CoreUtils.getPlayerSuffix(player));
			}

		});
		placeholders.put("%server%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return CoreUtils.getServerName();
			}

		});
		placeholders.put("%online%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return Bukkit.getOnlinePlayers().size() + "";
			}

		});

		placeholders.put("%tag%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return CoreUtils.getTag(player)
						+ ChatColor.getLastColors(CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
			}
		});

		placeholders.put("%nitro%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
				if (mp.isNitro())
					return CoreUtils.colorize("&d" + Emoticons.NITRO)
							+ ChatColor.getLastColors(CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
				return "";
			}
		});

		placeholders.put("%displayname%", dname);
		placeholders.put("%dname%", dname);

		placeholders.put("%customname%", cname);
		placeholders.put("%cname%", cname);

		placeholders.put("%player%", name);
		placeholders.put("%pl%", name);

		placeholders.put("%bal%", balance);
		placeholders.put("%balance%", balance);

		placeholders.put("%gems%", gems);
		placeholders.put("%g%", gems);

		placeholders.put("%r%", rank);
		placeholders.put("%rank%", rank);
		placeholders.put("%prefix%", rank);

	}

	public static void registerPlaceholder(String key, PlaceholderWorker worker) {
		placeholders.put("%" + key + "%", worker);
	}

	public static String replace(Player player, String string) {

		for (Entry<String, PlaceholderWorker> e : placeholders.entrySet()) {
			if (string.contains(e.getKey())) {
				string = string.replaceAll(e.getKey(), e.getValue().run(player));
			}
		}

		string = emotify(string);

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
