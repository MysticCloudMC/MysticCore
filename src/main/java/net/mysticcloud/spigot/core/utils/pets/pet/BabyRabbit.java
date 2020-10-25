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
		return false;
	}

	@Override
	public String getPetOwner() {
		return owner;
	}

	@Override
	public double getSpeedMod() {
		return 0;
	}
	
	@Override
	public void forceJump() {
		jump();
	}

}
