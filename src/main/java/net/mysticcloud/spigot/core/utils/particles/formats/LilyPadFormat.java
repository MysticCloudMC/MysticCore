package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class LilyPadFormat extends ParticleFormat {

	public LilyPadFormat() {
		name = "Lily Pad";
		guiItem = new ItemStack(Material.LILY_PAD);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if(particle == null) return;
		spawnParticle(uid, particle, Bukkit.getPlayer(uid).getEyeLocation().clone().add(
				Math.cos(Math.toRadians(((i) * (360 / 20)))) * (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3)))),
				0.2,
				Math.sin(Math.toRadians(i) * (360 / 20)) * (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3))))));
		spawnParticle(uid, particle,
				Bukkit.getPlayer(uid).getEyeLocation().clone()
						.add(Math.cos(Math.toRadians(((i + 10) * (360 / 20))))
								* (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3)))), 0.2,
								Math.sin(Math.toRadians(i + 10) * (360 / 20))
										* (Math.cos(Math.toRadians(((i) * (360 / 260)) * (1.3))))));
	}

}
