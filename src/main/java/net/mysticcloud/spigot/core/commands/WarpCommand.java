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
import net.mysticcloud.spigot.core.utils.warps.WarpBuilder;
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
				if (args.length == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Here is a list of avalible warps:");
					List<String> warps = new ArrayList<>();
					for (String type : WarpUtils.getWarpTypes()) {
						String m = "&3" + type + "&f:";
						for (Warp warp : WarpUtils.getWarps(type)) {
							m = m + " " + warp.name();
						}
						warps.add(m);
					}
					for (String m : warps) {
						sender.sendMessage(CoreUtils.colorize(m));
					}
					return false;
				}
				String type = args.length >= 2 ? args[0] : "warp";
				String name = args.length >= 2 ? args[1] : args[0];
				int sel = args.length >= 3 ? Integer.parseInt(args[2]) - 1 : 0;
				List<Warp> warps = WarpUtils.getWarps(type, name);

				if (warps.size() == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Can't find that warp...");
					return false;
				}

				if (warps.size() >= 2 && sel == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "There are more than one warp with that name.");
					sender.sendMessage(CoreUtils.colorize("&3Warping to first in list. To specify type \"&f/warp " + type
							+ " " + name + " <1-" + warps.size() + ">&3\""));
				}
				((Player) sender).teleport(warps.get(sel).location());
				sender.sendMessage(CoreUtils.prefixes("warps") + "Teleporting to " + (type.equalsIgnoreCase("warp") ? "" : type + ":") + name);

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
				WarpBuilder warp = new WarpBuilder();
				warp.createWarp()
						.setType(args.length >= 2 ? args[0] : "warp")
						.setName(args.length >= 2 ? args[1] : args[0])
						.setLocation(((Player) sender).getLocation())
						.getWarp();
				sender.sendMessage(CoreUtils.prefixes("warps") + "Warp created! "
						+ (args.length >= 2 ? args[0] : "warp") + ":" + (args.length >= 2 ? args[1] : args[0]));

			} else {
				sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("removewarp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Here is a list of avalible warps:");
					List<String> warps = new ArrayList<>();
					for (String type : WarpUtils.getWarpTypes()) {
						String m = "&3" + type + "&f:";
						for (Warp warp : WarpUtils.getWarps(type)) {
							m = m + " " + warp.name();
						}
						warps.add(m);
					}
					for (String m : warps) {
						sender.sendMessage(CoreUtils.colorize(m));
					}
					return false;
				}
				String type = args.length >= 2 ? args[0] : "warp";
				String name = args.length >= 2 ? args[1] : args[0];
				int sel = args.length >= 3 ? Integer.parseInt(args[2]) - 1 : 0;
				List<Warp> warps = WarpUtils.getWarps(type, name);

				if (warps.size() == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "Can't find that warp...");
					return false;
				}

				if (warps.size() >= 2 && sel == 0) {
					sender.sendMessage(CoreUtils.prefixes("warps") + "There are more than one warp with that name.");
					sender.sendMessage(CoreUtils.colorize("&3To specify type \"&f/removewarp " + type
							+ " " + name + " <1-" + warps.size() + ">&3\""));
					return true;
				}
				
				WarpUtils.removeWarp(type,warps.get(sel));
				sender.sendMessage(CoreUtils.prefixes("warps") + "Removed warp " + type + ":" + name);

			} else {
				sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
				return true;
			}
		}

		return true;
	}
}
