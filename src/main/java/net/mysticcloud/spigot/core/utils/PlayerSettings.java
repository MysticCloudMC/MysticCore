package net.mysticcloud.spigot.core.utils;

public enum PlayerSettings {

	HOLIDAY_PARTICLES("holidayParticles", "true"), REGIONAL_PARTICLES("regionalParticles", "true"),
	SIDEBAR("sidebar", "true");

	String setting;
	String dvalue;

	PlayerSettings(String setting, String dvalue) {
		this.setting = setting;
		this.dvalue = dvalue;
	}

	public String getName() {
		return setting;
	}

	public String getDefaultValue() {
		return dvalue;
	}

}
