package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class AtomicFormat extends ParticleFormat {

	private double r = 1;
	private int spots = 40;

	public AtomicFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		guiItem = new ItemStack(Material.HEART_OF_THE_SEA);
		name = "&cAtomic";

	}

	@Override
	public void display(UUID uid, int i) {
		if (particle == null)
			return;
		spawnParticle(uid, particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i) * (360 / spots)) * (r))),
						1 + ((Math.cos(Math.toRadians(((i) * (360 / spots)) * (r))) * -1)),
						Math.sin(Math.toRadians(i) * (360 / spots)) * (r)));
		spawnParticle(uid, particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i + (spots / 3)) * (360 / spots)) * (r))),
						1 + Math.cos(Math.toRadians(((i + 10) * (360 / spots)) * (r))),
						Math.sin(Math.toRadians(i + (spots / 3)) * (360 / spots)) * (r)));
		spawnParticle(uid, particle,
				Bukkit.getPlayer(uid).getLocation().clone().add(
						Math.cos(Math.toRadians(((i + ((spots / 3)*2)) * (360 / spots)) * (r + (0.25)))),
						1,
						Math.sin(Math.toRadians(i + ((spots / 3)*2)) * (360 / spots)) * (r + (0.25))));

	}

}
