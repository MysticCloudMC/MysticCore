package net.mysticcloud.spigot.core.utils;

import java.util.ArrayList;
import java.util.List;

public enum ObjectiveType {
	
	DUMMY("dummy",true),
	TRIGGER("trigger",true),
	DEATH_COUNT("deathCount",true),
	PLAYER_KILL_COUNT("playerKillCount",true),
	TOTAL_KILL_COUNT("totalKillCount",true),
	
	HEALTH("health",false),
	XP("xp",false),
	LEVEL("level",false),
	FOOD("food",false),
	AIR("air",false),
	ARMOR("armor",false);
	
	String objName;
	boolean modify;
	
	ObjectiveType(String objName, boolean modify){
		this.objName = objName;
		this.modify = modify;
	}
	
	public String getName() {
		return objName;
	}
	
	public boolean canModify() {
		return modify;
	}
	
	public static List<ObjectiveType> getModifiables(){
		List<ObjectiveType> types = new ArrayList<>();
		for(ObjectiveType type : values()) {
			if(type.modify)
				types.add(type);
		}
		return types;
	}
	
	public static List<ObjectiveType> getUnModifiables(){
		List<ObjectiveType> types = new ArrayList<>();
		for(ObjectiveType type : values()) {
			if(!type.modify)
				types.add(type);
		}
		return types;
	}

}
