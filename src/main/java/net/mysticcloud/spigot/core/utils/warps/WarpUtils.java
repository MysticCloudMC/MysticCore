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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class WarpUtils {

	private static Map<String, List<Warp>> warps = new HashMap<>();
	static File warps_dir = new File(Main.getPlugin().getDataFolder() + "/warps/");
	
	static void addWarp(String type, Warp warp) {
		checkWarps(type);
		warps.get(type).add(warp);
	}

	public static void registerWarps() {
		
		if(!warps_dir.exists()){
			warps_dir.mkdir();
		}

		for (File file : warps_dir.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			String type = file.getName().replace(".yml", "");
			for (String idstr : config.getConfigurationSection("Warps").getKeys(false)) {
				Warp warp = new Warp(Integer.parseInt(idstr));
				for(String data : config.getConfigurationSection("Warps." + idstr).getKeys(false)) {
					if(data.equalsIgnoreCase("Location")){
						warp.location(CoreUtils.decryptLocation(config.getString("Warps." + idstr + "." + data)));
						continue;
					}
					if(data.equalsIgnoreCase("Name")){
						warp.name(config.getString("Warps." + idstr + "." + data));
						continue;
					}
					warp.metadata(data, config.getString("Warps." + idstr + "." + data));
					
				}
				addWarp(type,warp);
				Bukkit.getConsoleSender().sendMessage(CoreUtils.prefixes("warps") + "Registered Warp " + type + ":" + warp.name());
				
			}
		}

	}
	
	public static Set<String> getWarpTypes(){
		return warps.keySet();
	}
	
	public static List<Warp> getWarps(String type, String name) {
		checkWarps(type);
		List<Warp> rtrn = new ArrayList<>();
		for(Warp warp : warps.get(type)) {
			if(warp.name().equals(name))
				rtrn.add(warp);
		}
		return rtrn;
	}
	public static List<Warp> getAllWarps(){
		List<Warp> warps = new ArrayList<>();
		for(String type : getWarpTypes()) {
			for(Warp warp : WarpUtils.warps.get(type)) {
				warps.add(warp);
			}
		}
		return warps;
	}

	public static List<Warp> getWarps(String type) {
		checkWarps(type);
		return warps.get(type);
	}

	

	public static void removeWarp(String type, Warp warp) {
		checkWarps(type);
		File file = new File(warps_dir.getAbsoluteFile() + "/" + type + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if(config.isSet("Warps." + warp.id())) {
			config.set("Warps." + warp.id(), null);
			try {
				config.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (warps.get(type).contains(warp))
			warps.get(type).remove(warp);
	}
	
	public static void removeWarp(String type, int id) {
		checkWarps(type);
		Warp tmp = null;
		for(Warp warp : warps.get(type)) {
			if(warp.id() == id) {
				tmp = warp;
				break;
			}
		}
		removeWarp(type,tmp);
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
			config.set("Warps." + warp.id() + ".Location", CoreUtils.encryptLocation(warp.location()));
			config.set("Warps." + warp.id() + ".Name", warp.name());
			for(Entry<String, String> entry : warp.metadata().entrySet()) {
				config.set("Warps." + warp.id() + "." + entry.getKey(),entry.getValue());
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
		if (!warps.containsKey(type)) 
			warps.put(type, new ArrayList<Warp>());
		
	}

}
