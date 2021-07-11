package net.mysticcloud.spigot.core.runnables;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.TimedPerm;
import net.mysticcloud.spigot.core.utils.accounts.friends.FriendUtils;
import net.mysticcloud.spigot.core.utils.admin.DebugUtils;
import net.mysticcloud.spigot.core.utils.admin.Holiday;
import net.mysticcloud.spigot.core.utils.events.Event;
import net.mysticcloud.spigot.core.utils.events.EventUtils;
import net.mysticcloud.spigot.core.utils.portals.PortalUtils;
import net.mysticcloud.spigot.core.utils.punishment.Punishment;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentType;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class DateChecker implements Runnable {

	int counter = 0;
	long lastcheck = 0;
	long lastcheck2 = 0;
	boolean hourWarn = false;
	boolean tmWarn = false;
	boolean fmWarn = false;
	Calendar calendar = Calendar.getInstance();

	public DateChecker(int counter) {

	}

	public DateChecker() {

	}

	@Override
	public void run() {

		calendar = Calendar.getInstance();

		if (calendar.get(Calendar.HOUR_OF_DAY) == 17) {
			if (!hourWarn) {
				Bukkit.broadcastMessage(CoreUtils.colorize("&aThe network will be restarting in 1 hour."));
				hourWarn = true;
			}
			if (calendar.get(Calendar.MINUTE) >= 30) {
				if (!tmWarn) {
					Bukkit.broadcastMessage(CoreUtils.colorize("&a&lThe network will be restarting in 30 minutes."));
					tmWarn = true;
				}
				if (calendar.get(Calendar.MINUTE) >= 55) {
					if (!fmWarn) {
						Bukkit.broadcastMessage(CoreUtils
								.colorize("&c&l&k|||&a&lThe network will be restarting in 5 minutes.&c&l&k|||"));
						fmWarn = true;
					}
				}
			}
		}

		if (calendar.get(Calendar.HOUR_OF_DAY) == 18 && calendar.get(Calendar.MINUTE) >= 0
				&& calendar.get(Calendar.MINUTE) <= 1) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.kickPlayer("%kick%The network is restarting. Please join back in 15 minutes.");
			}

			Bukkit.getServer().shutdown();
		}

		if (new Date().getTime() - lastcheck >= TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES)) {
			DebugUtils.debug("Updating reports");
			PunishmentUtils.updatePunishments();
			DebugUtils.debug("Updating friendships");
			FriendUtils.update();
			DebugUtils.debug("Updating permissions");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex reload");
			lastcheck = new Date().getTime();
		}

		if (new Date().getTime() - lastcheck2 >= TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS)) {
			if (CoreUtils.useCoreScoreboard()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					CoreUtils.updateScoreboard(player);
//					if (PortalUtils.isEditing(player)) {
//						if (PortalUtils.getEditingInfo(player.getUniqueId()).has("x1")
//								&& PortalUtils.getEditingInfo(player.getUniqueId()).has("x2")) {
//							double x1 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("x1");
//							double y1 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("y1");
//							double z1 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("z1");
//							
//							double x2 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("x2");
//							double y2 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("y2");
//							double z2 = PortalUtils.getEditingInfo(player.getUniqueId()).getDouble("z2");
//							
//							String world = PortalUtils.getEditingInfo(player.getUniqueId()).getString("world");
//							
//							for(int x=0;x!=Math.abs(x1-x2)*3;x++) {
//								Bukkit.getWorld(world).spawnParticle(Particle.FLAME, x1, y1, z1, x, x2, y2, z2, x, null);
//							}
//						}
//					}
				}
			}
			lastcheck2 = new Date().getTime();
		}
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

				for (Punishment punishment : PunishmentUtils.getPunishments()) {
					if (punishment.getType().equals(PunishmentType.BAN)) {
						if (Bukkit.getPlayer(punishment.getUser()) != null) {
							Bukkit.getPlayer(punishment.getUser())
									.kickPlayer(CoreUtils.colorize("&cYou're are banned for "
											+ CoreUtils.getSimpleTimeFormat(punishment.getDate()
													+ punishment.getDuration() - new Date().getTime())
											+ "\n&f[Reason] " + punishment.getNotes()));
						}
					}
					if (punishment.getDate() + punishment.getDuration() - new Date().getTime() <= 1) {
						PunishmentUtils.finishPunishment(punishment);
					}
				}
				PunishmentUtils.finishPunishments();
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

			for (UUID uid : CoreUtils.getControllers()) {
				if (Bukkit.getPlayer(uid) == null)
					continue;
				if (CoreUtils.controllingWho(Bukkit.getPlayer(uid)) == null)
					continue;
				CoreUtils.controllingWho(Bukkit.getPlayer(uid)).teleport(Bukkit.getPlayer(uid));
			}

			if (DebugUtils.holidayCheck()) {

				boolean holiday = false;

				if (CoreUtils.getMonth() == Calendar.JANUARY) {
					if (CoreUtils.getDay() >= 1 && CoreUtils.getDay() <= 7) {
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
					if (CoreUtils.getDay() <= 13 && CoreUtils.getDay() >= 17) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.ST_PATRICKS);
					}

				}
				if (CoreUtils.getMonth() == Calendar.MAY) {
					if (CoreUtils.getDay() == 5) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.MAY_THE_FORTH);
					}
					if (CoreUtils.getDay() == 5) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.CINCO_DE_MAYO);
					}
					if (CoreUtils.getDay() >= 22 && CoreUtils.getDay() <= 24) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.MEMORIAL_DAY);
					}

				}

				if (CoreUtils.getMonth() == Calendar.JUNE) {
					if (CoreUtils.getDay() >= 27) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.JULY_4TH);
					}
				}

				if (CoreUtils.getMonth() == Calendar.JULY) {
					if (CoreUtils.getDay() >= 0 && CoreUtils.getDay() <= 4) {
						holiday = true;
						CoreUtils.setHoliday(Holiday.JULY_4TH);
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

		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this, 1);
	}

}
