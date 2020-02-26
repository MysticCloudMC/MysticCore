package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HelixFormat extends ParticleFormat {

	public HelixFormat() {
		changeParticle = true;

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		
		
		name = "Helix";
		guiItem = new ItemStack(Material.REDSTONE);
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		
		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))),
						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
						Math.sin(Math.toRadians(i) * (360 / 20)) * (1)),
				0, 0, 0, 0, 2);

		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(Math.cos(Math.toRadians(((i + 10) * (360 / 20)) * (1))),
						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
						Math.sin(Math.toRadians(i + 10) * (360 / 20)) * (1)),
				0, 0, 0, 0, 2);

	}

}
