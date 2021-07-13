package net.mysticcloud.spigot.core.utils.regions;

import java.util.HashMap;
import java.util.Map;

public class RegionUtils {

	private static Map<String, Region> regions = new HashMap<>();

	public static Region createRegion(String name, String world, double x1, double y1, double z1, double x2, double y2,
			double z2) {
		Region rg = new Region(name, world, x1, y1, z1, x2, y2, z2);
		regions.put(name, rg);
		// TODO Make sure to tell whoever is creating the region that they will override
		// if that name exists
		return regions.get(name);
	}

	public static Region getRegion(String name) {
		return regions.containsKey(name) ? regions.get(name) : null;
	}

}
