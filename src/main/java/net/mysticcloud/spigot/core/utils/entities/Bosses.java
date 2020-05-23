package net.mysticcloud.spigot.core.utils.entities;

public enum Bosses {
	
	TEST_CHICKEN("chicken"),
	GOBLIN_BOSS("goblin"), IRON_BOSS("tinman"),SPIDER_QUEEN_BOSS("spider_queen"), SPIDER_QUEEN_MINION("spider_queen_minion");
	
	String callname;
	
	Bosses(String callname){
		this.callname = callname;
	}

	public String getCallName() {
		return callname;
	}

	public String getFormattedCallName() {
		String name = "";
		if (getCallName().contains("_")) {
			for (String g :getCallName().split("_")) {
				name = name + g.substring(0, 1).toUpperCase() + g.substring(1, g.length()).toLowerCase() + " ";
			}
			name = name.substring(0,name.length()-1);
		} else {
			name = getCallName().substring(0, 1).toUpperCase()
					+ getCallName().substring(1, getCallName().length()).toLowerCase();
		}
		return name;
	}

}
