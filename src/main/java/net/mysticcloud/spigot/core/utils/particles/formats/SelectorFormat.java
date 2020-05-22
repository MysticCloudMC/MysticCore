package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class SelectorFormat extends ParticleFormat {
	Location loc = null;
	
	RandomFormat random = new RandomFormat();
	

	public SelectorFormat() {
		changeParticle = true;
		cols = 5;
		
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		name = "&6Selector";
		guiItem = new ItemStack(Material.BLAZE_POWDER);
	}

	@Override
	public void display(UUID uid, int i) {
		display(Bukkit.getPlayer(uid).getLocation(),i);
	}
	
	@Override
	public void display(Location loc, int i) {
		if(particle == null) return;
		random.particle(particle);
		random.setBlockData(blockdata);
		random.setDustOptions(dustoptions);
		random.setMaterialData(materialdata);
		random.setLength(l);
		random.setHeight(h);
		this.loc = loc;
		random.display(loc, i);
		for(int t=0;t!=cols;t++){
			spawnParticle(particle,loc.clone().add(rotateAroundAxisY(new Vector(
					Math.cos(Math.toRadians(t) * (360 / cols) * (r)),
					0.05,
					Math.sin(Math.toRadians(t) * (360 / cols)) * (r)),i*2)));
		}

	}

}
