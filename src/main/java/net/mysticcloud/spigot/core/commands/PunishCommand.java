package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentType;
import net.mysticcloud.spigot.core.utils.punishment.PunishmentUtils;

public class PunishCommand implements CommandExecutor {

	public PunishCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player)
				PunishmentUtils.punish(((Player) sender).getName(), ((Player) sender).getUniqueId(), PunishmentType.MUTE);
			return false;
		}
		if (args.length == 1) {
			if (sender instanceof Player) {
				if(Bukkit.getPlayer(args[0]) != null)
				GUIManager.openInventory(((Player) sender),
						PunishmentUtils.getPunishmentGUI(Bukkit.getPlayer(args[0]).getUniqueId().toString()), "Punishment Dashboard");
				else sender.sendMessage("Player not online. Use the /opunish command to punish offline users.");
			}
		}
		
		return true;
	}
}
