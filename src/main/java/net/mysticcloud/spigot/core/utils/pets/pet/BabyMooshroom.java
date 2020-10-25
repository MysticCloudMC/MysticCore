package net.mysticcloud.spigot.core.utils.pets.pet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityMushroomCow;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.SoundEffects;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pathfindergoals.PathfinderGoalWalkToLoc;
import net.mysticcloud.spigot.core.utils.pets.Pet;
import net.mysticcloud.spigot.core.utils.pets.PetManager;
import net.mysticcloud.spigot.core.utils.pets.PetManager.PetUtils;

public class BabyMooshroom extends EntityMushroomCow implements Pet {

	PathfinderGoalWalkToLoc pf;
	String owner;

	String prefix = "&c";
	String suffix = "&c&lBaby Mooshroom";
	
	double speedMod = 10D;

	public BabyMooshroom(World world, EntityTypes<? extends EntityMushroomCow> entityType) {
		this(world);
	}

	public BabyMooshroom(EntityTypes<? extends EntityMushroomCow> entityType, World world) {
		this(world);
	}

	public BabyMooshroom(World world) {
		super(EntityTypes.MOOSHROOM, world);
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

	protected void initPathfinder() {
		pf = new PathfinderGoalWalkToLoc(this, 1.5D);
		this.goalSelector.a(1, pf);

	}
	
	@Override
	public void movementTick() {
		setBaby(true);
		super.movementTick();
		PetManager.PetUtils.jumpingPetRidingTick(this);
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

	@Override
	public double getSpeedMod() {
		return speedMod;
	}
	
	@Override
	public void forceJump() {
		jump();
	}

}
