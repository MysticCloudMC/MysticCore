package net.mysticcloud.spigot.core.utils.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityCaveSpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SpiderQueenMinion extends EntityCaveSpider {

	int z = 0;
	public List<Location> webs = new ArrayList<>();


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
		
		
		if(z % ((CoreUtils.getRandom().nextInt(1)+1)*100) == 0) {
			if (getBukkitEntity().getLocation().getBlock().getType().equals(Material.AIR)) {
				webs.add(getBukkitEntity().getLocation());
				getBukkitEntity().getLocation().getBlock().setType(Material.COBWEB);
			}
		}
		
		z = z + 1;
		
	}
	
	@Override
	public void die() {
		for(Location loc : webs) {
			loc.getBlock().setType(Material.AIR);
		}
		
		if(getBukkitEntity().hasMetadata("queen")) {
			((LivingEntity)Bukkit.getEntity((UUID) getBukkitEntity().getMetadata("queen").get(0).value())).damage(1);
		}
		
		super.die();
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
	}

}
