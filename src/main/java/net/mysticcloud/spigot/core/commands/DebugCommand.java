package net.mysticcloud.spigot.core.commands;

import java.awt.Color;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd) {
		plugin.getCommand(cmd).setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mysticcloud.admin.cmd.debug")) {

			if (args.length == 0)
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("color")) {
					
					
//					builder.color(net.md_5.bungee.api.ChatColor.of(new Color(arg0)));
					TextComponent cmp = new TextComponent("");
					
					String s = "Hello World! Aren't rainbows cool?";
					ComponentBuilder builder = new ComponentBuilder();
					for(int i=0;i!=s.length();i++) {
						
						builder.append(s.substring(i,i+1)).color(ChatColor.of(CoreUtils.generateColor(i, 0.5)));
					}
//					for(BaseComponent bc : builder.create())
//						cmp.addExtra(bc);
					sender.spigot().sendMessage(builder.create());
				}
				if (args[0].equalsIgnoreCase("time")) {
					sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.getTime()));

				}
			}
		}
		return true;
	}
}
