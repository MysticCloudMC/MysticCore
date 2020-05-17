package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.TestChicken;
import net.mysticcloud.spigot.core.utils.entities.TestZombie;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;

public class TeleportCommand implements CommandExecutor {

	public TeleportCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			plugin.getCommand(cmd).setExecutor(this);
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (args.length == 0)
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tp <user> <to-user>\""
						+ ((sender instanceof Player) ? " or \"/tp <to-user>\"" : ""));

			if (args.length == 1)
				if (sender.hasPermission("mysticcloud.cmd.tp"))
					if (sender instanceof Player)
						if (Bukkit.getPlayer(args[0]) != null)
							CoreUtils.teleportPlayer(((Player) sender), Bukkit.getPlayer(args[0]));
						else
							sender.sendMessage(CoreUtils.prefixes("teleport") + "Sorry that player isn't online.");

					else
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			if (args.length == 2) {
				if (sender.hasPermission("mysticcloud.admin.tpother")) {
					String name = sender instanceof Player ? ((Player) sender).getName() : "CONSOLE";
					if (Bukkit.getPlayer(args[0]) != null)
						if (Bukkit.getPlayer(args[1]) != null)
							CoreUtils.teleportPlayer(name, Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]));
						else
							sender.sendMessage(CoreUtils
									.colorize(CoreUtils.prefixes("teleport") + "&7" + args[1] + "&f isn't online."));

					else
						sender.sendMessage(CoreUtils
								.colorize(CoreUtils.prefixes("teleport") + "&7" + args[0] + "&f isn't online."));

				} else
					sender.sendMessage(CoreUtils.colorize(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command."));

			}
		}

		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpa")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tpa <player>\"");

					if (args.length >= 1)
						if (Bukkit.getPlayer(args[0]) != null)
							TeleportUtils.requestTeleport(((Player) sender), Bukkit.getPlayer(args[0]));
						else
							sender.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		if (cmd.getName().equalsIgnoreCase("tpaccept"))
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpaccept"))
					TeleportUtils.acceptTeleport(((Player) sender));
				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		if (cmd.getName().equalsIgnoreCase("tpdeny"))
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpdeny"))
					TeleportUtils.denyTeleport(((Player) sender));
				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.admin.tphere")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tphere <player>\"");

					if (args.length >= 1)
						if (Bukkit.getPlayer(args[0]) != null)
							TeleportUtils.teleportPlayer(Bukkit.getPlayer(args[0]), ((Player) sender));
						else
							sender.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		if (cmd.getName().equalsIgnoreCase("tpoff")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.admin.tpoff")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Teleport requests "
								+ CoreUtils.colorize(TeleportUtils.toggleTeleportRequests((Player) sender) ? "&cdisabled&f."
										: "&aenabled&f."));

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		return true;
	}
}
