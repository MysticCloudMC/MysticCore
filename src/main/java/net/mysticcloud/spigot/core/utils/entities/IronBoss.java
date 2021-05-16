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

public class IronBoss extends EntityIronGolem {

	private int z = 1;
	
	private ArmorStand armor;

	private SelectorFormat format = new SelectorFormat();

	public IronBoss(World world, EntityTypes<? extends EntityIronGolem> entityType) {
		this(world);
	}

	public IronBoss(EntityTypes<? extends EntityIronGolem> entityType, World world) {
		this(world);
	}

	public IronBoss(World world) {
		
		super(EntityTypes.IRON_GOLEM, world);
	}

	public void spawn(Location loc) {
		armor = loc.getWorld().spawn(loc, ArmorStand.class);
		armor.setGravity(false);
		armor.setHelmet(new ItemStack(Material.EMERALD));
		armor.setVisible(false);
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
		this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
		this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.6D));
		this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, (Class) EntityHuman.class, 6.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));

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
		format.setLifetime(z);

		armor.teleport(getBukkitEntity().getLocation().clone().add(0,1.5,0));
		armor.setRotation(0, (float) Math.toDegrees(z));
		format.display(new Location(Bukkit.getWorld(world.getWorld().getName()), locX(), locY(), locZ()));

		try {

			if (z % ((CoreUtils.getRandom().nextInt(4)*100)+100) == 0) {

				Player target = getTarget();


				float X = (float) ((locX()) - (target.getLocation().getX()));
				float Y = (float) ((locZ()) - (target.getLocation().getZ()));
				float A = (float) (Math.sqrt(Math.pow(target.getLocation().getX() - (locX()), 2)));
				float O = (float) (Math.sqrt(Math.pow(target.getLocation().getZ() - (locZ()), 2)));
				Snowball fb = getBukkitEntity().getWorld().spawn(getBukkitEntity().getLocation().add(0, 2, 0),
						Snowball.class);
				fb.setMetadata("doesDamage", new FixedMetadataValue(Main.getPlugin(), "true"));
				if (X < 0 && Y < 0) {
					fb.setVelocity(rotateAroundAxisY(new Vector(1, 0, 0), (Math.toDegrees(Math.atan(O / A)))));
				}
				if (X < 0 && Y > 0) {
					fb.setVelocity(rotateAroundAxisY(new Vector(1, 0, 0), -(Math.toDegrees(Math.atan(O / A)) - 360)));
				}
				if (X > 0 && Y > 0) {
					fb.setVelocity(rotateAroundAxisY(new Vector(1, 0, 0), (Math.toDegrees(Math.atan(O / A)) + 180)));
				}
				if (X > 0 && Y < 0) {
					fb.setVelocity(rotateAroundAxisY(new Vector(1, 0, 0), -(Math.toDegrees(Math.atan(O / A)) - 180)));
				}

			}
		} catch (ArithmeticException ex) {
			Bukkit.broadcastMessage("ERROR");
			ex.printStackTrace();
		}

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
