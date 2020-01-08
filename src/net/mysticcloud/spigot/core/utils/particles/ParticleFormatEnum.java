package net.mysticcloud.spigot.core.utils.particles;

import net.mysticcloud.spigot.core.utils.particles.formats.CircleHeadFormat;

public enum ParticleFormatEnum {
	
	CIRCLE_HEAD(new CircleHeadFormat());
	
	
	ParticleFormat formatter;
	
	ParticleFormatEnum(ParticleFormat formatter){
		this.formatter = formatter;
	}
	
	public ParticleFormat formatter() {
		return formatter;
	}

}
