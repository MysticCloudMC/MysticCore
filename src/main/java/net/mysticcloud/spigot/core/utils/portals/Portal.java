package net.mysticcloud.spigot.core.utils.portals;

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

}
