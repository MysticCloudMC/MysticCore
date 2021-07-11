package net.mysticcloud.spigot.core.utils.portals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.regions.Region;
import net.mysticcloud.spigot.core.utils.regions.RegionUtils;

public class PortalUtils {

	private static Map<String, Portal> portals = new HashMap<>();
	private static Map<UUID, JSONObject> editors = new HashMap<>();

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

	public static boolean isEditing(Player player) {
		return editors.containsKey(player.getUniqueId());
	}

	public static void enterPortalEditor(Player player) {
		editors.put(player.getUniqueId(), new JSONObject("{}"));
	}

	public static void setEditorData(Player player, String key, Object value) {
		editors.get(player.getUniqueId()).put(key, value);
	}

	public static Portal playerCreatePortal(Player player, String name) {
		try {

			JSONObject json = editors.get(player.getUniqueId());
			double x1 = json.getDouble("x1") > json.getDouble("x2") ? json.getDouble("x2") : json.getDouble("x1");
			double x2 = json.getDouble("x1") > json.getDouble("x2") ? json.getDouble("x1") : json.getDouble("x2");

			double y1 = json.getDouble("y1") > json.getDouble("y2") ? json.getDouble("y2") : json.getDouble("y1");
			double y2 = json.getDouble("y1") > json.getDouble("y2") ? json.getDouble("y1") : json.getDouble("y2");

			double z1 = json.getDouble("z1") > json.getDouble("z2") ? json.getDouble("z2") : json.getDouble("z1");
			double z2 = json.getDouble("z1") > json.getDouble("z2") ? json.getDouble("z1") : json.getDouble("z2");

			String world = json.getString("world");

			Region rg = RegionUtils.createRegion("portalregion-" + name, world, x1 - 0.5, y1 - 0.5, z1 - 0.5, x2 + 0.5,
					y2 + 0.5, z2 + 0.5);

			Portal portal = createPortal(name, rg);

			editors.remove(player.getUniqueId());

			player.sendMessage(
					CoreUtils.prefixes("portals") + "You've successfully created the portal &f" + name + "&7.");

			return portal;

		} catch (Exception ex) {
			player.sendMessage(CoreUtils.prefixes("portals")
					+ "There was an error creating that portal. Make sure you set both corners");
			editors.remove(player.getUniqueId());
			return null;
		}
	}

}
