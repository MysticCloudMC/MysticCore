package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HaloFormat extends ParticleFormat {

	public HaloFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.NAUTILUS);
		allowedParticles.add(Particle.WATER_DROP);
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);

		particle = (Particle) allowedParticles.toArray()[0];

		name = "Halo";
		guiItem = new ItemStack(Material.DIAMOND);
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

		for (int a = 1; a != 41; a++) {
//			if (i % 0.009765625 == 0)
				if (i % 2 == 0) {
					if (a % 2 == 0)
						Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
								Bukkit.getPlayer(uid).getLocation().clone().add(
										Math.cos(Math.toRadians(a) * ((360) / (40))) * (1), 2,
										Math.sin(Math.toRadians(a) * ((360) / (40))) * (1)),
								0, 0, 0, 0, 2);
				} else {
					if (a % 2 != 0)
						Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
								Bukkit.getPlayer(uid).getLocation().clone().add(
										Math.cos(Math.toRadians(a) * ((360) / (40))) * (1), 2,
										Math.sin(Math.toRadians(a) * ((360) / (40))) * (1)),
								0, 0, 0, 0, 2);
				}

		}

	}

}