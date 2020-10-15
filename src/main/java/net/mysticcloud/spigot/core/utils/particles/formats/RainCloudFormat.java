package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainCloudFormat extends ParticleFormat {

	private Location cloc = null;
	double scalar = 6;

	public RainCloudFormat() {

		spots = 30;
		name = "&7Rain Cloud";
		guiItem = new ItemStack(Material.GRAY_DYE);
		particle = Particle.COMPOSTER;

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
		cloc = loc.clone();
		if (0.05 > CoreUtils.getRandom().nextDouble() * 100) {
			for (int z = 0; z != 5 * scalar; i++) {
				loc.add((CoreUtils.getRandom().nextDouble()
						* (CoreUtils.getRandom().nextBoolean() ? (double) 1 / scalar
								: -(double) 1 / scalar)),
						-1 / scalar,
						(CoreUtils.getRandom().nextDouble()
								* (CoreUtils.getRandom().nextBoolean() ? (double) 1 / scalar
										: -(double) 1 / scalar)));
				spawnParticle(Particle.END_ROD, loc);
			}
		}
		if (i % 2 == 0)
			for (int a = 0; a != 11; a++) {
				for (int t = 0; t != spots + 1; t++) {
						spawnParticle(Particle.CLOUD, cloc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)),
								h + 1, Math.sin(t * (360 / spots)) * (a * (r / 10))));
				}

			}
	}

}
