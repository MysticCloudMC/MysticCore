package net.mysticcloud.spigot.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.levels.LevelUtils;

public class MysticPlayer {

	public UUID uid;
	private double balance = 0;
	private int gems = 0;
	private double xp = 0.0;
	private Map<String, Object> extraData = new HashMap<>();
	private boolean nitro = false;
	private Map<PlayerSettings, String> settings = new HashMap<>();
	private GameVersion version = null;

	long needed = 0;

	MysticPlayer(UUID uid) {
		this.uid = uid;
	}

	public void setNitro(boolean nitro) {
		this.nitro = nitro;
	}

	public boolean isNitro() {
		return nitro;
	}

	public String setSetting(PlayerSettings setting, String value) {
		settings.put(setting, value);
		return value;
	}

	public String getSetting(PlayerSettings setting) {
		return settings.containsKey(setting) ? settings.get(setting) : setting.getDefaultValue();
	}

	public void setBalance(double balance, boolean save) {
		this.balance = balance;
		if (Bukkit.getPlayer(uid) != null && save) {
			CoreUtils.saveMysticPlayer(Bukkit.getPlayer(uid));
		}
	}

	void setBalance(double balance) {
		setBalance(balance, false);

	}

	void setGems(int gems) {
		this.gems = gems;
	}

	public double getBalance() {
		return balance;
	}

	public int getLevel() {
		return (int) LevelUtils.getMainWorker().getLevel((long) (xp * 100));
	}

	public int getGems() {
		return gems;
	}

	public UUID getUUID() {
		return uid;
	}

	public Object getData(String key) {
		return extraData.get(key);
	}

	public Map<String, Object> getExtraData() {
		return extraData;
	}

	void setExtraData(Map<String, Object> extraData) {
		this.extraData = extraData;
	}

	public void addGems(int i) {
		gems = gems + i;
	}

	public void gainXP(double xp) {
		this.xp = CoreUtils.getMoneyFormat(this.xp + xp);

		needed = (long) LevelUtils.getMainWorker().untilNextLevel((this.xp * 100));
//		Bukkit.broadcastMessage("XP: " + this.xp);
//		Bukkit.broadcastMessage("NEEDED: " + needed);
//		Bukkit.broadcastMessage("LEVEL2: " + LevelUtils.getMainWorker().getLevel((long) (this.xp*100)));

//		
		sendRawMessage("&3" + CoreUtils.getMoneyFormat((double) xp * 100.0) + " xp.");

//		sendRawMessage(((xp * 100) <= needed)
//				? "&3" + CoreUtils.getMoneyFormat((double) xp * 100.0) + " xp. You need &7" + needed
//						+ "&f more points to level up."
//				: "You gained &7" + CoreUtils.getMoneyFormat((double) xp * 100.0) + " &fxp.");

		if ((xp * 100) >= needed) {
			levelUp(((long) LevelUtils.getMainWorker().getLevel((long) (xp * 100))));
		}
		CoreUtils.saveMysticPlayer(Bukkit.getPlayer(uid));
	}

	public void levelUp() {
//		level = level + 1;
		sendMessage("You leveled up to level &7" + getLevel() + "&f!");
	}

	public void levelUp(long level) {
//		this.level = level;
		sendMessage("You leveled up to level &7" + getLevel() + "&f!");
	}

	public void sendMessage(String message) {
		sendMessage("account", message);
	}

	public void sendMessage(String prefix, String message) {
		if (Bukkit.getPlayer(uid) != null) {
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes(prefix) + CoreUtils.colorize(message));
		}
	}

	public void sendRawMessage(String message) {
		if (Bukkit.getPlayer(uid) != null) {
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.colorize(message));
		}
	}

	public double getXP() {
		return xp;
	}

	public void setXP(double xp) {
		this.xp = xp;
	}

	public boolean isFriends(String username) {
		return getFriends().contains(username);
	}

	public boolean isFriends(UUID uid) {
		return isFriends(CoreUtils.lookupUsername(uid));
	}

	public boolean isFriends(int forumId) {
		return isFriends(CoreUtils.lookupUsername(CoreUtils.LookupUUID(forumId)));
	}

	public List<String> getFriends() {

		List<String> friends = new ArrayList<>();

		String id = "0";

		try {
			URL apiUrl = new URL("http://www.mysticcloud.net/api/friends.php?u=" + getUUID());
			URLConnection yc = apiUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			JSONObject json = null;
			while ((inputLine = in.readLine()) != null)
				json = new JSONObject(inputLine);

			id = json.getString("FORUMS_ID");

			for (Object o : json.getJSONArray("FRIENDS")) {
				if (o.toString().equalsIgnoreCase("0"))
					continue;
				ResultSet rs = CoreUtils.getForumsDatabase()
						.query("SELECT * FROM xf_user_follow WHERE user_id='" + o.toString() + "';");
				while (rs.next()) {
					if (rs.getInt("follow_user_id") == Integer.parseInt(id)) {
						URL apiUrl2 = new URL("http://www.mysticcloud.net/api/player.php?forumId=" + o.toString());
						URLConnection yc2 = apiUrl2.openConnection();
						BufferedReader in2 = new BufferedReader(new InputStreamReader(yc2.getInputStream()));
						String inputLine2;
						JSONObject json2 = null;
						while ((inputLine2 = in2.readLine()) != null)
							json2 = new JSONObject(inputLine2);
						if (json2.has("USERNAME"))
							friends.add(json2.getString("USERNAME"));
					}
				}
				rs.close();

			}

		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friends;

	}

	public GameVersion getGameVersion() {
		if (version == null) {
			ResultSet rs = CoreUtils.sendQuery("SELECT * FROM MysticPlayers WHERE UUID='" + uid + "';");
			try {
				if (rs.next()) {
					version = GameVersion.getGameVersion(Integer.parseInt(rs.getString("VERSION")));
					rs.close();
					return version;
				}

				rs.close();
				return GameVersion.V1_8;
			} catch (SQLException e) {
				return GameVersion.V1_8;
			}
		}

		return version;
	}

	public JSONObject getExtraData_JSON() {
		JSONObject json = new JSONObject();
		for (Entry<String, Object> e : extraData.entrySet()) {
			json.put(e.getKey(), e.getValue());
		}

		return json;
	}

}
