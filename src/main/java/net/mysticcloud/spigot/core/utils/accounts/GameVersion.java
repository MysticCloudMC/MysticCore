package net.mysticcloud.spigot.core.utils.accounts;

public enum GameVersion {

	V1_17(999, 755, "1.17"), V1_16(754, 735, "1.16"), V1_15(734, 573, "1.15"), V1_14(572, 490, "1.14"),
	V1_13(489, 393, "1.13"), V1_12(392, 335, "1.12"), V1_11(334, 315, "1.11"), V1_10(314, 210, "1.10"),
	V1_9(209, 107, "1.9"), V1_8(106, 0, "1.8 or lower");

	int lowerBound;
	int upperBound;
	String version;

	GameVersion(int upperBound, int lowerBound, String version) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.version = version;
	}

	public static GameVersion getGameVersion(int v) {
		for (GameVersion version : GameVersion.values()) {
			if (v >= version.lowerBound && v < version.upperBound + 1)
				return version;
		}
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
