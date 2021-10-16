package net.mysticcloud.spigot.core.runnables;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class ParticleTimer implements Runnable {

	int i;

	public ParticleTimer(int i) {
		this.i = i;
	}

	@Override
	public void run() {

		if (i > 360)
			i = 1;

		for (Entry<UUID, ParticleFormat> entry : CoreUtils.particles.entrySet()) {
			try {
				entry.getValue().setLifetime(i);
				entry.getValue().display(Bukkit.getEntity(entry.getKey()).getLocation());
			} catch (NullPointerException ex) {
				CoreUtils.particles__remove.add(entry.getKey());
			}

		}
		for (UUID uid : CoreUtils.particles__remove) {
			CoreUtils.particles.remove(uid);
		}
		CoreUtils.particles__remove.clear();

		for (Entry<Location, ParticleFormat> entry : CoreUtils.blockparticles__add.entrySet()) {
			CoreUtils.blockparticles.put(entry.getKey(), entry.getValue());
		}
		CoreUtils.blockparticles__add.clear();

		for (Entry<Location, ParticleFormat> entry : CoreUtils.blockparticles.entrySet()) {
			try {
				entry.getValue().setLifetime(i);
				entry.getValue().display(entry.getKey());
			} catch (NullPointerException ex) {
				CoreUtils.blockparticles__remove.add(entry.getKey());
			}

		}
		for (Location uid : CoreUtils.blockparticles__remove) {
			CoreUtils.blockparticles.remove(uid);
		}
		CoreUtils.blockparticles__remove.clear();

		i = i + 1;

		Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), this, 1);

	}

}
