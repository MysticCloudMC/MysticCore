package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class UUIDCommand implements CommandExecutor {

	public UUIDCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.uuid")){
			if(args.length > 1) {
				sender.sendMessage(CoreUtils.colorize("&e&lUUID &7>&f Search result for \"" + args[0] + "\": " + CoreUtils.LookupUUID(args[0])));
			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/uuid <user>");
			}
		}
		return true;
	}
}
