package net.mysticcloud.spigot.core.utils.entities;

import java.awt.Event;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityTransformEvent;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.AttributeModifier;
import net.minecraft.server.v1_15_R1.AttributeRanged;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.DynamicOpsNBT;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityChicken;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.EntityCreeper;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPigZombie;
import net.minecraft.server.v1_15_R1.EntityPose;
import net.minecraft.server.v1_15_R1.EntitySize;
import net.minecraft.server.v1_15_R1.EntityTurtle;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityVillager;
import net.minecraft.server.v1_15_R1.EntityVillagerAbstract;
import net.minecraft.server.v1_15_R1.EntityZombie;
import net.minecraft.server.v1_15_R1.EntityZombieVillager;
import net.minecraft.server.v1_15_R1.EnumDifficulty;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.EnumMonsterType;
import net.minecraft.server.v1_15_R1.GameRules;
import net.minecraft.server.v1_15_R1.GeneratorAccess;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.GroupDataEntity;
import net.minecraft.server.v1_15_R1.IAttribute;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IEntitySelector;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemMonsterEgg;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.Navigation;
import net.minecraft.server.v1_15_R1.PathfinderGoalBreakDoor;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.PathfinderGoalRemoveBlock;
import net.minecraft.server.v1_15_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.TagsFluid;
import net.minecraft.server.v1_15_R1.World;

public class TestZombie extends EntityZombie {
	protected static final IAttribute d = (new AttributeRanged(null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D))
			.a("Spawn Reinforcements Chance");

	private static final UUID b = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");

	private static final AttributeModifier c = new AttributeModifier(b, "Baby speed boost", 0.5D,
			AttributeModifier.Operation.MULTIPLY_BASE);

	private static final DataWatcherObject<Boolean> bw = DataWatcher.a((Class) TestZombie.class, DataWatcherRegistry.i);

	private static final DataWatcherObject<Integer> bx = DataWatcher.a((Class) TestZombie.class, DataWatcherRegistry.b);

	public static final DataWatcherObject<Boolean> DROWN_CONVERTING = DataWatcher.a((Class) TestZombie.class,
			DataWatcherRegistry.i);

	private static final Predicate<EnumDifficulty> bz;

	private final PathfinderGoalBreakDoor bA;

	private boolean bB;

	private int bC;

	public int drownedConversionTime;

	static {
		bz = (enumdifficulty -> (enumdifficulty == EnumDifficulty.HARD));
	}

	private int lastTick = MinecraftServer.currentTick;

	public TestZombie(World world, EntityTypes<? extends EntityZombie> entityType) {
		this(world);
	}

	public TestZombie(EntityTypes<? extends EntityZombie> entityType, World world) {
		this(world);
	}

	public TestZombie(World world) {
		super(EntityTypes.ZOMBIE, world);
		this.bA = new PathfinderGoalBreakDoor(this, bz);
	}

