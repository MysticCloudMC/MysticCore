package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class EconomyCommand implements CommandExecutor {

	public EconomyCommand(Main plugin, String... cmds){
		for(String cmd : cmds)plugin.getCommand(cmd).setExecutor(this);
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("balance")) {
			if(sender instanceof Player) {
				if(args.length == 1) {
				CoreUtils.getEconomy().depositPlayer(((Player)sender).getUniqueId().toString(), Double.parseDouble(args[0]));
				} else {
					Bukkit.broadcastMessage("" + CoreUtils.getEconomy().getBalance(((Player)sender).getUniqueId().toString()));
				}
			} else {
				sender.sendMessage(CoreUtils.prefixes("eco") + "Only players can use that command.");
			}
		}
		return true;
	}
}
