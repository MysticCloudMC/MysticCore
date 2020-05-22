package net.mysticcloud.spigot.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
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
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

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
		if(waitingInv == null) {
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
				((ParticleFormatEnum.values().length / 9) + 1) * 9);
		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eComing Soon", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		for (int i = 0; i != (((int) (ParticleFormatEnum.values().length / 9)) + 1) * 9; i++) {
			if (i < ParticleFormatEnum.values().length) {
				if (player.hasPermission("mysticcloud.particleformat." + ChatColor.stripColor(
						ParticleFormatEnum.values()[i].name().toLowerCase().replaceAll(" ", "_")))) {
					inv.addItem(ParticleFormatEnum.values()[i].formatter().item(),
							ParticleFormatEnum.values()[i].formatter().name(), (char) i, (String[]) null, false);
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE),
							ParticleFormatEnum.values()[i].formatter().name(), (char) i, new String[] { "&cLocked..." },
							false);
				}
				c.add((char) i);
			} else {
				c.add('X');
			}

		}
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
						(char) i, (String[]) null, true, false, (short)0);
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

	public static Inventory createSettingsMenu() {
		InventoryCreator inv = new InventoryCreator("&6Settings", null, 9);
		inv.addItem(new ItemStack(Material.DIAMOND), "&eParticles", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.PAPER), "&cSidebar", 'B', new String[] {});
		inv.setConfiguration(new char[] { 'A', 'B', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Inventory getSettingsMenu() {
		return createSettingsMenu();
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

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);
		
		inv.setConfiguration(new char[] {
				'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
				'X', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'X',
				'X', 'X', 'H', 'I', 'X', 'J', 'K', 'X', 'X',
				'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'
				});
		return inv.getInventory();
	}

}
