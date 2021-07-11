package net.mysticcloud.spigot.core.utils.portals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mysticcloud.spigot.core.utils.regions.Region;

public class PortalUtils {

	private static Map<String, Portal> portals = new HashMap<>();

	public static Portal createPortal(String name, Region region) {
		Portal portal = new Portal(name, region);
		portals.put(name, portal);
		return portal;
	}

	public static Portal getPortal(String name) {
		return portals.containsKey(name) ? portals.get(name) : null;
	}

	public static Collection<Portal> getPortals() {
		return portals.values();
	}

}
