package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SudoCommand implements CommandExecutor {

	public SudoCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.sudo")){
			if(args.length > 1) {
				if(Bukkit.getPlayer(args[0]) == null) {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Player not online.");
					return true;
				}
				String command = "";
				for(String s : args) {
					command = command == "" ? s : command + " " + s;
				}
				if(args[1].startsWith("/")) {
					Bukkit.getPlayer(args[0]).performCommand(command);
				} else {
					Bukkit.getPlayer(args[0]).chat(command);
				}
			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/sudo <user> <command>");
			}
		}
		return true;
	}
}
