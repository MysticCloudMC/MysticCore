package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class LilyPadFormat extends ParticleFormat {

	Location loc = null;
	double r = 1;
	int spots = 40;
	int rspots = 130;
	int corners = 4;

	public LilyPadFormat() {
		name = "&2Lily Pad";
		guiItem = new ItemStack(Material.LILY_PAD);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if (particle == null)
			return;
		loc = Bukkit.getPlayer(uid).getLocation();

		for (int t = 0; t != corners; t++) {
			spawnParticle(uid, particle, loc.clone().add(
					Math.cos(Math.toRadians(i + ((spots / (corners)) * t)) * (360 / spots)) * (Math
							.cos(Math.toRadians(i) * (360 / rspots) * (r))),
					0.05, 
					Math.sin(Math.toRadians(i + ((spots / (corners)) * t)) * (360 / spots)) * (Math
							.cos(Math.toRadians(i) * (360 / rspots) * (r)))));
		}
		
		for (int t = 0; t != corners; t++) {
			spawnParticle(uid, particle, loc.clone().add(
					Math.cos(Math.toRadians(i + ((spots / (corners)) * t)) * (360 / spots)) * (Math
							.cos(Math.toRadians((i + (rspots/4))) * (360 / rspots) * (r))),
					0.05, 
					Math.sin(Math.toRadians(i + ((spots / (corners)) * t)) * (360 / spots)) * (Math
							.cos(Math.toRadians((i + (rspots/4))) * (360 / rspots) * (r)))));
		}
		
		
	}

}
