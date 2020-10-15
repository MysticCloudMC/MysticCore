package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class HatFormat extends ParticleFormat {

	Vector v = new Vector(0, 1, 0);

	public HatFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.CRIT);
		allowedParticles.add(Particle.REDSTONE);
		allowedParticles.add(Particle.SNEEZE);
		allowedParticles.add(Particle.CRIT_MAGIC);

		name = "&eHat";
		guiItem = new ItemStack(Material.LEATHER_HELMET);
	}
	
	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) 
			display(Bukkit.getPlayer(uid).getLocation());
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		v = rotateAroundAxisX(new Vector(0, 1, 0), loc.getPitch());
		v = rotateAroundAxisY(v, loc.getYaw());
		spawnParticle(particle, loc.add(v));
	}

}
