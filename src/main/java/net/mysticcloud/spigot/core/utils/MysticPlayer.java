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

import net.mysticcloud.spigot.core.utils.levels.LevelUtils;

public class MysticPlayer {

	public UUID uid;
	private double balance = 0;
	private int gems = 0;
	private double xp = 0.0;
	private Map<String, Object> extraData = new HashMap<>();
	
	long needed = 0;

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

	void setGems(int gems) {
		this.gems = gems;
	}

	public double getBalance() {
		return balance;
	}

	public int getLevel() {
		return (int) LevelUtils.getMainWorker().getLevel((long) (xp*100));
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
		this.xp = CoreUtils.getMoneyFormat(this.xp + xp);
		
		needed = LevelUtils.getMainWorker().untilNextLevel((long) (this.xp*100));
		Bukkit.broadcastMessage("XP: " + this.xp);
		Bukkit.broadcastMessage("NEEDED: " + needed);
		Bukkit.broadcastMessage("LEVEL2: " + LevelUtils.getMainWorker().getLevel((long) (xp*100)));
		
		sendMessage(
				((xp*100)<needed) ? "You gained &7" + ((double) xp * 100.0) + " &fXP points. You need &7" + needed
						+ "&f more points to level up." : "You gained &7" + ((double) xp * 100.0) + " &fXP points.");
		if ((xp*100)>needed) {
			levelUp(LevelUtils.getMainWorker().getLevel((long) (xp*100)));
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
		sendMessage("root", message);
	}

	public void sendMessage(String prefix, String message) {
		if (Bukkit.getPlayer(uid) != null) {
			Bukkit.getPlayer(uid).sendMessage(CoreUtils.prefixes(prefix) + CoreUtils.colorize(message));
		}
	}
	
	public double getXP(){
		return xp;
	}
	public void setXP(double xp){
		this.xp = xp;
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
