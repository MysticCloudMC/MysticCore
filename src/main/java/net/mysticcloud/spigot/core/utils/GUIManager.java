package net.mysticcloud.spigot.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;
import net.mysticcloud.spigot.core.utils.accounts.PlayerSettings;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.InventoryCreator;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;

public class GUIManager {

	private static Map<UUID, String> invTracker = new HashMap<>();
	private static Inventory waitingInv = null;
	private static boolean init = false;

	public static boolean init() {
		if (!init) {
			InventoryCreator inv = new InventoryCreator(CoreUtils.colorize("&7Waiting..."), null, 9);

			inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "Waiting...", 'X');

			inv.setConfiguration(new char[] { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
			waitingInv = inv.getInventory();
			init = true;
		}
		return init;

	}

	public static void openInventory(Player player, Inventory inventory, String title) {

		player.openInventory(inventory);
		invTracker.put(player.getUniqueId(), title);
	}

	public static String getOpenInventory(Player player) {
		return invTracker.containsKey(player.getUniqueId()) ? invTracker.get(player.getUniqueId()) : "none";
	}

	public static void switchInventory(Player player, Inventory inventory, String title) {
		if (waitingInv == null) {
			CoreUtils.debug("Waiting inv failed to init. Retrying. Result: " + init());
		}
		player.openInventory(waitingInv);
		invTracker.put(player.getUniqueId(), "waiting");
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				player.openInventory(inventory);
				invTracker.put(player.getUniqueId(), title);
			}

		}, 5);
	}

	public static void closeInventory(Player player) {
		if (invTracker.containsKey(player.getUniqueId())) {
			if (invTracker.get(player.getUniqueId()) != "none") {
				invTracker.put(player.getUniqueId(), "none");
				player.closeInventory();
			}

		} else {
			try {
			} catch (Exception ex) {
			}
			invTracker.put(player.getUniqueId(), "none");
		}

	}

	// Generators for core inventories

	public static Inventory generateParticleFormatMenu(Player player) {

		InventoryCreator inv = new InventoryCreator("Particle Formats", (null),
				(((ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).size() / 9) + 1) * 9) + 9);
		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eComing Soon", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		for (int i = 0; i != (((int) (ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).size() / 9)) + 1)
				* 9; i++) {
			if (i < ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).size()) {
				if (player.hasPermission("mysticcloud.particleformat." + ChatColor.stripColor(ParticleFormatEnum
						.getAvalibleFormats(player.getUniqueId()).get(i).name().toLowerCase().replaceAll(" ", "_")))) {
					inv.addItem(ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).get(i).formatter().item(),
							ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).get(i).formatter().name(),
							(char) i, (String[]) null, false);
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE),
							ParticleFormatEnum.getAvalibleFormats(player.getUniqueId()).get(i).formatter().name(),
							(char) i, new String[] { "&cLocked..." }, false);
				}
				c.add((char) i);
			} else {
				c.add('X');
			}

		}

		inv.addItem(new ItemStack(Material.BARRIER), CoreUtils.colorize("&cRemove Particles"), 'z', (String[]) null,
				false);

		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('z');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');

		inv.setConfiguration(c);
		c.clear();
		c = null;

		return inv.getInventory();

	}

	public static Inventory generateParticleMenu(Player player, ParticleFormat format) {
		InventoryCreator inv = new InventoryCreator("Particles", (null),
				(((int) (format.allowedParticles().size() / 9)) + 1) * 9);
		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eComing Soon", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		for (int i = 0; i != (((int) (format.allowedParticles().size() / 9)) + 1) * 9; i++) {
			if (i < format.allowedParticles().size()) {
				Particle p = (Particle) format.allowedParticles().toArray()[i];
				boolean perm = player
						.hasPermission("mysticcloud.particle." + ChatColor.stripColor(p.name().toLowerCase()));

				inv.addItem(CoreUtils.particleToItemStack(p, perm),
						perm ? CoreUtils.colorize(CoreUtils.particlesToString(p)) : CoreUtils.colorize("&cLocked..."),
						(char) i, (String[]) null, true, false, (short) 0);
				c.add((char) i);
			} else {
				c.add('X');
			}

		}
		inv.setConfiguration(c);
