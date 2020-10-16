package net.mysticcloud.spigot.core.utils.particles;

import java.util.ArrayList;
import java.util.List;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.formats.AtomicFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.CapeFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleHeadFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.DoubleHelixFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.GemsFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HaloFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HatFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.HelixFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.LilyPadFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.RainCloudFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.RainbowFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.RandomFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.WingsFormat;

public enum ParticleFormatEnum {

	CIRCLE_HEAD("Circle Head"),
	CIRCLE_FEET("Circle Feet"),
	HALO("Halo"),
	HAT("Hat"),
	HELIX("Helix"),
	DOUBLE_HELIX("Double Helix"),
	RANDOM("Random"),
	CAPE("Cape"), 
	POPPER("Popper", false),
	RAINBOW("Rainbow"), 
	LILY_PAD("Lily Pad"),
	RAIN_CLOUD("Rain Cloud"),
	ATOMIC("Atomic"),
	SELECTOR("Selector"),
	GEMS("Gems"),
	WINGS("Wings", false);

	String name;
	boolean avalible = true;

	ParticleFormat formatter;

	ParticleFormatEnum(String name) {
		this.name = name;
		formatter = formatter();
	}
	
	ParticleFormatEnum(String name, boolean avalible) {
		this.name = name;
		this.avalible = avalible;
		formatter = formatter();
	}
	
	public static List<ParticleFormatEnum> getAvalibleFormats() {
		List<ParticleFormatEnum> formats = new ArrayList<>();
		for(ParticleFormatEnum format : ParticleFormatEnum.values()) {
			if(format.isAvalible() || CoreUtils.debugOn()) {
				formats.add(format);
			}
		}
		return formats;
	}
	
	public boolean isAvalible() {
		return avalible;
	}

	public ParticleFormat formatter() {

		switch (name) {
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
		case "Atomic":
			return new AtomicFormat();
		case "Double Helix":
			return new DoubleHelixFormat();
		case "Selector":
			return new SelectorFormat();
		case "Gems":
			return new GemsFormat();
		case "Wings":
			return new WingsFormat();
		default:
			return new CircleHeadFormat();

		}
	}

}
