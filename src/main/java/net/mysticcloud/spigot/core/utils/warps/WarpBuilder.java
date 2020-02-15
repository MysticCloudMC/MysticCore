package net.mysticcloud.spigot.core.utils.warps;

import org.bukkit.Location;

public class WarpBuilder {

	private Warp warp;
	private String type = "warp";

	public WarpBuilder() {
	}

	public WarpBuilder createWarp() {
		warp = new Warp(WarpUtils.getAllWarps().size());
		return this;
	}

	public WarpBuilder setMetadata(String key, Object value) {
		warp.metadata(key, value);
		return this;
	}

	public WarpBuilder setLocation(Location location) {
		warp.location(location);
		return this;
	}

	public WarpBuilder setName(String name) {
		warp.name(name);
		return this;
	}

	public WarpBuilder setType(String type) {
		this.type = type;
		return this;
	}

	public Warp getWarp() {
		if (warp != null)
			if (warp.name() != null)
				if (warp.location() != null)
					if (type != null) {
						WarpUtils.addWarp(type, warp);
						return warp;
					}
		return null;
	}

}
