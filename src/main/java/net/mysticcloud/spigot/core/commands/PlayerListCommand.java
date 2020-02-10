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

public class PlayerListCommand implements CommandExecutor {

	public PlayerListCommand(Main plugin, String... cmd) {
		for (String comd : cmd) {
			PluginCommand com = plugin.getCommand(comd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			String s = "";
			for(Player player : Bukkit.getOnlinePlayers()){
				s = s + player.getName() + ", ";
			}
			s = s.substring(0, s.length()-2);
			sender.sendMessage(CoreUtils.fullPrefix + "Here's a list of online players: " + s);
			return true;
		}
		if (args.length >= 2) {
//			if(args[1].equalsIgnoreCase("header")) {
			String builder = "";
			for (int i = 1; i != args.length; i++) {
				builder = builder + args[i] + " ";

			}
			
			builder = builder.substring(0, builder.length()-1);
			
			switch(args[0].toLowerCase()) {
			case "header":
			case "name":
			case "footer":
				break;
			default:
				sender.sendMessage(CoreUtils.prefixes("hub") + CoreUtils.colorize("Valid arguments are: [header, name, footer]"));
				return true;
			}

			CoreUtils.playerList(args[0].toLowerCase(), builder);
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.setPlayerListHeader(CoreUtils.colorize(CoreUtils.playerList("header")));
				String phs = CoreUtils.playerList("name");
				if (phs.contains("%prefix%")) {
					phs = phs.replaceAll("%prefix%", CoreUtils.getPlayerPrefix(player));
				}
				if (phs.contains("%player%")) {
					phs = phs.replaceAll("%player%", player.getName());
				}
				

				player.setPlayerListName(CoreUtils.colorize(phs));

				player.setPlayerListFooter(CoreUtils.colorize(CoreUtils.playerList("footer")));
			}
			
			sender.sendMessage(CoreUtils.prefixes("hub") + CoreUtils.colorize("Set \"&7" + args[0].toLowerCase() + "&f\" to \"&7" + builder + "&f\"."));

		} else {

		}
		return true;
	}
}
