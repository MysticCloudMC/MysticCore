package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

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
				if (args[1].startsWith("-punch")) {
					for(Entity e : Bukkit.getPlayer(args[0]).getNearbyEntities(5, 5, 5)) {
						if(e.equals(Bukkit.getPlayer(args[0]))) continue;
						if(Bukkit.getPlayer(args[0]).hasLineOfSight(e) && e instanceof LivingEntity) {
							((LivingEntity)e).damage(0.1, Bukkit.getPlayer(args[0]));
							break;
						}
					}
					return true;
					
				}
				if (args[1].startsWith("-walk")) {
					command = command.replaceFirst("-walk ", "");
					String[] loc = command.split(" ");
					Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(args[0]).getLocation()
							.add(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2])));
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
