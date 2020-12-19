package net.mysticcloud.spigot.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	private boolean nitro = false;
	private Map<PlayerSettings, String> settings = new HashMap<>();
	
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
	
	void setExtraData(Map<String,Object> extraData){
		this.extraData = extraData;
	}

	public void addGems(int i) {
		gems = gems + i;
	}
	
	public void gainXP(double xp) {
		this.xp = CoreUtils.getMoneyFormat(this.xp + xp);
		
		needed = LevelUtils.getMainWorker().untilNextLevel((long) (this.xp*100));
//		Bukkit.broadcastMessage("XP: " + this.xp);
//		Bukkit.broadcastMessage("NEEDED: " + needed);
//		Bukkit.broadcastMessage("LEVEL2: " + LevelUtils.getMainWorker().getLevel((long) (this.xp*100)));
//		
		sendMessage(
				((xp*100)<=needed) ? "You gained &7" + ((double) xp * 100.0) + " &fxp. You need &7" + needed
						+ "&f more points to level up." : "You gained &7" + ((double) xp * 100.0) + " &fxp.");
		if ((xp*100)>=needed) {
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
		sendMessage("account", message);
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
	



}