	public void spawn(Location loc) {
		Bukkit.broadcastMessage("CustomZombie spawned!");
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
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
		this.goalSelector.a(4, new a(this, 1.0D, 3));
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
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
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
		getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(4.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35.0D);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.11500000417232513D);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10.0D);
		getAttributeInstance(GenericAttributes.ARMOR).setValue(5.0D);
		getAttributeMap().b(d).setValue(this.random.nextDouble() * 0.10000000149011612D);
	}

	@Override
	protected void initDatawatcher() {
		super.initDatawatcher();
		getDataWatcher().register(bw, Boolean.valueOf(false));
		getDataWatcher().register(bx, Integer.valueOf(0));
		getDataWatcher().register(DROWN_CONVERTING, Boolean.valueOf(false));
	}

	@Override
	public boolean isDrownConverting() {
		return ((Boolean) getDataWatcher().<Boolean>get(DROWN_CONVERTING)).booleanValue();
	}

	@Override
	public boolean ey() {
		return this.bB;
	}

	@Override
	public void s(boolean flag) {
		if (eq()) {
			if (this.bB != flag) {
				this.bB = flag;
				((Navigation) getNavigation()).a(flag);
				if (flag) {
					this.goalSelector.a(1, this.bA);
				} else {
					this.goalSelector.a(this.bA);
				}
			}
		} else if (this.bB) {
			this.goalSelector.a(this.bA);
			this.bB = false;
		}
	}

	@Override
	protected boolean eq() {
		return true;
	}

	@Override
	public boolean isBaby() {
		return ((Boolean) getDataWatcher().<Boolean>get(bw)).booleanValue();
	}

	@Override
	protected int getExpValue(EntityHuman entityhuman) {
		if (isBaby())
			this.f = (int) (this.f * 2.5F);
		return super.getExpValue(entityhuman);
	}

	@Override
	public void setBaby(boolean flag) {
		getDataWatcher().set(bw, Boolean.valueOf(flag));
		if (this.world != null && !this.world.isClientSide) {
			AttributeInstance attributeinstance = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
			attributeinstance.removeModifier(c);
			if (flag)
				attributeinstance.addModifier(c);
		}
	}

	@Override
	public void a(DataWatcherObject<?> datawatcherobject) {
		if (bw.equals(datawatcherobject))
			updateSize();
		super.a(datawatcherobject);
	}

	@Override
	protected boolean et() {
		return true;
	}

	@Override
	public void tick() {
		if (!this.world.isClientSide && isAlive())
			if (isDrownConverting()) {
				int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
				this.lastTick = MinecraftServer.currentTick;
				this.drownedConversionTime -= elapsedTicks;
				if (this.drownedConversionTime < 0)
					ev();
			} else if (et()) {
				if (a(TagsFluid.WATER)) {
					this.bC++;
					if (this.bC >= 600)
						startDrownedConversion(300);
				} else {
					this.bC = -1;
				}
			}
		super.tick();
	}

	@Override
	public void movementTick() {
		if (isAlive()) {
			boolean flag = (K_() && en());
			if (flag) {
				ItemStack itemstack = getEquipment(EnumItemSlot.HEAD);
				if (!itemstack.isEmpty()) {
					if (itemstack.e()) {
						itemstack.setDamage(itemstack.getDamage() + this.random.nextInt(2));
						if (itemstack.getDamage() >= itemstack.h()) {
							setSlot(EnumItemSlot.HEAD, ItemStack.a);
						}
					}
					flag = false;
				}
				if (flag)
					setOnFire(8);
			}
		}
		super.movementTick();
	}

	@Override
	public void startDrownedConversion(int i) {
		this.lastTick = MinecraftServer.currentTick;
		this.drownedConversionTime = i;
		getDataWatcher().set(DROWN_CONVERTING, Boolean.valueOf(true));
	}

	@Override
	protected void ev() {
		b((EntityTypes) EntityTypes.DROWNED);
		this.world.a((EntityHuman) null, 1040, new BlockPosition(this), 0);
	}

	@Override
	protected void b(EntityTypes<? extends EntityZombie> entitytypes) {
		if (!this.dead) {
			TestZombie entityzombie = (TestZombie) entitytypes.a(this.world);
			entityzombie.u(this);
			entityzombie.setCanPickupLoot(canPickupLoot());
			entityzombie.s((entityzombie.eq() && ey()));
			entityzombie.v(entityzombie.world.getDamageScaler(new BlockPosition(entityzombie)).d());
			entityzombie.setBaby(isBaby());
			entityzombie.setNoAI(isNoAI());
			EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
			int i = aenumitemslot.length;
			for (int j = 0; j < i; j++) {
				EnumItemSlot enumitemslot = aenumitemslot[j];
				ItemStack itemstack = getEquipment(enumitemslot);
				if (!itemstack.isEmpty()) {
					entityzombie.setSlot(enumitemslot, itemstack.cloneItemStack());
					entityzombie.a(enumitemslot, d(enumitemslot));
					itemstack.setCount(0);
				}
			}
			if (hasCustomName()) {
				entityzombie.setCustomName(getCustomName());
				entityzombie.setCustomNameVisible(getCustomNameVisible());
			}
			if (isPersistent())
				entityzombie.setPersistent();
			entityzombie.setInvulnerable(isInvulnerable());
			if (CraftEventFactory
					.callEntityTransformEvent(this, entityzombie, EntityTransformEvent.TransformReason.DROWNED)
					.isCancelled()) {
				((Zombie) getBukkitEntity()).setConversionTime(-1);
				return;
			}
			this.world.addEntity(entityzombie, CreatureSpawnEvent.SpawnReason.DROWNED);
			die();
		}
	}

	@Override
	public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
		ItemStack itemstack = entityhuman.b(enumhand);
		Item item = itemstack.getItem();
		if (item instanceof ItemMonsterEgg && ((ItemMonsterEgg) item).a(itemstack.getTag(), getEntityType())) {
			if (!this.world.isClientSide) {
				TestZombie entityzombie = (TestZombie) getEntityType().a(this.world);
				if (entityzombie != null) {
					entityzombie.setBaby(true);
					entityzombie.setPositionRotation(locX(), locY(), locZ(), 0.0F, 0.0F);
					this.world.addEntity(entityzombie);
					if (itemstack.hasName())
						entityzombie.setCustomName(itemstack.getName());
					if (!entityhuman.abilities.canInstantlyBuild)
						itemstack.subtract(1);
				}
			}
			return true;
		}
		return super.a(entityhuman, enumhand);
	}

	@Override
	protected boolean K_() {
		return true;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (super.damageEntity(damagesource, f)) {
			EntityLiving entityliving = getGoalTarget();
			if (entityliving == null && damagesource.getEntity() instanceof EntityLiving)
				entityliving = (EntityLiving) damagesource.getEntity();
			if (entityliving != null && this.world.getDifficulty() == EnumDifficulty.HARD
					&& this.random.nextFloat() < getAttributeInstance(d).getValue()
					&& this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
				int i = MathHelper.floor(locX());
				int j = MathHelper.floor(locY());
				int k = MathHelper.floor(locZ());
				TestZombie entityzombie = new TestZombie(this.world);
				for (int l = 0; l < 50; l++) {
					int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
					int j1 = j + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
					int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
					BlockPosition blockposition = new BlockPosition(i1, j1 - 1, k1);
					// if (this.world.getType(blockposition).a(this.world,
					// blockposition, entityzombie) &&
					// this.world.getLightLevel(new BlockPosition(i1, j1, k1)) <
					// 10) {
					// entityzombie.setPosition(i1, j1, k1);
					// if (!this.world.isPlayerNearby(i1, j1, k1, 7.0D) &&
					// this.world.i(entityzombie) &&
					// this.world.getCubes(entityzombie) &&
					// !this.world.containsLiquid(entityzombie.getBoundingBox()))
					// {
					// this.world.addEntity(entityzombie,
					// CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
					// entityzombie.setGoalTarget(entityliving,
					// EntityTargetEvent.TargetReason.REINFORCEMENT_TARGET,
					// true);
					// entityzombie.prepare(this.world,
					// this.world.getDamageScaler(new
					// BlockPosition(entityzombie)), EnumMobSpawn.REINFORCEMENT,
					// (GroupDataEntity)null, (NBTTagCompound)null);
					// getAttributeInstance(d).addModifier(new
					// AttributeModifier("Zombie reinforcement caller charge",
					// -0.05000000074505806D,
					// AttributeModifier.Operation.ADDITION));
					// entityzombie.getAttributeInstance(d).addModifier(new
					// AttributeModifier("Zombie reinforcement callee charge",
					// -0.05000000074505806D,
					// AttributeModifier.Operation.ADDITION));
					// break;
					// }
					// }
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean B(Entity entity) {
		boolean flag = super.B(entity);

		return flag;
	}

	@Override
	protected SoundEffect getSoundAmbient() {
		return SoundEffects.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEffect getSoundHurt(DamageSource damagesource) {
		return SoundEffects.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEffect getSoundDeath() {
		return SoundEffects.ENTITY_ZOMBIE_DEATH;
	}

	@Override
	protected SoundEffect getSoundStep() {
		return SoundEffects.ENTITY_ZOMBIE_STEP;
	}

	@Override
	protected void a(BlockPosition blockposition, IBlockData iblockdata) {
		a(getSoundStep(), 0.15F, 1.0F);
	}

	@Override
	public EnumMonsterType getMonsterType() {
		return EnumMonsterType.UNDEAD;
	}

	@Override
	protected void a(DifficultyDamageScaler difficultydamagescaler) {
		super.a(difficultydamagescaler);
		if (this.random.nextFloat() < ((this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.05F : 0.01F)) {
			int i = this.random.nextInt(3);
			if (i == 0) {
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			} else {
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
			}
		}
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		if (isBaby())
			nbttagcompound.setBoolean("IsBaby", true);
		nbttagcompound.setBoolean("CanBreakDoors", ey());
		nbttagcompound.setInt("InWaterTime", isInWater() ? this.bC : -1);
		nbttagcompound.setInt("DrownedConversionTime", isDrownConverting() ? this.drownedConversionTime : -1);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		if (nbttagcompound.getBoolean("IsBaby"))
			setBaby(true);
		s(nbttagcompound.getBoolean("CanBreakDoors"));
		this.bC = nbttagcompound.getInt("InWaterTime");
		if (nbttagcompound.hasKeyOfType("DrownedConversionTime", 99)
				&& nbttagcompound.getInt("DrownedConversionTime") > -1)
			startDrownedConversion(nbttagcompound.getInt("DrownedConversionTime"));
	}

	@Override
	public void b(EntityLiving entityliving) {

	}

	@Override
	protected float b(EntityPose entitypose, EntitySize entitysize) {
		return isBaby() ? 0.93F : 1.74F;
	}

	@Override
	protected boolean g(ItemStack itemstack) {
		return (itemstack.getItem() == Items.EGG && isBaby() && isPassenger()) ? false : super.g(itemstack);
	}

	@Override
	@Nullable
	public GroupDataEntity prepare(GeneratorAccess generatoraccess, DifficultyDamageScaler difficultydamagescaler,
			EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity,
			@Nullable NBTTagCompound nbttagcompound) {
		Object object = super.prepare(generatoraccess, difficultydamagescaler, enummobspawn, groupdataentity,
				nbttagcompound);

		return (GroupDataEntity) object;
	}

	@Override
	protected void v(float f) {
		getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE)
				.addModifier(new AttributeModifier("Random spawn bonus",
						this.random.nextDouble() * 0.05000000074505806D, AttributeModifier.Operation.ADDITION));
		double d0 = this.random.nextDouble() * 1.5D * f;
		if (d0 > 1.0D)
			getAttributeInstance(GenericAttributes.FOLLOW_RANGE).addModifier(
					new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
		if (this.random.nextFloat() < f * 0.05F) {
			getAttributeInstance(d).addModifier(new AttributeModifier("Leader zombie bonus",
					this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADDITION));
			getAttributeInstance(GenericAttributes.MAX_HEALTH).addModifier(new AttributeModifier("Leader zombie bonus",
					this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
			s(eq());
		}
	}

	@Override
	public double aR() {
		return isBaby() ? 0.0D : -0.45D;
	}

	@Override
	protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
		super.dropDeathLoot(damagesource, i, flag);
		Entity entity = damagesource.getEntity();
		if (entity instanceof EntityCreeper) {
			EntityCreeper entitycreeper = (EntityCreeper) entity;
			if (entitycreeper.canCauseHeadDrop()) {
				entitycreeper.setCausedHeadDrop();
				ItemStack itemstack = es();
				if (!itemstack.isEmpty())
					a(itemstack);
			}
		}
	}

	@Override
	protected ItemStack es() {
		return new ItemStack(Items.ZOMBIE_HEAD);
	}

	class a extends PathfinderGoalRemoveBlock {
		a(EntityCreature entitycreature, double d0, int i) {
			super(Blocks.TURTLE_EGG, entitycreature, d0, i);
		}

		public void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
			generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_ZOMBIE_DESTROY_EGG,
					SoundCategory.HOSTILE, 0.5F, 0.9F + TestZombie.this.random.nextFloat() * 0.2F);
		}

		public void a(World world, BlockPosition blockposition) {
			world.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_BREAK,
					SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
		}

		public double h() {
			return 1.14D;
		}
	}

	public class GroupDataZombie implements GroupDataEntity {
		public final boolean a;

		private GroupDataZombie(boolean flag) {
			this.a = flag;
		}
	}
}
