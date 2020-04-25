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
		loc = Bukkit.getPlayer(uid).getLocation().clone().add(-0.75 + (CoreUtils.getRandom().nextDouble()*1.5), 3,
				-0.5 + CoreUtils.getRandom().nextDouble());
		
		spawnParticle(uid, Particle.CAMPFIRE_COSY_SMOKE, loc);
		spawnParticle(uid, Particle.FALLING_WATER, loc);
		
	}

}
