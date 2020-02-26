package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HaloFormat extends ParticleFormat {

	public HaloFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.NAUTILUS);
		allowedParticles.add(Particle.WATER_DROP);
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);

		name = "Halo";
		guiItem = new ItemStack(Material.DIAMOND);
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);

		for (int a = 1; a != 41; a++) {
//			if (i % 0.009765625 == 0)
			if (i % 2 == 0) {
				if (a % 2 == 0)
					spawnParticle(uid, particle,
							Bukkit.getPlayer(uid).getEyeLocation().clone().add(
									Math.cos(Math.toRadians(a) * ((360) / (40))) * (1), 2,
									Math.sin(Math.toRadians(a) * ((360) / (40))) * (1)));
			} else {
				if (a % 2 != 0)
					spawnParticle(uid, particle,
							Bukkit.getPlayer(uid).getEyeLocation().clone().add(
									Math.cos(Math.toRadians(a) * ((360) / (40))) * (1), 2,
									Math.sin(Math.toRadians(a) * ((360) / (40))) * (1)));
			}

		}

	}

}
