package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainbowFormat extends ParticleFormat {

	public RainbowFormat() {
		name = "Rainbow";
		guiItem = new ItemStack(Material.COAL);
		particle = Particle.REDSTONE;
	}

	@Override
	public void display(UUID uid, int i) {

		if (particle == null)
			return;
		setDustOptions(new DustOptions(Color.RED, 1));
		for (int a = 1; a != 21; a++) {
			spawnParticle(uid, particle,
					Bukkit.getPlayer(uid).getLocation().clone().add(0,
							2 + Math.cos(Math.toRadians(a) * ((360) / (40))) * (1),
							Math.sin(Math.toRadians(a) * ((360) / (40))) * (1)));

		}
		
		setDustOptions(new DustOptions(Color.ORANGE, 1));
		for (int a = 1; a != 21; a++) {
			spawnParticle(uid, particle,
					Bukkit.getPlayer(uid).getLocation().clone().add(0,
							2 + Math.cos(Math.toRadians(a) * ((360) / (40))) * (0.8),
							Math.sin(Math.toRadians(a) * ((360) / (40))) * (0.8)));

		}

	}

}
