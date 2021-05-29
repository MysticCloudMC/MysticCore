package net.mysticcloud.spigot.core.utils.skulls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class SkullUtils {

	private static Map<String, CustomSkull> skulls = new HashMap<>();

	public static void start() {
		skulls.put("chicken", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2YzN2Q1MjRjM2VlZDE3MWNlMTQ5ODg3ZWExZGVlNGVkMzk5OTA0NzI3ZDUyMTg2NTY4OGVjZTNiYWM3NWUifX19";
			}

			@Override
			public String getID() {
				return "[I;854160726,1555254121,-1159745984,75904377]";
			}
		});

		skulls.put("monitor", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg2ZjcyYzE2YjFlOWZlNmUwOTllNzZiNWY3YTg4NGZiNzgyY2ZjYzU4OGM5NWM0ZTM4M2RjNTI3ZDFiODQifX19";
			}

			@Override
			public String getID() {
				return "[I;1519558527,443829541,-2045951745,1395314622]";
			}
		});
		skulls.put("discord", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3M2MxMmJmZmI1MjUxYTBiODhkNWFlNzVjNzI0N2NiMzlhNzVmZjFhODFjYmU0YzhhMzliMzExZGRlZGEifX19";
			}

			@Override
			public String getID() {
				return "[I;-566027055,-1373812234,-1824937619,-1364409557]";
			}
		});
		skulls.put("chest", new CustomSkull() {

			@Override
			public String getValue() {
				return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19";
			}

			@Override
			public String getID() {
				return "[I;866667617,641484425,-1507050624,1155661645]";
			}
		});
	}

	public static ItemStack getSkull(String skull) {
		return skulls.containsKey(skull) ? CoreUtils.getHead(skulls.get(skull).getID(), skulls.get(skull).getValue())
				: new ItemStack(Material.SKELETON_SKULL);
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
