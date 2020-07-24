package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.MysticPlayer;

public class FriendCommand implements CommandExecutor {

	public FriendCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("friends")) {
			String friends = "";
			for (MysticPlayer player : CoreUtils.getMysticPlayer(((Player) sender)).getFriends()) {
				friends = friends == "" ? friends = CoreUtils.lookupUsername(player.getUUID())
						: friends + ", " + CoreUtils.lookupUsername(player.getUUID());
			}
			sender.sendMessage(CoreUtils.prefixes("friends") + "Below is a list of your friends:");
			sender.sendMessage(CoreUtils.colorize("&f" + (friends == "" ? "&cYou don't have any registered friends. &fTo add a friend link your Minecraft account with your MysticCloud web account, and friend a user that has done the same!" : friends)));
		}
		return true;
	}
}
