package net.mysticcloud.spigot.core.runnables;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.Main;
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
				entry.getValue()
				.display(
						Bukkit.getEntity(
								entry.getKey())
						.getLocation()
						, i);
			} catch (NullPointerException ex) {
				CoreUtils.particles__remove.add(entry.getKey());
			}

		}
		for (UUID uid : CoreUtils.particles__remove) {
			CoreUtils.particles.remove(uid);
		}

		i = i + 1;

		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new ParticleTimer(i), 1);

	}

}
