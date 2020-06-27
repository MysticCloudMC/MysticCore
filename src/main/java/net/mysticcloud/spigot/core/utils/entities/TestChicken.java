package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.DamageSource;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityAgeable;
import net.minecraft.server.v1_16_R1.EntityChicken;
import net.minecraft.server.v1_16_R1.EntityHuman;
import net.minecraft.server.v1_16_R1.EntityLiving;
import net.minecraft.server.v1_16_R1.EntityPose;
import net.minecraft.server.v1_16_R1.EntitySize;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.GenericAttributes;
import net.minecraft.server.v1_16_R1.IBlockData;
import net.minecraft.server.v1_16_R1.IMaterial;
import net.minecraft.server.v1_16_R1.ItemStack;
import net.minecraft.server.v1_16_R1.Items;
import net.minecraft.server.v1_16_R1.MathHelper;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.PathfinderGoalBreed;
import net.minecraft.server.v1_16_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R1.PathfinderGoalFollowParent;
import net.minecraft.server.v1_16_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_16_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R1.PathfinderGoalTempt;
import net.minecraft.server.v1_16_R1.RecipeItemStack;
import net.minecraft.server.v1_16_R1.SoundEffect;
import net.minecraft.server.v1_16_R1.SoundEffects;
import net.minecraft.server.v1_16_R1.World;

public class TestChicken extends EntityChicken {

	private static final RecipeItemStack bD = RecipeItemStack
			.a(new IMaterial[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });

	public float bw;

	public float bx;

	public float by;

	public float bz;

	public float bA = 1.0F;

	public int eggLayTime;

	public boolean bC;

	private int z = 1;

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
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));
		this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.0D, false, bD));
		this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
		this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	}

	@Override
	protected float b(EntityPose entitypose, EntitySize entitysize) {
		return isBaby() ? (entitysize.height * 0.85F) : (entitysize.height * 1.92F);
	}

	@Override
	public boolean b(float f, float f1) {
		return false;
	}

	@Override
	protected SoundEffect getSoundAmbient() {
		return SoundEffects.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEffect getSoundHurt(DamageSource damagesource) {
		return SoundEffects.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEffect getSoundDeath() {
		return SoundEffects.ENTITY_CHICKEN_DEATH;
	}

	@Override
	public EntityChicken createChild(EntityAgeable entityageable) {
		return EntityTypes.CHICKEN.a(this.world);
	}

	@Override
	public boolean i(ItemStack itemstack) {
		return bD.test(itemstack);
	}

	@Override
	protected int getExpValue(EntityHuman entityhuman) {
		return isChickenJockey() ? 10 : super.getExpValue(entityhuman);
	}

	@Override
	public boolean isTypeNotPersistent(double d0) {
		return (isChickenJockey() && !isVehicle());
	}

	@Override
	public boolean isChickenJockey() {
		return this.bC;
	}

}