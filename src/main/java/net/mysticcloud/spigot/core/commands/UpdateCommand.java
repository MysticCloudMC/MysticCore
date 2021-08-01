package net.mysticcloud.spigot.core.commands;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

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
				String url = "/job/" + plugin + "/lastSuccessfulBuild/artifact/target/"
						+ filename;
				sender.sendMessage(CoreUtils.prefixes("admin") + "Downloading " + filename + "...");
				
				try {
					String website = "http://jenkins.mysticcloud.net" + url;
					URL uri = new URL(website);
					InputStream inputStream = uri.openStream();
					OutputStream outputStream = new FileOutputStream(Main.getPlugin().getDataFolder().getParentFile().getAbsolutePath() + "/" + filename);
					byte[] buffer = new byte[2048];

					int length = 0;

					while ((length = inputStream.read(buffer)) != -1) {
						System.out.println("Buffer Read of length: " + length);
						outputStream.write(buffer, 0, length);
					}

					inputStream.close();
					outputStream.close();

				} catch (Exception e1) {
					sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils.colorize(
							"There was an error downloading that plugin. Make sure it's on the Jenkins. (&ohttp://jenkins.mysticcloud.net/"
									+ ChatColor.getLastColors(CoreUtils.prefixes("admin")) + ")"));
					e1.printStackTrace();
				}
				
				
				
				
//				try {
//					InputStream in = new URL(url).openStream();
//					Files.copy(in, Paths
//							.get(Main.getPlugin().getDataFolder().getParentFile().getAbsolutePath() + "/" + filename),
//							StandardCopyOption.REPLACE_EXISTING);
//					sender.sendMessage(CoreUtils.prefixes("admin") + "Done!");
////					Bukkit.broadcastMessage("Done!");
//				} catch (IOException e) {
//					sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils.colorize(
//							"There was an error downloading that plugin. Make sure it's on the Jenkins. (&ohttp://jenkins.mysticcloud.net/"
//									+ ChatColor.getLastColors(CoreUtils.prefixes("admin")) + ")"));
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}

		}
		return true;
	}
}
