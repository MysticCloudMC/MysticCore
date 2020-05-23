package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityCaveSpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

public class SpiderQueenMinion extends EntityCaveSpider {

	int z = 0;


	public SpiderQueenMinion(World world, EntityTypes<? extends EntityCaveSpider> entityType) {
		this(world);
	}

	public SpiderQueenMinion(EntityTypes<? extends EntityCaveSpider> entityType, World world) {
		this(world);
	}

	public SpiderQueenMinion(World world) {
		super(EntityTypes.CAVE_SPIDER, world);
	}

	public void spawn(Location loc) {
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		
		
		
		
		z = z + 1;
		
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
