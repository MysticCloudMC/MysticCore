package net.mysticcloud.spigot.core.utils.pathfindergoals;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.NavigationAbstract;
import net.minecraft.server.v1_16_R2.PathEntity;
import net.minecraft.server.v1_16_R2.PathType;
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

	// Should start
	public boolean a() {
		return true;
	}

	// Start tick
	public void c() {
		if (owner != null) {
			PathEntity pathEntity = this.navigation.a(owner.getLocation().getX(), owner.getLocation().getY(),
					owner.getLocation().getZ(), 20);
			entity.a(PathType.WATER);
			this.navigation.a(pathEntity, speed);
		}
	}

	// Movement Tick
	public void e() {
		if (owner != null) {
			PathEntity pathEntity = this.navigation.a(owner.getLocation().getX(), owner.getLocation().getY(),
					owner.getLocation().getZ(), 1);
			if (distance() > 20) {
				entity.getBukkitEntity().teleport(owner);
			}
			if (distance() > 2)
				this.navigation.a(pathEntity, speed);
		}
	}

	private double distance() {
		return Math.sqrt(Math.pow(owner.getLocation().getX() - entity.locX(), 2)
				+ Math.pow(owner.getLocation().getY() - entity.locY(), 2));
	}
}
