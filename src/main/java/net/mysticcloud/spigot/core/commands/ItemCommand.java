package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.md_5.bungee.api.ChatColor;

public class ItemCommand implements CommandExecutor {

	public ItemCommand(Main plugin, String cmd) {
		PluginCommand com = plugin.getCommand(cmd);
		com.setExecutor(this);
		com.setTabCompleter(new CommandTabCompleter());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(CoreUtils.prefixes().get("items")
					+ ((sender instanceof Player) ? "Usage: /item <item>" : "Usage: /item <item> <player>"));
		}
		if (args.length == 1) {
			if (sender instanceof Player && sender.hasPermission("mysticcloud.item." + args[0].toLowerCase())) {
				ItemStack i = CoreUtils.getItem(args[0]);
				((Player) sender).getInventory().addItem(i);
				if (ChatColor.stripColor(i.getItemMeta().getDisplayName()).equalsIgnoreCase("ERROR")) {
					sender.sendMessage(CoreUtils.prefixes("items") + "There was an error finding that item...");
				} else {
					sender.sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize("Gave &7"
							+ ((Player) sender).getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));
				}

			} else
				sender.sendMessage(CoreUtils.prefixes().get("items")
						+ CoreUtils.colorize("You don't have permission to use that command."));
		}
		if (args.length == 2) {
			if (sender instanceof Player && sender.hasPermission("mysticcloud.item." + args[0].toLowerCase())) {
				Player player = (Player) sender;
				ItemStack i = CoreUtils.getItem(args[0]);
				i.setAmount(Integer.parseInt(args[1]));
				player.getInventory().addItem(i);
				sender.sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize(
						"Gave &7" + ((Player) sender).getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));
			}
		}
		if (args.length == 3) {
			if (sender.hasPermission("mysticcloud.item." + args[0].toLowerCase())
					&& sender.hasPermission("mysticcloud.item.give")) {
				if (Bukkit.getPlayer(args[2]) == null) {
					if (args[2].equalsIgnoreCase("all") || args[2].equalsIgnoreCase("*")) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							ItemStack i = CoreUtils.getItem(args[0]);
							i.setAmount(Integer.parseInt(args[1]));
							player.getInventory().addItem(i);
							sender.sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize(
									"Gave &7" + player.getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));
							player.sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize(
									"Gave &7" + player.getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));

						}
						return true;
					}
					sender.sendMessage(CoreUtils.prefixes().get("items") + "That player is not online.");
					return true;
				}
				ItemStack i = CoreUtils.getItem(args[0]);
				i.setAmount(Integer.parseInt(args[1]));
				Bukkit.getPlayer(args[2]).getInventory().addItem(i);
				sender.sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize("Gave &7"
						+ Bukkit.getPlayer(args[2]).getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));
				Bukkit.getPlayer(args[2]).sendMessage(CoreUtils.prefixes("items") + CoreUtils.colorize("Gave &7"
						+ Bukkit.getPlayer(args[2]).getName() + "&f " + i.getAmount() + " of &7" + args[0] + "&f."));
			}
		}

		return true;
	}
}
