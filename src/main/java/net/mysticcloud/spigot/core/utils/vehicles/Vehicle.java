package net.mysticcloud.spigot.core.utils.vehicles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class Vehicle {
	
	int id;
	EntityType type;
	float speed = 1;
	
	Entity passenger = null;
	LivingEntity v = null;
	
	
	protected Vehicle(int id, EntityType type,Location loc) {
		Entity e = loc.getWorld().spawnEntity(loc, type);
		if(!(e instanceof LivingEntity)) {
			Bukkit.broadcastMessage("ERROR");
			return;
		}
		LivingEntity v = (LivingEntity) e;
		v.setAI(false);
		this.v = v;
		
	}


	public void move() {
		if(passenger!=null) {
			((CraftLivingEntity)v).getHandle();
			
		}
		
	}

}
