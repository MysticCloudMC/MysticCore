package net.mysticcloud.spigot.core.utils.pathfindergoals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.NavigationAbstract;
import net.minecraft.server.v1_16_R2.PathEntity;
import net.minecraft.server.v1_16_R2.PathfinderGoal;

public class PathfinderGoalWalkToLoc extends PathfinderGoal {
	private double speed;

	private EntityInsentient entity;


	private NavigationAbstract navigation;

	LivingEntity owner;

	public PathfinderGoalWalkToLoc(EntityInsentient entity, double speed) {
		this.entity = entity;
		this.navigation = this.entity.getNavigation();
		this.speed = speed;
	}

	public void setOwner(LivingEntity owner) {
		this.owner = owner;
	}

	//Should start
	public boolean a() {
		return true;
	}

	//Start tick
	public void c() {
		if (owner != null) {
			PathEntity pathEntity = this.navigation.a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 20);
			this.navigation.a(pathEntity, speed);
		}
	}
	
	//Movement Tick
	public void e() {
		if (owner != null) {
			PathEntity pathEntity = this.navigation.a(owner.getLocation().getX(), owner.getLocation().getY(), owner.getLocation().getZ(), 1);
			this.navigation.a(pathEntity, speed);
		}
	}
}
