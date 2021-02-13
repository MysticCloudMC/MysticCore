package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.CustomTag;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;

public class TagCommand implements CommandExecutor {

	public TagCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			PluginCommand com = plugin.getCommand(cmd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			Player player = (Player) sender;
			if (args.length == 0) {
				GUIManager.openInventory(player, GUIManager.getTagsMenu(player), "tags");
			}
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length >= 3) {
						String tag = args[2];
						if (args.length > 3) {
							for (int s = 3; s != args.length; s++)
								tag = tag + " " + args[s];
							CustomTag.addTag(args[1], tag);
						}
					} else {
						player.sendMessage(
								CoreUtils.colorize(CoreUtils.prefixes("tags") + "Try /tags add <name> <value>"));
					}
				}
				if (args[0].equalsIgnoreCase("list")) {
					for(String key : CustomTag.keys()) {
						player.sendMessage(key + ": " + PlaceholderUtils.emotify(CustomTag.getTag(key)));
					}
				}
				if (args[0].equalsIgnoreCase("remove")) {
					CustomTag.removeTag(args[1]);
				}
			}

		} else {
			if (args.length != 2) {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("tags") + "Try \"/tags <player> <type>\"."));
			} else {
				if (Bukkit.getPlayer(args[0]) != null) {
					if (args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove")) {
						CoreUtils.removeTag(Bukkit.getPlayer(args[0]));
					}
					if (!CustomTag.getTag(args[1]).contains("[NT]")) {
						CoreUtils.setTag(Bukkit.getPlayer(args[0]), CustomTag.getTag(args[1]));
					} else
						sender.sendMessage(
								CoreUtils.colorize(CoreUtils.prefixes("tags") + "Sorry, that tag doesn't exist."));

				} else
					sender.sendMessage(
							CoreUtils.colorize(CoreUtils.prefixes("tags") + "Sorry, the player must be online."));

			}
		}
		return true;
	}
}
