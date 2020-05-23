package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.EntityCreeper;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.IMonster;
import net.minecraft.server.v1_15_R1.PathfinderGoalDefendVillage;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_15_R1.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalOfferFlower;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.PathfinderGoalStrollVillage;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;

public class IronBoss extends EntityIronGolem {

	private int z = 1;
	

	private SelectorFormat format = new SelectorFormat();

	public IronBoss(World world, EntityTypes<? extends EntityIronGolem> entityType) {
		this(world);
	}

	public IronBoss(EntityTypes<? extends EntityIronGolem> entityType, World world) {
		this(world);
	}

	public IronBoss(World world) {
		super(EntityTypes.IRON_GOLEM, world);
	}

	public void spawn(Location loc) {
		
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		format.particle(Particle.FLAME);
		format.setHeight(3);
		format.setColumns(4);
		format.setLength(1);
		format.setRadius(1.25);

	}
	@Override
	protected void initPathfinder() {
	    this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
	    this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
	    this.goalSelector.a(2, new PathfinderGoalStrollVillage(this, 0.6D));
	    this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, false, 4, () -> false));
	    this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
	    this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.6D));
	    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, (Class)EntityHuman.class, 6.0F));
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	    this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
	    
	    //This is from zombies
	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	    
	    //This should b 2
	    this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, new Class[0]));
	    
//	    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 5, false, false, entityliving -> 
//	          (entityliving instanceof IMonster && !(entityliving instanceof EntityCreeper))));
	  }
	
	@Override
	public void movementTick() {
		super.movementTick();
		format.display(new Location(Bukkit.getWorld(world.getWorld().getName()), locX(), locY(), locZ()),z);
		
		
		z = z+1;
		
	}

}
