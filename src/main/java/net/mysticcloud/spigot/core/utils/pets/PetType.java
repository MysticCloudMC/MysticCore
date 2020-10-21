package net.mysticcloud.spigot.core.utils.pets;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPig;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;

public enum PetType {
	
	SNOWMAN("&f&lSnowman",Material.PUMPKIN),
	BABY_PIG("&d&lBaby Pig", Material.PIG_SPAWN_EGG);
	
	String name;
	Material gui;
	
	PetType(String name, Material gui){
		this.name = name;
		this.gui = gui;
	}

	public Pet newPet(World world) {
		switch(this) {
		case SNOWMAN: return new Snowman(world);
		case BABY_PIG: return new BabyPig(world);
		default: return null;
		}
	}

	public Material getGUIMaterial() {
		return gui;
	}
	
	public String getName() {
		return name;
	}

	public String getStrippedName() {
		return ChatColor.stripColor(name);
	}

}
