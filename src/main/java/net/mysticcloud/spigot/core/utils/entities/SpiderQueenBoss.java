package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;

public class SpiderQueenBoss extends EntitySpider {

	int z = 0;
	private CircleFeetFormat format = new CircleFeetFormat();


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
		format.setHeight(1);
		format.setRadius(1.25);
		format.setSpots(20);
		format.particle(Particle.REDSTONE);
		format.setDustOptions(new DustOptions(Color.WHITE, 1));
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		
		
		if(z%500 == 0) {
			new SpiderQueenMinion(world).spawn(getBukkitEntity().getLocation());
		}
		format.display(getBukkitEntity().getLocation(), z);
		z = z + 1;
		
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
