package net.mysticcloud.spigot.core.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.DebugUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
									Bukkit.getPlayer(uid)
											.sendMessage(CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
								} else {
									sender.sendMessage(
											CoreUtils.prefixes("debug") + "That player is already in the Debug Group");
								}
								return true;
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

							builder.append(s.substring(i, i + 1)).color(ChatColor.of(CoreUtils.generateColor(i, 0.5)));
						}
						sender.spigot().sendMessage(builder.create());
					}
					if (args[0].equalsIgnoreCase("time")) {
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.getTime()));

					}
				}
			}

		}
		return true;
	}
}
