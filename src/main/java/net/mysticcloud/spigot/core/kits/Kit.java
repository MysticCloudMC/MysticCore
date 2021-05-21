package net.mysticcloud.spigot.core.kits;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class Kit {

	List<ItemStack> items = new ArrayList<>();
	long cooldown = 0;
	String name;
	List<String> desc = new ArrayList<>();
	ItemStack guiitem = new ItemStack(Material.DIAMOND_SWORD);
	String display = "";
	Map<UUID, Long> cooldowns = new HashMap<>();

	public Kit(String name) {
		this.name = name;
		display = name;
	}

	public void setDisplayName(String displayName) {
		this.display = CoreUtils.colorize(displayName);
	}

	public void setGUIItem(ItemStack i) {
		guiitem = i;
	}

	public void setDescription(String desc) {
		if (desc.contains("%n")) {
			for (String s : desc.split("%n")) {
				this.desc.add(CoreUtils.colorize("&f" + s));
			}
		} else {
			this.desc.add(CoreUtils.colorize(desc));
		}

	}

	public String getName() {
		return name;
	}

	public void addItem(ItemStack item) {
		items.add(item);
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public long getCooldown() {
		return cooldown;
	}

	public long getStartingTime(Player player) {
		return cooldowns.containsKey(player.getUniqueId()) ? cooldowns.get(player.getUniqueId()) : 0;
	}

	public boolean checkCooldown(Player player) {
		if (cooldowns.containsKey(player.getUniqueId())) {
			if (new Date().getTime() - (cooldowns.get(player.getUniqueId())) >= cooldown) {
				cooldowns.remove(player.getUniqueId());
				return false;
			}
			return true;
		}
		return false;
	}

	public void addCooldown(Player player) {
		cooldowns.put(player.getUniqueId(), new Date().getTime());
	}

	public Map<UUID, Long> getCooldowns() {
		return cooldowns;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public ItemStack getItem() {

		return guiitem;
	}

	public List<String> getDescription() {
		return desc;
	}

	public String getDisplayName() {
		return display;
	}

}
