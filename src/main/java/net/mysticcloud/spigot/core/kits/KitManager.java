package net.mysticcloud.spigot.core.kits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.runnables.KitCooldownTimer;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.admin.InventoryCreator;

public class KitManager {

	static boolean loaded = false;

	static String name = "Kit GUI";

	static File cooldownFile = new File(Main.getPlugin().getDataFolder() + "/kitcache/cooldowns.yml");

	static Map<String, Kit> kitsManager = new HashMap<>();
	static List<Kit> kits = new ArrayList<>();

	public static List<Kit> getKits() {
		return kits;
	}

	@SuppressWarnings("deprecation")
	public static void registerKits() {
		try {
			for (File file : getAllFiles()) {
				Kit kit = new Kit(file.getName().replace(".yml", ""));
				FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
				if (fc.isSet("Items")) {
					for (String s : fc.getStringList("Items")) {
						if (s.contains("CustomItem")) {
							kit.addItem(CoreUtils.getItem(s.split(":")[1]));
						} else {
							kit.addItem(CoreUtils.decryptItemStack(s));
						}
					}
				}

				if (fc.isSet("Item")) {
//					ItemStack item = new ItemStack(Material.COOKED_BEEF);
					if (fc.getString("Item").contains("CustomItem")) {
						kit.setGUIItem(CoreUtils.getItem(fc.getString("Item").split(":")[1]));
					} else {
						kit.setGUIItem(CoreUtils.decryptItemStack(fc.getString("Item")));
					}
				}
				if (fc.isSet("DisplayName")) {
					kit.setDisplayName(fc.getString("DisplayName"));
				}
				if (fc.isSet("Description")) {
					kit.setDescription(fc.getString("Description"));
				}
				if (fc.isSet("Cooldown")) {
					kit.setCooldown(fc.getInt("Cooldown"));
				}

				kitsManager.put(kit.getName(), kit);
				kits.add(kit);
			}
		} catch (NullPointerException ex) {
			createDemoFile();

		}
		try {
			FileConfiguration cc = YamlConfiguration.loadConfiguration(cooldownFile);

			for (String uid : cc.getStringList("UUIDS")) {
				for (String kit : cc.getStringList(uid + ".kits")) {
					KitWrapper wrapper = new KitWrapper(cc.getString(uid + "." + kit), cc.getLong(uid + ".started"));
					kitsManager.get(kit).cooldowns.put(UUID.fromString(uid), cc.getLong(uid + ".started"));
					Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(),
							new KitCooldownTimer(UUID.fromString(uid), wrapper), 20);
				}

			}

		} catch (NullPointerException ex) {
			cooldownFile.getParentFile().mkdirs();
			try {
				cooldownFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!loaded) {
			loaded = true;
			reloadKits();
		}
	}

	private static File[] getAllFiles() {
		return new File(Main.getPlugin().getDataFolder() + "/kits").listFiles();
	}

	public static void createFiles() {
		new File(Main.getPlugin().getDataFolder() + "/kits").mkdir();
	}

