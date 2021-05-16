package net.mysticcloud.spigot.core.utils.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EntityZombie;
import net.minecraft.server.v1_16_R2.EnumItemSlot;
import net.minecraft.server.v1_16_R2.Items;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class ReaperBoss extends EntityZombie {

	private int z = 0;

	private ArmorStand armor;
	private ArmorStand armor1;

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
		armor.setHelmet(new ItemStack(Material.SKELETON_SKULL));
		armor.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		armor.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		armor.setBoots(new ItemStack(Material.LEATHER_BOOTS));
		armor.setVisible(false);
		armor.setSmall(true);
		armor.setMetadata("locked", new FixedMetadataValue(Main.getPlugin(), "yes"));

		armor1 = loc.getWorld().spawn(loc, ArmorStand.class);
		armor1.setGravity(false);
		armor1.setHelmet(new ItemStack(Material.SKELETON_SKULL));
		armor1.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		armor1.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		armor1.setBoots(new ItemStack(Material.LEATHER_BOOTS));
		armor1.setVisible(false);
		armor1.setSmall(true);
		armor1.setMetadata("locked", new FixedMetadataValue(Main.getPlugin(), "yes"));

		this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
//		setBaby(true);
		getBukkitEntity().setCustomName(CoreUtils.colorize("&6&l" + Bosses.REAPER_BOSS.getFormattedCallName()));
		setCustomNameVisible(true);
//		setSlot(EnumItemSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
//		setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
//		setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	}

	@Override
	public boolean isBaby() {
		return true;
	}

	@Override
	public void movementTick() {
		super.movementTick();
		armor.teleport(getBukkitEntity().getLocation().clone().add(
				Math.cos(Math.toRadians(getBukkitEntity().getLocation().getYaw())) * 0.5, 0,
				Math.sin(Math.toRadians(getBukkitEntity().getLocation().getYaw())) * 0.5));
		armor1.teleport(getBukkitEntity().getLocation().clone().add(
				Math.cos(-(Math.toRadians(getBukkitEntity().getLocation().getYaw())) * 0.5), 0,
				Math.sin(-(Math.toRadians(getBukkitEntity().getLocation().getYaw())) * 0.5)));

		try {

			if (z % ((CoreUtils.getRandom().nextInt(4) * 100) + 50) == 0) {

				Player target = getTarget();

				float X = (float) ((locX()) - (target.getLocation().getX()));
				float Y = (float) ((locZ()) - (target.getLocation().getZ()));
				float A = (float) (Math.sqrt(Math.pow(target.getLocation().getX() - (locX()), 2)));
				float O = (float) (Math.sqrt(Math.pow(target.getLocation().getZ() - (locZ()), 2)));
				SmallFireball fb = getBukkitEntity().getWorld().spawn(
						(CoreUtils.getRandom().nextBoolean() ? armor : armor1).getLocation().add(0, 0.5, 0),
						SmallFireball.class);
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

	@Override
	protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
		armor.remove();
		armor1.remove();
		// TODO Auto-generated method stub
		super.dropDeathLoot(damagesource, i, flag);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {

		return super.damageEntity(damagesource, f);
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
