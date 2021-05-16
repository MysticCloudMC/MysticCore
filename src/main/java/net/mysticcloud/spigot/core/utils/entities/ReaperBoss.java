package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityIronGolem;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EntityZombie;
import net.minecraft.server.v1_16_R2.PathfinderGoalDefendVillage;
import net.minecraft.server.v1_16_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R2.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_16_R2.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalOfferFlower;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;

public class ReaperBoss extends EntityZombie {

	private int z = 1;
	
	private ArmorStand armor;

	private SelectorFormat format = new SelectorFormat();

	public ReaperBoss(World world, EntityTypes<? extends EntityZombie> entityType) {
		this(world);
	}

	public ReaperBoss(EntityTypes<? extends EntityZombie> entityType, World world) {
		this(world);
	}

	public ReaperBoss(World world) {
		
		super(EntityTypes.ZOMBIE, world);
	}

	public void spawn(Location loc) {
		armor = loc.getWorld().spawn(loc, ArmorStand.class);
		armor.setGravity(false);
		armor.setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
//		armor.setVisible(false);
		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		format.particle(Particle.FLAME);
		format.setHeight(3);
		format.setColumns(4);
		format.setLength(1);
		format.setRadius(1.25);
		getBukkitEntity().setCustomName(CoreUtils.colorize("&e" + Bosses.IRON_BOSS.getFormattedCallName()));
		setCustomNameVisible(true);

	}

	@Override
	protected void initPathfinder() {
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
		this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, false, 4, () -> false));
		this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.6D));
		this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

		// This is from zombies
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

		// This should b 2
		this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, new Class[0]));

//	    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 5, false, false, entityliving -> 
//	          (entityliving instanceof IMonster && !(entityliving instanceof EntityCreeper))));
	}

	@Override
	public void movementTick() {
		super.movementTick();
		armor.teleport(getBukkitEntity().getLocation().clone().add(Math.cos(getBukkitEntity().getLocation().getYaw()),0,Math.sin(getBukkitEntity().getLocation().getYaw())));
		armor.setRotation(0, (float) Math.toDegrees(z));
		z = z + 1;

	}

	private Player getTarget() {

		while (true) {
			for (org.bukkit.entity.Entity en : getBukkitEntity().getNearbyEntities(40, 40, 40)) {
				if (en instanceof Player && CoreUtils.getRandom().nextBoolean()) {
					return (Player) en;
				}
			}
		}

	}

	protected Vector rotateAroundAxisX(Vector v, double angle) {
		angle = Math.toRadians(angle);
		double y, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		y = v.getY() * cos - v.getZ() * sin;
		z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	protected Vector rotateAroundAxisY(Vector v, double angle) {
		angle = -angle;
		angle = Math.toRadians(angle);
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos + v.getZ() * sin;
		z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

}
