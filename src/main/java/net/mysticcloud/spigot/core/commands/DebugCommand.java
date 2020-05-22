package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.debug")){
			
			if(args.length == 0)
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("time")) {
					sender.sendMessage(CoreUtils.prefixes("date") + "Today is: " + CoreUtils.getMonth() + " " + CoreUtils.getDay() + " - " + CoreUtils.getTime());
					
				}
			}
		}
		return true;
	}
}
