package net.mysticcloud.spigot.core.utils.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.Vec3D;
import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.InventoryCreator;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;

public class PetManager {

	static Map<UUID, Pet> pets = new HashMap<>();

	public static void spawnPet(Pet pet, Location loc, Player owner) {
		if (pets.containsKey(owner.getUniqueId()) && pets.get(owner.getUniqueId()).getEntity().isAlive()) {
			pet.spawn(pets.get(owner.getUniqueId()).getEntity().getBukkitEntity().getLocation(), owner.getName());
			pets.get(owner.getUniqueId()).getEntity().getBukkitEntity().remove();
			pet.getEntity().getBukkitEntity().setMetadata("pet", new FixedMetadataValue(Main.getPlugin(), true));
			pets.put(owner.getUniqueId(), pet);
			return;
		}
		pet.spawn(loc, owner.getName());
		pet.getEntity().getBukkitEntity().setMetadata("pet", new FixedMetadataValue(Main.getPlugin(), true));
		pets.put(owner.getUniqueId(), pet);

	}

	public static void spawnPet(PetType type, Location loc, Player owner) {
		Pet pet = type.newPet(((CraftWorld) loc.getWorld()).getHandle());
		if (pet != null)
			spawnPet(pet, loc, owner);
	}

	public static Inventory generatePetGUI(Player player) {

		InventoryCreator inv = new InventoryCreator("&e&lPets", (null),
				(((PetType.values().length / 9) + 1) * 9) + 9 * 2);
		inv.addItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "&eComing Soon", 'O', (String[]) null);
		inv.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "&7Click an Option", 'X', (String[]) null);
		ArrayList<Character> c = new ArrayList<Character>();
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		for (int i = 0; i != (((int) (PetType.values().length / 9)) + 1) * 9; i++) {
			if (i < PetType.values().length) {
				if (player.hasPermission("mysticcloud.pet."
						+ PetType.values()[i].getStrippedName().toLowerCase().replaceAll(" ", "_"))) {
					inv.addItem(new ItemStack(PetType.values()[i].getGUIMaterial()), PetType.values()[i].getName(),
							(char) i, PetType.values()[i].getDescription(), false);
				} else {
					inv.addItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), PetType.values()[i].getName(), (char) i,
							new String[] { "&cLocked..." }, false);
				}
				c.add((char) i);
			} else {
				c.add('O');
			}

		}

		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');
		c.add('X');

		inv.setConfiguration(c);
//		c.clear();
//		c = null;

		return inv.getInventory();

	}

	public static Pet getPet(UUID uid) {
		return pets.get(uid);
	}

	public static void removePets(Player player) {
//		for(Pet pet : pets.get(player.getUniqueId())) {
//			pet.getEntity().getBukkitEntity().remove();
//		}
		pets.get(player.getUniqueId()).getEntity().getBukkitEntity().remove();
		pets.remove(player.getUniqueId());
	}

	public static class PetUtils {

		public static void flyingPetRidingTick(Pet pet) {
			if (!pet.getEntity().getPassengers().isEmpty()) {
				for (Entity e : pet.getEntity().passengers) {
					if (e instanceof EntityPlayer) {
						Vec3D vec = e.getLookDirection();
						pet.getEntity().yaw = e.yaw;
						pet.getEntity().setMot(new Vec3D(vec.x / pet.getSpeedMod(), vec.y / pet.getSpeedMod(),
								vec.z / pet.getSpeedMod()));
						break;
					}
				}
			}
		}

		public static void jumpingPetRidingTick(Pet pet) {
			if (!pet.getEntity().getPassengers().isEmpty()) {
				for (Entity e : pet.getEntity().passengers) {
					if (e instanceof EntityPlayer) {
//						((EntityPlayer)e).
						Vec3D vec = e.getLookDirection();
						pet.getEntity().yaw = e.yaw;
						pet.getEntity().setMot(new Vec3D(vec.x / pet.getSpeedMod(), pet.getEntity().getMot().getY(),
								vec.z / pet.getSpeedMod()));
						if (!pet.getEntity().getBukkitEntity().getLocation().add(vec.x, 0, vec.z).getBlock().getType()
								.equals(org.bukkit.Material.AIR) && pet.getEntity().isOnGround())
							pet.forceJump();
						break;
					}
				}
			}
		}

	}

}
