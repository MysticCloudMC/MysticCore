package net.mysticcloud.spigot.core.utils;

public enum GameVersion {

	V1_16(999, 735, "1.16"), V1_15(734, 573, "1.15"), V1_14(572, 490, "1.14"), V1_13(489, 393, "1.13"),
	V1_12(392, 335, "1.12"), V1_11(334, 315, "1.11"), V1_10(314, 210, "1.10"), V1_9(209, 107, "1.9"),
	V1_8(106, 0, "1.8 or lower");

	int lowerBound;
	int upperBound;
	String version;

	GameVersion(int upperBound, int lowerBound, String version) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.version = version;
	}

	public static GameVersion getGameVersion(int v) {
		if (v >= 735)
			return V1_16;
		if (v >= 573 && v < 735)
			return V1_15;
		if (v >= 490 && v < 573)
			return V1_14;
		if (v >= 393 && v < 490)
			return V1_13;
		if (v >= 335 && v < 393)
			return V1_12;
		if (v >= 315 && v < 335)
			return V1_11;
		if (v >= 210 && v < 315)
			return V1_10;
		if (v >= 107 && v < 210)
			return V1_9;
		if (v < 107)
			return V1_8;

		return V1_8;
	}

	public String getVersionName() {
		return version;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}

}
