package net.mysticcloud.spigot.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.kits.Kit;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;

public class KitCommand implements CommandExecutor {

	public KitCommand(Main plugin, String cmd) {
		PluginCommand com = plugin.getCommand(cmd);
		com.setExecutor(this);
		com.setTabCompleter(new CommandTabCompleter());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {

				List<String> s = new ArrayList<>();
				for (Kit kit : KitManager.getKits()) {
					if (sender.hasPermission("mysticcloud.kit." + kit.getName())) {
						s.add(kit.getName());
					}
				}
				if (sender.hasPermission("mysticcloud.kit.gui")) {
					GUIManager.openInventory(((Player) sender),KitManager.getGUI(((Player) sender)), "Kits");
				}
				sender.sendMessage(
						CoreUtils.prefixes().get("kits") + ("You have access to these kits: " + s.toString()));
			} else {
				sender.sendMessage(CoreUtils.prefixes().get("kits") + ("Only players can run that command."));
			}
		} else {
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("mysticcloud.kit.admin.reload")) {
				KitManager.reloadKits();
			} else {
				if (sender instanceof Player) {

					if (KitManager.kitExists(args[0])) {
						if (sender.hasPermission("mysticcloud.kit." + args[0])) {
							KitManager.applyKit(((Player) sender), args[0]);
							return false;
						} else {
							sender.sendMessage(
									CoreUtils.prefixes().get("kits") + ("You don't have permission to use that kit"));
						}

					} else {
						sender.sendMessage(CoreUtils.prefixes().get("kits") + ("That kit doesn't exist."));
					}
				} else {
					sender.sendMessage(CoreUtils.prefixes().get("kits") + ("Only players can run that command."));
				}

			}
		}

		return false;
	}

}
