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

	private Location cloc = null;

	public RainCloudFormat() {
		
		spots = 30;
		name = "&7Rain Cloud";
		guiItem = new ItemStack(Material.GRAY_DYE);
		particle = Particle.COMPOSTER;
	}
	
	@Override
	public void display(UUID uid, int i) {
		if (Bukkit.getPlayer(uid) != null) 
			display(Bukkit.getPlayer(uid).getLocation(), i);
	}

	@Override
	public void display(Location loc, int i) {
		if (particle == null)
			return;
		cloc = loc.clone();
		if (i % 2 == 0)
			for (int a = 0; a != 11; a++) {
				for (int t = 0; t != spots + 1; t++) {
					if (t == (CoreUtils.getRandom().nextInt(spots-1) + 1))
						spawnParticle(Particle.FALLING_WATER,
								cloc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)), h+1,
										Math.sin(t * (360 / spots)) * (a * (r / 10))));
					else {

						spawnParticle(Particle.CLOUD, cloc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)),
								h+1, Math.sin(t * (360 / spots)) * (a * (r / 10))));
					}
				}

			}
	}

}
