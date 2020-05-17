package net.mysticcloud.spigot.core.runnables;

import java.util.UUID;

import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;

public class TimeoutTeleportationRequest implements Runnable {

	UUID player;
	UUID other;

	public TimeoutTeleportationRequest(UUID player, UUID other) {
		this.player = player;
		this.other = other;
	}

	@Override
	public void run() {
		TeleportUtils.timeoutRequest(player,other);
		
	}

}
