package net.mysticcloud.spigot.core.utils.pets;

import java.util.List;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPig;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;

public enum PetType {
	
	SNOWMAN("&f&lSnowman",Material.PUMPKIN),
	BABY_PIG("&d&lBaby Pig", Material.PIG_SPAWN_EGG, new String[] {"&fRight click to ride!"});
	
	String name;
	Material gui;
	
	String[] desc = (String[]) null;
	
	PetType(String name, Material gui){
		this.name = name;
		this.gui = gui;
	}
	PetType(String name, Material gui, String[] desc){
		this.name = name;
		this.gui = gui;
		this.desc = CoreUtils.colorizeStringList(desc).toArray(new String[desc.length]);
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

	public String[] getDescription() {
		return desc;
	}

}
