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

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainbowFormat extends ParticleFormat {

	Location cloc = null;
	double r = 1;

	public RainbowFormat() {
		name = "&cR&ea&ain&bb&2o&5w";
		guiItem = new ItemStack(Material.COAL);
		particle = Particle.REDSTONE;
	}
	@Override
	public void display(UUID uid, int i) {
		if (Bukkit.getPlayer(uid) != null) 
			display(Bukkit.getPlayer(uid).getEyeLocation(), i);
	}

	@Override
	public void display(Location loc, int i) {

		if (particle == null)
			return;

		cloc = loc.clone();

		for (int b = 0; b != 6; b++) {
//			if (b == 0)
//				color = Color.RED;
//			if (b == 1)
//				color = Color.ORANGE;
//			if (b == 2)
//				color = Color.YELLOW;
//			if (b == 3)
//				color = Color.GREEN;
//			if (b == 4)
//				color = Color.BLUE;
//			if (b == 5)
//				color = Color.PURPLE;
//			Color.fromRGB(red, green, blue)
			java.awt.Color color = CoreUtils.generateColor(b+1, 1,127);
			setDustOptions(new DustOptions(Color.fromRGB(color.getRed(),color.getBlue(),color.getGreen()), 1));
			for (int a = 1; a != 21; a++) {
				Vector v = new Vector(0, 2 + Math.cos(Math.toRadians(a + 30) * ((360) / (40))) * (r - (0.1 * b)),
						Math.sin(Math.toRadians(a + 30) * ((360) / (40))) * (r - (0.1 * b)));
				// v = rotateAroundAxisX(v,90);
				v = rotateAroundAxisY(v, cloc.getYaw() + 90);
				spawnParticle(particle, loc.clone().add(v));

			}
		}

	}

}
