package net.mysticcloud.spigot.core.utils.pets.pet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityPig;
import net.minecraft.server.v1_16_R2.EntityPose;
import net.minecraft.server.v1_16_R2.EntityRabbit;
import net.minecraft.server.v1_16_R2.EntitySize;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.SoundEffects;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pathfindergoals.PathfinderGoalWalkToLoc;
import net.mysticcloud.spigot.core.utils.pets.Pet;

public class BabyRabbit extends EntityRabbit implements Pet {

	PathfinderGoalWalkToLoc pf;
	String owner;

	String prefix = "&6";
	String suffix = "&6&lBaby Bunny";

	public BabyRabbit(World world, EntityTypes<? extends EntityRabbit> entityType) {
		this(world);
	}

	public BabyRabbit(EntityTypes<? extends EntityRabbit> entityType, World world) {
		this(world);
	}

	public BabyRabbit(World world) {
		super(EntityTypes.RABBIT, world);
	}

	public void spawn(Location loc, String owner) {
		this.owner = owner;
		setBaby(true);
		setAge(-1);
		pf.setOwner(Bukkit.getPlayer(owner));
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		getBukkitEntity()
				.setCustomName(CoreUtils.colorize(prefix + owner + (owner.endsWith("s") ? "' " : "'s ") + suffix));
		setCustomNameVisible(true);

	}

	@Override
	public void initPathfinder() {
		pf = new PathfinderGoalWalkToLoc(this, 1.5D);
		this.goalSelector.a(1, pf);
//		this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
//		this.goalSelector.a(2, new PathfinderGoalRandomStrollLand(this, 1.0D, 1.0000001E-5F));
//		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
//		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
//		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 10, true,
//				false, entityliving -> entityliving instanceof IMonster));

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

	protected float b(EntityPose entitypose, EntitySize entitysize) {
		return 1.7F;
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

	@Override
	public Location getLocation() {
		return getBukkitEntity().getLocation();
	}

	@Override
	public Entity getEntity() {
		return this;
	}

	@Override
	public boolean isMountable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPetOwner() {
		return owner;
	}

}
