package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class SelectorFormat extends ParticleFormat {
	Location loc = null;
	double r = 1;
	int cols = 5;
	
	RandomFormat random = new RandomFormat();
	

	public SelectorFormat() {
		changeParticle = true;

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&6Selector";
		guiItem = new ItemStack(Material.BLAZE_POWDER);
	}

	@Override
	public void display(UUID uid, int i) {
		if(particle == null) return;
		random.particle(particle);
		random.display(uid, i);
		for(int t=0;t!=cols;t++){
			loc = Bukkit.getPlayer(uid).getLocation();
			spawnParticle(uid,particle,loc.clone().add(rotateAroundAxisY(new Vector(
					Math.cos(Math.toRadians(t) * (360 / cols) * (r)),
					0,
					Math.sin(Math.toRadians(t) * (360 / cols)) * (r)),i*2)));
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
