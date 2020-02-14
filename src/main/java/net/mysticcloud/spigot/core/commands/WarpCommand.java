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
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("warp")) {
				sender.sendMessage(CoreUtils.prefixes("warps") + "Here is a list of avalible warps:");
				List<String> warps = new ArrayList<>();
				for(String type : WarpUtils.getWarpTypes()) {
					String m = type + ":";
					for(Warp warp : WarpUtils.getWarps(type)) {
						m = m + " " + warp.name();
					}
					warps.add(m);
				}
				for(String m : warps) {
					sender.sendMessage(m);
				}
			}

		} else {
			sender.sendMessage(CoreUtils.prefixes("warps") + "You must be a player to use that command.");
		}
		return true;
	}
}
