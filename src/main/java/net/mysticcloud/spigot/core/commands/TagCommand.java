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
import net.mysticcloud.spigot.core.utils.CustomTag;

public class TagCommand implements CommandExecutor {

	public TagCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			PluginCommand com = plugin.getCommand(cmd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) {

				CoreUtils.removeTag(((Player) sender));
			} else
				CoreUtils.setTag(((Player) sender), CustomTag.valueOf(args[0]));
		} else {
			if (args.length != 2) {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("tags") + "Try \"/tags <player> <type>\"."));
			} else {
				if (Bukkit.getPlayer(args[0]) != null) {
					if (args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove")) {
						CoreUtils.removeTag(Bukkit.getPlayer(args[0]));
					}
					if (!CustomTag.tagFromName(args[1]).equals(CustomTag.NONE)) {
						CoreUtils.setTag(Bukkit.getPlayer(args[0]), CustomTag.tagFromName(args[1]));
					} else
						sender.sendMessage(
								CoreUtils.colorize(CoreUtils.prefixes("tags") + "Sorry, that tag doesn't exist."));

				} else
					sender.sendMessage(
							CoreUtils.colorize(CoreUtils.prefixes("tags") + "Sorry, the player must be online."));

			}
		}
		return true;
	}
}
