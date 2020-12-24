package net.mysticcloud.spigot.core.utils.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.Holiday;
import net.mysticcloud.spigot.core.utils.MysticPlayer;

public class PlaceholderUtils {

	public static String replace(Player player, String string) {
		MysticPlayer mp = CoreUtils.getMysticPlayer(player);
		string = string.replaceAll("%player", player.getName());
		string = string.replaceAll("%pl", player.getName());
		string = string.replaceAll("%world", player.getWorld().getName());
		string = string.replaceAll("%time", CoreUtils.getTime());
		string = string.replaceAll("%balance", "" + mp.getBalance());
		string = string.replaceAll("%gems", "" + mp.getGems());
		string = string.replaceAll("%g", "" + mp.getGems());
		string = string.replaceAll("%level", "" + mp.getLevel());
		string = string.replaceAll("%rank", "%r");
		string = string.replaceAll("%prefix", "%r");
		string = string.replaceAll("%r", CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%displayname", player.getDisplayName());
		string = string.replaceAll("%customname", player.getCustomName());
		string = string.replaceAll("%time", player.getWorld().getFullTime() + "");
		string = string.replaceAll("%realtime", CoreUtils.getTime());
		string = string.replaceAll("%playertime", player.getPlayerTime() + "");
		string = string.replaceAll("%suffix", CoreUtils.colorize(CoreUtils.getPlayerSuffix(player)));
		string = string.replaceAll("%server", Bukkit.getName());
		string = string.replaceAll("%tag", CoreUtils.getTag(player));
		if (CoreUtils.getMysticPlayer(player).isNitro()) // if nitro

			string = string.replace("%nitro",
					CoreUtils.colorize("&d\u25C6") + ChatColor.getLastColors(string.split("%nitro")[0]));

		else
			string = string.replace("%nitro", "");

		if (!CoreUtils.getHoliday().equals(Holiday.NONE)) {
			string = string.replaceAll("%holiday", "&b" + CoreUtils.getHoliday().getName());
			string = string.replaceAll("%hdayline", CoreUtils.getHoliday().getScoreboardLine());
		} else {
			string = string.replaceAll("%holiday", "");
			string = string.replaceAll("%hdayline", "");
		}
		return string;
	}

}
