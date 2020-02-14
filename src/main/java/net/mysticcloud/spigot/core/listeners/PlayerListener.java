package net.mysticcloud.spigot.core.listeners;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.mysticcloud.spigot.core.Main;
import net.mysticcloud.spigot.core.kits.Kit;
import net.mysticcloud.spigot.core.kits.KitManager;
import net.mysticcloud.spigot.core.utils.CoreUtils;
import net.mysticcloud.spigot.core.utils.FoodInfo;
import net.mysticcloud.spigot.core.utils.GUIManager;
import net.mysticcloud.spigot.core.utils.SpawnReason;
import net.mysticcloud.spigot.core.utils.particles.ParticleFormatEnum;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.Pet;
import net.mysticcloud.spigot.core.utils.pets.v1_15_R1.PetManager;

public class PlayerListener implements Listener {

	public PlayerListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().toUpperCase().startsWith("!PARTICLES") && e.getPlayer().hasPermission("mysticcloud.admin")) {
			String[] args = e.getMessage().split(" ");
			if (args.length == 3) {

				CoreUtils.particles.put(e.getPlayer().getUniqueId(), ParticleFormatEnum.valueOf(args[1].toUpperCase()));
				CoreUtils.particles(e.getPlayer().getUniqueId()).formatter()
						.particle(Particle.valueOf(args[2].toUpperCase()));

			}
		}
	}

	@EventHandler
	public void onSignEdit(SignChangeEvent e) {
		for (int i = 0; i != e.getLines().length; i++) {
			e.setLine(i, CoreUtils.colorize(e.getLine(i)));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		CoreUtils.playerparticles.put(e.getPlayer().getUniqueId(), false);
		if (PetManager.hasPet(e.getPlayer())) {
			PetManager.removePets(e.getPlayer());
		}
	}

//	@EventHandler
//	public void onPlayerInteract(PlayerInteractEvent e) {
//		if(e.getAction().equals(Action.LEFT_CLICK_AIR) && e.getPlayer().isSneaking()) {
//			CoreUtils.particletest = CoreUtils.particletest > Particle.values().length-1 ? 1 : CoreUtils.particletest+1;
//		}
//	}
//	

	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
				e.setCancelled(true);
				CoreUtils.teleportToSpawn((Player) e.getEntity(), SpawnReason.DEATH);
				((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getMaxHealth());
				((Player) e.getEntity()).setFoodLevel(20);

			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		CoreUtils.updateDate();

		for (Entry<UUID, String> entry : CoreUtils.offlineTimedUsers.entrySet()) {

			if (!entry.getKey().equals(e.getPlayer().getUniqueId()))
				continue;

			CoreUtils.removeTimedPermission(e.getPlayer(), entry.getValue());

			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					CoreUtils.offlineTimedUsers.remove(entry.getKey());
				}

			}, 20);
			break;
		}

		Player player = e.getPlayer();

		player.setPlayerListHeader(CoreUtils.colorize(CoreUtils.playerList("header")));
		String phs = CoreUtils.playerList("name");
		if (phs.contains("%prefix%")) {
			phs = phs.replaceAll("%prefix%", CoreUtils.getPlayerPrefix(player));
		}
		if (phs.contains("%player%")) {
			phs = phs.replaceAll("%player%", player.getName());
		}

		player.setPlayerListName(CoreUtils.colorize(phs));

		player.setPlayerListFooter(CoreUtils.colorize(CoreUtils.playerList("footer")));

		CoreUtils.playerparticles.put(e.getPlayer().getUniqueId(), true);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && CoreUtils.debugOn()) {
			CoreUtils.testblock = e.getClickedBlock();
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if (e.getItem() == null)
				return;
			if (!e.getItem().hasItemMeta())
				return;
			if (CoreUtils.isFood(e.getItem())) {
				CoreUtils.debug("This is food.");

				if (e.getPlayer().getFoodLevel() < 20 && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
					FoodInfo info = CoreUtils.getFood(e.getItem());
					try {
						e.getPlayer().setHealth(e.getPlayer().getHealth() + info.getHealingFactor());
					} catch (IllegalArgumentException ex) {
						e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
					}
					e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + info.getHungerLevel());
					if (info.getPotionEffects().size() != 0) {
						for (PotionEffect p1 : info.getPotionEffects()) {
							e.getPlayer().addPotionEffect(p1);
						}
					}
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
					if (e.getItem().getAmount() > 1)
						e.getItem().setAmount(e.getItem().getAmount() - 1);
					else {
						e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
					}
				}
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		try {
			GUIManager.closeInventory((Player) e.getPlayer());
		} catch (Exception ex) {
			// this is stupid.
			CoreUtils.debug("Inventories probably didn't update.");
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getSlot() == -999)
			return;
		if (e.getClickedInventory() == null)
			return;
		if (e.getInventory().getHolder() == null)
			e.setCancelled(true);
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particles") {
			if (CoreUtils.particles(e.getWhoClicked().getUniqueId()) == null)
				return;
			for (Particle particle : CoreUtils.particles(e.getWhoClicked().getUniqueId()).formatter()
					.allowedParticles()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase(CoreUtils.colorize(CoreUtils.particlesToString(particle)))) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId()).formatter().particle(particle);
					e.getWhoClicked().closeInventory();
					return;
				}
			}

		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Particle Format") {
			e.setCancelled(true);
			for (ParticleFormatEnum format : ParticleFormatEnum.values()) {
				if (e.getCurrentItem().getType().equals(format.formatter().item().getType())) {
					CoreUtils.particles(e.getWhoClicked().getUniqueId(), format);
					if (format.formatter().changeParticle())
						GUIManager.switchInventory(((Player) e.getWhoClicked()),
								GUIManager.generateParticleMenu(((Player) e.getWhoClicked()), format.formatter()),
								"Particles");
					else {
						e.getWhoClicked().closeInventory();
					}
					return;
				}
			}
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Kits") {
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.AIR))
				return;
			if (!e.getCurrentItem().hasItemMeta())
				return;
			for (Kit kit : KitManager.getKits()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals(kit.getDisplayName())) {
					KitManager.applyKit(((Player) e.getWhoClicked()), kit.getName());
				}
			}

			e.getWhoClicked().closeInventory();
		}
		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Settings") {
			if (e.getCurrentItem().getType().equals(Material.DIAMOND)) {
				CoreUtils.toggleParticles((Player) e.getWhoClicked());
				e.getWhoClicked().closeInventory();
			}
		}

		if (GUIManager.getOpenInventory(((Player) e.getWhoClicked())) == "Pets") {
			e.setCancelled(true);
			if (e.getCurrentItem() == null)
				return;
			if (PetManager.getPetType(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) == null)
				return;
			Pet p = PetManager.spawnPet(((Player) e.getWhoClicked()),
					ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()),
					((Player) e.getWhoClicked()).getLocation());
			e.getWhoClicked().closeInventory();
			((ArmorStand) p).setVisible(false);
		}
	}

}