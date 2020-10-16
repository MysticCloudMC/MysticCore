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

	public WingsFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.CRIT);

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
		for (double f = -10.0D; f < 6.2D; f += 0.2) {
			double var = Math.sin(f / 12.0D);
			double x = Math.sin(f) * (Math.exp(Math.cos(f)) - 2.0D * Math.cos(4.0D * f) - Math.pow(var, 5.0D)) / 2.0D;
			double z = Math.cos(f) * (Math.exp(Math.cos(f)) - 2.0D * Math.cos(4.0D * f) - Math.pow(var, 5.0D)) / 2.0D;
			Vector v = new Vector(-x, f, -z);
			rotateAroundAxisX(v, ((loc.getPitch() + 90.0F) * 0.017453292F));
			rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
			setDustOptions(new DustOptions(Color.RED, 1));
			spawnParticle(particle, loc.clone().add(v));
		}
	}

}
