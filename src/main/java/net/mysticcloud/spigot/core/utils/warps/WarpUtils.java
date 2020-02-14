package net.mysticcloud.spigot.core.utils.warps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
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
			for (String name : config.getConfigurationSection("Warps").getKeys(false)) {
				Warp warp = new Warp(name);
				for(String data : config.getConfigurationSection("Warps." + name).getKeys(false)) {
					if(data.equalsIgnoreCase("Location"))
						warp.location(CoreUtils.decryptLocation(config.getString("Warps." + name + "." + data)));
					else 
						warp.metadata(data, config.get("Warps." + name + "." + data));
					
				}
				
				Bukkit.getConsoleSender().sendMessage(CoreUtils.prefixes("warps") + "Registered Warp " + warp.name());
			}
		}

	}
	
	public static Set<String> getWarpTypes(){
		return warps.keySet();
	}
	
	public static Warp getWarp(String type, String name) {
		checkWarps(type);
		for(Warp warp : warps.get(type)) {
			if(warp.name().equals(name))
				return warp;
		}
		return null;
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
		for(String type : warps.keySet()){
			save(type);
		}
	}

	public static void save(String type) {
		File file = new File(warps_dir.getAbsoluteFile() + "/" + type + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for(Warp warp : warps.get(type)) {
			config.set("Warps." + warp.name() + ".Location", CoreUtils.encryptLocation(warp.location()));
			for(Entry<String, Object> entry : warp.metadata().entrySet()) {
				config.set("Warps." + warp.name() + "." + entry.getKey(),entry.getValue());
			}
		}
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void checkWarps(String type) {
		if (!warps.containsKey(type)) {

		}
		warps.put(type, new ArrayList<Warp>());
	}

}
