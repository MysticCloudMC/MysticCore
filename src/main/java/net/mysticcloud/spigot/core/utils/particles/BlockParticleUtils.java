package net.mysticcloud.spigot.core.utils.particles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.formats.RandomFormat;

public class BlockParticleUtils {

	private static Map<String, Location> blocks = new HashMap<>();

//
	public static void start() {
		if (CoreUtils.getPlugin().getConfig().isSet("BlockParticles"))
			for (String id : CoreUtils.getPlugin().getConfig().getConfigurationSection("BlockParticles").getKeys(false)) {
				ParticleFormat format = ParticleFormatEnum
						.valueOf(CoreUtils.getPlugin().getConfig().getString("BlockParticles." + id + ".format"))
						.formatter();
				JSONObject options = new JSONObject(
						CoreUtils.getPlugin().getConfig().getString("BlockParticles." + id + ".json", "{}"));
				format.particle(CoreUtils.getPlugin().getConfig().isSet("BlockParticles." + id + ".particle")
						? Particle.valueOf(CoreUtils.getPlugin().getConfig().getString("BlockParticles." + id + ".particle"))
						: Particle.FLAME);
				format.loadOptions(options);
				Location loc = CoreUtils
						.decryptLocation(CoreUtils.getPlugin().getConfig().getString("BlockParticles." + id + ".location"));
				CoreUtils.blockparticles.put(loc, format);
				blocks.put(id, loc);
			}
	}

	public static void end() {
		for (Entry<String, Location> e : blocks.entrySet()) {
			ParticleFormat format = getBlockParticleFormat(e.getKey());
			if (format == null) {
				continue;
			}
			CoreUtils.getPlugin().getConfig().set("BlockParticles." + e.getKey() + ".format",
					ParticleFormatEnum.enumName(format).name());
			CoreUtils.getPlugin().getConfig().set("BlockParticles." + e.getKey() + ".particle", format.particle().name());
			CoreUtils.getPlugin().getConfig().set("BlockParticles." + e.getKey() + ".json", format.getOptions().toString());
			CoreUtils.getPlugin().getConfig().set("BlockParticles." + e.getKey() + ".location",
					CoreUtils.encryptLocation(e.getValue()));

		}
		CoreUtils.getPlugin().saveConfig();
	}

	public static void createBlockParticles(String id, Location loc) {
		createBlockParticles(id, loc, new RandomFormat());
	}

	public static void createBlockParticles(String id, Location loc, ParticleFormat format) {
		CoreUtils.blockparticles.put(loc, format);
		blocks.put(id, loc);
		end();
	}

	public static void updateFormat(String id, ParticleFormat format) {
		if (!blocks.containsKey(id))
			return;
		CoreUtils.blockparticles.put(blocks.get(id), format);
		end();
	}

	public static ParticleFormat getBlockParticleFormat(String id) {
		return blocks.containsKey(id)
				? (CoreUtils.blockparticles.containsKey(blocks.get(id)) ? CoreUtils.blockparticles.get(blocks.get(id))
						: null)
				: null;
	}

	public static void updateOptions(String id, String... args) {
		ParticleFormat format = getBlockParticleFormat(id);
		for (String a : args) {
			if (a.contains("=")) {
				String key = a.split("=")[0];
				String value = a.split("=")[1];
				format.setOption(key, value);
			}
		}
		end();
	}

	public static void deleteBlockParticle(String id) {
		CoreUtils.blockparticles__remove.add(blocks.get(id));
		blocks.remove(id);
		CoreUtils.getPlugin().getConfig().set("BlockParticles." + id, null);
		CoreUtils.getPlugin().saveConfig();
	}

	public static Map<String, Location> getBlocks() {
		return blocks;
	}

//	public static Portal createPortal(String name, Region region) {
//		Portal portal = new Portal(name, region);
//		portals.put(name, portal);
//		return portal;
//	}
//
//	public static Portal getPortal(String name) {
//		return portals.containsKey(name) ? portals.get(name) : null;
//	}
//
//	public static Collection<Portal> getPortals() {
//		return portals.values();
//	}
//
//	public static boolean isEditing(Player player) {
//		return editors.containsKey(player.getUniqueId());
//	}
//
//	public static void enterPortalEditor(Player player) {
//		editors.put(player.getUniqueId(), new JSONObject("{}"));
//	}
//
//	public static void setEditorData(Player player, String key, Object value) {
//		editors.get(player.getUniqueId()).put(key, value);
//	}
//
//	public static Portal playerCreatePortal(Player player, String name) {
//		try {
//
//			JSONObject json = editors.get(player.getUniqueId());
//			double x1 = json.getDouble("x1") > json.getDouble("x2") ? json.getDouble("x2") : json.getDouble("x1");
//			double x2 = json.getDouble("x1") > json.getDouble("x2") ? json.getDouble("x1") : json.getDouble("x2");
//
//			double y1 = json.getDouble("y1") > json.getDouble("y2") ? json.getDouble("y2") : json.getDouble("y1");
//			double y2 = json.getDouble("y1") > json.getDouble("y2") ? json.getDouble("y1") : json.getDouble("y2");
//
//			double z1 = json.getDouble("z1") > json.getDouble("z2") ? json.getDouble("z2") : json.getDouble("z1");
//			double z2 = json.getDouble("z1") > json.getDouble("z2") ? json.getDouble("z1") : json.getDouble("z2");
//
//			String world = json.getString("world");
//
//			y1 = y1 - 0.5;
//
//			x2 = x2 + 1;
//			y2 = y2 + 1;
//			z2 = z2 + 1;
//
//			Region rg = RegionUtils.createRegion("portalregion-" + name, world, x1, y1, z1, x2, y2, z2);
//
//			Portal portal = createPortal(name, rg);
//
//			editors.remove(player.getUniqueId());
//
//			player.sendMessage(CoreUtils.colorize(
//					CoreUtils.prefixes("portals") + "You've successfully created the portal &f" + name + "&7."));
//
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".x1", x1);
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".y1", y1);
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".z1", z1);
//
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".x2", x2);
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".y2", y2);
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".z2", z2);
//
//			CoreUtils.getPlugin().getConfig().set("Portal." + name + ".world", world);
//
//			CoreUtils.getPlugin().saveConfig();
//
//			return portal;
//
//		} catch (Exception ex) {
//			player.sendMessage(CoreUtils.prefixes("portals")
//					+ "There was an error creating that portal. Make sure you set both corners");
//			editors.remove(player.getUniqueId());
//			return null;
//		}
//	}
//
//	public static JSONObject getEditingInfo(UUID uid) {
//		return editors.get(uid);
//	}

}
