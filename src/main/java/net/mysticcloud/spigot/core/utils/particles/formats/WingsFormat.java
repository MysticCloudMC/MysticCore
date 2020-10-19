package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class WingsFormat extends ParticleFormat {
	
	double wstart = -10.0D;
	double wend = 6.2D;
	double step = 0.2D;
	

	public WingsFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.CRIT);
		particleSize = 0.5f;
		name = "&aWings";
		guiItem = new ItemStack(Material.ELYTRA);
		allowedParticles.add(Particle.REDSTONE);
		r = 0.5;
		h = 2.25;
		spots = spots / 2;
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
		int a = 0;
		for (double f = -10.0D; f < 6.2D; f += 0.1) {
			double var = Math.sin(f / 12.0D);
			double x = Math.sin(f) * (Math.exp(Math.cos(f)) - 2.0D * Math.cos(4.0D * f) - Math.pow(var, 5.0D)) / 2.0D;
			double z = Math.cos(f) * (Math.exp(Math.cos(f)) - 2.0D * Math.cos(4.0D * f) - Math.pow(var, 5.0D)) / 2.0D;
			//(a/((wend-wstart)/step))*2
			Vector v = new Vector(-x, 0D, -z);
			rotateAroundAxisX(v, 90);
			v = v.add(new Vector(0,0,-0.125));
			rotateAroundAxisY(v, loc.getYaw());
//			rotateAroundAxisX(v, ((loc.getPitch() + 90.0F) * 0.017453292F));
//			rotateAroundAxisZ(v, (-loc.getYaw() * 0.017453292F));
			spawnParticle(particle, loc.clone().add(v).add(new Vector(0, 1.125, 0)));
			a = a + 1;
		}
	}

}
