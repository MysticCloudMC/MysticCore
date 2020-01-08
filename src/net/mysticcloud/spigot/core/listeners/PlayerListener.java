package net.mysticcloud.spigot.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.Kit;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.SpawnReason;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.Pet;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.PetManager;

public class PlayerListener implements Listener {

	public PlayerListener(Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onSignEdit(SignChangeEvent e) {
		for(int i=0; i!=e.getLines().length;i++) {
			e.setLine(i, CoreUtils.colorize(e.getLine(i)));
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		CoreUtils.playerparticles.put(e.getPlayer().getUniqueId(), false);
		if(PetManager.hasPet(e.getPlayer())) {
			PetManager.removePets(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking()) {
			CoreUtils.particletest = CoreUtils.particletest > Particle.values().length ? 1 : CoreUtils.particletest+1;
		}
	}
//	
	
	
	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(((Player)e.getEntity()).getHealth() - e.getDamage() <= 0) {
				e.setCancelled(true);
				CoreUtils.teleportToSpawn((Player)e.getEntity(), SpawnReason.DEATH);
				((Player)e.getEntity()).setHealth(((Player)e.getEntity()).getMaxHealth());
				
			}
		}
	}

	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		CoreUtils.particles.put(e.getPlayer().getUniqueId(), ParticleFormatEnum.CIRCLE_HEAD);
		
		
		e.getPlayer().setPlayerListName(CoreUtils.colorize(CoreUtils.getPlayerPrefix(e.getPlayer()) + e.getPlayer().getName()));
		CoreUtils.playerparticles.put(e.getPlayer().getUniqueId(), true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if(e.getSlot() == -999) return;
		if(e.getClickedInventory() == null) return;
		if(e.getWhoClicked().hasMetadata("kitinv")){
			e.setCancelled(true);
			if(e.getCurrentItem().getType().equals(Material.AIR)) return;
			if(!e.getCurrentItem().hasItemMeta()) return;
			for(Kit kit : KitManager.getKits()){
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(kit.getDisplayName())){
					KitManager.applyKit(((Player)e.getWhoClicked()), kit.getName());
				}
			}
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().removeMetadata("kitinv", Main.getPlugin());
		}
		if(e.getInventory().getHolder() == null) {
			
			if(e.getCurrentItem().getType().equals(Material.DIAMOND)) {
				CoreUtils.toggleParticles((Player)e.getWhoClicked());
			}
			e.setCancelled(true);
		}
		if (e.getWhoClicked().hasMetadata("petinv")) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null)
				return;
			if (PetManager.getPetType(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) == null)
				return;
			Pet p = PetManager.spawnPet(((Player) e.getWhoClicked()),
					ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()),
					((Player) e.getWhoClicked()).getLocation());
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().removeMetadata("petinv", Main.getPlugin());
			((ArmorStand)p).setVisible(false);
		}
	}
	
	

	

}