	public static void createDemoFile() {
		createFiles();
		File demo = new File(Main.getPlugin().getDataFolder() + "/kits/demo.yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(demo);
		try {
			if (!demo.exists())
				demo.getParentFile().mkdirs();
			demo.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fc.set("Cooldown", 100);

		List<String> demoList1 = new ArrayList<String>();
		demoList1.add("CustomItem:SuperSword-5");

		fc.set("Items", demoList1);

		fc.set("DisplayName", "&6Demo");
		fc.set("Description", "&fThis is the default kit.%nGenerated when no other kits exist.");
		fc.set("Item", "CustomItem:Backpack");

		try {
			fc.save(demo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		registerKits();

	}

	public static void reloadKits() {
		kitsManager.clear();
		kits.clear();
		registerKits();
	}

	public static boolean kitExists(String kit) {
		return kitsManager.containsKey(kit);
	}

	public static Kit getKit(String kit) {
		return kitsManager.get(kit);
	}

	public static void applyKit(Player player, String kit) {
		if (kitsManager.get(kit).getCooldowns().containsKey(player.getUniqueId())
				&& !player.hasPermission("mysticcloud.admin.kit.override")) {
			if (getKit(kit).checkCooldown(player)) {
				player.sendMessage(
						CoreUtils.prefixes().get("kits")
								+ CoreUtils.colorize("You can't use that kit yet! You must wait &7"
										+ getSimpleTimeFormat((getKit(kit).getCooldown() * 1000)
												- (new Date().getTime() - getKit(kit).getStartingTime(player)))
										+ "&f."));
				return;
			}
		}
		for (ItemStack item : kitsManager.get(kit).getItems()) {
			if (player.getInventory().firstEmpty() != -1) {
				if (item.getType().name().contains("_HELMET") || item.getType().name().contains("_CHESTPLATE")
						|| item.getType().name().contains("_LEGGINGS") || item.getType().name().contains("_BOOTS")) {
					switch (item.getType().name().split("_")[item.getType().name().split("_").length - 1]) {
					case "HELMET":
						player.getInventory().setItem(EquipmentSlot.HEAD, item);
						break;
					case "CHESTPLATE":
						player.getInventory().setItem(EquipmentSlot.CHEST, item);
						break;
					case "LEGGINGS":
						player.getInventory().setItem(EquipmentSlot.LEGS, item);
						break;
					case "BOOTS":
						player.getInventory().setItem(EquipmentSlot.FEET, item);
						break;
					default:
						break;
					}
				} else
					player.getInventory().addItem(item);
			} else {
				player.getWorld().dropItemNaturally(player.getLocation(), item);
				player.sendMessage(CoreUtils.prefixes().get("kits")
						+ CoreUtils.colorize("No room in inventory. Dropping item on ground.."));
			}
		}
		KitWrapper wrapper = new KitWrapper(kit, new Date().getTime());
		getKit(kit).cooldowns.put(player.getUniqueId(), wrapper.started());
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(),
				new KitCooldownTimer(player.getUniqueId(), wrapper), 20);
	}

	private static String getSimpleTimeFormat(long micro) {
		return CoreUtils.formatDateTime(micro, "&7", "&f");
	}

	@SuppressWarnings("deprecation")
	public static Inventory getGUI(Player player) {

		InventoryCreator inv = new InventoryCreator(name, (player), ((KitManager.getKits().size() / 9) + 1) * 9);
		inv.addItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "&eComing Soon", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		for (int i = 0; i != (((int) (KitManager.getKits().size() / 9)) + 1) * 9; i++) {
			if (i < KitManager.getKits().size()) {
				if (player.hasPermission("mysticcloud.kit." + KitManager.getKits().get(i).getName())) {
					inv.addItem(KitManager.getKits().get(i).getItem(), KitManager.getKits().get(i).getDisplayName(),
							(char) i, KitManager.getKits().get(i).getDescription(), true, false,
							KitManager.getKits().get(i).getItem().getDurability());
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE),
							KitManager.getKits().get(i).getDisplayName(), (char) i, new String[] { "&cLocked..." },
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

	public static String getGUIName() {
		return name;
	}

	public static void removeFromCooldown(UUID uid, String kit) {
		getKit(kit).cooldowns.remove(uid);
	}

	public static void unloadCooldowns() {
		FileConfiguration cc = YamlConfiguration.loadConfiguration(cooldownFile);
		Map<String, List<String>> uids = new HashMap<>();
		for (Kit kit : getKits()) {
			for (Entry<UUID, Long> entry : kit.cooldowns.entrySet()) {
				if (!uids.containsKey(entry.getKey() + "")) {
					uids.put(entry.getKey().toString(), new ArrayList<>());
					uids.get(entry.getKey().toString()).add(kit.name);

				} else {
					uids.get(entry.getKey().toString()).add(kit.name);
				}

				cc.set(entry.getKey() + "." + kit.name, entry.getValue());
			}
		}

		cc.set("UUIDS", uids.keySet());
		for (Entry<String, List<String>> entry : uids.entrySet()) {
			cc.set(entry + ".kits", entry.getValue());
		}

		try {
			cc.save(cooldownFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
