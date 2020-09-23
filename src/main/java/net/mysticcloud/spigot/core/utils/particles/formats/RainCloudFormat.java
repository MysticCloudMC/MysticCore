package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class RainCloudFormat extends ParticleFormat {

	private Location cloc = null;
	double scalar = 6;

	public RainCloudFormat() {

		spots = 30;
		name = "&7Rain Cloud";
		guiItem = new ItemStack(Material.GRAY_DYE);
		particle = Particle.COMPOSTER;

	}

	@Override
	public void display(UUID uid, int i) {
		if (Bukkit.getPlayer(uid) != null)
			display(Bukkit.getPlayer(uid).getLocation(), i);
	}

	@Override
	public void display(Location loc, int i) {
		if (particle == null)
			return;
		cloc = loc.clone();
		if (i % 2 == 0)
			for (int a = 0; a != 11; a++) {
				for (int t = 0; t != spots + 1; t++) {
					if (0.1 > CoreUtils.getRandom().nextDouble() * 100) {

						for (int f = 0; f != 2; f++) {
							LinkedList<Vector> points = new LinkedList<>();
							points.add(new Vector(loc.getX(), loc.getY(), loc.getZ()));
							for (int z = 0; z != 10 * scalar; i++) {
								Vector point = points.get(points.size() - 1).clone();
								point.add(loc.add(0, (double) -(1 / scalar), 0).toVector());
								point.setX(point.getX() + (CoreUtils.getRandom().nextDouble()
										* (CoreUtils.getRandom().nextBoolean() ? (double) 1 / scalar
												: -(double) 1 / scalar)));
								point.setY(point.getY() + (CoreUtils.getRandom().nextDouble()
										* (CoreUtils.getRandom().nextBoolean() ? (double) 1 / scalar
												: -(double) 1 / scalar)));
								point.setZ(point.getZ() + (CoreUtils.getRandom().nextDouble()
										* (CoreUtils.getRandom().nextBoolean() ? (double) 1 / scalar
												: -(double) 1 / scalar)));
								spawnParticle(Particle.END_ROD,
										new Location(loc.getWorld(), point.getX(), point.getY(), point.getZ()));
							}
//							for (Vector vec : points) {
//								Location loca = new Location(loc.getWorld(), vec.getX(), vec.getY(), vec.getZ());
//								spawnParticle(Particle.END_ROD, loca);
//							}
						}

					} else {

						spawnParticle(Particle.CLOUD, cloc.clone().add(Math.cos(t * (360 / spots)) * (a * (r / 10)),
								h + 1, Math.sin(t * (360 / spots)) * (a * (r / 10))));
					}
				}

			}
	}

}
