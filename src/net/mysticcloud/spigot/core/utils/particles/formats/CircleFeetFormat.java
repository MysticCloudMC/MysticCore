package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CircleFeetFormat extends ParticleFormat {
	
	public CircleFeetFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.DAMAGE_INDICATOR);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		
		guiItem = new ItemStack(Material.DIAMOND_BOOTS);
		name = "Circle Feet";
		
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
						Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))), 0.1,
						Math.sin(Math.toRadians(i) * (360 / 20)) * (1)),
				0, 0, 0, 0, 2);
		
		Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i+10) * (360 / 20)) * (1))), 0.1,
						Math.sin(Math.toRadians(i+10) * (360 / 20)) * (1)),
				0, 0, 0, 0, 2);
		
	}

}
