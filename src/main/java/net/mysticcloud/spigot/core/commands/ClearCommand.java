package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class ClearCommand implements CommandExecutor {

	public ClearCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.clear")) {

			if (args.length == 0) {
				if (sender instanceof Player) {

					((Player) sender)
							.sendMessage(CoreUtils.prefixes("root") + ((Player) sender).getInventory().getSize()
									+ " items have been removed from your inventory.");
					((Player) sender).getInventory().clear();
					((Player)sender).updateInventory();
				} else {
					sender.sendMessage("Player only command silly. Try /clear <player>");
				}
			}
			if(args.length >= 1) {
				if(Bukkit.getPlayer(args[0])!= null) {
					if(sender instanceof Player) {
						sender.sendMessage(CoreUtils.prefixes("root") + "You have cleared " + Bukkit.getPlayer(args[0]).getInventory().getSize() + " items from " + args[0] + "'s inventory.");
					}
					Bukkit.getPlayer(args[0]).getInventory().clear();
					Bukkit.getPlayer(args[0]).updateInventory();
				} else {
					sender.sendMessage("Player not online.");
				}
			}
			
		}
		return true;
	}
}
