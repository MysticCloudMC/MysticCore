package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HatFormat extends ParticleFormat {

	public HatFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.NAUTILUS);
		allowedParticles.add(Particle.WATER_DROP);
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);


		name = "Hat";
		guiItem = new ItemStack(Material.LEATHER_HELMET);
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		
		for(int f=0;f!=5;f++) {
			for(int g=0;g!=20;g++) {
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
						Bukkit.getPlayer(uid).getEyeLocation().clone().add(
								Math.cos(Math.toRadians(g) * ((360) / (20))) * (0.5- Integer.parseInt("0."+f)), 1 + Integer.parseInt("0."+f),
								Math.sin(Math.toRadians(g) * ((360) / (20))) * (0.5- Integer.parseInt("0."+f))),
						0, 0, 0, 0, 2);
			}
		}


	}

}
