package net.mysticcloud.spigot.core.utils.accounts;

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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json2.JSONArray;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.levels.LevelUtils;

public class MysticPlayer {

	public UUID uid;
	private double balance = 0;
	private int gems = 0;
	private double xp = 0.0;
	private JSONObject extraData = new JSONObject();
	private boolean nitro = false;
	private GameVersion version = null;

	List<UUID> friends = new ArrayList<>();

	long needed = 0;

	MysticPlayer(UUID uid) {
		this.uid = uid;
		updateFriends();
	}

	public void setNitro(boolean nitro) {
		this.nitro = nitro;
	}

	public boolean isNitro() {
		return nitro;
	}

	public String setSetting(PlayerSettings setting, String value) {
		getSettings().put(setting.name(), value);
		if (Bukkit.getPlayer(uid) != null)
			switch (setting) {
			case SIDEBAR:
				if (value.equalsIgnoreCase("true")) {
					CoreUtils.setScoreboard(Bukkit.getPlayer(uid));
				} else {
					CoreUtils.removeScoreboard(Bukkit.getPlayer(uid));
				}
				break;
			default:
				break;
			}
		return value;
	}

	public String getSetting(PlayerSettings setting) {
		return getSettings().has(setting.name()) ? (String) getSettings().get(setting.name())
				: setting.getDefaultValue();
	}

	public JSONObject getSettings() {
		return extraData.has("settings") ? extraData.getJSONObject("settings")
				: extraData.put("settings", new JSONObject("{}").get("settings"));
	}

	public void setBalance(double balance, boolean save) {
		this.balance = balance;
		if (Bukkit.getPlayer(uid) != null && save) {
			MysticAccountManager.saveMysticPlayer(Bukkit.getPlayer(uid));
		}
	}

	public void setBalance(double balance) {
		setBalance(balance, false);

	}

	public void setGems(int gems) {
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

	public JSONObject getExtraData() {
		return extraData;
	}

	void setExtraData(JSONObject json) {
		this.extraData = json;
	}

	public void addGems(int i) {
		sendMessage("", "&9+" + i + " gems");
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
		MysticAccountManager.saveMysticPlayer(Bukkit.getPlayer(uid));
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

//	public boolean isFriends(String username) {
//		return FriendUtils.isFriends(CoreUtils.LookupForumID(uid), CoreUtils.LookupForumID(username));
//	}
//
//	public boolean isFriends(UUID uid) {
//		return FriendUtils.isFriends(CoreUtils.LookupForumID(uid), CoreUtils.LookupForumID(uid));
//	}
//
//	public boolean isFriends(int forumId) {
//		return FriendUtils.isFriends(CoreUtils.LookupForumID(uid), forumId);
//	}
//
//	public int getForumID() {
//		return FriendUtils.getForumsID(uid);
//	}
//
	@SuppressWarnings("deprecation")
	public void updateFriends() {
		friends.clear();
		JSONObject json = null;
		try {
			URL apiUrl = new URL("https://api.mysticcloud.net/player/" + getUUID());
			URLConnection yc = apiUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				json = new JSONObject(inputLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (json != null) {
			if (json.has("friends")) {
				JSONArray array = json.getJSONArray("friends");
				array.forEach(friend -> {
					JSONObject fdata = (JSONObject) friend;
					if (fdata.getBoolean("accepted") && CoreUtils.LookupUUID(fdata.getInt("friend_id")) != null) {
						friends.add(CoreUtils.LookupUUID(fdata.getInt("friend_id")));
					}
				});
			}
		}
	}

	public List<UUID> getFriends() {
		updateFriends();
		return friends;
	}
//
//		List<String> friends = new ArrayList<>();
//
//		for (UUID uid : FriendUtils.getFriends(uid)) {
//			if (Bukkit.getPlayer(uid) == null) {
//				friends.add(CoreUtils.lookupUsername(uid));
//			} else {
//				friends.add(Bukkit.getPlayer(uid).getName());
//			}
//		}
////
////		String id = "0";
////
////		try {
////			URL apiUrl = new URL("http://www.mysticcloud.net/api/friends.php?u=" + getUUID());
////			URLConnection yc = apiUrl.openConnection();
////			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
////			String inputLine;
////			JSONObject json = null;
////			while ((inputLine = in.readLine()) != null)
////				json = new JSONObject(inputLine);
////
////			id = json.getString("FORUMS_ID");
////
////			for (Object o : json.getJSONArray("FRIENDS")) {
////				if (o.toString().equalsIgnoreCase("0"))
////					continue;
////				ResultSet rs = CoreUtils.getForumsDatabase()
////						.query("SELECT * FROM xf_user_follow WHERE user_id='" + o.toString() + "';");
////				while (rs.next()) {
////					if (rs.getInt("follow_user_id") == Integer.parseInt(id)) {
////						URL apiUrl2 = new URL("http://www.mysticcloud.net/api/player.php?forumId=" + o.toString());
////						URLConnection yc2 = apiUrl2.openConnection();
////						BufferedReader in2 = new BufferedReader(new InputStreamReader(yc2.getInputStream()));
////						String inputLine2;
////						JSONObject json2 = null;
////						while ((inputLine2 = in2.readLine()) != null)
////							json2 = new JSONObject(inputLine2);
////						if (json2.has("USERNAME"))
////							friends.add(json2.getString("USERNAME"));
////					}
////				}
////				rs.close();
////
////			}
////
////		} catch (IOException | SQLException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		return friends;
//
//	}

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

}
