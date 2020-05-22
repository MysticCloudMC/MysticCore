package net.mysticcloud.spigot.core.utils.entities;

public enum Bosses {
	
	TEST_ZOMBIE("zombie"),
	TEST_CHICKEN("chicken"),
	GOBLIN_BOSS("goblin");
	
	String callname;
	
	Bosses(String callname){
		this.callname = callname;
	}

	public String getCallName() {
		return callname;
	}

}
