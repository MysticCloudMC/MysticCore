package net.mysticcloud.spigot.core.utils;

public enum Emoticons {

	SWORD("\uD83D\uDDE1"), BOW("\uD83C\uDFF9"), TRIDENT("\uD83D\uDD31"), POTION("\uD83E\uDDEA"),
	SPLASH_POTION("\u2697"), FISHING_ROD("\uD83C\uDFA3"), SHIELD("\uD83D\uDEE1"), AXE("\uD83E\uDE93"), STAR_1("\u2605"),
	STAR_2("\u2606"), STAR_3("\u272A"), STAR_4("\u272A"), STAR_5("\u272F"), STAR_6("\u066D"), STAR_7("\u272D"),
	STAR_8("\u2730"), STAR_9("\u269D"), STAR_10("\u2734"), STAR_11("\u2733"), STAR_12("\u272B"), STAR_13("\u235F"),
	STAR_14("\u2727"), STAR_15("\u2742"),
	UNKNOWN("???");

	String unicode;

	Emoticons(String unicode) {
		this.unicode = unicode;
	}

	@Override
	public String toString() {
		return unicode;
	}

}
