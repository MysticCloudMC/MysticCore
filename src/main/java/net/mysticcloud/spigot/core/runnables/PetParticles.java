package net.mysticcloud.spigot.core.runnables;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.pets.Pet;

public class PetParticles implements Runnable {

	Pet pet;
	int i;
	ParticleFormat format;

	public PetParticles(Pet pet, int i, ParticleFormat format) {
		this.pet = pet;
		this.i = i;
		this.format = format;
	}

	@Override
	public void run() {
		format.setLifetime(i);
		format.display(pet.getLocation());
		i = i + 1;

		if (format.getLifetime() > -1) {
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new PetParticles(pet, i, format), 1);
		}
	}

}
