package net.mysticcloud.spigot.core.utils;

import org.bukkit.Material;

public enum Holiday {
	
	CHRISTMAS("Christmas","&c&lMerry &a&lChristmas&f"),
	NEW_YEARS("New Years","&a&lHappy New Years!"),
	HALLOWEEN("Halloween","&6&lHappy &5&lHalloween&f"), 
	NONE(""), 
	VALENTINES("Valentines Day"), 
	BIRTHDAY("Birthday"), 
	ST_PATRICKS("St. Patrick's Day","&fHappy &a&lSt. Patrick's day&f"), 
	CINCO_DE_MAYO("Cinco de Mayo","&c&lCinco &f&lDe &a&lMayo"), 
	AVACADO_DAY("(USA) National Avacado Day", "&2&lAVACADO DAY!"), 
	TEST("Test Holiday"),
	MAY_THE_FORTH("May The Forth","&eMay The Forth Be With You"),
	JULY_4TH("Forth of July (USA)","&fKeep your fingers safe!"),
	MEMORIAL_DAY("Memorial Day (USA)","&cThank &fYour &9Veterans");
	
	String name;
	String line = "";
	
	Holiday(String name){
		this.name = name;
	}
	Holiday(String name,String line){
		this.name = name;
		this.line = line;
	}
	
	public String getName() {
		return name;
	}

	Material getGUIItem() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getScoreboardLine() {
		return line;
	}

}
