package net.mysticcloud.spigot.core.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.MysticPlayer;
import net.mysticcloud.spigot.core.utils.UID;

public class RegisterCommand implements CommandExecutor {

	public RegisterCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CoreUtils.connected()) {
			sender.sendMessage(CoreUtils.colorize("&eSQL&7 >&f SQL is disabled."));
			return true;
		}
		if (sender instanceof Player) {
			if (args.length == 0) {
				sender.sendMessage(CoreUtils.colorize("&e/register <discord|forums(wip)>"));
				return true;
			}
			if (args[0].equalsIgnoreCase("forums")) {
				if (args.length == 1) {
					sender.sendMessage(
							CoreUtils.colorize("&e/register forums <email you used to create your forums account>"));
					return true;
				}
				ResultSet rs = CoreUtils.getForumsDatabase().query("SELECT * FROM xf_user;");

				String id = "";
				try {
					while (rs.next()) {
						if (rs.getString("email").equalsIgnoreCase(args[1])) {
							id = rs.getString("user_id");
							break;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!id.equals("")) {
					CoreUtils.sendUpdate("UPDATE MysticPlayers SET FORUMS_NAME='" + id + "' WHERE UUID='"
							+ ((Player) sender).getUniqueId() + "';");
					sender.sendMessage(
							CoreUtils.colorize("&aLinked your Mystic Account with your Forums account!"));
				} else {
					sender.sendMessage(
							CoreUtils.colorize("&cUnable to find your forums account using that email address."));
				}
			}
			if (args[0].equalsIgnoreCase("discord")) {
				String key = "dc-" + new UID(5);
				ResultSet rs = CoreUtils.sendQuery(
						"SELECT UUID,DISCORD_ID,DISCORD_KEY FROM MysticPlayers WHERE DISCORD_KEY IS NOT NULL;");
				try {
					while (rs.next()) {
						if (UUID.fromString(rs.getString("UUID")).equals(((Player) sender).getUniqueId())) {
							if (rs.getString("DISCORD_ID") == null) {
								sender.sendMessage(CoreUtils.colorize(
										"&eYou've already started the registering process. &aHead to the MysticCloud discord, and type '>linktominecraft &2"
												+ rs.getString("DISCORD_KEY") + "&a' in the #bot-commands channel."));
							} else {
								sender.sendMessage(CoreUtils.colorize("&cYou've already registered on Discord."));
							}
							return true;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CoreUtils.sendUpdate("UPDATE MysticPlayers SET DISCORD_KEY='" + key + "' WHERE UUID='"
						+ ((Player) sender).getUniqueId() + "';");
				sender.sendMessage(CoreUtils.colorize("&aHead to the MysticCloud discord, and type '>linktominecraft &2"
						+ key + "&a' in the #bot-commands channel."));
			}

		} else {
			sender.sendMessage(CoreUtils.colorize("&c/register <web username>"));
		}
		return true;
	}
}
