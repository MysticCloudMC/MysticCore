package net.mysticcloud.spigot.core.commands;

import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.teleport.TeleportUtils;

public class TeleportCommand implements CommandExecutor {

	public TeleportCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			plugin.getCommand(cmd).setExecutor(this);
		}

	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (args.length == 0)
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tp <user> <to-user>\""
						+ ((sender instanceof Player) ? " or \"/tp <to-user>\"" : ""));

			if (args.length == 1)
				if (sender.hasPermission("mysticcloud.cmd.tp"))
					if (sender instanceof Player)
						if (Bukkit.getPlayer(args[0]) != null)
							CoreUtils.teleportPlayer(((Player) sender), Bukkit.getPlayer(args[0]));
						else
							sender.sendMessage(CoreUtils.prefixes("teleport") + "Sorry that player isn't online.");

					else
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			if (args.length >= 2)
				if (args.length == 2) {
					if (sender.hasPermission("mysticcloud.admin.tpother")) {
						String name = sender instanceof Player ? ((Player) sender).getName() : "CONSOLE";
						if (Bukkit.getPlayer(args[1]) != null)
							if (args[0].contains(","))
								for (String pn : args[0].split(","))
									if (Bukkit.getPlayer(pn) != null)
										CoreUtils.teleportPlayer(name, Bukkit.getPlayer(pn), Bukkit.getPlayer(args[1]));
									else
										sender.sendMessage(CoreUtils.colorize(
												CoreUtils.prefixes("teleport") + "&7" + pn + "&f isn't online."));

							else if (Bukkit.getPlayer(args[0]) != null)
								CoreUtils.teleportPlayer(name, Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]));
							else
								sender.sendMessage(CoreUtils.colorize(
										CoreUtils.prefixes("teleport") + "&7" + args[0] + "&f isn't online."));

						else
							sender.sendMessage(CoreUtils
									.colorize(CoreUtils.prefixes("teleport") + "&7" + args[1] + "&f isn't online."));

					} else
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("teleport")
								+ "Sorry, you don't have permission to use that command."));
				} else {
					if(args.length == 3 || args.length == 5) {
						Location loc = ((Player)sender).getLocation().clone();
						if(args[0].contains("~")) 
							loc = loc.add(Double.parseDouble(args[0] == "~" ? "0" : args[0].replaceAll("~", "")), 0, 0);
						 else 
							loc.setX(Double.parseDouble(args[0]));
						
						if(args[1].contains("~")) 
							loc = loc.add(0, Double.parseDouble(args[1] == "~" ? "0" : args[1].replaceAll("~", "")), 0);
						 else 
							loc.setY(Double.parseDouble(args[1]));
						
						if(args[2].contains("~")) 
							loc = loc.add(0, 0, Double.parseDouble(args[2] == "~" ? "0" : args[2].replaceAll("~", "")));
						 else 
							loc.setZ(Double.parseDouble(args[2]));
						
						if(args.length == 5) {
							loc.setYaw(Float.parseFloat(args[3]));
							loc.setPitch(Float.parseFloat(args[4]));
						}
						
						TeleportUtils.teleportLocation(((Player)sender), loc);
						
					}
					
					if(args.length == 4 || args.length == 6) {
						if(Bukkit.getPlayer(args[0]) != null || args[0].contains("@p")) {
							Player pl = null;
							if(args[0].contains("@p")) {
								for(Entity e : ((BlockCommandSender)sender).getBlock().getWorld().getNearbyEntities(((BlockCommandSender)sender).getBlock().getLocation(), 50, 50, 50)) {
									if(e instanceof Player) {
										pl = (Player) e;
										break;
									}
								}
							} else {
								pl = Bukkit.getPlayer(args[0]);
							}
							Location loc = pl.getLocation().clone();
							if(args[1].contains("~")) 
								loc = loc.add(Double.parseDouble(args[1]), 0, 0);
							 else 
								loc.setX(Double.parseDouble(args[1]));
							
							if(args[2].contains("~")) 
								loc = loc.add(0, Double.parseDouble(args[2]), 0);
							 else 
								loc.setY(Double.parseDouble(args[2]));
							
							if(args[3].contains("~")) 
								loc = loc.add(0, 0, Double.parseDouble(args[3]));
							 else 
								loc.setZ(Double.parseDouble(args[3]));
							
							if(args.length == 6) {
								loc.setYaw(Float.parseFloat(args[4]));
								loc.setPitch(Float.parseFloat(args[5]));
							}
							
							TeleportUtils.teleportLocation(pl, loc);
							
							
						} else {
							sender.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");
						}
					}
				}

		}

		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpa")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tpa <player>\"");

					if (args.length >= 1)
						if (Bukkit.getPlayer(args[0]) != null)
							TeleportUtils.requestTeleport(((Player) sender), Bukkit.getPlayer(args[0]));
						else
							sender.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		if (cmd.getName().equalsIgnoreCase("tpaccept"))
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpaccept"))
					TeleportUtils.acceptTeleport(((Player) sender));
				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		if (cmd.getName().equalsIgnoreCase("tpdeny"))
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.cmd.tpdeny"))
					TeleportUtils.denyTeleport(((Player) sender));
				else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.admin.tphere")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Try \"/tphere <player>\"");

					if (args.length >= 1)
						if (args[0].contains(",")) {
							for (String pn : args[0].split(","))
								if (Bukkit.getPlayer(pn) != null)
									CoreUtils.teleportPlayer(Bukkit.getPlayer(pn), ((Player) sender));
								else
									sender.sendMessage(CoreUtils
											.colorize(CoreUtils.prefixes("teleport") + "&7" + pn + "&f isn't online."));
						}
					if (Bukkit.getPlayer(args[0]) != null)
						TeleportUtils.teleportPlayer(Bukkit.getPlayer(args[0]), ((Player) sender));
					else
						sender.sendMessage(CoreUtils.prefixes("teleport") + "That player isn't online.");

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		if (cmd.getName().equalsIgnoreCase("tpoff")) {
			if (sender instanceof Player)
				if (sender.hasPermission("mysticcloud.admin.tpoff")) {
					if (args.length == 0)
						sender.sendMessage(CoreUtils.prefixes("teleport") + "Teleport requests "
								+ CoreUtils.colorize(
										TeleportUtils.toggleTeleportRequests((Player) sender) ? "&cdisabled&f."
												: "&aenabled&f."));

				} else
					sender.sendMessage(
							CoreUtils.prefixes("teleport") + "Sorry, you don't have permission to use that command.");

			else
				sender.sendMessage(CoreUtils.prefixes("teleport") + "Player only command.");

		}

		return true;
	}
}
