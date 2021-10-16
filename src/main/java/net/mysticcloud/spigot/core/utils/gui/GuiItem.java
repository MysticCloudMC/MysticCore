package net.mysticcloud.spigot.core.utils.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json2.JSONArray;
import org.json2.JSONObject;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.placeholder.PlaceholderUtils;

public class GuiItem {
	String id;
	String display_name;
	Material mat = Material.GRASS_BLOCK;
	List<String> lore = null;
	boolean single_action = false;
	double buy = 0;
	double sell = 0;
	JSONObject action = new JSONObject();
	JSONArray actions = new JSONArray();
	boolean does_action = false;
	ItemStack storedItem = null;

	public GuiItem(String id) {
		this.id = id;
	}

	public void setDisplayName(String display_name) {
		this.display_name = display_name;
	}

	public void setMaterial(Material mat) {
		this.mat = mat;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void setSingleAction(boolean single_action) {
		this.single_action = single_action;
	}

	public boolean isSingleAction() {
		return single_action;
	}

	public void setBuyPrice(String string) {
		try {
			buy = Double.parseDouble(string);
		} catch (NumberFormatException ex) {
		}
	}

	public double getBuyPrice() {
		return buy;
	}

	public void setSellPrice(String string) {
		try {
			sell = Double.parseDouble(string);
		} catch (NumberFormatException ex) {
		}
	}

	public double getSellPrice() {
		return sell;
	}

	public void setSingleAction(JSONObject json) {
		does_action = true;
		this.action = json;
	}

	public void setActions(JSONArray actions) {
		does_action = true;
		this.actions = actions;
	}

	public JSONObject getAction() {
		return action;
	}

	public JSONArray getActions() {
		return actions;
	}

	public String getIdentifier() {
		return id;
	}

	public ItemStack getItem(Player player) {
		if (storedItem == null) {
			ItemStack item = new ItemStack(mat);
			ItemMeta meta = item.getItemMeta();
			if (lore != null) {
				List<String> tmp = new ArrayList<>();
				for (String a : lore) {
					tmp.add(PlaceholderUtils.replace(player, CoreUtils.colorize(a)));
				}
				meta.setLore(tmp);
			}
			meta.setDisplayName(PlaceholderUtils.replace(player, CoreUtils.colorize(display_name)));
			item.setItemMeta(meta);
			this.storedItem = item;
		}
		return this.storedItem.clone();
	}

	public boolean hasAction() {
		return does_action;
	}

}