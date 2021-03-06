package net.mysticcloud.spigot.core.utils.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

public class HomeUtils {

	public static List<Warp> getHomes(UUID uid) {
		List<Warp> homes = new ArrayList<>();
		for (Warp home : WarpUtils.getWarps("home~" + uid.toString())) {
			homes.add(home);
		}

		return homes;
	}
	
	public static Warp getHome(UUID uid) {
		List<Warp> homes = getHomes(uid);
		Warp thome = homes.get(0);
		return thome;
	}

	public static Warp getHome(UUID uid, String name) {
		List<Warp> homes = getHomes(uid);
		Warp thome = homes.get(0);
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
