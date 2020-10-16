package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class WingsFormat extends ParticleFormat {

	public WingsFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.CRIT);

		name = "&aWings";
		guiItem = new ItemStack(Material.GOLDEN_HELMET);
		allowedParticles.add(Particle.REDSTONE);
		r = 0.5;
		h = 2.25;
		spots = spots/2;
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getEyeLocation());
		}
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		for (int a = 1; a != spots + 1; a++) {
			spawnParticle(particle,
					loc.clone().add(rotateAroundAxisY(new Vector(Math.cos(Math.toRadians(a) * ((360) / (spots))) * (r),
							h, Math.sin(Math.toRadians(a) * ((360) / (spots))) * (r)), i)));

		}

	}

}
