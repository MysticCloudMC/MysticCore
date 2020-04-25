package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainCloudFormat extends ParticleFormat {

	private Location loc = null;
	private int spots = 20;
	private double r = 1;

	public RainCloudFormat() {
		name = "Rain Cloud";
		guiItem = new ItemStack(Material.GRAY_DYE);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if (particle == null)
			return;
		loc = Bukkit.getPlayer(uid).getLocation();

		for (int a = 0; a != 11; a++) {
			for (int t = 0; t != spots + 1; i++) {
				if (t % 2 == 0)
					spawnParticle(uid, Particle.FALLING_WATER,
							loc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)), 3,
									Math.sin(t * (360 / spots)) * (a * (r / 10))));
				else
					spawnParticle(uid, Particle.CAMPFIRE_COSY_SMOKE,
							loc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)), 3,
									Math.sin(t * (360 / spots)) * (a * (r / 10))));

			}

		}
	}

}
