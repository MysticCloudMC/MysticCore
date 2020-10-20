package net.mysticcloud.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.pets.Pet;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;

public class PetCommand implements CommandExecutor {

	public PetCommand(Main plugin, String cmd){
		plugin.getCommand(cmd).setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Pet snowman = new Snowman(((CraftWorld)((Player)sender).getWorld()).getHandle());
			snowman.spawn(((Player)sender).getLocation());
////			PetManager.spawnPet(((Player)sender), ((Player)sender).getLocation());
////			Minion minion = (Minion) CoreUtils.spawnEntity(new Minion(((CraftWorld)((Player)sender).getWorld()).getHandle()), ((Player)sender).getLocation());
////			minion.setOwner(((Player)sender).getName());
//			GUIManager.openInventory(((Player)sender), PetManager.getGUI(((Player)sender)), "Pets");
////			PetManager.spawnPet(((Player) sender), "demo", ((Player)sender).getLocation());
		}
		return false;
	}
}
