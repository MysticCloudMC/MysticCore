package net.mysticcloud.spigot.core.utils.placeholder;

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
		string = string.replaceAll("%rank", CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%prefix", CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%r", CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player)));
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
