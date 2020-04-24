package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainbowFormat extends ParticleFormat {

	public RainbowFormat() {
		name = "Rainbow";
		guiItem = new ItemStack(Material.COAL);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		if(particle == null) return;
		for (int a = 1; a != 41; a++) {
					spawnParticle(uid, particle,
							Bukkit.getPlayer(uid).getLocation().clone().add(
									0, Math.cos(Math.toRadians(a) * ((360) / (40))) * (1),
									0));

		}

	}

}
