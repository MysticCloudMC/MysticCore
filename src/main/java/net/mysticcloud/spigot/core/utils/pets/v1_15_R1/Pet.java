package net.mysticcloud.spigot.core.utils.pets.v1_15_R1;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.World;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.pets.PetType;

public class Pet extends EntityArmorStand {

	double speed = 0.15;
	double dx = 0;
	double dz = 0;

	double dx2 = 0;
	double dz2 = 0;

	double speedmod = 0.075;

	double distance = 0;

	double dy = 0;
	double dd = 0;
	double p = 0;
	double pp = 0;
	UUID owner = null;
	PetType type = null;

	public Pet(World world) {
		super(world, 0.0, 0.0, 0.0);

		go();
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public void turnOnVehicle() {
		go();
	}

	@SuppressWarnings("deprecation")
	public void go() {

		if (type == null) {
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new MovePet(this), 1);
			return;
		}

		if (owner == null) {
			for (org.bukkit.entity.Entity e : getBukkitEntity().getNearbyEntities(5, 5, 5)) {
				if (e instanceof Player) {
					setOwner(((Player) e).getUniqueId());
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new MovePet(this), 1);
					return;
				}
			}
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new MovePet(this), 1);
			return;
		}
		if (Bukkit.getPlayer(owner) == null) {
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new MovePet(this), 1);
			return;
		}

		float X = (float) ((locX()) - (Bukkit.getPlayer(owner).getLocation().getX()));
		float Y = (float) ((locZ()) - (Bukkit.getPlayer(owner).getLocation().getZ()));
		float A = (float) (Math.sqrt(Math.pow(Bukkit.getPlayer(owner).getLocation().getX() - (locX()), 2)));
		float O = (float) (Math.sqrt(Math.pow(Bukkit.getPlayer(owner).getLocation().getZ() - (locZ()), 2)));
		if (X < 0 && Y < 0) {
			this.yaw = (float) (Math.toDegrees(Math.atan(O / A)) - 90);
		}
		if (X < 0 && Y > 0) {
			this.yaw = (float) -(Math.toDegrees(Math.atan(O / A)) - 270);
		}
		if (X > 0 && Y > 0) {
			this.yaw = (float) (Math.toDegrees(Math.atan(O / A)) + 90);
		}
		if (X > 0 && Y < 0) {
			this.yaw = (float) -(Math.toDegrees(Math.atan(O / A)) - 90);
		}
		if (Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2)) > 2) {
			Vector v;
			if (!getBukkitEntity().getLocation().add(rotateAroundAxisY(new Vector(0, 0, 0.19), yaw)).getBlock()
					.getType().equals(Material.AIR) && getBukkitEntity().getLocation().add(rotateAroundAxisY(new Vector(0, 1, speed), yaw)).getBlock()
					.getType().equals(Material.AIR)) {
				v = rotateAroundAxisY(new Vector(0, 1, speed), yaw);
			} else {
				if (getBukkitEntity().getLocation().add(new Vector(0, -0.5, 0)).getBlock().getType()
						.equals(Material.AIR))
					v = rotateAroundAxisY(new Vector(0, -1, speed), yaw);
				else
					v = rotateAroundAxisY(new Vector(0, 0, speed), yaw);
			}
			setMot(v.getX(), v.getY(), v.getZ());
			if (!getEntity().getHelmet().equals(type.getMovingItem()))
				getEntity().setHelmet(type.getMovingItem());
		} else {
			if (!getEntity().getHelmet().equals(type.getIdleItem()))
				getEntity().setHelmet(type.getIdleItem());
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new MovePet(this), 1);
	}

	public void setOwner(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			String name = "";
			String owner = Bukkit.getPlayer(uid).getName();
			if (owner.endsWith("s"))
				name = owner + "' Pet";
			else
				name = owner + "'s Pet";

			getBukkitEntity().setCustomName(name);
			setCustomNameVisible(true);

		}
		owner = uid;
	}

	public UUID getOwner() {
		return owner;
	}

	private ArmorStand getEntity() {
		return ((ArmorStand) getBukkitEntity());
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

	protected Vector rotateAroundAxisZ(Vector v, double angle) {
		angle = Math.toRadians(angle);
		double x, y, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos - v.getY() * sin;
		y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}

}