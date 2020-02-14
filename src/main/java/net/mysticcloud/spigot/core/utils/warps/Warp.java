package net.mysticcloud.spigot.core.utils.warps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class Warp {
	
	Location loc;
	String name;
	
	Map<String, Object> metadata = new HashMap<>();
	
	public Warp(String name, Location location){
		this.loc = location;
		this.name = name;
	}
	
	public String name(){
		return name;
	}
	public Location location(){
		return loc;
	}

}
