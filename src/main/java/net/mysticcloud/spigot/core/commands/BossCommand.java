package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.Bosses;
import net.mysticcloud.spigot.core.utils.entities.MysticEntityUtils;

public class BossCommand implements CommandExecutor {

	public BossCommand(Main plugin, String... cmds) {
		for (String cmd : cmds) {
			PluginCommand com = plugin.getCommand(cmd);
			com.setExecutor(this);
			com.setTabCompleter(new CommandTabCompleter());
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.boss")) {
			if (sender instanceof Player) {
				if (args.length == 1) {
					for (Bosses boss : Bosses.values()) {
						if (boss.getCallName().equalsIgnoreCase(args[0])) {
							MysticEntityUtils.spawnBoss(boss, ((Player) sender).getLocation());
							return true;
						}
					}
				}
			} else {
				sender.sendMessage(CoreUtils.prefixes("bosses") + "Player only command.");
			}
		}
		return true;
	}
}
