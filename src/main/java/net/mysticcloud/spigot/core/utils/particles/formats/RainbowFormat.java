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

public class RainbowFormat extends ParticleFormat {

	Location cloc = null;

	public RainbowFormat() {
		name = "Rainbow";
		guiItem = new ItemStack(Material.COAL);
		particle = Particle.REDSTONE;
	}

	@Override
	public void display(UUID uid, int i) {

		if (particle == null)
			return;

		cloc = Bukkit.getPlayer(uid).getLocation();

		setDustOptions(new DustOptions(Color.RED, 1));
		for (int a = 1; a != 21; a++) {
			Vector v = new Vector(0, 2 + Math.cos(Math.toRadians(a+30) * ((360) / (40))) * (1),
					Math.sin(Math.toRadians(a+30) * ((360) / (40))) * (1));
//			v = rotateAroundAxisX(v,90);
			v = rotateAroundAxisY(v, cloc.getYaw());
			spawnParticle(uid, particle, Bukkit.getPlayer(uid).getLocation().clone().add(v));

		}

		setDustOptions(new DustOptions(Color.ORANGE, 1));
		for (int a = 1; a != 21; a++) {
			Vector v = new Vector(0, 2 + Math.cos(Math.toRadians(a+30) * ((360) / (40))) * (0.8),
					Math.sin(Math.toRadians(a+30) * ((360) / (40))) * (0.8));
			v = rotateAroundAxisY(v, cloc.getYaw());
			spawnParticle(uid, particle, Bukkit.getPlayer(uid).getLocation().clone().add(v));

		}

	}

}
