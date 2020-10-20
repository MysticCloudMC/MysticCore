package net.mysticcloud.spigot.core.utils.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.InventoryCreator;

public class PetManager {
	
	static Map<UUID, Pet> pets = new HashMap<>();
	
	public static void spawnPet(Pet pet, Location loc, Player owner) {
		pet.spawn(loc, owner.getName());
		pets.put(owner.getUniqueId(), pet);
	}
	
	public static Inventory generatePetGUI(Player player) {
		
		InventoryCreator inv = new InventoryCreator("&3&lPet GUI", null, 27);

		inv.addItem(new ItemStack(Material.PUMPKIN), "&f&lSnowman Pet", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.SADDLE), "&5&lBaby Pig Pet", 'A', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'A', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

}
