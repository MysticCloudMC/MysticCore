package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
		Location loc = Bukkit.getPlayer(uid).getLocation().clone();
		
		 double r = 2;
		 double t = Bukkit.getPlayer(uid).getEyeLocation().getYaw();
//         double x = r * Math.cos(t);
		 double y = Bukkit.getPlayer(uid).getEyeLocation().getY();
//         double z = r * Math.sin(t);
         double x = Bukkit.getPlayer(uid).getEyeLocation().getX();
         double z = Bukkit.getPlayer(uid).getEyeLocation().getZ();
         Vector v = new Vector(x, y, z);
         v = rotateAroundAxisY(v, t);
         loc.add(v.getX(), v.getY(), v.getZ());
         
         spawnParticle(uid, particle, Bukkit.getPlayer(uid).getLocation().clone().add(
 				2,0,0),0,-0.5,0);

		
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
