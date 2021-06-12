package net.mysticcloud.spigot.core.utils.accounts;

public enum PlayerSettings {

	HOLIDAY_PARTICLES("holidayParticles", "true"), REGIONAL_PARTICLES("regionalParticles", "true"),
	SIDEBAR("sidebar", "true"), COSMETIC_PARTICLES("cosmeticParticles", "true");

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
