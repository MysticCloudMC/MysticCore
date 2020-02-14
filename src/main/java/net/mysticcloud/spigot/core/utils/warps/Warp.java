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
	Warp(String name){
		this.name = name;
	}
	
	public String name(){
		return name;
	}
	public Location location(){
		return loc;
	}
	void location(Location location){
		loc = location;
	}
	public void metadata(String data, Object object) {
		metadata.put(data,object);
		
	}
	
	public Object metadata(String data) {
		if(metadata.containsKey(data))
			return metadata.get(data);
		else return null;
	}
	public Map<String, Object> metadata() {
		return metadata;
	}

}
