package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CircleHeadFormat extends ParticleFormat {
	
	double delta = 20;
	double radius = 1;
	
	public CircleHeadFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.TOTEM);
		allowedParticles.add(Particle.CRIT);
		allowedParticles.add(Particle.CRIT_MAGIC);
		allowedParticles.add(Particle.SMOKE_NORMAL);
		allowedParticles.add(Particle.VILLAGER_ANGRY);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.ENCHANTMENT_TABLE);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.HEART);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DAMAGE_INDICATOR);
		
		guiItem = new ItemStack(Material.DIAMOND_HELMET);
		name = "Circle Head";
		
		particle = (Particle) allowedParticles.toArray()[0];
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
		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i) * (360 / delta)) * (radius))), 2,
						Math.sin(Math.toRadians(i) * (360 / delta)) * (radius)),
				0, 0, 0, 0, 2);
		
		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i+(delta/2)) * (360 / delta)) * (radius))), 2,
						Math.sin(Math.toRadians(i+(delta/2)) * (360 / delta)) * (radius)),
				0, 0, 0, 0, 2);
		
	}

}
