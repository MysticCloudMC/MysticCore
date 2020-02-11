package main.java.net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.net.mysticcloud.spigot.core.Main;

public class EditSignCommand implements CommandExecutor {

	public EditSignCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.signedit")){
//			if(args.length < 4)
		}
		return false;
	}
}
