package net.mysticcloud.spigot.core.utils;

import org.bukkit.Material;

public enum Holiday {
	
	CHRISTMAS("Christmas"),
	NEW_YEARS("New Years"),
	HALLOWEEN("Halloweed"), 
	NONE(""), 
	VALENTINES("Valentines Day"), 
	BIRTHDAY("Birthday"), 
	ST_PATRICKS("St. Patrick's Day"), 
	CINCO_DE_MAYO("Cinco de Mayo"), 
	AVACADO_DAY("(USA) National Avacado Day"), 
	TEST("Test Holiday"),
	MAY_THE_FORTH("May The Forth"),
	JULY_4TH("Forth of July (USA)"),
	MEMORIAL_DAY("Memorial Day (USA)");
	
	String name;
	
	Holiday(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	Material getGUIItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
