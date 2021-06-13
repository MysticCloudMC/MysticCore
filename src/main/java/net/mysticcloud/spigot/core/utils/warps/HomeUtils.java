package net.mysticcloud.spigot.core.utils.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class HomeUtils {

	public static Warp addHome(Player player, String name, Location location) {
		int size = HomeUtils.getHomes(player.getUniqueId()).size();
		if (player.hasPermission("mysticcloud.maxhomes." + (size + 1))) {
			name = !name.equals("") ? name : (HomeUtils.getHomes(player.getUniqueId()).size() + 1) + "";
			for (Warp home : getHomes(player.getUniqueId())) {
				if (home.name().equals(name)) {
					return addHome(player, name + "-1", location);
				}
			}
			WarpBuilder wb = new WarpBuilder();
			Warp warp = wb.createWarp().setType("home~" + player.getUniqueId().toString()).setName(name)
					.setLocation(location).getWarp();
			if (warp != null) {
				player.sendMessage(CoreUtils.prefixes("homes") + CoreUtils.colorize("Home '&7" + name + "&f' set!"));
			} else
				player.sendMessage(CoreUtils.prefixes("homes") + "There was an error setting you home.");

			return warp;
		}
		player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("account") + "You've reached your max homes"));

		return null;

	}

	public static List<Warp> getHomes(UUID uid) {
		List<Warp> homes = new ArrayList<>();
		for (Warp home : WarpUtils.getWarps("home~" + uid.toString())) {
			homes.add(home);
		}

		return homes;
	}

	public static Warp getHome(UUID uid) {
		List<Warp> homes = getHomes(uid);
		return homes.size() >= 1 ? homes.get(0) : null;
	}

	public static Warp getHome(UUID uid, String name) {
		List<Warp> homes = getHomes(uid);
		Warp thome = null;
		if (homes.size() > 1) {
			for (Warp home : homes) {
				if (home.name().equalsIgnoreCase(name)) {
					return home;
				}
			}
		}
		return thome;
	}

}
