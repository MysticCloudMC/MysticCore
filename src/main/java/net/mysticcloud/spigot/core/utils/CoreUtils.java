package net.mysticcloud.spigot.core.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.json2.JSONObject;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.milkbowl.vault.economy.Economy;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.runnables.GenericCooldownRunnable;
import net.mysticcloud.spigot.core.utils.admin.AlertType;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.FoodInfo;
import net.mysticcloud.spigot.core.utils.admin.Holiday;
import net.mysticcloud.spigot.core.utils.admin.VaultAPI;
import net.mysticcloud.spigot.core.utils.chat.CoreChatUtils;
import net.mysticcloud.spigot.core.utils.levels.LevelUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;
import net.mysticcloud.spigot.core.utils.skulls.CustomSkull;
import net.mysticcloud.spigot.core.utils.skulls.SkullUtils;
import net.mysticcloud.spigot.core.utils.sql.IDatabase;
import net.mysticcloud.spigot.core.utils.sql.SQLDriver;
import net.mysticcloud.spigot.core.utils.sql.SQLUtils;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;
import net.mysticcloud.spigot.core.utils.warps.WarpUtils;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CoreUtils {

//	private static IDatabase db;
//	static IDatabase fdb;
	private static boolean connected = false;
	private static Holiday holiday = Holiday.NONE;
	static Map<UUID, Scoreboard> scoreboards = new HashMap<>();
	public static Map<UUID, ParticleFormat> particles = new HashMap<>();

	public static Map<Location, ParticleFormat> blockparticles = new HashMap<>();
	public static Map<Location, ParticleFormat> blockparticles__add = new HashMap<>();

	public static List<UUID> particles__remove = new ArrayList<>();
	public static List<Location> blockparticles__remove = new ArrayList<>();

	public static String prefix = "MysticCloud";
	public static String fullPrefix = colorize("&3&l" + prefix + " &7>&f ");

	private static boolean handleDamage = true;

	private static Map<String, String> prefixes = new HashMap<>();

	private static Location spawn = null;

	private static Map<String, String> playerlist = new HashMap<>();

	private static boolean coreBoard = true;

	private static File itemfile = new File(Main.getPlugin().getDataFolder() + "/Items");
	private static List<FileConfiguration> itemFiles = new ArrayList<>();
	private static Map<String, ItemStack> items = new HashMap<>();
	private static Map<String, ItemStack> foods = new HashMap<>();
	private static Map<String, FoodInfo> food = new HashMap<>();
	private static Map<String, String> variables = new HashMap<>();
	private static Map<String, String> messages = new HashMap<>();
	public static Map<UUID, List<TimedPerm>> timedPerms = new HashMap<>();
	public static Map<UUID, String> offlineTimedUsers = new HashMap<>();

	public static List<String> ecoaccounts = new ArrayList<>();
	public static double startingBalance = 100.00;
	private static Economy economy = null;

	private static DecimalFormat df = new DecimalFormat("0.00");

	private static Map<UUID, UUID> controllers = new HashMap<>();
	static ItemStack gem = new ItemStack(Material.SUNFLOWER);

	private static List<String> voidWorlds = new ArrayList<>();

	private static String serverName = "Server Name Here";
	private static String scoreboardTitle = "   &3&lMYSTIC&7&lCLOUD   &8%emoticon:BAR_2%   &a&l%server   ";
	private static List<String> scoreboardInfo = new ArrayList<>();

	static Map<String, List<UUID>> genericCooldowns = new HashMap<>();

	public static void start() {

		df.setRoundingMode(RoundingMode.DOWN);

		ItemMeta gemeta = gem.getItemMeta();
		gemeta.setDisplayName(colorize("&aGem"));
		gem.setItemMeta(gemeta);

		setupEconomy();

		LevelUtils.start();

		prefixes.put("root", fullPrefix);

		prefixes.put("sql", colorize("&3&lSQL &7>&f "));
		prefixes.put("settings", colorize("&a&lSettings &7>&f "));
		prefixes.put("items", colorize("&a&lItems &7>&a "));
		prefixes.put("kits", colorize("&3&lKits &7>&f "));
		prefixes.put("pets", colorize("&e&lPets &7>&f "));
		prefixes.put("admin", colorize("&c&lAdmin &7>&f "));
		prefixes.put("debug", colorize("&c&lDebug &7>&f "));
		prefixes.put("warps", colorize("&b&lWarps &7>&f "));
		prefixes.put("eco", colorize("&6&lEconomy &7>&f "));
		prefixes.put("error", colorize("&c&lError &7>&f "));
		prefixes.put("account", colorize("&a&lAccount &7>&f "));
		prefixes.put("teleport", colorize("&3&lTeleport &7>&f "));
		prefixes.put("punishments", colorize("&4&lInfringements &7>&f "));
		prefixes.put("afk", colorize("&a&lAFK &f>&7 "));
		prefixes.put("reports", colorize("&c&lReports &7>&f "));
		prefixes.put("vote", colorize("&a&lVote &7>&a "));
		prefixes.put("portals", colorize("&9&lPortals &f> &7"));

		messages.put("noperm", prefixes("root") + "You don't have permission to use that command.");
		messages.put("onlyplayer", prefixes("root") + "That is a player only command.");

		if (Main.getPlugin().getConfig().isSet("TimedUsers")) {
			for (String uid : Main.getPlugin().getConfig().getStringList("TimedUsers")) {
				List<String> perms = Main.getPlugin().getConfig().getStringList("TimedPerm." + uid);
				for (String data : perms) {
					addExistingTimedPermission(UUID.fromString(uid), data.split(":")[2], data.split(":")[3],
							Long.parseLong(data.split(":")[0]), Long.parseLong(data.split(":")[1]));
				}
			}
		}
		connected = true;
		try {
			if (SQLUtils.createDatabase("minecraft",
					new IDatabase(SQLDriver.MYSQL, "sql.mysticcloud.net", "Minecraft", 3306, "blue", "g3&clly"))) {
				System.out.println(prefixes.get("sql") + "Successfully connected to MySQL (Minecraft).");
			} else {
				System.out.println(prefixes.get("sql") + "Error connecting to MySQL (Minecraft).");
				if (SQLUtils.createDatabase("minecraft", new IDatabase(SQLDriver.SQLITE, "Minecraft"))) {
					System.out.println(prefixes.get("sql") + "Successfully connected to contingency SQL (Minecraft).");
				} else {
					connected = false;
					System.out.println(prefixes.get("sql") + "Error connecting to contingency SQL (Minecraft).");
				}
			}

			if (SQLUtils.createDatabase("forums",
					new IDatabase(SQLDriver.MYSQL, "sql.mysticcloud.net", "forums", 3306, "blue", "g3&clly"))) {
				System.out.println(prefixes.get("sql") + "Successfully connected to MySQL (Forums).");
			} else {
				connected = false;
				System.out.println(prefixes.get("sql") + "Error connecting to MySQL (Forums).");
			}
		} catch (SQLException e1) {
			System.out.println(prefixes.get("sql") + "Error connecting to SQL (ALL).");
			e1.printStackTrace();
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
				e.printStackTrace();
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

			fc.set("SuperSword.Id", "DIAMOND_SWORD");
			fc.set("SuperSword.Data", "570");
			fc.set("SuperSword.Display", "&6Super Sword");

			try {
				fc.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			itemFiles.add(fc);

		}

		if (itemFiles.size() == 0) {

			File file = new File(itemfile.getPath() + "/itemDefault.yml");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

			fc.set("SuperSword.Id", "DIAMOND_SWORD");
			fc.set("SuperSword.Data", "570");
			fc.set("SuperSword.Options.Display", "&6Super Sword");

			try {
				fc.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			itemFiles.add(fc);

		}

		if (Main.getPlugin().getConfig().isSet("Scoreboard.Title")) {
			scoreboardTitle = Main.getPlugin().getConfig().getString("Scoreboard.Title");
		} else {
			Main.getPlugin().getConfig().set("Scoreboard.Title", scoreboardTitle);
		}

		if (Main.getPlugin().getConfig().isSet("Scoreboard.Info")) {
			scoreboardInfo = Main.getPlugin().getConfig().getStringList("Scoreboard.Info");
		} else {
			scoreboardInfo.add("&f");
			scoreboardInfo.add("&a&lPLAYER&8:");
			scoreboardInfo.add("&7 Rank&8: &a%prefix");
			scoreboardInfo.add("&7 Tag&8: &a%tag");
			scoreboardInfo.add("&7 Balance&8: &a$%bal");
			scoreboardInfo.add("&7 Level&8: &a%lvl");
			scoreboardInfo.add("&a");
			scoreboardInfo.add("&a&lSERVER&8:");
			scoreboardInfo.add("&7 Online&8: &a%online");
			scoreboardInfo.add("&7 Time&8: &a%fulltime");
			Main.getPlugin().getConfig().set("Scoreboard.Info", scoreboardInfo);
		}

		if (Main.getPlugin().getConfig().isSet("Name")) {
			serverName = Main.getPlugin().getConfig().getString("Name");
		} else {
			Main.getPlugin().getConfig().set("Name", serverName);

		}

		Main.getPlugin().saveConfig();

		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {

				if (Main.getPlugin().getConfig().isSet("SPAWN")
						&& Main.getPlugin().getConfig().getString("SPAWN") != "")
					spawn = decryptLocation(Main.getPlugin().getConfig().getString("SPAWN"));

				if (Main.getPlugin().getConfig().isSet("PlayerList.Header")) {
					playerlist.put("header",
							CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.Header")));
				}
				if (Main.getPlugin().getConfig().isSet("PlayerList.PlayerName")) {
					playerlist.put("name",
							CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.PlayerName")));
				}
				if (Main.getPlugin().getConfig().isSet("PlayerList.Footer")) {
					playerlist.put("footer",
							CoreUtils.colorize(Main.getPlugin().getConfig().getString("PlayerList.Footer")));
				}
			}

		}, 5 * 20);

		if (playerList("header").equals("")) {
			playerlist.put("header", "&3&lMystic&7&lCloud &f&lNetwork");
		}
		if (playerList("name").equals("")) {
			playerlist.put("name", "%prefix%nitro%player");
		}
		if (playerList("footer").equals("")) {
			playerlist.put("footer", "&3play.mysticcloud.net");
		}

		WarpUtils.registerWarps();

//		MysticEntityUtils.registerEntities();

	}

	public static String getServerName() {
		return serverName;
	}

	public static Set<UUID> getControllers() {
		return controllers.keySet();
	}

	public static Player controllingWho(Player controller) {
		return controllers.containsKey(controller.getUniqueId()) ? null
				: Bukkit.getPlayer(controllers.get(controller.getUniqueId()));
	}

	public static void control(Player controller, Player player) {

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(Main.getPlugin(), controller);
		}
		controllers.put(controller.getUniqueId(), player.getUniqueId());
	}

	public static void leaveControl(Player controller) {
		controllers.remove(controller.getUniqueId());
		controller.teleport(CoreUtils.getSpawnLocation());
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(Main.getPlugin(), controller);
		}
	}

	public static void addVoidWorld(String worldname) {
		voidWorlds.add(worldname);
	}

	public static List<String> getVoidWorlds() {
		return voidWorlds;
	}

	public static void setTag(Player player, String key) {
		CustomTag.setTag(player, key);
	}

	public static String getTag(Player player) {
		return CustomTag.getTag(player);
	}

	public static void removeTag(Player player) {
		CustomTag.removeTag(player);
	}

	public static void addCoreMessage(String name, String message) {
		messages.put(name, colorize(message));
	}

	public static String getCoreMessage(String name) {
		return messages.get(name);
	}

	public static Item spawnGem(Location loc) {
		Item item = loc.getWorld().dropItem(loc, gem);

		item.setCustomName(colorize(gem.getItemMeta().getDisplayName()));
		item.setCustomNameVisible(true);

		return item;

	}

	public static ItemStack getGemItem() {
		return gem;
	}

	public static ItemStack createUnstackableItemStack(Material type) {
		ItemStack i = new ItemStack(type);
		ItemMeta im = i.getItemMeta();
		List<String> list = new ArrayList<>();
		list.add(getRandom().nextInt() + "");
		im.setLore(list);
		i.setItemMeta(im);
		return i;
	}

	public static ItemStack makeItemStackUnstackable(ItemStack i) {
		ItemMeta im = i.getItemMeta();
		List<String> list = new ArrayList<>();
		list.add(getRandom().nextInt() + "");
		im.setLore(list);
		i.setItemMeta(im);
		return i;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(/* Collections.reverseOrder */(Map.Entry.comparingByValue()));

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static UUID LookupUUID(String player) {
		UUID uid = null;
		ResultSet rs = sendQuery("SELECT * FROM PlayerStats");
		try {
			while (rs.next()) {
				if (rs.getString("NAME").equalsIgnoreCase(player)) {
					uid = UUID.fromString(rs.getString("UUID"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return uid;

	}

	public static int LookupForumID(String player) {
		return LookupForumID(LookupUUID(player));
	}

	public static int LookupForumID(UUID uuid) {
		int uid = 0;
		ResultSet rs = sendQuery("SELECT * FROM MysticPlayers WHERE FORUMS_NAME IS NOT NULL");
		try {
			while (rs.next()) {
				if (rs.getString("UUID").equalsIgnoreCase(uuid.toString())) {
					uid = Integer.parseInt(rs.getString("FORUMS_NAME"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return uid;

	}

	@Deprecated
	public static UUID LookupUUID(int forumId) {
		UUID uid = null;
		ResultSet rs = sendQuery("SELECT * FROM MysticPlayers");
		try {
			while (rs.next()) {
				if (rs.getString("FORUMS_NAME") != null && rs.getString("FORUMS_NAME").equalsIgnoreCase(forumId + "")) {
					uid = UUID.fromString(rs.getString("UUID"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return uid;

	}

	@Deprecated
	public static long LookupLastSeen(String player) {
		long date = 0;
		ResultSet rs = sendQuery("SELECT * FROM PlayerStats");
		try {
			while (rs.next()) {
				if (rs.getString("NAME").equalsIgnoreCase(player)) {
					date = Long.parseLong(rs.getString("DATE"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return date;

	}

	@Deprecated
	public static void teleportPlayer(String sender, Player player, Player other) {

		TeleportUtils.teleportPlayer(sender, player, other);
	}

	@Deprecated
	public static void teleportPlayer(Player player, Player other) {
		TeleportUtils.teleportPlayer(player, other);
	}

	@Deprecated
	public static void teleportPlayer(Player player, Player other, boolean sender) {
		TeleportUtils.teleportPlayer(player, other, sender);
	}

	public static long LookupLastSeen(UUID player) {
		long date = 0;
		ResultSet rs = sendQuery("SELECT * FROM PlayerStats");
		try {
			while (rs.next()) {
				if (rs.getString("UUID").equalsIgnoreCase(player.toString())) {
					date = Long.parseLong(rs.getString("DATE"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return date;

	}

	public static String lookupUsername(UUID uid) {
		String user = "";
		ResultSet rs = sendQuery("SELECT * FROM PlayerStats");
		try {
			while (rs.next()) {
				if (uid.toString().equalsIgnoreCase(rs.getString("UUID"))) {
					user = (rs.getString("NAME"));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;

	}

	public static IDatabase getForumsDatabase() {
		return SQLUtils.getDatabase("forums");

	}

	public static String lookupWebname(UUID uid) {
		String name = "";
		try {
			ResultSet rs = getForumsDatabase().query("SELECT * FROM Users WHERE REGISTERED='true'");
			try {
				while (rs.next()) {
					if (rs.getString("MINECRAFT_UUID").equalsIgnoreCase(uid.toString())) {
						name = (rs.getString("USERNAME"));
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs.close();

		} catch (SQLException e1) {
			System.out.println("Not connected to SQL");
			e1.printStackTrace();
		}

		return name;

	}

	public static Economy getEconomy() {
		return economy;
	}

	public static void setScoreboard(Player pl) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

		Objective obj = board.registerNewObjective("title", ObjectiveType.DUMMY.getName(),
				CoreUtils.colorize(PlaceholderUtils.markup(PlaceholderUtils.replace(pl, scoreboardTitle))));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		List<String> usedColors = new ArrayList<>();
		int i = scoreboardInfo.size() + 1;
		for (String s : scoreboardInfo) {
			String color = "";
			boolean go = true;
			while (go) {
				color = org.bukkit.ChatColor.values()[new Random().nextInt(org.bukkit.ChatColor.values().length)] + ""
						+ org.bukkit.ChatColor.values()[new Random().nextInt(org.bukkit.ChatColor.values().length)];
				if (!usedColors.contains(color)) {
					usedColors.add(color);
					go = false;
				}
			}
			Team team = board.registerNewTeam(i + "");
			team.addEntry(color);
			team.setPrefix(CoreUtils.colorize(PlaceholderUtils.replace(pl, s)));
			obj.getScore(color).setScore(i);
			i = i - 1;

		}

		scoreboards.put(pl.getUniqueId(), board);
		pl.setScoreboard(board);

	}

	public static void updateScoreboard(Player pl) {
		if (scoreboards.containsKey(pl.getUniqueId())) {
			Scoreboard board = scoreboards.get(pl.getUniqueId());
			int i = scoreboardInfo.size() + 1;
			for (String s : scoreboardInfo) {
				board.getTeam(i + "").setPrefix(CoreUtils.colorize(PlaceholderUtils.replace(pl, s)));
				i = i - 1;
			}
//			MysticPlayer player = MysticAccountManager.getMysticPlayer(pl);
//
//			board.getTeam("rankTracker")
//					.setPrefix(CoreUtils.colorize(PlaceholderUtils.replace(pl, "&7 Rank&8: &b%prefix")));
//			board.getTeam("tagTracker").setPrefix(CoreUtils.colorize(PlaceholderUtils.replace(pl, "&7 Tag&8: &b%tag")));
//
////			board.getTeam("gemCounter").setPrefix(CoreUtils.colorize("&a" + Emoticons.GEMS + "&f " + player.getGems()));
//			board.getTeam("balanceCounter").setPrefix(CoreUtils.colorize("&7 Balance&8: &b$" + player.getBalance()));
//			board.getTeam("levelCounter").setPrefix(CoreUtils.colorize("&7 Level&8: &b " + player.getLevel()));
//			board.getTeam("onlineCounter")
//					.setPrefix(CoreUtils.colorize("&7 Online&8: &b " + Bukkit.getOnlinePlayers().size()));
//			board.getTeam("timeTracker")
//					.setPrefix(CoreUtils.colorize("&7 Time&8: &b" + PlaceholderUtils.replace(pl, "%fulltime")));
		}
	}

	public static void removeScoreboard(Player pl) {
		scoreboards.remove(pl.getUniqueId());
		pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
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
			rs.close();
		} catch (SQLException e) {
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

	public static void particlesOff(UUID uid) {
		particles__remove.add(uid);
	}

	public static void particles(UUID uid, ParticleFormatEnum format) {
		particles.put(uid, format.formatter());
	}

	public static ParticleFormat particles(UUID uid) throws NullPointerException {
		return particles.containsKey(uid) ? particles.get(uid) : null;
	}

	public static String encryptLocation(Location loc) {
		String r = loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
				+ loc.getPitch() + ":" + loc.getYaw();
		r = r.replaceAll("\\.", ",");
		r = "location:" + r;
		return r;
	}

	public static Location decryptLocation(String s) {
		debug("Decrypting Location: " + s);
		if (s.startsWith("location:"))
			s = s.replaceAll("location:", "");

		if (s.contains(","))
			s = s.replaceAll(",", ".");
		String[] args = s.split(":");
		Location r = new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
				Double.parseDouble(args[3]));
		if (args.length >= 5) {
			r.setPitch(Float.parseFloat(args[4]));
			r.setYaw(Float.parseFloat(args[5]));
		}
		return r;
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
			TeleportUtils.teleport(player, spawn, true, false);
			player.teleport(spawn);
			player.sendMessage(fullPrefix + reason.message());
		} catch (IllegalArgumentException ex) {
			// spawn.getWorld().loadChunk(spawn.getChunk());
			// player.teleport(spawn);
			// player.sendMessage(fullPrefix + reason.message());
		}

	}

	public static String prefixes(String key) {
		if (prefixes.get(key) == null)
			prefixes.put(key, colorize("&e&l" + key.toUpperCase().substring(0, 1)
					+ key.toLowerCase().substring(1, key.length()) + " &7>&f "));
		return prefixes.get(key);
	}

	public static Map<String, ItemStack> getCachedItems() {

		return items;
	}

	public static void sendPluginMessage(Player player, String channel, String... arguments) {
		if (arguments == null | arguments.length == 0)
			return;
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String s : arguments) {
			out.writeUTF(s);
		}
		player.sendPluginMessage(Main.getPlugin(), channel, out.toByteArray());
	}

	@SuppressWarnings("unused")
	private static boolean testSQLConnection() throws SQLException {
		return new IDatabase(SQLDriver.MYSQL, "localhost", "Minecraft", 3306, "mysql", "v4pob8LW").init();

	}

	public static String colorize(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (message.contains("#")) {

			for (String s : message.split("#")) {
				try {
					message = message.replace("#" + s.substring(0, 6),
							net.md_5.bungee.api.ChatColor.of(Color.decode("#" + s.substring(0, 6))) + "");

				} catch (Exception ex) {
				}
			}
		}
		return message;

	}

	public static Color generateColor(double seed, double frequency) {
		return generateColor(seed, frequency, 100);
	}

	public static Color generateColor(double seed, double frequency, int amp) {

		if (amp > 127)
			amp = 127;
		int peak = 255 - amp;
		int red = (int) (Math.sin(frequency * (seed) + 0) * amp + peak);
		int green = (int) (Math.sin(frequency * (seed) + 2 * Math.PI / 3) * amp + peak);
		int blue = (int) (Math.sin(frequency * (seed) + 4 * Math.PI / 3) * amp + peak);
		if (red > 255)
			red = 255;
		if (green > 255)
			green = 255;
		if (blue > 255)
			blue = 255;

		return new Color(red, green, blue);
	}

	@SuppressWarnings("deprecation")
	public static String getTime() {

		String min = new Date().getMinutes() + "";
		String hr = new Date().getHours() + "";
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
		for (Entry<String, ItemStack> entry : foods.entrySet()) {
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

	public static ResultSet sendQuery(String query) {
		if (!connected) {
			alert(AlertType.HIGH, "SQL Not Connected! &7(SNC001)");

		}
		return SQLUtils.getDatabase("minecraft").query(query);
	}

	public static Integer sendUpdate(String query) throws NullPointerException {
		if (!connected)
			alert(AlertType.HIGH, "SQL Not Connected! &7(SNC002)");
		return SQLUtils.getDatabase("minecraft").update(query);
	}

	public static boolean sendInsert(String query) throws NullPointerException {
		if (!connected)
			alert(AlertType.HIGH, "SQL Not Connected! &7(SNC003)");
		return SQLUtils.getDatabase("minecraft").input(query);
	}

	public static String toString(String[] args) {
		String s = "";
		for (String a : args)
			s = s == "" ? a : s + " " + a;
		return s;
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.MONTH);
	}

	public static int getDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static void setHoliday(Holiday hol) {
		holiday = hol;
	}

	public static Holiday getHoliday() {
		return holiday;
	}

	public static Date getDate() {
		return new Date();
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
				if (group.getName().equals(player.getUniqueId().toString()))
					continue;

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

	public static void debug(Object obj) {
		debug(obj + "");
	}

	public static void debug(String message) {

		DebugUtils.debug(message);

//		if (debug)
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				if (player.hasPermission("mysticcloud.admin")) {
//					player.sendMessage(colorize(prefixes("debug") + message));
//				}
//			}
//		Bukkit.getConsoleSender().sendMessage(colorize(prefixes("debug") + message));
	}

	@SuppressWarnings("deprecation")
	public static String encryptItemStack(ItemStack i) {
		try {
			return i.getType() + ":" + i.getAmount() + ":" + i.getDurability();
		} catch (NullPointerException ex) {
			return "AIR:1:0";
		}

	}

	@Deprecated
	public static ItemStack decryptItemStack(String s) {
		String[] d = s.split(":");
		ItemStack i = s
				.contains(":")
						? (d.length >= 2
								? (d.length == 2 ? new ItemStack(Material.valueOf(d[0]), 1, Short.parseShort(d[1]))
										: new ItemStack(Material.valueOf(d[0]), Integer.parseInt(d[1]),
												Short.parseShort(d[2])))
								: new ItemStack(Material.valueOf(d[0])))
						: new ItemStack(Material.valueOf(s));
		ItemMeta m = i.getItemMeta();
		String j = "";
		if (d.length >= 3)
			for (int f = 3; f != d.length; f++) {
				j = j == "" ? d[f] : j + ":" + d[f];
			}
		JSONObject json = new JSONObject("{}");
		if (j.contains("{") && j.contains("}")) {
			json = new JSONObject(j);
		}

		if (!json.isEmpty()) {
			if (json.has("PotionMeta")) {
				PotionMeta meta = (PotionMeta) m;
				meta.setBasePotionData(new PotionData(PotionType.valueOf(json.getString("PotionMeta"))));
			}
		}
		i.setItemMeta(m);
		return i;

	}

	public static void sendZachsMessage(String sender, String message) {
		CoreChatUtils.sendChannelChat("zachs", message);
	}

//	public static void enableScoreboard(Player player) {
//		if (coreBoard) {
//			if (!MysticAccountManager.getMysticPlayer(player).getSetting(PlayerSettings.SIDEBAR)
//					.equalsIgnoreCase("true")) {
//				return;
//			}
//			Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
//			Objective objective = scoreboard.registerNewObjective("sidebar", "dummy",
//					colorize("        &3&lMystic&f&lCloud        "));
//			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//			registerSidebarList();
//			for (Entry<Integer, String> entry : sidebar.entrySet()) {
//
//				for (String s : scoreboard.getEntries()) {
//
//					if (entry.getKey() == objective.getScore(s).getScore()) {
//						if (!objective.getScore(s).getEntry().equals(colorize(entry.getValue()))) {
//							scoreboard.resetScores(s);
//						}
//					} else
//						continue;
//				}
//				objective.getScore(colorize(PlaceholderUtils.replace(player, entry.getValue())))
//						.setScore(entry.getKey());
//
//			}
//
//			player.setScoreboard(scoreboard);
//		}
//	}

	@SuppressWarnings("deprecation")
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

			if (i.getType().equals(Material.PLAYER_HEAD)) {
				if (item.isSet(name + ".Options.SkullOwner")) {
					if (item.isSet(name + ".Options.SkullOwner.Name")) {
						CustomSkull cs = SkullUtils.getCustomSkull(item.getString(name + ".Options.SkullOwner.Name"));
						i = modifyHead(i, cs.getID(), cs.getValue());
						a = i.getItemMeta();
					}
					if (item.isSet(name + ".Options.SkullOwner.Value") && item.isSet(name + ".Options.SkullOwner.ID")) {
						i = modifyHead(i, item.getString(name + ".Options.SkullOwner.ID"),
								item.getString(name + ".Options.SkullOwner.Value"));
						a = i.getItemMeta();
					}
				}
			}

			if (item.isSet(name + ".Data"))
				i.setDurability(Short.parseShort(item.getString(name + ".Data")));
			a.setDisplayName(colorize(item.getString(name + ".Options.Display",
					ChatColor.RESET + (i.getType() + "").substring(0, 1).toUpperCase()
							+ (i.getType() + "").substring(1, (i.getType() + "").length()).toLowerCase())));

			if (item.isSet(name + ".Options.Unbreakable"))
				a.setUnbreakable(Boolean.parseBoolean(item.getString(name + ".Options.Unbreakable")));

			if (item.isSet(name + ".Options.Enchantments")) {

				for (String b : item.getStringList(name + ".Options.Enchantments")) {
					for (Enchantment en : Enchantment.values()) {
						if (en.getName().equalsIgnoreCase(b.split(":")[0])) {
							a.addEnchant(en, Integer.parseInt(b.split(":")[1]), true);
							break;
						}
					}

				}
			}

			if (item.isSet(name + ".Options.Lore")) {
				List<String> lore = a.hasLore() ? a.getLore() : new ArrayList<>();
				if (item.get(name + ".Options.Lore") instanceof List<?>) {
					for (String s : item.getStringList(name + ".Options.Lore")) {
						lore.add(colorize(s));
					}
				}
				if (item.get(name + ".Options.Lore") instanceof String) {
					lore.add(colorize(item.getString(name + ".Options.Lore")));
				}
				if (!lore.isEmpty())
					a.setLore(lore);
			}

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

			if (item.isSet(name + ".MysticEnhancements.FireDamage")) {
				List<String> lore = a.hasLore() ? a.getLore() : new ArrayList<>();
				lore.add(colorize("&cFire&7 Damage: &c&l" + item.get(name + ".MysticEnhancements.FireDamage")));
				a.setLore(lore);
			}
			if (item.isSet(name + ".MysticEnhancements.FrostDamage")) {
				List<String> lore = a.hasLore() ? a.getLore() : new ArrayList<>();
				lore.add(colorize("&bFrost&7 Damage: &b&l" + item.get(name + ".MysticEnhancements.FrostDamage")));
				a.setLore(lore);
			}

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

				AttributeModifier am = new AttributeModifier(UUID.randomUUID(), "Boots Protection",
						item.getDouble(name + ".Attributes.Boots.Protection"), Operation.ADD_NUMBER,
						EquipmentSlot.FEET);
				a.addAttributeModifier(Attribute.GENERIC_ARMOR, am);

			}

			// Attribute.
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

			if (item.isSet(name + ".Options.Hide"))
				if (item.get(name + ".Options.Hide") != "All")
					for (String s : item.getStringList(name + ".Options.Hide"))
						a.addItemFlags(ItemFlag.valueOf("HIDE_" + s.toUpperCase()));
				else
					for (ItemFlag flag : ItemFlag.values())
						a.addItemFlags(flag);

		}
		a.setDisplayName(a.hasDisplayName() ? a.getDisplayName() : CoreUtils.colorize("&cERROR"));
		i.setItemMeta(a);
		if (!found)

		{
			debug("Item was not found.");

			return i.clone();
		}

		debug("Item loaded from config, and saved to cache.");
		if (food) {
			foods.put(name, i.clone());
			debug("Item " + name + " was food.");
		}
		items.put(name, i.clone());

		if (food)
			CoreUtils.food.put(name, info);
		return i.clone();
	}

	public static boolean connected() {
		return connected;
	}

	public static PageResult pagify(List<String> things, int items) {
		return pagify(things, items, 1);
	}

	public static PageResult pagify(List<String> things, int items, int page) {

		List<String> rtn = new ArrayList<>();
		int totalposts = 0;
		int pagetracker = ((page - 1) * items) - 1;

		for (String s : things) {
			// REFERENCE for (int i = (page - 1) * pageResult; i < page * pageResult; i++)
			totalposts = totalposts + 1;
			pagetracker = pagetracker + 1;
			if (pagetracker < page * items && pagetracker > ((page - 1) * items) - 1) {
				rtn.add(s);
			}

		}
		int pages = ((int) (totalposts / items));
//		if (pages > 1) {
//			if (page == 1) {
//				// can't go back
//			} else {
//				// can go back
//			}
//			// page / pages
//			if (page == pages) {
//				// can't go forward
//			} else {
//				// can go forward
//			}
//
//		}

		return new PageResult(rtn, page, pages);

	}

	public static String formatMessage(String message, String... values) {
		int i = 0;
		String string = message;
		while (string.contains("@")) {
			string = string.replaceFirst("@", values[i]);
			i = i + 1;
		}
		return string;
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
		case END_ROD:
			return "&fWhite Dots";
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
			return new ItemStack(unlocked ? Material.SUNFLOWER : Material.YELLOW_STAINED_GLASS_PANE);
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
		case END_ROD:
			return new ItemStack(unlocked ? Material.SNOWBALL : Material.WHITE_STAINED_GLASS_PANE);
		default:
			return new ItemStack(Material.GRASS_BLOCK);
		}
	}

	public static ItemStack particleToItemStack(Particle p) {
		return particleToItemStack(p, true);

	}

	public static List<String> getPageResults(List<String> rules, int page, int pageResult) {
		List<String> rturn = new ArrayList<>();
		for (int i = (page - 1) * pageResult; i < page * pageResult; i++) {
			if (i < rules.size())
				rturn.add(rules.get(i));
		}
		return rturn;
	}

	public static double getMoneyFormat(double amount) {
		return (Double.parseDouble(df.format(Double.valueOf(amount))));
	}

	public static String formatDate(long ms, String tcolor, String ncolor) {

		int l = (int) (ms / 1000);
		int sec = l % 60;
		int min = (l / 60) % 60;
		int hours = ((l / 60) / 60) % 24;
		int days = (((l / 60) / 60) / 24) % 7;
		int weeks = (((l / 60) / 60) / 24) / 7;

		if (weeks > 0) {
			return ncolor + weeks + tcolor + " weeks" + (days > 0 ? ", " + ncolor + days + tcolor + " days" : "")
					+ (hours > 0 ? ", " + ncolor + hours + tcolor + " hours" : "")
					+ (min > 0 ? ", " + ncolor + min + tcolor + " minutes" : "")
					+ (sec > 0 ? ", and " + ncolor + sec + tcolor + " " + (sec == 1 ? "second" : "seconds") : "");
		}
		if (days > 0) {
			return ncolor + days + tcolor + " days" + (hours > 0 ? ", " + ncolor + hours + tcolor + " hours" : "")
					+ (min > 0 ? ", " + ncolor + min + tcolor + " minutes" : "")
					+ (sec > 0 ? ", and " + ncolor + sec + tcolor + " " + (sec == 1 ? "second" : "seconds") : "");
		}
		if (hours > 0) {
			return ncolor + hours + tcolor + " hours" + (min > 0 ? ", " + ncolor + min + tcolor + " minutes" : "")
					+ (sec > 0 ? ", and " + ncolor + sec + tcolor + " " + (sec == 1 ? "second" : "seconds") : "");
		}
		if (min > 0) {
			return ncolor + min + tcolor + " minutes"
					+ (sec > 0 ? ", and " + ncolor + sec + tcolor + " " + (sec == 1 ? "second" : "seconds") : "");
		}
		if (sec > 0) {
			return ncolor + sec + tcolor + " " + (sec == 1 ? "second" : "seconds");
		}

		return ncolor + "less than a second" + tcolor + "";
	}

	public static String formatDateRaw(long ms) {

		return formatDate(ms, "", "");

//		int l = (int) (ms / 1000);
//		int sec = l % 60;
//		int min = (l / 60) % 60;
//		int hours = ((l / 60) / 60) % 24;
//		int days = (((l / 60) / 60) / 24) % 7;
//		int weeks = (((l / 60) / 60) / 24) / 7;
//
//		if (weeks > 0) {
//			return weeks + " weeks" + (days > 0 ? ", " + days + " days" : "")
//					+ (hours > 0 ? ", " + hours + " hours" : "") + (min > 0 ? ", " + min + " minutes" : "")
//					+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
//		}
//		if (days > 0) {
//			return days + " days" + (hours > 0 ? ", " + hours + " hours" : "")
//					+ (min > 0 ? ", " + min + " minutes" : "")
//					+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
//		}
//		if (hours > 0) {
//			return hours + " hours" + (min > 0 ? ", " + min + " minutes" : "")
//					+ (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
//		}
//		if (min > 0) {
//			return min + " minutes" + (sec > 0 ? ", and " + sec + " " + (sec == 1 ? "second" : "seconds") : "");
//		}
//		if (sec > 0) {
//			return sec + " " + (sec == 1 ? "second" : "seconds");
//		}
//
//		return "less than a second" + "";
	}

	public static String formatDateTime(long ms, String ncolor, String tcolor) {

		int sec60 = (int) (ms % 1000) / 10;

		int l = (int) (ms / 1000);

		int sec = l % 60;
		int min = (l / 60) % 60;
		int hours = ((l / 60) / 60) % 24;
		int days = (((l / 60) / 60) / 24) % 7;
		int weeks = (((l / 60) / 60) / 24) / 7;

		DecimalFormat format = new DecimalFormat("00");

		if (weeks > 0) {
			return ncolor + format.format(weeks) + tcolor + ":" + ncolor + format.format(days) + tcolor + ":" + ncolor
					+ format.format(hours) + tcolor + ":" + ncolor + format.format(min) + tcolor + ":" + ncolor
					+ format.format(sec) + tcolor + ":" + ncolor + format.format(sec60) + tcolor;

		}
		if (days > 0) {
			return ncolor + format.format(days) + tcolor + ":" + ncolor + format.format(hours) + tcolor + ":" + ncolor
					+ format.format(min) + tcolor + ":" + ncolor + format.format(sec) + tcolor + ":" + ncolor
					+ format.format(sec60) + tcolor;
		}
		if (hours > 0) {
			return ncolor + format.format(hours) + tcolor + ":" + ncolor + format.format(min) + tcolor + ":" + ncolor
					+ format.format(sec) + tcolor + ":" + ncolor + format.format(sec60) + tcolor;
		}
		if (min > 0) {
			return ncolor + format.format(min) + tcolor + ":" + ncolor + format.format(sec) + tcolor + ":" + ncolor
					+ format.format(sec60) + tcolor;
		}
		if (sec > 0) {
			return ncolor + format.format(sec) + tcolor + ":" + ncolor + format.format(sec60) + tcolor;
		}
		if (sec60 > 0) {
			return ncolor + "00" + tcolor + ":" + ncolor + format.format(sec60) + tcolor;
		}

		return ncolor + "less than a millisecond" + tcolor + "";
	}

	public static String formatDateTimeRaw(long ms) {

		return formatDateTime(ms, "", "");

//		int l = (int) (ms / 1000);
//		int sec = l % 60;
//		int min = (l / 60) % 60;
//		int hours = ((l / 60) / 60) % 24;
//		int days = (((l / 60) / 60) / 24) % 7;
//		int weeks = (((l / 60) / 60) / 24) / 7;
//
//		DecimalFormat format = new DecimalFormat("00");
//
//		if (weeks > 0) {
//			return format.format(weeks) + ":" + format.format(days) + ":" + format.format(hours) + ":"
//					+ format.format(min) + ":" + format.format(sec);
//
//		}
//		if (days > 0) {
//			return format.format(days) + ":" + format.format(hours) + ":" + format.format(min) + ":"
//					+ format.format(sec);
//		}
//		if (hours > 0) {
//			return format.format(hours) + ":" + format.format(min) + ":" + format.format(sec);
//		}
//		if (min > 0) {
//			return format.format(min) + ":" + format.format(sec);
//		}
//		if (sec > 0) {
//			return "00" + ":" + format.format(sec);
//		}
//
//		return "less than a second";
	}

	public static String getSimpleTimeFormat(long ms) {
		return formatDate(ms, "&c", "&4");
	}

	public static void alert(AlertType type, String message) {
		Bukkit.broadcastMessage(colorize("&4&l" + type.name() + " ALERT &f> &c" + message));
	}

	public static void setGameMode(Player pl, GameMode gm) {
		pl.setGameMode(gm);
		pl.sendMessage(prefixes("gamemode")
				+ colorize("Your current gamemode has been set to &7" + gm.name().toLowerCase() + "&f."));
	}

	public static void coreHandleDamage(boolean handle) {
		handleDamage = handle;
	}

	public static boolean coreHandleDamage() {
		return handleDamage;
	}

	public static boolean useCoreScoreboard() {
		return coreBoard;
	}

	public static void useCoreScoreboard(boolean use) {
		coreBoard = use;
	}

	public static ItemStack getHead(String id, String value) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

		return Bukkit.getUnsafe().modifyItemStack(skull,
				"{SkullOwner:{Id:" + id + ",Properties:{textures:[{Value:\"" + value + "\"}]}}}");
	}

	public static ItemStack modifyHead(ItemStack skull, String id, String value) {

		return Bukkit.getUnsafe().modifyItemStack(skull.clone(),
				"{SkullOwner:{Id:" + id + ",Properties:{textures:[{Value:\"" + value + "\"}]}}}");
	}

	public static void proxyKick(String p) {
		String u = "";
		String m = "";
		int a = 0;
		for (String b : p.split(" ")) {
			if (a == 0)
				u = b;
			else
				m = m == "" ? b : m + " " + b;
			a = a + 1;
		}
		if (Bukkit.getPlayer(u) == null)
			return;
		Bukkit.getPlayer(u).kickPlayer(m);
	}

	public static void startGenericCooldown(Player p, String name, Runnable start, int cooldown, BossBar bar,
			Runnable finish) {
		if (!CoreUtils.getGenericCooldown(name).contains(p.getUniqueId())) {
			CoreUtils.getGenericCooldown(name).add(p.getUniqueId());
			start.run();
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(),
					new GenericCooldownRunnable(bar, name, p.getUniqueId(), new Date().getTime(), cooldown, finish), 1);

		}

	}

	public static void removeGenericCooldown(UUID uid, String name) {
		if (!genericCooldowns.containsKey(name)) {
			genericCooldowns.put(name, new ArrayList<>());
			return;
		}
		if (genericCooldowns.get(name).contains(uid))
			genericCooldowns.get(name).remove(uid);
	}

	public static Map<String, List<UUID>> getGenericCooldowns() {
		return genericCooldowns;
	}

	public static List<UUID> getGenericCooldown(String name) {
		if (!genericCooldowns.containsKey(name))
			genericCooldowns.put(name, new ArrayList<UUID>());

		return genericCooldowns.get(name);
	}

	public static void useGenericCooldown(String name, String display, Player player, BarColor color, int delay) {
		useGenericCooldown(name, display, player, color, delay, new Runnable() {

			@Override
			public void run() {
			}
		});
	}

	public static void useGenericCooldown(String name, String display, Player player, BarColor color, int delay,
			Runnable finish) {
		if (!genericCooldowns.containsKey(name))
			genericCooldowns.put(name, new ArrayList<>());

		if (!genericCooldowns.get(name).contains(player.getUniqueId())) {
			genericCooldowns.get(name).add(player.getUniqueId());
			BossBar bar = Bukkit.createBossBar(CoreUtils.colorize(display), color, BarStyle.SOLID);
			bar.addPlayer(player);
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new GenericCooldownRunnable(bar, name,
					player.getUniqueId(), new Date().getTime(), delay * 5, finish), 1);
		}
	}

	public static void log(String log) {
		System.out.println(colorize("&9[LOG]&7 >&f " + log));
	}

	public static boolean downloadFile(String url, String filename, String... auth) {

		boolean success = true;
		InputStream in = null;
		FileOutputStream out = null;

		try {

			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("GET");

			if (auth != null && auth.length >= 2) {
				String userCredentials = auth[0].trim() + ":" + auth[1].trim();
				String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
				conn.setRequestProperty("Authorization", basicAuth);
			}
			in = conn.getInputStream();
			out = new FileOutputStream(filename);
			int c;
			byte[] b = new byte[1024];
			while ((c = in.read(b)) != -1)
				out.write(b, 0, c);

		}

		catch (Exception ex) {
			log(("There was an error downloading " + filename + ". Check console for details."));
			ex.printStackTrace();
			success = false;
		}

		finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					log(("There was an error downloading " + filename + ". Check console for details."));
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					log(("There was an error downloading " + filename + ". Check console for details."));
					e.printStackTrace();
				}
		}
		return success;
	}

}
