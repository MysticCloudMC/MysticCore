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
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.CRIT);
		allowedParticles.add(Particle.REDSTONE);

		name = "Hat";
		guiItem = new ItemStack(Material.LEATHER_HELMET);
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if (dustoptions != null)
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
					Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0), 0, 0, 0, 0, 2, dustoptions);
		else if (materialdata != null)
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
					Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0), 0, 0, 0, 0, 2, materialdata);
		else if (itemstack != null)
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
					Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0), 0, 0, 0, 0, 2, itemstack);
		else if (blockdata != null)
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
					Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0), 0, 0, 0, 0, 2, blockdata);
		else Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0), 0, 0, 0, 0, 2);

	}

}
