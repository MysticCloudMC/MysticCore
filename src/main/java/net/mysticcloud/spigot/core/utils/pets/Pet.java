package net.mysticcloud.spigot.core.utils.pets;

import java.util.UUID;

import org.bukkit.Location;

import net.minecraft.server.v1_16_R2.Entity;

public interface Pet {
	
	public void spawn(Location loc, String owner);

	public Location getLocation();
	
	public Entity getEntity();
	
	public boolean isMountable();

	public String getPetOwner();


}
