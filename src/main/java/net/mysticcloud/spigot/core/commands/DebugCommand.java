package main.java.net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.net.mysticcloud.spigot.core.Main;
import main.java.net.mysticcloud.spigot.core.utils.CoreUtils;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.debug")){
			sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));
		}
		return true;
	}
}
