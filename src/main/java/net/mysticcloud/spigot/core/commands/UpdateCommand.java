package net.mysticcloud.spigot.core.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class UpdateCommand implements CommandExecutor {

	public UpdateCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.update")) {
			if (args.length == 1) {

				String plugin = args[0];
				String filename = plugin + ".jar";
				String url = "http://jenkins.mysticcloud.net/job/" + plugin + "/lastSuccessfulBuild/artifact/target/"
						+ filename;
				sender.sendMessage(CoreUtils.prefixes("admin") + "Downloading " + filename + "...");
				try {
					InputStream in = new URL(url).openStream();
					Files.copy(in, Paths
							.get(Main.getPlugin().getDataFolder().getParentFile().getAbsolutePath() + "/" + filename),
							StandardCopyOption.REPLACE_EXISTING);
					sender.sendMessage(CoreUtils.prefixes("admin") + "Done!");
//					Bukkit.broadcastMessage("Done!");
				} catch (IOException e) {
					sender.sendMessage(CoreUtils.prefixes("admin")
							+ "There was an error downloading that plugin. Make sure it's on the Jenkins. (&ohttp://jenkins.mysticcloud.net/"
							+ ChatColor.getLastColors(CoreUtils.prefixes("admin")) + ")");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return true;
	}
}
