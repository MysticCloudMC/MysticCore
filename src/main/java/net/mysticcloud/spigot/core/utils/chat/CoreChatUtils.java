package net.mysticcloud.spigot.core.utils.chat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;

public class CoreChatUtils {

	private static Map<UUID, String> currentChannel = new HashMap<>();
	private static Map<String, Channel> channels = new HashMap<>();

	private static String playerformat = "%player: %message";
	private static String staffformat = "&c[StaffChat]&f%player: %message";
	private static String channelformat = "&a[%channel]&f%player: %message";

	private static String[] puncuation = new String[] { ".", ",", ";", ":", "!", "?", "\"", "\'", ")", "*" };

	private static Map<CharSequence[], CharSequence> replacements = new LinkedHashMap<>();

	public static void start() {
		addChannel("default", "", false);
		addChannel("staff", "&7[&3&lStaff&7] ", true);
		addChannel("zachs", "&7[&c&lzACHS&7] ", true);
		registerChannels();

		if (!CoreUtils.getVariable("playerchat.format").equalsIgnoreCase("ERROR"))
			playerformat = CoreUtils.getVariable("playerchat.format");
		if (!CoreUtils.getVariable("staffchat.format").equalsIgnoreCase("ERROR"))
			staffformat = CoreUtils.getVariable("staffchat.format");
		if (!CoreUtils.getVariable("channelchat.format").equalsIgnoreCase("ERROR"))
			channelformat = CoreUtils.getVariable("channelchat.format");

		registerReplacements();
	}

	public static Map<CharSequence[], CharSequence> getReplacements() {
		return replacements;
	}

	public static void registerReplacements() {

		replacements.put(new CharSequence[] { "shit", "sh!t" }, "s***");
		replacements.put(new CharSequence[] { "fuck", "fuk", "fu(k" }, "frick");
		replacements.put(new CharSequence[] { "cunt" }, "****");
		replacements.put(new CharSequence[] { "bitch", "b!tch", "bit(h", "b!t(h", "bich" }, "dog");
		replacements.put(new CharSequence[] { "fag", "faggot" }, "*****");
		replacements.put(new CharSequence[] { "nigger", "nigga", "n!gger", "n!gga" }, "****");
		replacements.put(new CharSequence[] { " ass " }, "**");
		replacements.put(new CharSequence[] { "pussy" }, "*****");
		replacements.put(new CharSequence[] { " cum " }, "***");

	}

	public static String getPlayerFormat() {
		return playerformat;
	}

	public static String getStaffFormat() {
		return staffformat;
	}

	public static String getChannelFormat(String channel) {
		return channelformat.replaceAll("%channel",
				channel.substring(0, 1).toUpperCase() + channel.substring(1, channel.length()).toLowerCase());
	}

	public static Channel addChannel(String name, String tag, boolean global) {
		return channels.put(name, createChannel(name, tag, global));
	}

