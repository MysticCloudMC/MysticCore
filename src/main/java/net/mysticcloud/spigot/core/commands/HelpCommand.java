package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class HelpCommand implements CommandExecutor {

	public HelpCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			PluginCommand com = plugin.getCommand(cmd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(CoreUtils.colorize("&3---------------&7[&3&lHelp Menu&7]&3---------------"));
		sender.sendMessage(CoreUtils.colorize(""));
		sender.sendMessage(CoreUtils.colorize("&f Need help with your current gamemode? There is always help posted at the /spawn of every gamemode"));
		sender.sendMessage(CoreUtils.colorize(""));
		sender.sendMessage(CoreUtils.colorize("&fNeed to report player/staff abuse? Head onto the forums and let us know please! :)"));
		sender.sendMessage(CoreUtils.colorize(""));
		sender.sendMessage(CoreUtils.colorize("&fNeed a referesher on the rules? /rules should help you out. :)"));
		sender.sendMessage(CoreUtils.colorize(""));
		sender.sendMessage(CoreUtils.colorize("&fWant to apply for staff? You can do that on the forums as-well."));
		sender.sendMessage(CoreUtils.colorize(""));
		sender.sendMessage(CoreUtils.colorize("&3-----------------------------------------"));
		return true;
	}
}
