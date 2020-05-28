package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SudoCommand implements CommandExecutor {

	public SudoCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.sudo")) {
			if (args.length > 1) {
				if (Bukkit.getPlayer(args[0]) == null) {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Player not online.");
					return true;
				}
				String command = "";
				for (int s = 1; s != args.length; s++) {
					command = command == "" ? args[s] : command + " " + args[s];
				}
				if (args[1].startsWith("/")) {
					command = command.replaceFirst("/", "");
					Bukkit.getPlayer(args[0]).performCommand(command);
					return true;
				}
				if (args[1].startsWith("*walk")) {
					command = command.replaceFirst("*walk", "");
					String[] loc = command.split(" ");
					Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(args[0]).getLocation()
							.add(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2])));
					return true;
				}

				Bukkit.getPlayer(args[0]).chat(command);

			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/sudo <user> <command>");
			}
		}
		return true;
	}
}
