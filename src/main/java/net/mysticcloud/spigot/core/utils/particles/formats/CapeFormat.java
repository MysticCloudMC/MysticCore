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
	double agl = 0;

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
		double y = -0.25;
		double z = -0.25;
		
		
		CoreUtils.debug(Math.abs((agl)-(cloc.getYaw())));
		
		if(Math.abs((agl)-(cloc.getYaw())) > 45){
			if(((agl)-(cloc.getYaw())) >= 0){
				agl = cloc.getYaw()-40;
			} else {
				agl = cloc.getYaw()+40;
			}
		}

		for (int t = 0; t != 10; t++) {
			Vector v = new Vector(x, y, z);
			v = rotateAroundAxisY(v, agl);
			spawnParticle(uid, particle, cloc.clone().add(v), 0, 0, 0);
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

	

}
