package net.mysticcloud.spigot.core.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;
import net.mysticcloud.spigot.core.utils.accounts.friends.FriendUtils;

public class FriendCommand implements CommandExecutor {

	public FriendCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("friends")) {
				List<String> friendsl = MysticAccountManager.getMysticPlayer((Player) sender).getFriends();
				String friends = "";
				for (String s : friendsl) {
					friends = friends == "" ? s : friends + ", " + s;
				}
				sender.sendMessage(CoreUtils.prefixes("friends") + "Below is a list of your friends:");
				sender.sendMessage(CoreUtils.colorize("&f" + (friends == ""
						? "&cYou don't have any registered friends. &fTo add a friend link your Minecraft account with your MysticCloud web account, and friend a user that has done the same!"
						: friends)));
			}
			if (cmd.getName().equalsIgnoreCase("friend")) {
				if (args.length == 0) {

					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length != 2) {
						sender.sendMessage(CoreUtils.colorize("friends") + "Try \"/friend add <username>\".");
						return true;
					}
					switch (FriendUtils.addFriend(((Player) sender).getUniqueId(),
							Bukkit.getPlayer(args[1]) == null ? CoreUtils.LookupUUID(args[1])
									: Bukkit.getPlayer(args[1]).getUniqueId())
							.getStatus()) {
					case FRIEND_NO_FORUMS:
						sender.sendMessage(CoreUtils.prefixes("friends")
								+ "Sorry, that player doesn't seem to have a forums account.");
						break;
					case FRIENDS:
						sender.sendMessage(CoreUtils.prefixes("friends") + "Already friends.");
						break;
					case FRIENDS_NEW:
						sender.sendMessage(CoreUtils.prefixes("friends") + "You are now friends!");
						break;
					case NOT_FRIENDS:
						sender.sendMessage(CoreUtils.prefixes("friends") + "You are not friends with that player.");
						break;
					case PLAYER_NO_FORUMS:
						sender.sendMessage(
								CoreUtils.prefixes("friends") + "Sorry, you don't seem to have a forums account.");
						break;
					case REQUEST_SENT:
						sender.sendMessage(CoreUtils.prefixes("friends") + "Friend request sent to " + args[0] + ".");
						break;
					case NONE:
					default:
						sender.sendMessage(CoreUtils.prefixes("friends")
								+ "There was an error proccessing your request. Please try again later. The the problem continues, please contact a staff member.");
						break;
					}

					return true;
				}
				if (CoreUtils.LookupUUID(args[0]) != null) {
					if (MysticAccountManager.getMysticPlayer((Player) sender).isFriends(args[0])) {
						sender.sendMessage(CoreUtils.prefixes("friends") + CoreUtils
								.colorize("http://www.mysticcloud.net/stats/?u=" + CoreUtils.LookupUUID(args[0])));
					} else {
						sender.sendMessage(CoreUtils.prefixes("friends") + CoreUtils.colorize(
								"You are not friends with that player. Follow someone on your forum account, and if you both have linked your Mystic Account and Forum accounts (/register) you will be their friend."));
					}
				} else {
					sender.sendMessage(
							CoreUtils.prefixes("friends") + CoreUtils.colorize("We couldn't find that player."));
				}

//				sender.sendMessage("" + CoreUtils.LookupForumID(args[0]));
//
//				sender.sendMessage("" + CoreUtils.getMysticPlayer((Player) sender).isFriends(args[0]));
			}

		} else {
			sender.sendMessage("Players only. ;)");
		}
		return true;
	}
}
