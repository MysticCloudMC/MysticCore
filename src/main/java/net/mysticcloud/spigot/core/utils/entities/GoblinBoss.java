package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.EntityChicken;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import net.minecraft.server.v1_15_R1.EntityPigZombie;
import net.minecraft.server.v1_15_R1.EntityTurtle;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityVillagerAbstract;
import net.minecraft.server.v1_15_R1.EntityZombie;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_15_R1.World;

public class GoblinBoss extends EntityZombie {

	public GoblinBoss(World world, EntityTypes<? extends EntityZombie> entityType) {
		this(world);
	}

	public GoblinBoss(EntityTypes<? extends EntityZombie> entityType, World world) {
		this(world);
	}

	public GoblinBoss(World world) {
		super(EntityTypes.ZOMBIE, world);
	}

	public void spawn(Location loc) {
		Bukkit.broadcastMessage("CustomZombie spawned!");
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		setSlot(EnumItemSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
		setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
		setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
	}

	// public TestZombie(EntityTypes<? extends TestZombie> entitytypes, World
	// world) {
	// super((EntityTypes)entitytypes, world);
	// this.bA = new PathfinderGoalBreakDoor(this, bz);
	// }
	//
	// public TestZombie(World world) {
	// this(EntityTypes.ZOMBIE, world);
	// }
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		l();
	}

	@Override
	protected void l() {
		this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, true, 4, this::ey));
		this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.targetSelector.a(1,
				(new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[] { EntityPigZombie.class }));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityChicken.class, true));
		if (this.world.spigotConfig.zombieAggressiveTowardsVillager)
			this.targetSelector.a(3,
					new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, false));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
		this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityTurtle.class, 10, true, false,
				EntityTurtle.bw));
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35.0D);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.11500000417232513D);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(12.0D);
		getAttributeInstance(GenericAttributes.ARMOR).setValue(5.0D);
	}


	@Override
	public boolean isDrownConverting() {
		return ((Boolean) getDataWatcher().<Boolean>get(DROWN_CONVERTING)).booleanValue();
	}

	@Override
	protected boolean eq() {
		return true;
	}

}
