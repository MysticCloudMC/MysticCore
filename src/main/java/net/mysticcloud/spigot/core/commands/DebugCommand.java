package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.TextComponent;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.debug")) {

			if (args.length == 0)
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("color")) {
					sender.spigot().sendMessage(new TextComponent("{\"text\":\"Hello!\",\"color\":\"#22FF22\"}"));
				}
				if (args[0].equalsIgnoreCase("time")) {
					sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.getTime()));

				}
			}
		}
		return true;
	}
}
