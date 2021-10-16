package net.mysticcloud.spigot.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.MysticCore;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class GamemodeCommand implements CommandExecutor {

	public GamemodeCommand(MysticCore plugin, String... cmds) {
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
					boolean other = !player.getUniqueId().equals(((Player) sender).getUniqueId());
					if (args.length == 2 && !sender.hasPermission("mysticcloud.admin.cmd.gamemode.others")) {
						sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
						return true;
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
						CoreUtils.setGameMode(player, player.getGameMode());
						break;
					}
					if (other) {
						sender.sendMessage(CoreUtils.prefixes("gamemode")
								+ CoreUtils.colorize("You set &7" + formatPossessive(player.getName()) + "&f gamemode to &7" + player.getGameMode().name().toLowerCase() + "&f."));
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
					Bukkit.dispatchCommand(sender, "gamemode c" + (args.length > 0 ? " " + args[0] : ""));
				}
				if (cmd.getName().equalsIgnoreCase("gms")) {
					Bukkit.dispatchCommand(sender, "gamemode s" + (args.length > 0 ? " " + args[0] : ""));
				}
				if (cmd.getName().equalsIgnoreCase("gmsp")) {
					Bukkit.dispatchCommand(sender, "gamemode sp" + (args.length > 0 ? " " + args[0] : ""));
				}
				if (cmd.getName().equalsIgnoreCase("gma")) {
					Bukkit.dispatchCommand(sender, "gamemode a" + (args.length > 0 ? " " + args[0] : ""));
				}
			} else {
				sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
			}
		}
		return true;
	}

	private String formatPossessive(String name) {
		return name + ((name.endsWith("s") || name.endsWith("x")) ? "'" : "'s");
	}

	private String compileString(String[] args) {
		String s = "";
		for (String a : args) {
			s = s == "" ? a : s + " " + a;
		}
		return s;
	}
}
