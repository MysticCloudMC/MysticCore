package net.mysticcloud.spigot.core.utils.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.mysticcloud.spigot.core.utils.regions.Region;

public class Portal {

	String name;
	Region rg;

	Portal link = null;

	public Portal(String name, Region region) {
		this.name = name;
		this.rg = region;
	}

	public Region region() {
		return rg;
	}

	public Portal link() {
		return link;
	}

	public void link(Portal portal) {
		this.link = portal;
	}

	public Location center() {
		double x = (rg.x1 + rg.x2) / 2;
		double z = (rg.z1 + rg.z2) / 2;
		return new Location(Bukkit.getWorld(rg.world()), x, rg.y1, z);

	}

}
