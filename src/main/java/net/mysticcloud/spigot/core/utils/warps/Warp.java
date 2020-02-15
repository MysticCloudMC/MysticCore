package net.mysticcloud.spigot.core.utils.warps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class Warp {
	
	Location loc;
	String name;
	int id;
	
	Map<String, String> metadata = new HashMap<>();
	
	public Warp(int id, String name, Location location){
		this.id = id;
		this.loc = location;
		this.name = name;
	}
	Warp(int id){
		this.id = id;
	}
	void location(Location location){
		loc = location;
	}
	void name(String name) {
		this.name = name;
	}
	
	public int id() {
		return id;
	}
	
	public String name(){
		return name;
	}
	public Location location(){
		return loc;
	}
	
	public void metadata(String key, String value) {
		metadata.put(key,value);
		
	}
	
	public Object metadata(String data) {
		if(metadata.containsKey(data))
			return metadata.get(data);
		else return null;
	}
	public Map<String, String> metadata() {
		return metadata;
	}

}
