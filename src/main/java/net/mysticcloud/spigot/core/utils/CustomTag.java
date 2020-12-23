package net.mysticcloud.spigot.core.utils;

public enum CustomTag {
	GOD("&e[GOD] "),
	TEST("&e[Tester] ");
	
	String tag;
	
	CustomTag(String tag){
		this.tag = CoreUtils.colorize(tag);
	}
	
	public String getTag() {
		return tag;
	}
}
