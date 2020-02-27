package net.mysticcloud.spigot.core.utils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.milkbowl.vault.economy.Economy;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.PetManager;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;
import net.mysticcloud.spigot.core.utils.warps.WarpUtils;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CoreUtils {

	private static IDatabase db;
	private static IDatabase wbconn;
	private static boolean connected = false;
	private static Holiday holiday = Holiday.NONE;
	private static Date date = new Date();
	public static Map<UUID, Boolean> holidayparticles = new HashMap<>();

	public static Map<UUID, ParticleFormatEnum> particles = new HashMap<>();
	static Map<UUID, MysticPlayer> mplayers = new HashMap<>();

	public static String prefix = "MysticCloud";
	public static String fullPrefix = colorize("&3&l" + prefix + " &7>&f ");

	private static Map<String, String> prefixes = new HashMap<>();

	private static boolean debug = false;
	private static Location spawn = null;

	private static Map<String, String> playerlist = new HashMap<>();

	private static File itemfile = new File(Main.getPlugin().getDataFolder() + "/Items");
	private static List<FileConfiguration> itemFiles = new ArrayList<>();
	private static Map<String, ItemStack> items = new HashMap<>();
	private static Map<String, FoodInfo> food = new HashMap<>();
	private static Map<String, String> variables = new HashMap<>();

	public static Map<UUID, List<TimedPerm>> timedPerms = new HashMap<>();
	public static Map<UUID, String> offlineTimedUsers = new HashMap<>();

	private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private static Objective objective = null;

	public static List<String> ecoaccounts = new ArrayList<>();
	public static double startingBalance = 100.00;
	private static Economy economy = null;
	private static Map<Integer, String> sidebar = new HashMap<>();

	public static float t = 0;

	public static void start() {
		
		setupEconomy();

		prefixes.put("root", fullPrefix);

		prefixes.put("sql", colorize("&3&lSQL &7>&f "));
		prefixes.put("settings", colorize("&3&lSettings &7>&f "));
		prefixes.put("items", colorize("&3&lItems &7>&f "));
		prefixes.put("kits", colorize("&3&lKits &7>&f "));
		prefixes.put("pets", colorize("&e&lPets &7>&f "));
		prefixes.put("admin", colorize("&c&lAdmin &7>&f "));
		prefixes.put("debug", colorize("&3&lDebug &7>&f "));
		prefixes.put("warps", colorize("&b&lWarps &7>&f "));
		prefixes.put("eco", colorize("&6&lEconomy &7>&f "));

		registerScoreboard("sidebar", colorize("        &3&lMystic&f&lCloud        "));

		registerSidebarList();

		if (Main.getPlugin().getConfig().isSet("TimedUsers")) {
			for (String uid : Main.getPlugin().getConfig().getStringList("TimedUsers")) {
				List<String> perms = Main.getPlugin().getConfig().getStringList("TimedPerm." + uid);
				for (String data : perms) {
					addExistingTimedPermission(UUID.fromString(uid), data.split(":")[2], data.split(":")[3],
							Long.parseLong(data.split(":")[0]), Long.parseLong(data.split(":")[1]));
				}
			}
		}

		if (testSQLConnection()) {
			connected = true;
			db = new IDatabase(SQLDriver.MYSQL, "157.245.121.66", "Minecraft", 3306, "mysql", "v4pob8LW");
			wbconn = new IDatabase(SQLDriver.MYSQL, "157.245.121.66", "Website", 3306, "mysql", "v4pob8LW");
			Bukkit.getConsoleSender().sendMessage(prefixes.get("sql") + "Successfully connected to MySQL.");
		} else {
			connected = false;
			db = new IDatabase(SQLDriver.SQLITE, "Minecraft");
			wbconn = new IDatabase(SQLDriver.SQLITE, "Website");
			Bukkit.getConsoleSender().sendMessage(prefixes.get("sql") + "Error connecting to MySQL. Using SQLite");
		}
		loadVariables();
		if (!itemfile.exists()) {
			itemfile.mkdirs();
		}
		try {

			for (File file : itemfile.listFiles()) {
				if (file.getName().startsWith("item") && file.getName().endsWith(".yml")) {
					itemFiles.add(YamlConfiguration.loadConfiguration(file));
				}
			}
			itemFiles.size();

		} catch (NullPointerException ex) {

			File file = new File(itemfile.getPath() + "/itemDefault.yml");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

			fc.set("SuperSword.Id", "DIAMOND_SWORD");
			fc.set("SuperSword.Data", "570");
			fc.set("SuperSword.Display", "&6Super Sword");

			try {
				fc.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			itemFiles.add(fc);

		}

		if (itemFiles.size() == 0) {

			File file = new File(itemfile.getPath() + "/itemDefault.yml");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

			fc.set("SuperSword.Id", "DIAMOND_SWORD");
			fc.set("SuperSword.Data", "570");
			fc.set("SuperSword.Options.Display", "&6Super Sword");

			try {
				fc.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			itemFiles.add(fc);

		}
		PetManager.registerPets();

		if (Main.getPlugin().getConfig().isSet("SPAWN") && Main.getPlugin().getConfig().getString("SPAWN") != "")
			spawn = decryptLocation(Main.getPlugin().getConfig().getString("SPAWN"));

		if (Main.getPlugin().getConfig().isSet("PlayerList.Header")) {
			playerlist.put("header", CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.Header")));
		}
		if (Main.getPlugin().getConfig().isSet("PlayerList.PlayerName")) {
			playerlist.put("name", CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.PlayerName")));
		}
		if (Main.getPlugin().getConfig().isSet("PlayerList.Footer")) {
			playerlist.put("footer", CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.Footer")));
		}

		WarpUtils.registerWarps();

	}
	
	public static Economy getEconomy() {
		return economy;
	}
	

	public static void setupEconomy() {
		Main.getPlugin().getServer().getServicesManager().register(Economy.class, new VaultAPI(),
				(Plugin) Main.getPlugin(), ServicePriority.Normal);
		economy = (Economy) new VaultAPI();
	}

	public static void loadVariables() {
		ResultSet rs = sendQuery("SELECT * FROM Settings");
		try {
			while (rs.next()) {
				variables.put(rs.getString("SETTING"), rs.getString("VALUE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getVariable(String var) {
		return variables.containsKey(var) ? variables.get(var) : "ERROR";
	}

	public static void end() {
		KitManager.unloadCooldowns();
		WarpUtils.save();
		saveConfig();
	}

	public static void saveConfig() {

		if (spawn != null)
			Main.getPlugin().getConfig().set("SPAWN", encryptLocation(spawn));
		if (playerlist.containsKey("header"))
			Main.getPlugin().getConfig().set("PlayerList.Header", playerList("header"));
		if (playerlist.containsKey("name"))
			Main.getPlugin().getConfig().set("PlayerList.PlayerName", playerList("name"));
		if (playerlist.containsKey("footer"))
			Main.getPlugin().getConfig().set("PlayerList.Footer", playerList("footer"));
		Map<String, List<String>> storage = new HashMap<>();
		List<String> users = new ArrayList<>();
		for (Entry<UUID, List<TimedPerm>> entry : timedPerms.entrySet()) {
			users.add(entry.getKey() + "");

			storage.put(entry.getKey() + "", new ArrayList<>());
			for (TimedPerm perm : entry.getValue()) {
				storage.get(entry.getKey() + "")
						.add(perm.started() + ":" + perm.liveFor() + ":" + perm.permission() + ":" + perm.display());
			}

		}
		for (Entry<String, List<String>> estorage : storage.entrySet()) {
			Main.getPlugin().getConfig().set("TimedPerm." + estorage.getKey(), estorage.getValue());
		}
		Main.getPlugin().getConfig().set("TimedUsers", users);

		Main.getPlugin().saveConfig();

	}

	public static String playerList(String key) {
		return playerlist.containsKey(key) ? playerlist.get(key) : "";
	}

	public static void playerList(String key, String value) {
		playerlist.put(key, value);
	}

	public static void particles(UUID uid, ParticleFormatEnum format) {
		particles.put(uid, format);
	}

	public static ParticleFormatEnum particles(UUID uid) throws NullPointerException {
		return particles.containsKey(uid) ? particles.get(uid) : null;
	}

	public static String encryptLocation(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getPitch()
				+ ":" + loc.getYaw();
	}

	public static Location decryptLocation(String s) {
		debug("Decrypting Location: " + s);
		String[] args = s.split(":");
		return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
				Double.parseDouble(args[3]));
	}

	public static Map<String, String> prefixes() {
		return prefixes;
	}

	public static void addPrefix(String key, String value) {
		prefixes.put(key, (colorize(value)));
	}

	public static void teleportToSpawn(Player player) {
		teleportToSpawn(player, SpawnReason.SELF);
	}

	public static void teleportToSpawn(Player player, SpawnReason reason) {
		if (spawn == null) {
			debug("Spawn Location null...");
			return;
		}
		try {
			player.teleport(spawn);
			player.sendMessage(fullPrefix + reason.message());
		} catch (IllegalArgumentException ex) {
//			spawn.getWorld().loadChunk(spawn.getChunk());
//			player.teleport(spawn);
//			player.sendMessage(fullPrefix + reason.message());
		}

	}

	public static String prefixes(String key) {
		return prefixes.get(key);
	}

	public static Map<String, ItemStack> getCachedItems() {

		return items;
	}

	public static void sendPluginMessage(Player player, String channel, String... arguments) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String s : arguments) {
			out.writeUTF(s);
		}
		player.sendPluginMessage(Main.getPlugin(), channel, out.toByteArray());
	}

	private static boolean testSQLConnection() {
		return new IDatabase(SQLDriver.MYSQL, "localhost", "Minecraft", 3306, "mysql", "v4pob8LW").init();

	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	@SuppressWarnings("deprecation")
	public static String getTime() {

		String min = date.getMinutes() + "";
		String hr = date.getHours() + "";
		if (min.length() == 1)
			min = "0" + min;
		if (Integer.parseInt(hr) > 12) {
			return (Integer.parseInt(hr) - 12) + ":" + min + " PM";
		} else {
			return hr + ":" + min + " AM";
		}
	}

	public static boolean loreContains(List<String> lore, String string) {
		for (String s : lore) {
			if (s.contains(string)) {
				return true;
			}
		}
		return false;

	}

	public static boolean isFood(ItemStack item) {
		for (Entry<String, ItemStack> entry : items.entrySet()) {
			if (entry.getValue().hasItemMeta()
					&& entry.getValue().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
				return true;

			}
		}
		if (!item.hasItemMeta())
			return false;
		if (!item.getItemMeta().hasLore())
			return false;
		if (loreContains(item.getItemMeta().getLore(), "Food")) {
			for (String l : item.getItemMeta().getLore()) {
				if (l.contains("Food")) {
					getItem(l.split(":")[1]);
					return true;
				}
			}
		}
		return false;
	}

	public static FoodInfo getFood(ItemStack item) {
		for (Entry<String, ItemStack> entry : items.entrySet()) {
			if (entry.getValue().hasItemMeta()
					&& entry.getValue().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {

				return food.get(entry.getKey());

			}
		}
		return null;
	}

	public static int registerPlayer(String webName, Player player) throws SQLException {
		if (isPlayerRegistered(webName, player))
			return -100;
		return wbconn.update("UPDATE Users SET REGISTERED='waiting',MINECRAFT_UUID='" + player.getUniqueId()
				+ "' WHERE USERNAME='" + webName + "'");
	}

	private static boolean isPlayerRegistered(String webName, Player player) throws SQLException {

		wbconn.init();
		ResultSet rs = wbconn.query("SELECT * FROM Users");
		while (rs.next()) {
			if (rs.getString("USERNAME").equalsIgnoreCase(webName)) {
				try {
					if (!rs.getString("REGISTERED").equalsIgnoreCase("true"))
						return false;
					else
						return true;
				} catch (NullPointerException ex) {
					return false;
				}

			}

		}
		return false;
	}

	public static ResultSet sendQuery(String query) throws NullPointerException {
		return db.query(query);
	}

	public static Integer sendUpdate(String query) throws NullPointerException {
		return db.update(query);
	}

	public static boolean sendInsert(String query) throws NullPointerException {
		return db.input(query);
	}

	public static String toString(String[] args) {
		String s = "";
		for (String a : args) {
			s = s + " " + a;
		}
		return s;
	}

	public static void updateDate() {

		date = new Date();
	}

	@SuppressWarnings("deprecation")
	public static int getMonth() {
		return date.getMonth();
	}

	@SuppressWarnings("deprecation")
	public static int getDay() {
		return date.getDay();
	}

	public static void setHoliday(Holiday hol) {
		holiday = hol;
	}

	public static Holiday getHoliday() {
		return holiday;
	}

	public static Date getDate() {
		return date;
	}

	public static List<String> colorizeStringList(List<String> stringList) {
		return colorizeStringList((String[]) stringList.toArray());
	}

	public static List<String> colorizeStringList(String[] stringList) {
		List<String> ret = new ArrayList<>();
		for (String s : stringList) {
			ret.add(colorize(s));
		}
		return ret;
	}

	public static Random getRandom() {
		return new Random();
	}

	public static void toggleParticles(Player player) {
		if (!holidayparticles.containsKey(player.getUniqueId())) {
			holidayparticles.put(player.getUniqueId(), true);
			return;
		}
		if (holidayparticles.get(player.getUniqueId())) {
			player.sendMessage(prefixes.get("settings") + "Particles turned off");
			holidayparticles.put(player.getUniqueId(), false);
		} else {
			player.sendMessage(prefixes.get("settings") + "Particles turned on");
			holidayparticles.put(player.getUniqueId(), true);
		}
	}

	public static void addPermission(Player player, String permission) {
		PermissionsEx.getUser(player).addPermission(permission);
	}

	public static void addTimedPermission(Player player, String permission, String display, long livefor) {
		TimedPerm perm = new TimedPerm(player.getUniqueId(), permission, livefor);
		perm.display(display);
		if (timedPerms.get(player.getUniqueId()) == null) {
			timedPerms.put(player.getUniqueId(), new ArrayList<>());
		}
		timedPerms.get(player.getUniqueId()).add(perm);
		debug("Added TimedPerm for " + player.getName());
		PermissionsEx.getUser(player).addPermission(permission);
	}

	public static void addExistingTimedPermission(UUID uid, String permission, String display, long started,
			long livefor) {
		TimedPerm perm = new TimedPerm(uid, permission, livefor);
		perm.display(display);
		if (timedPerms.get(uid) == null) {
			timedPerms.put(uid, new ArrayList<>());
		}
		perm.started(started);
		debug("Added existing TimedPerm for " + uid);
		timedPerms.get(uid).add(perm);
	}

	public static void removeTimedPermission(Player player, String permission) {
		PermissionsEx.getUser(player).removePermission(permission);
		debug("Removed TimedPerm for " + player.getName());
		timedPerms.remove(player.getUniqueId());
	}

	@SuppressWarnings("deprecation")
	public static String getPlayerPrefix(Player player) {
		if (PermissionsEx.getUser(player).getGroups().length > 0) {
			String prefix = "";
			for (PermissionGroup group : PermissionsEx.getUser(player).getGroups()) {

				prefix = prefix + group.getPrefix();
			}
			return colorize(prefix);
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public static String getPlayerSuffix(Player player) {
		if (PermissionsEx.getUser(player).getGroups().length > 0) {

			String suffix = "";
			for (PermissionGroup group : PermissionsEx.getUser(player).getGroups()) {

				suffix = suffix + group.getSuffix();
			}
			return colorize(suffix);
		}
		return "";
	}

	public static boolean debugOn() {
		return debug;
	}

	public static boolean toggleDebug() {
		debug = !debug;
		return debug;
	}

	public static void setDebug(boolean status) {
		debug = status;
	}

	public static void debug(Object obj) {
		debug(obj + "");
	}

	public static void debug(String message) {
		if (debug)
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission("mysticcloud.admin")) {
					player.sendMessage(colorize(prefixes("debug") + message));
				}
			}
		Bukkit.getConsoleSender().sendMessage(colorize(prefixes("debug") + message));
	}

	public static String encryptItemStack(ItemStack i) {
		try {
			return i.getType() + ":" + i.getAmount() + ":" + i.getDurability();
		} catch (NullPointerException ex) {
			return "AIR:1:0";
		}

	}

	@Deprecated
	public static ItemStack decryptItemStack(String s) {
		if (s.contains(":")) {
			String[] d = s.split(":");
			if (d.length == 2) {
				return new ItemStack(Material.valueOf(d[0]), 1, Short.parseShort(d[1]));
			}
			if (d.length == 3) {
				return new ItemStack(Material.valueOf(d[0]), Integer.parseInt(d[1]), Short.parseShort(d[2]));
			}
			return new ItemStack(Material.valueOf(d[0]));

		} else {
			return new ItemStack(Material.valueOf(s));
		}

	}

	public static void registerScoreboard(String name, String title) {
		objective = scoreboard.registerNewObjective(name, "dummy", title);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	}

	public static void enableScoreboard(Player player) {
		registerSidebarList();
		for (Entry<Integer, String> entry : sidebar.entrySet()) {

			for (String s : scoreboard.getEntries()) {

				if (entry.getKey() == objective.getScore(s).getScore()) {
					if (!objective.getScore(s).getEntry().equals(colorize(entry.getValue()))) {
						scoreboard.resetScores(s);
					}
				} else
					continue;
			}
			objective.getScore(colorize(PlaceholderUtils.replace(player, entry.getValue()))).setScore(entry.getKey());

		}

		player.setScoreboard(scoreboard);
	}

	private static void registerSidebarList() {
		sidebar.clear();
		sidebar.put(15, "&c");
		sidebar.put(14, "&eTime");
		sidebar.put(13, "&f%time%");
		sidebar.put(12, "&c&c");
		sidebar.put(11, "&6Balance");
		sidebar.put(10, "&6$&f %balance%");
		sidebar.put(9, "&c&c&c");
		sidebar.put(8, "&cLevel");
		sidebar.put(7, "&f%level%");
		sidebar.put(6, "&c&f&c");
		sidebar.put(5, "&aGems");
		sidebar.put(4, "&f%gems%");
		sidebar.put(3, "&b");
		sidebar.put(2, "&b%holiday%");
		sidebar.put(1, "&c%holidayline%");
	}

	public static ItemStack getItem(String name) {
		debug("Loading Item " + name);

		ItemStack i = new ItemStack(Material.STONE);
		int amount = 1;
		if (name.contains("-")) {
			amount = Integer.parseInt(name.split("-")[1]);
			name = name.split("-")[0];
		}
		if (items.containsKey(name)) {

			i = items.get(name).clone();
			i.setAmount(amount);
			return i;
		}
		debug("Couldn't find item " + name + ". Searching in Items folder...");
		boolean food = false;
		boolean found = false;
		FoodInfo info = new FoodInfo(name);
		ItemMeta a = i.getItemMeta();
		for (FileConfiguration item : itemFiles) {
			if (!item.isSet(name))
				continue;
			found = true;

			if (item.isSet(name + ".Id"))
				i.setType(Material.valueOf(item.getString(name + ".Id")));
			if (item.isSet(name + ".Data"))
				i.setDurability(Short.parseShort(item.getString(name + ".Data")));
			a.setDisplayName(item.isSet(name + ".Options.Display") ? colorize(item.getString(name + ".Options.Display"))
					: ChatColor.RESET + (i.getType() + "").substring(0, 1).toUpperCase()
							+ (i.getType() + "").substring(1, (i.getType() + "").length()).toLowerCase());
			if (item.isSet(name + ".Options.Hide"))
				if (item.get(name + ".Options.Hide") != "All")
					for (String s : item.getStringList(name + ".Options.Hide"))
						a.addItemFlags(ItemFlag.valueOf("HIDE_" + s.toUpperCase()));
				else
					for (ItemFlag flag : ItemFlag.values())
						a.addItemFlags(flag);

			if (item.isSet(name + ".Options.Unbreakable"))
				a.setUnbreakable(Boolean.parseBoolean(item.getString(name + ".Options.Unbreakable")));

			if (item.isSet(name + ".Options.Lore"))
				a.setLore(CoreUtils.colorizeStringList(item.getStringList(name + ".Options.Lore")));

			if (item.isSet(name + ".Food.Hunger")) {
				food = true;
				info.setHungerLevel(item.getInt(name + ".Food.Hunger"));
			}
			if (item.isSet(name + ".Food.Potion")) {
				food = true;
				for (String s : item.getStringList(name + ".Food.Potion"))
					info.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s.split("-")[0].toUpperCase()),
							Integer.parseInt(s.split("-")[1]), Integer.parseInt(s.split("-")[2])));

			}
			if (item.isSet(name + ".Food.Health")) {
				food = true;
				info.setHealingFactor(item.getInt(name + ".Food.Health"));
			}
			if (food) {
				List<String> lore = a.hasLore() ? a.getLore() : new ArrayList<>();
				lore.add(ChatColor.DARK_GRAY + "Food:" + name);
				a.setLore(lore);
			}
			i.setAmount(amount);

			if (item.isSet(name + ".Attributes.MainHand.Damage")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Attack Damage",
						item.getDouble(name + ".Attributes.MainHand.Damage"), Operation.ADD_NUMBER, EquipmentSlot.HAND);
				a.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, am);
			}
			if (item.isSet(name + ".Attributes.MainHand.AttackSpeed")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Attack Speed",
						item.getDouble(name + ".Attributes.MainHand.AttackSpeed"), Operation.ADD_NUMBER,
						EquipmentSlot.HAND);
				a.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, am);
			}
			if (item.isSet(name + ".Attributes.Helmet.Protection")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Helmet Protection",
						item.getDouble(name + ".Attributes.Helmet.Protection"), Operation.ADD_NUMBER,
						EquipmentSlot.HEAD);
				a.addAttributeModifier(Attribute.GENERIC_ARMOR, am);
			}
			if (item.isSet(name + ".Attributes.Chestplate.Protection")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Chestplate Protection",
						item.getDouble(name + ".Attributes.Chestplate.Protection"), Operation.ADD_NUMBER,
						EquipmentSlot.CHEST);
				a.addAttributeModifier(Attribute.GENERIC_ARMOR, am);
			}
			if (item.isSet(name + ".Attributes.Pants.Protection")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Pants Protection",
						item.getDouble(name + ".Attributes.Pants.Protection"), Operation.ADD_NUMBER,
						EquipmentSlot.LEGS);
				a.addAttributeModifier(Attribute.GENERIC_ARMOR, am);
			}
			if (item.isSet(name + ".Attributes.Boots.Protection")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Boot Protection",
						item.getDouble(name + ".Attributes.Boots.Protection"), Operation.ADD_NUMBER,
						EquipmentSlot.FEET);
				a.addAttributeModifier(Attribute.GENERIC_ARMOR, am);

			}

