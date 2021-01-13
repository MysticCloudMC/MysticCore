package net.mysticcloud.spigot.core.commands.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.mysticcloud.spigot.core.utils.Holiday;
import net.mysticcloud.spigot.core.utils.entities.Bosses;
import net.mysticcloud.spigot.core.utils.warps.Warp;
import net.mysticcloud.spigot.core.utils.warps.WarpUtils;

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
		if (cmd.getName().equalsIgnoreCase("debug")) {
			if (args.length == 0) {

				StringUtil.copyPartialMatches(args[0], cmds.get("debug"), completions);
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("holiday"))
					StringUtil.copyPartialMatches(args[0], cmds.get("debug.holiday"), completions);
			}
		}

		return completions;

	}





}
