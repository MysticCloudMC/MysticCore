package net.mysticcloud.spigot.core.utils.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class HomeUtils {

	public static Warp addHome(UUID uid, String name, Location location) {
		name = !name.equals("") ? name : (HomeUtils.getHomes(uid).size() + 1) + "";
		WarpBuilder wb = new WarpBuilder();
		Warp warp = wb.createWarp().setType("home~" + uid.toString()).setName(name).setLocation(location).getWarp();
		if (warp != null) {
			if (Bukkit.getPlayer(uid) != null)
				Bukkit.getPlayer(uid)
						.sendMessage(CoreUtils.prefixes("homes") + CoreUtils.colorize("Home '&7" + name + "&f' set!"));
		} else if (Bukkit.getPlayer(uid) != null)
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes("homes") + "There was an error setting you home.");

		return warp;

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

	public static List<Warp> getHome(UUID uid, String name) {
		List<Warp> homes = getHomes(uid);
		List<Warp> thome = new ArrayList<>();
		if (homes.size() > 1) {
			for (Warp home : homes) {
				if (home.name().equalsIgnoreCase(name)) {
					thome.add(home);
				}
			}
		}
		return thome;
	}

}
