package net.mysticcloud.spigot.core.commands.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.mysticcloud.spigot.core.kits.Kit;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.CustomTag;
import net.mysticcloud.spigot.core.utils.entities.Bosses;
import net.mysticcloud.spigot.core.utils.warps.Warp;
import net.mysticcloud.spigot.core.utils.warps.WarpUtils;

public class CommandTabCompleter implements TabCompleter {

	private List<String> tags = new ArrayList<>();
	private List<String> bosses = new ArrayList<>();

	public CommandTabCompleter() {

		for (CustomTag tag : CustomTag.values()) {
			tags.add(tag.name());
		}
		tags.add("remove");

		for (Bosses boss : Bosses.values()) {
			bosses.add(boss.getCallName());
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		if (cmd.getName().equalsIgnoreCase("tags")) {
			if (sender instanceof Player) {
				if (args.length == 1) {
					StringUtil.copyPartialMatches(args[0], getOnlinePlayers(), completions);
				}
				if (args.length == 2) {
					StringUtil.copyPartialMatches(args[1], tags, completions);
				}
			} else {
				if (args.length == 1) {
					StringUtil.copyPartialMatches(args[0], getOnlinePlayers(), completions);
				}
				if (args.length == 2) {
					StringUtil.copyPartialMatches(args[1], tags, completions);
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("boss")) {
			if (args.length == 1) {
				StringUtil.copyPartialMatches(args[0], getBosses(), completions);
			}
		}
		if (cmd.getName().equalsIgnoreCase("warp")) {
			if (args.length == 1) {
				StringUtil.copyPartialMatches(args[0], getWarps(), completions);
			}
			if (args.length == 2) {
				StringUtil.copyPartialMatches(args[1], getWarps(args[0]), completions);
			}
		}

		if (label.toUpperCase().contains("SPAWN")) {

			if (args.length == 1) {
				StringUtil.copyPartialMatches(args[0], getOnlinePlayers(), completions);
			}

		}

		if (label.toUpperCase().contains("ITEM")) {
			if (args.length == 1) {
				StringUtil.copyPartialMatches(args[0], CoreUtils.getCachedItems().keySet(), completions);
			}
			if (args.length == 2) {
				StringUtil.copyPartialMatches(args[1],
						Arrays.asList(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }), completions);
			}
			if (args.length == 3) {
				List<String> players = new ArrayList<>();
				for (Player player : Bukkit.getOnlinePlayers()) {
					players.add(player.getName());
				}
				StringUtil.copyPartialMatches(args[2], players, completions);
			}
		}

		if (label.toUpperCase().contains("KIT")) {
			if (args.length == 1) {
				List<String> kits = new ArrayList<>();
				for (Kit kit : KitManager.getKits()) {
					if (sender.hasPermission("mysticcloud.kit." + kit.getName()))
						kits.add(kit.getName());
				}
				StringUtil.copyPartialMatches(args[0], kits, completions);
			}
			if (args.length == 2) {

				StringUtil.copyPartialMatches(args[1], getOnlinePlayers(), completions);
			}
		}

		return completions;

	}

	private List<String> getWarps() {
		List<String> warps = new ArrayList<>();
		for (Warp warp : WarpUtils.getWarps("warp")) {
			warps.add(warp.name());
		}
		return warps;
	}

	private List<String> getWarps(String type) {
		List<String> warps = new ArrayList<>();
		for (Warp warp : WarpUtils.getWarps(type)) {
			warps.add(warp.name());
		}
		return warps;
	}

	public List<String> getOnlinePlayers() {
		List<String> players = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player.getName());
		}
		return players;
	}

	public List<String> getBosses() {

		return bosses;
	}

}
