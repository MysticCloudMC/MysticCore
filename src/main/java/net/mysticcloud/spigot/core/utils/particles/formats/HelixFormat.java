package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HelixFormat extends ParticleFormat {
	Location cloc = null;
	int colspots = 180;

	public HelixFormat() {
		changeParticle = true;

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&eHelix";
		guiItem = new ItemStack(Material.REDSTONE);
	}
	
	@Override
	public void display(UUID uid, int i) {
		if (Bukkit.getPlayer(uid) != null) 
			display(Bukkit.getPlayer(uid).getLocation(), i);
	}

	@Override
	public void display(Location loc, int i) {
		if(particle == null) return;
		for(int t=0;t!=cols;t++){
			cloc = loc.clone().add(
					Math.cos(Math.toRadians(i + ((spots/cols)*t)) * (360 / spots) * (r)),
					1 + Math.cos(Math.toRadians(((i) * (360 / colspots)) * (r*2))),
					Math.sin(Math.toRadians(i + ((spots/cols)*t)) * (360 / spots)) * (r));
			spawnParticle(particle,cloc);
		}
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
