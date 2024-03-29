package net.mysticcloud.spigot.core.utils.regions;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Region {

	String name;
	String world;
	public double x1;
	public double y1;
	public double z1;
	public double x2;
	public double y2;
	public double z2;

	Region(String name, String world, double x1, double y1, double z1, double x2, double y2, double z2) {
		this.name = name;
		this.world = world;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.z1 = z1;
		this.z2 = z2;
	}

	public boolean inside(Entity entity) {
		return inside(entity.getLocation());
	}

	public boolean inside(Location loc) {
		if (loc.getWorld().getName().equalsIgnoreCase(world)) {
			if (x1 <= loc.getX() && x2 >= loc.getX()) {
				if (y1 <= loc.getY() && y2 >= loc.getY()) {
					if (z1 <= loc.getZ() && z2 >= loc.getZ()) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public String world() {
		return world;
	}

	public String name() {
		return name;
	}
}
