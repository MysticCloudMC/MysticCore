package net.mysticcloud.spigot.core.utils.portals;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.mysticcloud.spigot.core.utils.regions.Region;

public class Portal {

	String name;
	Region rg;

	String link = "";

	public Portal(String name, Region region) {
		this.name = name;
		this.rg = region;
	}

	public Region region() {
		return rg;
	}

	public String link() {
		return link;
	}

	public void link(String portal) {
		this.link = portal;
	}

	public String name() {
		return name;
	}

	public Location center() {
		double x = (rg.x1 + rg.x2) / 2;
		double z = (rg.z1 + rg.z2) / 2;
		Location loc = new Location(Bukkit.getWorld(rg.world()), x, rg.y1, z);
		loc.setY(loc.getBlockY() + 1);
		return loc;

	}

}
