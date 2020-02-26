package net.mysticcloud.spigot.core.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.Holiday;

public class HolidayParticles implements Runnable {

	@Override
	public void run() {
		if (CoreUtils.getHoliday().equals(Holiday.NONE))
			return;
		switch (CoreUtils.getHoliday()) {
		case TEST:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.holidayparticles.get(player.getUniqueId())) {
					switch(CoreUtils.getRandom().nextInt(3)) {
					case 1:
						player.spawnParticle(Particle.REDSTONE,
								player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
										(1.5 + CoreUtils.getRandom().nextDouble())
												- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
										-0.5 + CoreUtils.getRandom().nextDouble()),
								0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 0, (int) 0), 1));
						break;
					case 2:
						player.spawnParticle(Particle.REDSTONE,
								player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
										(1.5 + CoreUtils.getRandom().nextDouble())
												- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
										-0.5 + CoreUtils.getRandom().nextDouble()),
								0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 255, (int) 255), 1));
						break;
					case 3:
						player.spawnParticle(Particle.REDSTONE,
								player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
										(1.5 + CoreUtils.getRandom().nextDouble())
												- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
										-0.5 + CoreUtils.getRandom().nextDouble()),
								0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 0, (int) 255, (int) 0), 1));
						break;
					default:
						player.spawnParticle(Particle.REDSTONE,
								player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
										(1.5 + CoreUtils.getRandom().nextDouble())
												- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
										-0.5 + CoreUtils.getRandom().nextDouble()),
								0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 0, (int) 0), 1));
						break;
					}
				}
				player.spawnParticle(Particle.REDSTONE,
						player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
								(1.5 + CoreUtils.getRandom().nextDouble())
										- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
								-0.5 + CoreUtils.getRandom().nextDouble()),
						0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 0, (int) 255, (int) 2), 1));
				
			}
			break;
		case ST_PATRICKS:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.holidayparticles.get(player.getUniqueId())) {
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 0, (int) 255, (int) 2), 1));
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(0.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(1) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 84, (int) 53, (int) 0), 1));
				}
				
			}
			break;
		case VALENTINES:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.holidayparticles.get(player.getUniqueId()))
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 125, (int) 255), 1));
			}

			break;
		case CHRISTMAS:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.getRandom().nextBoolean())
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 0, (int) 0), 1));
				else
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 0, (int) 255, (int) 0), 1));
			}

			break;

		case NEW_YEARS:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.getRandom().nextBoolean())
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 0, (int) 0, (int) 0), 1));
				else
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 125, (int) 0, (int) 255), 1));
			}

			break;

		case HALLOWEEN:
			for (Player player : Bukkit.getOnlinePlayers()) {
				try {
					if (!CoreUtils.holidayparticles.get(player.getUniqueId()))
						continue;
				} catch (NullPointerException ex) {
					continue;
				}
				if (CoreUtils.getRandom().nextBoolean())
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 125, (int) 0, (int) 255), 1));
				else
					player.spawnParticle(Particle.REDSTONE,
							player.getLocation().add(-0.5 + CoreUtils.getRandom().nextDouble(),
									(1.5 + CoreUtils.getRandom().nextDouble())
											- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
									-0.5 + CoreUtils.getRandom().nextDouble()),
							0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB((int) 255, (int) 125, (int) 0), 1));
			}

			break;
		default:
			break;
		}

	}

}
