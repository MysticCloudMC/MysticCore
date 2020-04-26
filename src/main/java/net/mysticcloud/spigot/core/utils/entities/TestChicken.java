package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.EntityChicken;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

public class TestChicken extends EntityChicken {

	public TestChicken(World world, EntityTypes<? extends EntityChicken> entityType) {
	    this(world);
	}
	public TestChicken(EntityTypes<? extends EntityChicken> entityType, World world) {
	    this(world);
	}

	public TestChicken(World world) {
	    super(EntityTypes.CHICKEN, world);
	}

	public void spawn(Location loc) {
		Bukkit.broadcastMessage("CustomChicken spawned!");
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

}
