package net.mysticcloud.spigot.core.utils.particles;

import net.mysticcloud.spigot.core.utils.particles.formats.*;

public enum ParticleFormatEnum {

	CIRCLE_HEAD("Circle Head"),
	CIRCLE_FEET("Circle Feet"),
	HALO("Halo"),
	HAT("Hat"),
	HELIX("Helix"),
	RANDOM("Random"),
	CAPE("Cape"),
	RAINBOW("Rainbow"),
	LILY_PAD("Lily Pad"),
	RAIN_CLOUD("Rain Cloud");

	String name;
	
	ParticleFormat formatter;

	ParticleFormatEnum(String name) {
		this.name = name;
		
		formatter = formatter();
	}

	public ParticleFormat formatter() {
		
		switch(name) {
		case "Circle Head":
			return new CircleHeadFormat();
		case "Circle Feet":
			return new CircleFeetFormat();
		case "Helix":
			return new HelixFormat();
		case "Halo":
			return new HaloFormat();
		case "Random":
			return new RandomFormat();
		case "Lily Pad":
			return new LilyPadFormat();
		case "Hat":
			return new HatFormat();
		case "Cape":
			return new CapeFormat();
		case "Rainbow":
			return new RainbowFormat();
		case "Rain Cloud":
			return new RainCloudFormat();
		default:
			return new CircleHeadFormat();
			
		}
	}

	
	


}
