package net.mysticcloud.spigot.core.utils;

import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;

public enum CustomTag {
	GOD("&e[GOD] "),
	TEST("&e[Tester] "),
	BEAST("&6[%emoticon:SWORD%BEAST%emoticon:SWORD%] "),
	STAR("&e[%emoticon:STAR_7%STAR%emoticon:STAR_7%] "),
	SUPERNOVA("&5[%emoticon:SUN%SUPER NOVA%emoticon:SUN%] "),
	NONE("you shouldn't have this.");

	String tag;

	CustomTag(String tag) {
		this.tag = CoreUtils.colorize(tag);
	}

	public String getTag() {
		return PlaceholderUtils.emotify(tag);
	}

	public static CustomTag tagFromName(String name) {

		for (CustomTag tag : CustomTag.values()) {
			if (tag.name().equalsIgnoreCase(name))
				return tag;
		}

		return CustomTag.NONE;
	}
}
