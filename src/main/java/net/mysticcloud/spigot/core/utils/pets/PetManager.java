package net.mysticcloud.spigot.core.utils.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.InventoryCreator;

public class PetManager {
	
	static Map<UUID, Pet> pets = new HashMap<>();
	
	public static void spawnPet(Pet pet, Location loc, Player owner) {
		if(pets.containsKey(owner.getUniqueId())) {
			pets.get(owner.getUniqueId()).getEntity().getBukkitEntity().remove();
		}
		pet.spawn(loc, owner.getName());
		pet.getEntity().getBukkitEntity().setMetadata("pet", new FixedMetadataValue(Main.getPlugin(), true));
		pets.put(owner.getUniqueId(), pet);
	}
	
	public static Inventory generatePetGUI(Player player) {
		
		InventoryCreator inv = new InventoryCreator("&3&lPet GUI", null, 27);

		inv.addItem(new ItemStack(Material.PUMPKIN), "&f&lSnowman Pet", 'A', new String[] {});
		inv.addItem(new ItemStack(Material.SADDLE), "&d&lBaby Pig Pet", 'B', new String[] {});
		inv.addItem(new ItemStack(Material.RED_MUSHROOM_BLOCK), "&c&lBaby Mooshroom Pet", 'C', new String[] {});

		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&eClick an option", 'X', (String[]) null);

		inv.setConfiguration(
				new char[] { 
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',
						'X', 'X', 'A', 'X', 'B', 'X', 'C', 'X', 'X',
						'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' });
		return inv.getInventory();
	}

	public static Pet getPet(UUID uid) {
		return pets.get(uid);
	}

}
