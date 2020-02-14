package net.mysticcloud.spigot.core.utils.warps;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class WarpUtils {

	private static Map<String, List<Warp>> warps = new HashMap<>();
	static File warps_dir = new File(Main.getPlugin().getDataFolder() + "/warps/");

	public static void registerWarps() {
		
		if(!warps_dir.exists()){
			warps_dir.mkdir();
		}

		for (File file : warps_dir.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			for (String test : config.getConfigurationSection("Warps").getKeys(false)) {
				CoreUtils.debug(test);
			}
		}

	}

	public static List<Warp> getWarps(String type) {
		checkWarps(type);
		return warps.get(type);
	}

	public static void addWarp(String type, Warp warp) {
		checkWarps(type);
		warps.get(type).add(warp);
	}

	public static void removeWarp(String type, Warp warp) {
		checkWarps(type);
		if (warps.get(type).contains(warp))
			warps.get(type).remove(warp);
	}

	public static void removeWarp(String type, Location location) {
		checkWarps(type);
		Warp tmp = null;
		for (Warp warp : warps.get(type)) {
			if (warp.location().equals(location)) {
				tmp = warp;
				break;
			}
		}
		if (tmp != null)
			removeWarp(type, tmp);
		tmp = null;
	}

	public static void removeWarp(String type, String name) {
		checkWarps(type);
		Warp tmp = null;
		for (Warp warp : warps.get(type)) {
			if (warp.name().equals(name)) {
				tmp = warp;
				break;
			}
		}
		if (tmp != null)
			removeWarp(type, tmp);
		tmp = null;
	}

	public static void save() {
		for (Entry<String, List<Warp>> entry : warps.entrySet()) {
		}

	}

	private static void checkWarps(String type) {
		if (!warps.containsKey(type)) {

		}
		warps.put(type, new ArrayList<Warp>());
	}

}