//			Attribute.
			if (item.isSet(name + ".Attributes.Helmet.MovementSpeed")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Helmet Movement Speed",
						item.getDouble(name + ".Attributes.Helmet.MovementSpeed"), Operation.ADD_NUMBER,
						EquipmentSlot.HEAD);
				a.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, am);
			}
			if (item.isSet(name + ".Attributes.Chestplate.MovementSpeed")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Chestplate Movement Speed",
						item.getDouble(name + ".Attributes.Chestplate.MovementSpeed"), Operation.ADD_NUMBER,
						EquipmentSlot.CHEST);
				a.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, am);
			}
			if (item.isSet(name + ".Attributes.Pants.MovementSpeed")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Pants Movement Speed",
						item.getDouble(name + ".Attributes.Pants.MovementSpeed"), Operation.ADD_NUMBER,
						EquipmentSlot.LEGS);
				a.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, am);
			}
			if (item.isSet(name + ".Attributes.Boots.MovementSpeed")) {

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Boots Movement Speed",
						item.getDouble(name + ".Attributes.Boots.MovementSpeed"), Operation.ADD_NUMBER,
						EquipmentSlot.FEET);
				a.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, am);
			}

			if (item.isSet(name + ".Enchantments")) {
				for (String en : item.getStringList(name + ".Enchantments")) {
					if (en.contains("-")) {
						a.addEnchant(Enchantment.getByName(en.split("-")[0].toUpperCase()),
								Integer.parseInt(en.split("-")[1]), true);
					} else {
						a.addEnchant(Enchantment.getByName(en.toUpperCase()), 1, false);
					}

				}
			}

		}
		a.setDisplayName(a.hasDisplayName() ? a.getDisplayName() : CoreUtils.colorize("&cERROR"));
		i.setItemMeta(a);
		if (!found)

		{
			debug("Item was not found.");

			return i.clone();
		}

		debug("Item loaded from config, and saved to cache.");
		if (food)
			debug("Item " + name + " was food.");
		items.put(name, i.clone());
		if (food)
			CoreUtils.food.put(name, info);
		return i.clone();
	}

	public static boolean connected() {
		return connected;
	}

	public static Location getSpawnLocation() {
		return spawn;
	}

	public static void setSpawnLocation(Location location) {
		spawn = location;
	}

	public static String particlesToString(Particle p) {
		switch (p) {
		case FLAME:
			return "&6Flame";
		case FALLING_LAVA:
			return "&cLava";
		case FALLING_WATER:
			return "&bWater";
		case TOTEM:
			return "&aGreen Dots";
		case NOTE:
			return "&3Notes";
		case CRIT:
			return "&eYellow Sparkles";
		case CRIT_MAGIC:
			return "&3Magic Sparkles";
		case ENCHANTMENT_TABLE:
			return "&7Enchantments";
		case SMOKE_NORMAL:
			return "&8Smoke";
		case DAMAGE_INDICATOR:
			return "&cBouncing Hearts";
		case COMPOSTER:
			return "&aGreen Sparkles";
		case PORTAL:
			return "&dNether Portal";
		case SPELL_WITCH:
			return "&5Purple Sparkles";
		case SPELL_MOB:
			return "&8Black Swirls";
		case SPELL_INSTANT:
			return "&fWhite Sparkles";
		case HEART:
			return "&cHearts";
		case VILLAGER_ANGRY:
			return "&7Storm Clouds";
		case NAUTILUS:
			return "&9Nautilus";
		case WATER_DROP:
			return "&bRain";
		case DOLPHIN:
			return "&bBlue Dots";
		case REDSTONE:
			return "&cColorful Dust";
		default:
			return "0";
		}
	}

	public static ItemStack particleToItemStack(Particle p, boolean unlocked) {
		switch (p) {
		case FLAME:
			return new ItemStack(unlocked ? Material.CAMPFIRE : Material.ORANGE_STAINED_GLASS_PANE);
		case FALLING_LAVA:
			return new ItemStack(unlocked ? Material.LAVA_BUCKET : Material.ORANGE_STAINED_GLASS_PANE);
		case FALLING_WATER:
			return new ItemStack(unlocked ? Material.WATER_BUCKET : Material.BLUE_STAINED_GLASS_PANE);
		case TOTEM:
			return new ItemStack(unlocked ? Material.LIME_DYE : Material.LIME_STAINED_GLASS_PANE);
		case NOTE:
			return new ItemStack(unlocked ? Material.MUSIC_DISC_BLOCKS : Material.GREEN_STAINED_GLASS_PANE);
		case CRIT:
			return new ItemStack(unlocked ? Material.GLOWSTONE_DUST : Material.YELLOW_STAINED_GLASS_PANE);
		case CRIT_MAGIC:
			return new ItemStack(unlocked ? Material.LIGHT_BLUE_DYE : Material.CYAN_STAINED_GLASS_PANE);
		case ENCHANTMENT_TABLE:
			return new ItemStack(unlocked ? Material.ENCHANTING_TABLE : Material.WHITE_STAINED_GLASS_PANE);
		case SMOKE_NORMAL:
			return new ItemStack(unlocked ? Material.COAL : Material.GRAY_STAINED_GLASS_PANE);
		case DAMAGE_INDICATOR:
			return new ItemStack(unlocked ? Material.RED_DYE : Material.RED_STAINED_GLASS_PANE);
		case COMPOSTER:
			return new ItemStack(unlocked ? Material.SCUTE : Material.GREEN_STAINED_GLASS_PANE);
		case PORTAL:
			return new ItemStack(unlocked ? Material.MAGENTA_DYE : Material.MAGENTA_STAINED_GLASS_PANE);
		case SPELL_WITCH:
			return new ItemStack(unlocked ? Material.PURPLE_DYE : Material.PURPLE_STAINED_GLASS_PANE);
		case SPELL_MOB:
			return new ItemStack(unlocked ? Material.POTION : Material.BLACK_STAINED_GLASS_PANE);
		case SPELL_INSTANT:
			return new ItemStack(unlocked ? Material.NETHER_STAR : Material.WHITE_STAINED_GLASS_PANE);
		case HEART:
			return new ItemStack(unlocked ? Material.REDSTONE : Material.MAGENTA_STAINED_GLASS_PANE);
		case VILLAGER_ANGRY:
			return new ItemStack(unlocked ? Material.FIRE_CHARGE : Material.GRAY_STAINED_GLASS_PANE);
		case NAUTILUS:
			return new ItemStack(unlocked ? Material.ENDER_PEARL : Material.PURPLE_STAINED_GLASS_PANE);
		case WATER_DROP:
			return new ItemStack(unlocked ? Material.WATER_BUCKET : Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		case DOLPHIN:
			return new ItemStack(unlocked ? Material.LIGHT_BLUE_DYE : Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		case REDSTONE:
			return new ItemStack(unlocked ? Material.REDSTONE : Material.RED_STAINED_GLASS_PANE);
		default:
			return new ItemStack(Material.GRASS_BLOCK);
		}
	}

	public static ItemStack particleToItemStack(Particle p) {
		return particleToItemStack(p, true);

	}

	public static MysticPlayer getMysticPlayer(Player player) {
		return getMysticPlayer(player.getUniqueId());
	}

	public static MysticPlayer getMysticPlayer(UUID uid) {

		if (mplayers.containsKey(uid)) {
			return mplayers.get(uid);
		}

		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM MysticPlayers WHERE UUID='" + uid.toString() + "';");
		int a = 0;
		try {
			while (rs.next()) {
				a = a + 1;
				MysticPlayer mp = new MysticPlayer(uid);
				mp.setBalance(Integer.parseInt(rs.getString("BALANCE")));
				mp.setGems(Integer.parseInt(rs.getString("GEMS")));
				mp.setLevel(Integer.parseInt(rs.getString("LEVEL")));
				mplayers.put(uid, mp);
				CoreUtils.debug("Registered MysticPlayer: " + uid);
				return mp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (a == 0) {
			CoreUtils.sendInsert("INSERT INTO MysticPlayers (UUID, BALANCE, GEMS, LEVEL) VALUES ('" + uid.toString()
					+ "','0','0','1');");

		}

		if (Bukkit.getPlayer(uid) == null) {
			Bukkit.getConsoleSender().sendMessage(
					CoreUtils.colorize("&cA new MysticPlayer was requested while the associated player was offline."));
			return null;
		}

		MysticPlayer player = new MysticPlayer(uid);

		mplayers.put(uid, player);

		saveMysticPlayer(player);

		return player;

	}

	public static void saveMysticPlayer(Player player) {
		saveMysticPlayer(getMysticPlayer(player));
	}

	public static void saveMysticPlayer(MysticPlayer player) {
		CoreUtils
				.sendUpdate("UPDATE MysticPlayers SET BALANCE='" + player.getBalance() + "',LEVEL='" + player.getLevel()
						+ "',GEMS='" + player.getGems() + "' WHERE UUID='" + player.getUUID().toString() + "';");
	}

	public static void updateMysticPlayer(Player player) {
		updateMysticPlayer(player.getUniqueId());
	}

	private static void updateMysticPlayer(UUID uid) {
		ResultSet rs = CoreUtils.sendQuery("SELECT * FROM MysticPlayers WHERE UUID='" + uid.toString() + "';");
		int a = 0;
		try {
			while (rs.next()) {
				a = a + 1;
				MysticPlayer mp = new MysticPlayer(uid);
				mp.setBalance(Integer.parseInt(rs.getString("BALANCE")));
				mp.setGems(Integer.parseInt(rs.getString("GEMS")));
				mp.setLevel(Integer.parseInt(rs.getString("LEVEL")));
				mplayers.put(uid, mp);
				CoreUtils.debug("Registered MysticPlayer: " + uid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static double getMoneyFormat(double amount) {
		return (Double.parseDouble(new DecimalFormat("#0.00").format(Double.valueOf(amount))));
	}


}
