package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class UpdateCommand implements CommandExecutor {

	public UpdateCommand(MysticCore plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.update")) {
			if (args.length == 1) {

				String plugin = args[0];
				String filename = plugin + ".jar";
				String url = "https://jenkins.mysticcloud.net/job/" + plugin
						+ "/job/master/lastSuccessfulBuild/artifact/target/" + filename;
				sender.sendMessage(CoreUtils.prefixes("admin") + "Downloading " + filename + "...");
				if (CoreUtils.downloadFile(url, "plugins/" + filename, "admin", "BAG3jbe!Q#C7XaYJ"))
					sender.sendMessage(
							CoreUtils.prefixes("admin") + CoreUtils.colorize("Finished downloading " + filename));
				else {
					sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils
							.colorize("There was an error downloading " + filename + ". Trying alt site..."));
					if (CoreUtils.downloadFile("https://downloads.mysticcloud.net/" + filename, "plugins/" + filename,
							"admin", "v4pob8LW"))
						sender.sendMessage(
								CoreUtils.prefixes("admin") + CoreUtils.colorize("Finished downloading " + filename));
					else {
						sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils
								.colorize("There was an error downloading " + filename + ". Trying alt site..."));
					}
				}

			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /update <plugin>");
			}

		}
		return true;
	}
}
