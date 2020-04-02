package net.mysticcloud.spigot.core.utils;

import java.util.UUID;

public class MysticPlayer {
	
	public UUID uid;
	private double balance = 0;
	private int level = 1;
	private int gems = 0;
	
	MysticPlayer(UUID uid){
		this.uid = uid;
	}

	public void setBalance(double balance) {
		
		this.balance = balance;
		if(Bukkit.getPlayer(uid) != null){
			CoreUtils.saveMysticPlayer(Bukkit.getPlayer(uid));
		}
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

	public void addGems(int i) {
		gems = gems + i;
	}

}
