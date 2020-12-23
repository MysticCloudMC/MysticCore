package net.mysticcloud.spigot.core.commands;

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
		if (args.length == 0) {
			CoreUtils.removeTag(((Player) sender));
		} else
			CoreUtils.setTag(((Player) sender), CustomTag.valueOf(args[0]));
		return true;
	}
}
