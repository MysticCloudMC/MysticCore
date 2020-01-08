package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CircleHeadFormat implements ParticleFormat {

	
	@Override
	public void display(UUID uid, int i) {
		Bukkit.getPlayer(uid).spawnParticle(Particle.CLOUD,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(i * (360 / 360)) * (2), 2.3,
						Math.sin(i * (360 / 360)) * (2)),
				0, 0, 0, 0, 2);
	}

}
