package net.mysticcloud.spigot.core.utils;

public enum SpawnReason {
	
	SELF("You teleported to Spawn."),
	OTHER("Someone has teleported you to Spawn."),
	DEATH("You died. Good job.");
	
	
	String message;
	
	SpawnReason(String message){
		this.message = message;
	}
	
	public String message() {
		return message;
	}

}
