package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class DoubleHelixFormat extends HelixFormat {

	public DoubleHelixFormat() {
		changeParticle = true;

		cols = 4;
		
		allowedParticles.clear();
		
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&e&lDouble &eHelix";
		guiItem = new ItemStack(Material.REDSTONE_BLOCK);
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
//		spawnParticle(uid, particle,
//				Bukkit.getPlayer(uid).getLocation().clone().add(Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))),
//						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
//						Math.sin(Math.toRadians(i) * (360 / 20)) * (1)));
//		spawnParticle(uid, particle,
//				Bukkit.getPlayer(uid).getLocation().clone().add(
//						Math.cos(Math.toRadians(((i + 10) * (360 / 20)) * (1))),
//						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
//						Math.sin(Math.toRadians(i + 10) * (360 / 20)) * (1)));

	}

}
