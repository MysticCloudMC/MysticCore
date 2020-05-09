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

public class PunishCommand implements CommandExecutor {

	public PunishCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(CoreUtils.prefixes("punishments")
					+ ((sender instanceof Player) ? "Try \"/punish <player>\" &7OR&f t" : "T")
					+ "ry \"/punish <player> <severity-MINIMAL|LOW|MEDIUM|HIGH|EXTREME> [notes]\"");
			return true;
		}
		if (args.length >= 1) {

			if (args[0].equalsIgnoreCase("complete")) {
				List<Object> info = PunishmentUtils.punishmentBuilder
						.get((sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE");

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

				PunishmentUtils.punishmentBuilder
						.put((sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE", info);

				if (!PunishmentUtils
						.finishPunishment((sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE")) {
					sender.sendMessage(
							CoreUtils.prefixes("error") + "You aren't in a punishment editor. Try /punish <player>");
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
					((Player) sender).setMetadata("punish", new FixedMetadataValue(Main.getPlugin(), args[0]));
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
					sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("punishments")
							+ "Try \"&c/punish <player> <CHAT|HACKING|GREIF> <MINIMAL,LOW,MEDIUM,HIGH,EXTREME> [notes]"));
					return true;
				}
				String notes = "";

				for (int a = 3; a != args.length; a++)
					notes = notes == "" ? args[a] : notes + " " + args[a];
				String staff = (sender instanceof Player) ? ((Player) sender).getName() : "CONSOLE";

				PunishmentUtils.punish(staff, uid, inf, sev, notes);
			} else
				sender.sendMessage(CoreUtils.prefixes("punishments")
						+ "Couldn't find that player online, or in the MysticCloud UUID database.");
		}

		return true;
	}
}
