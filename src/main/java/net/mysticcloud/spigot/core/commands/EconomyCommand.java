package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class EconomyCommand implements CommandExecutor {

	public EconomyCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pay")) {
			if (args.length <= 1)
				return false;
			if (Bukkit.getPlayer(args[0]) == null) {
				sender.sendMessage(
						CoreUtils.prefixes("eco") + "Sorry, the player you are trying to pay must be online.");
				return true;
			}
			if (CoreUtils.getEconomy().has(((Player) sender).getUniqueId().toString(), Double.parseDouble(args[1]))) {
				Economy eco = CoreUtils.getEconomy();
				eco.withdrawPlayer(((Player) sender).getUniqueId().toString(), Double.parseDouble(args[1]));
				eco.depositPlayer(Bukkit.getPlayer(args[0]).getUniqueId().toString(), Double.parseDouble(args[1]));
				sender.sendMessage(CoreUtils.prefixes("eco")
						+ CoreUtils.colorize("You paid &6$" + args[1] + "&f to &7" + args[0] + "&f."));
			}
		}
		if (cmd.getName().equalsIgnoreCase("balance")) {
			if (sender instanceof Player) {
				if (args.length == 1 && sender.hasPermission("mysticcloud.admin.setbalance")) {
					CoreUtils.getEconomy().depositPlayer(((Player) sender).getUniqueId().toString(),
							Double.parseDouble(args[0]));
					sender.sendMessage(CoreUtils.prefixes("eco") + CoreUtils.colorize("Your current balance is &6$"
							+ CoreUtils.getEconomy().getBalance(((Player) sender).getUniqueId().toString()) + "&f."));

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
