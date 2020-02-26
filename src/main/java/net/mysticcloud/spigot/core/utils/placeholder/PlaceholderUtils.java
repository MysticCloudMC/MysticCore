package net.mysticcloud.spigot.core.utils.placeholder;

import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.Holiday;
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
		if(!CoreUtils.getHoliday().equals(Holiday.NONE)) {
			switch(CoreUtils.getHoliday()) {
			case CHRISTMAS:
				string = string.replaceAll("%holidayline%", "&c&lMerry &a&lChristmas&f");
				break;
			case ST_PATRICKS:
				string = string.replaceAll("%holidayline%", "&fHappy &a&lSt. Patrick's day&f");
				break;
			case AVACADO_DAY:
				string = string.replaceAll("%holidayline%", "&2&lAVACADO DAY!");
				break;
			case TEST:
				string = string.replaceAll("%holidayline%", "&d&lH&f&lA&b&lP&a&lP&e&lY &cB&4&lI&6&lR&e&lT&a&lH&2&lD&b&lA&3&lY&1&l!");
				break;
			case HALLOWEEN:
				string = string.replaceAll("%holidayline%", "&6&lHappy &5&lHolloween&f");
				break;
			}
		} else {
			string = string.replaceAll("%holiday%", "");
			string = string.replaceAll("%holidayline%", "");
		}
		
		return string;
	}

}
