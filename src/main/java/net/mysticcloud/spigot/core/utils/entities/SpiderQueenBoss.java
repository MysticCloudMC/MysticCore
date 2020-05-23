package net.mysticcloud.spigot.core.utils.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;

public class SpiderQueenBoss extends EntitySpider {

	int z = 0;
	private CircleFeetFormat format = new CircleFeetFormat();
	
	public List<SpiderQueenMinion> minions = new ArrayList<>();


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
		getBukkitEntity().setCustomName(CoreUtils.colorize("&e" + Bosses.SPIDER_QUEEN_BOSS.getFormattedCallName()));
		setCustomNameVisible(true);
		format.setHeight(1);
		format.setRadius(1.25);
		format.setSpots(20);
		format.particle(Particle.REDSTONE);
		format.setDustOptions(new DustOptions(Color.WHITE, 1));
	}
	
	@Override
	public void die() {
		
		for(SpiderQueenMinion minion : minions) {
			minion.killEntity();
		}
		
		super.die();
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		
		
		if(z%500 == 0) {
			SpiderQueenMinion minion = new SpiderQueenMinion(world);
			minion.spawn(getBukkitEntity().getLocation());
			minions.add(minion);
		}
		format.display(getBukkitEntity().getLocation(), z);
		z = z + 1;
		
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
