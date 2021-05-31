package net.mysticcloud.spigot.core.utils.skulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SkullUtils {

	private static Map<String, CustomSkull> skulls = new HashMap<>();

	public static void start() {
		skulls.put("chicken", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2YzN2Q1MjRjM2VlZDE3MWNlMTQ5ODg3ZWExZGVlNGVkMzk5OTA0NzI3ZDUyMTg2NTY4OGVjZTNiYWM3NWUifX19=";
			}

			@Override
			public String getID() {
				return "[I;854160726,1555254121,-1159745984,75904377]";
			}
		});

		skulls.put("monitor", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg2ZjcyYzE2YjFlOWZlNmUwOTllNzZiNWY3YTg4NGZiNzgyY2ZjYzU4OGM5NWM0ZTM4M2RjNTI3ZDFiODQifX19=";
			}

			@Override
			public String getID() {
				return "[I;1519558527,443829541,-2045951745,1395314622]";
			}
		});
		skulls.put("discord", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3M2MxMmJmZmI1MjUxYTBiODhkNWFlNzVjNzI0N2NiMzlhNzVmZjFhODFjYmU0YzhhMzliMzExZGRlZGEifX19=";
			}

			@Override
			public String getID() {
				return "[I;-566027055,-1373812234,-1824937619,-1364409557]";
			}
		});
		skulls.put("chest", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19=";
			}

			@Override
			public String getID() {
				return "[I;866667617,641484425,-1507050624,1155661645]";
			}
		});
		skulls.put("iron_ore", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIyNjhjYzJmYjBhZjY3NWZmZjE3ZjE1YWY0YTZhYzI0MGJkYmY0MGE4Mzk2ZTAzNWJkMWE1YWViNGJjYzI3YyJ9fX0=";
			}

			@Override
			public String getID() {
				return "[I;59827843,1542733887,-1194148457,-780314585]";
			}
		});

		skulls.put("coal_ore", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU3OGQ4ZWI1ODgzYTExMDY2N2I1MWVlMjBhM2IyMWQ1MmQwODlhMGQ0YzkxYmUzY2I3ODZjMzE4MjhhMjA5NyJ9fX0=";
			}

			@Override
			public String getID() {
				return "[I;1445858061,-1370995685,-1785879255,1196244322]";
			}
		});

		skulls.put("gold_ore", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhlOTlkZGY5NTExZDM1YjU3OTA2N2Y2ZmI3OWEwYTJhNWI3N2ZjNGMyODk4YmJlZjU3MTJmOTg1M2NmYmQwNCJ9fX0=";
			}

			@Override
			public String getID() {
				return "[I;1123981930,-41334804,-2055628355,1453198438]";
			}
		});

		skulls.put("diamond_ore", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmIxYzI2OGVmZWM4ZDdkODhhMWNiODhjMmJmYTA5N2ZhNTcwMzc5NDIyOTlmN2QyMDIxNTlmYzkzY2QzMDM2ZCJ9fX0=";
			}

			@Override
			public String getID() {
				return "[I;735751021,887703109,-1542100497,-2004956133]";
			}
		});
	}

	public static ItemStack getSkull(String skull) {
		ItemStack s;
		if (skulls.containsKey(skull))
			s = CoreUtils.getHead(skulls.get(skull).getID(), skulls.get(skull).getValue());
		else {
			if (CoreUtils.LookupUUID(skull) == null)
				return new ItemStack(Material.SKELETON_SKULL);
			s = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) s.getItemMeta();
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(CoreUtils.LookupUUID(skull)));
			s.setItemMeta(meta);
		}
		return s;
	}

	public static Map<String, CustomSkull> getSkulls() {
		return skulls;
	}

	public static List<String> getSkullNames() {
		List<String> skls = new ArrayList<>();
		for (String s : skulls.keySet()) {
			skls.add(s);
		}
		return skls;
	}

}