	public static Channel createChannel(String name, String tag, boolean global) {
		return new Channel() {

			@Override
			public boolean isGlobal() {
				return global;
			}

			@Override
			public String getTag() {
				return tag;
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

	public static void registerChannels() {
		try {

			FileConfiguration conf = Main.getPlugin().getConfig();
			for (String idstr : conf.getConfigurationSection("Channels").getKeys(false)) {
				addChannel(idstr, conf.getString("Channels." + idstr + ".tag", "&a&l[%channel]&7 "),
						conf.getBoolean("Channels." + idstr + ".global", false));

			}

		} catch (NullPointerException ex) {

		}
	}

	public static String getChannel(UUID uid) {
		return currentChannel.containsKey(uid) ? currentChannel.get(uid) : "default";
	}

	public static void sendBroadcast(String message) {
		CoreUtils.sendPluginMessage((Player) Bukkit.getOnlinePlayers().toArray()[0], "mystic:mystic",
				"MysticChat-default", message);
	}

	public static void sendChannelChat(Player player, String channel, String message) {

		String format;
		switch (channel) {
		case "staff":
			format = getStaffFormat();
			break;
		case "default":
			format = getPlayerFormat();
			break;
		default:
			format = getChannelFormat(channel);
			break;
		}
		format = CoreUtils.colorize(getChannel(channel).getTag() + "&7") + format;
		if (getChannel(channel).isGlobal())
			CoreUtils.sendPluginMessage(player, "mystic:mystic", "MysticChat-" + channel,
					replaceholders(player, format, message));
		else {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (s.hasPermission("mysticcloud.chat." + channel.toLowerCase()))
					s.sendMessage((replaceholders(player, format, message)));
			}
		}
	}

	public static void sendChannelChat(String channel, String message) {

		String format;
		switch (channel) {
		case "staff":
			format = getStaffFormat();
			break;
		case "default":
			format = getPlayerFormat();
			break;
		default:
			format = getChannelFormat(channel);
			break;
		}
		format = CoreUtils.colorize(getChannel(channel).getTag() + "&7") + format;
		if (getChannel(channel).isGlobal())
			CoreUtils.sendPluginMessage((Player) Bukkit.getOnlinePlayers().toArray()[0], "mystic:mystic",
					"MysticChat-" + channel, replaceholders("&6CONSOLE", format, message));
		else {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (s.hasPermission("mysticcloud.chat." + channel.toLowerCase()))
					s.sendMessage((replaceholders("&6CONSOLE", format, message)));
			}
		}
	}

	public static String censor(String message) {
//		String s = "";
//		String original = message;
//		int i = message.length();
//		for (int l = 0; l != i; l++) {
//			try {
//
//				if (message.substring(l, l + 1).equalsIgnoreCase(message.substring(l - 2, l - 1))
//						&& message.substring(l, l + 1).equalsIgnoreCase(message.substring(l - 1, l)))
//					continue;
//				s = s + (l == 0 ? message.substring(l, l + 1).toUpperCase() : message.substring(l, l + 1));
//				// Curses "try"
//				try {
//					for (CharSequence[] curses : replacements.keySet()) {
//						for (CharSequence curse : curses) {
//							if (s.length() < curse.length())
//								continue;
//
//							if (s.substring(s.length() - (curse.length()), s.length()).toLowerCase().contains(curse)) {
//								s = s.substring(0, s.length() - (curse.length()));
//								s = s + replacements.get(curses);
//								i = i + ((curse.length() - replacements.get(curses).length())); // Temp code. Should set
//																								// i to i +
//								// (difference in length in curse
//								// and
//								// replacement) but the try catch
//								// under the first loop catches this
//								// mistake for
//								// me.
//								original = original.replace(curse, replacements.get(curses));
//							}
//						}
//					}
//				} catch (IndexOutOfBoundsException ex) {
//					continue;
//				}
//
//			} catch (IndexOutOfBoundsException ex) {
//				if ((l + 1) < message.length())
//					s = s + (l == 0 ? message.substring(l, l + 1).toUpperCase() : message.substring(l, l + 1));
//			}
//
//		}
//		boolean tmp = false;
//		for (String p : puncuation)
//			if (s.endsWith(p)) {
//				tmp = true;
//				break;
//			}
//
//		if (!tmp)
//			s = s + ".";
//
//		return s;
		return message;
	}

	public static String replaceholders(Player player, String format, String message) {
		format = CoreUtils.colorize(format);
		message = censor(message);

		if (format.contains("%message"))
			format = format.replace("%message",
					player.hasPermission("mysticcloud.chat.color")
							? CoreUtils.colorize(PlaceholderUtils.emotify(message))
							: message);
		format = PlaceholderUtils.replace(player, format);

		return format;
	}

	public static String replaceholders(String player, String format, String message) {
		format = CoreUtils.colorize(format);
		message = censor(message);

		if (format.contains("%message"))
			format = format.replace("%message", CoreUtils.colorize(PlaceholderUtils.emotify(message)));
		format = PlaceholderUtils.replace(player, format);

		return format;
	}

	public static Channel getChannel(String name) {
		return channels.containsKey(name) ? channels.get(name) : channels.get("default");
	}

	public static void sendMessage(Player player, String message) {
		sendChannelChat(player, getChannel(player.getUniqueId()), message);
	}

	public static void setChannel(Player player, String channel) {
		if (channels.containsKey(channel.toLowerCase())) {
			if (player.hasPermission("mysticcloud.channel." + channel.toLowerCase())) {
				currentChannel.put(player.getUniqueId(), channel);
				player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("chat") + "You're now chatting in the &7"
						+ channel.toLowerCase() + "&f channel."));
			} else {
				player.sendMessage(
						CoreUtils.prefixes("chat") + "Sorry, you don't have permission to join that channel.");
			}
		} else {
			player.sendMessage(CoreUtils.prefixes("chat") + "Sorry that channel doesn't exist.");
		}
	}

}
