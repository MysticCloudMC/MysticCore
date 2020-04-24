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

	private Location cloc = null;

	public CapeFormat() {
		name = "Cape";
		guiItem = new ItemStack(Material.RED_BANNER);
		particle = Particle.ENCHANTMENT_TABLE;

	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		if (particle == null || Bukkit.getPlayer(uid) == null)
			return;

		cloc = Bukkit.getPlayer(uid).getEyeLocation().clone();
		double x = -0.5;
		double y = -0.5;
		double z = -0.15;

		for (int t = 0; t != 10; t++) {
			Vector v = new Vector(x, y, z);
			v = rotateAroundAxisY(v, Bukkit.getPlayer(uid).getLocation().getYaw());
			spawnParticle(uid, particle, cloc.clone().add(v.getX(), v.getY(), v.getZ()), 0, 0.5, 0.09);
			x = x + 0.1;
		}

		// v = rotateAroundAxisY(v,
		// Bukkit.getPlayer(uid).getEyeLocation().getYaw());
		// loc.add(v.getX(), v.getY(), v.getZ());
		// double z = 0;

		// for (int t = 1; t != 11; t++) {
		// double x = 0;
		// double y = 2;
		//
		// double z = -0.5 + ((((double)1/10)*t));
		// Vector v = new Vector(x, y, z);
		// v = rotateAroundAxisY(v,
		// Bukkit.getPlayer(uid).getEyeLocation().getYaw()+90);
		// loc.add(v.getX(), 0, v.getZ());
		// spawnParticle(uid, particle, loc, 0, 0.5, 0.09);
		// z = z-0.1;
		// }

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
