package net.mysticcloud.spigot.core.commands.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import net.mysticcloud.spigot.core.utils.Holiday;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class AdminCommandTabCompleter implements TabCompleter {

	Map<String, List<String>> cmds = new HashMap<>();

	public AdminCommandTabCompleter() {
		List<String> debugSubCmds = new ArrayList<>();
		List<String> debugHolidaySubCmds = new ArrayList<>();
		debugSubCmds.add("holiday");
		debugSubCmds.add("remove");
		debugSubCmds.add("add");
		debugSubCmds.add("delete");
		debugSubCmds.add("pcolor");
		debugSubCmds.add("particles");
		debugSubCmds.add("time");

		for (Holiday h : Holiday.values()) {
			debugHolidaySubCmds.add(h.name());
		}

		cmds.put("debug", debugSubCmds);
		cmds.put("debug.holiday", debugHolidaySubCmds);

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		if (cmd.getName().equalsIgnoreCase("invsee") || cmd.getName().equalsIgnoreCase("sudo")) {
			if (args.length == 1)
				StringUtil.copyPartialMatches(args[0], getOnlinePlayers(), completions);
		}
		if (cmd.getName().equalsIgnoreCase("sudo")) {
			if (args.length == 2) {
				List<String> allCmds = new ArrayList<>();
				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					for (String s : plugin.getDescription().getCommands().keySet())
						allCmds.add("/" + s);
				}
//				for (String s : Bukkit.getPl) {
//					allCmds.add("/" + s);
//				}
				allCmds.add("-walk");
				allCmds.add("-punch");
				StringUtil.copyPartialMatches(args[1], allCmds, completions);

			}
		}
		if (cmd.getName().equalsIgnoreCase("debug")) {
			if (args.length == 1) {

				StringUtil.copyPartialMatches(args[0], cmds.get("debug"), completions);
			}
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("holiday"))
					StringUtil.copyPartialMatches(args[1], cmds.get("debug.holiday"), completions);
				if (args[0].equalsIgnoreCase("particles")) {
					if (args.length == 3) {
						List<String> tmp1 = new ArrayList<>();
						for (ParticleFormatEnum format : ParticleFormatEnum.values()) {
							tmp1.add(format.name());
						}
						StringUtil.copyPartialMatches(args[2], tmp1, completions);
					}
					if (args.length == 4) {
						List<String> tmp1 = new ArrayList<>();
						for (Particle p : Particle.values()) {
							tmp1.add(p.name());
						}
						StringUtil.copyPartialMatches(args[3], tmp1, completions);
					}
				}
			}
		}

		return completions;

	}

	public List<String> getOnlinePlayers() {
		List<String> players = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player.getName());
		}
		return players;
	}

}
