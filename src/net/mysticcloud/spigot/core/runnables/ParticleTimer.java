package net.mysticcloud.spigot.core.runnables;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class ParticleTimer implements Runnable {
	
	int i;
	
	public ParticleTimer(int i) {
		this.i = i;
	}

	@Override
	public void run() {
		
		for(Entry<UUID, ParticleFormatEnum> entry : CoreUtils.particles.entrySet()) {
				entry.getValue().formatter().display(entry.getKey(), i);
		}
		i=i+1;
		
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new ParticleTimer(i), 1);
		
	}

}
