package net.mysticcloud.spigot.core.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SeenCommand implements CommandExecutor {

	public SeenCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.seen")){
			if(args.length == 1) {
				UUID uid = CoreUtils.LookupUUID(args[0]);
				if(uid != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
					
					String seen = sdf.format(new Date(CoreUtils.LookupLastSeen(uid)));
					sender.sendMessage(CoreUtils.colorize("&3&lMysticCore &7>&f " + args[0] + "was last seen: " + (seen)));
					
				} else {
					sender.sendMessage(CoreUtils.colorize("&3&lMysticCore &7>&f That player couldn't be found in the database."));
				}
				
			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "/seen <user>");
			}
		}
		return true;
	}
}
