package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.AdminCommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SudoCommand implements CommandExecutor {

	public SudoCommand(Main plugin, String cmd) {
		PluginCommand com = plugin.getCommand(cmd);
		com.setExecutor(this);
		com.setTabCompleter(new AdminCommandTabCompleter());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.sudo")) {
			boolean allplayers = false;
			if (args.length > 1) {
				if (Bukkit.getPlayer(args[0]) == null) {
					if (!args[0].equalsIgnoreCase("@a")) {
						sender.sendMessage(CoreUtils.prefixes("admin") + "Player not online.");
						return true;
					}
					allplayers = true;

				}
				String command = "";
				for (int s = 1; s != args.length; s++) {
					command = command == "" ? args[s] : command + " " + args[s];
				}
				if (args[1].startsWith("/")) {
					command = command.replaceFirst("/", "");
					if (allplayers)
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.performCommand(command);
						}
					else
						Bukkit.getPlayer(args[0]).performCommand(command);
					return true;
				}
				if (args[1].startsWith("-punch")) {
					if (allplayers) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							for (Entity e : player.getNearbyEntities(5, 5, 5)) {
								if (e.equals(player))
									continue;
								if (player.hasLineOfSight(e) && e instanceof LivingEntity) {
									((LivingEntity) e).damage(0.1, player);
									break;
								}
							}
						}
					} else
						for (Entity e : Bukkit.getPlayer(args[0]).getNearbyEntities(5, 5, 5)) {
							if (e.equals(Bukkit.getPlayer(args[0])))
								continue;
							if (Bukkit.getPlayer(args[0]).hasLineOfSight(e) && e instanceof LivingEntity) {
								((LivingEntity) e).damage(0.1, Bukkit.getPlayer(args[0]));
								break;
							}
						}
					return true;

				}
				if (args[1].startsWith("-walk")) {
					command = command.replaceFirst("-walk ", "");
					String[] loc = command.split(" ");
					if (allplayers)
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.teleport(Bukkit.getPlayer(args[0]).getLocation().add(Double.parseDouble(loc[0]),
									Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
						}
					else
						Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(args[0]).getLocation().add(
								Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
					return true;
				}
				if (allplayers)
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.chat(command);
					}
				else
					Bukkit.getPlayer(args[0]).chat(command);

			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/sudo <user> <command>");
			}
		}
		return true;
	}
}