//		c.clear();
//		c = null;
		return inv.getInventory();

	}

	public static Inventory createSettingsMenu(Player player) {
		MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
		InventoryCreator inv = new InventoryCreator("&6&lSettings Menu", null, 9);
		inv.addItem(new ItemStack(Material.DIAMOND), "&aParticle Settings", 'A',
				new String[] { "&7Click to open Particle Settings." });
		inv.addItem(new ItemStack(Material.PAPER), "&eSidebar", 'B', new String[] { "&7Currently is "
				+ (mp.getSetting(PlayerSettings.SIDEBAR).equalsIgnoreCase("true") ? "&a&lon" : "&c&loff") + "&7." });
		inv.setConfiguration(new char[] { 'A', 'B', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Inventory getParticleSettingsMenu(Player player) {
		MysticPlayer mp = MysticAccountManager.getMysticPlayer(player);
		InventoryCreator inv = new InventoryCreator("&a&lParticle Settings Menu", null, 9);
		inv.addItem(new ItemStack(Material.FIREWORK_ROCKET), "&aHoliday Particles", 'A',
				new String[] { "&7Currently is "
						+ (mp.getSetting(PlayerSettings.HOLIDAY_PARTICLES).equalsIgnoreCase("true") ? "&a&lon"
								: "&c&loff")
						+ "&7." });
		inv.addItem(new ItemStack(Material.GRASS_BLOCK), "&cRegional Particles", 'B', new String[] { "&7Currently is "
				+ (mp.getSetting(PlayerSettings.REGIONAL_PARTICLES).equalsIgnoreCase("true") ? "&a&lon" : "&c&loff")
				+ "&7." });
		inv.setConfiguration(new char[] { 'A', 'B', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Inventory getSettingsMenu(Player player) {
		return createSettingsMenu(player);
	}

	public static Inventory generateAngelicConfigurations(Player player) {

		String rainbow = "";
		for (int i = 0; i != ("Rainbow").length(); i++) {
			rainbow = rainbow + net.md_5.bungee.api.ChatColor.of(CoreUtils.generateColor(i + 10, 0.5))
					+ ("Rainbow").substring(i, i + 1);
		}
		InventoryCreator inv = new InventoryCreator("&e&lAngelic &eConfigurations", null, 27);

		inv.addItem(new ItemStack(Material.WHITE_DYE), "&e&lArch&f Angel", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&cDemon", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.BLACK_DYE), "&4Satan", 'C', new String[] {});
		inv.addItem(new ItemStack(Material.COAL), "&8Night Flyer", 'D', new String[] {});
		inv.addItem(new ItemStack(Material.FEATHER), "&7Angel", 'E', new String[] {});
		inv.addItem(new ItemStack(Material.MAGMA_CREAM), rainbow, 'F', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(new char[] { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'A', 'B', 'C', 'X', 'D',
				'E', 'F', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Inventory generateParticleColorMenu(Player player, Particle particle) {
		InventoryCreator inv = new InventoryCreator("&b&lColors", null, 36);

		inv.addItem(new ItemStack(Material.PINK_DYE), "&dPink", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.RED_DYE), "&cRed", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.ORANGE_DYE), "&6Orange", 'C', new String[] {});
		inv.addItem(new ItemStack(Material.YELLOW_DYE), "&eYellow", 'D', new String[] {});

		inv.addItem(new ItemStack(Material.LIME_DYE), "&aLime Green", 'E', new String[] {});
		inv.addItem(new ItemStack(Material.GREEN_DYE), "&2Green", 'F', new String[] {});

		inv.addItem(new ItemStack(Material.BLUE_DYE), "&3Blue", 'G', new String[] {});
		inv.addItem(new ItemStack(Material.PURPLE_DYE), "&5Purple", 'H', new String[] {});
		inv.addItem(new ItemStack(Material.BROWN_DYE), "&6Brown", 'I', new String[] {});

		inv.addItem(new ItemStack(Material.BLACK_DYE), "&7Black", 'J', new String[] {});
		inv.addItem(new ItemStack(Material.WHITE_DYE), "&fWhite", 'K', new String[] {});

		String a = "";
		for (int i = 0; i != ("Rainbow").length(); i++) {
			a = a + net.md_5.bungee.api.ChatColor.of(CoreUtils.generateColor(i + 2, 0.9, 127))
					+ ("Rainbow").substring(i, i + 1);
		}
		inv.addItem(new ItemStack(Material.MAGMA_CREAM), a, 'L', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'X',
						'X', 'X', 'H', 'I', 'L', 'J', 'K', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Inventory getTagsMenu(Player player) {
		return getTagsMenu(player, 1);
	}

	public static Inventory getTagsMenu(Player player, int page) {
		boolean pages = CustomTag.values().length > 36;
		DebugUtils.debug("Pages:" + pages);

		int size = !pages ? (int) ((((CustomTag.values().length - 1) / 9) + 1) * 9) : 36;
		DebugUtils.debug("Size:" + size);

		InventoryCreator inv = new InventoryCreator("&e&lCustom Tags&7:", (null), size + 18);

		ArrayList<Character> c = new ArrayList<Character>();

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eComing Soon", 'x', (String[]) null);
		inv.addItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "&7Choose an option.", 'X', (String[]) null);
		inv.addItem(new ItemStack(Material.BARRIER), "&cRemove Tag", 'Y', (String[]) null);

		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');

		List<String> keys = new ArrayList<>();
		for (String s : CustomTag.keys())
			keys.add(s);
		int x = 0;

		for (int i = 0; i != size; i++) {

			if (i < (size)) {
				String key = CoreUtils.getPageResults(keys, page, 36).get(i);
				String value = CustomTag.getTag(key);
				if (value.contains("[NT]")) {
					x = x + 1;
					continue;
				}
				String name = key.substring(0, 1).toUpperCase() + key.substring(1, key.length()).toLowerCase();
				if (player.hasPermission("mysticcloud.customtag." + key.toLowerCase())) {
					inv.addItem(new ItemStack(Material.NAME_TAG), CoreUtils.colorize("&e" + name), (char) i,
							new String[] { PlaceholderUtils.emotify(value) }, false);
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), "&cLocked...", (char) i,
							new String[] { PlaceholderUtils.emotify(value) }, false);
				}
				c.add((char) i);
			} else {
				c.add('X');
			}

		}

		inv.addItem(new ItemStack(Material.BARRIER), "&aPage &7" + (page + 1), (char) (size + 1), (String[]) null);
		inv.addItem(new ItemStack(Material.BARRIER), "&aPage &7" + (page - 1), (char) (size + 2), (String[]) null);

		c.add('X');
		c.add('X');
		c.add('X');
		if (page != 1) {
			c.add((char) (size + 2));
		} else {
			c.add('X');
		}

		c.add('X');
		c.add('Y');
		c.add('X');
		if (CustomTag.keys().length / 36 > page) {
			c.add((char) (size + 1));
		} else {
			c.add('X');
		}

		c.add('X');
		c.add('X');

		inv.setConfiguration(c);
		c.clear();
		c = null;

		return inv.getInventory();

	}

}
