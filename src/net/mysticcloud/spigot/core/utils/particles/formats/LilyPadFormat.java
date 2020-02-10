package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class LilyPadFormat extends ParticleFormat {

	public LilyPadFormat() {
		name = "Lily Pad";
		guiItem = new ItemStack(Material.LILY_PAD);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		if(!Bukkit.getPlayer(uid).hasPermission("mysticcloud.particle." + particle.name().toLowerCase())) {
			for(Particle particle : allowedParticles) {
				if(Bukkit.getPlayer(uid).hasPermission("mysticcloud.particle." + particle.name().toLowerCase())) {
					this.particle = particle;
					return;
				}
			}
			return;
		}
		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, Bukkit.getPlayer(uid).getLocation().clone().add(
				Math.cos(Math.toRadians(((i) * (360 / 20)))) * (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3)))),
				0.2,
				Math.sin(Math.toRadians(i) * (360 / 20)) * (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3))))), 0,
				0, 0, 0, 2);

		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone()
						.add(Math.cos(Math.toRadians(((i + 10) * (360 / 20))))
								* (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3)))), 0.2,
								Math.sin(Math.toRadians(i + 10) * (360 / 20))
										* (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3))))),
				0, 0, 0, 0, 2);
	}

}
