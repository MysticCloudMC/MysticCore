package net.mysticcloud.spigot.core.utils.particles;

import net.mysticcloud.spigot.core.utils.particles.formats.CapeFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleHeadFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HaloFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HatFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HelixFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.LilyPadFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.RainbowFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.RandomFormat;

public enum ParticleFormatEnum {

	CIRCLE_HEAD("Circle Head"),
	CIRCLE_FEET("Circle Feet"),
	HALO("Halo"),
	HAT("Hat"),
	HELIX("Helix"),
	RANDOM("Random"),
	CAPE("Cape"),
	RAINBOW("Rainbow"),
	LILY_PAD("Lily Pad");

	String name;
	
	ParticleFormat formatter;

	ParticleFormatEnum(String name) {
		this.name = name;
		
		switch(name) {
		case "Circle Head":
			formatter = new CircleHeadFormat();
			break;
		case "Circle Feet":
			formatter = new CircleFeetFormat();
			break;
		case "Helix":
			formatter = new HelixFormat();
			break;
		case "Halo":
			formatter = new HaloFormat();
			break;
		case "Random":
			formatter = new RandomFormat();
			break;
		case "Lily Pad":
			formatter = new LilyPadFormat();
			break;
		case "Hat":
			formatter = new HatFormat();
			break;
		case "Cape":
			formatter = new CapeFormat();
			break;
		case "Rainbow":
			formatter = new RainbowFormat();
			break;
		default:
			formatter = new CircleHeadFormat();
			break;
			
		}
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
		default:
			return new CircleHeadFormat();
			
		}
	}

	
	


}
