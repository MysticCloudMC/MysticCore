package net.mysticcloud.spigot.core.utils.pets.pet;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R2.AttributeProvider;
import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.EntityLiving;
import net.minecraft.server.v1_16_R2.EntityPose;
import net.minecraft.server.v1_16_R2.EntitySize;
import net.minecraft.server.v1_16_R2.EntitySnowball;
import net.minecraft.server.v1_16_R2.EntitySnowman;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EnumHand;
import net.minecraft.server.v1_16_R2.EnumInteractionResult;
import net.minecraft.server.v1_16_R2.GenericAttributes;
import net.minecraft.server.v1_16_R2.ItemStack;
import net.minecraft.server.v1_16_R2.Items;
import net.minecraft.server.v1_16_R2.MathHelper;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.SoundCategory;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.SoundEffects;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.Bosses;
import net.mysticcloud.spigot.core.utils.pathfindergoals.PathfinderGoalWalkToLoc;
import net.mysticcloud.spigot.core.utils.pets.Pet;

public class Snowman extends EntitySnowman implements Pet {

	PathfinderGoalWalkToLoc pf;
	String owner;
	
	String prefix = "&f";
	String suffix = "&f&lSnowman";

	public Snowman(World world, EntityTypes<? extends EntitySnowman> entityType) {
		this(world);
	}

	public Snowman(EntityTypes<? extends EntitySnowman> entityType, World world) {
		this(world);
	}

	public Snowman(World world) {
		super(EntityTypes.SNOW_GOLEM, world);
	}

	public void spawn(Location loc, String owner) {
		this.owner = owner;
		setBaby(true);
		pf.setOwner(Bukkit.getPlayer(owner));
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		getBukkitEntity().setCustomName(CoreUtils.colorize(prefix + owner + (owner.endsWith("s") ? "' " : "'s ") + suffix));
		setCustomNameVisible(true);

	}

	protected void initPathfinder() {
		pf = new PathfinderGoalWalkToLoc(this, 1.5D);
		this.goalSelector.a(1, pf);
//		this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
//		this.goalSelector.a(2, new PathfinderGoalRandomStrollLand(this, 1.0D, 1.0000001E-5F));
//		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
//		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
//		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 10, true,
//				false, entityliving -> entityliving instanceof IMonster));

	}

	public static AttributeProvider.Builder m() {
		return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 4.0D).a(GenericAttributes.MOVEMENT_SPEED,
				0.20000000298023224D);
	}

	public void saveData(NBTTagCompound nbttagcompound) {
		super.saveData(nbttagcompound);
		nbttagcompound.setBoolean("Pumpkin", hasPumpkin());
	}

	public void loadData(NBTTagCompound nbttagcompound) {
		super.loadData(nbttagcompound);
		if (nbttagcompound.hasKey("Pumpkin"))
			setHasPumpkin(nbttagcompound.getBoolean("Pumpkin"));
	}

	public boolean dN() {
		return true;
	}

//	@Override
//	public void movementTick() {
////		super.movementTick();
//		pf.setOwner(Bukkit.getPlayer(owner));
////		if (!this.world.isClientSide) {
////			int i = MathHelper.floor(locX());
////			int j = MathHelper.floor(locY());
////			int k = MathHelper.floor(locZ());
////			if (this.world.getBiome(new BlockPosition(i, 0, k))
////					.getAdjustedTemperature(new BlockPosition(i, j, k)) > 1.0F)
////				damageEntity(CraftEventFactory.MELTING, 1.0F);
////			if (!this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING))
////				return;
////			IBlockData iblockdata = Blocks.SNOW.getBlockData();
////			for (int l = 0; l < 4; l++) {
////				i = MathHelper.floor(locX() + ((l % 2 * 2 - 1) * 0.25F));
////				j = MathHelper.floor(locY());
////				k = MathHelper.floor(locZ() + ((l / 2 % 2 * 2 - 1) * 0.25F));
////				BlockPosition blockposition = new BlockPosition(i, j, k);
////				if (this.world.getType(blockposition).isAir()
////						&& this.world.getBiome(blockposition).getAdjustedTemperature(blockposition) < 0.8F
////						&& iblockdata.canPlace(this.world, blockposition))
////					CraftEventFactory.handleBlockFormEvent(this.world, blockposition, iblockdata, this);
////			}
////		}
//	}

	public void a(EntityLiving entityliving, float f) {
		EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
		double d0 = entityliving.getHeadY() - 1.100000023841858D;
		double d1 = entityliving.locX() - locX();
		double d2 = d0 - entitysnowball.locY();
		double d3 = entityliving.locZ() - locZ();
		float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		entitysnowball.shoot(d1, d2 + f1, d3, 1.6F, 12.0F);
		playSound(SoundEffects.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entitysnowball);
	}

	protected float b(EntityPose entitypose, EntitySize entitysize) {
		return 1.7F;
	}

	protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
		ItemStack itemstack = entityhuman.b(enumhand);
		if (itemstack.getItem() == Items.SHEARS && canShear()) {
			if (!CraftEventFactory.handlePlayerShearEntityEvent(entityhuman, this, itemstack, enumhand))
				return EnumInteractionResult.PASS;
			shear(SoundCategory.PLAYERS);
			return EnumInteractionResult.a(this.world.isClientSide);
		}
		return EnumInteractionResult.PASS;
	}

	public void shear(SoundCategory soundcategory) {
		this.world.playSound((EntityHuman) null, this, SoundEffects.ENTITY_SNOW_GOLEM_SHEAR, soundcategory, 1.0F, 1.0F);
		if (!this.world.s_()) {
			setHasPumpkin(false);
			a(new ItemStack(Items.dj), 1.7F);
		}
	}

	public boolean canShear() {
		return (isAlive() && hasPumpkin());
	}

	protected SoundEffect getSoundAmbient() {
		return SoundEffects.ENTITY_SNOW_GOLEM_AMBIENT;
	}

	protected SoundEffect getSoundHurt(DamageSource damagesource) {
		return SoundEffects.ENTITY_SNOW_GOLEM_HURT;
	}

	protected SoundEffect getSoundDeath() {
		return SoundEffects.ENTITY_SNOW_GOLEM_DEATH;
	}

}
