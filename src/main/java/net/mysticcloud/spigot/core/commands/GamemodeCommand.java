package net.mysticcloud.spigot.core.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.MysticPlayer;

public class GamemodeCommand implements CommandExecutor {

	public GamemodeCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if(sender.hasPermission("mysticcloud.admin.cmd.gamemode")) {
				if (cmd.getName().equalsIgnoreCase("gamemode")) {
					if (args.length == 0) {
						sender.sendMessage(CoreUtils.prefixes("gamemode") + "Your current gamemode is "
								+ ((Player) sender).getGameMode().name());
						return true;
					}
					switch (args[0].toLowerCase()) {
					case "creative":
					case "c":
						CoreUtils.setGameMode((Player) sender, GameMode.CREATIVE);
						break;

					case "survival":
					case "s":
						CoreUtils.setGameMode((Player) sender, GameMode.SURVIVAL);
						break;

					case "adventure":
					case "a":
						CoreUtils.setGameMode((Player) sender, GameMode.ADVENTURE);
						break;

					case "spectator":
					case "sp":
						CoreUtils.setGameMode((Player) sender, GameMode.SPECTATOR);
						break;
						default:
							sender.sendMessage(CoreUtils.prefixes("gamemode") + "Your current gamemode is "
									+ ((Player) sender).getGameMode().name());
							break;
					}
					return true;
					
				}
				if (cmd.getName().equalsIgnoreCase("gmc")) {
					CoreUtils.setGameMode((Player) sender, GameMode.CREATIVE);
				}
				if (cmd.getName().equalsIgnoreCase("gms")) {
					CoreUtils.setGameMode((Player) sender, GameMode.SURVIVAL);
				}
				if (cmd.getName().equalsIgnoreCase("gmsp")) {
					CoreUtils.setGameMode((Player) sender, GameMode.SPECTATOR);
				}
				if (cmd.getName().equalsIgnoreCase("gma")) {
					CoreUtils.setGameMode((Player) sender, GameMode.ADVENTURE);
				}
			} else {
				sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
			}
		}
		return true;
	}
}
