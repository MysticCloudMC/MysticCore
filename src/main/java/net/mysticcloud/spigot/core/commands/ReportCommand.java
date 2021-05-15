package net.mysticcloud.spigot.core.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.punishment.InfringementSeverity;
import net.mysticcloud.spigot.core.utils.punishment.InfringementType;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class ReportCommand implements CommandExecutor {

	public ReportCommand(Main plugin, String... cmd) {
		for (String s : cmd)
			plugin.getCommand(s).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String reporter = (sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE";
		if (sender.hasPermission("mysticcloud.staff")) {
			/*
			 * 
			 * Staff Members
			 * 
			 */
			if (args.length == 0) {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("reports")
						+ ((sender instanceof Player) ? "Try \"/report <player>\" &oor&f t" : "T")
						+ "ry \"/report <player> <severity-MINIMAL|LOW|MEDIUM|HIGH|EXTREME> [notes]\""));
				return true;
			}
			if (args.length >= 1) {

				if (args[0].equalsIgnoreCase("complete")) {
					List<Object> info = PunishmentUtils.punishmentBuilder.get(reporter);
					if (info == null) {
						sender.sendMessage(
								CoreUtils.prefixes("error") + "You aren't in a report editor. Try /report <player>");
						return true;
					}

					String x = "";
					for (int a = 1; a != args.length; a++) {
						x = x == "" ? args[1] : x + " " + args[a];
					}
					Object rm = null;
					Object ad = null;
					for (Object o : info) {
						if (o instanceof String) {
							ad = (((String) o) + x);
							rm = o;
						}

					}

					info.remove(rm);
					info.add(ad);

					PunishmentUtils.punishmentBuilder.put(reporter, info);
					if (!PunishmentUtils.finishPunishment(reporter)) {
						sender.sendMessage(
								CoreUtils.prefixes("error") + "You aren't in a report editor. Try /report <player>");
					}

					return true;
				}

				if (sender instanceof Player) {
					UUID uid = null;
					if (Bukkit.getPlayer(args[0]) == null) {
						uid = CoreUtils.LookupUUID(args[0]);
					} else {
						uid = Bukkit.getPlayer(args[0]).getUniqueId();
					}
					if (uid != null) {
						((Player) sender).setMetadata("report", new FixedMetadataValue(Main.getPlugin(), args[0]));
						GUIManager.openInventory(((Player) sender),

								PunishmentUtils.getPunishmentGUI(""), "OffenceTypes");
					} else
						sender.sendMessage("Player not online. Use the /opunish command to punish offline users.");
				}
			}
			if (args.length >= 3) {
				UUID uid = null;
				if (Bukkit.getPlayer(args[0]) == null) {
					uid = CoreUtils.LookupUUID(args[0]);
				} else {
					uid = Bukkit.getPlayer(args[0]).getUniqueId();
				}
				if (uid != null) {
					InfringementType inf = null;
					InfringementSeverity sev = null;
					try {
						inf = InfringementType.valueOf(args[1].toUpperCase());
						sev = InfringementSeverity.valueOf(args[2].toUpperCase());
					} catch (NullPointerException ex) {
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("reports")
								+ "Try \"&c/report <player> <CHAT|HACKING|GREIF> <MINIMAL,LOW,MEDIUM,HIGH,EXTREME> [notes]"));
						return true;
					}
					String notes = "";

					for (int a = 3; a != args.length; a++)
						notes = notes == "" ? args[a] : notes + " " + args[a];
					String staff = (sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE";

					PunishmentUtils.punish(staff, uid, inf, sev, notes);
				} else
					sender.sendMessage(CoreUtils.prefixes("reports")
							+ "Sorry couldn't find the player. If they aren't online, please use"
							+ " their full (case-sensitive) name.");
			}

			return true;
		} else {
			/*
			 * 
			 * Normal Players
			 * 
			 */
			if (args.length == 0) {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("reports") + "Try \"/report <player>\""));
				return true;
			}
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("complete")) {
					List<Object> info = PunishmentUtils.punishmentBuilder.get(reporter);
					if (info == null) {
						sender.sendMessage(
								CoreUtils.prefixes("error") + "You aren't in a report editor. Try /report <player>");
						return true;
					}

					String x = "";
					for (int a = 1; a != args.length; a++) {
						x = x == "" ? args[1] : x + " " + args[a];
					}
					Object rm = null;
					Object ad = null;
					for (Object o : info) {
						if (o instanceof String) {
							ad = (((String) o) + x);
							rm = o;
						}

					}

					info.remove(rm);
					info.add(ad);

					PunishmentUtils.punishmentBuilder.put(reporter, info);

					if (!PunishmentUtils.finishPunishment(reporter)) {
						sender.sendMessage(
								CoreUtils.prefixes("error") + "You aren't in a report editor. Try /report <player>");
					}

					return true;
				}
				UUID uid = null;
				if (Bukkit.getPlayer(args[0]) == null) {
					uid = CoreUtils.LookupUUID(args[0]);
				} else {
					uid = Bukkit.getPlayer(args[0]).getUniqueId();
				}
				if (uid != null) {
					((Player) sender).setMetadata("report", new FixedMetadataValue(Main.getPlugin(), args[0]));
					GUIManager.openInventory(((Player) sender),

							PunishmentUtils.getPunishmentGUI(""), "OffenceTypes");
				} else
					sender.sendMessage(CoreUtils.prefixes("reports")
							+ "Sorry couldn't find the player. If they aren't online, please use"
							+ " their full (case-sensitive) name.");
			}
			return true;
		}
	}
}
