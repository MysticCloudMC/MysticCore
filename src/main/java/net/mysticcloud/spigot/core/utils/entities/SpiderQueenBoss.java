package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

public class SpiderQueenBoss extends EntitySpider {

	int z = 0;


	public SpiderQueenBoss(World world, EntityTypes<? extends EntitySpider> entityType) {
		this(world);
	}

	public SpiderQueenBoss(EntityTypes<? extends EntitySpider> entityType, World world) {
		this(world);
	}

	public SpiderQueenBoss(World world) {
		super(EntityTypes.SPIDER, world);
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
		
		
		if(z%1000 == 0) {
			new SpiderQueenMinion(world).spawn(getBukkitEntity().getLocation());
		}
		
		z = z + 1;
		
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
