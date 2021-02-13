package net.mysticcloud.spigot.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.AdminCommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.CustomTag;
import net.mysticcloud.spigot.core.utils.DebugUtils;
import net.mysticcloud.spigot.core.utils.Emoticons;
import net.mysticcloud.spigot.core.utils.Holiday;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(Main plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("control")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(CoreUtils.prefixes("error") + "Player only command.");
				return false;
			}

			if (sender.hasPermission("mysticcloud.admin.control")) {
				sender.sendMessage(CoreUtils.prefixes("error") + "No permission.");
				return false;
			}
			if (args.length == 1) {
				sender.sendMessage(CoreUtils.prefixes("error") + "Usage: /control <player>");
				return false;
			}
			if (Bukkit.getPlayer(args[0]) != null) {
				sender.sendMessage(
						CoreUtils.prefixes("error") + "Could not find that player. Player must be online to control.");
				return false;
			}

			CoreUtils.control((Player) sender, Bukkit.getPlayer(args[0]));

		}

		if (cmd.getName().equalsIgnoreCase("invsee")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("mysticcloud.admin.cmd.invsee")) {
					if (args.length == 1) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player other = Bukkit.getPlayer(args[0]);
							Inventory inv = other.getInventory();
							player.openInventory(inv);
						} else {
							player.sendMessage(CoreUtils.prefixes("admin") + "That player isn't online.");
						}
					} else {
						player.sendMessage(CoreUtils.prefixes("admin") + "You must specify a player");
					}
				} else {
					player.sendMessage(CoreUtils.prefixes("admin") + "You don't have permission to use that command.");
				}

			} else {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Player only command.");
			}
		}

		if (cmd.getName().equalsIgnoreCase("debug")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("mysticcloud.admin.cmd.debug")) {

					if (args.length == 0) {
						if (DebugUtils.isDebugger(((Player) sender).getUniqueId())) {
							sender.sendMessage(CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
							DebugUtils.removeDebugger(((Player) sender).getUniqueId());
							return true;
						}
						sender.sendMessage(CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
						DebugUtils.addDebugger(((Player) sender).getUniqueId());
						return true;

					}

					if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("emoticons")) {
							if (args.length == 1) {
								for (Emoticons emote : Emoticons.values()) {
									sender.sendMessage(emote.name() + ": " + emote);
								}
							}
							if (args.length == 2) {
								for (Emoticons emote : Emoticons.values()) {
									if (emote.name().contains(args[1].toUpperCase()))
										sender.sendMessage(emote.name() + ": " + emote);
								}
							}
						}
						if (args[0].equalsIgnoreCase("hide")) {
							if (args.length == 2) {
								if (Bukkit.getPlayer(args[1]) != null)
									((Player) sender).hidePlayer(Bukkit.getPlayer(args[1]));
								else
									sender.sendMessage(CoreUtils.prefixes("error") + "That player isn't online.");
							}
							if (args.length == 3) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[1] + " isn't online.");
									return true;
								}
								if (Bukkit.getPlayer(args[2]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[2] + " isn't online.");
									return true;
								}
								Bukkit.getPlayer(args[1]).hidePlayer(Bukkit.getPlayer(args[2]));
							}
						}
						if (args[0].equalsIgnoreCase("show")) {
							if (args.length == 2) {
								if (Bukkit.getPlayer(args[1]) != null)
									((Player) sender).showPlayer(Bukkit.getPlayer(args[1]));
								else
									sender.sendMessage(CoreUtils.prefixes("error") + "That player isn't online.");
							}
							if (args.length == 3) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[1] + " isn't online.");
									return true;
								}
								if (Bukkit.getPlayer(args[2]) == null) {
									sender.sendMessage(CoreUtils.prefixes("error") + args[2] + " isn't online.");
									return true;
								}
								Bukkit.getPlayer(args[1]).showPlayer(Bukkit.getPlayer(args[2]));
							}
						}
						if (args.length == 2) {
							if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")
									|| args[0].equalsIgnoreCase("add")) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("debug") + "That player is not online.");
									return true;
								}
								if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (DebugUtils.isDebugger(uid)) {
										DebugUtils.removeDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
									} else {
										sender.sendMessage(
												CoreUtils.prefixes("debug") + "That player isn't in the Debug Group");
									}
									return true;
								}
								if (args[0].equalsIgnoreCase("add")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (!DebugUtils.isDebugger(uid)) {
										DebugUtils.addDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
									} else {
										sender.sendMessage(CoreUtils.prefixes("debug")
												+ "That player is already in the Debug Group");
									}
									return true;
								}

							}
						}

						if (args[0].equalsIgnoreCase("reload")) {
							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("tags")) {
									if (sender instanceof Player)
										DebugUtils.addDebugger(((Player) sender).getUniqueId());
									CustomTag.reloadTags();
								}
							}
						}

						if (args[0].equalsIgnoreCase("holiday")) {
							if (args.length == 1) {
								DebugUtils.holidayCheck(!DebugUtils.holidayCheck());
								sender.sendMessage(CoreUtils.prefixes("debug") + "Holiday Check is set to " + CoreUtils
										.colorize((DebugUtils.holidayCheck() ? "&a&ltrue" : "&c&lfalse") + "&f."));
							}
							if (args.length == 2) {
								CoreUtils.setHoliday(Holiday.valueOf(args[1]));
								if (DebugUtils.holidayCheck()) {
									DebugUtils.holidayCheck(false);
									sender.sendMessage(CoreUtils.prefixes("debug") + "Holiday Check is set to "
											+ CoreUtils.colorize("&c&lfalse&f."));
								}
							}
						}

						if (args[0].equalsIgnoreCase("pcolor")) {
							if (args.length == 3) {
								CoreUtils.particles(((Player) sender).getUniqueId())
										.setDustOptions(new DustOptions(
												org.bukkit.Color.fromRGB(Integer.parseInt(args[1].split(",")[0]),
														Integer.parseInt(args[1].split(",")[1]),
														Integer.parseInt(args[1].split(",")[2])),
												Float.parseFloat(args[2])));
							}
						}

						if (args[0].equalsIgnoreCase("particles")) {
							if (args.length == 3) {
								CoreUtils.particles.put(((Player) sender).getUniqueId(),
										ParticleFormatEnum.valueOf(args[1].toUpperCase()).formatter());
								CoreUtils.particles(((Player) sender).getUniqueId())
										.particle(Particle.valueOf(args[2].toUpperCase()));
							}
						}

						if (args[0].equalsIgnoreCase("color")) {

							String s = "Hello World! Aren't rainbows cool?";
							ComponentBuilder builder = new ComponentBuilder();
							for (int i = 0; i != s.length(); i++) {

								builder.append(s.substring(i, i + 1))
										.color(ChatColor.of(CoreUtils.generateColor(i, 0.5)));
							}
							sender.spigot().sendMessage(builder.create());
						}
						if (args[0].equalsIgnoreCase("time")) {
							sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.getMonth()
									+ "/" + CoreUtils.getDay() + " " + CoreUtils.getTime() + " "
									+ (CoreUtils.getHoliday().equals(Holiday.NONE) ? ""
											: CoreUtils.getHoliday().getName())));

						}
					}
				}

			}
			return true;
		}
		return true;
	}
}
