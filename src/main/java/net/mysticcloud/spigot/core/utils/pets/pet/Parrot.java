package net.mysticcloud.spigot.core.utils.pets.pet;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityParrot;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.EntityPose;
import net.minecraft.server.v1_16_R2.EntitySize;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.SoundEffects;
import net.minecraft.server.v1_16_R2.Vec3D;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pathfindergoals.PathfinderGoalWalkToLoc;
import net.mysticcloud.spigot.core.utils.pets.Pet;
import net.mysticcloud.spigot.core.utils.pets.PetManager;

public class Parrot extends EntityParrot implements Pet {

	PathfinderGoalWalkToLoc pf;
	String owner;

	String prefix = "&2";
	String suffix = "&2&lParrot";
	
	double speedMod = 5;

	public Parrot(World world, EntityTypes<? extends EntityParrot> entityType) {
		this(world);
	}

	public Parrot(EntityTypes<? extends EntityParrot> entityType, World world) {
		this(world);
	}

	public Parrot(World world) {
		super(EntityTypes.PARROT, world);
	}

	public void spawn(Location loc, String owner) {
		this.owner = owner;
		setBaby(true);
		((org.bukkit.entity.Parrot) getBukkitEntity()).setVariant(Variant.values()[new Random().nextInt(Variant.values().length)]);
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
		super.movementTick();
		PetManager.PetUtils.flyingPetRidingTick(this);
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
		// TODO Auto-generated method stub
		return speedMod;
	}
	
	@Override
	public void forceJump() {
		jump();
	}

}
