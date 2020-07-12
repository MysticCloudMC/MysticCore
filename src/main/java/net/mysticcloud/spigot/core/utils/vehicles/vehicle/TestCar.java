package net.mysticcloud.spigot.core.utils.vehicles.vehicle;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R1.EntityBoat;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import net.mysticcloud.spigot.core.utils.vehicles.Vehicle;

public class TestCar extends EntityBoat implements Vehicle {

	public TestCar(World world, EntityTypes<? extends EntityBoat> entityType) {
		this(world);
	}

	public TestCar(EntityTypes<? extends EntityBoat> entityType, World world) {
		this(world);
	}

	public TestCar(World world) {
		super(EntityTypes.BOAT, world);
	}

	public void spawn(Location loc) {
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

}