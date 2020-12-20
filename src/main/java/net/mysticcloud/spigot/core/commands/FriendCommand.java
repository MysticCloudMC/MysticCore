package net.mysticcloud.spigot.core.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class FriendCommand implements CommandExecutor {

	public FriendCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("friends")) {
			List<String> friendsl = CoreUtils.getMysticPlayer((Player)sender).getFriends();
			String friends = "";
			for(String s : friendsl) {
				friends = friends == "" ? s : CoreUtils.LookupForumID(s) + ", " + s;
			}
			sender.sendMessage(CoreUtils.prefixes("friends") + "Below is a list of your friends:");
			sender.sendMessage(CoreUtils.colorize("&f" + (friends == "" ? "&cYou don't have any registered friends. &fTo add a friend link your Minecraft account with your MysticCloud web account, and friend a user that has done the same!" : friends)));
		}
		if (cmd.getName().equalsIgnoreCase("friend")) {
			
			sender.sendMessage("" + CoreUtils.LookupForumID(args[0]));
			
//			sender.sendMessage("" +CoreUtils.getMysticPlayer((Player)sender).isFriends(args[0]));
		}
		return true;
	}
}
