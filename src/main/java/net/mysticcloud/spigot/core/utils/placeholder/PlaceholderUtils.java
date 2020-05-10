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
		string = string.replaceAll("%balance", ""+mp.getBalance());
		string = string.replaceAll("%gems", ""+mp.getGems());
		string = string.replaceAll("%g", ""+mp.getGems());
		string = string.replaceAll("%level", ""+mp.getLevel());
		string = string.replaceAll("%rank", CoreUtils.colorize(""+CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%prefix", CoreUtils.colorize(""+CoreUtils.getPlayerPrefix(player)));
		string = string.replaceAll("%r", CoreUtils.colorize(""+CoreUtils.getPlayerPrefix(player)));
		if(!CoreUtils.getHoliday().equals(Holiday.NONE)) {
			switch(CoreUtils.getHoliday()) {
			case CHRISTMAS:
				string = string.replaceAll("%hdayline", "&c&lMerry &a&lChristmas&f");
				break;
			case TEST:
				string = string.replaceAll("%hdayline", "&fHappy &a&lSt. Patrick's day&f");
				break;
			case AVACADO_DAY:
				string = string.replaceAll("%hdayline", "&2&lAVACADO DAY!");
				break;
			case BIRTHDAY:
				string = string.replaceAll("%hdayline", "&bHappy Birthday!");
				break;
			case HALLOWEEN:
				string = string.replaceAll("%hdayline", "&6&lHappy &5&lHolloween&f");
			case CINCO_DE_MAYO:
				string = string.replaceAll("%hdayline", "&6&lHappy &5&lHolloween&f");
				break;
			case MAY_THE_FORTH:
				string = string.replaceAll("%hdayline", "&eMay The Forth Be With You");
				break;
			default:
				break;
			}
			string = string.replaceAll("%holiday", "&b" + CoreUtils.getHoliday().getName());
			
		} else {
			string = string.replaceAll("%holiday", "");
			string = string.replaceAll("%hdayline", "");
		}
		
		return string;
	}

}
