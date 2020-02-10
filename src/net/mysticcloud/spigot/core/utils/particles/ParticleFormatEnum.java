package net.mysticcloud.spigot.core.utils.particles;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.formats.*;

public enum ParticleFormatEnum {

	CIRCLE_HEAD("Circle Head"),
	CIRCLE_FEET("Circle Feet"),
	HALO("Halo"),
	HAT("Hat"),
	HELIX("Helix"),
	RANDOM("Random"),
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
		default:
			formatter = new CircleHeadFormat();
			break;
			
		}
	}

	public ParticleFormat formatter() {
		return formatter;
	}

	
	


}
