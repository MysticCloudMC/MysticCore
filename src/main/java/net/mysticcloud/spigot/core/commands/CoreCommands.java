package net.mysticcloud.spigot.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.AdminCommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.PageResult;
import net.mysticcloud.spigot.core.utils.SpawnReason;
import net.mysticcloud.spigot.core.utils.afk.AFKUtils;
import net.mysticcloud.spigot.core.utils.pets.PetManager;

public class CoreCommands implements CommandExecutor {

	List<String> rules = new ArrayList<>();

	public CoreCommands(Main plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
		rules.add("Staff are trained, if they tell you something against the rules is allowed, go with what they say.");
		rules.add("Don't use any hacks or game exploits to give you and advantage over other players.");
		rules.add("Don't curse in public chat.");
		rules.add("Use English in public chat.");
		rules.add("Don't try and evade bans with alts.");
		rules.add("Don't disrespect staff.");
		rules.add("Be kind to others.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("rules")) {
			// Don't forget to add these commands to the list

			int page;
			try {
				page = args.length == 0 ? 1 : Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(CoreUtils.prefixes("admin") + args[0] + " isn't an integer.");
				return true;
			}

			int pages = ((int) Math.ceil(rules.size() / 3)) + 1;
			if (page > pages)
				page = pages;
			String top = CoreUtils.colorize(
					"&3-------------------&f[Page &7" + page + "&f of &7" + pages + "&f]&3-------------------");
			String bottom = "";
			for (int i = 0; i != ChatColor.stripColor(top).length(); i++) {
				bottom = bottom + "-";
			}
			bottom = CoreUtils.colorize("&3" + bottom);

			sender.sendMessage(top);
			sender.sendMessage("");

			for (String s : CoreUtils.getPageResults(rules, page, 3)) {
				sender.sendMessage(CoreUtils.colorize("&3 - &f" + s));
			}
			sender.sendMessage("");
			sender.sendMessage(bottom);

		}
		if (cmd.getName().equalsIgnoreCase("plugins"))

		{

		}
		if (cmd.getName().equalsIgnoreCase("clear")) {
			if (sender.hasPermission("mysticcloud.admin.cmd.clear")) {

				if (args.length == 0) {
					if (sender instanceof Player) {

						((Player) sender)
								.sendMessage(CoreUtils.prefixes("root") + ((Player) sender).getInventory().getSize()
										+ " items have been removed from your inventory.");
						((Player) sender).getInventory().clear();
						((Player) sender).updateInventory();
					} else {
						sender.sendMessage("Player only command silly. Try /clear <player>");
					}
				}
				if (args.length >= 1) {
					if (Bukkit.getPlayer(args[0]) != null) {
						if (sender instanceof Player) {
							sender.sendMessage(CoreUtils.prefixes("root") + "You have cleared "
									+ Bukkit.getPlayer(args[0]).getInventory().getSize() + " items from " + args[0]
									+ "'s inventory.");
						}
						Bukkit.getPlayer(args[0]).getInventory().clear();
						Bukkit.getPlayer(args[0]).updateInventory();
					} else {
						sender.sendMessage("Player not online.");
					}
				}

			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("afk")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1 && sender.hasPermission("mysticcloud.admin.afkother")) {
					if (Bukkit.getPlayer(args[0]) != null) {
						player = Bukkit.getPlayer(args[0]);
					} else {
						sender.sendMessage(CoreUtils.prefixes("afk") + "That player isn't online.");
					}
				}
				AFKUtils.afk(player, !AFKUtils.isAFK(player));
			} else {
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) != null) {
						AFKUtils.afk(Bukkit.getPlayer(args[0]), !AFKUtils.isAFK(Bukkit.getPlayer(args[0])));
					} else {
						sender.sendMessage(CoreUtils.prefixes("afk") + "That player isn't online.");
					}
				} else {
					sender.sendMessage(CoreUtils.prefixes("afk") + "Try /afk <player>");
				}

			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pets")) {
			if (sender instanceof Player) {
				GUIManager.openInventory(((Player) sender), PetManager.generatePetGUI(((Player) sender)), "Pets");
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (CoreUtils.getSpawnLocation() == null) {
				sender.sendMessage(
						CoreUtils.colorize(CoreUtils.fullPrefix + "The spawn point is not yet set. (/setspawn)"));
				return true;
			}
			if (args.length == 0) {
				if (sender instanceof Player) {
					CoreUtils.teleportToSpawn(((Player) sender));
				} else {
					sender.sendMessage(CoreUtils.prefixes("admin") + "Usage: /spawn <player>");
				}
			}
			if (args.length == 1) {
				if (sender.hasPermission("mysticcloud.spawn.other")) {
					if (Bukkit.getPlayer(args[0]) == null) {
						sender.sendMessage(CoreUtils.prefixes("admin") + "That player is not currently online.");
					} else {
						CoreUtils.teleportToSpawn(Bukkit.getPlayer(args[0]), SpawnReason.OTHER);
						sender.sendMessage(CoreUtils.prefixes("admin")
								+ CoreUtils.colorize("Teleported &7" + args[0] + "&f to spawn."));
					}
				} else {
					sender.sendMessage(
							CoreUtils.prefixes("admin") + CoreUtils.colorize("That is an admin only command."));

				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("settings")) {
			if (sender instanceof Player) {
				Player player = ((Player) sender);
				if (args.length == 0) {

					GUIManager.openInventory(player, GUIManager.getSettingsMenu(player), "Settings Menu");
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("particles")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mysticcloud.cmd.particles")) {
					GUIManager.openInventory(((Player) sender), GUIManager.generateParticleFormatMenu((Player) sender),
							"Particle Format");
				} else {
					sender.sendMessage(CoreUtils.getCoreMessage("noperm"));
				}
			} else {
				sender.sendMessage(CoreUtils.getCoreMessage("playeronly"));
			}

			return true;
		}
		return true;
	}
}
