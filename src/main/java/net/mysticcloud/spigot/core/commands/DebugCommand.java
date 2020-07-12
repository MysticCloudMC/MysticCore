package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.MysticEntityUtils;
import net.mysticcloud.spigot.core.utils.vehicles.vehicle.TestCar;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.debug")){
			
			if(args.length == 0)
				sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("car")) {
					MysticEntityUtils.spawnBoss(new TestCar(((CraftWorld)((Player)sender).getWorld()).getHandle()), ((Player)sender).getLocation());
				}
			}
		}
		return true;
	}
}
