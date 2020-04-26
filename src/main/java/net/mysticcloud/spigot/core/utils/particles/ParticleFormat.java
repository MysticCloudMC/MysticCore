package net.mysticcloud.spigot.core.utils.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class ParticleFormat {

	protected boolean changeParticle = false;
	protected Particle particle = null;
	protected DustOptions dustoptions = new DustOptions(Color.RED, 1);
	protected ItemStack itemstack = new ItemStack(Material.STONE);
	protected BlockData blockdata = null;
	protected MaterialData materialdata = new MaterialData(Material.GOLD_BLOCK);
	protected List<Particle> allowedParticles = new ArrayList<>();

	protected String name = "Format Name";

	protected ItemStack guiItem = new ItemStack(Material.GRASS_BLOCK);

	public void display(UUID uid, int i) throws IllegalArgumentException {
		if (particle == null)
			return;

	}

	public void setDustOptions(DustOptions dustoptions) {
		this.dustoptions = dustoptions;
	}

	public void setItemStack(ItemStack itemstack) {
		this.itemstack = itemstack;
	}

	public void setBlockData(BlockData blockdata) {
		this.blockdata = blockdata;
	}

	public void setMaterialData(MaterialData materialdata) {
		this.materialdata = materialdata;
	}

	public void spawnParticle(UUID uid, Particle particle, Location loc, double offsetX, double offsetY,
			double offsetZ) {
		if (particle.getDataType() == null) {
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2);
			return;
		}

		if (particle.getDataType() != Void.class) {
			if (particle.getDataType() == DustOptions.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2,
						dustoptions);
			if (particle.getDataType() == MaterialData.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2,
						materialdata);
			if (particle.getDataType() == ItemStack.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2,
						itemstack);
			if (particle.getDataType() == BlockData.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2,
						blockdata);
		} else {
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle, loc, 0, offsetX, offsetY, offsetZ, 2);
		}

	}

	public void spawnParticle(UUID uid, Particle particle, Location loc) {
		spawnParticle(uid, particle, loc, 0, 0, 0);
	}

	public List<Particle> allowedParticles() {
		return allowedParticles;
	}

	public ItemStack item() {
		return guiItem;
	}

	public String name() {
		return name;
	}

	public void particle(Particle particle) {
		this.particle = particle;
	}

	public Particle particle() {
		return particle;
	}

	public boolean changeParticle() {
		return changeParticle;
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
