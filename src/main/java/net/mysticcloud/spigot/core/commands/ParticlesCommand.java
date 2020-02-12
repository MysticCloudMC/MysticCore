package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.commands.listeners.CommandTabCompleter;
import net.mysticcloud.spigot.core.utils.GUIManager;

public class ParticlesCommand implements CommandExecutor {

	public ParticlesCommand(Main plugin, String cmd){
		PluginCommand com = plugin.getCommand(cmd);
		com.setExecutor(this);
		com.setTabCompleter(new CommandTabCompleter());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player) {
			if(sender.hasPermission("mysticcloud.cmd.particles")){
				GUIManager.openInventory(((Player)sender),GUIManager.generateParticleFormatMenu((Player)sender), "Particle Format");
			} else {
				//No perms (shouldn't happen but handle it anyway incase you forget
			}
		} else {
			//Console
		}
		
		return true;
	}
}
