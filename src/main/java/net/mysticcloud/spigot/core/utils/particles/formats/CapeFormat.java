package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CapeFormat extends ParticleFormat {

	public CapeFormat() {
		name = "Cape";
		guiItem = new ItemStack(Material.RED_BANNER);
		particle = Particle.CRIT;
		
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if(particle == null) return;
		spawnParticle(uid, particle, Bukkit.getPlayer(uid).getLocation().clone().add(
				0,2,0),0,2,0);
		
	}

}
