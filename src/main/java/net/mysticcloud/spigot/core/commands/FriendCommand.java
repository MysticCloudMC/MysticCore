package net.mysticcloud.spigot.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.accounts.MysticAccountManager;

public class FriendCommand implements CommandExecutor {

	public FriendCommand(Main plugin, String... cmds) {
		for (String cmd : cmds)
			plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("friends")) {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("friends")
						+ "Add friends on the forums. If you both have your MC and Forums accounts linked, their name will apear here."));
				String s = "";
				for (UUID uid : MysticAccountManager.getMysticPlayer(((Player) sender).getUniqueId()).getFriends()) {
					String a = Bukkit.getPlayer(uid) == null ? CoreUtils.lookupUsername(uid)
							: Bukkit.getPlayer(uid).getName();
					s = s == "" ? a : s + ", " + a;
				}
				s = s + ".";
				sender.sendMessage(s);
			}
		}
		return true;
	}
}
