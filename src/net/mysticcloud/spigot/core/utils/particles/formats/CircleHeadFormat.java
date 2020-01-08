package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CircleHeadFormat implements ParticleFormat {

	
	@Override
	public void display(UUID uid, int i) {
		Bukkit.getPlayer(uid).getWorld().spawnParticle(Particle.values()[CoreUtils.particletest],
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(i * (360 / 20))) * (1.5), 2.3,
						Math.sin(Math.toRadians(i * (360 / 20))) * (1.5)),
				0, 0, 0, 0, 2);
	}

}
