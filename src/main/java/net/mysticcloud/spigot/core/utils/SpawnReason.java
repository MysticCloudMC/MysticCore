package net.mysticcloud.spigot.core.utils;

import java.util.Random;

public enum SpawnReason {
	
	SELF(new String[] {"You teleported to Spawn.","Welcome to Spawn."}),
	OTHER(new String[] {"Someone has teleported you to Spawn."}),
	DEATH(new String[] {"You died. Good job.","Another death message. There's loads of these","Hey careful, you caught an mildly-extreame case of death."});
	
	
	String[] messages;
	
	SpawnReason(String[] messages){
		this.messages = messages;
	}
	
	public String message() {
		return messages[new Random().nextInt(messages.length)];
	}
	public String[] messages() {
		return messages;
	}

}
