package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.entities.TestChicken;
import net.mysticcloud.spigot.core.utils.entities.TestZombie;
import net.mysticcloud.spigot.core.utils.particles.formats.CircleFeetFormat;
import net.mysticcloud.spigot.core.utils.particles.formats.SelectorFormat;

public class DebugCommand implements CommandExecutor {

	public DebugCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender.hasPermission("mysticcloud.admin.cmd.debug")){
			sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("debug") + CoreUtils.toggleDebug()));
			if(sender instanceof Player)
				if(CoreUtils.debugOn()){
					TestZombie zom = new TestZombie(((CraftWorld) ((Player)sender).getWorld()).getHandle());
					zom.spawn(((Player)sender).getLocation());
					CoreUtils.entityparticles.put(zom.getBukkitEntity().getUniqueId(),new SelectorFormat());
				} else {
					TestChicken chi = new TestChicken(((CraftWorld) ((Player)sender).getWorld()).getHandle());
					chi.spawn(((Player)sender).getLocation());
					CoreUtils.entityparticles.put(chi.getBukkitEntity().getUniqueId(),new CircleFeetFormat());
			}
		}
		return true;
	}
}
