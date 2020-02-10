package net.mysticcloud.spigot.core.utils.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class ParticleFormat {
	
	protected boolean changeParticle = false;
	protected Particle particle = Particle.COMPOSTER;
	protected List<Particle> allowedParticles = new ArrayList<>();
	
	protected String name = "Format Name";
	
	protected ItemStack guiItem = new ItemStack(Material.GRASS_BLOCK);
	
	public void display(UUID uid, int i) throws IllegalArgumentException {
		
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
