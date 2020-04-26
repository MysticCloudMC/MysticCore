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

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormat;

public class GemsFormat extends ParticleFormat {
	
	List<Material> gems = new ArrayList<>();
	Location loc = null;

	public GemsFormat() {
		changeParticle = false;
		
		gems.add(Material.DIAMOND);
		gems.add(Material.GOLD_INGOT);
		gems.add(Material.EMERALD);
		
		guiItem = new ItemStack(Material.EMERALD);
		name = "&bGems";


	}

	@Override
	public void display(UUID uid, int i) {
		super.display(uid, i);
		loc = Bukkit.getPlayer(uid).getLocation().add(-0.75 + (CoreUtils.getRandom().nextDouble()*1.5),
						(1.5 + CoreUtils.getRandom().nextDouble())
								- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
						-0.75 + (CoreUtils.getRandom().nextDouble()*1.5));
		ItemStack itemstack = new ItemStack(gems.get(CoreUtils.getRandom().nextInt(gems.size())));
		Item item = loc.getWorld().dropItem(loc, itemstack);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setPortalCooldown(Integer.MAX_VALUE);
		new BukkitRunnable(){
			
			@Override
			public void run(){
				item.remove();
			}
		}.runTaskLater(Main.getPlugin(), 20);
		
		
		
	}

}
