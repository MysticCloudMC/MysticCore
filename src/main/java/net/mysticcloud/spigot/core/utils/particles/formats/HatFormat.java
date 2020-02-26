package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

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
		spawnParticle(
				uid,
				particle,
				Bukkit.getPlayer(uid).getEyeLocation().clone().add(0, 1, 0));
	}

}
