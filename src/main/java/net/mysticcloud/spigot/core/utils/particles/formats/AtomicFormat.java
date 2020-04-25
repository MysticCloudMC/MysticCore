package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class AtomicFormat extends ParticleFormat {

	public AtomicFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		guiItem = new ItemStack(Material.CONDUIT);
		name = "&cAtomic";

	}

	@Override
	public void display(UUID uid, int i) {
		if(particle == null) return;
		spawnParticle(uid, particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))),
						Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))), Math.sin(Math.toRadians(i) * (360 / 20)) * (1)));
		spawnParticle(uid, Particle.FLAME,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i + 10) * (360 / 20)) * (1))), Math.sin(Math.toRadians(((i) * (360 / 20)) * (1))),
						Math.cos(Math.toRadians(i + 10) * (360 / 20)) * (1)));

	}

}
