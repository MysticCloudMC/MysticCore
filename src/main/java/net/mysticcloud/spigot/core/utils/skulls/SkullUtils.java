package net.mysticcloud.spigot.core.utils.skulls;

import java.util.HashMap;
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
				return "[I;854160726,1555254121,-1159745984,75904377]";
			}
		});
	}

	public static ItemStack getSkull(String skull) {
		return skulls.containsKey(skull) ? CoreUtils.getHead(skulls.get(skull).getID(), skulls.get(skull).getValue())
				: new ItemStack(Material.SKELETON_SKULL);
	}

}
