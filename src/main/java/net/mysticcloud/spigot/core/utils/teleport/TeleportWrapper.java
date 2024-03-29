package net.mysticcloud.spigot.core.utils.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportWrapper {

	Location start;
	Player player;
	Integer id;

	public TeleportWrapper(Player player, Location start, int id) {
		this.start = start;
		this.id = id;
		this.player = player;
	}

	public Location getStartingPoint() {
		return start;
	}

	public int getID() {
		return id;
	}

}
