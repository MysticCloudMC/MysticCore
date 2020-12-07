package net.mysticcloud.spigot.core.runnables;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.DebugUtils;
import net.mysticcloud.spigot.core.utils.Holiday;
import net.mysticcloud.spigot.core.utils.TimedPerm;
import net.mysticcloud.spigot.core.utils.events.Event;
import net.mysticcloud.spigot.core.utils.events.EventUtils;
import net.mysticcloud.spigot.core.utils.punishment.Punishment;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class DateChecker implements Runnable {

	int counter;

	public DateChecker(int counter) {
		this.counter = counter;
	}

	public DateChecker() {
		counter = 0;
	}

	@Override
	public void run() {
		try {
			for (Entry<Integer, Event> entry : EventUtils.getEvents().entrySet()) {
				if (!entry.getValue().populated())
					continue;
				if (entry.getValue().getEventCheck().check()) {
					entry.getValue().end();
					EventUtils.addRemoveEvent(entry.getKey());
				}
			}
			for (Integer id : EventUtils.removeEvents()) {
				EventUtils.removeEvent(id);
			}
			EventUtils.clearRemoveEvents();
			counter = counter + 1;
			if (counter % 40 == 0) {
				counter = 0;
				for (Punishment punishment : PunishmentUtils.getPunishments()) {
					if (punishment.getDate() + punishment.getDuration() - new Date().getTime() <= 1) {
						PunishmentUtils.finishPunishment(punishment);
					}
				}
				PunishmentUtils.finishPunishments();
				for (Player player : Bukkit.getOnlinePlayers()) {
					CoreUtils.enableScoreboard(player);
				}
			}

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

			if (DebugUtils.holidayCheck()) {

				boolean holiday = false;

				if (CoreUtils.getMonth() == Calendar.JANUARY) {
					if (CoreUtils.getDay() >= 1 && CoreUtils.getDay() <= 16) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.NEW_YEARS);
					}

				}

				if (CoreUtils.getMonth() == Calendar.FEBRUARY) {
					if (CoreUtils.getDay() == 13 || CoreUtils.getDay() == 14) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.VALENTINES);
					}
				}

				if (CoreUtils.getMonth() == Calendar.MARCH) {
					if (CoreUtils.getDay() <= 15 && CoreUtils.getDay() >= 20) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.ST_PATRICKS);
					}

				}
				if (CoreUtils.getMonth() == Calendar.MAY) {
					if (CoreUtils.getDay() == 5) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.CINCO_DE_MAYO);
					}
					if (CoreUtils.getDay() >= 22 && CoreUtils.getDay() <= 24) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.MEMORIAL_DAY);
					}

				}
				if (CoreUtils.getMonth() == Calendar.JULY) {
					if (CoreUtils.getDay() == 31) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.AVACADO_DAY);
					}

				}

				if (CoreUtils.getMonth() == Calendar.OCTOBER) {
					if (CoreUtils.getDay() >= 20) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.HALLOWEEN);
					}

				}
//		if (CoreUtils.getMonth() == Calendar.NOVEMBER) {
//		}
				if (CoreUtils.getMonth() == Calendar.DECEMBER) {
					if (CoreUtils.getDay() <= 26) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.CHRISTMAS);
					}

					if (CoreUtils.getDay() >= 27) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.NEW_YEARS);
					}
//
//			
				}

//		if(CoreUtils.getHoliday() != Holiday.NONE){
//			CoreUtils.runRunnables();
//		}

//		if(CoreUtils.debugOn())CoreUtils.debug(CoreUtils.getHoliday());

//		CoreUtils.updateDate();

				if (holiday)
					Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new HolidayParticles());
				else
					CoreUtils.setHoliday(Holiday.NONE);
			}
		} catch (Exception ex) {
			CoreUtils.debug("There was an error!");
			ex.printStackTrace();
		}

		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new DateChecker(counter), 1);
	}

}
