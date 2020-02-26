package net.mysticcloud.spigot.core.utils.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ParticleFormat {
	
	protected boolean changeParticle = false;
	protected Particle particle = null;
	protected DustOptions dustoptions = null;
	protected ItemStack itemstack = null;
	protected BlockData blockdata = null;
	protected MaterialData materialdata = null;
	protected List<Particle> allowedParticles = new ArrayList<>();
	
	protected String name = "Format Name";
	
	protected ItemStack guiItem = new ItemStack(Material.GRASS_BLOCK);
	
	public void display(UUID uid, int i) throws IllegalArgumentException {
		if(particle == null) return;
		
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
	
	
	public void spawnParticle(UUID uid, Particle particle, Location loc) {
		if(particle.getDataType() == null) {
			Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
					loc, 0, 0, 0, 0, 2);
			return;
		}

		if(particle.getDataType() != Void.class) {
			if (particle.getDataType() == DustOptions.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
						loc, 0, 0, 0, 0, 2, dustoptions);
			if (particle.getDataType() == MaterialData.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
						loc, 0, 0, 0, 0, 2, materialdata);
			if (particle.getDataType() == ItemStack.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
						loc, 0, 0, 0, 0, 2, itemstack);
			if (particle.getDataType() == BlockData.class)
				Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
						loc, 0, 0, 0, 0, 2, blockdata);
		} else {
			 Bukkit.getPlayer(uid).getWorld().spawnParticle(particle,
				loc, 0, 0, 0, 0, 2);
		}
		

	
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

}
