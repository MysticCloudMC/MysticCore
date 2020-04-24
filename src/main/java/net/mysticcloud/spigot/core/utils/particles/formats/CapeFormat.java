package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class CapeFormat extends ParticleFormat {

	public CapeFormat() {
		name = "Cape";
		guiItem = new ItemStack(Material.RED_BANNER);
		particle = Particle.ENCHANTMENT_TABLE;

	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if (particle == null)
			return;

		// double r = 2;
		// double t = Bukkit.getPlayer(uid).getEyeLocation().getYaw();
		// // double x = r * Math.cos(t);
		// double y = Bukkit.getPlayer(uid).getEyeLocation().getY();
		// // double z = r * Math.sin(t);
		// double x = Bukkit.getPlayer(uid).getEyeLocation().getX()+2;
		// double z = Bukkit.getPlayer(uid).getEyeLocation().getZ();
		// Vector v = new Vector(x, y, z);
		// v = rotateAroundAxisY(v, t);
		//// loc.add(2, 0, 0);
		// loc.add(v.getX(), v.getY(), v.getZ());
		//
		// spawnParticle(uid, particle, loc, 0, -0.5, 0);

		Location loc = Bukkit.getPlayer(uid).getLocation().clone().add(-0.5, 0, 0);

		// v = rotateAroundAxisY(v,
		// Bukkit.getPlayer(uid).getEyeLocation().getYaw());
		// loc.add(v.getX(), v.getY(), v.getZ());

		for (int t = 1; t != 11; t++) {
			double x = 0;
			double y = 2;
			double z = (-0.5)+Double.parseDouble("0." + t);
			Vector v = new Vector(x, y, z);
			v = rotateAroundAxisY(v, Bukkit.getPlayer(uid).getEyeLocation().getYaw());
			loc.add(v.getX(), v.getY(), v.getZ());
			spawnParticle(uid, particle, loc, 0, 0.5, -0.09);
		}

	}

	private Vector rotateAroundAxisY(Vector v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	private Vector rotateAroundAxisY(Vector v, double angle) {
		angle = -angle;
		angle = Math.toRadians(angle);
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos + v.getZ() * sin;
		z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

}
