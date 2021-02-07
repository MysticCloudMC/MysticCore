package net.mysticcloud.spigot.core.utils;

public enum CustomTag {
	GOD("&e[GOD] "),
	TEST("&e[Tester] "),
	BEAST("&6[" + Emoticons.SWORD + "BEAST" + Emoticons.SWORD + "] "),
	STAR("&e[" + Emoticons.STAR_7 + "STAR" + Emoticons.STAR_7 + "] "),
	SUPERNOVA("&5[" + Emoticons.SUN + "SUPER NOVA" + Emoticons.SUN + "] "),
	NONE("you shouldn't have this.");

	String tag;

	CustomTag(String tag) {
		this.tag = CoreUtils.colorize(tag);
	}

	public String getTag() {
		return tag;
	}

	public static CustomTag tagFromName(String name) {

		for (CustomTag tag : CustomTag.values()) {
			if (tag.name().equalsIgnoreCase(name))
				return tag;
		}

		return CustomTag.NONE;
	}
}
