package net.mysticcloud.spigot.core.utils.pets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;


public class PetType {
	
	String name = "";
	boolean fly = false;
	ItemStack idle = new ItemStack(Material.AIR);
	ItemStack moving = new ItemStack(Material.AIR);
	String displayName = "";
	
	public PetType(String name){
		this.name = name;
		displayName = name;
		
	}
	public void setCanFly(boolean fly){
		this.fly = fly;
	}

	public void setIdleItem(ItemStack i) {
		this.idle = i;
	}
	public void setMovingItem(ItemStack i) {
		this.moving = i;
	}
	public void setItems(ItemStack idle, ItemStack moving){
		this.idle = idle;
		this.moving = moving;
	}

	public String getName() {
		return name;
	}
	public String getDisplayName() {
		return CoreUtils.colorize(displayName);
	}
	public ItemStack getIdleItem() {
		return idle;
	}
	public ItemStack getMovingItem() {
		return moving;
	}

	public boolean canFly() {
		return fly;
	}
	public void setDisplayName(String name) {
		this.displayName = name;
	}

}
