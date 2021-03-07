package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticPlayer;

public class GamemodeCommand implements CommandExecutor {

	public GamemodeCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("mysticcloud.admin.cmd.gamemode")) {
				if (cmd.getName().equalsIgnoreCase("gamemode")) {
					if (args.length == 0) {
						sender.sendMessage(CoreUtils.prefixes("gamemode") + "Your current gamemode is "
								+ ((Player) sender).getGameMode().name());
						return true;
					}
					Player player = args.length == 1 ? ((Player) sender) : Bukkit.getPlayer(args[1]);
					if (player == null) {
						sender.sendMessage(CoreUtils.prefixes("That player isn't online."));
						return true;
					}
					if (args.length == 2 && !sender.hasPermission("mysticcloud.admin.cmd.gamemode.others")) {
						sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
					}
					switch (args[0].toLowerCase()) {
					case "creative":
					case "c":
						CoreUtils.setGameMode(player, GameMode.CREATIVE);
						break;

					case "survival":
					case "s":
						CoreUtils.setGameMode(player, GameMode.SURVIVAL);
						break;

					case "adventure":
					case "a":
						CoreUtils.setGameMode(player, GameMode.ADVENTURE);
						break;

					case "spectator":
					case "sp":
						CoreUtils.setGameMode(player, GameMode.SPECTATOR);
						break;
					default:
						sender.sendMessage(CoreUtils.prefixes("gamemode") + "Your current gamemode is "
								+ ((Player) sender).getGameMode().name());
						break;
					}
					return true;

				}
				Player player = args.length == 0 ? ((Player) sender) : Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(CoreUtils.prefixes("That player isn't online."));
					return true;
				}
				if (args.length == 1 && !sender.hasPermission("mysticcloud.admin.cmd.gamemode.others")) {
					sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
				}
				if (cmd.getName().equalsIgnoreCase("gmc")) {
					CoreUtils.setGameMode(player, GameMode.CREATIVE);
				}
				if (cmd.getName().equalsIgnoreCase("gms")) {
					CoreUtils.setGameMode(player, GameMode.SURVIVAL);
				}
				if (cmd.getName().equalsIgnoreCase("gmsp")) {
					CoreUtils.setGameMode(player, GameMode.SPECTATOR);
				}
				if (cmd.getName().equalsIgnoreCase("gma")) {
					CoreUtils.setGameMode(player, GameMode.ADVENTURE);
				}
			} else {
				sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
			}
		}
		return true;
	}
}
