package net.mysticcloud.spigot.core.utils.particles.formats;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class GemsFormat extends ParticleFormat {

	List<Material> gems = new ArrayList<>();
	Location cloc = null;

	public GemsFormat() {
		changeParticle = false;

		gems.add(Material.DIAMOND);
		gems.add(Material.GOLD_INGOT);
		gems.add(Material.EMERALD);
		gems.add(Material.IRON_INGOT);
		gems.add(Material.GOLD_NUGGET);
		gems.add(Material.IRON_NUGGET);

		guiItem = new ItemStack(Material.EMERALD);
		name = "&bGems";

	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getLocation());
		}
	}

	@Override
	public void display(Location loc) {
		super.display(loc);
		cloc = loc.add(
				-(getOptions().getDouble("l") / 2) + (CoreUtils.getRandom().nextDouble() * getOptions().getDouble("l")),
				(1.5 + CoreUtils.getRandom().nextDouble())
						- (CoreUtils.getRandom().nextInt((int) getOptions().getDouble("h"))
								+ CoreUtils.getRandom().nextDouble()),
				-(getOptions().getDouble("l") / 2)
						+ (CoreUtils.getRandom().nextDouble() * getOptions().getDouble("l")));
		ItemStack itemstack = new ItemStack(gems.get(CoreUtils.getRandom().nextInt(gems.size())));
		Item item = cloc.getWorld().dropItem(cloc, itemstack);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setPortalCooldown(Integer.MAX_VALUE);

		new BukkitRunnable() {

			@Override
			public void run() {
				item.remove();
			}
		}.runTaskLater(CoreUtils.getPlugin(), 13);

	}

}
