package main.java.net.mysticcloud.spigot.core.runnables;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;

import main.java.net.mysticcloud.spigot.core.Main;
import main.java.net.mysticcloud.spigot.core.utils.CoreUtils;
import main.java.net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class ParticleTimer implements Runnable {
	
	int i;
	
	public ParticleTimer(int i) {
		this.i = i;
	}

	@Override
	public void run() {
		
		if(i>360)
			i=1;
		
		for(Entry<UUID, ParticleFormatEnum> entry : CoreUtils.particles.entrySet()) {
				try{
					entry.getValue().formatter().display(entry.getKey(), i);
				} catch(IllegalArgumentException ex) {
					
				}
//				CoreUtils.debug(Particle.values()[CoreUtils.particletest]);
			
		}
		i=i+1;
		
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new ParticleTimer(i), 1);
		
	}

}
