package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityCaveSpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;

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
		getBukkitEntity().setCustomName(CoreUtils.colorize("&e" + Bosses.SPIDER_QUEEN_MINION.getFormattedCallName()));
		setCustomNameVisible(true);
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		
		
		if(z%100==0) {
			getBukkitEntity().getLocation().getBlock().setType(Material.COBWEB);
		}
		
		z = z + 1;
		
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
