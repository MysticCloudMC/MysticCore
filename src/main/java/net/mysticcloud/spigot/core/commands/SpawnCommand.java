package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.SpawnReason;

public class SpawnCommand implements CommandExecutor {

	public SpawnCommand(Main plugin, String... cmd) {
		for(String comd : cmd) {
			PluginCommand com = plugin.getCommand(comd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}
		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (CoreUtils.getSpawnLocation() == null) {
				sender.sendMessage(
						CoreUtils.colorize(CoreUtils.fullPrefix + "The spawn point is not yet set. (/setspawn)"));
				return true;
			}
			if (args.length == 0) {
				if (sender instanceof Player) {
					CoreUtils.teleportToSpawn(((Player) sender));
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /spawn <player>");
				}
			}
			if (args.length == 1) {
				if (sender.hasPermission("mysticcloud.spawn.other")) {
					if (Bukkit.getPlayer(args[0]) == null) {
						sender.sendMessage(CoreUtils.prefixes("admin") + "That player is not currently online.");
					} else {
						CoreUtils.teleportToSpawn(Bukkit.getPlayer(args[0]), SpawnReason.OTHER);
						sender.sendMessage(CoreUtils.prefixes("admin")
								+ CoreUtils.colorize("Teleported &7" + args[0] + "&f to spawn."));
					}
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin")
							+ CoreUtils.colorize("That is an admin only command."));
					
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			// Spawn set code
			if (args.length == 0)
				if (sender instanceof Player) {
					CoreUtils.setSpawnLocation(((Player) sender).getLocation());
					sender.sendMessage(CoreUtils.prefixes("admin") + "Set spawn!");
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /setspawn <world> <x> <y> <x>");
				}
		}
		return true;
	}
}
