package main.java.net.mysticcloud.spigot.core.runnables;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

import main.java.net.mysticcloud.spigot.core.Main;
import main.java.net.mysticcloud.spigot.core.utils.CoreUtils;
import main.java.net.mysticcloud.spigot.core.utils.Holiday;
import main.java.net.mysticcloud.spigot.core.utils.TimedPerm;

public class DateChecker implements Runnable {

	@Override
	public void run() {

		for (List<TimedPerm> perms : CoreUtils.timedPerms.values()) {
			for (TimedPerm perm : perms) {
				if (perm.started() + perm.liveFor() <= new Date().getTime()) {
					if (Bukkit.getPlayer(perm.uid()) != null) {
						CoreUtils.removeTimedPermission(Bukkit.getPlayer(perm.uid()), perm.permission());
					} else {
						CoreUtils.offlineTimedUsers.put(perm.uid(), perm.permission());
					}
				}
			}
		}

		if (CoreUtils.testblock != null) {
			for (int r = 0; r != 20; r++) {
				double dx = Math.sqrt(Math.pow(Bukkit.getPlayer("QuickScythe").getLocation().getX()
						- (CoreUtils.testblock.getLocation().getX() + 0.5), 2));
				double dz = Math.sqrt(Math.pow(Bukkit.getPlayer("QuickScythe").getLocation().getZ()
						- (CoreUtils.testblock.getLocation().getZ() + 0.5), 2));
				double distance = Math.sqrt(Math.pow(((Bukkit.getPlayer("QuickScythe").getLocation().getZ())
						- (CoreUtils.testblock.getLocation().getZ() + 0.5))
						+ ((Bukkit.getPlayer("QuickScythe").getLocation().getX())
								- (CoreUtils.testblock.getLocation().getX() + 0.5)),
						2));
				float d1 = (float) (Math.atan(dz / dx));
				float d2 = (float) (Math.atan(dx / dz));

				float d3 = (float) Math.asin(Math.toDegrees(dx) / Math.toDegrees(distance));
				float d4 = (float) Math.asin(Math.toDegrees(dz) / Math.toDegrees(distance));

				float test = (float) Math.asin(dz / distance);
				float test2 = (float) Math.acos(Math.toDegrees(dx) / Math.toDegrees(distance));

				Location loc;
				if (Bukkit.getPlayer("QuickScythe").getLocation().getX()
						- (CoreUtils.testblock.getLocation().getX() + 0.5) > 0) {
					if (Bukkit.getPlayer("QuickScythe").getLocation().getZ()
							- (CoreUtils.testblock.getLocation().getZ() + 0.5) > 0) {
						CoreUtils.t = 1;
						loc = CoreUtils.testblock.getLocation().clone().add(
								Math.cos((r) * (2 * Math.PI / 20)) * ((d1)) + 0.5,
								Math.sin((r) * (2 * Math.PI / 20)) * (1.5) + 2.5,
								Math.cos((r) * (2 * Math.PI / 20)) * ((-d2)) + 0.5);

					} else {
						CoreUtils.t = 4;
						loc = CoreUtils.testblock.getLocation().clone().add(
								Math.cos((r) * (2 * Math.PI / 20)) * ((d1)) + 0.5,
								Math.sin((r) * (2 * Math.PI / 20)) * (1.5) + 2.5,
								Math.cos((r) * (2 * Math.PI / 20)) * ((d2)) + 0.5);
					}

				} else {
					if (Bukkit.getPlayer("QuickScythe").getLocation().getZ()
							- (CoreUtils.testblock.getLocation().getZ() + 0.5) > 0) {
						CoreUtils.t = 2;
						loc = CoreUtils.testblock.getLocation().clone().add(
								Math.cos((r) * (2 * Math.PI / 20)) * ((d1)) + 0.5,
								Math.sin((r) * (2 * Math.PI / 20)) * (1.5) + 2.5,
								Math.cos((r) * (2 * Math.PI / 20)) * ((d2)) + 0.5);
					} else {
						CoreUtils.t = 3;
						loc = CoreUtils.testblock.getLocation().clone().add(
								Math.cos((r) * (2 * Math.PI / 20)) * ((d1)) + 0.5,
								Math.sin((r) * (2 * Math.PI / 20)) * (1.5) + 2.5,
								Math.cos((r) * (2 * Math.PI / 20)) * ((-d2)) + 0.5);
					}
				}

				CoreUtils.debug("Quadrant: " + CoreUtils.t);
				Bukkit.getPlayer("QuickScythe").spawnParticle(Particle.COMPOSTER, loc, 0);
			}
		}

		CoreUtils.setHoliday(Holiday.NONE);

		if (CoreUtils.getMonth() == Calendar.JANUARY) {
			if (CoreUtils.getDay() >= 1 && CoreUtils.getDay() <= 16)
				CoreUtils.setHoliday(Holiday.NEW_YEARS);

		}

		if (CoreUtils.getMonth() == Calendar.FEBRUARY || CoreUtils.getMonth() == Calendar.JANUARY) {
			CoreUtils.setHoliday(Holiday.VALENTINES);
		}

		if (CoreUtils.getMonth() == Calendar.OCTOBER) {
//			if (CoreUtils.getDay() == 31)
			CoreUtils.setHoliday(Holiday.HALLOWEEN);

		}
//		if (CoreUtils.getMonth() == Calendar.NOVEMBER) {
//		}
		if (CoreUtils.getMonth() == Calendar.DECEMBER) {
			if (CoreUtils.getDay() <= 26)
				CoreUtils.setHoliday(Holiday.CHRISTMAS);

			if (CoreUtils.getDay() >= 27)
				CoreUtils.setHoliday(Holiday.NEW_YEARS);
//
//			
		}

//		if(CoreUtils.getHoliday() != Holiday.NONE){
//			CoreUtils.runRunnables();
//		}

//		if(CoreUtils.debugOn())CoreUtils.debug(CoreUtils.getHoliday());

//		CoreUtils.updateDate();

		if (CoreUtils.getHoliday() != Holiday.NONE)
			Bukkit.getScheduler().runTask(Main.getPlugin(), new HolidayParticles());

		if (!CoreUtils.debugOn())
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new DateChecker(), 1);
		else
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new DateChecker(), 10);
	}

}
