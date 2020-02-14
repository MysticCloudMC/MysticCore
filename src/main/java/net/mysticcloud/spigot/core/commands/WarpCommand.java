package net.mysticcloud.spigot.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.warps.Warp;
import net.mysticcloud.spigot.core.utils.warps.WarpUtils;

public class WarpCommand implements CommandExecutor {

	public WarpCommand(Main plugin, String... cmd) {
		for (String comd : cmd) {
			PluginCommand com = plugin.getCommand(comd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("warp")) {
			if (sender instanceof Player) {
				if(args.length == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Here is a list of avalible warps:");
					List<String> warps = new ArrayList<>();
					for (String type : WarpUtils.getWarpTypes()) {
						String m = type + ":";
						for (Warp warp : WarpUtils.getWarps(type)) {
							m = m + " " + warp.name();
						}
						warps.add(m);
					}
					for (String m : warps) {
						sender.sendMessage(m);
					}
					return false;
				}
				String type = args.length == 2 ? args[0] : "warp";
				String name = args.length == 2 ? args[1] : args[0];
				
				if(WarpUtils.getWarp(type, name) != null) {
					((Player)sender).teleport(WarpUtils.getWarp(type,name).location());
					sender.sendMessage(CoreUtils.prefixes("warps") + "You have been warped to " + name);
				} else {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Can't find that warp...");
				}
				
			} else {
				sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("addwarp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					return false;
				}
				WarpUtils.addWarp(args.length == 2 ? args[0] : "warp",
						new Warp(args.length == 2 ? args[1] : args[0], ((Player) sender).getLocation()));
				sender.sendMessage(CoreUtils.prefixes("warps") + "Warp created!");

			} else {
				sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("removewarp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					return false;
				}
				WarpUtils.removeWarp(args.length == 2 ? args[0] : "warp",
						new Warp(args.length == 2 ? args[1] : args[0], ((Player) sender).getLocation()));
				sender.sendMessage(CoreUtils.prefixes("warps") + "Warp removed!");

			} else {
				sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
			}
		}

		return true;
	}
}
