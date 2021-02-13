package net.mysticcloud.spigot.core.utils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.warps.Warp;

public class CustomTag {

	static Map<String, String> tags = new HashMap<>();

	static File tags_dir = new File(Main.getPlugin().getDataFolder() + "/tags/");

//	AVACADO("&2[AVACADO]"),
//	BEAST("&6[%emoticon:SWORD%BEAST%emoticon:SWORD%] "),
//	STAR("&e[%emoticon:STAR_7%STAR%emoticon:STAR_7%] "),
//	SUPERNOVA("&5[%emoticon:SUN%SUPER NOVA%emoticon:SUN%] "),
//	SPARLE("&d[%emoticon:SPARKLE_SMALL%" + "SPARKLE" + "&d%emoticon:SPARKLE_SMALL%] "),
//	PSYCHO("&d[%emoticon:HEART_1%PSYCHO%emoticon:HEART_1%] "),
//	
//	NONE("you shouldn't have this.");

//	String tag;

//	CustomTag(String tag) {
//		this.tag = CoreUtils.colorize(tag);
//	}

	public void start() {

	}

	public static void reloadTags() {
		tags.clear();
		registerTags();
		DebugUtils.debug("Tags reloaded.");
	}

	public static void registerTags() {

		if (!tags_dir.exists()) {
			tags_dir.mkdir();
		}

		for (File file : tags_dir.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
//			try {
			for (String key : config.getConfigurationSection("Tags").getKeys(false)) {
				if (config.isSet("Tags." + key + ".value")) {
					tags.put(key.toUpperCase(), CoreUtils.colorize(config.getString("Tags." + key + ".value")));
				}
			}
//			} catch(NullPointerException ex) {
			// skip
//			}
		}

	}

	public static String getTag(String key) {
		return tags.containsKey(key.toUpperCase()) ? tags.get(key.toUpperCase()) : "[NT] ";
	}

	public static String[] values() {
		String[] r = new String[tags.values().size()];
		r = tags.values().toArray(r);
		return r;
	}

	public static String[] keys() {
		String[] r = new String[tags.keySet().size()];
		r = tags.keySet().toArray(r);
		return r;
	}

}
