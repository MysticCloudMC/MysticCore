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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.runnables.KitCooldownTimer;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.InventoryCreator;

public class KitManager {

	static boolean loaded = false;

	static String name = "Kit GUI";
	
	static File cooldownFile = new File(Main.getPlugin().getDataFolder() + "/kitcache/cooldowns.yml");

	static Map<String, Kit> kitsManager = new HashMap<>();
	static Map<UUID, KitWrapper> cooldown = new HashMap<>();
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
			
			for(String uid : cc.getStringList("UUIDS")) {
				for(String kit : cc.getStringList(uid + ".kits")) {
					KitWrapper wrapper = new KitWrapper(cc.getString(uid + "." + kit), cc.getLong(uid + ".started"));
					cooldown.put(UUID.fromString(uid), wrapper);
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

	public static boolean isInCooldown(UUID uid, String kit) {
		if (cooldown.containsKey(uid)) {
			if (cooldown.get(uid).kit().equals(kit))
				return true;
		}
		return false;
	}

	public static Kit getKit(String kit) {
		return kitsManager.get(kit);
	}

	public static void applyKit(Player player, String kit) {
		if (cooldown.containsKey(player.getUniqueId()) && !player.hasPermission("mysticcloud.admin.kit.override")) {
			CoreUtils.debug((new Date().getTime() - cooldown.get(player.getUniqueId()).started()) + "");
			player.sendMessage(CoreUtils.prefixes().get("kits") + CoreUtils.colorize("You can't use that kit yet! You must wait &7"
					+ getSimpleTimeFormat((getKit(kit).getCooldown()*1000)-(new Date().getTime() - cooldown.get(player.getUniqueId()).started())) + "&f."));
			return;
		}
		for (ItemStack item : kitsManager.get(kit).getItems()) {
			if (player.getInventory().firstEmpty() != -1) {
				player.getInventory().addItem(item);
			} else {
				player.getWorld().dropItemNaturally(player.getLocation(), item);
				player.sendMessage(
						CoreUtils.prefixes().get("kits") +  CoreUtils.colorize("No room in inventory. Dropping item on ground.."));
			}
		}
		KitWrapper wrapper = new KitWrapper(kit, new Date().getTime());
		cooldown.put(player.getUniqueId(), wrapper);
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(),
				new KitCooldownTimer(player.getUniqueId(), wrapper), 20);
	}

	private static String getSimpleTimeFormat(long micro) {

		
		int l = (int) (micro/1000);
		int sec = l % 60;
		int min = (l / 60) % 60;
		int hours = ((l / 60) / 60) % 24;
		int days = (((l / 60) / 60) / 24) % 7;
		int weeks = (((l / 60) / 60) / 24) / 7;

		if (weeks > 0) {
			return weeks + " &fweeks, &7" + days + "&f days, &7" + hours + "&f hours, &7" + min + "&f minutes, and &7" + sec
					+ " seconds";
		}
		if (days > 0) {
			return days + "&f days, &7" + hours + "&f hours, &7" + min + "&f minutes, and &7" + sec + "&f seconds";
		}
		if (hours > 0) {
			return hours + "&f hours, &7" + min + "&f minutes, and &7" + sec + "&f seconds";
		}
		if (min > 0) {
			return min + "&f minutes, and &7" + sec + "&f seconds";
		}
		if (sec > 0) {
			return sec + "&f seconds";
		}

		return ((long)(micro/1000)) + "&f seconds";
	}

	public static Inventory getGUI(Player player) {

		InventoryCreator inv = new InventoryCreator(name, (player), ((KitManager.getKits().size() / 9) + 1) * 9);
		inv.addItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "&eComing Soon", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		for (int i = 0; i != (((int) (KitManager.getKits().size() / 9)) + 1) * 9; i++) {
			if (i < KitManager.getKits().size()) {
				if (player.hasPermission("mysticcloud.kit." + KitManager.getKits().get(i).getName())) {
					inv.addItem(KitManager.getKits().get(i).getItem(), KitManager.getKits().get(i).getDisplayName(),
							(char) i, KitManager.getKits().get(i).getDescription(),
							 true, true, KitManager.getKits().get(i).getItem().getDurability());
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE),
							KitManager.getKits().get(i).getDisplayName(), (char) i, new String[] { "&cLocked..." },false);
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
		cooldown.remove(uid);
	}

	public static void unloadCooldowns() {
		FileConfiguration cc = YamlConfiguration.loadConfiguration(cooldownFile);
		Map<String, List<String>> uids = new HashMap<>();
		for (Entry<UUID, KitWrapper> entry : cooldown.entrySet()) {
			if(!uids.containsKey(entry.getKey() + "")) {
				uids.put(entry.getKey() +"", new ArrayList<>());
				uids.get(entry.getKey()).add(entry.getValue().kit());

			} else {
				uids.get(entry.getKey()).add(entry.getValue().kit());
			}
			
			cc.set(entry.getKey() + "." + entry.getValue().kit(), entry.getValue().started());
		}
		
		cc.set("UUIDS", uids.keySet());
		for(Entry<String, List<String>> entry : uids.entrySet()) {
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
