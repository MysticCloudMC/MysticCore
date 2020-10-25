package net.mysticcloud.spigot.core.utils.pets;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.World;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyMooshroom;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPanda;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPig;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyPolarBear;
import net.mysticcloud.spigot.core.utils.pets.pet.BabyRabbit;
import net.mysticcloud.spigot.core.utils.pets.pet.Parrot;
import net.mysticcloud.spigot.core.utils.pets.pet.Snowman;

public enum PetType {
	
	SNOWMAN("&f&lSnowman",Material.PUMPKIN, new String[] {"&a&lHoliday &c&lExclusive"}),
	BABY_PIG("&d&lBaby Pig", Material.CARROT, new String[] {"&fRight click to ride!"}),
	BABY_MOOSHROOM("&c&lBaby Mooshroom", Material.RED_MUSHROOM_BLOCK, new String[] {"&fRight click to ride!"}),
	PARROT("&2&lPet Parrot", Material.PARROT_SPAWN_EGG, new String[] {"&fRight click to ride!"}),
	/*BABY_SHEEP("&f&lBaby Sheep", Material.WHITE_WOOL),*/
	BABY_PANDA("&7&lBaby Panda",Material.PANDA_SPAWN_EGG, new String[] {"&fRight click to ride!"}),
	BABY_POLAR_BEAR("&f&lBaby Polar Bear",Material.SNOW_BLOCK),
	BABY_RABBIT("&6&lBaby Rabbit",Material.RABBIT_SPAWN_EGG);
	
	
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
		case BABY_PANDA: return new BabyPanda(world);
		case BABY_POLAR_BEAR: return new BabyPolarBear(world);
		case BABY_RABBIT: return new BabyRabbit(world);
		case PARROT: return new Parrot(world);
		//case BABY_SHEEP: return new BabySheep(world);
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
