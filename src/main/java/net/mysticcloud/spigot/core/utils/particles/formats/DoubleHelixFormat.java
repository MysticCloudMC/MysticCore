package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class DoubleHelixFormat extends HelixFormat {

	public DoubleHelixFormat() {
		changeParticle = true;

		cols = 4;
		
		spots = 20;
		
		allowedParticles.clear();
		
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&e&lDouble &eHelix";
		guiItem = new ItemStack(Material.REDSTONE_BLOCK);
		
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		if(Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getLocation(),i);
		}
	}
	
	@Override
	public void display(Location loc, int i) {

		super.display(loc, i);
		spawnParticle(particle,
				loc.clone().add(Math.cos(Math.toRadians(((i) * (360 / spots)) * (r))),
						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (h))),
						Math.sin(Math.toRadians(i) * (360 / spots)) * (r)));
		spawnParticle(particle,
				loc.clone().add(
						Math.cos(Math.toRadians(((i + (spots/2)) * (360 / spots)) * (r))),
						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (h))),
						Math.sin(Math.toRadians(i + (spots/2)) * (360 / spots)) * (r)));

	
	}

}
