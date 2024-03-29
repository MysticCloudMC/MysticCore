package net.mysticcloud.spigot.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class EconomyCommand implements CommandExecutor {

	public EconomyCommand(MysticCore plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pay")) {
			if (args.length <= 1)
				return false;
			if (Bukkit.getPlayer(args[0]) == null && CoreUtils.LookupUUID(args[0]) == null) {
				sender.sendMessage(
						CoreUtils.prefixes("eco") + "Sorry, the player you are trying to pay hasn't played before.");
				return true;
			}
			UUID uid = Bukkit.getPlayer(args[0]) == null ? CoreUtils.LookupUUID(args[0])
					: Bukkit.getPlayer(args[0]).getUniqueId();
			if (Double.parseDouble(args[1]) < 0) {
				sender.sendMessage(CoreUtils.prefixes("eco") + "Value must be above 0.");
				return true;
			}
			if (!(sender instanceof Player) || CoreUtils.getEconomy().has(((Player) sender).getUniqueId().toString(),
					Double.parseDouble(args[1]))) {

				Economy eco = CoreUtils.getEconomy();
				if (sender instanceof Player)
					eco.withdrawPlayer(((Player) sender).getUniqueId().toString(), Double.parseDouble(args[1]));
				eco.depositPlayer(uid.toString(), Double.parseDouble(args[1]));
				sender.sendMessage(CoreUtils.prefixes("eco")
						+ CoreUtils.colorize("You paid &6$" + args[1] + "&f to &7" + args[0] + "&f."));
				if (Bukkit.getPlayer(args[0]) != null && sender instanceof Player)
					Bukkit.getPlayer(args[0]).sendMessage(CoreUtils.prefixes("eco")
							+ CoreUtils.colorize("&7" + sender.getName() + "&f has sent you &6$" + args[1] + "&f."));
			} else {
				sender.sendMessage(CoreUtils.prefixes("eco") + "You can't afford that.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("balance")) {
			if (sender instanceof Player) {

				if (args.length == 1 && sender.hasPermission("mysticcloud.admin.setbalance")) {
					CoreUtils.getEconomy().withdrawPlayer(((Player) sender).getUniqueId().toString(),
							CoreUtils.getEconomy().getBalance(((Player) sender).getUniqueId().toString()));
					CoreUtils.getEconomy().depositPlayer(((Player) sender).getUniqueId().toString(),
							Double.parseDouble(args[0]));

				}
				sender.sendMessage(CoreUtils.prefixes("eco") + CoreUtils.colorize("Your current balance is &6$"
						+ CoreUtils.getEconomy().getBalance(((Player) sender).getUniqueId().toString()) + "&f."));

			} else {
				sender.sendMessage(CoreUtils.prefixes("eco") + "Only players can use that command.");
			}
		}
		return true;
	}
}
