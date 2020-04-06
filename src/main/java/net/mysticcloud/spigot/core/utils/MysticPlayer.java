package net.mysticcloud.spigot.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

public class MysticPlayer {

	public UUID uid;
	private double balance = 0;
	private int level = 1;
	private int gems = 0;
	private Map<String, Object> extraData = new HashMap<>();

	MysticPlayer(UUID uid) {
		this.uid = uid;
	}

	public void setBalance(double balance, boolean save) {
		this.balance = balance;
		if (Bukkit.getPlayer(uid) != null && save) {
			CoreUtils.saveMysticPlayer(Bukkit.getPlayer(uid));
		}
	}

	public void setBalance(double balance) {
		setBalance(balance, false);

	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setGems(int gems) {
		this.gems = gems;
	}

	public double getBalance() {
		return balance;
	}

	public int getLevel() {
		return level;
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

	public void addGems(int i) {
		gems = gems + i;
	}

	public List<MysticPlayer> getFriends() {
		try {
			if (!CoreUtils.isPlayerRegistered(uid))
				return null;
		} catch (SQLException e) {
			return null;
		}
		String sfriends = "";
		List<MysticPlayer> friends = new ArrayList<>();
		ResultSet rs = CoreUtils.wbconn.query("SELECT * FROM Users");
		try {
			while (rs.next()) {
				sfriends = rs.getString("FRIENDS");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResultSet rs2 = CoreUtils.wbconn.query("SELECT * FROM Users WHERE REGISTERED='true'");
		try {
			while (rs2.next()) {
				for (String friend : sfriends.split(",")) {
					if(rs2.getString("USERNAME").equalsIgnoreCase(friend)){
						friends.add(CoreUtils.getMysticPlayer(UUID.fromString(rs2.getString("MINECRAFT_UUID"))));
					}
				}
			}
		} catch (SQLException e) {
			return null;
		}
		return friends;
	}

}
