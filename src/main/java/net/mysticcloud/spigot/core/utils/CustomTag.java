package net.mysticcloud.spigot.core.utils;

public enum CustomTag {
	AVACADO("&2[AVACADO]"),
	BEAST("&6[%emoticon:SWORD%BEAST%emoticon:SWORD%] "),
	STAR("&e[%emoticon:STAR_7%STAR%emoticon:STAR_7%] "),
	SUPERNOVA("&5[%emoticon:SUN%SUPER NOVA%emoticon:SUN%] "),
	SPARLE("&d[%emoticon:SPARKLE_SMALL%" + "SPARKLE" + "&d%emoticon:SPARKLE_SMALL%] "),
	PSYCHO("&d[%emoticon:HEART_1%PSYCHO%emoticon:HEART_1%] "),
	
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
