package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityAgeable;
import net.minecraft.server.v1_15_R1.EntityChicken;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPose;
import net.minecraft.server.v1_15_R1.EntitySize;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IMaterial;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.RecipeItemStack;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;

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
	
	private CircleFeetFormat format = new CircleFeetFormat();

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

	@Override
	protected void initPathfinder() {
//		this.goalSelector.a(0, new PathfinderGoalFloat(this));
//		this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));
//		this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
//		this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.0D, false, bD));
//		this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
//		this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
//		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
//		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
	}

	@Override
	protected float b(EntityPose entitypose, EntitySize entitysize) {
		return isBaby() ? (entitysize.height * 0.85F) : (entitysize.height * 1.92F);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(4.0D);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
	}

	@Override
	public void movementTick() {
//		if (isChickenJockey())
//			this.persistent = !isTypeNotPersistent(0.0D);
//		super.movementTick();
//		this.bz = this.bw;
//		this.by = this.bx;
//		this.bx = (float) (this.bx + (this.onGround ? -1 : 4) * 0.3D);
//		this.bx = MathHelper.a(this.bx, 0.0F, 1.0F);
//		if (!this.onGround && this.bA < 1.0F)
//			this.bA = 1.0F;
//		this.bA = (float) (this.bA * 0.9D);
//		Vec3D vec3d = getMot();
//		if (!this.onGround && vec3d.y < 0.0D)
//			setMot(vec3d.d(1.0D, 0.6D, 1.0D));
//		this.bw += this.bA * 2.0F;
		
		format.display(getBukkitEntity().getLocation(), z);
		
		z = z + 1;
		
//		a(SoundEffects.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
		if (!this.world.isClientSide && isAlive() && !isBaby() && !isChickenJockey() && --this.eggLayTime <= 0) {
//			a(SoundEffects.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			a(Items.EGG);
			this.eggLayTime = this.random.nextInt(100) + 1;
		}
		
		setMot(0,getMot().getY(),0);
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
	protected void a(BlockPosition blockposition, IBlockData iblockdata) {
		a(SoundEffects.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
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
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.bC = nbttagcompound.getBoolean("IsChickenJockey");
		if (nbttagcompound.hasKey("EggLayTime"))
			this.eggLayTime = nbttagcompound.getInt("EggLayTime");
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		nbttagcompound.setBoolean("IsChickenJockey", this.bC);
		nbttagcompound.setInt("EggLayTime", this.eggLayTime);
	}

	@Override
	public boolean isTypeNotPersistent(double d0) {
		return (isChickenJockey() && !isVehicle());
	}

	@Override
	public void k(Entity entity) {
		super.k(entity);
		float f = MathHelper.sin(this.aI * 0.017453292F);
		float f1 = MathHelper.cos(this.aI * 0.017453292F);
		entity.setPosition(locX() + (0.1F * f), e(0.5D) + entity.aR() + 0.0D, locZ() - (0.1F * f1));
		if (entity instanceof EntityLiving)
			((EntityLiving) entity).aI = this.aI;
	}

	@Override
	public boolean isChickenJockey() {
		return this.bC;
	}

	@Override
	public void r(boolean flag) {
		this.bC = flag;
	}

}
