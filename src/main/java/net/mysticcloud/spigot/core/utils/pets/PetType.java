package net.mysticcloud.spigot.core.utils.pets;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyMooshroom;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPig;
import net.mysticcloud.spigot.core.utils.pets.pet.Bat;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;

public enum PetType {
	
	SNOWMAN("&f&lSnowman",Material.PUMPKIN, new String[] {"&a&lHoliday &c&lExclusive"}),
	BABY_PIG("&d&lBaby Pig", Material.PIG_SPAWN_EGG, new String[] {"&fRight click to ride!"}),
	BABY_MOOSHROOM("&d&lBaby Mooshroom", Material.RED_MUSHROOM_BLOCK, new String[] {"&fRight click to ride!"}),
	BAT("&6Bat", Material.BAT_SPAWN_EGG);
	
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
		case BABY_MOOSHROOM: return new BabyMooshroom(world);
		case BAT: return new Bat(world);
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
