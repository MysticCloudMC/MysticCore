package net.mysticcloud.spigot.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
	private double xp = 0.0;
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

	void setBalance(double balance) {
		setBalance(balance, false);

	}

	void setLevel(int level) {
		this.level = level;
	}

	void setGems(int gems) {
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
	
	public void gainXP(double xp) {
		this.xp = this.xp + xp;
		sendMessage(
				(xp < 1) ? "You gained &7" + ((double) xp * 100.0) + " &fXP points. You need &7" + ((1 - this.xp) * 100)
						+ "&f more points to level up." : "You gained &7" + ((double) xp * 100.0) + " &fXP points.");
		if (this.xp >= 1) {

			for (int a = 0; a < (int) this.xp; a++) {
				levelUp();
				this.xp = Double.parseDouble(new DecimalFormat("#0.00").format(Double.valueOf(this.xp - 1.0)));
			}
		}
		CoreUtils.saveMysticPlayer(Bukkit.getPlayer(uid));
	}

	public void levelUp() {
		level = level + 1;
		CoreUtils.getMysticPlayer(uid).getExtraData().put("SKYBLOCK_LEVEL", this.level);
		sendMessage("You leveled up to level &7" + level + "&f!");
	}
	public void sendMessage(String message) {
		sendMessage("root", message);
	}

	public void sendMessage(String prefix, String message) {
		if (Bukkit.getPlayer(uid) != null) {
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes(prefix) + CoreUtils.colorize(message));
		}
	}
	
	public boolean isFriends(UUID uid){
		for(MysticPlayer pl : getFriends()){
			if(pl.getUUID().equals(uid)) return true;
		}
		return false;
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
				if (uid.toString().equalsIgnoreCase(rs.getString("MINECRAFT_UUID")))
					sfriends = rs.getString("FRIENDS");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CoreUtils.debug("SFriends: " + sfriends);
		if (sfriends != "") {
			ResultSet rs2 = CoreUtils.wbconn.query("SELECT * FROM Users WHERE REGISTERED='true'");
			try {
				while (rs2.next()) {
					if (sfriends.contains(","))
						for (String friend : sfriends.split(",")) {
							if (rs2.getString("USERNAME").equalsIgnoreCase(friend)) {
								friends.add(
										CoreUtils.getMysticPlayer(UUID.fromString(rs2.getString("MINECRAFT_UUID"))));
							}
						}
					else {
						if (rs2.getString("USERNAME").equalsIgnoreCase(sfriends)) {
							friends.add(CoreUtils.getMysticPlayer(UUID.fromString(rs2.getString("MINECRAFT_UUID"))));
						}
					}

				}
			} catch (SQLException e) {
				return friends;
			}
		}
		return friends;
	}

}
