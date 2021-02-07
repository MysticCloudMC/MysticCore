package net.mysticcloud.spigot.core.utils;

public enum Emoticons {

	SWORD("\uD83D\uDDE1"), BOW("\uD83C\uDFF9"), TRIDENT("\uD83D\uDD31"), POTION("\uD83E\uDDEA"),
	SPLASH_POTION("\u2697"), FISHING_ROD("\uD83C\uDFA3"), SHIELD("\uD83D\uDEE1"), AXE("\uD83E\uDE93"), STAR_1("\u2605"),
	STAR_2("\u2606"), STAR_3("\u272A"), STAR_4("\u272A"), STAR_5("\u272F"), STAR_6("\u066D"), STAR_7("\u272D"),
	STAR_8("\u2730"), STAR_9("\u269D"), STAR_10("\u2734"), STAR_11("\u2733"), STAR_12("\u272B"), STAR_13("\u235F"),
	STAR_14("\u2727"), STAR_15("\u2742"), SUN("\u2600"), MULTIPLICATION("\u2716"), CHECK_MARK("\u2714"),
	MUSIC_NOTE_1("\u2600"), MUSIC_NOTE_2("\u2669"), MUSIC_NOTE_3("\u266B"), MUSIC_NOTE_4("\u266C"),
	SCISSORS_1("\u2704"), SCISSORS_2("\u2702"), ENVELOPE("\u27BF"), COMET("\u2604"), UNKNOWN("???");

	String unicode;

	Emoticons(String unicode) {
		this.unicode = unicode;
	}

	@Override
	public String toString() {
		return unicode;
	}

}
