package net.mysticcloud.spigot.core.utils.placeholder;

import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.MysticPlayer;

public class PlaceholderUtils {
	
	public static String replace(Player player, String string) {
		MysticPlayer mp = CoreUtils.getMysticPlayer(player);
		string = string.replaceAll("%player%", player.getName());
		string = string.replaceAll("%world%", player.getWorld().getName());
		string = string.replaceAll("%time%", CoreUtils.getTime());
		string = string.replaceAll("%balance%", ""+mp.getBalance());
		string = string.replaceAll("%gems%", ""+mp.getGems());
		string = string.replaceAll("%level%", ""+mp.getLevel());
		
		return string;
	}

}
